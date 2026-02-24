package service.impl;

import bd.ConnectorBD;
import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;
import dto.medico.MedicoTokenVerificacionResponse;
import exception.BadRequestException;
import exception.ConflictException;
import model.Medico;
import service.interfaces.IMedicoService;
import util.JavaMail;
import util.PasswordGenerator;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MedicoServiceImpl implements IMedicoService {
    @Override
    public List<MedicoResponse> listar(int pagina, int tamanioPagina) {
        return List.of();
    }

    @Override
    public MedicoResponse buscarPorId(int idMedico) {
        return null;
    }

    @Override
    public MedicoResponse registrarMedico(MedicoRequest medico) {
        String query = "CALL registrar_medico(?,?,?,?,?,?,?,?,?)";
        String passwordPlano = PasswordGenerator.generarPasswordPlano(10);
        String passwordHash = PasswordGenerator.generarHash(passwordPlano);

        String tokenVerificacion = UUID.randomUUID().toString();
        Date date = new Date();
        Date tokenExpiracion = new Date(date.getTime() + (24 * 60 * 60 * 1000));
        String link = "http://localhost:8080/ClinicaProyect/App/verificacion-email/activar?token=" + tokenVerificacion;

        try (CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)) {

            cs.setString(1, medico.nombres());
            cs.setString(2, medico.apellidos());
            cs.setString(3, medico.correo());
            cs.setString(4, medico.numeroColegiatura());
            cs.setString(5, medico.telefono());
            cs.setInt(6, medico.idEspecialidad());
            cs.setString(7, passwordHash);
            cs.setString(8,tokenVerificacion);
            cs.setTimestamp(9, new java.sql.Timestamp(tokenExpiracion.getTime()));
            boolean hasResult = cs.execute();

            if (hasResult) {
                ResultSet rs = cs.getResultSet();

                if (rs.next()) {
                    MedicoResponse medicoDto = MedicoResponse.builder()
                            .idMedico(rs.getInt("id_medico"))
                            .nombres(rs.getString("nombres"))
                            .apellidos(rs.getString("apellidos"))
                            .correo(rs.getString("correo"))
                            .numeroColegiatura(rs.getString("numero_colegiatura"))
                            .telefono(rs.getString("telefono"))
                            .idEspecialidad(rs.getInt("id_especialidad"))
                            .nombreEspecialidad(rs.getString("nombre"))
                            .fechaRegistro(rs.getDate("fecha_registro"))
                            .build();
                    try {
                        JavaMail.enviarCorreo(medico.correo(),medico.nombres(),medico.apellidos(), passwordPlano, link);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return medicoDto;
                }
            }

            throw new ConflictException("No se pudo registrar el médico");

        } catch (SQLException e) {
            throw new ConflictException("Error registrando medico: " + e);
        }
    }

    @Override
    public MedicoTokenVerificacionResponse buscarPorToken(String token) {
        String sql = "SELECT id_medico, token_verificacion, token_expiracion FROM tb_medico WHERE token_verificacion = ?";
        try (PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)) {

            ps.setString(1, token);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return MedicoTokenVerificacionResponse.builder()
                        .idMedico(rs.getInt("id_medico"))
                        .tokenVerificacion(rs.getString("token_verificacion"))
                        .tokenExpiracion(
                                rs.getTimestamp("token_expiracion").toLocalDateTime()
                        )
                        .build();
            }

            return null;

        } catch (SQLException e) {
            throw new BadRequestException("Error buscando por token" + e);
        }
    }

    @Override
    public void activarCuentaToken(String token) {
        String query = "{CALL activar_medico(?, ?)}";
        try (CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)) {

            cs.setString(1, token);
            cs.registerOutParameter(2, java.sql.Types.BOOLEAN);

            cs.execute();

            boolean activado = cs.getBoolean(2);

            if (activado) {
                System.out.println("El medico fue activado correctamente");
            } else {
                System.out.println("El medico no se activo");
            }

        } catch (SQLException e) {
            throw new BadRequestException("Error buscando por token: " + e);
        }
    }

    @Override
    public MedicoResponse actualizarMedico(int id, MedicoRequest medico) {
        return null;
    }

    @Override
    public void eliminar(int idMedico) {

    }
}

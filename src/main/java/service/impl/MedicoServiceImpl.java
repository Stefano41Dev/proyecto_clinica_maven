package service.impl;

import bd.ConnectorBD;
import dto.especialidad.EspecialidadResponse;
import dto.medico.*;
import dto.paginacion.PageResponse;
import exception.BadRequestException;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import javassist.NotFoundException;
import service.interfaces.IMedicoService;
import util.JavaMail;
import util.PasswordGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MedicoServiceImpl implements IMedicoService {
    @Override
    public PageResponse<MedicoResponse> listar(int pagina, int tamanioPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<MedicoResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;

        String sqlData = "CALL listar_medico_paginacion(?,?)";

        String sqlCount = "SELECT COUNT(*) FROM tb_medico WHERE activo = 1";

        try (Connection conn = ConnectorBD.getConexion()) {

            try (PreparedStatement psCount = conn.prepareStatement(sqlCount);
                 ResultSet rsCount = psCount.executeQuery()) {

                if (rsCount.next()) {
                    totalRegistros = rsCount.getLong(1);
                }
            }


            try (CallableStatement csData = conn.prepareCall(sqlData)) {

                csData.setInt(1, tamanioPagina);
                csData.setInt(2, offset);

                try (ResultSet rs = csData.executeQuery()) {

                    while (rs.next()) {
                        lista.add(MedicoResponse.builder()
                                .idMedico(rs.getInt("id_medico"))
                                .nombres(rs.getString("nombres"))
                                .apellidos(rs.getString("apellidos"))
                                .correo(rs.getString("correo"))
                                .numeroColegiatura(rs.getString("numero_colegiatura"))
                                .telefono(rs.getString("telefono"))
                                .especialidadResponse(EspecialidadResponse.builder()
                                        .idEspecialidad(rs.getInt("id_especialidad"))
                                        .nombre(rs.getString("nombre"))
                                        .build())
                                .fechaRegistro(rs.getDate("fecha_registro"))
                                .build());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar médicos paginados", e);
        }

        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }

    @Override
    public MedicoResponse buscarPorId(int idMedico) {
        String query = "CALL buscar_medico_id(?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)){
            cs.setInt(1,idMedico);
            boolean hasResult = cs.execute();
            if(hasResult){
                ResultSet rs = cs.getResultSet();

                if(rs.next()){
                    return MedicoResponse.builder()
                            .idMedico(rs.getInt("id_medico"))
                            .nombres(rs.getString("nombres"))
                            .apellidos(rs.getString("apellidos"))
                            .correo(rs.getString("correo"))
                            .numeroColegiatura(rs.getString("numero_colegiatura"))
                            .telefono(rs.getString("telefono"))
                            .especialidadResponse(EspecialidadResponse.builder()
                                    .idEspecialidad(rs.getInt("id_especialidad"))
                                    .nombre(rs.getString("nombre"))
                                    .build())
                            .fechaRegistro(rs.getDate("fecha_registro"))
                            .build();
                }else{
                    throw new ResourceNotFoundException("No se encontro el medico con id " + idMedico);
                }
            }

            throw new ConflictException("No se pudo registrar el médico");

        }catch (SQLException e){
            throw new RuntimeException("SQL EXCEPTION ", e);
        }

    }

    @Override
    public String registrarMedico(MedicoRequest medico) {
        String query = "CALL registrar_medico(?,?,?,?,?,?,?,?,?)";
        //String passwordPlano = PasswordGenerator.generarPasswordPlano(10);
        String passwordHash = PasswordGenerator.generarHash(medico.password());

        String tokenVerificacion = UUID.randomUUID().toString();
        Date date = new Date();
        Date tokenExpiracion = new Date(date.getTime() + (24 * 60 * 60 * 1000));
        String link = "http://localhost:8080/ClinicaProyect/App/verificacion-email/activar-medico?token=" + tokenVerificacion;

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
                    return link;
                }
            }

            throw new ConflictException("No se pudo registrar el médico");

        } catch (SQLException e) {
            throw new RuntimeException("SQL EXCEPTION: " + e);
        }
    }

    @Override
    public MedicoResponse registrarMedicoVerificacionGmail(MedicoRequestGmail medico) {
        String query = "CALL registrar_medico(?,?,?,?,?,?,?,?,?)";
        String passwordPlano = PasswordGenerator.generarPasswordPlano(10);
        String passwordHash = PasswordGenerator.generarHash(passwordPlano);

        String tokenVerificacion = UUID.randomUUID().toString();
        Date date = new Date();
        Date tokenExpiracion = new Date(date.getTime() + (24 * 60 * 60 * 1000));
        String link = "http://localhost:8080/ClinicaProyect/App/verificacion-email/activar-medico?token=" + tokenVerificacion;

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
                            .especialidadResponse(EspecialidadResponse.builder()
                                    .idEspecialidad(rs.getInt("id_especialidad"))
                                    .nombre(rs.getString("nombre"))
                                    .build())
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
            throw new RuntimeException("Error registrando medico: " + e);
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
        String query = "CALL actualizar_medico_id(?,?,?,?,?,?,?,?)";
        String passwordHash = PasswordGenerator.generarHash(medico.password());
        try (CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)){
            cs.setInt(1,id);
            cs.setString(2,medico.nombres());
            cs.setString(3, medico.apellidos());
            cs.setString(4, medico.numeroColegiatura());
            cs.setString(5,medico.telefono());
            cs.setInt(6,medico.idEspecialidad());
            cs.setString(7,medico.correo());
            cs.setString(8,passwordHash);

            boolean hasResult = cs.execute();

            if (hasResult) {
                ResultSet rs = cs.getResultSet();
                if(rs.next()){
                    return MedicoResponse.builder()
                            .idMedico(rs.getInt("id_medico"))
                            .nombres(rs.getString("nombres"))
                            .apellidos(rs.getString("apellidos"))
                            .correo(rs.getString("correo"))
                            .numeroColegiatura(rs.getString("numero_colegiatura"))
                            .telefono(rs.getString("telefono"))
                            .especialidadResponse(EspecialidadResponse.builder()
                                    .idEspecialidad(rs.getInt("id_especialidad"))
                                    .nombre(rs.getString("nombre"))
                                    .build())
                            .fechaRegistro(rs.getDate("fecha_registro"))
                            .build();
                }else{
                    throw new ResourceNotFoundException("No se encontro el medico con id " + id);
                }
            }

            throw new ConflictException("No se pudo registrar el médico");

        }catch (SQLException e){
            throw new RuntimeException("SQLEXCEPTION: No se pudo realizar la actualizacion de datos: ",e);
        }
    }

    @Override
    public String eliminar(int idMedico) {
        String sql = "CALL eliminar_medico_por_id(?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)){
            cs.setInt(1,idMedico);
            boolean hasResult = cs.execute();

            if (hasResult) {
                ResultSet rs = cs.getResultSet();
                if(rs.next()){
                    return rs.getString("mensaje");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("SQL Exception", e);
        }

        throw new ResourceNotFoundException("No se encontro el medico con id " + idMedico);
    }
}

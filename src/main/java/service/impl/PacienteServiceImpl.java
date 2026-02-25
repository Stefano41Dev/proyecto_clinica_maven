package service.impl;

import bd.ConnectorBD;
import dto.medico.MedicoTokenVerificacionResponse;
import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;
import dto.paciente.PacienteTokenVerificacionResponse;
import exception.BadRequestException;
import exception.ConflictException;
import service.interfaces.IPacienteService;
import util.PasswordGenerator;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class PacienteServiceImpl implements IPacienteService {
    @Override
    public List<PacienteResponse> listaPacientes(int pagina, int tamanioPagina) {
        return List.of();
    }

    @Override
    public PacienteResponse buscarPorId(int idPaciente) {
        return null;
    }

    @Override
    public String registrarPaciente(PacienteRequest request) {
        String query = "CALL registrar_paciente(?,?,?,?,?,?,?,?,?,?,?)";
        String passwordHash = PasswordGenerator.generarHash(request.password());
        String tokenVerificacion = UUID.randomUUID().toString();
        java.util.Date date = new java.util.Date();
        java.util.Date tokenExpiracion = new java.util.Date(date.getTime() + (24 * 60 * 60 * 1000));
        String link = "http://localhost:8080/ClinicaProyect/App/verificacion-email/activar-paciente?token=" + tokenVerificacion;

        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)){
            cs.setString(1,request.nombres());
            cs.setString(2,request.apellidos());
            cs.setString(3, request.correo());
            cs.setString(4, passwordHash);
            cs.setInt(5, request.idTipoDocumento());
            cs.setString(6,request.numeroDocumento());
            cs.setDate(7, (Date) request.fechaNacimiento());
            cs.setInt(8,request.idSexo());
            cs.setInt(9,request.idEstadoCivil());
            cs.setString(10,tokenVerificacion);
            cs.setTimestamp(9, new java.sql.Timestamp(tokenExpiracion.getTime()));

            boolean hasResult = cs.execute();

            if (hasResult) {
                ResultSet rs = cs.getResultSet();
                if (rs.next()) {
                    return link;
                }
            }
            throw new ConflictException("No se pudo registrar el paciente");

        }catch (SQLException e){
            throw new RuntimeException("SQLException : ", e);
        }
    }

    @Override
    public PacienteTokenVerificacionResponse buscarPorToken(String token) {
        String sql = "SELECT id_paciente,token_verificacion, token_expiracion FROM tb_paciente WHERE token_verificacion = ?";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setString(1,token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return PacienteTokenVerificacionResponse.builder()
                        .idPaciente(rs.getInt("id_paciente"))
                        .tokenVerificacion(rs.getString("token_verificacion"))
                        .tokenExpiracion(
                                rs.getTimestamp("token_expiracion").toLocalDateTime()
                        )
                        .build();
            }
        }catch (SQLException e){
            throw new RuntimeException("SQLException ",e);
        }
        return null;
    }

    @Override
    public void activarCuenta(String token) {
        String query = "CALL activar_paciente(?)";
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
    public PacienteResponse actualizarPaciente(int idPaciente, PacienteRequest request) {
        return null;
    }

    @Override
    public void eliminar(int idPaciente) {

    }
}

package service.impl;

import bd.ConnectorBD;
import dto.estado_cita.EstadoCitaResponse;
import dto.medico.MedicoResponse;
import dto.medico.MedicoTokenVerificacionResponse;
import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;
import dto.paciente.PacienteTokenVerificacionResponse;
import dto.paginacion.PageResponse;
import dto.tipo_documento.TipoDocumentoResponse;
import dto.tipo_sexo.TipoSexoResponse;
import exception.BadRequestException;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import javassist.NotFoundException;
import service.interfaces.IPacienteService;
import util.PasswordGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacienteServiceImpl implements IPacienteService {
    @Override
    public PageResponse<PacienteResponse> listaPacientes(int pagina, int tamanioPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<PacienteResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;

        String sqlData = "CALL listar_paciente_paginado(?,?)";
        String sqlCount = "SELECT COUNT(*) FROM tb_paciente WHERE activo = 1";

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
                        lista.add(
                                PacienteResponse.builder()
                                        .idPaciente(rs.getInt("id_paciente"))
                                        .nombres(rs.getString("nombres"))
                                        .apellidos(rs.getString("apellidos"))
                                        .correo(rs.getString("correo"))
                                        .tipoDocumentoResponse(new TipoDocumentoResponse(
                                                rs.getInt("id_tipo_documento"),
                                                rs.getString("nombre_documento")))
                                        .numeroDocumento(rs.getString("numero_documento"))
                                        .fechaNacimiento(rs.getDate("fecha_nacimiento"))
                                        .fechaRegistro(rs.getDate("fecha_registro"))
                                        .tipoSexoResponse(new TipoSexoResponse(
                                                rs.getInt("id_sexo"),
                                                rs.getString("sexo")))
                                        .estadoCitaResponse(new EstadoCitaResponse(
                                                rs.getInt("id_estado_civil"),
                                                rs.getString("nombre_estado")))
                                        .build()
                        );

                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pacientes paginados", e);
        }
        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }

    @Override
    public PacienteResponse buscarPorId(int idPaciente) {
        String query = "CALL buscar_paciente_id(?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)){
            cs.setInt(1,idPaciente);
            cs.executeQuery();
            ResultSet rs = cs.getResultSet();
            if(rs.next()){
                return PacienteResponse.builder()
                        .idPaciente(rs.getInt("id_paciente"))
                        .nombres(rs.getString("nombres"))
                        .apellidos(rs.getString("apellidos"))
                        .correo(rs.getString("correo"))
                        .tipoDocumentoResponse(new TipoDocumentoResponse(
                                rs.getInt("id_tipo_documento"),
                                rs.getString("nombre_documento")))
                        .numeroDocumento(rs.getString("numero_documento"))
                        .fechaNacimiento(rs.getDate("fecha_nacimiento"))
                        .fechaRegistro(rs.getDate("fecha_registro"))
                        .tipoSexoResponse(new TipoSexoResponse(
                                rs.getInt("id_sexo"),
                                rs.getString("sexo")))
                        .estadoCitaResponse(new EstadoCitaResponse(
                                rs.getInt("id_estado_civil"),
                                rs.getString("nombre_estado")))
                        .build();
            }else{
                throw new ResourceNotFoundException("No se encontro el paciente con id " + idPaciente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQLEXCEPTION: ",e);
        }

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
            cs.setDate(7, new java.sql.Date(request.fechaNacimiento().getTime()));            cs.setInt(8,request.idSexo());
            cs.setInt(9,request.idEstadoCivil());
            cs.setString(10,tokenVerificacion);
            cs.setTimestamp(11, new java.sql.Timestamp(tokenExpiracion.getTime()));

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
        String query = "CALL activar_paciente(?,?)";
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
        String sql = "CALL actualizar_paciente_id(?,?,?,?,?,?,?,?,?,?)";
        String passwordHash = PasswordGenerator.generarHash(request.password());
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)){
            cs.setInt(1,idPaciente);
            cs.setString(2,request.nombres());
            cs.setString(3,request.apellidos());
            cs.setString(4, request.correo());
            cs.setString(5, passwordHash);
            cs.setInt(6, request.idTipoDocumento());
            cs.setString(7,request.numeroDocumento());
            cs.setDate(8, new java.sql.Date(request.fechaNacimiento().getTime()));
            cs.setInt(9,request.idSexo());
            cs.setInt(10,request.idEstadoCivil());

            cs.executeQuery();
            ResultSet rs = cs.getResultSet();
            if(rs.next()){
                return PacienteResponse.builder()
                        .idPaciente(rs.getInt("id_paciente"))
                        .nombres(rs.getString("nombres"))
                        .apellidos(rs.getString("apellidos"))
                        .correo(rs.getString("correo"))
                        .tipoDocumentoResponse(new TipoDocumentoResponse(
                                rs.getInt("id_tipo_documento"),
                                rs.getString("nombre_documento")))
                        .numeroDocumento(rs.getString("numero_documento"))
                        .fechaNacimiento(rs.getDate("fecha_nacimiento"))
                        .fechaRegistro(rs.getDate("fecha_registro"))
                        .tipoSexoResponse(new TipoSexoResponse(
                                rs.getInt("id_sexo"),
                                rs.getString("sexo")))
                        .estadoCitaResponse(new EstadoCitaResponse(
                                rs.getInt("id_estado_civil"),
                                rs.getString("nombre_estado")))
                        .build();
            }
            throw new ResourceNotFoundException("No se encontro el paciente con id " + idPaciente);
        }catch (SQLException e){
            throw new RuntimeException("SQL EXCEPTION : ",e);
        }

    }

    @Override
    public String eliminar(int idPaciente) {
        String query = "CALL eliminar_paciente_por_id(?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(query)){
            cs.setInt(1,idPaciente);
            boolean hasResult = cs.execute();

            if (hasResult) {
                ResultSet rs = cs.getResultSet();
                if(rs.next()){
                    return rs.getString("mensaje");
                }
            }

            throw new ResourceNotFoundException("No se encontro el paciente con id " + idPaciente);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

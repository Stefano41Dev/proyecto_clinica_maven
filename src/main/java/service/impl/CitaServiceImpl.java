package service.impl;

import bd.ConnectorBD;
import dto.cita.*;
import dto.especialidad.EspecialidadResponse;
import dto.estado_cita.EstadoCitaResponse;
import dto.estado_civil.EstadoCivilResponse;
import dto.medico.MedicoResponse;
import dto.paciente.PacienteResponse;
import dto.paginacion.PageResponse;
import dto.tipo_documento.TipoDocumentoResponse;
import dto.tipo_sexo.TipoSexoResponse;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import service.interfaces.ICitaService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitaServiceImpl implements ICitaService {

    @Override
    public PageResponse<CitaListaResponse> listar(int pagina, int tamanioPagina) {

        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<CitaListaResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;
        String sqlData = "CALL listar_citas_paginado(?,?)";
        String sqlCount = "SELECT COUNT(*) FROM tb_cita WHERE activo = 1";

        try(Connection conn = ConnectorBD.getConexion()){
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
                                CitaListaResponse.builder()
                                        .idCita(rs.getInt("id_cita"))
                                        .fechaProgramada(rs.getDate("fecha_programada"))
                                        .hora(rs.getTime("hora"))
                                        .motivo(rs.getString("motivo"))

                                        .idPaciente(rs.getInt("id_paciente"))
                                        .nombresPaciente(rs.getString("paciente_nombres"))
                                        .apellidosPaciente(rs.getString("paciente_apellidos"))

                                        .idMedico(rs.getInt("id_medico"))
                                        .nombresMedico(rs.getString("medico_nombres"))
                                        .apellidosMedico(rs.getString("medico_apellidos"))

                                        .estadoCitaResponse(
                                                EstadoCitaResponse.builder()
                                                        .idEstadoCita(rs.getInt("id_estado_cita"))
                                                        .nombreEstado(rs.getString("nombre_estado"))
                                                        .build()
                                        )

                                        .build()
                        );
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("SQLEXCEPTION : ",e);
        }
        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }

    @Override
    public CitaDatosCompletosResponse buscarDatosCompletosCitaPorId(int idCita) {

        String sql = "CALL buscar_cita_datos_completos_id(?)";

        try (CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)) {

            cs.setInt(1, idCita);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {

                return CitaDatosCompletosResponse.builder()
                        .idCita(rs.getInt("id_cita"))

                        // =========================
                        // PACIENTE
                        // =========================
                        .pacienteResponse(
                                PacienteResponse.builder()
                                        .idPaciente(rs.getInt("id_paciente"))
                                        .numeroDocumento(rs.getString("numero_documento"))
                                        .fechaNacimiento(rs.getDate("fecha_nacimiento"))
                                        .fechaRegistro(rs.getDate("paciente_fecha_registro"))
                                        .correo(rs.getString("paciente_correo"))

                                        .nombres(rs.getString("paciente_nombres"))
                                        .apellidos(rs.getString("paciente_apellidos"))

                                        .tipoDocumentoResponse(
                                                TipoDocumentoResponse.builder()
                                                        .idTipoDocumento(rs.getInt("id_tipo_documento"))
                                                        .nombreDocumento(rs.getString("nombre_documento"))
                                                        .build()
                                        )

                                        .tipoSexoResponse(
                                                TipoSexoResponse.builder()
                                                        .idSexo(rs.getInt("id_sexo"))
                                                        .sexo(rs.getString("sexo"))
                                                        .build()
                                        )

                                        .estadoCivilResponse(
                                                EstadoCivilResponse.builder()
                                                        .idEstadoCivil(rs.getInt("id_estado_civil"))
                                                        .nombreEstado(rs.getString("estado_civil"))
                                                        .build()
                                        )

                                        .build()
                        )

                        // =========================
                        // MEDICO
                        // =========================
                        .medicoResponse(
                                MedicoResponse.builder()
                                        .idMedico(rs.getInt("id_medico"))
                                        .numeroColegiatura(rs.getString("numero_colegiatura"))
                                        .telefono(rs.getString("telefono"))
                                        .fechaRegistro(rs.getDate("medico_fecha_registro"))
                                        .correo(rs.getString("medico_correo"))

                                        .nombres(rs.getString("medico_nombres"))
                                        .apellidos(rs.getString("medico_apellidos"))

                                        .especialidadResponse(
                                                EspecialidadResponse.builder()
                                                        .idEspecialidad(rs.getInt("id_especialidad"))
                                                        .nombre(rs.getString("especialidad"))
                                                        .build()
                                        )
                                        .build()
                        )
                        .fechaProgramada(rs.getDate("fecha_programada"))
                        .hora(rs.getTime("hora"))
                        .motivo(rs.getString("motivo"))
                        .estadoCitaResponse(
                                EstadoCitaResponse.builder()
                                        .idEstadoCita(rs.getInt("id_estado_cita"))
                                        .nombreEstado(rs.getString("estado_cita"))
                                        .build()
                        )
                        .build();
            }

            throw new ResourceNotFoundException("No se encontro la cita con id " + idCita);
        } catch (SQLException e) {
            throw new RuntimeException("SQL EXCEPTION", e);
        }
    }

    @Override
    public boolean consultarEstadoCita(int idCita) {
        String sql = "SELECT id_estado_cita FROM tb_cita WHERE id_cita = ? AND activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCita);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int idEstado = rs.getInt("id_estado_cita");


                    return idEstado == 2;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar estado de cita", e);
        }

        return false;
    }

    @Override
    public CitaDatosCompletosResponse registrarCita(CitaRequest cita) {
        String sql = "CALL registrar_cita_validando(?,?,?,?,?,?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)){
            cs.setInt(1,cita.idPaciente());
            cs.setInt(2,cita.idMedico());
            cs.setDate(3,new java.sql.Date(cita.fechaProgramada().getTime()));
            cs.setTime(4, new java.sql.Time(cita.hora().getTime()));
            cs.setInt(5, cita.idEstadoCita());
            cs.setString(6, cita.motivo());


            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                int idGenerado = rs.getInt("id_cita");
                return buscarDatosCompletosCitaPorId(idGenerado);
            }
            throw new ConflictException("No se pudo registrar la cita");
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                throw new ConflictException(e.getMessage());
            }
            throw new RuntimeException(e);
        }

    }

    @Override
    public CitaResponse actualizarCita(int idCita, CitaUpdateRequest cita) {

        String sql = "CALL actualizar_cita_validando(?,?,?,?)";
       try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)){
            cs.setInt(1,idCita);
            cs.setInt(2, cita.idMedico());
           cs.setDate(3,new java.sql.Date(cita.fechaProgramada().getTime()));
           cs.setTime(4, new java.sql.Time(cita.hora().getTime()));

           ResultSet rs = cs.executeQuery();

           if (rs.next()) {
               int idCitaConsultar = rs.getInt("id_cita");
               return buscarDatosCita(idCitaConsultar);
           }
           throw new ConflictException("No se pudo registrar la cita");
       }catch (SQLException e){
           if ("45000".equals(e.getSQLState())) {
               throw new ConflictException(e.getMessage());
           }
           throw new RuntimeException("SQLEXCEPTION : ",e);
       }
    }

    @Override
    public CitaResponse buscarDatosCita(int idCita) {
        String sql = "SELECT * FROM tb_cita WHERE id_cita = ?";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setInt(1,idCita);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return CitaResponse.builder()
                        .idCita(rs.getInt("id_cita"))
                        .idPaciente(rs.getInt("id_paciente"))
                        .idMedico(rs.getInt("id_medico"))
                        .fechaProgramada(rs.getDate("fecha_programada"))
                        .hora(rs.getTime("hora"))
                        .idEstadoCita(rs.getInt("id_estado_cita"))
                        .motivo(rs.getString("motivo"))
                        .build();
            }
            throw new ResourceNotFoundException("No se encontro la cita con id " + idCita);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CitaResponse cambiarEstadoCita( CitaCambiarEstadoRequest citaCambiarEstadoRequest) {
        String sql = "CALL cambiar_estado_cita(?,?)";
        try(CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)) {
            cs.setInt(1, citaCambiarEstadoRequest.idCita());
            cs.setInt(2, citaCambiarEstadoRequest.idEstadoCita());
            cs.executeUpdate();
            return buscarDatosCita(citaCambiarEstadoRequest.idCita());


        }catch (SQLException e){
            if ("45000".equals(e.getSQLState())) {
                throw new ConflictException(e.getMessage());
            }
            throw new RuntimeException("SQLEXCEPTION : ",e);
        }


    }

    @Override
    public String eliminar(int idCita) {
        String sql = "UPDATE tb_cita " +
                "SET activo = 0 WHERE id_cita = ?";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setInt(1,idCita);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                return "Cita eliminada correctamente";
            } else {
                return "No existe una cita con ese ID";
            }
        }catch (SQLException e){
            throw new RuntimeException("SQLEXCEPTION : ",e);
        }
    }

    @Override
    public PageResponse<CitaListaResponse> listarPaginacionPorEstadoCitaYFecha(int pagina, int tamanioPagina, Integer idEstadoCita, Date fecha) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<CitaListaResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;

        String sqlData = "CALL listar_citas_paginado_filtro(?,?,?,?)";

        String sqlCount = """
        SELECT COUNT(*)
        FROM tb_cita c
        WHERE c.activo = 1
          AND (? IS NULL OR c.id_estado_cita = ?)
          AND (? IS NULL OR c.fecha_programada = ?)
    """;

        try (Connection conn = ConnectorBD.getConexion()) {


            try (PreparedStatement psCount = conn.prepareStatement(sqlCount)) {

                psCount.setObject(1, idEstadoCita);
                psCount.setObject(2, idEstadoCita);
                psCount.setDate(3, fecha != null ? new java.sql.Date(fecha.getTime()) : null);
                psCount.setDate(4, fecha != null ? new java.sql.Date(fecha.getTime()) : null);

                try (ResultSet rsCount = psCount.executeQuery()) {
                    if (rsCount.next()) {
                        totalRegistros = rsCount.getLong(1);
                    }
                }
            }


            try (CallableStatement cs = conn.prepareCall(sqlData)) {

                cs.setInt(1, tamanioPagina);
                cs.setInt(2, offset);

                if (idEstadoCita != null) {
                    cs.setInt(3, idEstadoCita);
                } else {
                    cs.setNull(3, Types.INTEGER);
                }

                if (fecha != null) {
                    cs.setDate(4, new java.sql.Date(fecha.getTime()));
                } else {
                    cs.setNull(4, Types.DATE);
                }

                try (ResultSet rs = cs.executeQuery()) {

                    while (rs.next()) {
                        lista.add(
                                CitaListaResponse.builder()
                                        .idCita(rs.getInt("id_cita"))
                                        .fechaProgramada(rs.getDate("fecha_programada"))
                                        .hora(rs.getTime("hora"))
                                        .motivo(rs.getString("motivo"))

                                        .idPaciente(rs.getInt("id_paciente"))
                                        .nombresPaciente(rs.getString("paciente_nombres"))
                                        .apellidosPaciente(rs.getString("paciente_apellidos"))

                                        .idMedico(rs.getInt("id_medico"))
                                        .nombresMedico(rs.getString("medico_nombres"))
                                        .apellidosMedico(rs.getString("medico_apellidos"))

                                        .estadoCitaResponse(
                                                EstadoCitaResponse.builder()
                                                        .idEstadoCita(rs.getInt("id_estado_cita"))
                                                        .nombreEstado(rs.getString("nombre_estado"))
                                                        .build()
                                        )
                                        .build()
                        );
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQLEXCEPTION: ", e);
        }

        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }

    @Override
    public PageResponse<CitaListaResponse> buscarCitasPorCorreo(String correo, int pagina, int tamanioPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<CitaListaResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;

        String sql = "{CALL listar_citas_por_correo_paginado(?, ?, ?)}";

        try (Connection conn = ConnectorBD.getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, correo);
            cs.setInt(2, tamanioPagina);
            cs.setInt(3, offset);

            boolean hasResults = cs.execute();

            // PRIMER RESULTSET → TOTAL
            if (hasResults) {
                try (ResultSet rsCount = cs.getResultSet()) {
                    if (rsCount.next()) {
                        totalRegistros = rsCount.getLong("total");
                    }
                }
            }

            // SEGUNDO RESULTSET → DATA
            if (cs.getMoreResults()) {
                try (ResultSet rs = cs.getResultSet()) {

                    while (rs.next()) {
                        lista.add(
                                CitaListaResponse.builder()
                                        .idCita(rs.getInt("id_cita"))
                                        .fechaProgramada(rs.getDate("fecha_programada"))
                                        .hora(rs.getTime("hora"))
                                        .motivo(rs.getString("motivo"))

                                        .idPaciente(rs.getInt("id_paciente"))
                                        .nombresPaciente(rs.getString("paciente_nombres"))
                                        .apellidosPaciente(rs.getString("paciente_apellidos"))

                                        .idMedico(rs.getInt("id_medico"))
                                        .nombresMedico(rs.getString("medico_nombres"))
                                        .apellidosMedico(rs.getString("medico_apellidos"))

                                        .estadoCitaResponse(
                                                EstadoCitaResponse.builder()
                                                        .idEstadoCita(rs.getInt("id_estado_cita"))
                                                        .nombreEstado(rs.getString("nombre_estado"))
                                                        .build()
                                        )
                                        .build()
                        );
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar citas por correo", e);
        }

        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }
}

package service.impl;

import bd.ConnectorBD;
import dto.historial_medico.HistorialMedicoRequest;
import dto.historial_medico.HistorialMedicoResponse;
import dto.historial_medico.HistorialMedicoUpdateRequest;
import dto.paginacion.PageResponse;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import service.interfaces.IHistorialMedicoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedicoServiceImpl implements IHistorialMedicoService {
    @Override
    public PageResponse<HistorialMedicoResponse> listar(int pagina, int tamanioPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<HistorialMedicoResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;
        String sqlData = "SELECT * FROM tb_historial_medico WHERE activo = 1 LIMIT ? OFFSET ?";
        String sqlCount = "SELECT COUNT(*) FROM tb_historial_medico WHERE activo = 1";

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
                        lista.add(HistorialMedicoResponse.builder()
                                .idHistorial(rs.getInt("id_historial"))
                                .idCita(rs.getInt("id_cita"))
                                .fechaConsulta(rs.getDate("fecha_consulta"))
                                .diagnostico(rs.getString("diagnostico"))
                                .tratamiento(rs.getString("tratamiento"))
                                .observaciones(rs.getString("observaciones"))
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
    public HistorialMedicoResponse buscarPorId(int idHistorial) {
        String sql = "SELECT * FROM tb_historial_medico WHERE id_historial = ?";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setInt(1,idHistorial);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if (rs.next()){
                return HistorialMedicoResponse.builder()
                        .idHistorial(rs.getInt("id_historial"))
                        .idCita(rs.getInt("id_cita"))
                        .fechaConsulta(rs.getDate("fecha_consulta"))
                        .diagnostico(rs.getString("diagnostico"))
                        .tratamiento(rs.getString("tratamiento"))
                        .observaciones(rs.getString("observaciones"))
                        .build();
            }
            throw new ResourceNotFoundException("No se encontro el historial con id " + idHistorial);
        }catch (SQLException e){
            throw new RuntimeException("SQL EXCEPTION ",e);
        }
    }

    @Override
    public PageResponse<HistorialMedicoResponse> listaHistorialMedicoUsuarioPorCorreo(int pagina, int tamanioPagina, String correo) {
        if (pagina < 1) pagina = 1;
        if (tamanioPagina <= 0) tamanioPagina = 10;

        List<HistorialMedicoResponse> lista = new ArrayList<>();
        long totalRegistros = 0;

        int offset = (pagina - 1) * tamanioPagina;

        String sql = "{CALL listar_historial_por_correo(?, ?, ?)}";

        try (Connection conn = ConnectorBD.getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, correo);
            cs.setInt(2, tamanioPagina);
            cs.setInt(3, offset);

            boolean hasResults = cs.execute();

            if (hasResults) {
                try (ResultSet rsCount = cs.getResultSet()) {
                    if (rsCount.next()) {
                        totalRegistros = rsCount.getLong("total");
                    }
                }
            }

            // Segundo resultset → datos
            if (cs.getMoreResults()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        lista.add(HistorialMedicoResponse.builder()
                                .idHistorial(rs.getInt("id_historial"))
                                .idCita(rs.getInt("id_cita"))
                                .fechaConsulta(rs.getDate("fecha_consulta"))
                                .diagnostico(rs.getString("diagnostico"))
                                .tratamiento(rs.getString("tratamiento"))
                                .observaciones(rs.getString("observaciones"))
                                .build());
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar historial por correo", e);
        }

        return new PageResponse<>(lista, pagina, tamanioPagina, totalRegistros);
    }

    @Override
    public HistorialMedicoResponse guardar(HistorialMedicoRequest historial) {
        String sql = "CALL registrar_historial_medico(?,?,?,?)";

        try (CallableStatement cs = ConnectorBD.getConexion().prepareCall(sql)){
            cs.setInt(1,historial.idCita());
            cs.setString(2, historial.diagnostico());
            cs.setString(3, historial.tratamiento());
            cs.setString(4, historial.observaciones());
            cs.executeUpdate();
            ResultSet rs = cs.getResultSet();
            if(rs.next()){
                int idHistorialMedico = rs.getInt("id_historial");
                return buscarPorId(idHistorialMedico);
            }
            throw new ConflictException("No se puede registrar el historial");
        }catch (SQLException e){
            if ("45000".equals(e.getSQLState())) {
                throw new ConflictException(e.getMessage());
            }
            throw new RuntimeException("SQLEXCEPTION: ",e);
        }
    }

    @Override
    public HistorialMedicoResponse actualizar(int idHistorial, HistorialMedicoUpdateRequest historial) {
        String sql = "UPDATE tb_historial_medico " +
                "SET diagnostico = ? , tratamiento = ?, observaciones = ?" +
                "WHERE id_historial = ? AND activo = 1";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setString(1,historial.diagnostico());
            ps.setString(2, historial.tratamiento());
            ps.setString(3,historial.observaciones());
            ps.setInt(4,idHistorial);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                return buscarPorId(idHistorial);
            } else {
                throw new ConflictException("No se puede actualizar el historial");
            }

        }catch (SQLException e){
            throw new RuntimeException("SQL Exception ",e);
        }
    }

    @Override
    public String eliminar(int idHistorial) {
        String sql = "UPDATE tb_historial_medico " +
                "SET activo = 0 WHERE id_historial = ? AND activo = 1 ";
        try(PreparedStatement ps = ConnectorBD.getConexion().prepareStatement(sql)){
            ps.setInt(1,idHistorial);
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
}

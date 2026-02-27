package service.impl;

import bd.ConnectorBD;
import dto.tipo_documento.TipoDocumentoRequest;
import dto.tipo_documento.TipoDocumentoResponse;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import service.interfaces.ITipoDocumentoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDocumentoServiceImpl implements ITipoDocumentoService {

    @Override
    public List<TipoDocumentoResponse> listar() {
        List<TipoDocumentoResponse> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_tipo_documento WHERE activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(
                        TipoDocumentoResponse.builder()
                                .idTipoDocumento(rs.getInt("id_tipo_documento"))
                                .nombreDocumento(rs.getString("nombre_documento"))
                                .build()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando tipos de documento", e);
        }

        return lista;
    }

    @Override
    public TipoDocumentoResponse buscarPorId(int idTipoDocumento) {
        String sql = "SELECT * FROM tb_tipo_documento WHERE id_tipo_documento = ? AND activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTipoDocumento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return TipoDocumentoResponse.builder()
                        .idTipoDocumento(rs.getInt("id_tipo_documento"))
                        .nombreDocumento(rs.getString("nombre_documento"))
                        .build();
            }

            throw new ResourceNotFoundException("No se encontró el tipo de documento con id " + idTipoDocumento);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TipoDocumentoResponse guardar(TipoDocumentoRequest tipoDocumento) {
        String sql = "INSERT INTO tb_tipo_documento(nombre_documento, activo) VALUES(?,1)";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tipoDocumento.nombreDocumento());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buscarPorId(rs.getInt(1));
            }

            throw new ConflictException("No se pudo registrar el tipo de documento");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TipoDocumentoResponse actualizar(int id, TipoDocumentoRequest tipoDocumento) {
        String sql = "UPDATE tb_tipo_documento SET nombre_documento = ? WHERE id_tipo_documento = ? AND activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoDocumento.nombreDocumento());
            ps.setInt(2, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                return buscarPorId(id);
            }

            throw new ResourceNotFoundException("No se encontró el tipo de documento con id " + id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(int idTipoDocumento) {
        String sql = "UPDATE tb_tipo_documento SET activo = 0 WHERE id_tipo_documento = ?";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTipoDocumento);
            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new ResourceNotFoundException("No se encontró el tipo de documento con id " + idTipoDocumento);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
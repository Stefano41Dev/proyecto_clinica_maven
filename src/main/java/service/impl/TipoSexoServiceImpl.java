package service.impl;

import bd.ConnectorBD;
import dto.tipo_sexo.TipoSexoRequest;
import dto.tipo_sexo.TipoSexoResponse;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import service.interfaces.ITipoSexoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoSexoServiceImpl implements ITipoSexoService {

    @Override
    public List<TipoSexoResponse> listar() {
        List<TipoSexoResponse> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_tipo_sexo WHERE activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(
                        TipoSexoResponse.builder()
                                .idSexo(rs.getInt("id_sexo"))
                                .sexo(rs.getString("sexo"))
                                .build()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando tipos de sexo", e);
        }

        return lista;
    }

    @Override
    public TipoSexoResponse buscarPorId(int idSexo) {
        String sql = "SELECT * FROM tb_tipo_sexo WHERE id_sexo = ? AND activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSexo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return TipoSexoResponse.builder()
                        .idSexo(rs.getInt("id_sexo"))
                        .sexo(rs.getString("sexo"))
                        .build();
            }

            throw new ResourceNotFoundException("No se encontró el tipo de sexo con id " + idSexo);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TipoSexoResponse guardar(TipoSexoRequest tipoSexo) {
        String sql = "INSERT INTO tb_tipo_sexo(sexo, activo) VALUES(?,1)";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tipoSexo.sexo());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buscarPorId(rs.getInt(1));
            }

            throw new ConflictException("No se pudo registrar el tipo de sexo");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TipoSexoResponse actualizar(int idSexo, TipoSexoRequest tipoSexo) {
        String sql = "UPDATE tb_tipo_sexo SET sexo = ? WHERE id_sexo = ? AND activo = 1";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoSexo.sexo());
            ps.setInt(2, idSexo);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                return buscarPorId(idSexo);
            }

            throw new ResourceNotFoundException("No se encontró el tipo de sexo con id " + idSexo);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(int idSexo) {
        String sql = "UPDATE tb_tipo_sexo SET activo = 0 WHERE id_sexo = ?";

        try (Connection conn = ConnectorBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSexo);
            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new ResourceNotFoundException("No se encontró el tipo de sexo con id " + idSexo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
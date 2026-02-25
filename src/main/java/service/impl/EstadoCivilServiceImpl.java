package service.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bd.ConnectorBD;
import dto.estado_civil.EstadoCivilRequest;
import dto.estado_civil.EstadoCivilResponse;
import exception.BadRequestException;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import service.interfaces.IEstadoCivilService;

public class EstadoCivilServiceImpl implements IEstadoCivilService {

    @Override
    public List<EstadoCivilResponse> listar() {

        List<EstadoCivilResponse> lista = new ArrayList<>();
        String sql = "SELECT id_estado_civil, nombre_estado FROM tb_estado_civil WHERE activo = 1";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstadoCivilResponse(
                        rs.getInt("id_estado_civil"),
                        rs.getString("nombre_estado")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public EstadoCivilResponse buscarPorId(int idEstadoCivil) {

        String sql = "SELECT id_estado_civil, nombre_estado FROM tb_estado_civil WHERE id_estado_civil = ? AND activo = 1";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idEstadoCivil);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new EstadoCivilResponse(
                        rs.getInt("id_estado_civil"),
                        rs.getString("nombre_estado")
                );
            } else {
                throw new ResourceNotFoundException("No existe EstadoCivil con id: " + idEstadoCivil);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EstadoCivilResponse guardar(EstadoCivilRequest request) {

        if (request == null || request.nombreEstado() == null || request.nombreEstado().isBlank()) {
            throw new BadRequestException("El nombre del estado civil es obligatorio");
        }

        try (Connection cn = ConnectorBD.getConexion()) {

            // 🔎 Validar duplicado
            String verificar = "SELECT COUNT(*) FROM tb_estado_civil WHERE LOWER(nombre_estado)=LOWER(?) AND activo=1";
            PreparedStatement psVer = cn.prepareStatement(verificar);
            psVer.setString(1, request.nombreEstado());
            ResultSet rs = psVer.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                throw new ConflictException("Ya existe un EstadoCivil con ese nombre");
            }

            // 💾 Insertar
            String insertar = "INSERT INTO tb_estado_civil (nombre_estado, activo) VALUES (?,1)";
            PreparedStatement ps = cn.prepareStatement(insertar, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.nombreEstado());
            ps.executeUpdate();

            ResultSet generated = ps.getGeneratedKeys();
            if (generated.next()) {
                return new EstadoCivilResponse(
                        generated.getInt(1),
                        request.nombreEstado()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public EstadoCivilResponse actualizar(int id, EstadoCivilRequest request) {

        // validar existencia
        buscarPorId(id);

        if (request == null || request.nombreEstado() == null || request.nombreEstado().isBlank()) {
            throw new BadRequestException("El nombre del estado civil es obligatorio");
        }

        String sql = "UPDATE tb_estado_civil SET nombre_estado=? WHERE id_estado_civil=?";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, request.nombreEstado());
            ps.setInt(2, id);
            ps.executeUpdate();

            return new EstadoCivilResponse(id, request.nombreEstado());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean eliminar(int idEstadoCivil) {

        // validar existencia
        buscarPorId(idEstadoCivil);

        String sql = "UPDATE tb_estado_civil SET activo=0 WHERE id_estado_civil=?";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idEstadoCivil);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
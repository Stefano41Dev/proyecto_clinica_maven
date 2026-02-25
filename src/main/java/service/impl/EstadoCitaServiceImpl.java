package service.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bd.ConnectorBD;
import dto.estado_cita.EstadoCitaRequest;
import dto.estado_cita.EstadoCitaResponse;
import exception.BadRequestException;
import exception.ConflictException;
import exception.ResourceNotFoundException;
import model.EstadoCita;
import service.interfaces.IEstadoCitaService;

public class EstadoCitaServiceImpl implements IEstadoCitaService {

    @Override
    public List<EstadoCitaResponse> listar() {

        List<EstadoCitaResponse> lista = new ArrayList<>();
        String sql = "SELECT id_estado_cita, nombre_estado FROM tb_estado_cita WHERE activo = 1";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstadoCitaResponse(
                        rs.getInt("id_estado_cita"),
                        rs.getString("nombre_estado")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public EstadoCitaResponse buscarPorId(int idEstadoCita) {

        String sql = "SELECT id_estado_cita, nombre_estado FROM tb_estado_cita WHERE id_estado_cita = ? AND activo = 1";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idEstadoCita);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new EstadoCitaResponse(
                        rs.getInt("id_estado_cita"),
                        rs.getString("nombre_estado")
                );
            } else {
                throw new ResourceNotFoundException("No existe EstadoCita con id: " + idEstadoCita);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EstadoCitaResponse guardar(EstadoCitaRequest estadoCita) {

        if (estadoCita == null || estadoCita.nombreEstado() == null || estadoCita.nombreEstado().isBlank()) {
            throw new BadRequestException("El nombre del estado es obligatorio");
        }

        try (Connection cn = ConnectorBD.getConexion()) {

            String verificar = "SELECT COUNT(*) FROM tb_estado_cita WHERE LOWER(nombre_estado)=LOWER(?) AND activo=1";
            PreparedStatement psVer = cn.prepareStatement(verificar);
            psVer.setString(1, estadoCita.nombreEstado());
            ResultSet rs = psVer.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                throw new ConflictException("Ya existe un EstadoCita con ese nombre");
            }
            String insertar = "INSERT INTO tb_estado_cita (nombre_estado, activo) VALUES (?,1)";
            PreparedStatement ps = cn.prepareStatement(insertar, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, estadoCita.nombreEstado());
            ps.executeUpdate();

            ResultSet generated = ps.getGeneratedKeys();
            if (generated.next()) {
                return new EstadoCitaResponse(
                        generated.getInt(1),
                        estadoCita.nombreEstado()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL EXCEPTION",e);
        }

        return null;
    }

    @Override
    public EstadoCitaResponse actualizar(int id, EstadoCitaRequest estadoCita) {

        buscarPorId(id);

        if (estadoCita.nombreEstado() == null || estadoCita.nombreEstado().isBlank()) {
            throw new BadRequestException("El nombre del estado es obligatorio");
        }

        String sql = "UPDATE tb_estado_cita SET nombre_estado=? WHERE id_estado_cita=?";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estadoCita.nombreEstado());
            ps.setInt(2, id);
            ps.executeUpdate();

            return new EstadoCitaResponse(id, estadoCita.nombreEstado());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(int idEstadoCita) {

        buscarPorId(idEstadoCita);

        String sql = "UPDATE tb_estado_cita SET activo=0 WHERE id_estado_cita=?";

        try (Connection cn = ConnectorBD.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idEstadoCita);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
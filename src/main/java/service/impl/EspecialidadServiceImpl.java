package service.impl;

import bd.ConnectorBD;
import dto.especialidad.EspecialidadRequest;
import dto.especialidad.EspecialidadResponse;
import exception.ResourceNotFoundException;
import service.interfaces.IEspecialidadService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadServiceImpl implements IEspecialidadService {
    @Override
    public List<EspecialidadResponse> listarEspecialidad() {
        String query = "SELECT * FROM tb_especialidad WHERE activo = 1";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<EspecialidadResponse> listaEspecialidad = new ArrayList<>();
        try{
            preparedStatement = ConnectorBD.getConexion().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                listaEspecialidad.add(
                        EspecialidadResponse.builder()
                                .idEspecialidad(resultSet.getInt(1))
                                .nombre(resultSet.getString(2))
                                .build()
                );

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando especialidades", e);
        }
        return listaEspecialidad;
    }

    @Override
    public EspecialidadResponse buscarPorId(int idEspecialidad) {
        String query = "SELECT * FROM tb_especialidad WHERE id_especialidad = ? AND activo = 1";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = ConnectorBD.getConexion().prepareStatement(query);
            preparedStatement.setInt(1, idEspecialidad);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return EspecialidadResponse.builder()
                        .idEspecialidad(resultSet.getInt(1))
                        .nombre(resultSet.getString(2))
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando especialidad", e);
        }

        throw new ResourceNotFoundException("Especialidad con " + idEspecialidad+" no encontrado");
    }

    @Override
    public EspecialidadResponse guardarEspecialidad(EspecialidadRequest especialidad) {
        String query = "INSERT INTO tb_especialidad (nombre, activo) VALUES (?, 1)";
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            preparedStatement = ConnectorBD.getConexion()
                    .prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, especialidad.nombre());
            preparedStatement.executeUpdate();

            generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return EspecialidadResponse.builder()
                        .idEspecialidad(generatedKeys.getInt(1))
                        .nombre(especialidad.nombre())
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando especialidad", e);
        }

        throw new RuntimeException("No se pudo guardar la especialidad");
    }

    @Override
    public EspecialidadResponse actualizarEspecialidad(int id, EspecialidadRequest especialidad) {
        String query = "UPDATE tb_especialidad SET nombre = ? WHERE id_especialidad = ? AND activo = 1";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = ConnectorBD.getConexion().prepareStatement(query);
            preparedStatement.setString(1, especialidad.nombre());
            preparedStatement.setInt(2, id);

            int filas = preparedStatement.executeUpdate();

            if (filas == 0) {
                throw new ResourceNotFoundException("Especialidad no encontrada o inactiva con id" + id);
            }

            return buscarPorId(id);

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando especialidad", e);
        }
    }

    @Override
    public String eliminarEspecialidadPorId(int idEspecialidad) {
        String query = "UPDATE tb_especialidad SET activo = 0 WHERE id_especialidad = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = ConnectorBD.getConexion().prepareStatement(query);
            preparedStatement.setInt(1, idEspecialidad);

            int filas = preparedStatement.executeUpdate();

            if (filas == 0) {
                throw new ResourceNotFoundException("Especialidad no encontrada");
            }else{
                return "Se elimino correctamente la especilidad";
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando especialidad", e);
        }
    }
}

package service.interfaces;

import java.util.List;

import dto.especialidad.EspecialidadRequest;
import dto.especialidad.EspecialidadResponse;

public interface IEspecialidadService {

    List<EspecialidadResponse> listar();
    EspecialidadResponse buscarPorId(int idEspecialidad);
    EspecialidadResponse guardar(EspecialidadRequest especialidad);
    EspecialidadResponse actualizar(int id, EspecialidadRequest especialidad);
    void eliminar(int idEspecialidad);
}
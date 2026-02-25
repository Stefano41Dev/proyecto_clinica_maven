package service.interfaces;

import java.util.List;

import dto.especialidad.EspecialidadRequest;
import dto.especialidad.EspecialidadResponse;

public interface IEspecialidadService {

    List<EspecialidadResponse> listarEspecialidad();
    EspecialidadResponse buscarPorId(int idEspecialidad);
    EspecialidadResponse guardarEspecialidad(EspecialidadRequest especialidad);
    EspecialidadResponse actualizarEspecialidad(int id, EspecialidadRequest especialidad);
    String eliminarEspecialidadPorId(int idEspecialidad);
}
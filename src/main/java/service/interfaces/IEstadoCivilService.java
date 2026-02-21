package service.interfaces;

import java.util.List;

import dto.estado_civil.EstadoCivilRequest;
import dto.estado_civil.EstadoCivilResponse;

public interface IEstadoCivilService {
	List<EstadoCivilResponse> listar();
	EstadoCivilResponse buscarPorId(int idEstadoCivil);
	EstadoCivilResponse guardar(EstadoCivilRequest estadoCivil);
	EstadoCivilResponse actualizar(int id, EstadoCivilRequest estadoCivil);
    boolean eliminar(int idEstadoCivil);
}

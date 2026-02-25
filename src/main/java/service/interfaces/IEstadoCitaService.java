package service.interfaces;

import java.util.List;

import dto.estado_cita.EstadoCitaRequest;
import dto.estado_cita.EstadoCitaResponse;
import model.EstadoCita;

public interface IEstadoCitaService {
	List<EstadoCitaResponse> listar();
	EstadoCitaResponse buscarPorId(int idEstadoCita);
	EstadoCitaResponse guardar(EstadoCitaRequest estadoCita);
	EstadoCitaResponse actualizar(int id, EstadoCitaRequest estadoCita);
    void eliminar(int idEstadoCita);
}

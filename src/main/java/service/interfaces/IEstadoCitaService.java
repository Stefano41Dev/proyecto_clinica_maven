package service.interfaces;

import java.util.List;

import dto.estado_cita.EstadoCitaResponse;
import model.EstadoCita;

public interface IEstadoCitaService {
	List<EstadoCitaResponse> listar();
	EstadoCitaResponse buscarPorId(int idEstadoCita);
	EstadoCitaResponse guardar(EstadoCita estadoCita);
	EstadoCitaResponse actualizar(int id, EstadoCita estadoCita);
    void eliminar(int idEstadoCita);
}

package service.interfaces;

import java.util.List;

import dto.cita.CitaRequest;
import dto.cita.CitaResponse;

public interface ICitaService {
	List<CitaResponse> listar(int pagina, int tamanioPagina);
	CitaResponse buscarPorId(int idCita);
	CitaResponse guardar(CitaRequest cita);
	CitaResponse actualizar(int id, CitaRequest cita);
    void eliminar(int idCita);
}

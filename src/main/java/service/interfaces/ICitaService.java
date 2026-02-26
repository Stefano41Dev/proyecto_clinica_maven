package service.interfaces;

import java.util.List;

import dto.cita.*;
import dto.paginacion.PageResponse;

public interface ICitaService {
	PageResponse<CitaListaResponse> listar(int pagina, int tamanioPagina);
	CitaDatosCompletosResponse buscarDatosCompletosCitaPorId(int idCita);
	CitaDatosCompletosResponse registrarCita(CitaRequest cita);
	CitaResponse actualizarCita(int idCita, CitaUpdateRequest cita);
	CitaResponse buscarDatosCita(int idCita);
	String eliminar(int idCita);
}

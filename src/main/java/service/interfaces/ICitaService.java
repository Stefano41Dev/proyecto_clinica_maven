package service.interfaces;

import dto.cita.*;
import dto.paginacion.PageResponse;

public interface ICitaService {
	PageResponse<CitaListaResponse> listar(int pagina, int tamanioPagina);
	CitaDatosCompletosResponse buscarDatosCompletosCitaPorId(int idCita);
	CitaDatosCompletosResponse registrarCita(CitaRequest cita);
	CitaResponse actualizarCita(int idCita, CitaUpdateRequest cita);
	CitaResponse buscarDatosCita(int idCita);
	CitaResponse cambiarEstadoCita(CitaCambiarEstadoRequest citaCambiarEstadoRequest);
	String eliminar(int idCita);
	PageResponse<CitaListaResponse> buscarCitasPorCorreo(String correo, int pagina, int tamanioPag);
}

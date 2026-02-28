package service.interfaces;

import java.util.List;

import dto.historial_medico.HistorialMedicoRequest;
import dto.historial_medico.HistorialMedicoResponse;
import dto.historial_medico.HistorialMedicoUpdateRequest;
import dto.paginacion.PageResponse;

public interface IHistorialMedicoService {
	PageResponse<HistorialMedicoResponse> listar(int pagina, int tamanioPagina);
	HistorialMedicoResponse buscarPorId(int idHistorial);
	PageResponse<HistorialMedicoResponse> listaHistorialMedicoUsuarioPorCorreo(int pagina, int tamanioPagina, String correo);
	HistorialMedicoResponse guardar(HistorialMedicoRequest historial);
	HistorialMedicoResponse actualizar(int id, HistorialMedicoUpdateRequest historial);
    String eliminar(int idHistorial);
}

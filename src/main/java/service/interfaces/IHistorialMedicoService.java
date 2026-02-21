package service.interfaces;

import java.util.List;

import dto.historial_medico.HistorialMedicoRequest;
import dto.historial_medico.HistorialMedicoResponse;

public interface IHistorialMedicoService {
	List<HistorialMedicoResponse> listar(int pagina, int tamanioPagina);
	HistorialMedicoResponse buscarPorId(int idHistorial);
	HistorialMedicoResponse guardar(HistorialMedicoRequest historial);
	HistorialMedicoResponse actualizar(int id, HistorialMedicoRequest historial);
    void eliminar(int idHistorial);
}

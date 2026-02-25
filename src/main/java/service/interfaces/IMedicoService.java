package service.interfaces;

import java.util.List;

import dto.medico.*;
import dto.paginacion.PageResponse;

public interface IMedicoService {
	 PageResponse<MedicoResponse> listar(int pagina, int tamanioPagina);
	 MedicoResponse buscarPorId(int idMedico);
	 String registrarMedico(MedicoRequest medico);
	 MedicoResponse registrarMedicoVerificacionGmail(MedicoRequestGmail medico);
	 MedicoTokenVerificacionResponse buscarPorToken(String token);
	 void activarCuentaToken(String token);
	 MedicoResponse actualizarMedico(int id, MedicoRequest medico);
	 String eliminar(int idMedico);
}

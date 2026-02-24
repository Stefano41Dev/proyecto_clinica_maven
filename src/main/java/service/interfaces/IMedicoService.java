package service.interfaces;

import java.util.List;

import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;
import dto.medico.MedicoTokenVerificacionResponse;

public interface IMedicoService {
	 List<MedicoResponse> listar(int pagina, int tamanioPagina);
	 MedicoResponse buscarPorId(int idMedico);
	 MedicoResponse registrarMedico(MedicoRequest medico);
	 MedicoTokenVerificacionResponse buscarPorToken(String token);
	 void activarCuentaToken(String token);
	 MedicoResponse actualizarMedico(int id, MedicoRequest medico);
	 void eliminar(int idMedico);
}

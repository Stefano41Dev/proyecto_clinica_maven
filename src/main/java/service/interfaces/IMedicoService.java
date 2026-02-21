package service.interfaces;

import java.util.List;

import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;

public interface IMedicoService {
	 List<MedicoResponse> listar(int pagina, int tamanioPagina);
	 MedicoResponse buscarPorId(int idMedico);
	 MedicoResponse guardar(MedicoRequest medico);
	 MedicoResponse actualizar(int id, MedicoRequest medico);
	 void eliminar(int idMedico);
}

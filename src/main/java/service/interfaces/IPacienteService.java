package service.interfaces;

import java.util.List;

import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;

public interface IPacienteService {
	List<PacienteResponse> listar(int pagina, int tamanioPagina);
    PacienteResponse buscarPorId(int idPaciente);
    PacienteResponse guardar(PacienteRequest request);
    PacienteResponse actualizar(int idPaciente, PacienteRequest request);
    void eliminar(int idPaciente);
}

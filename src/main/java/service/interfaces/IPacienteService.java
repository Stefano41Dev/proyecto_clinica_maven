package service.interfaces;

import java.util.List;

import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;

public interface IPacienteService {
	List<PacienteResponse> listaPacientes(int pagina, int tamanioPagina);
    PacienteResponse buscarPorId(int idPaciente);
    PacienteResponse registrarPaciente(PacienteRequest request);
    PacienteResponse actualizarPaciente(int idPaciente, PacienteRequest request);
    void eliminar(int idPaciente);
}

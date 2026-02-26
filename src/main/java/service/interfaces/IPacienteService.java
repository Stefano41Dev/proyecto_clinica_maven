package service.interfaces;

import java.util.List;

import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;
import dto.paciente.PacienteTokenVerificacionResponse;
import dto.paginacion.PageResponse;

public interface IPacienteService {
	PageResponse<PacienteResponse> listaPacientes(int pagina, int tamanioPagina);
    PacienteResponse buscarPorId(int idPaciente);
    String registrarPaciente(PacienteRequest request);
    PacienteTokenVerificacionResponse buscarPorToken(String token);
    void activarCuenta(String token);
    PacienteResponse actualizarPaciente(int idPaciente, PacienteRequest request);
    String eliminar(int idPaciente);
}

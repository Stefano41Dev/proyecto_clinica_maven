package dto.paciente;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PacienteTokenVerificacionResponse (
        int idPaciente,
        String tokenVerificacion,
        LocalDateTime tokenExpiracion
){
}

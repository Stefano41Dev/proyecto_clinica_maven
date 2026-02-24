package dto.medico;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
public record MedicoTokenVerificacionResponse(
        int idMedico,
        String tokenVerificacion,
        LocalDateTime tokenExpiracion
) {

}

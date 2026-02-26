package dto.cita;

import lombok.Builder;

import java.sql.Time;
import java.util.Date;

@Builder
public record CitaUpdateRequest(
        Integer idMedico,
        Date fechaProgramada,
        Time hora
) {
}

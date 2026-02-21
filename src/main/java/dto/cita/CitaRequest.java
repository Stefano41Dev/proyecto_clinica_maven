package dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaRequest(
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaProgramada,
        LocalTime hora,
        Integer idEstadoCita,
        String motivo
) {}

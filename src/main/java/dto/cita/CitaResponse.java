package dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaResponse(
        Integer idCita,
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaProgramada,
        LocalTime hora,
        Integer idEstadoCita,
        String motivo
) {}

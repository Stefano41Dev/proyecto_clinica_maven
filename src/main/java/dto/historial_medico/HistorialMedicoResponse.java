package dto.historial_medico;

import java.time.LocalDate;

public record HistorialMedicoResponse(
        Integer idHistorial,
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaConsulta,
        String diagnostico,
        String tratamiento,
        String observaciones
) {}
package dto.historial_medico;

import java.time.LocalDate;

public record HistorialMedicoRequest(
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaConsulta,
        String diagnostico,
        String tratamiento,
        String observaciones
) {}

package dto.historial_medico;

public record HistorialMedicoUpdateRequest (
        String diagnostico,
        String tratamiento,
        String observaciones
) {}

package dto.estado_cita;

import lombok.Builder;

@Builder
public record EstadoCitaResponse(
        Integer idEstadoCita,
        String nombreEstado
) {}
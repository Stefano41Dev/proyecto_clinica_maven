package dto.estado_civil;

import lombok.Builder;

@Builder
public record EstadoCivilResponse(
        Integer idEstadoCivil,
        String nombreEstado
) {}

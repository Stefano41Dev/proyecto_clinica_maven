package dto.especialidad;

import lombok.Builder;

@Builder
public record EspecialidadResponse(
        Integer idEspecialidad,
        String nombre
) {}
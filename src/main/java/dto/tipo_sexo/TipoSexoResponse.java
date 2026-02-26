package dto.tipo_sexo;

import lombok.Builder;

@Builder
public record TipoSexoResponse(
        Integer idSexo,
        String sexo
) {}

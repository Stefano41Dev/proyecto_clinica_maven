package dto.tipo_documento;

import lombok.Builder;
@Builder
public record TipoDocumentoResponse(
        Integer idTipoDocumento,
        String nombreDocumento
) {}

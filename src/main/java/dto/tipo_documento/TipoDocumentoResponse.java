package dto.tipo_documento;

import lombok.Builder;

public record TipoDocumentoResponse(
        Integer idTipoDocumento,
        String nombreDocumento
) {}

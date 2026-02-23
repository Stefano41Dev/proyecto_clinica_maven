package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumento {

    private Integer idTipoDocumento;
    private String nombreDocumento;
    private Boolean activo;

}

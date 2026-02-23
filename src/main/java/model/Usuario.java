package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.Rol;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    private Integer idUsuario;
    private Integer idPersona;
    private String correo;
    private String passwd;
    private Rol rol;
    private Boolean activo;

}
package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {
    private Integer idEspecialidad;
    private String nombre;
    private Boolean activo;

}

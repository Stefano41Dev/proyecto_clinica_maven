package model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Medico {

    private Integer idMedico;
    private String nombres;
    private String apellidos;
    private Integer idEspecialidad;
    private String numeroColegiatura;
    private String telefono;
    private String correo;
    private LocalDate fechaRegistro;
    private Boolean activo;

}

package model;

import java.time.LocalDate;
import java.util.Date;

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
    private Integer idPersona;
    private Integer idEspecialidad;
    private String numeroColegiatura;
    private String telefono;
    private Date fechaRegistro;
    private Boolean activo;

}

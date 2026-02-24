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
public class Paciente {
    private Integer idPaciente;
    private Integer idTipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento ;
    private LocalDate fechaRegistro;
    private Integer idSexo;
    private Integer idEstadoCivil;
    private Boolean activo;

}

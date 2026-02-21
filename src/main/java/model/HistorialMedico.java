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
public class HistorialMedico {

    private Integer idHistorial;
    private Integer idPaciente;
    private Integer idMedico;
    private LocalDate fechaConsulta;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

}

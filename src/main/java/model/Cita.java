package model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    private Integer idCita;
    private Integer idPaciente;
    private Integer idMedico;
    private LocalDate fechaProgramada;
    private LocalTime hora;
    private Integer idEstadoCita;
    private String motivo;

}
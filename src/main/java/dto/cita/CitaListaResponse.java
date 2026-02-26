package dto.cita;

import dto.estado_cita.EstadoCitaResponse;
import lombok.Builder;

import java.sql.Time;
import java.util.Date;
@Builder
public record CitaListaResponse(
        Integer idCita,
        Date fechaProgramada,
        Time hora,
        String motivo,
        Integer idPaciente,
        String nombresPaciente,
        String apellidosPaciente,
        Integer idMedico,
        String nombresMedico,
        String apellidosMedico,
        EstadoCitaResponse estadoCitaResponse
) {
}

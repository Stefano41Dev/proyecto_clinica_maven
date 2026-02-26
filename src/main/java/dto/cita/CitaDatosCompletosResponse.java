package dto.cita;

import com.fasterxml.jackson.annotation.JsonFormat;
import dto.estado_cita.EstadoCitaResponse;
import dto.medico.MedicoResponse;
import dto.paciente.PacienteResponse;
import lombok.Builder;

import java.sql.Time;
import java.util.Date;

@Builder
public record CitaDatosCompletosResponse(
        Integer idCita,
        PacienteResponse pacienteResponse,
        MedicoResponse medicoResponse,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date fechaProgramada,
        Time hora,
        EstadoCitaResponse estadoCitaResponse,
        String motivo
) {}

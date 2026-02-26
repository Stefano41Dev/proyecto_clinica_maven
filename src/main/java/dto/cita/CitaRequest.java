package dto.cita;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.util.Date;

public record CitaRequest(
        Integer idPaciente,
        Integer idMedico,
        Date fechaProgramada,
        Time hora,
        Integer idEstadoCita,
        String motivo
) {}

package dto.cita;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.util.Date;

public record CitaRequest(
        Integer idPaciente,
        Integer idMedico,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date fechaProgramada,
        @JsonFormat(pattern = "HH:mm")
        Time hora,
        Integer idEstadoCita,
        String motivo
) {}

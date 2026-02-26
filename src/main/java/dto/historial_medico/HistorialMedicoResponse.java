package dto.historial_medico;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;


import java.util.Date;

@Builder
public record HistorialMedicoResponse(
        Integer idHistorial,
        Integer idCita,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date fechaConsulta,
        String diagnostico,
        String tratamiento,
        String observaciones
) {}
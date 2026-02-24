package dto.medico;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.util.Date;

@Builder
public record MedicoResponse(
        Integer idMedico,
        String nombres,
        String apellidos,
        Integer idEspecialidad,
        String nombreEspecialidad,
        String numeroColegiatura,
        String telefono,
        String correo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date fechaRegistro
) {}
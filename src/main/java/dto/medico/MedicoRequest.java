package dto.medico;

import java.time.LocalDate;

public record MedicoRequest(
        String nombres,
        String apellidos,
        Integer idEspecialidad,
        String numeroColegiatura,
        String telefono,
        String correo,
        LocalDate fechaRegistro
) {}
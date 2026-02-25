package dto.paciente;

import java.util.Date;

public record PacienteRequest(
        Integer idTipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        Date fechaNacimiento,
        Integer idSexo,
        Integer idEstadoCivil,
        String correo,
        String password
) {}
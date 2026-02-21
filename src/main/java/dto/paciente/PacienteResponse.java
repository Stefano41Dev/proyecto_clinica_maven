package dto.paciente;

import java.time.LocalDate;

public record PacienteResponse(
        Integer idPaciente,
        Integer idTipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        LocalDate fechaRegistro,
        Integer idSexo,
        Integer idEstadoCivil,
        String correo
) {}
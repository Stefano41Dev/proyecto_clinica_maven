package dto.medico;

public record MedicoRequestGmail(
        String nombres,
        String apellidos,
        Integer idEspecialidad,
        String numeroColegiatura,
        String telefono,
        String correo

) {}
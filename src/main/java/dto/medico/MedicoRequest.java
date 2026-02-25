package dto.medico;

public record MedicoRequest(
        String nombres,
        String apellidos,
        Integer idEspecialidad,
        String numeroColegiatura,
        String telefono,
        String correo,
        String password
) {
}

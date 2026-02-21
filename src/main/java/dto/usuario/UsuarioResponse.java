package dto.usuario;

public record UsuarioResponse(
        Integer idPersona,
        String correo,
        String rol
) {}
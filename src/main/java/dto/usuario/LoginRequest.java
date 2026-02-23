package dto.usuario;

public record LoginRequest(
        String correo,
        String passwd
      
) {}

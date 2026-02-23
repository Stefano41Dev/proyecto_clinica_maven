package dto.usuario;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token
) {
}

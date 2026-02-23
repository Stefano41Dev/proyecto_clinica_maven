package exception;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends GlobalExceptionHttp {
    public UnauthorizedException(String message) {
        super(message, Response.Status.UNAUTHORIZED);
    }
}

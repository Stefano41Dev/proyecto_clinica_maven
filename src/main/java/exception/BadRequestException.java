package exception;

import jakarta.ws.rs.core.Response;

public class BadRequestException extends GlobalExceptionHttp {
    public BadRequestException(String message) {
        super(message, Response.Status.NOT_FOUND);
    }
}

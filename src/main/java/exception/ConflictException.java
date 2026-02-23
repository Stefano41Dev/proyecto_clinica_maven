package exception;

import jakarta.ws.rs.core.Response;

public class ConflictException extends GlobalExceptionHttp {
    public ConflictException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}

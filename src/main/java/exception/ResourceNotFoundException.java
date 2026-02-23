package exception;

import jakarta.ws.rs.core.Response;

public class ResourceNotFoundException extends GlobalExceptionHttp{
    public ResourceNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND);
    }
}

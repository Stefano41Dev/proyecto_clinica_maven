package exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class GlobalExceptionHttp extends RuntimeException{
    private final Response.Status status;

    public GlobalExceptionHttp(String message, Response.Status status) {
        super(message);
        this.status = status;
    }

}

package exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHttpMapper implements ExceptionMapper<GlobalExceptionHttp> {
    @Override
    public Response toResponse(GlobalExceptionHttp ex) {
        return Response.status(ex.getStatus())
                .entity(ex.getMessage())
                .build();
    }
}

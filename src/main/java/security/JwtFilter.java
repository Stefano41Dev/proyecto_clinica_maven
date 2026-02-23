package security;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (path.contains("auth/login")) {
            return; // no proteger login
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext);
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            Claims claims = JwtUtil.validarToken(token);

            String rolUsuario = claims.get("rol", String.class);

            // 🔎 validar anotación
            Method method = ((ResourceInfo) requestContext
                    .getProperty("org.glassfish.jersey.server.model.internal.ResourceInfo"))
                    .getResourceMethod();

        } catch (Exception e) {
            abort(requestContext);
        }
    }
    private void abort(ContainerRequestContext ctx) {
        ctx.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build()
        );
    }
}

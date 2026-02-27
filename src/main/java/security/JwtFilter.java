package security;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import model.enums.Rol;

import java.io.IOException;
import java.lang.reflect.Method;
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        if (path.contains("auth/login") || path.contains("verificacion-email/activar")) {
            return;
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

            Method method = resourceInfo.getResourceMethod();
            Class<?> resourceClass = resourceInfo.getResourceClass();

            RolesPermitidos rolesPermitidos = method.getAnnotation(RolesPermitidos.class);

            if (rolesPermitidos == null) {
                rolesPermitidos = resourceClass.getAnnotation(RolesPermitidos.class);
            }

            if (rolesPermitidos != null) {
                boolean autorizado = false;

                for (Rol rol : rolesPermitidos.value()) {
                    if (rol.name().equals(rolUsuario)) {
                        autorizado = true;
                        break;
                    }
                }

                if (!autorizado) {
                    abort(requestContext);
                }
            }

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

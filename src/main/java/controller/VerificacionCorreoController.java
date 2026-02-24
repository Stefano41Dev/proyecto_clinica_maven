package controller;

import dto.medico.MedicoTokenVerificacionResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Medico;
import service.impl.MedicoServiceImpl;
import service.interfaces.IMedicoService;

import java.time.LocalDateTime;
@Path("/verificacion-email")
public class VerificacionCorreoController {
    IMedicoService medicoService = new MedicoServiceImpl();

    @GET
    @Path("/activar")
    @Produces(MediaType.TEXT_PLAIN)
    public Response activarCuenta(@QueryParam("token") String token) {

        MedicoTokenVerificacionResponse medico = medicoService.buscarPorToken(token);

        if (medico == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token inválido")
                    .build();
        }

        if (medico.tokenExpiracion().isBefore(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token expirado")
                    .build();
        }

        medicoService.activarCuentaToken(token);

        return Response.ok("Cuenta activada correctamente").build();
    }
}

package controller;

import dto.medico.MedicoTokenVerificacionResponse;
import dto.paciente.PacienteTokenVerificacionResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Medico;
import service.impl.MedicoServiceImpl;
import service.impl.PacienteServiceImpl;
import service.interfaces.IMedicoService;
import service.interfaces.IPacienteService;

import java.time.LocalDateTime;
@Path("/verificacion-email")
public class VerificacionCorreoController {
    IMedicoService medicoService = new MedicoServiceImpl();
    IPacienteService pacienteService = new PacienteServiceImpl();
    @GET
    @Path("/activar-medico")
    @Produces(MediaType.TEXT_PLAIN)
    public Response activarCuentaMedico(@QueryParam("token") String token) {

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
    @GET
    @Path("/activar-paciente")
    @Produces(MediaType.TEXT_PLAIN)
    public Response activarCuentaPaciente(@QueryParam("token") String token) {

        PacienteTokenVerificacionResponse paciente = pacienteService.buscarPorToken(token);

        if (paciente == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token inválido")
                    .build();
        }

        if (paciente.tokenExpiracion().isBefore(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token expirado")
                    .build();
        }

        pacienteService.activarCuenta(token);

        return Response.ok("Cuenta activada correctamente").build();
    }
}

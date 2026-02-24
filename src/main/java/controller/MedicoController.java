package controller;

import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;
import dto.usuario.LoginRequest;
import dto.usuario.LoginResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.MedicoServiceImpl;
import service.interfaces.IMedicoService;

@Path("/medico")
public class MedicoController {
    IMedicoService medicoService = new MedicoServiceImpl();
    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR})
    public Response registrarMedico(
            MedicoRequest medicoRequest
    ){
        MedicoResponse medicoDto = medicoService.registrarMedico(medicoRequest);
        return Response.ok().entity(medicoDto).build();
    }
}

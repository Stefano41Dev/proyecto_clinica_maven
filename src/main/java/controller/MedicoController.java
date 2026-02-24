package controller;

import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;
import dto.medico.MedicoUpdateRequest;
import jakarta.ws.rs.*;
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
    @GET
    @Path("/buscar/{id_medico}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarMedicoPorId(
            @PathParam("id_medico") int id
    ){
        MedicoResponse medicoDto = medicoService.buscarPorId(id);
        return Response.ok().entity(medicoDto).build();
    }

    @PUT
    @Path("/actualizar/{id_medico}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR})
    public Response actualizarMedico(
            @PathParam("id_medico") int idMedico,
            MedicoUpdateRequest medicoRequest
    ){
        MedicoResponse medicoDto = medicoService.actualizarMedico(idMedico,medicoRequest);
        return Response.ok().entity(medicoDto).build();
    }

}

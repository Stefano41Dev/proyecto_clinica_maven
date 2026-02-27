package controller;

import dto.medico.MedicoRequest;
import dto.medico.MedicoRequestGmail;
import dto.medico.MedicoResponse;
import dto.paginacion.PageResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.MedicoServiceImpl;
import service.interfaces.IMedicoService;

@Path("/medico")
@RolesPermitidos({Rol.ADMINISTRADOR})
public class MedicoController {
    IMedicoService medicoService = new MedicoServiceImpl();
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMedicos(
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina

    ){
        PageResponse<MedicoResponse> medicoDto = medicoService.listar(pagina,tamanhioPagina);
        return Response.ok().entity(medicoDto).build();
    }

    @POST
    @Path("/registrar-gmail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarMedicoPorGmail(
            MedicoRequestGmail medicoRequest
    ){
        MedicoResponse medicoDto = medicoService.registrarMedicoVerificacionGmail(medicoRequest);
        return Response.ok().entity(medicoDto).build();
    }

    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarMedico(
            MedicoRequest medicoRequest
    ){
        String linkVerificacion = medicoService.registrarMedico(medicoRequest);
        return Response.ok().entity(linkVerificacion).build();
    }
    @GET
    @Path("/buscar/{id_medico}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR,Rol.MEDICO,Rol.PACIENTE})
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
    public Response actualizarMedico(
            @PathParam("id_medico") int idMedico,
            MedicoRequest medicoRequest
    ){
        MedicoResponse medicoDto = medicoService.actualizarMedico(idMedico,medicoRequest);
        return Response.ok().entity(medicoDto).build();
    }

    @DELETE
    @Path("/eliminar/{id_medico}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarMedico(
            @PathParam("id_medico") int idMedico
    ){
        String mensaje = medicoService.eliminar(idMedico);
        return Response.ok().entity(mensaje).build();
    }
}

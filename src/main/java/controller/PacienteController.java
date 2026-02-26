package controller;

import dto.paciente.PacienteRequest;
import dto.paciente.PacienteResponse;
import dto.paginacion.PageResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.PacienteServiceImpl;
import service.interfaces.IPacienteService;

@Path("/paciente")
@RolesPermitidos(Rol.ADMINISTRADOR)
public class PacienteController {
    IPacienteService pacienteService = new PacienteServiceImpl();
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMedicos(
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina

    ){
        PageResponse<PacienteResponse> pacienteDto = pacienteService.listaPacientes(pagina,tamanhioPagina);
        return Response.ok().entity(pacienteDto).build();
    }
    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarPaciente(
        PacienteRequest pacienteRequest
    ){
        String linkVerificacion = pacienteService.registrarPaciente(pacienteRequest);
        return Response.ok().entity(linkVerificacion).build();
    }
    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPaciente(
            @PathParam("id") int id
    ){
        PacienteResponse pacienteDto = pacienteService.buscarPorId(id);
        return Response.ok().entity(pacienteDto).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPaciente(
            @PathParam("id") int id
    ){
        String mensaje = pacienteService.eliminar(id);
        return Response.ok().entity(mensaje).build();
    }
    @PUT
    @Path("/actualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarPaciente(
            @PathParam("id") int id,
            PacienteRequest pacienteRequest
    ){
        PacienteResponse pacienteResponse = pacienteService.actualizarPaciente(id,pacienteRequest);
        return Response.ok().entity(pacienteResponse).build();
    }

}

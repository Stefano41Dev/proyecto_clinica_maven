package controller;

import dto.historial_medico.HistorialMedicoRequest;
import dto.historial_medico.HistorialMedicoResponse;
import dto.historial_medico.HistorialMedicoUpdateRequest;
import dto.paginacion.PageResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.HistorialMedicoServiceImpl;
import service.interfaces.IHistorialMedicoService;

@Path("/historial-medico")
@RolesPermitidos({Rol.ADMINISTRADOR,Rol.MEDICO})
public class HistorialMedicoController {
    IHistorialMedicoService historialMedicoService = new HistorialMedicoServiceImpl();
    @POST
    @Path("/registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarHistorialMedico(
            HistorialMedicoRequest historialMedicoRequest
    ){
        HistorialMedicoResponse historialMedicoResponse = historialMedicoService.guardar(historialMedicoRequest);
        return Response.status(Response.Status.CREATED).entity(historialMedicoResponse).build();
    }
    @GET
    @Path("/buscar/{idHistorial}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarHistorialMedico(
            @PathParam("idHistorial") int idHistorial
    ){
        HistorialMedicoResponse historialMedicoResponse = historialMedicoService.buscarPorId(idHistorial);
        return Response.status(Response.Status.CREATED).entity(historialMedicoResponse).build();
    }
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarHistorialMedico(
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina
    ){
        PageResponse<HistorialMedicoResponse> historialMedicoResponse = historialMedicoService.listar(pagina,tamanhioPagina);
        return Response.status(Response.Status.CREATED).entity(historialMedicoResponse).build();
    }
    @DELETE
    @Path("/eliminar/{idHistorial}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarHistorial(
            @PathParam("idHistorial") int idHistorial
    ){
        String mensaje = historialMedicoService.eliminar(idHistorial);
        return Response.status(Response.Status.CREATED).entity(mensaje).build();
    }
    @PUT
    @Path("/actualizar/{idHistorial}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarHistorial(
            @PathParam("idHistorial") int idHistorial,
            HistorialMedicoUpdateRequest request
    ){
        HistorialMedicoResponse historialMedicoResponse = historialMedicoService.actualizar(idHistorial, request);
        return Response.status(Response.Status.CREATED).entity(historialMedicoResponse).build();
    }
    @GET
    @Path("/buscar-historial-paciente/{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR, Rol.PACIENTE})
    public Response buscarHistorialPaciente(
            @PathParam("correo") String correo,
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina
    ){
        PageResponse<HistorialMedicoResponse> historialMedicoResponse = historialMedicoService.listaHistorialMedicoUsuarioPorCorreo(pagina,tamanhioPagina,correo);
        return Response.ok().entity(historialMedicoResponse).build();
    }

}

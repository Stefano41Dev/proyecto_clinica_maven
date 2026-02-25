package controller;

import java.util.List;

import dto.estado_cita.EstadoCitaResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import model.EstadoCita;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.EstadoCitaServiceImpl;
import service.interfaces.IEstadoCitaService;

@Path("/estado-cita")
@RolesPermitidos({Rol.ADMINISTRADOR})   
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoCitaController {

    private IEstadoCitaService service = new EstadoCitaServiceImpl();

    @GET
    @Path("/listado")
    public Response listar() {
        List<EstadoCitaResponse> lista = service.listar();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) {
        EstadoCitaResponse response = service.buscarPorId(id);
        return Response.ok(response).build();
    }

    @POST
    public Response guardar(EstadoCita estadoCita) {
        EstadoCitaResponse response = service.guardar(estadoCita);
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, EstadoCita estadoCita) {
        EstadoCitaResponse response = service.actualizar(id, estadoCita);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        service.eliminar(id);
        return Response.ok("EstadoCita eliminado correctamente").build();
    }
}
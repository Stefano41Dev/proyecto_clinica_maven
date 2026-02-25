package controller;

import java.util.List;

import dto.estado_civil.EstadoCivilRequest;
import dto.estado_civil.EstadoCivilResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.EstadoCivilServiceImpl;
import service.interfaces.IEstadoCivilService;


@Path("/estado-civil")
@RolesPermitidos({Rol.ADMINISTRADOR})
public class EstadoCivilController {

    IEstadoCivilService service = new EstadoCivilServiceImpl();

    @GET
    @Path("/listado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        List<EstadoCivilResponse> lista = service.listar();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("id") int id) {
        EstadoCivilResponse response = service.buscarPorId(id);
        return Response.ok(response).build();
    }

    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardar(EstadoCivilRequest request) {
        EstadoCivilResponse response = service.guardar(request);
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/actualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") int id, EstadoCivilRequest request) {
        EstadoCivilResponse response = service.actualizar(id, request);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") int id) {
        service.eliminar(id);
        return Response.ok("EstadoCivil eliminado correctamente").build();
    }
}
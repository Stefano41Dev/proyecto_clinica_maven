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
@RolesPermitidos({Rol.ADMINISTRADOR})   // rol
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoCivilController {

    private IEstadoCivilService service = new EstadoCivilServiceImpl();

    @GET
    @Path("/listado")
    public Response listar() {
        List<EstadoCivilResponse> lista = service.listar();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) {
        EstadoCivilResponse response = service.buscarPorId(id);
        return Response.ok(response).build();
    }

    @POST
    public Response guardar(EstadoCivilRequest request) {
        EstadoCivilResponse response = service.guardar(request);
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, EstadoCivilRequest request) {
        EstadoCivilResponse response = service.actualizar(id, request);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        service.eliminar(id);
        return Response.ok("EstadoCivil eliminado correctamente").build();
    }
}
package controller;

import dto.tipo_sexo.TipoSexoRequest;
import dto.tipo_sexo.TipoSexoResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.TipoSexoServiceImpl;
import service.interfaces.ITipoSexoService;

import java.util.List;

@Path("/tipo-sexo")
@RolesPermitidos({Rol.ADMINISTRADOR})
public class TipoSexoController {

    ITipoSexoService service = new TipoSexoServiceImpl();

    @GET
    @Path("/listado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        List<TipoSexoResponse> lista = service.listar();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("id") int id) {
        TipoSexoResponse response = service.buscarPorId(id);
        return Response.ok(response).build();
    }

    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrar(TipoSexoRequest request) {
        TipoSexoResponse response = service.guardar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/actualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") int id, TipoSexoRequest request) {
        TipoSexoResponse response = service.actualizar(id, request);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") int id) {
        service.eliminar(id);
        return Response.ok("Tipo de sexo eliminado correctamente").build();
    }
}
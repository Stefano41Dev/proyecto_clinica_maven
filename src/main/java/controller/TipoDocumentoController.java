package controller;

import dto.tipo_documento.TipoDocumentoRequest;
import dto.tipo_documento.TipoDocumentoResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.TipoDocumentoServiceImpl;
import service.interfaces.ITipoDocumentoService;

import java.util.List;

@Path("/tipo-documento")
@RolesPermitidos({Rol.ADMINISTRADOR})
public class TipoDocumentoController {

    ITipoDocumentoService service = new TipoDocumentoServiceImpl();

    @GET
    @Path("/listado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        List<TipoDocumentoResponse> lista = service.listar();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("id") int id) {
        TipoDocumentoResponse response = service.buscarPorId(id);
        return Response.ok(response).build();
    }

    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrar(TipoDocumentoRequest request) {
        TipoDocumentoResponse response = service.guardar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/actualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") int id, TipoDocumentoRequest request) {
        TipoDocumentoResponse response = service.actualizar(id, request);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") int id) {
        service.eliminar(id);
        return Response.ok("Tipo de documento eliminado correctamente").build();
    }
}
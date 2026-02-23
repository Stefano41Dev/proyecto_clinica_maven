package controller;

import java.util.List;

import dto.especialidad.EspecialidadRequest;
import dto.especialidad.EspecialidadResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.EspecialidadServiceImpl;
import service.interfaces.IEspecialidadService;

@Path("/especialidad")
@RolesPermitidos({Rol.ADMINISTRADOR})
public class EspecialidadController {
    IEspecialidadService service = new EspecialidadServiceImpl();
    @GET
    @Path("/listado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaEspecialidad() {
        List<EspecialidadResponse>  listar=service.listarEspecialidad();

        return Response.ok(listar).build();
    }
    @GET
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarEspecialidad(
            EspecialidadRequest especialidadRequest
    ) {
        EspecialidadResponse especialidadResponse = service.guardarEspecialidad(especialidadRequest);
        return Response.status(Status.CREATED).entity(especialidadResponse).build();
    }
    @GET
    @Path("/actualizar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEspecialidad() {
        List<EspecialidadResponse>  listar=service.listarEspecialidad();

        return Response.ok(listar).build();
    }
    @GET
    @Path("/eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEspecialidad() {
        List<EspecialidadResponse>  listar=service.listarEspecialidad();

        return Response.ok(listar).build();
    }
    @GET
    @Path("/buscar/{idEspecialidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarEspecialidad(
            @PathParam("idEspecialidad") int idEspecialidad
    ) {
        EspecialidadResponse  especialidadBuscada =service.buscarPorId(idEspecialidad);

        return Response.ok(especialidadBuscada).build();
    }
}

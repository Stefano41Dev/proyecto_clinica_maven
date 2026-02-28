package controller;

import dto.cita.*;
import dto.paginacion.PageResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.enums.Rol;
import security.RolesPermitidos;
import service.impl.CitaServiceImpl;
import service.interfaces.ICitaService;

import java.util.Date;


@Path("/cita")
@RolesPermitidos({Rol.MEDICO, Rol.ADMINISTRADOR})
public class CitaController {
    ICitaService citaService = new CitaServiceImpl();

    @GET
    @Path("/buscar-completa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR, Rol.PACIENTE})

    public Response buscarCitaCompletaId(
            @PathParam("id") int id
    ){
        CitaDatosCompletosResponse citaDto = citaService.buscarDatosCompletosCitaPorId(id);
        return Response.ok().entity(citaDto).build();
    }

    @POST
    @Path("/registrar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesPermitidos(Rol.ADMINISTRADOR)
    public Response registrarCita(
            CitaRequest citaRequest
    ){
        CitaDatosCompletosResponse citaDto = citaService.registrarCita(citaRequest);
        return Response.ok().entity(citaDto).build();
    }
    @PUT
    @Path("/actualizar/{idCita}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarCita(
            @PathParam("idCita") int idCita,
            CitaUpdateRequest citaUpdateRequest
    ){
        CitaResponse citaDto = citaService.actualizarCita(idCita, citaUpdateRequest);
        return Response.ok().entity(citaDto).build();
    }
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina
    ){
        PageResponse<CitaListaResponse> lista = citaService.listar(pagina,tamanhioPagina);
        return Response.ok().entity(lista).build();
    }
    @DELETE
    @Path("/eliminar/{idCita}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCita(
           @PathParam("idCita") int idCita
    ){
        String mensaje = citaService.eliminar(idCita);
        return Response.ok().entity(mensaje).build();
    }

    @PATCH
    @Path("/cambiar-estado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarEstado(
          CitaCambiarEstadoRequest estadoRequest
    ){
        CitaResponse citaDto = citaService.cambiarEstadoCita(estadoRequest);
        return Response.ok().entity(citaDto).build();
    }

    @GET
    @Path("/buscar-citas-correo/{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.ADMINISTRADOR, Rol.PACIENTE})
    public Response buscarCitasCorreo(
            @PathParam("correo") String correo,
            @QueryParam("pagina") int pagina,
            @QueryParam("tamPag") int tamanhioPagina
    ){
        PageResponse<CitaListaResponse> citaDto = citaService.buscarCitasPorCorreo(correo, pagina, tamanhioPagina);
        return Response.ok().entity(citaDto).build();
    }

    @GET
    @Path("/listar-filtro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesPermitidos({Rol.MEDICO})
    public Response listarCitasConFiltro(

            @QueryParam("pagina") @DefaultValue("1") int pagina,
            @QueryParam("tamPag") @DefaultValue("10") int tamanhioPagina,

            @QueryParam("idEstadoCita") Integer idEstadoCita,
            @QueryParam("fecha") String fechaStr
    ) {

        Date fecha = null;

        try {
            if (fechaStr != null && !fechaStr.isEmpty()) {
                fecha = java.sql.Date.valueOf(fechaStr);
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Use yyyy-MM-dd")
                    .build();
        }

        PageResponse<CitaListaResponse> citaDto =
                citaService.listarPaginacionPorEstadoCitaYFecha(pagina, tamanhioPagina, idEstadoCita, fecha);

        return Response.ok().entity(citaDto).build();
    }
}

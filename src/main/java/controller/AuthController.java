package controller;

import dto.usuario.LoginRequest;
import dto.usuario.LoginResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.impl.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;

@Path("/auth")
public class AuthController {
    IUsuarioService usuarioService = new UsuarioServiceImpl();
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            LoginRequest loginRequest
    ){
        LoginResponse loginResponse = usuarioService.login(loginRequest);
        return Response.ok().entity(loginResponse).build();
    }
}

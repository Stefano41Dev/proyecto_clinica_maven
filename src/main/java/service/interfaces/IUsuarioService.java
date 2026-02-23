package service.interfaces;

import java.util.List;

import dto.usuario.LoginRequest;
import dto.usuario.LoginResponse;
import dto.usuario.UsuarioResponse;

public interface IUsuarioService {
	 	
		List<UsuarioResponse> listar(int pagina, int tamanioPagina);
	    UsuarioResponse guardar(LoginRequest request);
	    UsuarioResponse actualizar(int idUsuario, LoginRequest request);
	    LoginResponse login(LoginRequest loginRequest);

	    void eliminar();
}

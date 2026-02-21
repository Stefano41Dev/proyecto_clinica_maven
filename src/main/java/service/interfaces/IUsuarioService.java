package service.interfaces;

import java.util.List;

import dto.usuario.UsuarioRequest;
import dto.usuario.UsuarioResponse;

public interface IUsuarioService {
	 	
		List<UsuarioResponse> listar(int pagina, int tamanioPagina);
	    UsuarioResponse guardar(UsuarioRequest request);
	    UsuarioResponse actualizar(int idUsuario, UsuarioRequest request);
	    UsuarioResponse login(String correo, String password);
	    void eliminar();
}

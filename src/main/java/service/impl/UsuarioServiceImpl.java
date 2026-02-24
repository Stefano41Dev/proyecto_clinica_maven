package service.impl;

import bd.ConnectorBD;
import dto.usuario.LoginRequest;
import dto.usuario.LoginResponse;
import dto.usuario.UsuarioResponse;
import exception.ResourceNotFoundException;
import exception.UnauthorizedException;
import model.Usuario;
import model.enums.Rol;
import org.mindrot.jbcrypt.BCrypt;
import security.JwtUtil;
import service.interfaces.IUsuarioService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsuarioServiceImpl implements IUsuarioService {
    @Override
    public List<UsuarioResponse> listar(int pagina, int tamanioPagina) {
        return List.of();
    }

    @Override
    public UsuarioResponse guardar(LoginRequest request) {
        return null;
    }

    @Override
    public UsuarioResponse actualizar(int idUsuario, LoginRequest request) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String query = "SELECT * FROM tb_usuario WHERE correo = ? AND activo = 1 LIMIT 1";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Usuario usuarioBuscado = new Usuario();

        try {
            preparedStatement = ConnectorBD.getConexion().prepareStatement(query);
            preparedStatement.setString(1, loginRequest.correo());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                 usuarioBuscado.setIdUsuario(resultSet.getInt(1));
                 usuarioBuscado.setIdPersona(resultSet.getInt(2));
                 usuarioBuscado.setCorreo(resultSet.getString(3));
                 usuarioBuscado.setPasswd(resultSet.getString(4));
                 usuarioBuscado.setRol(Rol.valueOf(resultSet.getString(5)));
            }else{
                throw new ResourceNotFoundException("No se encontro el usuario con correo: " + loginRequest.correo());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando especialidad", e);
        }
        if (!BCrypt.checkpw(loginRequest.passwd(), usuarioBuscado.getPasswd())) {
            throw new UnauthorizedException("La contraseña no coincide");
        }
        String token = JwtUtil.generarToken(
                usuarioBuscado.getCorreo(),
                usuarioBuscado.getRol().name()
        );
        return LoginResponse.builder().token(token).build();
    }

    @Override
    public void eliminar() {

    }
}

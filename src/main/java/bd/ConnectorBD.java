package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorBD {
	public static String url = "jdbc:mysql://localhost:3306/bd_clinica";
	public static String usuario = "root";
	public static String password = "stefano";

	public static Connection getConexion() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			return DriverManager.getConnection(url, usuario, password);

		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException("Error al obtener conexión", e);
		}
	}
}

package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorBD {
	public static String url="jdbc:mysql://localhost:3306/bd_clinica";
	public static String usuario="root";
	public static String password="stefano";
	private static Connection cn;
	
	public static Connection getConexion(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	
			cn=DriverManager.getConnection(url,usuario,password);
				
			if(cn!=null) {
				System.out.print("Se realizo correctamente la conexion");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return cn;	
	} 
}

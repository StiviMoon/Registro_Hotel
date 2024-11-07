package BD;

import java.sql.*;

public class Conexion implements AutoCloseable {

    private Connection c;
    private Statement s;

    // Parámetros de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String USER = "root";
    private static final String PASSWORD = "001001";

    public Conexion() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            c = DriverManager.getConnection(URL, USER, PASSWORD);

            // Crear el statement
            s = c.createStatement();

        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return c;
    }

    // Método para obtener el Statement
    public Statement getStatement() {
        return s;
    }

    // Método para verificar si la base de datos está conectada
    public boolean isConnected() {
        try {
            // Verificar si la conexión está activa con una consulta simple
            if (c != null && !c.isClosed()) {
                ResultSet rs = s.executeQuery("SELECT 1");
                if (rs.next()) {
                    return true;  // La conexión está activa
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return false;  // La conexión no está activa
    }

    // Método que implementa AutoCloseable
    @Override
    public void close() {
        try {
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

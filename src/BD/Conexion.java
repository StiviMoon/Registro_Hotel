package BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conexion implements AutoCloseable {

    private Connection c;
    public Statement s;

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

    public List<Map<String, Object>> obtenerUsuarios() {
        List<Map<String, Object>> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, celular_principal FROM Persona";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("ID", rs.getInt("id"));
                usuario.put("Nombre", rs.getString("nombre"));
                usuario.put("Apellido", rs.getString("apellido"));
                usuario.put("Celular", rs.getString("celular_principal"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }

    // En la clase Conexion
    public boolean updateUsuario(int id, String nombre, String apellido, String celular) {
        String sql = "UPDATE Persona SET nombre = ?, apellido = ?, celular_principal = ? WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, celular);
            pstmt.setInt(4, id);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUsuario(int id) {
        String sql = "DELETE FROM Persona WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
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

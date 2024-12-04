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
        String sql = "SELECT id,   nombre, apellido, celular_principal, numero_documento FROM Persona";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("ID", rs.getInt("id"));
                usuario.put("Numero_Doc", rs.getInt("numero_documento"));
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

    public List<Map<String, Object>> obtenerUsuariosConFiltro(String filtro) {
        List<Map<String, Object>> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, celular_principal, numero_documento FROM Persona WHERE nombre LIKE ? OR apellido LIKE ? OR numero_documento LIKE ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            String filtroLike = "%" + filtro + "%";
            pstmt.setString(1, filtroLike);
            pstmt.setString(2, filtroLike);
            pstmt.setString(3, filtroLike);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("ID", rs.getInt("id"));
                    usuario.put("Numero_Doc", rs.getInt("numero_documento"));
                    usuario.put("Nombre", rs.getString("nombre"));
                    usuario.put("Apellido", rs.getString("apellido"));
                    usuario.put("Celular", rs.getString("celular_principal"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios con filtro de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }

    public boolean usuarioExiste(String numeroDocumento, String tipoDocumento) {
        String sql = "SELECT COUNT(*) AS total FROM Persona WHERE numero_documento = ? AND tipo_documento = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, numeroDocumento);
            pstmt.setString(2, tipoDocumento);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("total");
                    return count > 0;  // Retorna true si existe al menos una coincidencia
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar si el usuario existe: " + e.getMessage());
            e.printStackTrace();
        }
        return false;  // Si no se encontró el usuario o hubo un error, retorna false
    }

    // En la clase Conexion
    public boolean updateUsuario(int id, String nombre, String apellido, String celular, String numero_doc) {
        String sql = "UPDATE Persona SET nombre = ?, apellido = ?, celular_principal = ?, numero_documento = ? WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, celular);
            pstmt.setString(4, numero_doc);
            pstmt.setInt(5, id);

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
    public boolean deleteUsuarioCC(int cc) {
        String sql = "DELETE FROM Persona WHERE numero_documento = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, cc);

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
        }
    }


    public List<Map<String, Object>> obtenerHabitaciones() {
        List<Map<String, Object>> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM Habitaciones";
        try (Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> habitacion = new HashMap<>();
                habitacion.put("ID", rs.getInt("id"));
                habitacion.put("NumeroHabitacion", rs.getInt("numero_habitacion"));
                habitacion.put("Piso", rs.getInt("piso"));
                habitacion.put("Tipo", rs.getString("tipo"));
                habitacion.put("CostoPorNoche", rs.getDouble("costo_por_noche"));
                habitacion.put("MaxPersonas", rs.getInt("max_personas"));
                habitacion.put("MinPersonas", rs.getInt("min_personas"));
                habitacion.put("Ocupada", rs.getBoolean("ocupada"));
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones: " + e.getMessage());
        }
        return habitaciones;
    }


    public List<Map<String, Object>> obtenerHabitacionesReservadas() {
        List<Map<String, Object>> habitacionesReservadas = new ArrayList<>();
        String sql = "SELECT id, numero_habitacion, piso, tipo, costo_por_noche, max_personas, min_personas, ocupada "
                + "FROM Habitaciones WHERE ocupada = TRUE";  // Solo habitaciones ocupadas

        try (PreparedStatement pstmt = c.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> habitacion = new HashMap<>();
                habitacion.put("ID", rs.getInt("id"));
                habitacion.put("NumeroHabitacion", rs.getInt("numero_habitacion"));
                habitacion.put("Piso", rs.getInt("piso"));
                habitacion.put("Tipo", rs.getString("tipo"));
                habitacion.put("CostoPorNoche", rs.getDouble("costo_por_noche"));
                habitacion.put("MaxPersonas", rs.getInt("max_personas"));
                habitacion.put("MinPersonas", rs.getInt("min_personas"));
                habitacion.put("Ocupada", rs.getBoolean("ocupada"));
                habitacionesReservadas.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones reservadas: " + e.getMessage());
            e.printStackTrace();
        }

        return habitacionesReservadas;
    }




    // Método para obtener habitaciones ocupadas o desocupadas
    public List<Map<String, Object>> obtenerHabitacionesPorEstado(boolean ocupada) {
        List<Map<String, Object>> habitaciones = new ArrayList<>();
        String sql = "SELECT id, numero_habitacion, piso, tipo_habitacion, costo_por_noche, max_personas, min_personas, ocupada FROM habitaciones WHERE ocupada = ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setBoolean(1, ocupada);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> habitacion = new HashMap<>();
                    habitacion.put("ID", rs.getInt("id"));
                    habitacion.put("NumeroHabitacion", rs.getString("numero_habitacion"));
                    habitacion.put("Piso", rs.getInt("piso"));
                    habitacion.put("Tipo", rs.getString("tipo_habitacion"));
                    habitacion.put("CostoPorNoche", rs.getBigDecimal("costo_por_noche"));
                    habitacion.put("MaxPersonas", rs.getInt("max_personas"));
                    habitacion.put("MinPersonas", rs.getInt("min_personas"));
                    habitacion.put("Ocupada", rs.getBoolean("ocupada"));
                    habitaciones.add(habitacion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones por estado: " + e.getMessage());
        }

        return habitaciones;
    }

    // Método para cambiar el estado de una habitación
    public boolean cambiarEstadoHabitacion(int id, boolean nuevoEstado) {
        String sql = "UPDATE habitaciones SET ocupada = ? WHERE id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setBoolean(1, nuevoEstado);
            pstmt.setInt(2, id);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar el estado de la habitación: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarDisponibilidadHabitacion(String idHabitacion) {
        String sql = "SELECT ocupada FROM Habitaciones WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, idHabitacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return !rs.getBoolean("ocupada"); // Retorna true si no está ocupada
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
        }
        return false; // Si no se encuentra la habitación o hay error, retorna false
    }



    public Object[][] obtenerHabitacionesOcupadas() {
        String sql = "SELECT id, piso, numero_habitacion, ocupada FROM habitaciones WHERE ocupada = 1";
        List<Object[]> habitaciones = new ArrayList<>();

        try (PreparedStatement pstmt = c.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Agregar cada fila como un arreglo de objetos
                habitaciones.add(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("piso"),
                        rs.getString("numero_habitacion"),
                        rs.getBoolean("ocupada")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones ocupadas: " + e.getMessage());
        }

        // Convertir la lista en un arreglo bidimensional
        return habitaciones.toArray(new Object[0][0]);
    }



    public boolean reservarHabitacion(String idHabitacion) {
        String sql = "UPDATE Habitaciones SET ocupada = ? WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setBoolean(1, true); // Marca la habitación como ocupada
            pstmt.setString(2, idHabitacion);

            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0; // Retorna true si se actualizó correctamente
        } catch (SQLException e) {
            System.err.println("Error al reservar la habitación: " + e.getMessage());
        }
        return false;
    }





    public List<Map<String, Object>> obtenerHabitacionesPorTipo(String tipoHabitacion) {
        List<Map<String, Object>> habitaciones = new ArrayList<>();
        String sql = tipoHabitacion.equalsIgnoreCase("Todos")
                ? "SELECT id, numero_habitacion, piso, tipo_habitacion, costo_por_noche, max_personas, min_personas, ocupada FROM habitaciones"
                : "SELECT id, numero_habitacion, piso, tipo_habitacion, costo_por_noche, max_personas, min_personas, ocupada FROM habitaciones WHERE tipo_habitacion = ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            if (!tipoHabitacion.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, tipoHabitacion);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> habitacion = new HashMap<>();
                    habitacion.put("ID", rs.getInt("id"));
                    habitacion.put("NumeroHabitacion", rs.getString("numero_habitacion"));
                    habitacion.put("Piso", rs.getInt("piso"));
                    habitacion.put("Tipo", rs.getString("tipo_habitacion"));
                    habitacion.put("CostoPorNoche", rs.getBigDecimal("costo_por_noche"));
                    habitacion.put("MaxPersonas", rs.getInt("max_personas"));
                    habitacion.put("MinPersonas", rs.getInt("min_personas"));
                    habitacion.put("Ocupada", rs.getBoolean("ocupada"));
                    habitaciones.add(habitacion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones filtradas: " + e.getMessage());
            e.printStackTrace();
        }

        return habitaciones;
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

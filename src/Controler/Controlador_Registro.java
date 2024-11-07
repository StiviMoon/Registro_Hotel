package Controler;

import BD.Conexion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Controlador_Registro {
    private Map<String, String[]> ciudadesPorDepartamento;

    public Controlador_Registro() {
        inicializarCiudades();
    }

    // Método para guardar el registro, ahora con Map<String, Object>
    public void guardarRegistro(Map<String, Object> datosUsuario) {
        // Obtener y validar los valores de los campos desde el mapa (realizamos casting a String cuando es necesario)
        String tipoDocumento = (String) datosUsuario.get("Tipo de documento");
        String numeroDocumento = (String) datosUsuario.get("Número de documento:");
        String nombre = (String) datosUsuario.get("Nombre:");
        String apellido = (String) datosUsuario.get("Apellido:");
        String fechaNacimiento = (String) datosUsuario.get("Fecha de nacimiento:");
        String direccion = (String) datosUsuario.get("Dirección:");
        String telefonoFijo = (String) datosUsuario.get("Teléfono fijo:");
        String celularPrincipal = (String) datosUsuario.get("Celular principal:");
        String celularSecundario = (String) datosUsuario.get("Celular secundario:");
        String paisResidencia = (String) datosUsuario.get("País");
        String departamentoResidencia = (String) datosUsuario.get("Departamento");
        String ciudadResidencia = (String) datosUsuario.get("Ciudad");
        String ocupacion = (String) datosUsuario.get("Ocupación:");
        String telefonoEmpresa = (String) datosUsuario.get("Teléfono empresa:");

        // Mostrar los valores obtenidos como prueba
        System.out.println("Tipo de documento: " + tipoDocumento);
        System.out.println("Número de documento: " + numeroDocumento);
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Fecha de nacimiento: " + fechaNacimiento);
        System.out.println("Dirección: " + direccion);
        System.out.println("Teléfono fijo: " + telefonoFijo);
        System.out.println("Celular principal: " + celularPrincipal);
        System.out.println("Celular secundario: " + celularSecundario);
        System.out.println("País de residencia: " + paisResidencia);
        System.out.println("Departamento de residencia: " + departamentoResidencia);
        System.out.println("Ciudad de residencia: " + ciudadResidencia);
        System.out.println("Ocupación: " + ocupacion);
        System.out.println("Teléfono empresa: " + telefonoEmpresa);
    }

    public void enviarRegistroABaseDeDatos(Map<String, Object> datosUsuario) {
        String tipoDocumento = (String) datosUsuario.get("Tipo de documento");
        String numeroDocumento = (String) datosUsuario.get("Número de documento:");
        String nombre = (String) datosUsuario.get("Nombre:");
        String apellido = (String) datosUsuario.get("Apellido:");
        String fechaNacimiento = (String) datosUsuario.get("Fecha de nacimiento:");
        String direccion = (String) datosUsuario.get("Dirección:");
        String telefonoFijo = (String) datosUsuario.get("Teléfono fijo:");
        String celularPrincipal = (String) datosUsuario.get("Celular principal:");
        String celularSecundario = (String) datosUsuario.get("Celular secundario:");
        String paisResidencia = (String) datosUsuario.get("País");
        String departamentoResidencia = (String) datosUsuario.get("Departamento");
        String ciudadResidencia = (String) datosUsuario.get("Ciudad");
        String ocupacion = (String) datosUsuario.get("Ocupación:");
        String telefonoEmpresa = (String) datosUsuario.get("Teléfono empresa:");

        String sql = "INSERT INTO Persona (tipo_documento, numero_documento, nombre, apellido, direccion, " +
                "telefono_fijo, celular_principal, celular_secundario, ciudad_residencia, " +
                "pais_residencia, ocupacion, telefono_empresa, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Conexion conexion = new Conexion();
             Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignación de valores usando el mapa de datos
            pstmt.setString(1, (String) tipoDocumento);
            pstmt.setString(2, (String) numeroDocumento);
            pstmt.setString(3, (String) nombre);
            pstmt.setString(4, (String) apellido);
            pstmt.setString(5, (String) direccion);
            pstmt.setString(6, (String) telefonoFijo);
            pstmt.setString(7, (String) celularPrincipal);
            pstmt.setString(8, (String) celularSecundario);
            pstmt.setString(9, (String) ciudadResidencia);
            pstmt.setString(10, (String) paisResidencia);
            pstmt.setString(11, (String) ocupacion);
            pstmt.setString(12, (String) telefonoEmpresa);

            // Convierte el valor de la fecha de nacimiento usando SimpleDateFormat
            String fechaNacimientoStr = fechaNacimiento;
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaUtil;

            try {
                fechaUtil = formatoFecha.parse(fechaNacimientoStr);
                java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());
                pstmt.setDate(13, fechaSql);
            } catch (ParseException e) {
                System.err.println("Error al parsear la fecha de nacimiento: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            // Ejecuta la inserción
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registro insertado correctamente en la tabla Persona.");
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el registro en la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Método para inicializar las ciudades por departamento
    public void inicializarCiudades() {
        ciudadesPorDepartamento = new HashMap<>();
        ciudadesPorDepartamento.put("Cundinamarca", new String[]{"Bogotá", "Soacha"});
        ciudadesPorDepartamento.put("Antioquia", new String[]{"Medellín", "Bello"});
        ciudadesPorDepartamento.put("Valle del Cauca", new String[]{"Cali", "Palmira"});
        ciudadesPorDepartamento.put("CDMX", new String[]{"Ciudad de México"});
        ciudadesPorDepartamento.put("Jalisco", new String[]{"Guadalajara", "Zapopan"});
        ciudadesPorDepartamento.put("Nuevo León", new String[]{"Monterrey", "San Nicolás"});
        ciudadesPorDepartamento.put("Buenos Aires", new String[]{"La Plata", "Mar del Plata"});
        ciudadesPorDepartamento.put("CABA", new String[]{"Buenos Aires"});
        ciudadesPorDepartamento.put("Mendoza", new String[]{"Mendoza"});
    }

    // Método para actualizar los departamentos según el país seleccionado
    public void actualizarDepartamentos(JComboBox<String> departamentoComboBox, String pais) {
        departamentoComboBox.removeAllItems();
        if ("Colombia".equals(pais)) {
            departamentoComboBox.addItem("Cundinamarca");
            departamentoComboBox.addItem("Antioquia");
            departamentoComboBox.addItem("Valle del Cauca");
        } else if ("México".equals(pais)) {
            departamentoComboBox.addItem("CDMX");
            departamentoComboBox.addItem("Jalisco");
            departamentoComboBox.addItem("Nuevo León");
        } else if ("Argentina".equals(pais)) {
            departamentoComboBox.addItem("Buenos Aires");
            departamentoComboBox.addItem("CABA");
            departamentoComboBox.addItem("Mendoza");
        }
    }

    // Método para actualizar las ciudades según el departamento seleccionado
    public void actualizarCiudades(JComboBox<String> ciudadComboBox, String departamento) {
        ciudadComboBox.removeAllItems();
        String[] ciudades = ciudadesPorDepartamento.getOrDefault(departamento, new String[]{});
        for (String ciudad : ciudades) {
            ciudadComboBox.addItem(ciudad);
        }
    }

    // Método para agregar un label y un JTextField al panel
    public void addLabelAndTextField(JPanel panel, String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 150, 30);
        JTextField textField = new JTextField();
        textField.setBounds(x + 150, y, 150, 30);
        panel.add(label);
        panel.add(textField);
    }
}

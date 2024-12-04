package Vista;

import BD.Conexion;
import Controler.Controlador_Registro;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Controler.PanelReservas;
import com.toedter.calendar.JDateChooser;

public class Registro extends JPanel {
    private JComboBox<String> registrationComboBox;
    private JPanel registeredPanel;
    private JPanel unregisteredPanel;
    private CardLayout cardLayout;
    private Controlador_Registro controladorRegistro;
    private Conexion conexion;
    private JComboBox<String> paisComboBox;
    private JComboBox<String> departamentoComboBox;
    private JComboBox<String> ciudadComboBox;
    private Map<String, JTextField> textFieldsMap;
    private JComboBox<String> tipoDocComboBoxUnregistered;
    private JDateChooser fechaNacimientoChooser;

    public Registro() {

        this.conexion = new Conexion();  // Inicializa la conexión aquí
        setLayout(null);
        setPreferredSize(new Dimension(600, 800));
        this.controladorRegistro = new Controlador_Registro();
        controladorRegistro.inicializarCiudades();
        textFieldsMap = new HashMap<>();

        paisComboBox = new JComboBox<>(new String[]{"Selecciona", "Colombia", "México", "Argentina"});
        departamentoComboBox = new JComboBox<>(new String[]{"Selecciona"});
        ciudadComboBox = new JComboBox<>(new String[]{"Selecciona"});

        JPanel comboBoxPanel = new JPanel(null);
        comboBoxPanel.setBounds(10, 10, 580, 50);
        comboBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel comboBoxLabel = new JLabel("Seleccione el estado de registro:");
        comboBoxLabel.setBounds(10, 10, 200, 30);
        String[] options = {"Seleccionar", "Registrado", "No Registrado"};
        registrationComboBox = new JComboBox<>(options);
        registrationComboBox.setBounds(220, 10, 200, 30);
        comboBoxPanel.add(comboBoxLabel);
        comboBoxPanel.add(registrationComboBox);

        JPanel mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setBounds(10, 70, 580, 680);

        registeredPanel = new JPanel(null);
        JLabel registeredLabel = new JLabel("Si está registrado en el hotel");
        registeredLabel.setBounds(10, 10, 200, 30);
        registeredPanel.add(registeredLabel);

        // Código de creación de componentes
        JLabel tipoDocLabel = new JLabel("Tipo de documento:");
        tipoDocLabel.setBounds(10, 50, 150, 30);
        String[] tipoDocOptions = {"Selecciona", "Cédula", "NIT"};
        JComboBox<String> tipoDocComboBox = new JComboBox<>(tipoDocOptions);
        tipoDocComboBox.setBounds(160, 50, 150, 30);
        registeredPanel.add(tipoDocLabel);
        registeredPanel.add(tipoDocComboBox);

        addLabelAndTextField(registeredPanel, "Número de documento R:", 10, 90);

        JButton submitButton2 = new JButton("Validar");
        submitButton2.setBounds(200, 540, 150, 30);
        registeredPanel.add(submitButton2);


        unregisteredPanel = new JPanel(null);
        JLabel tipoDocLabelUnregistered = new JLabel("Tipo de documento:");
        tipoDocLabelUnregistered.setBounds(10, 10, 150, 30);
        tipoDocComboBoxUnregistered = new JComboBox<>(tipoDocOptions);
        tipoDocComboBoxUnregistered.setBounds(160, 10, 150, 30);
        unregisteredPanel.add(tipoDocLabelUnregistered);
        unregisteredPanel.add(tipoDocComboBoxUnregistered);

        addLabelAndTextField(unregisteredPanel, "Número de documento:", 10, 50);
        addLabelAndTextField(unregisteredPanel, "Dirección:", 10, 90);
        addLabelAndTextField(unregisteredPanel, "Nombre:", 10, 130);
        addLabelAndTextField(unregisteredPanel, "Apellido:", 10, 170);

        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento:");
        lblFechaNacimiento.setBounds(10, 210, 200, 30); // Posición y tamaño
        unregisteredPanel.add(lblFechaNacimiento);

        fechaNacimientoChooser = new JDateChooser();
        fechaNacimientoChooser.setBounds(160, 210, 150, 30);
        unregisteredPanel.add(fechaNacimientoChooser);

        addLabelAndTextField(unregisteredPanel, "Teléfono fijo:", 10, 250);
        addLabelAndTextField(unregisteredPanel, "Celular principal:", 10, 290);
        addLabelAndTextField(unregisteredPanel, "Celular secundario:", 10, 330);
        addLabelAndTextField(unregisteredPanel, "Ocupación:", 10, 370);

        JLabel paisLabel = new JLabel("País:");
        paisLabel.setBounds(10, 410, 150, 30);
        paisComboBox.setBounds(160, 410, 150, 30);
        paisComboBox.addActionListener(e -> controladorRegistro.actualizarDepartamentos(departamentoComboBox, (String) paisComboBox.getSelectedItem()));
        unregisteredPanel.add(paisLabel);
        unregisteredPanel.add(paisComboBox);

        JLabel departamentoLabel = new JLabel("Departamento:");
        departamentoLabel.setBounds(10, 450, 150, 30);
        departamentoComboBox.setBounds(160, 450, 150, 30);
        departamentoComboBox.addActionListener(e -> controladorRegistro.actualizarCiudades(ciudadComboBox, (String) departamentoComboBox.getSelectedItem()));
        unregisteredPanel.add(departamentoLabel);
        unregisteredPanel.add(departamentoComboBox);

        JLabel ciudadLabel = new JLabel("Ciudad de residencia:");
        ciudadLabel.setBounds(10, 490, 150, 30);
        ciudadComboBox.setBounds(160, 490, 150, 30);
        unregisteredPanel.add(ciudadLabel);
        unregisteredPanel.add(ciudadComboBox);

        addLabelAndTextField(unregisteredPanel, "Teléfono empresa:", 10, 530);

        JButton submitButton = new JButton("Enviar");
        submitButton.setBounds(200, 580, 150, 30);
        unregisteredPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            if (esMayorDeEdad()) {
                Map<String, Object> datosUsuario = obtenerDatosUsuario(tipoDocComboBoxUnregistered);
                controladorRegistro.guardarRegistro(datosUsuario);
                controladorRegistro.enviarRegistroABaseDeDatos(datosUsuario);
                JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");
                limpiarCampos(); // Limpia los campos después de guardar los datos
            } else {
                JOptionPane.showMessageDialog(null, "Debe ser mayor de edad para registrarse.");
            }
        });

        // Acción del botón "Validar"
        submitButton2.addActionListener(e -> {
            String tipoDocSeleccionado = (String) tipoDocComboBox.getSelectedItem();
            String numeroDocumento = textFieldsMap.get("Número de documento R:").getText(); // Obtener el texto del campo


            if (numeroDocumento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese el número de documento.");
            } else {
                // Llama al método usuarioExiste en Controlador_Registro
                boolean usuarioExiste = conexion.usuarioExiste(numeroDocumento, tipoDocSeleccionado);

                if (usuarioExiste) {
                    JOptionPane.showMessageDialog(null, "El usuario ya está registrado.");
                    JOptionPane.showMessageDialog(null, "Bienvenido");
                    PanelReservas reserva = new PanelReservas();
                    reserva.mostrarPanelReservas();



                    // Limpiar campos después de la validación
                    textFieldsMap.get("Número de documento R:").setText("");
                    tipoDocComboBox.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario no está registrado.");

                    // Limpiar campos después de la validación
                    textFieldsMap.get("Número de documento R:").setText("");
                    tipoDocComboBox.setSelectedIndex(0);
                }
            }
        });



        mainPanel.add(new JPanel(), "defaultPanel");
        mainPanel.add(registeredPanel, "registeredPanel");
        mainPanel.add(unregisteredPanel, "unregisteredPanel");

        registrationComboBox.addActionListener(e -> {
            if (registrationComboBox.getSelectedItem().equals("Registrado")) {
                cardLayout.show(mainPanel, "registeredPanel");
            } else if (registrationComboBox.getSelectedItem().equals("No Registrado")) {
                cardLayout.show(mainPanel, "unregisteredPanel");
            }
        });

        add(comboBoxPanel);
        add(mainPanel);
        cardLayout.show(mainPanel, "defaultPanel");
    }

    private void addLabelAndTextField(JPanel panel, String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 150, 30);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(x + 150, y, 150, 30);
        panel.add(textField);

        textFieldsMap.put(labelText, textField);
    }

    private boolean esMayorDeEdad() {
        Date fechaNacimiento = fechaNacimientoChooser.getDate(); // Obtener la fecha del JDateChooser

        if (fechaNacimiento != null) {
            LocalDate fechaNacimientoLocal = LocalDate.ofInstant(fechaNacimiento.toInstant(), java.time.ZoneId.systemDefault());
            LocalDate fechaActual = LocalDate.now();
            long edad = ChronoUnit.YEARS.between(fechaNacimientoLocal, fechaActual);

            return edad >= 18;
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fecha de nacimiento.");
            return false;
        }
    }

    private Map<String, Object> obtenerDatosUsuario(JComboBox<String> tipoDocComboBox) {
        Map<String, Object> datos = new HashMap<>();

        // Obtener los datos de los campos de texto
        for (String key : textFieldsMap.keySet()) {
            datos.put(key, textFieldsMap.get(key).getText());
        }

        // Obtener el valor seleccionado del tipo de documento
        datos.put("Tipo de documento", tipoDocComboBox.getSelectedItem());

        // Obtener los valores de los ComboBoxes
        datos.put("País", paisComboBox.getSelectedItem());
        datos.put("Departamento", departamentoComboBox.getSelectedItem());
        datos.put("Ciudad", ciudadComboBox.getSelectedItem());

        // Obtener la fecha seleccionada en el JDateChooser
        Date fechaNacimiento = fechaNacimientoChooser.getDate();
        if (fechaNacimiento != null) {
            // Guardar la fecha como Date
            datos.put("Fecha de nacimiento", fechaNacimiento);

            // Formatear la fecha en el formato deseado
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formato.format(fechaNacimiento);

            // Mostrar la fecha formateada
            System.out.println("Fecha seleccionada: " + fechaFormateada);

            // Guardar la fecha formateada en el mapa con una clave específica
            datos.put("Fecha de nacimiento", fechaFormateada);
        } else {
            datos.put("Fecha de nacimiento", "Fecha no seleccionada");
        }

        System.out.println(datos);

        return datos;
    }


    private void limpiarCampos() {
        // Limpiar todos los campos de texto
        for (JTextField field : textFieldsMap.values()) {
            if (field != null) {
                field.setText("");
            }
        }

        // Verificar y limpiar los JComboBox si contienen elementos
        if (tipoDocComboBoxUnregistered != null && tipoDocComboBoxUnregistered.getItemCount() > 0) {
            tipoDocComboBoxUnregistered.setSelectedIndex(0);
        }
        if (paisComboBox != null && paisComboBox.getItemCount() > 0) {
            paisComboBox.setSelectedIndex(0);
        }
        if (departamentoComboBox != null && departamentoComboBox.getItemCount() > 0) {
            departamentoComboBox.setSelectedIndex(0);
        }
        if (ciudadComboBox != null && ciudadComboBox.getItemCount() > 0) {
            ciudadComboBox.setSelectedIndex(0);
        }

        // Limpiar el selector de fecha si no es null
        if (fechaNacimientoChooser != null) {
            fechaNacimientoChooser.setDate(null);
        }
    }

}

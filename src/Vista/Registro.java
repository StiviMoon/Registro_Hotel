package Vista;

import Controler.Controlador_Registro;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Registro extends JPanel {
    private JComboBox<String> registrationComboBox;
    private JPanel registeredPanel;
    private JPanel unregisteredPanel;
    private CardLayout cardLayout;
    private Controlador_Registro controladorRegistro;
    private JComboBox<String> paisComboBox;
    private JComboBox<String> departamentoComboBox;
    private JComboBox<String> ciudadComboBox;
    private Map<String, JTextField> textFieldsMap;

    public Registro() {
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

        JLabel tipoDocLabel = new JLabel("Tipo de documento:");
        tipoDocLabel.setBounds(10, 50, 150, 30);
        String[] tipoDocOptions = {"Selecciona", "Cédula", "NIT"};
        JComboBox<String> tipoDocComboBox = new JComboBox<>(tipoDocOptions);
        tipoDocComboBox.setBounds(160, 50, 150, 30);
        registeredPanel.add(tipoDocLabel);
        registeredPanel.add(tipoDocComboBox);

        addLabelAndTextField(registeredPanel, "Número de documento:", 10, 90);

        JButton submitButton1 = new JButton("Enviar");
        submitButton1.setBounds(200, 540, 150, 30);
        registeredPanel.add(submitButton1);

        unregisteredPanel = new JPanel(null);
        JLabel tipoDocLabelUnregistered = new JLabel("Tipo de documento:");
        tipoDocLabelUnregistered.setBounds(10, 10, 150, 30);
        JComboBox<String> tipoDocComboBoxUnregistered = new JComboBox<>(tipoDocOptions);
        tipoDocComboBoxUnregistered.setBounds(160, 10, 150, 30);
        unregisteredPanel.add(tipoDocLabelUnregistered);
        unregisteredPanel.add(tipoDocComboBoxUnregistered);

        addLabelAndTextField(unregisteredPanel, "Número de documento:", 10, 50);
        addLabelAndTextField(unregisteredPanel, "Dirección:", 10, 90);
        addLabelAndTextField(unregisteredPanel, "Nombre:", 10, 130);
        addLabelAndTextField(unregisteredPanel, "Apellido:", 10, 170);
        addLabelAndTextField(unregisteredPanel, "Fecha de nacimiento:", 10, 210);
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
            if (esMayorDeEdad()){
                Map<String, Object> datosUsuario = obtenerDatosUsuario(tipoDocComboBoxUnregistered);
                controladorRegistro.guardarRegistro(datosUsuario);
                controladorRegistro.enviarRegistroABaseDeDatos(datosUsuario);
                JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");
            }else {
                JOptionPane.showMessageDialog(null, "Debe ser mayor de edad para registrarse.");
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
        String fechaNacimientoTexto = textFieldsMap.get("Fecha de nacimiento:").getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Ajusta el formato según el ingreso

        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoTexto, formatter);
            LocalDate fechaActual = LocalDate.now();
            long edad = ChronoUnit.YEARS.between(fechaNacimiento, fechaActual);

            return edad >= 18;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Usa dd/MM/yyyy.");
            return false;
        }
    }

    private Map<String, Object> obtenerDatosUsuario(JComboBox<String> tipoDocComboBox) {
        Map<String, Object> datos = new HashMap<>();

        for (String key : textFieldsMap.keySet()) {
            datos.put(key, textFieldsMap.get(key).getText());
        }

        datos.put("Tipo de documento", tipoDocComboBox.getSelectedItem());
        datos.put("País", paisComboBox.getSelectedItem());
        datos.put("Departamento", departamentoComboBox.getSelectedItem());
        datos.put("Ciudad", ciudadComboBox.getSelectedItem());
        System.out.println(datos);
        return datos;
    }
}

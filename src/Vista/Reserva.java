package Vista;

import Controler.Controlador_Registro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Reserva extends JPanel {
    private JPanel mainPanel;
    private JComboBox<String> registrationComboBox;
    private JPanel registeredPanel;
    private JPanel unregisteredPanel;
    private CardLayout cardLayout;
    private Controlador_Registro controladorRegistro;

    private JComboBox<String> paisComboBox;
    private JComboBox<String> departamentoComboBox;
    private JComboBox<String> ciudadComboBox;

    // Mapa para almacenar ciudades por departamento
    private Map<String, String[]> ciudadesPorDepartamento;

    public Reserva() {
        setLayout(null);
        setPreferredSize(new Dimension(600, 800));
        this.controladorRegistro = new Controlador_Registro();
        inicializarCiudades();

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

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setBounds(10, 70, 580, 680);

        // Panel para usuarios registrados
        registeredPanel = new JPanel(null);
        JLabel registeredLabel = new JLabel("Si está registrado en el hotel");
        registeredLabel.setBounds(10, 10, 200, 30);
        registeredPanel.add(registeredLabel);

        // JComboBox para seleccionar tipo de documento
        JLabel tipoDocLabel = new JLabel("Tipo de documento:");
        tipoDocLabel.setBounds(10, 50, 150, 30);
        String[] tipoDocOptions = {"Cédula", "NIT"};
        JComboBox<String> tipoDocComboBox = new JComboBox<>(tipoDocOptions);
        tipoDocComboBox.setBounds(160, 50, 150, 30);
        registeredPanel.add(tipoDocLabel);
        registeredPanel.add(tipoDocComboBox);

        // Campos para el número de documento
        controladorRegistro.addLabelAndTextField(registeredPanel, "Número de documento:", 10, 90);

        JButton submitButton1 = new JButton("Enviar");
        submitButton1.setBounds(200, 540, 150, 30);
        registeredPanel.add(submitButton1);

        // Panel para usuarios no registrados
        unregisteredPanel = new JPanel(null);

        // JComboBox para seleccionar tipo de documento
        JLabel tipoDocLabelUnregistered = new JLabel("Tipo de documento:");
        tipoDocLabelUnregistered.setBounds(10, 10, 150, 30);
        JComboBox<String> tipoDocComboBoxUnregistered = new JComboBox<>(tipoDocOptions);
        tipoDocComboBoxUnregistered.setBounds(160, 10, 150, 30);
        unregisteredPanel.add(tipoDocLabelUnregistered);
        unregisteredPanel.add(tipoDocComboBoxUnregistered);

        // Campos adicionales para los usuarios no registrados
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Número de documento:", 10, 50);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Dirección:", 10, 90);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Nombre:", 10, 130);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Apellido:", 10, 170);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Fecha de nacimiento:", 10, 210);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Teléfono fijo:", 10, 250);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Celular principal:", 10, 290);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Celular secundario:", 10, 330);
        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Ocupación:", 10, 370);

        // Campos para País, Departamento y Ciudad
        JLabel paisLabel = new JLabel("País:");
        paisLabel.setBounds(10, 410, 150, 30);
        paisComboBox = new JComboBox<>(new String[]{"Seleccionar", "Colombia", "México", "Argentina"});
        paisComboBox.setBounds(160, 410, 150, 30);
        paisComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDepartamentos((String) paisComboBox.getSelectedItem());
            }
        });
        unregisteredPanel.add(paisLabel);
        unregisteredPanel.add(paisComboBox);

        JLabel departamentoLabel = new JLabel("Departamento:");
        departamentoLabel.setBounds(10, 450, 150, 30);
        departamentoComboBox = new JComboBox<>();
        departamentoComboBox.setBounds(160, 450, 150, 30);
        departamentoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCiudades((String) departamentoComboBox.getSelectedItem());
            }
        });
        unregisteredPanel.add(departamentoLabel);
        unregisteredPanel.add(departamentoComboBox);

        JLabel ciudadLabel = new JLabel("Ciudad de residencia:");
        ciudadLabel.setBounds(10, 490, 150, 30);
        ciudadComboBox = new JComboBox<>();
        ciudadComboBox.setBounds(160, 490, 150, 30);
        unregisteredPanel.add(ciudadLabel);
        unregisteredPanel.add(ciudadComboBox);

        controladorRegistro.addLabelAndTextField(unregisteredPanel, "Teléfono empresa:", 10, 530);

        JButton submitButton = new JButton("Enviar");
        submitButton.setBounds(200, 580, 150, 30);
        unregisteredPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Datos enviados correctamente.");
            }
        });

        mainPanel.add(new JPanel(), "defaultPanel");
        mainPanel.add(registeredPanel, "registeredPanel");
        mainPanel.add(unregisteredPanel, "unregisteredPanel");

        registrationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (registrationComboBox.getSelectedItem().equals("Registrado")) {
                    cardLayout.show(mainPanel, "registeredPanel");
                } else if (registrationComboBox.getSelectedItem().equals("No Registrado")) {
                    cardLayout.show(mainPanel, "unregisteredPanel");
                }
            }
        });

        add(comboBoxPanel);
        add(mainPanel);
        cardLayout.show(mainPanel, "defaultPanel");
    }

    private void inicializarCiudades() {
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

    private void actualizarDepartamentos(String pais) {
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

    private void actualizarCiudades(String departamento) {
        ciudadComboBox.removeAllItems();
        String[] ciudades = ciudadesPorDepartamento.getOrDefault(departamento, new String[]{});
        for (String ciudad : ciudades) {
            ciudadComboBox.addItem(ciudad);
        }
    }
}

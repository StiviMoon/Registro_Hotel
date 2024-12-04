package Controler;

import BD.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class PanelReservas extends JFrame {

    private JTable tablaHabitaciones;
    private JComboBox<String> comboFiltro;
    private JButton btnFiltrar, btnReservar,btnVerReserva;
    private DefaultTableModel modeloTabla;

    public PanelReservas() {
        setLayout(new BorderLayout());

        // Título del panel
        JLabel lblTitulo = new JLabel("Gestión de Reservas de Habitaciones", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentro = new JPanel(new BorderLayout());

        // Crear la tabla con un modelo
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Número", "Piso", "Tipo", "Costo/Noche", "Máx. Personas", "Mín. Personas", "Ocupada"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHabitaciones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaHabitaciones);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Panel de filtros y botones
        JPanel panelInferior = new JPanel(new FlowLayout());

        // Filtro de tipo de habitación
        comboFiltro = new JComboBox<>(new String[]{"Todos", "Familiar", "Doble", "Sencilla"});
        panelInferior.add(new JLabel("Tipo de habitación:"));
        panelInferior.add(comboFiltro);

        // Botones
        btnFiltrar = new JButton("Filtrar");
        btnReservar = new JButton("Reservar");
        btnVerReserva = new JButton("Ver Reservas");
        panelInferior.add(btnFiltrar);
        panelInferior.add(btnReservar);
        panelInferior.add(btnVerReserva);



        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnFiltrar.addActionListener(e -> filtrarHabitaciones());
        btnReservar.addActionListener(e -> reservarHabitacion());
        btnVerReserva.addActionListener(e -> mostrarHabitacionesEnTabla());
    }

    private void mostrarHabitacionesEnTabla() {
        Conexion conexion = new Conexion();
        Object[][] datos = conexion.obtenerHabitacionesOcupadas();

        // Definir los nombres de las columnas
        String[] columnas = {"ID", "Piso", "Número de Habitación", "Ocupada"};

        // Crear un modelo de tabla con los datos y las columnas
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas);

        // Crear el JTable
        JTable tabla = new JTable(modelo);

        // Mostrar la tabla en un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabla);
        JFrame frame = new JFrame("Habitaciones Ocupadas");
        frame.add(scrollPane);
        frame.setSize(500, 300);

        frame.setVisible(true);
    }


    // Método para cargar las habitaciones desde la base de datos
    public void cargarHabitacionesDesdeBD() {
        try (Conexion conexion = new Conexion()) {
            List<Map<String, Object>> habitaciones = conexion.obtenerHabitaciones();
            modeloTabla.setRowCount(0); // Limpiar la tabla

            for (Map<String, Object> habitacion : habitaciones) {
                modeloTabla.addRow(new Object[]{
                        habitacion.get("ID"),
                        habitacion.get("NumeroHabitacion"),
                        habitacion.get("Piso"),
                        habitacion.get("Tipo"),
                        habitacion.get("CostoPorNoche"),
                        habitacion.get("MaxPersonas"),
                        habitacion.get("MinPersonas"),
                        (boolean) habitacion.get("Ocupada") ? "Sí" : "No"
                });
            }
        }
    }


    // Método para filtrar habitaciones
    private void filtrarHabitaciones() {
        String filtro = comboFiltro.getSelectedItem().toString();
        try (Conexion conexion = new Conexion()) {
            List<Map<String, Object>> habitaciones = conexion.obtenerHabitacionesPorTipo(filtro);
            modeloTabla.setRowCount(0); // Limpiar la tabla

            for (Map<String, Object> habitacion : habitaciones) {
                modeloTabla.addRow(new Object[]{
                        habitacion.get("ID"),
                        habitacion.get("NumeroHabitacion"),
                        habitacion.get("Piso"),
                        habitacion.get("Tipo"),
                        habitacion.get("CostoPorNoche"),
                        habitacion.get("MaxPersonas"),
                        habitacion.get("MinPersonas"),
                        (boolean) habitacion.get("Ocupada") ? "Sí" : "No"
                });
            }

            JOptionPane.showMessageDialog(this, "Filtro aplicado: " + filtro);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar habitaciones: " + e.getMessage());
        }
    }


    // Método para reservar la habitación seleccionada
    // Método para reservar la habitación seleccionada
    private void reservarHabitacion() {
        int filaSeleccionada = tablaHabitaciones.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una habitación para continuar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaHabitaciones.getModel();

            String idHabitacion = modelo.getValueAt(filaSeleccionada, 0).toString();
            int piso = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 2).toString());
            String tipo = modelo.getValueAt(filaSeleccionada, 3).toString();
            int maxPersonas = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 5).toString());
            int minPersonas = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 6).toString());
            boolean ocupada = modelo.getValueAt(filaSeleccionada, 7).toString().equalsIgnoreCase("Sí");

            if (ocupada) {
                JOptionPane.showMessageDialog(this, "La habitación seleccionada ya está reservada.", "Habitación no disponible", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Solicitar número de personas al usuario
            String inputPersonas = JOptionPane.showInputDialog(this, "¿Cuántas personas se hospedarán?");
            if (inputPersonas == null || inputPersonas.isEmpty()) {
                return; // Cancelado por el usuario
            }
            int numPersonas = Integer.parseInt(inputPersonas);

            // Validar restricciones de personas por piso
            if (numPersonas < minPersonas || numPersonas > maxPersonas) {
                JOptionPane.showMessageDialog(this, "El número de personas no cumple con los requisitos para esta habitación.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Calcular costo según el piso
            int costoPorNoche;
            switch (piso) {
                case 1:
                    costoPorNoche = 80000;
                    break;
                case 2:
                case 3:
                    costoPorNoche = 100000;
                    break;
                case 4:
                    costoPorNoche = 120000;
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "El piso seleccionado no tiene tarifas configuradas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            int costoTotal = costoPorNoche * numPersonas;

            // Mostrar costo total y confirmar la reserva
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "El costo total será: $" + costoTotal + ". ¿Deseas proceder con la reserva?",
                    "Confirmar reserva",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                procesarReserva(idHabitacion);
                actualizarTablaHabitaciones();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido de personas.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al procesar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Método para verificar disponibilidad de una habitación (ejemplo)
    private boolean verificarDisponibilidad(String idHabitacion) {
        try (Conexion conexion = new Conexion()) {
            return conexion.verificarDisponibilidadHabitacion(idHabitacion);
        }
    }


    // Método para procesar la reserva de la habitación (ejemplo)
    private void procesarReserva(String idHabitacion) {
        try (Conexion conexion = new Conexion()) {
            if (conexion.reservarHabitacion(idHabitacion)) {
                JOptionPane.showMessageDialog(this, "Reserva realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Método para actualizar la tabla de habitaciones (ejemplo)
    private void actualizarTablaHabitaciones() {
        cargarHabitacionesDesdeBD();
    }


    public void mostrarPanelReservas() {
        setTitle("Panel de Reservas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cargarHabitacionesDesdeBD(); // Cargar datos al iniciar
        setVisible(true);
    }



}

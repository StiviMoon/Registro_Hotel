package Controler;

import BD.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class User_Panel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable userTable; // Tabla para mostrar los usuarios

    public User_Panel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de Filtro de Búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Filtro de Búsqueda"));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Panel de Usuarios (Tabla)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Usuarios"));

        String[] columnNames = {"ID", "Nombre", "Apellido", "Celular"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel de Botones para CRUD
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        crudPanel.setBorder(BorderFactory.createTitledBorder("Acciones CRUD"));
        JButton addButton = new JButton("Editar");
        JButton editButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");

        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);

        add(searchPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(crudPanel, BorderLayout.SOUTH);

        // Acción del botón de Editar
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    editarUsuario(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un usuario para editar.");
                }
            }
        });

        // Acción del botón de Actualizar
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarUsuarios(); // Refresca la tabla
            }
        });

        // Acción del botón de Eliminar
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    eliminarUsuario(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un usuario para eliminar.");
                }
            }
        });

        // Cargar datos de la base de datos al inicializar el panel
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        tableModel.setRowCount(0); // Limpiar los datos actuales de la tabla

        try (Conexion conexion = new Conexion()) {
            List<Map<String, Object>> usuarios = conexion.obtenerUsuarios();
            for (Map<String, Object> usuario : usuarios) {
                Object[] fila = {
                        usuario.get("ID"),
                        usuario.get("Nombre"),
                        usuario.get("Apellido"),
                        usuario.get("Celular")
                };
                tableModel.addRow(fila);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios en la tabla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarUsuario(int rowIndex) {
        int id = (int) tableModel.getValueAt(rowIndex, 0);
        String nombre = (String) tableModel.getValueAt(rowIndex, 1);
        String apellido = (String) tableModel.getValueAt(rowIndex, 2);
        String celular = (String) tableModel.getValueAt(rowIndex, 3);

        JTextField nombreField = new JTextField(nombre);
        JTextField apellidoField = new JTextField(apellido);
        JTextField celularField = new JTextField(celular);

        Object[] message = {
                "Nombre:", nombreField,
                "Apellido:", apellidoField,
                "Celular:", celularField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Conexion conexion = new Conexion()) {
                boolean success = conexion.updateUsuario(id, nombreField.getText(), apellidoField.getText(), celularField.getText());
                if (success) {
                    JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.");
                    cargarUsuarios(); // Refresca la tabla
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el usuario.");
                }
            } catch (Exception ex) {
                System.err.println("Error al editar el usuario: " + ex.getMessage());
            }
        }
    }

    private void eliminarUsuario(int rowIndex) {
        int id = (int) tableModel.getValueAt(rowIndex, 0);

        int option = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Conexion conexion = new Conexion()) {
                boolean success = conexion.deleteUsuario(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
                    cargarUsuarios(); // Refresca la tabla
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el usuario.");
                }
            } catch (Exception ex) {
                System.err.println("Error al eliminar el usuario: " + ex.getMessage());
            }
        }
    }
}

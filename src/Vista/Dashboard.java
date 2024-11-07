
package Vista; // Cambia a un paquete adecuado

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton logout;
    private JLabel headerLabel;
    private UserPanel userPanel;
    private Reserva reservaPanel;

    // Constructor para inicializar el Dashboard
    public Dashboard() {
        setTitle("Dashboard - Sistema de Gestión");
        setSize(1550, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de Navegación Lateral
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(45, 45, 45));
        sidePanel.setPreferredSize(new Dimension(250, 750));
        sidePanel.setLayout(new GridLayout(6, 1, 10, 10));

        // Encabezado
        headerLabel = new JLabel("Perfil de Usuario", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBackground(new Color(45, 45, 45));
        headerLabel.setOpaque(true);
        sidePanel.add(headerLabel);

        // Botones de Navegación
        JButton btnReserva = new JButton("Reserva");
        JButton btnUsuarios = new JButton("Usuarios");


        styleButton(btnReserva);
        styleButton(btnUsuarios);


        btnReserva.addActionListener(this);
        btnUsuarios.addActionListener(this);


        // Botón de Cerrar Sesión
        logout = new JButton("Cerrar Sesión");
        styleButton(logout);
        logout.addActionListener(this);

        // Agregar botones al panel lateral
        sidePanel.add(btnReserva);
        sidePanel.add(btnUsuarios);
        sidePanel.add(logout);

        add(sidePanel, BorderLayout.WEST);

        // Panel Principal con CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Panel de Recepción
        JPanel reservaPanel = new JPanel();
        reservaPanel.add(new Reserva());
        cardPanel.add(reservaPanel, "Reserva");

        // Panel de Configuraciones
        JPanel usuariosPanel = new JPanel();
        usuariosPanel.add(new UserPanel());
        cardPanel.add(usuariosPanel, "Usuarios");


        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Método para estilizar los botones
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.PLAIN, 18));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Controlador de eventos para manejar los botones
    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        switch (command) {
            case "Reserva":
                cardLayout.show(cardPanel, "Reserva");
                break;
            case "Usuarios":
                cardLayout.show(cardPanel, "Usuarios");
                break;
            case "Cerrar Sesión":
                setVisible(false);
                //new Login(); // Redirige a la pantalla de inicio de sesión
                break;
            default:
                break;
        }
    }




}
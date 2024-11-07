package Controler;

import javax.swing.*;

public class Controlador_Registro {
    // Método auxiliar para añadir un JLabel y un JTextField en una posición específica
    public void addLabelAndTextField(JPanel panel, String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 150, 30);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(x + 160, y, 300, 30); // Espacio suficiente para el campo de texto
        panel.add(textField);
    }
}

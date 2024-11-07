import BD.Conexion;
import Vista.Dashboard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);


    }
}
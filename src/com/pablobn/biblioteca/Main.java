package com.pablobn.biblioteca;

import com.formdev.flatlaf.FlatLightLaf;
import com.pablobn.biblioteca.util.BBDDUtil;
import com.pablobn.biblioteca.vista.LoginPanel;

import javax.swing.*;

/**
 * Clase principal de la aplicación de gestión de biblioteca.
 *
 * <p>Esta clase es el punto de entrada de la aplicación. Se encarga de
 * configurar el look and feel (FlatLaf) y de mostrar la ventana
 * inicial de login ({@link LoginPanel}) al usuario.</p>
 *
 * @author Pablo Barriendos Navales
 * @version 1.0
 * @since 2024-05-24
 */
public class Main {

    /**
     * Método principal que inicia la aplicación de gestión de biblioteca.
     *
     * <p>Este método realiza las siguientes acciones:
     * <ol>
     * <li>Configura el look and feel FlatLightLaf para la interfaz gráfica.</li>
     * <li>Crea la ventana principal ({@link JFrame}) con el título "Gestión Biblioteca".</li>
     * <li>Establece la operación de cierre por defecto para que la aplicación finalice
     * cuando se cierra la ventana.</li>
     * <li>Define el tamaño inicial de la ventana y la centra en la pantalla.</li>
     * <li>Crea una instancia de {@link LoginPanel} y la establece como el panel
     * de contenido de la ventana principal. En caso de error durante la creación
     * del panel de login, se imprime la traza del error.</li>
     * <li>Hace visible la ventana principal.</li>
     * </ol>
     * </p>
     *
     * @param args los argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        BBDDUtil.initDatabase();
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al configurar FlatLaf: " + e.getMessage());
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLocationRelativeTo(null);

        try {
            frame.setContentPane(new LoginPanel());
        } catch (Exception e) {
            System.err.println("Error al crear el LoginPanel: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Error crítico al iniciar la aplicación. Consulte la consola para más detalles.",
                    "Error de Inicio",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hace visible la ventana.
        frame.setVisible(true);
    }
}
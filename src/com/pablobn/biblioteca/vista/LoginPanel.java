package com.pablobn.biblioteca.vista;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;

    public LoginPanel() {
        setLayout(new GridLayout(4, 2, 10, 10)); // 4 filas x 2 columnas
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Bordes para espacio interior

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrarse");

        // Añadir los componentes con el mismo estilo que el registro
        add(new JLabel("Nombre de Usuario:"));
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        add(txtPassword);

        add(new JLabel()); // Espacio vacío
        add(btnLogin);

        add(new JLabel()); // Espacio vacío
        add(btnRegistro);

        // Acción al pulsar Registrarse
        btnRegistro.addActionListener(e -> {
            JFrame frameRegistro = new JFrame("Registro de Usuario");
            frameRegistro.setContentPane(new RegistroUsuarioPanel());
            frameRegistro.setSize(400, 500);
            frameRegistro.setLocationRelativeTo(null); // Centrar
            frameRegistro.setVisible(true);
        });
    }
}

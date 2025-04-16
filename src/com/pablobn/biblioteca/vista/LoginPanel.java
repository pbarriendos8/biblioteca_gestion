package com.pablobn.biblioteca.vista;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;

    public LoginPanel() {
        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrarse");

        setLayout(new FormLayout(
                "10dlu, right:pref, 5dlu, 150dlu, 10dlu",
                "10dlu, p, 5dlu, p, 10dlu, p, 5dlu, p, 10dlu"
        ));

        CellConstraints cc = new CellConstraints();

        add(new JLabel("Usuario:"), cc.xy(2, 2));
        add(txtUsuario, cc.xy(4, 2));

        add(new JLabel("Contraseña:"), cc.xy(2, 4));
        add(txtPassword, cc.xy(4, 4));

        add(btnLogin, cc.xy(4, 6));
        add(btnRegistro, cc.xy(4, 8));

        btnRegistro.addActionListener(e -> {
            JFrame frameRegistro = new JFrame("Registro de Usuario");
            frameRegistro.setContentPane(new RegistroUsuarioPanel());
            frameRegistro.setSize(400, 500);
            frameRegistro.setLocationRelativeTo(null); // Centrar
            frameRegistro.setVisible(true);
        });

    }
}
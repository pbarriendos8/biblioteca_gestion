package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

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

        btnLogin.addActionListener(e -> autenticarUsuario());
    }
    private void autenticarUsuario() {
        String usuario = txtUsuario.getText();
        char[] passwordArray = txtPassword.getPassword();
        String password = new String(passwordArray);

        // Llamar al DAO para autenticar al usuario
        Usuario usuarioLogueado = UsuarioDAO.autenticar(usuario, password);

        if (usuarioLogueado != null) {
            // Si el usuario es válido, abre la ventana principal y pasa el usuario
            JFrame ventanaPrincipal = new VentanaPrincipal(usuarioLogueado);
            ventanaPrincipal.setSize(1000, 700);
            ventanaPrincipal.setLocationRelativeTo(null); // Centrar
            ventanaPrincipal.setVisible(true);
            // Cerrar el login después de iniciar sesión
            JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            loginFrame.dispose();
        } else {
            // Si el usuario o contraseña es incorrecto, mostrar mensaje
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}

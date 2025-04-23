package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class RegistroUsuarioPanel extends JPanel {

    public RegistroUsuarioPanel() {
        setLayout(new GridLayout(9, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nombreUsuarioField = new JTextField();
        JTextField correoField = new JTextField();
        JPasswordField contrasenaField = new JPasswordField();
        JTextField nombreCompletoField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField telefonoField = new JTextField();

        add(new JLabel("Nombre de Usuario:"));
        add(nombreUsuarioField);
        add(new JLabel("Correo:"));
        add(correoField);
        add(new JLabel("Contraseña:"));
        add(contrasenaField);
        add(new JLabel("Nombre completo:"));
        add(nombreCompletoField);
        add(new JLabel("Dirección:"));
        add(direccionField);
        add(new JLabel("Teléfono:"));
        add(telefonoField);

        JButton crearCuentaBtn = new JButton("Crear usuario");
        add(new JLabel());
        add(crearCuentaBtn);

        crearCuentaBtn.addActionListener(e -> {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuarioField.getText());
            nuevoUsuario.setCorreo(correoField.getText());
            nuevoUsuario.setPassword(new String(contrasenaField.getPassword()));
            nuevoUsuario.setNombreCompleto(nombreCompletoField.getText());
            nuevoUsuario.setDireccion(direccionField.getText());
            nuevoUsuario.setTelefono(telefonoField.getText());

            UsuarioDAO.registrarUsuario(nuevoUsuario);

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        });
    }
}

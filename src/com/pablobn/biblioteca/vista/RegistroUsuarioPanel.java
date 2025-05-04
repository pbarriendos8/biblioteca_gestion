package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class RegistroUsuarioPanel extends JPanel {

    public RegistroUsuarioPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 37, 41));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Campos
        JTextField nombreUsuarioField = createTextField();
        JTextField correoField = createTextField();
        JPasswordField contrasenaField = createPasswordField();
        JTextField nombreCompletoField = createTextField();
        JTextField direccionField = createTextField();
        JTextField telefonoField = createTextField();

        // Etiquetas y campos
        addField("Nombre de Usuario:", nombreUsuarioField, gbc);
        addField("Correo:", correoField, gbc);
        addField("Contraseña:", contrasenaField, gbc);
        addField("Nombre completo:", nombreCompletoField, gbc);
        addField("Dirección:", direccionField, gbc);
        addField("Teléfono:", telefonoField, gbc);

        // Botón crear
        JButton crearCuentaBtn = crearBoton("Crear Usuario", new Color(70, 130, 255));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(crearCuentaBtn, gbc);

        // Botón salir
        JButton salirBtn = crearBoton("Salir", new Color(120, 120, 120));
        gbc.gridy++;
        add(salirBtn, gbc);

        // Acción crear usuario
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

        // Acción salir
        salirBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) window.dispose();
        });
    }

    private void addField(String label, JComponent field, GridBagConstraints gbc) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jLabel.setForeground(new Color(60, 63, 65));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15); // Igual al login
        styleField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15); // Igual al login
        styleField(field);
        return field;
    }

    private void styleField(JTextComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }
}

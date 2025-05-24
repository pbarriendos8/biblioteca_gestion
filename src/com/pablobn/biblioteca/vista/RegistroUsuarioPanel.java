package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;

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
        JPasswordField repetirContrasenaField = createPasswordField();
        JTextField nombreCompletoField = createTextField();
        JTextField direccionField = createTextField();
        JTextField telefonoField = createTextField();

        int fila = 1;
        fila = addField("Nombre de Usuario:", nombreUsuarioField, gbc, fila);
        fila = addField("Correo:", correoField, gbc, fila);
        fila = addField("Contraseña:", contrasenaField, gbc, fila);
        fila = addField("Repetir Contraseña:", repetirContrasenaField, gbc, fila);
        fila = addField("Nombre completo:", nombreCompletoField, gbc, fila);
        fila = addField("Dirección:", direccionField, gbc, fila);
        fila = addField("Teléfono:", telefonoField, gbc, fila);

        // Botón crear
        JButton crearCuentaBtn = crearBoton("Crear Usuario", new Color(70, 130, 255));
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(crearCuentaBtn, gbc);

        // Botón salir
        JButton salirBtn = crearBoton("Salir", new Color(120, 120, 120));
        gbc.gridy = fila;
        add(salirBtn, gbc);

        // Acción crear usuario
        crearCuentaBtn.addActionListener(e -> {
            resetFieldBorders(nombreUsuarioField, contrasenaField, repetirContrasenaField);

            String nombreUsuario = nombreUsuarioField.getText().trim();
            String password = new String(contrasenaField.getPassword()).trim();
            String repetirPassword = new String(repetirContrasenaField.getPassword()).trim();

            StringBuilder errores = new StringBuilder();
            boolean hayErrores = false;

            if (nombreUsuario.isEmpty()) {
                errores.append("• El nombre de usuario no puede estar vacío.\n");
                marcarError(nombreUsuarioField);
                hayErrores = true;
            }

            if (password.isEmpty()) {
                errores.append("• La contraseña no puede estar vacía.\n");
                marcarError(contrasenaField);
                marcarError(repetirContrasenaField);
                hayErrores = true;
            } else if (!password.equals(repetirPassword)) {
                errores.append("• Las contraseñas no coinciden.\n");
                marcarError(contrasenaField);
                marcarError(repetirContrasenaField);
                hayErrores = true;
            }

            if (hayErrores) {
                JOptionPane.showMessageDialog(this, errores.toString(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setCorreo(correoField.getText());
            nuevoUsuario.setPassword(HashUtil.hashPassword(password));
            nuevoUsuario.setNombreCompleto(nombreCompletoField.getText());
            nuevoUsuario.setDireccion(direccionField.getText());
            nuevoUsuario.setTelefono(telefonoField.getText());

            UsuarioDAO.registrarUsuario(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");

            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }

        });

        salirBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar esta ventana?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Window window = SwingUtilities.windowForComponent(this);
                if (window != null) {
                    window.dispose();
                }
            }
        });
    }

    private int addField(String labelText, JComponent field, GridBagConstraints gbc, int fila) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(60, 63, 65));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(field, gbc);

        return fila + 1;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        styleField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
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

    private void marcarError(JComponent field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }

    private void resetFieldBorders(JComponent... fields) {
        for (JComponent field : fields) {
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
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

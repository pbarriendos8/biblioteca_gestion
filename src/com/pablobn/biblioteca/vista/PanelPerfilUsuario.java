package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class PanelPerfilUsuario extends JPanel {
    private final JTextField txtNombreCompleto;
    private final JTextField txtCorreo;
    private final JTextField txtDireccion;
    private final JTextField txtTelefono;
    private final JPasswordField txtPassword;

    public PanelPerfilUsuario(Usuario usuario) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblTitulo = new JLabel("Mi Perfil");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);
        JPanel formulario = new JPanel();
        formulario.setLayout(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(250, 30);

        txtNombreCompleto = new JTextField(usuario.getNombreUsuario());
        txtCorreo = new JTextField(usuario.getCorreo());
        txtDireccion = new JTextField(usuario.getDireccion());
        txtTelefono = new JTextField(usuario.getTelefono());
        txtPassword = new JPasswordField(usuario.getPassword());
        txtPassword.setEnabled(false);

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!txtPassword.isEnabled()) {
                    int confirm = JOptionPane.showConfirmDialog(PanelPerfilUsuario.this,
                            "¿Deseas cambiar la contraseña?", "Confirmar cambio", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        txtPassword.setEnabled(true);
                        txtPassword.requestFocus();
                        txtPassword.setText("");
                    }
                }
            }
        });
        addField(formulario, gbc, 0, "Nombre completo:", txtNombreCompleto, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 1, "Correo:", txtCorreo, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 2, "Dirección:", txtDireccion, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 3, "Teléfono:", txtTelefono, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 4, "Contraseña:", txtPassword, labelFont, fieldFont, fieldSize);

        add(formulario, BorderLayout.CENTER);
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(180, 40));

        btnGuardar.addActionListener(e -> {
            String nuevaPassword = new String(txtPassword.getPassword());
            if (!nuevaPassword.equals(usuario.getPassword())) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Deseas cambiar la contraseña?", "Confirmar cambio", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    txtPassword.setText(usuario.getPassword());
                    return;
                }
            }

            usuario.setNombreCompleto(txtNombreCompleto.getText());
            usuario.setCorreo(txtCorreo.getText());
            usuario.setDireccion(txtDireccion.getText());
            usuario.setTelefono(txtTelefono.getText());
            usuario.setPassword(nuevaPassword);

            UsuarioDAO.actualizarUsuario(usuario);
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
        });

        JPanel panelBoton = new JPanel();
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelBoton.add(btnGuardar);

        add(panelBoton, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JComponent field,
                          Font labelFont, Font fieldFont, Dimension fieldSize) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setFont(fieldFont);
        field.setPreferredSize(fieldSize);
        panel.add(field, gbc);
    }
}

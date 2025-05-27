package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para mostrar y editar el perfil del usuario logueado.
 * <p>
 * Permite modificar datos personales como nombre completo, correo,
 * dirección, teléfono y contraseña (previa confirmación).
 * Incluye validaciones básicas y actualiza el usuario en la base de datos
 * mediante UsuarioDAO.
 * </p>
 */
public class PanelPerfilUsuario extends JPanel {
    private final JTextField txtNombreCompleto;
    private final JTextField txtCorreo;
    private final JTextField txtDireccion;
    private final JTextField txtTelefono;
    private final JTextField txtPasswordPlaceHolder;
    private final JPasswordField txtPassword;

    private final CardLayout cardLayoutPassword;
    private final JPanel contenedorPassword;
    private String contraseñaSinHashear;

    /**
     * Construye el panel de perfil con los datos iniciales del usuario.
     *
     * @param usuario Usuario cuyos datos se mostrarán y podrán editarse.
     */
    public PanelPerfilUsuario(Usuario usuario, String contraseñaSinHashear) {
        this.contraseñaSinHashear = contraseñaSinHashear;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblTitulo = new JLabel("MI PERFIL");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(35, 0, 20, 0));
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

        txtNombreCompleto = new JTextField(usuario.getNombreCompleto());
        txtCorreo = new JTextField(usuario.getCorreo());
        txtDireccion = new JTextField(usuario.getDireccion());
        txtTelefono = new JTextField(usuario.getTelefono());
        cardLayoutPassword = new CardLayout();
        contenedorPassword = new JPanel(cardLayoutPassword);
        txtPasswordPlaceHolder = new JTextField("Haz click para cambiarla");
        txtPasswordPlaceHolder.setVisible(true);
        txtPasswordPlaceHolder.setEnabled(false);
        txtPasswordPlaceHolder.setDisabledTextColor(Color.GRAY);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setVisible(false);

        txtPasswordPlaceHolder.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = JOptionPane.showConfirmDialog(PanelPerfilUsuario.this,
                        "¿Deseas cambiar la contraseña?", "Confirmar cambio", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    cardLayoutPassword.show(contenedorPassword, "real");
                    txtPassword.requestFocus();
                }
            }
        });
        contenedorPassword.add(txtPasswordPlaceHolder, "placeholder");
        contenedorPassword.add(txtPassword, "real");
        cardLayoutPassword.show(contenedorPassword, "placeholder");

        addField(formulario, gbc, 0, "Nombre completo:", txtNombreCompleto, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 1, "Correo:", txtCorreo, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 2, "Dirección:", txtDireccion, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 3, "Teléfono:", txtTelefono, labelFont, fieldFont, fieldSize);
        addField(formulario, gbc, 4, "Contraseña:", contenedorPassword, labelFont, fieldFont, fieldSize);

        add(formulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(180, 40));

        btnGuardar.addActionListener(e -> {
            txtPassword.setBorder(UIManager.getBorder("TextField.border"));
            StringBuilder errores = new StringBuilder();
            boolean hayErrores = false;
            String nuevaPassword = new String(txtPassword.getPassword());
            if (nuevaPassword.isEmpty()) {
                errores.append("- La contraseña es obligatoria.\n");
                txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
                hayErrores = true;
            }
            if (hayErrores) {
                JOptionPane.showMessageDialog(this, "Corrige los siguientes errores:\n" + errores.toString(),
                        "Errores de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nuevaPasswordHasheada = HashUtil.hashPassword(nuevaPassword);
            usuario.setNombreCompleto(txtNombreCompleto.getText());
            usuario.setCorreo(txtCorreo.getText());
            usuario.setDireccion(txtDireccion.getText());
            usuario.setTelefono(txtTelefono.getText());
            usuario.setPassword(nuevaPasswordHasheada);

            UsuarioDAO.actualizarUsuario(usuario);
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
        });
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalir.setBackground(new Color(120, 120, 120));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(160, 40));
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Deseas salir del perfil?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }
            }
        });

        panelBoton.add(btnGuardar);
        panelBoton.add(btnSalir);

        add(panelBoton, BorderLayout.SOUTH);

    }

    /**
     * Método auxiliar para añadir una fila con etiqueta y campo al formulario.
     *
     * @param panel      Panel donde agregar los componentes.
     * @param gbc        Constraints para GridBagLayout.
     * @param y          Posición vertical (fila) en el GridBagLayout.
     * @param labelText  Texto de la etiqueta.
     * @param field      Componente de entrada (JTextField o JPasswordField).
     * @param labelFont  Fuente para la etiqueta.
     * @param fieldFont  Fuente para el campo.
     * @param fieldSize  Tamaño preferido para el campo.
     */
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

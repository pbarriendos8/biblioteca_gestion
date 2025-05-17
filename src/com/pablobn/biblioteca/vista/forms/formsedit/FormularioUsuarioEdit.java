package com.pablobn.biblioteca.vista.forms.formsedit;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;
import com.pablobn.biblioteca.util.TipoUsuario;

import javax.swing.*;
import java.awt.*;

public class FormularioUsuarioEdit extends JDialog {

    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> cmbTipoUsuario;
    private JTextField txtNombreCompleto;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    private final Usuario usuario;

    public FormularioUsuarioEdit(JFrame parent, Usuario usuario) {
        super(parent, "Editar Usuario", true);
        this.usuario = usuario;

        setSize(600, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelPrincipal);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int fila = 0;

        agregarCampo(panelCampos, gbc, fila++, "Nombre de Usuario:", txtNombreUsuario = new JTextField());
        agregarCampo(panelCampos, gbc, fila++, "Correo:", txtCorreo = new JTextField());
        agregarCampo(panelCampos, gbc, fila++, "Contraseña:", txtPassword = new JPasswordField());
        agregarCampo(panelCampos, gbc, fila++, "Tipo de Usuario:", cmbTipoUsuario = new JComboBox<>(TipoUsuario.values()));
        agregarCampo(panelCampos, gbc, fila++, "Nombre Completo:", txtNombreCompleto = new JTextField());
        agregarCampo(panelCampos, gbc, fila++, "Dirección:", txtDireccion = new JTextField());
        agregarCampo(panelCampos, gbc, fila++, "Teléfono:", txtTelefono = new JTextField());

        // Panel de botones (ambos a la derecha)
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

// Botón Guardar
        JButton btnGuardar = new JButton("Guardar Autor");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.addActionListener(e -> guardarCambios());

// Botón Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalir.setBackground(new Color(120, 120, 120));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(160, 40));
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas salir del formulario?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

// Añadir primero Guardar, luego Salir (para que Salir quede más a la derecha)
        panelBoton.add(btnGuardar);
        panelBoton.add(btnSalir);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);


        cargarDatosUsuario();
        txtPassword.setEditable(false);
        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        if (campo instanceof JTextField || campo instanceof JPasswordField) {
            campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            campo.setPreferredSize(new Dimension(200, 30));
        }
        if (campo instanceof JComboBox) {
            campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            ((JComboBox<?>) campo).setPreferredSize(new Dimension(200, 30));
        }
        panel.add(campo, gbc);
    }

    private void cargarDatosUsuario() {
        txtNombreUsuario.setText(usuario.getNombreUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtPassword.setText(usuario.getPassword());
        cmbTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
        txtNombreCompleto.setText(usuario.getNombreCompleto());
        txtDireccion.setText(usuario.getDireccion());
        txtTelefono.setText(usuario.getTelefono());
    }

    private void guardarCambios() {
        cmbTipoUsuario.setBorder(UIManager.getBorder("ComboBox.border"));
        txtNombreUsuario.setBorder(UIManager.getBorder("TextField.border"));
        txtCorreo.setBorder(UIManager.getBorder("TextField.border"));
        txtPassword.setBorder(UIManager.getBorder("TextField.border"));
        StringBuilder errores = new StringBuilder();
        boolean hayErrores = false;

        String nombreUsuario = txtNombreUsuario.getText().trim();
        if (nombreUsuario.isEmpty()) {
            errores.append("- El nombre del usuario es obligatorio.\n");
            txtNombreUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
            hayErrores = true;
        }
        String email = txtCorreo.getText().trim();
        if (nombreUsuario.isEmpty()) {
            errores.append("- El Email es obligatorio.\n");
            txtCorreo.setBorder(BorderFactory.createLineBorder(Color.RED));
            hayErrores = true;
        }
        String password = new String(txtPassword.getPassword()).trim();
        if (password.isEmpty()) {
            errores.append("- La contraseña es obligatoria.\n");
            txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
            hayErrores = true;
        }
        TipoUsuario tipoUsuario = (TipoUsuario) cmbTipoUsuario.getSelectedItem();
        if (tipoUsuario == null) {
            errores.append("- Debes seleccionar un tipo de usuario.\n");
            cmbTipoUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
            hayErrores = true;
        }
        if (hayErrores) {
            JOptionPane.showMessageDialog(this, "Corrige los siguientes errores:\n" + errores.toString(),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            usuario.setNombreUsuario(txtNombreUsuario.getText());
            usuario.setCorreo(txtCorreo.getText());
            usuario.setPassword(HashUtil.hashPassword(password));
            usuario.setTipoUsuario((TipoUsuario) cmbTipoUsuario.getSelectedItem());
            usuario.setNombreCompleto(txtNombreCompleto.getText());
            usuario.setDireccion(txtDireccion.getText());
            usuario.setTelefono(txtTelefono.getText());

            UsuarioDAO.actualizarUsuario(usuario); // Asegúrate de tener este método

            JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + e.getMessage());
        }
    }
}

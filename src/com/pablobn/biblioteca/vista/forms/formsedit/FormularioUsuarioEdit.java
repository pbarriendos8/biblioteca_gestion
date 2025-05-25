package com.pablobn.biblioteca.vista.forms.formsedit;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;
import com.pablobn.biblioteca.util.TipoUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para editar un usuario existente.
 * Muestra los campos del usuario y permite actualizar su información,
 * excepto la contraseña que no puede modificarse directamente desde este formulario.
 * <p>
 * El formulario incluye validación básica y muestra mensajes de error si hay campos obligatorios vacíos.
 */
public class FormularioUsuarioEdit extends JDialog {

    // Campos del formulario
    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> cmbTipoUsuario;
    private JTextField txtNombreCompleto;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    private final Usuario usuario;

    /**
     * Constructor del formulario de edición de usuario.
     *
     * @param parent   Ventana principal desde la que se abre el formulario.
     * @param usuario  Usuario a editar.
     */
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

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnGuardar = new JButton("Guardar Autor");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.addActionListener(e -> guardarCambios());

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

        panelBoton.add(btnGuardar);
        panelBoton.add(btnSalir);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        cargarDatosUsuario();
        txtPassword.setEditable(false);
        setVisible(true);
    }

    /**
     * Agrega un campo al formulario con su respectiva etiqueta.
     *
     * @param panel   Panel donde se agregará el campo.
     * @param gbc     Restricciones de GridBagLayout.
     * @param fila    Fila donde colocar el campo.
     * @param etiqueta Texto de la etiqueta.
     * @param campo    Componente del campo.
     */
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

    /**
     * Carga los datos del usuario en los campos del formulario.
     */
    private void cargarDatosUsuario() {
        txtNombreUsuario.setText(usuario.getNombreUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtPassword.setText(usuario.getPassword());
        cmbTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
        txtNombreCompleto.setText(usuario.getNombreCompleto());
        txtDireccion.setText(usuario.getDireccion());
        txtTelefono.setText(usuario.getTelefono());
    }

    /**
     * Valida los datos ingresados, actualiza el usuario y guarda los cambios en la base de datos.
     * Muestra mensajes de error si hay campos inválidos.
     */
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

            UsuarioDAO.actualizarUsuario(usuario);

            JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + e.getMessage());
        }
    }
}



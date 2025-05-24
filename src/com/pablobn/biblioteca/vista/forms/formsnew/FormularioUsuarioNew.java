package com.pablobn.biblioteca.vista.forms.formsnew;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;
import com.pablobn.biblioteca.util.TipoUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para crear un nuevo usuario en la aplicación.
 * <p>
 * Proporciona campos para ingresar nombre de usuario, correo electrónico, contraseña,
 * tipo de usuario, nombre completo, dirección y teléfono.
 * Realiza validaciones básicas antes de guardar el usuario en la base de datos.
 */
public class FormularioUsuarioNew extends JDialog {
    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> cmbTipoUsuario;
    private JTextField txtNombreCompleto;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    /**
     * Crea un nuevo formulario para agregar un usuario.
     *
     * @param parent la ventana padre sobre la que se mostrará este diálogo modal
     */
    public FormularioUsuarioNew(JFrame parent) {
        super(parent, "Nuevo Usuario", true);
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
        cmbTipoUsuario = new JComboBox<>();
        cmbTipoUsuario.addItem(null);
        for (TipoUsuario tipo : TipoUsuario.values()) {
            cmbTipoUsuario.addItem(tipo);
        }
        cmbTipoUsuario.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Selecciona un tipo de usuario --");
                } else {
                    setText(value.toString());
                }
                return this;
            }
        });

        agregarCampo(panelCampos, gbc, fila++, "Tipo de Usuario:", cmbTipoUsuario);
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
        btnGuardar.addActionListener(e -> guardarUsuario());
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


        setVisible(true);
    }

    /**
     * Agrega un campo con etiqueta y componente al panel usando GridBagLayout.
     *
     * @param panel   el panel donde se agregará el campo
     * @param gbc     restricciones de GridBagLayout
     * @param fila    fila en la que se agregará el campo
     * @param etiqueta texto que se mostrará como etiqueta del campo
     * @param campo   componente (campo de texto, contraseña o combo) a agregar
     */
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(etiqueta, SwingConstants.RIGHT);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(140, 30));
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
     * Valida los campos del formulario y guarda un nuevo usuario en la base de datos.
     * <p>
     * Realiza las siguientes validaciones:
     * <ul>
     *     <li>El nombre de usuario no puede estar vacío.</li>
     *     <li>El correo electrónico no puede estar vacío.</li>
     *     <li>La contraseña no puede estar vacía.</li>
     *     <li>Debe seleccionarse un tipo de usuario.</li>
     * </ul>
     * <p>
     * En caso de error muestra un mensaje indicando los campos faltantes y no realiza la operación.
     * Si la validación es exitosa, encripta la contraseña, crea un objeto Usuario y lo persiste.
     * Luego muestra un mensaje de éxito y cierra el formulario.
     */
    private void guardarUsuario() {
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
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(txtNombreUsuario.getText());
            usuario.setCorreo(txtCorreo.getText());
            usuario.setPassword(HashUtil.hashPassword(password));
            usuario.setTipoUsuario((TipoUsuario) cmbTipoUsuario.getSelectedItem());
            usuario.setNombreCompleto(txtNombreCompleto.getText());
            usuario.setDireccion(txtDireccion.getText());
            usuario.setTelefono(txtTelefono.getText());
            usuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));

            UsuarioDAO.crearUsuario(usuario);

            JOptionPane.showMessageDialog(this, "Usuario guardado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar usuario: " + e.getMessage());
        }
    }
}

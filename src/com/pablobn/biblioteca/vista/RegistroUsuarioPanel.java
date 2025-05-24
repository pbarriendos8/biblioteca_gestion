package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Panel de interfaz gráfica para el registro de nuevos usuarios en la aplicación.
 *
 * <p>Este panel presenta un formulario con campos para que un nuevo usuario ingrese
 * su nombre de usuario, correo electrónico, contraseña (con confirmación),
 * nombre completo, dirección y teléfono. Incluye validaciones básicas
 * y utiliza {@link UsuarioDAO} para persistir el nuevo usuario en la base de datos.
 * La contraseña se almacena hasheada utilizando {@link HashUtil}.</p>
 *
 * @see JPanel
 * @see Usuario
 * @see UsuarioDAO
 * @see HashUtil
 */
public class RegistroUsuarioPanel extends JPanel {

    /**
     * Constructor del panel de registro de usuarios.
     * Inicializa y configura los componentes de la interfaz gráfica,
     * incluyendo etiquetas, campos de texto y botones.
     * Establece los manejadores de eventos para los botones de "Crear Usuario" y "Salir".
     */
    public RegistroUsuarioPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 37, 41));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridwidth = 1; // Restablecer gridwidth para los campos
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
        JButton crearCuentaBtn = crearBoton("Crear Usuario", new Color(70, 130, 255));
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(crearCuentaBtn, gbc);
        JButton salirBtn = crearBoton("Salir", new Color(120, 120, 120));
        gbc.gridy = fila;
        add(salirBtn, gbc);
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

    /**
     * Añade una etiqueta y un campo de texto/componente al panel utilizando {@link GridBagLayout}.
     *
     * @param labelText El texto para la etiqueta del campo.
     * @param field El componente de Swing (e.g., JTextField, JPasswordField) a añadir.
     * @param gbc Las restricciones {@link GridBagConstraints} para posicionar los componentes.
     * @param fila La fila actual en el layout donde se añadirán la etiqueta y el campo.
     * @return El número de la siguiente fila disponible en el layout.
     */
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

    /**
     * Crea un {@link JTextField} con un estilo predefinido.
     *
     * @return Un nuevo objeto {@link JTextField} estilizado.
     */
    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        styleField(field);
        return field;
    }

    /**
     * Crea un {@link JPasswordField} con un estilo predefinido.
     *
     * @return Un nuevo objeto {@link JPasswordField} estilizado.
     */
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        styleField(field);
        return field;
    }

    /**
     * Aplica un estilo común a un componente de texto ({@link JTextComponent}).
     * Configura la fuente y el borde del campo.
     *
     * @param field El componente de texto (e.g., JTextField, JPasswordField) al que se aplicará el estilo.
     */
    private void styleField(JTextComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    /**
     * Marca un componente visualmente para indicar un error, cambiando su borde a color rojo.
     *
     * @param field El componente {@link JComponent} que se marcará como erróneo.
     */
    private void marcarError(JComponent field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }

    /**
     * Restablece el borde de los componentes especificados a su estilo original.
     * Útil para limpiar las indicaciones visuales de error después de una corrección.
     *
     * @param fields Un array varargs de {@link JComponent} cuyos bordes serán restablecidos.
     */
    private void resetFieldBorders(JComponent... fields) {
        for (JComponent field : fields) {
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
    }

    /**
     * Crea un {@link JButton} con un estilo predefinido.
     *
     * @param texto El texto que se mostrará en el botón.
     * @param fondo El color de fondo del botón.
     * @return Un nuevo objeto {@link JButton} estilizado.
     */
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
package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * Panel de inicio de sesión de la aplicación.
 * <p>
 * Contiene campos para ingresar el nombre de usuario y la contraseña,
 * así como botones para iniciar sesión, registrarse y salir de la aplicación.
 * </p>
 * <p>
 * Al iniciar sesión correctamente, abre la ventana principal con el usuario autenticado.
 * </p>
 *
 * @author Pablo Barriendos Navales
 */
public class LoginPanel extends JPanel {

    /**
     * Campo de texto para ingresar el nombre de usuario.
     */
    private JTextField txtUsuario;

    /**
     * Campo de texto para ingresar la contraseña.
     */
    private JPasswordField txtPassword;

    /**
     * Botón para iniciar sesión.
     */
    private JButton btnLogin;

    /**
     * Botón para abrir el formulario de registro de usuarios.
     */
    private JButton btnRegistro;

    /**
     * Botón para salir de la aplicación.
     */
    private JButton btnSalir;

    /**
     * Fuente utilizada para los campos de texto.
     */
    private final Font fuenteTexto = new Font("SansSerif", Font.PLAIN, 16);

    /**
     * Fuente utilizada para las etiquetas.
     */
    private final Font fuenteEtiqueta = new Font("SansSerif", Font.BOLD, 16);

    /**
     * Fuente utilizada para los botones.
     */
    private final Font fuenteBoton = new Font("SansSerif", Font.BOLD, 16);

    /**
     * Color de fondo principal del panel.
     */
    private final Color colorFondo = new Color(250, 250, 250);

    /**
     * Color primario para botones activos (login, registro).
     */
    private final Color colorPrimario = new Color(70, 130, 255);

    /**
     * Color para el botón de salir.
     */
    private final Color colorSalir = new Color(120, 120, 120);

    /**
     * Constructor que crea y configura el panel de login, incluyendo todos
     * los componentes gráficos y los eventos asociados.
     */
    public LoginPanel() {
        setLayout(new BorderLayout());
        setBackground(colorFondo);

        // Panel superior con logo
        JLabel lblLogoImg = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logotipo.png")); // Reemplaza con tu ruta
        Image img = logoIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        lblLogoImg.setIcon(new ImageIcon(img));
        lblLogoImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogoImg.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblLogoImg, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Etiqueta Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Nombre de Usuario:", JLabel.RIGHT) {{
            setFont(fuenteEtiqueta);
        }}, gbc);

        // Campo Usuario
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(fuenteTexto);
        formPanel.add(txtUsuario, gbc);

        // Etiqueta Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Contraseña:", JLabel.RIGHT) {{
            setFont(fuenteEtiqueta);
        }}, gbc);

        // Campo Contraseña
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(fuenteTexto);
        formPanel.add(txtPassword, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        btnLogin = crearBoton("Iniciar Sesión", colorPrimario);
        formPanel.add(btnLogin, gbc);

        gbc.gridy = 3;
        btnRegistro = crearBoton("Registrarse", colorPrimario);
        formPanel.add(btnRegistro, gbc);

        gbc.gridy = 4;
        btnSalir = crearBoton("Salir", colorSalir);
        formPanel.add(btnSalir, gbc);

        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        add(formPanel, BorderLayout.CENTER);

        // Eventos
        btnRegistro.addActionListener(e -> {
            JFrame frameRegistro = new JFrame("Registro de Usuario");
            frameRegistro.setContentPane(new RegistroUsuarioPanel());
            frameRegistro.setSize(480, 620);
            frameRegistro.setLocationRelativeTo(null);
            frameRegistro.setVisible(true);
        });

        btnLogin.addActionListener(e -> autenticarUsuario());
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    /**
     * Crea un botón con el texto y color especificados, aplicando el estilo
     * definido para los botones del panel.
     *
     * @param texto Texto que mostrará el botón.
     * @param color Color de fondo del botón.
     * @return JButton configurado con el estilo y texto indicados.
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteBoton);
        boton.setFocusPainted(false);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));
        return boton;
    }

    /**
     * Método que realiza la autenticación del usuario usando los datos ingresados
     * en los campos de texto. Si la autenticación es correcta, abre la ventana principal
     * con el usuario autenticado y cierra el panel de login. En caso contrario, muestra
     * un mensaje de error.
     */
    private void autenticarUsuario() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        Usuario usuarioLogueado = UsuarioDAO.autenticar(usuario, password);

        if (usuarioLogueado != null) {
            JFrame ventanaPrincipal = new VentanaPrincipal(usuarioLogueado);
            ventanaPrincipal.setSize(1200, 800);
            ventanaPrincipal.setLocationRelativeTo(null);
            ventanaPrincipal.setVisible(true);

            JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            loginFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class LoginPanel extends JPanel {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JButton btnSalir;
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());

    private final Font fuenteBoton = new Font("SansSerif", Font.BOLD, 14);
    private final Color colorHover = new Color(21, 101, 192);
    private final Color colorFondo = new Color(245, 245, 250);
    private final Color colorSalir = new Color(120, 120, 120);
    private final Color colorActivo = new Color(70, 130, 255);


    public LoginPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setBackground(colorFondo);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel lblLogo = new JLabel("📚", SwingConstants.CENTER);
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 48));
        JLabel lblTitulo = new JLabel("Biblioteca Central", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(lblLogo, BorderLayout.NORTH);
        topPanel.add(lblTitulo, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        txtUsuario = new JTextField(15);
        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);

        btnLogin = crearBoton("Iniciar Sesión", colorActivo);
        btnRegistro = crearBoton("Registrarse", colorActivo);
        btnSalir = crearBoton("Salir", colorSalir);


        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(btnLogin, gbc);

        gbc.gridy = 3;
        formPanel.add(btnRegistro, gbc);

        gbc.gridy = 4;
        formPanel.add(btnSalir, gbc);


        add(formPanel, BorderLayout.CENTER);
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

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteBoton);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        return boton;
    }

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

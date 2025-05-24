package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.jasper.GeneradorReporte;
import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.HibernateUtil;
import com.pablobn.biblioteca.util.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class VentanaPrincipal extends JFrame {
    private final Usuario usuarioLogueado;
    private final JPanel panelContenido;
    private final CardLayout cardLayout;

    private final Map<String, JPanel> vistasPorEntidad = new HashMap<>();

    private final Font fuenteBoton = new Font("SansSerif", Font.BOLD, 14);

    private final Color colorFondo = new Color(245, 245, 250);
    private final Color colorHover = new Color(220, 230, 255);
    private final Color colorActivo = new Color(70, 130, 255);
    private final Color colorTextoNormal = new Color(50, 50, 50);
    private final Color colorTextoActivo = Color.WHITE;

    private JButton botonSeleccionado = null;



    public VentanaPrincipal(Usuario usuario) {
        this.usuarioLogueado = usuario;

        setTitle("Biblioteca - Bienvenido " + usuario.getNombreUsuario());
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        add(crearPanelIzquierdo(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        add(panelContenido, BorderLayout.CENTER);

        agregarVista("Libros", new PanelEntidad("Libros", usuarioLogueado));
        agregarVista("Autores", new PanelEntidad("Autores", usuarioLogueado));
        agregarVista("Préstamos", new PanelEntidad("Préstamos", usuarioLogueado));

        if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
            agregarVista("Usuarios", new PanelEntidad("Usuarios", usuarioLogueado));
        }


        setVisible(true);
    }


    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, getHeight()));
        panel.setBackground(colorFondo);

        JPanel infoUsuario = new JPanel(new GridLayout(2, 1));
        infoUsuario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        infoUsuario.setBackground(colorFondo);

        JLabel nombre = new JLabel(usuarioLogueado.getNombreUsuario().toUpperCase());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        nombre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nombre.setForeground(new Color(30, 70, 160));
        nombre.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame perfilFrame = new JFrame("Mi Perfil");
                perfilFrame.setContentPane(new PanelPerfilUsuario(usuarioLogueado));
                perfilFrame.setSize(500, 600);
                perfilFrame.setLocationRelativeTo(null);
                perfilFrame.setVisible(true);
            }
        });


        JLabel rol = new JLabel(usuarioLogueado.getTipoUsuario().name().toUpperCase());
        rol.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rol.setForeground(new Color(100, 100, 100));

        infoUsuario.add(nombre);
        infoUsuario.add(rol);

        panel.add(infoUsuario, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(colorFondo);
        menu.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] opciones = {"Libros", "Autores", "Préstamos"};
        for (String opcion : opciones) {
            JButton boton = crearBotonMenu(opcion);
            menu.add(boton);
            menu.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        if (usuarioLogueado.getTipoUsuario() == TipoUsuario.ADMIN) {
            JButton boton = crearBotonMenu("Usuarios");
            menu.add(boton);
            menu.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JLabel labelInformes = new JLabel("INFORMES");
        labelInformes.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelInformes.setForeground(new Color(70, 130, 255));
        labelInformes.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 10));
        labelInformes.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(labelInformes);

        JButton btnInformeLibrosAutor = new JButton("Libros por Autor");
        btnInformeLibrosAutor.setFont(fuenteBoton);
        btnInformeLibrosAutor.setFocusPainted(false);
        btnInformeLibrosAutor.setBackground(colorFondo);
        btnInformeLibrosAutor.setForeground(colorTextoNormal);
        btnInformeLibrosAutor.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));
        btnInformeLibrosAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInformeLibrosAutor.setMaximumSize(new Dimension(300, 40));
        btnInformeLibrosAutor.setHorizontalAlignment(SwingConstants.LEFT);
        btnInformeLibrosAutor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInformeLibrosAutor.addActionListener(e -> generarInformeLibrosPorAutor());

        menu.add(btnInformeLibrosAutor);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnInformePrestamosUsuario = new JButton("Préstamos por Usuario");
        btnInformePrestamosUsuario.setFont(fuenteBoton);
        btnInformePrestamosUsuario.setFocusPainted(false);
        btnInformePrestamosUsuario.setBackground(colorFondo);
        btnInformePrestamosUsuario.setForeground(colorTextoNormal);
        btnInformePrestamosUsuario.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));
        btnInformePrestamosUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInformePrestamosUsuario.setMaximumSize(new Dimension(300, 40));
        btnInformePrestamosUsuario.setHorizontalAlignment(SwingConstants.LEFT);
        btnInformePrestamosUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInformePrestamosUsuario.addActionListener(e -> generarInformePrestamosPorUsuario());

        menu.add(btnInformePrestamosUsuario);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnGraficoPrestamos = new JButton("Préstamos Activos/Finalizados");
        btnGraficoPrestamos.setFont(fuenteBoton);
        btnGraficoPrestamos.setFocusPainted(false);
        btnGraficoPrestamos.setBackground(colorFondo);
        btnGraficoPrestamos.setForeground(colorTextoNormal);
        btnGraficoPrestamos.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));
        btnGraficoPrestamos.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGraficoPrestamos.setMaximumSize(new Dimension(300, 40));
        btnGraficoPrestamos.setHorizontalAlignment(SwingConstants.LEFT);
        btnGraficoPrestamos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGraficoPrestamos.addActionListener(e -> generarInformeGraficoPrestamos());

        menu.add(btnGraficoPrestamos);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));


        panel.add(menu, BorderLayout.CENTER);

        panel.add(menu, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorFondo);
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(fuenteBoton);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBackground(new Color(150, 150, 150));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(300, 40));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setContentPane(new LoginPanel());
                loginFrame.setSize(500, 500);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(fuenteBoton);
        btnSalir.setFocusPainted(false);
        btnSalir.setBackground(new Color(120, 120, 120));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSalir.setMaximumSize(new Dimension(300, 40));
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        addHoverEffect(btnCerrarSesion, new Color(130, 130, 130), new Color(150, 150, 150));
        addHoverEffect(btnSalir, new Color(90, 90, 90), new Color(120, 120, 120));

        panelInferior.add(btnCerrarSesion);
        panelInferior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInferior.add(btnSalir);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private void generarInformeGraficoPrestamos() {
        GeneradorReporte.mostrarInformeGraficoPrestamos(HibernateUtil.getSession());
    }

    private void generarInformePrestamosPorUsuario() {
        GeneradorReporte.mostrarInformePrestamosPorUsuario(HibernateUtil.getSession());
    }

    private void generarInformeLibrosPorAutor() {
        JFrame ventanaOpciones = new JFrame("Opciones de informe");
        ventanaOpciones.setSize(400, 250);
        ventanaOpciones.setLocationRelativeTo(null);
        ventanaOpciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaOpciones.setLayout(new GridBagLayout());

        Font fuenteBoton = new Font("SansSerif", Font.BOLD, 16);
        Color colorPrincipal = new Color(33, 150, 243);
        Color colorTexto = Color.WHITE;
        Color colorSalir = new Color(120, 120, 120);

        JButton btnInforme = new JButton("Ver Informe Detallado");
        JButton btnGrafico = new JButton("Ver Gráfico de Libros por Autor");
        JButton btnSalir = new JButton("Salir");

        JButton[] botones = {btnInforme, btnGrafico};
        for (JButton boton : botones) {
            boton.setFont(fuenteBoton);
            boton.setBackground(colorPrincipal);
            boton.setForeground(colorTexto);
            boton.setFocusPainted(false);
            boton.setPreferredSize(new Dimension(280, 40));
        }

        btnSalir.setFont(fuenteBoton);
        btnSalir.setBackground(colorSalir);
        btnSalir.setForeground(colorTexto);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(280, 40));

        btnInforme.addActionListener(e -> {
            ventanaOpciones.dispose();
            GeneradorReporte.mostrarInformeLibrosPorAutor(HibernateUtil.getSession());
        });

        btnGrafico.addActionListener(e -> {
            ventanaOpciones.dispose();
            GeneradorReporte.mostrarGraficoLibrosPorAutor(HibernateUtil.getSession());
        });

        btnSalir.addActionListener(e -> ventanaOpciones.dispose());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        ventanaOpciones.add(btnInforme, gbc);

        gbc.gridy = 1;
        ventanaOpciones.add(btnGrafico, gbc);

        gbc.gridy = 2;
        ventanaOpciones.add(btnSalir, gbc);

        ventanaOpciones.setVisible(true);
    }




    private void addHoverEffect(JButton btn, Color hoverColor, Color normalColor) {
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(normalColor);
            }
        });
    }
    private JButton crearBotonMenu(String nombre) {
        JButton boton = new JButton(nombre.toUpperCase());
        boton.setFont(fuenteBoton);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setBackground(colorFondo);
        boton.setForeground(colorTextoNormal);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 10));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setMaximumSize(new Dimension(300, 50));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorHover);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorFondo);
                }
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (botonSeleccionado != null) {
                    botonSeleccionado.setBackground(colorFondo);
                    botonSeleccionado.setForeground(colorTextoNormal);
                }

                boton.setBackground(colorActivo);
                boton.setForeground(colorTextoActivo);
                botonSeleccionado = boton;

                cardLayout.show(panelContenido, nombre);
                if (nombre.equals("Préstamos")) {
                    JPanel panel = vistasPorEntidad.get("Préstamos");
                    if (panel instanceof PanelEntidad) {
                        ((PanelEntidad) panel).cargarDatos();
                    }
                }
            }
        });

        return boton;
    }

    private void agregarVista(String clave, JPanel panel) {
        panelContenido.add(panel, clave);
        vistasPorEntidad.put(clave, panel);
    }
}


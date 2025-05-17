package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.jasper.GeneradorReporte;
import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.HibernateUtil;
import com.pablobn.biblioteca.util.TipoUsuario;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class VentanaPrincipal extends JFrame {
    private final Usuario usuarioLogueado;
    private final JPanel panelContenido;
    private final CardLayout cardLayout;

    private final Map<String, JPanel> vistasPorEntidad = new HashMap<>();

    private final Font fuenteBoton = new Font("SansSerif", Font.BOLD, 14);

    private final Color colorFondo = new Color(245, 245, 250);        // fondo gris muy claro
    private final Color colorHover = new Color(220, 230, 255);        // hover azul claro
    private final Color colorActivo = new Color(70, 130, 255);        // azul intenso
    private final Color colorTextoNormal = new Color(50, 50, 50);     // gris oscuro
    private final Color colorTextoActivo = Color.WHITE;

    private JButton botonSeleccionado = null;



    public VentanaPrincipal(Usuario usuario) {
        this.usuarioLogueado = usuario;

        setTitle("Biblioteca - Bienvenido " + usuario.getNombreUsuario());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Panel lateral izquierdo
        add(crearPanelIzquierdo(), BorderLayout.WEST);

        // Panel central con CardLayout para cambiar de vista
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        add(panelContenido, BorderLayout.CENTER);

        // Añadir vistas (puedes ir añadiendo más)
        agregarVista("Libros", new PanelEntidad("Libros", usuarioLogueado));
        agregarVista("Autores", new PanelEntidad("Autores", usuarioLogueado));
        agregarVista("Préstamos", new PanelEntidad("Préstamos", usuarioLogueado));

        // Si el usuario es ADMIN, añadir la vista de Usuarios
        if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
            agregarVista("Usuarios", new PanelEntidad("Usuarios", usuarioLogueado));
        }

        // Si el usuario es un Editor, no añadir la pestaña de "Usuarios"
        if (usuario.getTipoUsuario() == TipoUsuario.EDITOR) {
            // Puedes omitir la vista de "Usuarios" directamente aquí, sin agregarla.
        }

        setVisible(true);
    }


    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, getHeight()));
        panel.setBackground(colorFondo);

        // Info de usuario arriba
        JPanel infoUsuario = new JPanel(new GridLayout(2, 1));
        infoUsuario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        infoUsuario.setBackground(colorFondo);

        JLabel nombre = new JLabel(usuarioLogueado.getNombreUsuario().toUpperCase());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        nombre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nombre.setForeground(new Color(30, 70, 160)); // estilo link
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

        // Menú de botones
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(colorFondo);
        menu.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] opciones = {"Libros", "Autores", "Préstamos"};
        for (String opcion : opciones) {
            JButton boton = crearBotonMenu(opcion);
            menu.add(boton);
            menu.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre botones
        }

        if (usuarioLogueado.getTipoUsuario() == TipoUsuario.ADMIN) {
            JButton boton = crearBotonMenu("Usuarios");
            menu.add(boton);
            menu.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        // --- Aquí añadimos el bloque de botones de informes ---
        // Título sección informes
        JLabel labelInformes = new JLabel("INFORMES");
        labelInformes.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelInformes.setForeground(new Color(70, 130, 255));
        labelInformes.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 10));
        labelInformes.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(labelInformes);

        // Botón informe "Libros por Autor"
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

        // Aquí puedes añadir más botones para otros informes igual

        panel.add(menu, BorderLayout.CENTER);

        // Panel inferior (cerrar sesión, salir) - igual que antes
        // ...


        panel.add(menu, BorderLayout.CENTER);

        // Panel de botones inferiores
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorFondo);
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Botón cerrar sesión
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

        // Botón salir
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

        // Hover effects (opcionales)
        addHoverEffect(btnCerrarSesion, new Color(130, 130, 130), new Color(150, 150, 150));
        addHoverEffect(btnSalir, new Color(90, 90, 90), new Color(120, 120, 120));

        // Añadir botones al panel inferior
        panelInferior.add(btnCerrarSesion);
        panelInferior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInferior.add(btnSalir);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private void generarInformeLibrosPorAutor() {
        GeneradorReporte.mostrarInformeLibrosPorAutor(HibernateUtil.getSession());
    }


    private void addHoverEffect(JButton btn, Color hoverColor, Color normalColor) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
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
        boton.setBackground(colorFondo);  // Color de fondo por defecto (gris muy claro)
        boton.setForeground(colorTextoNormal);  // Color de texto gris oscuro
        boton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 10));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setMaximumSize(new Dimension(300, 50));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorHover);  // Cambiar al color hover cuando el mouse pasa por encima
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorFondo);  // Volver al color por defecto cuando el mouse sale
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (botonSeleccionado != null) {
                    botonSeleccionado.setBackground(colorFondo);  // Restablecer el color del botón previamente seleccionado
                    botonSeleccionado.setForeground(colorTextoNormal);
                }

                boton.setBackground(colorActivo);  // Color cuando el botón es seleccionado
                boton.setForeground(colorTextoActivo);  // Cambiar el color del texto a blanco
                botonSeleccionado = boton;

                cardLayout.show(panelContenido, nombre);  // Cambiar la vista asociada
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


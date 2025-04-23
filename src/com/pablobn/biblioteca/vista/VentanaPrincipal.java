package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.TipoUsuario;

import javax.swing.*;
import java.awt.*;
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
        agregarVista("Libros", new PanelEntidad("Libros", usuarioLogueado.getTipoUsuario()));
        agregarVista("Autores", new PanelEntidad("Autores", usuarioLogueado.getTipoUsuario()));
        agregarVista("Préstamos", new PanelEntidad("Préstamos", usuarioLogueado.getTipoUsuario()));
        if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
            agregarVista("Usuarios", new PanelEntidad("Usuarios", usuarioLogueado.getTipoUsuario()));
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

        JLabel nombre = new JLabel(usuarioLogueado.getNombreCompleto().toUpperCase());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 16));

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

        panel.add(menu, BorderLayout.CENTER);

        return panel;
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

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorHover);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(colorFondo);
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (botonSeleccionado != null) {
                    botonSeleccionado.setBackground(colorFondo);
                    botonSeleccionado.setForeground(colorTextoNormal);
                }

                boton.setBackground(colorActivo);
                boton.setForeground(colorTextoActivo);
                botonSeleccionado = boton;

                cardLayout.show(panelContenido, nombre);
            }
        });

        return boton;
    }




    private void agregarVista(String clave, JPanel panel) {
        panelContenido.add(panel, clave);
        vistasPorEntidad.put(clave, panel);
    }
}


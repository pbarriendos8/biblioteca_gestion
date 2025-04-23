package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.util.TipoUsuario;
import com.pablobn.biblioteca.vista.forms.FormularioAutorNew;

import javax.swing.*;
import java.awt.*;

public class PanelEntidad extends JPanel {
    private final String entidad;
    private final TipoUsuario tipoUsuario;
    private final JButton btnNuevo;
    private final JButton btnEditar;
    private final JButton btnEliminar;

    public PanelEntidad(String entidad, TipoUsuario tipoUsuario) {
        this.entidad = entidad;
        this.tipoUsuario = tipoUsuario;

        setLayout(new BorderLayout());

        // Crear botones de acciones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.NORTH);

        // Panel central para mostrar los datos de la entidad (Ej: Lista de autores)
        JPanel panelCentro = new JPanel(new BorderLayout());
        // Aquí puedes agregar una tabla o lista para mostrar los autores, libros, etc.
        // Por ahora solo un área de texto como ejemplo:
        JTextArea areaTexto = new JTextArea(20, 50);
        areaTexto.setText("Mostrar los datos de la entidad " + entidad + " aquí...");
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Configurar accesos dependiendo del tipo de usuario
        if (tipoUsuario == TipoUsuario.CONSULTA) {
            btnNuevo.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        } else if (tipoUsuario == TipoUsuario.EDITOR) {
            btnNuevo.setEnabled(true);
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        } else if (tipoUsuario == TipoUsuario.ADMIN) {
            btnNuevo.setEnabled(true);
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }

        // Acción de botones
        btnNuevo.addActionListener(e -> mostrarVentanaNuevo());
        btnEditar.addActionListener(e -> mostrarVentanaEditar());
        btnEliminar.addActionListener(e -> mostrarVentanaEliminar());
    }

    // Mostrar ventana para agregar nuevo
    private void mostrarVentanaNuevo() {
        if (entidad.equals("Autores")) {
            new FormularioAutorNew((JFrame) SwingUtilities.getWindowAncestor(this));
        } else {
            JOptionPane.showMessageDialog(this, "Ventana para agregar nuevo " + entidad);
        }
    }

    // Mostrar ventana para editar
    private void mostrarVentanaEditar() {
        // Este método abre un formulario para editar los datos existentes
        JOptionPane.showMessageDialog(this, "Ventana para editar " + entidad);
    }

    // Mostrar ventana para eliminar
    private void mostrarVentanaEliminar() {
        // Este método abre una ventana de confirmación para eliminar
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este " + entidad + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Eliminado con éxito.");
        }
    }
}

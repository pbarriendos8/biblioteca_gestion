package com.pablobn.biblioteca.vista.forms.formsedit;

import com.pablobn.biblioteca.modelo.*;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import com.pablobn.biblioteca.modelo.dao.PrestamoDAO;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.DateFormat;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

public class FormularioPrestamoEdit extends JDialog {

    private final UtilDateModel modelFechaInicio = new UtilDateModel();
    private final UtilDateModel modelFechaFin = new UtilDateModel();
    private final JDatePickerImpl datePickerInicio;
    private final JDatePickerImpl datePickerFin;
    private final JComboBox<Usuario> comboUsuarios;
    private final JComboBox<Libro> comboLibros;
    private final JTextArea textObservaciones;
    private final Usuario usuarioActual;
    private final Prestamo prestamo;

    public FormularioPrestamoEdit(JFrame parent, Prestamo prestamo, Usuario usuarioActual) {
        super(parent, "Editar Préstamo", true);
        this.prestamo = prestamo;
        this.usuarioActual = usuarioActual;

        setSize(600, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelPrincipal);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 13);

        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelFechaInicio, p), new DateFormat());
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFechaFin, p), new DateFormat());
        datePickerInicio.getJFormattedTextField().setFont(inputFont);
        datePickerFin.getJFormattedTextField().setFont(inputFont);

        comboUsuarios = new JComboBox<>();
        comboUsuarios.setFont(inputFont);
        comboLibros = new JComboBox<>();
        comboLibros.setFont(inputFont);

        cargarUsuarios();
        cargarLibros();

        textObservaciones = new JTextArea(5, 30);
        textObservaciones.setFont(inputFont);
        textObservaciones.setLineWrap(true);
        textObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObservaciones = new JScrollPane(textObservaciones);
        scrollObservaciones.setPreferredSize(new Dimension(300, 100));

        agregarCampo(panelCampos, gbc, "Fecha Inicio:", datePickerInicio, labelFont, 0);
        agregarCampo(panelCampos, gbc, "Fecha Fin:", datePickerFin, labelFont, 1);
        if (usuarioActual.getTipoUsuario() != TipoUsuario.CONSULTA) {
            agregarCampo(panelCampos, gbc, "Usuario:", comboUsuarios, labelFont, 2);
            agregarCampo(panelCampos, gbc, "Libro:", comboLibros, labelFont, 3);
            gbc.gridy = 4;
        } else {
            comboUsuarios.setVisible(false);
            agregarCampo(panelCampos, gbc, "Libro:", comboLibros, labelFont, 2);
            gbc.gridy = 3;
        }


        gbc.gridx = 0;
        panelCampos.add(new JLabel("Observaciones:", JLabel.RIGHT), gbc);

        gbc.gridx = 1;
        panelCampos.add(scrollObservaciones, gbc);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
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


        cargarDatosPrestamo();

        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String texto, JComponent componente, Font fontLabel, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lbl = new JLabel(texto, JLabel.RIGHT);
        lbl.setFont(fontLabel);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void cargarUsuarios() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
        for (Usuario u : usuarios) {
            comboUsuarios.addItem(u);
        }
    }

    private void cargarLibros() {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerTodosLibros();
        for (Libro l : libros) {
            comboLibros.addItem(l);
        }
    }

    private void cargarDatosPrestamo() {
        modelFechaInicio.setValue(prestamo.getFechaInicio());
        modelFechaFin.setValue(prestamo.getFechaFin());

        comboUsuarios.setSelectedItem(prestamo.getUsuario());
        comboLibros.setSelectedItem(prestamo.getLibro());
        textObservaciones.setText(prestamo.getObservaciones());
    }

    private void guardarCambios() {
        java.util.Date fechaInicioUtil = (java.util.Date) datePickerInicio.getModel().getValue();
        java.util.Date fechaFinUtil = (java.util.Date) datePickerFin.getModel().getValue();

        if (fechaInicioUtil == null || fechaFinUtil == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar ambas fechas.");
            return;
        }

        Usuario usuarioSeleccionado = (Usuario) comboUsuarios.getSelectedItem();
        Libro libroSeleccionado = (Libro) comboLibros.getSelectedItem();

        if (usuarioSeleccionado == null || libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un usuario y un libro.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas guardar los cambios en este préstamo?",
                "Confirmar edición",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // Asignar nuevos valores al préstamo
        prestamo.setFechaInicio(new Date(fechaInicioUtil.getTime()));
        prestamo.setFechaFin(new Date(fechaFinUtil.getTime()));
        prestamo.setUsuario(usuarioSeleccionado);
        prestamo.setLibro(libroSeleccionado);
        prestamo.setObservaciones(textObservaciones.getText());

        // Guardar cambios en la base de datos
        PrestamoDAO.actualizarPrestamo(prestamo);

        JOptionPane.showMessageDialog(this, "Préstamo actualizado correctamente.");
        dispose(); // cerrar la ventana
    }
}

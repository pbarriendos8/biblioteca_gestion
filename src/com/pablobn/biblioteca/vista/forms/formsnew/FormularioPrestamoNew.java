package com.pablobn.biblioteca.vista.forms.formsnew;

import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.modelo.Prestamo;
import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import com.pablobn.biblioteca.modelo.dao.PrestamoDAO;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.DateFormat;
import com.pablobn.biblioteca.util.EstadoPrestamo;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

public class FormularioPrestamoNew extends JDialog {

    private final UtilDateModel modelFechaInicio = new UtilDateModel();
    private final UtilDateModel modelFechaFin = new UtilDateModel();
    private final JDatePickerImpl datePickerInicio;
    private final JDatePickerImpl datePickerFin;
    private final JComboBox<Usuario> comboUsuarios;
    private final JComboBox<Libro> comboLibros;
    private final JTextArea textObservaciones;
    private final Usuario usuarioActual;

    public FormularioPrestamoNew(JFrame parent, Usuario usuarioActual) {
        super(parent, "Nuevo Préstamo", true);
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

        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 13);
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelFechaInicio, p), new DateFormat());
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFechaFin, p), new DateFormat());
        datePickerInicio.getJFormattedTextField().setFont(inputFont);
        datePickerFin.getJFormattedTextField().setFont(inputFont);

        if (usuarioActual.getTipoUsuario() != TipoUsuario.CONSULTA) {
            comboUsuarios = new JComboBox<>();
            comboUsuarios.setFont(inputFont);
            cargarUsuarios();
            agregarCampo(panelCampos, gbc, "Usuario:", comboUsuarios, labelFont, 2);
        } else {
            comboUsuarios = null;
        }
        comboLibros = new JComboBox<>();
        comboLibros.setFont(inputFont);

        cargarLibros();
        textObservaciones = new JTextArea(5, 30);
        textObservaciones.setFont(inputFont);
        textObservaciones.setLineWrap(true);
        textObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObservaciones = new JScrollPane(textObservaciones);
        scrollObservaciones.setPreferredSize(new Dimension(300, 100));
        agregarCampo(panelCampos, gbc, "Fecha Inicio:", datePickerInicio, labelFont, 0);
        agregarCampo(panelCampos, gbc, "Fecha Fin:", datePickerFin, labelFont, 1);
        agregarCampo(panelCampos, gbc, "Libro:", comboLibros, labelFont, (usuarioActual.getTipoUsuario() == TipoUsuario.CONSULTA ? 2 : 3));


        gbc.gridx = 0;
        gbc.gridy = (usuarioActual.getTipoUsuario() == TipoUsuario.CONSULTA ? 3 : 4);
        panelCampos.add(new JLabel("Observaciones:", JLabel.RIGHT), gbc);

        gbc.gridx = 1;
        panelCampos.add(scrollObservaciones, gbc);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnGuardar = new JButton("Guardar Préstamo");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.addActionListener(e -> guardarPrestamo());
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
        comboUsuarios.addItem(null);
        List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
        for (Usuario u : usuarios) {
            comboUsuarios.addItem(u);
        }
        comboUsuarios.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Selecciona un usuario --");
                } else {
                    setText(value.toString());
                }
                return this;
            }
        });
    }

    private void cargarLibros() {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerTodosLibros();
        comboLibros.addItem(null);
        for (Libro l : libros) {
            comboLibros.addItem(l);
        }
        comboLibros.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Selecciona un libro --");
                } else {
                    setText(value.toString());
                }
                return this;
            }
        });
    }

    private void guardarPrestamo() {
        java.util.Date fechaInicioUtil = (java.util.Date) datePickerInicio.getModel().getValue();
        java.util.Date fechaFinUtil = (java.util.Date) datePickerFin.getModel().getValue();
        comboLibros.setBorder(UIManager.getBorder("ComboBox.border"));
        if (!usuarioActual.getTipoUsuario().equals(TipoUsuario.CONSULTA)) {
            comboUsuarios.setBorder(UIManager.getBorder("ComboBox.border"));
        }
        StringBuilder errores = new StringBuilder();
        boolean hayErrores = false;

        if (usuarioActual.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
            if (fechaInicioUtil == null) {
                errores.append("- La fecha de inicio es obligatoria.\n");
                hayErrores = true;
            }
            if (fechaFinUtil == null) {
                errores.append("- La fecha de fin es obligatoria.\n");
                hayErrores = true;
            }
            Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
            if (usuario == null) {
                errores.append("- Debes seleccionar un usuario.\n");
                comboUsuarios.setBorder(BorderFactory.createLineBorder(Color.RED));
                hayErrores = true;
            }
            Libro libro = (Libro) comboLibros.getSelectedItem();
            if (libro == null) {
                errores.append("- Debes seleccionar un libro.\n");
                comboLibros.setBorder(BorderFactory.createLineBorder(Color.RED));
                hayErrores = true;
            }

        }
        if (usuarioActual.getTipoUsuario().equals(TipoUsuario.CONSULTA)) {
            if (fechaInicioUtil == null) {
                errores.append("- La fecha de inicio es obligatoria.\n");
                hayErrores = true;
            }
            if (fechaFinUtil == null) {
                errores.append("- La fecha de fin es obligatoria.\n");
                hayErrores = true;
            }
            Libro libro = (Libro) comboLibros.getSelectedItem();
            if (libro == null) {
                errores.append("- Debes seleccionar un libro.\n");
                comboLibros.setBorder(BorderFactory.createLineBorder(Color.RED));
                hayErrores = true;
            }
        }

        if (fechaInicioUtil != null && fechaFinUtil != null && fechaFinUtil.before(fechaInicioUtil)) {
            errores.append("- La fecha de fin no puede ser anterior a la fecha de inicio.\n");
            hayErrores = true;
        }
        if (hayErrores) {
            JOptionPane.showMessageDialog(this, "Corrige los siguientes errores:\n" + errores.toString(),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fechaInicio = new Date(fechaInicioUtil.getTime());
        Date fechaFin = new Date(fechaFinUtil.getTime());

        Usuario usuarioSeleccionado = (usuarioActual.getTipoUsuario() == TipoUsuario.CONSULTA)
                ? usuarioActual
                : (Usuario) comboUsuarios.getSelectedItem();
        Libro libroSeleccionado = (Libro) comboLibros.getSelectedItem();

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaFin(fechaFin);
        prestamo.setUsuario(usuarioSeleccionado);
        prestamo.setLibro(libroSeleccionado);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        prestamo.setObservaciones(textObservaciones.getText());

        PrestamoDAO.guardarPrestamo(prestamo, usuarioSeleccionado.getIdUsuario(), libroSeleccionado.getIdLibro());
        JOptionPane.showMessageDialog(this, "Préstamo guardado con éxito.");
        dispose();
    }
}

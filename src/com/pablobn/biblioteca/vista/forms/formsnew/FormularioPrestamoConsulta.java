package com.pablobn.biblioteca.vista.forms.formsnew;

import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.modelo.Prestamo;
import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.PrestamoDAO;
import com.pablobn.biblioteca.util.DateFormat;
import com.pablobn.biblioteca.util.EstadoPrestamo;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Properties;

public class FormularioPrestamoConsulta extends JDialog {
    private final UtilDateModel modelFechaInicio = new UtilDateModel();
    private final UtilDateModel modelFechaFin = new UtilDateModel();
    private final JDatePickerImpl datePickerInicio;
    private final JDatePickerImpl datePickerFin;
    private final JTextArea textObservaciones;
    private final Libro libroSeleccionado;
    private final Usuario usuarioConsultor;

    public FormularioPrestamoConsulta(JFrame parent, Libro libro, Usuario usuarioConsultor) {
        super(parent, "Realizar Préstamo", true);
        this.libroSeleccionado = libro;
        this.usuarioConsultor = usuarioConsultor;
        setSize(600, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelPrincipal);
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelFechaInicio, p), new DateFormat());
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFechaFin, p), new DateFormat());
        textObservaciones = new JTextArea(5, 30);
        JScrollPane scrollObservaciones = new JScrollPane(textObservaciones);
        scrollObservaciones.setPreferredSize(new Dimension(300, 100));
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 13);

        agregarCampo(panelCampos, gbc, "Fecha Inicio:", datePickerInicio, labelFont, 0);
        agregarCampo(panelCampos, gbc, "Fecha Fin:", datePickerFin, labelFont, 1);
        agregarCampo(panelCampos, gbc, "Libro:", new JLabel(libro.getTitulo()), labelFont, 2);
        agregarCampo(panelCampos, gbc, "Observaciones:", scrollObservaciones, labelFont, 3);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnConfirmar = new JButton("Confirmar Préstamo");
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnConfirmar.setBackground(new Color(33, 150, 243));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setPreferredSize(new Dimension(160, 40));
        btnConfirmar.addActionListener(e -> confirmarPrestamo());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(120, 120, 120));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setPreferredSize(new Dimension(160, 40));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

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

    private void confirmarPrestamo() {
        java.util.Date fechaInicioUtil = (java.util.Date) datePickerInicio.getModel().getValue();
        java.util.Date fechaFinUtil = (java.util.Date) datePickerFin.getModel().getValue();
        StringBuilder errores = new StringBuilder();
        boolean hayErrores = false;

        if (fechaInicioUtil == null) {
            errores.append("- La fecha de inicio es obligatoria.\n");
            hayErrores = true;
        }
        if (fechaFinUtil == null) {
            errores.append("- La fecha de fin es obligatoria.\n");
            hayErrores = true;
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

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaFin(fechaFin);
        prestamo.setUsuario(usuarioConsultor);
        prestamo.setLibro(libroSeleccionado);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        prestamo.setObservaciones(textObservaciones.getText());

        PrestamoDAO.guardarPrestamo(prestamo, usuarioConsultor.getIdUsuario(), libroSeleccionado.getIdLibro());
        JOptionPane.showMessageDialog(this, "Préstamo realizado con éxito.");
        dispose();
    }
}


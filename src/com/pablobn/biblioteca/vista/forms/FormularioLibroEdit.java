package com.pablobn.biblioteca.vista.forms;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

public class FormularioLibroEdit extends JDialog {
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JTextField txtIsbn;
    private JComboBox<Autor> cmbAutores;
    private JLabel lblPortada;
    private byte[] portadaBytes;
    private JLabel lblArchivoPdf;
    private byte[] archivoPdfBytes;
    private JDatePickerImpl datePicker;
    private Libro libro;

    public FormularioLibroEdit(JFrame parent, Libro libro) {
        super(parent, "Editar Libro", true);
        this.libro = libro;
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

        agregarCampo(panelCampos, gbc, "Título:", txtTitulo = new JTextField(libro.getTitulo()), labelFont, inputFont, 0);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCampos.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextArea(5, 30);
        txtDescripcion.setFont(inputFont);
        txtDescripcion.setText(libro.getDescripcion());
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(300, 100));
        panelCampos.add(scrollDescripcion, gbc);

        UtilDateModel model = new UtilDateModel();
        if (libro.getFechaPublicacion() != null) {
            model.setValue(libro.getFechaPublicacion());
        }
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        datePicker = new JDatePickerImpl(datePanel, new com.pablobn.biblioteca.util.DateFormat());
        datePicker.getJFormattedTextField().setFont(inputFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCampos.add(new JLabel("Fecha Publicación:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(datePicker, gbc);

        agregarCampo(panelCampos, gbc, "ISBN:", txtIsbn = new JTextField(libro.getIsbn()), labelFont, inputFont, 3);

        List<Autor> autores = AutorDAO.obtenerTodos();
        cmbAutores = new JComboBox<>(autores.toArray(new Autor[0]));
        cmbAutores.setSelectedItem(libro.getAutor());
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelCampos.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(cmbAutores, gbc);

        // Portada
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblPortada = new JLabel("Haz clic para subir una portada");
        lblPortada.setHorizontalAlignment(SwingConstants.CENTER);
        lblPortada.setPreferredSize(new Dimension(180, 180));
        lblPortada.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblPortada.setOpaque(true);
        lblPortada.setBackground(Color.WHITE);
        lblPortada.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblPortada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarPortada();
            }
        });

        if (libro.getPortada() != null) {
            portadaBytes = libro.getPortada();
            ImageIcon icon = new ImageIcon(new ImageIcon(portadaBytes).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            lblPortada.setIcon(icon);
            lblPortada.setText("");
        }

        panelCampos.add(lblPortada, gbc);

        // Archivo PDF
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        lblArchivoPdf = new JLabel("Haz clic para subir el archivo PDF");
        lblArchivoPdf.setHorizontalAlignment(SwingConstants.CENTER);
        lblArchivoPdf.setPreferredSize(new Dimension(180, 40));
        lblArchivoPdf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblArchivoPdf.setOpaque(true);
        lblArchivoPdf.setBackground(Color.WHITE);
        lblArchivoPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblArchivoPdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarArchivoPdf();
            }
        });

        if (libro.getNombreArchivoPdf() != null) {
            lblArchivoPdf.setText(libro.getNombreArchivoPdf());
        }

        if (libro.getArchivoPdf() != null) {
            archivoPdfBytes = libro.getArchivoPdf();
        }

        panelCampos.add(lblArchivoPdf, gbc);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.addActionListener(e -> guardarLibro());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnGuardar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String texto, JTextField campo, Font fontLabel, Font fontInput, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lbl = new JLabel(texto, JLabel.RIGHT);
        lbl.setFont(fontLabel);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        campo.setFont(fontInput);
        panel.add(campo, gbc);
    }

    private void seleccionarPortada() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona una imagen");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try {
                portadaBytes = Files.readAllBytes(archivo.toPath());
                ImageIcon icon = new ImageIcon(new ImageIcon(portadaBytes).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                lblPortada.setIcon(icon);
                lblPortada.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer la portada.");
            }
        }
    }

    private void seleccionarArchivoPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona un archivo PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try {
                archivoPdfBytes = Files.readAllBytes(archivo.toPath());
                lblArchivoPdf.setText(archivo.getName());
                libro.setNombreArchivoPdf(archivo.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo PDF.");
            }
        }
    }

    private void guardarLibro() {
        try {
            libro.setTitulo(txtTitulo.getText());
            libro.setDescripcion(txtDescripcion.getText());
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
            if (selectedDate != null) {
                libro.setFechaPublicacion(new Date(selectedDate.getTime()));
            }
            libro.setIsbn(txtIsbn.getText());
            libro.setAutor((Autor) cmbAutores.getSelectedItem());

            if (portadaBytes != null) {
                libro.setPortada(portadaBytes);
            }

            if (archivoPdfBytes != null) {
                libro.setArchivoPdf(archivoPdfBytes);
            }

            LibroDAO.guardarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro actualizado con éxito.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar libro: " + e.getMessage());
        }
    }
}

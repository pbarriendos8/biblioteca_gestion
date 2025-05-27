package com.pablobn.biblioteca.vista.forms.formsedit;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import org.jdatepicker.impl.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.Properties;

/**
 * Formulario para editar los datos de un autor existente.
 * Muestra los campos del autor cargados y permite modificar su información,
 * incluyendo nombre, apellidos, nacionalidad, biografía, foto y fecha de nacimiento.
 *
 * Al guardar, actualiza el autor en la base de datos.
 */
public class FormularioAutorEdit extends JDialog {
    private final Autor autor;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtNacionalidad;
    private JTextArea txtBiografia;
    private JLabel lblFoto;
    private byte[] fotoBytes;
    private JDatePickerImpl datePicker;

    /**
     * Crea el formulario de edición de autor con los datos precargados.
     *
     * @param parent JFrame padre para centrar y establecer la modal.
     * @param autor Autor a editar cuyos datos se muestran en el formulario.
     */
    public FormularioAutorEdit(JFrame parent, Autor autor) {
        super(parent, "Editar Autor", true);
        this.autor = autor;

        setSize(600, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("EDITAR AUTOR", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(new Color(0, 0, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        add(panelPrincipal);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 13);

        txtNombre = new JTextField(autor.getNombre());
        txtApellidos = new JTextField(autor.getApellidos());
        txtNacionalidad = new JTextField(autor.getNacionalidad());
        txtBiografia = new JTextArea(autor.getBiografia(), 5, 30);
        txtBiografia.setFont(inputFont);
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        fotoBytes = autor.getFoto();

        agregarCampo(panelCampos, gbc, "Nombre:", txtNombre, labelFont, inputFont, 0);
        agregarCampo(panelCampos, gbc, "Apellidos:", txtApellidos, labelFont, inputFont, 1);
        agregarCampo(panelCampos, gbc, "Nacionalidad:", txtNacionalidad, labelFont, inputFont, 2);

        UtilDateModel model = new UtilDateModel();
        if (autor.getFechaNacimiento() != null)
            model.setValue(autor.getFechaNacimiento());
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        datePicker = new JDatePickerImpl(datePanel, new com.pablobn.biblioteca.util.DateFormat());
        datePicker.getJFormattedTextField().setFont(inputFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelCampos.add(new JLabel("Fecha Nacimiento:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panelCampos.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblBiografia = new JLabel("Biografía:", JLabel.RIGHT);
        lblBiografia.setPreferredSize(new Dimension(120, 25));
        panelCampos.add(lblBiografia, gbc);
        gbc.gridx = 1;
        JScrollPane scrollBio = new JScrollPane(txtBiografia);
        scrollBio.setPreferredSize(new Dimension(300, 100));
        panelCampos.add(scrollBio, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblFoto = new JLabel("Haz clic para cambiar foto", JLabel.CENTER);
        lblFoto.setPreferredSize(new Dimension(180, 180));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        lblFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (fotoBytes != null) {
            lblFoto.setIcon(new ImageIcon(new ImageIcon(fotoBytes).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            lblFoto.setText("");
        }
        lblFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFoto();
            }
        });
        panelCampos.add(lblFoto, gbc);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnGuardar = new JButton("Guardar Autor");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.addActionListener(e -> guardarCambios());
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

    /**
     * Agrega un campo JLabel y JTextField al panel con GridBagLayout en la fila especificada.
     *
     * @param panel Panel donde se agregarán los componentes.
     * @param gbc Constraints de GridBagLayout para posicionamiento.
     * @param texto Texto de la etiqueta.
     * @param campo Campo de texto para entrada.
     * @param fontLabel Fuente para la etiqueta.
     * @param fontInput Fuente para el campo de texto.
     * @param fila Número de fila para ubicar los componentes.
     */
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

    /**
     * Abre un JFileChooser para seleccionar una nueva foto para el autor.
     * La imagen seleccionada se lee como bytes y se muestra en el JLabel.
     * En caso de error, muestra un mensaje.
     */
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona una imagen");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg", "gif"));

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try {
                fotoBytes = Files.readAllBytes(archivo.toPath());
                lblFoto.setIcon(new ImageIcon(new ImageIcon(fotoBytes).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
                lblFoto.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer la imagen.");
            }
        }
    }

    /**
     * Valida los campos obligatorios y, si son correctos,
     * actualiza el objeto autor con los datos del formulario y lo guarda en la base de datos.
     * Muestra mensajes de error o confirmación según corresponda.
     */
    private void guardarCambios() {
        txtNombre.setBorder(UIManager.getBorder("TextField.border"));

        StringBuilder errores = new StringBuilder();
        boolean hayErrores = false;
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
            hayErrores = true;
        }
        if (hayErrores) {
            JOptionPane.showMessageDialog(this, "Corrige los siguientes errores:\n" + errores.toString(),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        autor.setNombre(txtNombre.getText());
        autor.setApellidos(txtApellidos.getText());
        autor.setNacionalidad(txtNacionalidad.getText());
        autor.setBiografia(txtBiografia.getText());
        autor.setFoto(fotoBytes);
        java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
        if (selectedDate != null)
            autor.setFechaNacimiento(new Date(selectedDate.getTime()));

        AutorDAO.actualizarAutor(autor);
        JOptionPane.showMessageDialog(this, "Autor actualizado correctamente.");
        dispose();
    }
}

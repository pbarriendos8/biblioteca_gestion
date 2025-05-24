package com.pablobn.biblioteca.vista.forms.formsnew;

import com.pablobn.biblioteca.modelo.Autor;
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
import java.util.Properties;

/**
 * Diálogo modal para crear un nuevo autor.
 * <p>
 * Permite ingresar datos del autor como nombre, apellidos, nacionalidad,
 * fecha de nacimiento, biografía y foto.
 * Valida campos requeridos y guarda el autor usando AutorDAO.
 * </p>
 */
public class FormularioAutorNew extends JDialog {
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtNacionalidad;
    private JTextArea txtBiografia;
    private JLabel lblFoto;
    private byte[] fotoBytes;
    private JDatePickerImpl datePicker;

    /**
     * Crea el formulario para ingreso de nuevo autor.
     *
     * @param parent JFrame padre para centrar y bloquear el diálogo.
     */
    public FormularioAutorNew(JFrame parent) {
        super(parent, "Nuevo Autor", true);
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
        agregarCampo(panelCampos, gbc, "Nombre:", txtNombre = new JTextField(), labelFont, inputFont, 0);
        agregarCampo(panelCampos, gbc, "Apellidos:", txtApellidos = new JTextField(), labelFont, inputFont, 1);
        agregarCampo(panelCampos, gbc, "Nacionalidad:", txtNacionalidad = new JTextField(), labelFont, inputFont, 2);
        UtilDateModel model = new UtilDateModel();
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
        txtBiografia = new JTextArea(5, 30);
        txtBiografia.setFont(inputFont);
        JScrollPane scrollBio = new JScrollPane(txtBiografia);
        scrollBio.setPreferredSize(new Dimension(300, 100));
        panelCampos.add(scrollBio, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblFoto = new JLabel("Haz clic para subir una foto");
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(180, 180));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        lblFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        btnGuardar.addActionListener(e -> guardarAutor());
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
     * Método auxiliar para agregar una fila con etiqueta y campo de texto al formulario.
     *
     * @param panel     Panel contenedor.
     * @param gbc       Constraints de GridBagLayout.
     * @param texto     Texto de la etiqueta.
     * @param campo     Campo de texto a agregar.
     * @param fontLabel Fuente de la etiqueta.
     * @param fontInput Fuente del campo.
     * @param fila      Número de fila (posición vertical).
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
     * Abre un diálogo para seleccionar una imagen que se utilizará como foto del autor.
     * La imagen seleccionada se escala y se muestra en el JLabel correspondiente.
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
                ImageIcon icon = new ImageIcon(new ImageIcon(fotoBytes).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                lblFoto.setIcon(icon);
                lblFoto.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer la imagen.");
            }
        }
    }

    /**
     * Valida los datos del formulario y, si son correctos, crea un nuevo autor y lo guarda en la base de datos.
     * Muestra mensajes de error o éxito según corresponda.
     */
    private void guardarAutor() {
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
        try {
            Autor autor = new Autor();
            autor.setNombre(txtNombre.getText());
            autor.setApellidos(txtApellidos.getText());
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
            if (selectedDate != null) {
                autor.setFechaNacimiento(new Date(selectedDate.getTime()));
            }
            autor.setNacionalidad(txtNacionalidad.getText());
            autor.setBiografia(txtBiografia.getText());
            autor.setFoto(fotoBytes);

            AutorDAO.guardarAutor(autor);

            JOptionPane.showMessageDialog(this, "Autor guardado con éxito.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar autor: " + e.getMessage());
        }
    }
}

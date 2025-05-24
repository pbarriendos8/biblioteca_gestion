package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.*;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import com.pablobn.biblioteca.modelo.dao.PrestamoDAO;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.EstadoPrestamo;
import com.pablobn.biblioteca.util.TipoUsuario;
import com.pablobn.biblioteca.vista.forms.formsedit.FormularioAutorEdit;
import com.pablobn.biblioteca.vista.forms.formsedit.FormularioLibroEdit;
import com.pablobn.biblioteca.vista.forms.formsedit.FormularioPrestamoEdit;
import com.pablobn.biblioteca.vista.forms.formsedit.FormularioUsuarioEdit;
import com.pablobn.biblioteca.vista.forms.formsnew.*;
import org.hibernate.Session;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.pablobn.biblioteca.util.HibernateUtil.getSession;

public class PanelEntidad extends JPanel {
    private String entidad;
    private final Usuario usuarioLogueado;
    private final JButton btnNuevo;
    private final JButton btnEditar;
    private final JButton btnEliminar;
    private final JButton btnFinalizar;
    private JTable table;
    private DefaultTableModel tableModel;
    private PanelFiltroBusqueda panelFiltroBusqueda;
    private TableRowSorter<DefaultTableModel> rowSorter;

    private PanelFiltroTabla panelFiltroVisual;

    public PanelEntidad(String entidad, Usuario usuarioLoguedo) {
        this.entidad = entidad;
        this.usuarioLogueado = usuarioLoguedo;

        setLayout(new BorderLayout());

        panelFiltroBusqueda = new PanelFiltroBusqueda();
        add(panelFiltroBusqueda, BorderLayout.SOUTH);

        switch (entidad) {
            case "Usuarios":
                panelFiltroVisual = new PanelFiltroTabla(new String[]{"Todos", "Administradores", "Editores", "Consultores"});
                break;
            case "Préstamos":
                panelFiltroVisual = new PanelFiltroTabla(new String[]{"Todos", "Finalizados", "Activos"});
                break;
            case "Libros":
                panelFiltroVisual = new PanelFiltroTabla(new String[]{"Todos"});
                break;
            default:
                panelFiltroVisual = new PanelFiltroTabla(new String[]{"Todos"});
                break;
        }

        panelFiltroVisual.getComboBox().addActionListener(e -> cargarDatos());

        // Crear botones de acciones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnFinalizar = new JButton("Finalizar");

        // Desactivar botones si el usuario es consultor y entidad es Autor o Libro
        if (usuarioLoguedo.getTipoUsuario() == TipoUsuario.CONSULTA && (entidad.equals("Autores") || entidad.equals("Libros"))) {
            btnNuevo.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        if (entidad.equals("Préstamos")) {
            panelBotones.add(btnFinalizar);
        }
        if (usuarioLoguedo.getTipoUsuario() == TipoUsuario.CONSULTA && entidad.equals("Libros")) {
            JButton btnHacerPrestamo = new JButton("Hacer préstamo");
            panelBotones.add(btnHacerPrestamo);

            btnHacerPrestamo.addActionListener(e -> {
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int id = (int) tableModel.getValueAt(filaSeleccionada, 0);
                    Libro libro = LibroDAO.obtenerTodosLibros().stream().filter(a -> a.getIdLibro() == id).findFirst().orElse(null);
                    new FormularioPrestamoConsulta((JFrame) SwingUtilities.getWindowAncestor(this), libro, usuarioLoguedo);
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro primero.");
                }
            });
        }

        // Panel que contendrá filtro visual y botones
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());

        panelSuperior.add(panelFiltroVisual, BorderLayout.WEST);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(table);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Acción de botones
        btnNuevo.addActionListener(e -> mostrarVentanaNuevo());
        btnEditar.addActionListener(e -> mostrarVentanaEditar());
        btnEliminar.addActionListener(e -> mostrarVentanaEliminar());
        btnFinalizar.addActionListener(e -> finalizarPrestamo());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && entidad.equals("Préstamos")) {
                int fila = table.getSelectedRow();
                if (fila >= 0) {
                    int modeloFila = table.convertRowIndexToModel(fila);
                    Object estadoObj = table.getModel().getValueAt(modeloFila, 5); // Columna "Estado"
                    if (estadoObj != null && estadoObj instanceof EstadoPrestamo) {
                        EstadoPrestamo estado = (EstadoPrestamo) estadoObj;
                        btnFinalizar.setEnabled(estado != EstadoPrestamo.FINALIZADO);
                    } else {
                        btnFinalizar.setEnabled(true);
                    }
                } else {
                    btnFinalizar.setEnabled(false);
                }
            }
        });

        // Cargar datos
        cargarDatos();
    }


    public void cargarDatos() {
        String filtro = panelFiltroVisual.getSeleccion();
        Session session = getSession();
        try {
            switch (entidad) {
                case "Autores":
                    List<Autor> autores = AutorDAO.obtenerTodos();
                    cargarAutoresEnTabla(autores);
                    break;
                case "Libros":
                    List<Libro> libros = LibroDAO.obtenerTodosLibros();
                    if ("Disponibles".equals(filtro)) {
                        libros = libros.stream().filter(Libro::isDisponible).collect(Collectors.toList());
                    } else if ("No disponibles".equals(filtro)) {
                        libros = libros.stream().filter(libro -> !libro.isDisponible()).collect(Collectors.toList());
                    }
                    cargarLibrosEnTabla(libros);
                    break;
                case "Usuarios":
                    List<Usuario> usuarios = UsuarioDAO.obtenerTodosUsuarios();
                    if ("Administradores".equals(filtro)) {
                        usuarios = usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.ADMIN).collect(Collectors.toList());
                    } else if ("Editores".equals(filtro)) {
                        usuarios = usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.EDITOR).collect(Collectors.toList());
                    }else if ("Consultores".equals(filtro)) {
                        usuarios = usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuario.CONSULTA).collect(Collectors.toList());
                    }
                    cargarUsuariosEnTabla(usuarios);
                    break;
                case "Préstamos":
                    List<Prestamo> prestamos = PrestamoDAO.obtenerTodosPrestamos();

                    if (usuarioLogueado.getTipoUsuario() == TipoUsuario.CONSULTA) {
                        // Mostrar solo los préstamos del usuario actual
                        prestamos = prestamos.stream()
                                .filter(p -> p.getUsuario() != null && p.getUsuario().getIdUsuario() == usuarioLogueado.getIdUsuario())
                                .collect(Collectors.toList());
                    }

                    if ("Finalizados".equals(filtro)) {
                        prestamos = prestamos.stream().filter(p -> p.getEstado() == EstadoPrestamo.FINALIZADO).collect(Collectors.toList());
                    } else if ("Activos".equals(filtro)) {
                        prestamos = prestamos.stream().filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO).collect(Collectors.toList());
                    }

                    cargarPrestamosEnTabla(prestamos);
                    break;
            }
        } finally {
            session.close();
        }
    }

    private void cargarAutoresEnTabla(List<Autor> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Apellidos", "Nacimiento", "Nacionalidad"});
        tableModel.setRowCount(0);
        for (Autor a : lista) {
            tableModel.addRow(new Object[]{
                    a.getIdAutor(), a.getNombre(), a.getApellidos(),
                    a.getFechaNacimiento(), a.getNacionalidad()
            });
        }
        panelFiltroBusqueda.setOpcionesCampo(new String[]{"Nombre", "Apellidos"});
        panelFiltroBusqueda.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarAutores(); }
            public void removeUpdate(DocumentEvent e) { filtrarAutores(); }
            public void changedUpdate(DocumentEvent e) { filtrarAutores(); }
        });

    }

    private void filtrarAutores() {
        String texto = panelFiltroBusqueda.getCampoBusqueda().getText().trim();
        String campo = panelFiltroBusqueda.getCampoSeleccionado();
        if (texto.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        int columna = 1;
        if ("Apellidos".equals(campo)) {
            columna = 2;
        }
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columna));
    }


    private void cargarLibrosEnTabla(List<Libro> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Título", "Autor", "ISBN"});
        tableModel.setRowCount(0);
        for (Libro l : lista) {
            tableModel.addRow(new Object[]{
                    l.getIdLibro(), l.getTitulo(),
                    l.getAutor() != null ? l.getAutor().getNombre() + " " + l.getAutor().getApellidos() : "Desconocido",
                    l.getIsbn()
            });
        }
        panelFiltroBusqueda.setOpcionesCampo(new String[]{"Título", "Autor"});
        panelFiltroBusqueda.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrarLibros();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrarLibros();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrarLibros();
            }
        });

    }

    private void filtrarLibros() {
        String texto = panelFiltroBusqueda.getCampoBusqueda().getText().trim();
        String campo = panelFiltroBusqueda.getCampoSeleccionado();
        if (texto.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        int columna = campo.equals("Título") ? 1 : 2;
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columna));
    }

    private void cargarUsuariosEnTabla(List<Usuario> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Correo", "Nombre", "Tipo", "Fecha Registro"});
        tableModel.setRowCount(0);
        for (Usuario u : lista) {
            tableModel.addRow(new Object[]{
                    u.getIdUsuario(), u.getNombreUsuario(), u.getCorreo(),
                    u.getNombreCompleto(), u.getTipoUsuario(),
                    u.getFechaRegistro()
            });
        }
        panelFiltroBusqueda.setOpcionesCampo(new String[]{"Usuario", "Correo"});
        panelFiltroBusqueda.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarUsuarios(); }
            public void removeUpdate(DocumentEvent e) { filtrarUsuarios(); }
            public void changedUpdate(DocumentEvent e) { filtrarUsuarios(); }
        });

    }
    private void filtrarUsuarios() {
        String texto = panelFiltroBusqueda.getCampoBusqueda().getText().trim();
        String campo = panelFiltroBusqueda.getCampoSeleccionado();
        if (texto.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        int columna = campo.equals("Usuario") ? 1 : 2;
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columna));
    }


    private void cargarPrestamosEnTabla(List<Prestamo> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Libro", "Fecha inicio", "Fecha limite", "Fecha devuelto", "Estado"});
        tableModel.setRowCount(0);
        for (Prestamo p : lista) {
            tableModel.addRow(new Object[]{
                    p.getIdPrestamo(), p.getUsuario() != null ? p.getUsuario().getNombreUsuario() : "Desconocido",
                    p.getLibro() != null ? p.getLibro().getTitulo() : "Desconocido",
                    p.getFechaInicio(), p.getFechaFin(), p.getFechaDevolucionReal(), p.getEstado(),
            });
        }
        panelFiltroBusqueda.setOpcionesCampo(new String[]{"Usuario", "Libro"});
        panelFiltroBusqueda.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarPrestamos(); }
            public void removeUpdate(DocumentEvent e) { filtrarPrestamos(); }
            public void changedUpdate(DocumentEvent e) { filtrarPrestamos(); }
        });

    }
    private void filtrarPrestamos() {
        String texto = panelFiltroBusqueda.getCampoBusqueda().getText().trim();
        String campo = panelFiltroBusqueda.getCampoSeleccionado();
        if (texto.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        int columna = campo.equals("Usuario") ? 1 : 2;
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columna));
    }


    private void mostrarVentanaNuevo() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        switch (entidad) {
            case "Autores": new FormularioAutorNew(frame); break;
            case "Libros": new FormularioLibroNew(frame); break;
            case "Usuarios": new FormularioUsuarioNew(frame); break;
            case "Préstamos": new FormularioPrestamoNew(frame, usuarioLogueado); break;
        }
        cargarDatos();
    }

    private void mostrarVentanaEditar() {
        int id;
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro para editar.");
            return;
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        switch (entidad) {
            case "Autores":
                id = (int) tableModel.getValueAt(fila, 0);
                Autor autor = AutorDAO.obtenerTodos().stream().filter(a -> a.getIdAutor() == id).findFirst().orElse(null);
                if (autor != null) new FormularioAutorEdit(frame, autor);
                break;
            case "Libros":
                id = (int) tableModel.getValueAt(fila, 0);
                Libro libro = LibroDAO.obtenerTodosLibros().stream().filter(a -> a.getIdLibro() == id).findFirst().orElse(null);
                if (libro != null) new FormularioLibroEdit(frame, libro);
                break;
            case "Préstamos":
                id = (int) tableModel.getValueAt(fila, 0);
                Prestamo prestamo = PrestamoDAO.obtenerTodosPrestamos().stream().filter(p -> p.getIdPrestamo() == id).findFirst().orElse(null);
                if (prestamo != null) new FormularioPrestamoEdit(frame, prestamo, usuarioLogueado);
                break;
            case "Usuarios":
                id = (int) tableModel.getValueAt(fila, 0);
                Usuario usuario = UsuarioDAO.obtenerTodosUsuarios().stream().filter(p -> p.getIdUsuario() == id).findFirst().orElse(null);
                if (usuario != null) new FormularioUsuarioEdit(frame, usuario);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Edición no disponible para esta entidad.");
        }

        cargarDatos();
    }

    private void mostrarVentanaEliminar() {
        int id;
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este registro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        switch (entidad) {
            case "Autores":
                id = (int) tableModel.getValueAt(fila, 0);
                Autor autor = AutorDAO.obtenerTodos().stream().filter(a -> a.getIdAutor() == id).findFirst().orElse(null);
                if (autor != null) {
                    boolean tieneLibros = LibroDAO.obtenerTodosLibros().stream()
                            .anyMatch(libro -> libro.getAutor().getIdAutor() == id);
                    if (tieneLibros) {
                        JOptionPane.showMessageDialog(this, "Este autor no se puede eliminar porque tiene libros asociados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    AutorDAO.eliminarAutor(autor);
                    JOptionPane.showMessageDialog(this, "Autor eliminado.");
                }
                break;

            case "Libros":
                id = (int) tableModel.getValueAt(fila, 0);
                Libro libro = LibroDAO.obtenerTodosLibros().stream().filter(l -> l.getIdLibro() == id).findFirst().orElse(null);
                if (libro != null) {
                    boolean tienePrestamos = PrestamoDAO.obtenerTodosPrestamos().stream()
                            .anyMatch(prestamo -> prestamo.getLibro().getIdLibro() == id);
                    if (tienePrestamos) {
                        JOptionPane.showMessageDialog(this, "Este libro no se puede eliminar porque tiene préstamos asociados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    LibroDAO.eliminarLibro(libro);
                    JOptionPane.showMessageDialog(this, "Libro eliminado.");
                }
                break;

            case "Préstamos":
                id = (int) tableModel.getValueAt(fila, 0);
                Prestamo prestamo = PrestamoDAO.obtenerTodosPrestamos().stream().filter(p -> p.getIdPrestamo() == id).findFirst().orElse(null);
                if (prestamo != null) {
                    PrestamoDAO.eliminarPrestamo(prestamo);
                    JOptionPane.showMessageDialog(this, "Préstamo eliminado.");
                }
                break;

            case "Usuarios":
                id = (int) tableModel.getValueAt(fila, 0);
                Usuario usuario = UsuarioDAO.obtenerTodosUsuarios().stream().filter(u -> u.getIdUsuario() == id).findFirst().orElse(null);
                if (usuario != null) {
                    UsuarioDAO.eliminarUsuario(usuario);
                    JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "Eliminación no disponible para esta entidad.");
        }

        cargarDatos();
    }


    private void finalizarPrestamo() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo para finalizar.");
            return;
        }

        int id = (int) table.getValueAt(fila, 0);
        Prestamo prestamo = PrestamoDAO.obtenerTodosPrestamos().stream()
                .filter(p -> p.getIdPrestamo() == id)
                .findFirst()
                .orElse(null);

        if (prestamo != null && prestamo.getEstado() != EstadoPrestamo.FINALIZADO) {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas finalizar este préstamo?",
                    "Confirmar finalización",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                PrestamoDAO.finalizarPrestamo(id);
                JOptionPane.showMessageDialog(this, "Préstamo finalizado correctamente.");
                cargarDatos(); // Usa tu método para refrescar la tabla
            }

        } else {
            JOptionPane.showMessageDialog(this, "Este préstamo ya está finalizado.");
        }
    }
}

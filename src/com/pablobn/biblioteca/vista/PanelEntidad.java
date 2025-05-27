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

/**
 * Panel genérico para la visualización y gestión de diferentes entidades
 * (Usuarios, Préstamos, Libros, Autores, etc.) en la aplicación.
 * <p>
 * Proporciona una tabla para mostrar datos, filtros para búsqueda y
 * botones para realizar acciones comunes como crear, editar, eliminar
 * y finalizar (en el caso de préstamos).
 * </p>
 * <p>
 * También gestiona los permisos de acuerdo al tipo de usuario autenticado,
 * deshabilitando ciertas acciones para usuarios con perfil de consulta.
 * </p>
 *
 * @author PabloBN
 */
public class PanelEntidad extends JPanel {

    /**
     * Nombre de la entidad que se está gestionando (ejemplo: "Usuarios", "Libros").
     */
    private String entidad;

    /**
     * Usuario autenticado actualmente, para validar permisos y acciones.
     */
    private final Usuario usuarioLogueado;

    /**
     * Botón para crear un nuevo registro de la entidad.
     */
    private final JButton btnNuevo;

    /**
     * Botón para editar el registro seleccionado.
     */
    private final JButton btnEditar;

    /**
     * Botón para eliminar el registro seleccionado.
     */
    private final JButton btnEliminar;

    /**
     * Botón para finalizar un préstamo (visible solo en entidad "Préstamos").
     */
    private final JButton btnFinalizar;

    /**
     * Tabla para mostrar los datos de la entidad.
     */
    private JTable table;

    /**
     * Modelo de tabla que maneja los datos mostrados en la tabla.
     */
    private DefaultTableModel tableModel;

    /**
     * Panel con campo de filtro para búsqueda textual en la tabla.
     */
    private PanelFiltroBusqueda panelFiltroBusqueda;

    /**
     * Filtro visual con opciones específicas según la entidad seleccionada.
     */
    private PanelFiltroTabla panelFiltroVisual;

    /**
     * Ordenador de filas para la tabla, usado para aplicar filtros de búsqueda y orden.
     */
    private TableRowSorter<DefaultTableModel> rowSorter;

    /**
     * Constructor que inicializa el panel para la entidad especificada y
     * el usuario autenticado, configurando los componentes gráficos, filtros,
     * botones y eventos asociados.
     *
     * @param entidad       Nombre de la entidad que se gestionará.
     * @param usuarioLoguedo Usuario autenticado que interactúa con el panel.
     */
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
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnFinalizar = new JButton("Finalizar");
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
        btnNuevo.addActionListener(e -> mostrarVentanaNuevo());
        btnEditar.addActionListener(e -> mostrarVentanaEditar());
        btnEliminar.addActionListener(e -> mostrarVentanaEliminar());
        btnFinalizar.addActionListener(e -> finalizarPrestamo());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && entidad.equals("Préstamos")) {
                int fila = table.getSelectedRow();
                if (fila >= 0) {
                    int modeloFila = table.convertRowIndexToModel(fila);
                    Object estadoObj = table.getModel().getValueAt(modeloFila, 5);
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
        cargarDatos();
    }

    /**
     * Carga los datos correspondientes a la entidad actual en la tabla,
     * aplicando filtros específicos según la selección en el panelFiltroVisual
     * y el tipo de usuario logueado.
     * <p>
     * Obtiene los datos desde los DAOs correspondientes y los filtra en memoria
     * antes de pasarlos al modelo de tabla para su visualización.
     * </p>
     * <p>
     * Además, cierra la sesión de Hibernate (o similar) al finalizar la carga.
     * </p>
     */
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

    /**
     * Configura la tabla para mostrar la lista de autores recibida,
     * estableciendo las columnas, vaciando filas previas y agregando
     * cada autor con sus datos.
     * <p>
     * También configura el panel de búsqueda para filtrar por "Nombre"
     * o "Apellidos", añadiendo un DocumentListener para actualizar la tabla
     * en tiempo real al escribir en el campo de búsqueda.
     * </p>
     *
     * @param lista Lista de objetos Autor que se cargarán en la tabla.
     */
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

    /**
     * Aplica un filtro a la tabla para mostrar únicamente los autores
     * cuyo nombre o apellidos coincidan (parcialmente, sin distinción
     * de mayúsculas/minúsculas) con el texto ingresado en el campo de búsqueda.
     * <p>
     * El filtro se aplica a la columna correspondiente según el campo seleccionado
     * ("Nombre" o "Apellidos").
     * </p>
     */
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



    /**
     * Configura la tabla para mostrar la lista de libros recibida,
     * estableciendo las columnas, vaciando filas previas y agregando
     * cada libro con sus datos relevantes.
     * <p>
     * El autor se muestra concatenando nombre y apellidos; si no existe,
     * se muestra "Desconocido".
     * </p>
     * <p>
     * También configura el panel de búsqueda para filtrar por "Título"
     * o "Autor", añadiendo un DocumentListener para actualizar la tabla
     * en tiempo real al escribir en el campo de búsqueda.
     * </p>
     *
     * @param lista Lista de objetos Libro que se cargarán en la tabla.
     */
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

    /**
     * Aplica un filtro a la tabla para mostrar únicamente los libros
     * cuyo título o autor coincidan (parcialmente, sin distinción
     * de mayúsculas/minúsculas) con el texto ingresado en el campo de búsqueda.
     * <p>
     * El filtro se aplica a la columna correspondiente según el campo seleccionado
     * ("Título" o "Autor").
     * </p>
     */
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

    /**
     * Configura la tabla para mostrar la lista de usuarios recibida,
     * estableciendo las columnas, vaciando filas previas y agregando
     * cada usuario con sus datos relevantes.
     * <p>
     * También configura el panel de búsqueda para filtrar por "Usuario"
     * o "Correo", añadiendo un DocumentListener para actualizar la tabla
     * en tiempo real al escribir en el campo de búsqueda.
     * </p>
     *
     * @param lista Lista de objetos Usuario que se cargarán en la tabla.
     */
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
    /**
     * Aplica un filtro a la tabla para mostrar únicamente los usuarios
     * cuyo nombre de usuario o correo coincidan (parcialmente, sin distinción
     * de mayúsculas/minúsculas) con el texto ingresado en el campo de búsqueda.
     * <p>
     * El filtro se aplica a la columna correspondiente según el campo seleccionado
     * ("Usuario" o "Correo").
     * </p>
     */
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

    /**
     * Configura la tabla para mostrar la lista de préstamos recibida,
     * estableciendo las columnas, vaciando filas previas y agregando
     * cada préstamo con sus datos relevantes.
     * <p>
     * El usuario y libro se muestran con su nombre de usuario y título respectivamente,
     * o "Desconocido" si no están asignados.
     * </p>
     * <p>
     * También configura el panel de búsqueda para filtrar por "Usuario"
     * o "Libro", añadiendo un DocumentListener para actualizar la tabla
     * en tiempo real al escribir en el campo de búsqueda.
     * </p>
     *
     * @param lista Lista de objetos Prestamo que se cargarán en la tabla.
     */
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

    /**
     * Aplica un filtro a la tabla para mostrar únicamente los préstamos
     * cuyo usuario o libro coincidan (parcialmente, sin distinción
     * de mayúsculas/minúsculas) con el texto ingresado en el campo de búsqueda.
     * <p>
     * El filtro se aplica a la columna correspondiente según el campo seleccionado
     * ("Usuario" o "Libro").
     * </p>
     */
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

    /**
     * Muestra la ventana de creación de nuevo registro según la entidad actual.
     * <p>
     * Llama a la clase correspondiente para abrir el formulario de creación,
     * pasando el JFrame padre y, si corresponde, el usuario logueado.
     * </p>
     * <p>
     * Luego recarga los datos en la tabla para reflejar cualquier cambio.
     * </p>
     */
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


    /**
     * Muestra la ventana de edición del registro seleccionado en la tabla
     * según la entidad actual.
     * <p>
     * Si no hay fila seleccionada, muestra un mensaje solicitando la selección.
     * Obtiene el ID del registro seleccionado y busca el objeto correspondiente
     * mediante DAO. Si se encuentra, abre el formulario de edición específico.
     * </p>
     * <p>
     * Después de cerrar el formulario, recarga los datos para reflejar cambios.
     * </p>
     */
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

    /**
     * Elimina el registro seleccionado en la tabla según la entidad actual,
     * previa confirmación por parte del usuario.
     * <p>
     * Si no hay fila seleccionada, muestra un mensaje solicitando selección.
     * Antes de eliminar un autor o libro, verifica si existen dependencias
     * (libros asociados o préstamos asociados) para evitar eliminaciones inválidas.
     * </p>
     * <p>
     * Luego de eliminar o cancelar, recarga los datos para actualizar la tabla.
     * </p>
     */
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
                Usuario usuario = UsuarioDAO.obtenerTodosUsuarios().stream()
                        .filter(u -> u.getIdUsuario() == id)
                        .findFirst()
                        .orElse(null);

                if (usuario != null) {
                    List<Prestamo> prestamosUsuario = PrestamoDAO.obtenerTodosPrestamos().stream()
                            .filter(p -> p.getUsuario().getIdUsuario() == id)
                            .collect(Collectors.toList());


                    boolean tienePrestamosActivos = prestamosUsuario.stream()
                            .anyMatch(p -> p.getEstado().name().equals("ACTIVO"));

                    if (tienePrestamosActivos) {
                        JOptionPane.showMessageDialog(this,
                                "Este usuario no se puede eliminar porque tiene préstamos activos.",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    prestamosUsuario.forEach(PrestamoDAO::eliminarPrestamo);

                    UsuarioDAO.eliminarUsuario(usuario);
                    JOptionPane.showMessageDialog(this, "Usuario y sus préstamos finalizados eliminados.");
                }
                break;


            default:
                JOptionPane.showMessageDialog(this, "Eliminación no disponible para esta entidad.");
        }

        cargarDatos();
    }

    /**
     * Finaliza el préstamo seleccionado en la tabla si está activo,
     * previa confirmación del usuario.
     * <p>
     * Si no hay fila seleccionada, muestra un mensaje solicitando selección.
     * Si el préstamo ya está finalizado, informa al usuario.
     * </p>
     * <p>
     * Tras finalizar el préstamo, muestra un mensaje de éxito y recarga los datos.
     * </p>
     */
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
                cargarDatos();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Este préstamo ya está finalizado.");
        }
    }

}

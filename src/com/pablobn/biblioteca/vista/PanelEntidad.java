package com.pablobn.biblioteca.vista;

import com.pablobn.biblioteca.modelo.*;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import com.pablobn.biblioteca.util.TipoUsuario;
import com.pablobn.biblioteca.vista.forms.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static com.pablobn.biblioteca.util.HibernateUtil.getSession;

public class PanelEntidad extends JPanel {
    private final String entidad;
    private final TipoUsuario tipoUsuario;
    private final JButton btnNuevo;
    private final JButton btnEditar;
    private final JButton btnEliminar;
    private final JButton btnFinalizar;
    private JTable table;
    private DefaultTableModel tableModel;

    public PanelEntidad(String entidad, TipoUsuario tipoUsuario) {
        this.entidad = entidad;
        this.tipoUsuario = tipoUsuario;

        setLayout(new BorderLayout());

        // Crear botones de acciones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnFinalizar = new JButton("Finalizar"); // botón nuevo

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        if (entidad.equals("Préstamos")) { // solo en préstamos
            panelBotones.add(btnFinalizar);
        }

        add(panelBotones, BorderLayout.NORTH);

        // Panel central con JTable
        JPanel panelCentro = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Permisos
        if (tipoUsuario == TipoUsuario.CONSULTA) {
            btnNuevo.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }

        // Acción de botones
        btnNuevo.addActionListener(e -> mostrarVentanaNuevo());
        btnEditar.addActionListener(e -> mostrarVentanaEditar());
        btnEliminar.addActionListener(e -> mostrarVentanaEliminar());
        btnFinalizar.addActionListener(e -> finalizarPrestamo());

        // Cargar datos
        cargarDatos();
    }

    private void cargarDatos() {
        Session session = getSession();
        try {
            switch (entidad) {
                case "Autores":
                    Query<Autor> queryAutor = session.createQuery("FROM Autor", Autor.class);
                    List<Autor> autores = queryAutor.getResultList();
                    cargarAutoresEnTabla(autores);
                    break;
                case "Libros":
                    Query<Libro> queryLibro = session.createQuery("FROM Libro", Libro.class);
                    List<Libro> libros = queryLibro.getResultList();
                    cargarLibrosEnTabla(libros);
                    break;
                case "Usuarios":
                    Query<Usuario> queryUsuario = session.createQuery("FROM Usuario", Usuario.class);
                    List<Usuario> usuarios = queryUsuario.getResultList();
                    cargarUsuariosEnTabla(usuarios);
                    break;
                case "Préstamos":
                    Query<Prestamo> queryPrestamo = session.createQuery("FROM Prestamo", Prestamo.class);
                    List<Prestamo> prestamos = queryPrestamo.getResultList();
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
    }

    private void cargarLibrosEnTabla(List<Libro> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Título", "Autor", "ISBN", "Disponible"});
        tableModel.setRowCount(0);
        for (Libro l : lista) {
            tableModel.addRow(new Object[]{
                    l.getIdLibro(), l.getTitulo(),
                    l.getAutor() != null ? l.getAutor().getNombre() + " " + l.getAutor().getApellidos() : "Desconocido",
                    l.getIsbn(), l.isDisponible()
            });
        }
    }

    private void cargarUsuariosEnTabla(List<Usuario> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Correo", "Nombre", "Tipo"});
        tableModel.setRowCount(0);
        for (Usuario u : lista) {
            tableModel.addRow(new Object[]{
                    u.getIdUsuario(), u.getNombreUsuario(), u.getCorreo(),
                    u.getNombreCompleto(), u.getTipoUsuario()
            });
        }
    }

    private void cargarPrestamosEnTabla(List<Prestamo> lista) {
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Libro", "Inicio", "Fin", "Estado"});
        tableModel.setRowCount(0);
        for (Prestamo p : lista) {
            tableModel.addRow(new Object[]{
                    p.getIdPrestamo(),
                    p.getUsuario() != null ? p.getUsuario().getNombreUsuario() : "Desconocido",
                    p.getLibro() != null ? p.getLibro().getTitulo() : "Desconocido",
                    p.getFechaInicio(), p.getFechaFin(), p.getEstado()
            });
        }
    }

    private void mostrarVentanaNuevo() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        switch (entidad) {
            case "Autores": new FormularioAutorNew(frame); break;
            case "Libros": new FormularioLibroNew(frame); break;
            case "Usuarios": new FormularioUsuarioNew(frame); break;
            case "Préstamos": new FormularioPrestamoNew(frame); break;
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
            default:
                JOptionPane.showMessageDialog(this, "Edición no disponible para esta entidad.");
        }

        cargarDatos(); // recarga la tabla tras edición
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
                    AutorDAO.eliminarAutor(autor);
                    JOptionPane.showMessageDialog(this, "Autor eliminado.");
                }
                break;
            case "Libros":
                id = (int) tableModel.getValueAt(fila, 0);
                Libro libro = LibroDAO.obtenerTodosLibros().stream().filter(a -> a.getIdLibro() == id).findFirst().orElse(null);
                if (libro != null) {
                    LibroDAO.eliminarLibro(libro);
                    JOptionPane.showMessageDialog(this, "Libro eliminado.");
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Eliminación no disponible para esta entidad.");
        }

        cargarDatos(); // actualiza tabla
    }

    private void finalizarPrestamo() {
        JOptionPane.showMessageDialog(this, "Aquí se finalizará el préstamo más adelante.");
    }
}

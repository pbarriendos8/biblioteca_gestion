package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Clase DAO para la entidad Libro.
 * Proporciona métodos estáticos para operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y consultas específicas usando Hibernate para interactuar con la base de datos.
 */
public class LibroDAO {

    /**
     * Guarda un nuevo libro en la base de datos.
     * @param libro Objeto Libro a persistir.
     */
    public static void guardarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la lista completa de libros almacenados en la base de datos.
     * @return Lista de objetos Libro.
     */
    public static List<Libro> obtenerTodosLibros() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Libro", Libro.class).list();
        }
    }

    /**
     * Elimina un libro de la base de datos.
     * @param libro Objeto Libro a eliminar.
     */
    public static void eliminarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la información de un libro existente en la base de datos.
     * @param libro Objeto Libro con los cambios a persistir.
     */
    public static void actualizarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica si existe un libro con un título dado (insensible a mayúsculas/minúsculas).
     * @param titulo Título del libro a verificar.
     * @return true si existe al menos un libro con ese título, false en caso contrario.
     */
    public static boolean existeLibroPorTitulo(String titulo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE lower(titulo) = :titulo", Long.class)
                    .setParameter("titulo", titulo.toLowerCase())
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    /**
     * Verifica si existe un libro con un ISBN dado.
     * @param isbn ISBN del libro a verificar.
     * @return true si existe al menos un libro con ese ISBN, false en caso contrario.
     */
    public static boolean existeLibroPorIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE isbn = :isbn", Long.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    /**
     * Verifica si existe otro libro diferente al actual con el mismo título (insensible a mayúsculas/minúsculas).
     * Útil para evitar duplicados al actualizar.
     * @param titulo Título a verificar.
     * @param idActual ID del libro que se está actualizando (para excluirlo).
     * @return true si existe otro libro con ese título, false en caso contrario.
     */
    public static boolean existeOtroLibroConTitulo(String titulo, int idActual) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE lower(titulo) = :titulo AND id != :id", Long.class)
                    .setParameter("titulo", titulo.toLowerCase())
                    .setParameter("id", idActual)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    /**
     * Verifica si existe otro libro diferente al actual con el mismo ISBN.
     * Útil para evitar duplicados al actualizar.
     * @param isbn ISBN a verificar.
     * @param idActual ID del libro que se está actualizando (para excluirlo).
     * @return true si existe otro libro con ese ISBN, false en caso contrario.
     */
    public static boolean existeOtroLibroConIsbn(String isbn, int idActual) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE isbn = :isbn AND id != :id", Long.class)
                    .setParameter("isbn", isbn)
                    .setParameter("id", idActual)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

}

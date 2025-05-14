package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LibroDAO {
    public static void guardarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todos los libros
    public static List<Libro> obtenerTodosLibros() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Libro", Libro.class).list();
        }
    }
    // Eliminar libro
    public static void eliminarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void actualizarLibro(Libro libro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(libro);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean existeLibroPorTitulo(String titulo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE lower(titulo) = :titulo", Long.class)
                    .setParameter("titulo", titulo.toLowerCase())
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    public static boolean existeLibroPorIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Libro WHERE isbn = :isbn", Long.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
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

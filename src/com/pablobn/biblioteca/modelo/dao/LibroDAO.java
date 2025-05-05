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
}

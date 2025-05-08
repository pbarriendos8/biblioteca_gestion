package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.modelo.Prestamo;
import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.EstadoPrestamo;
import com.pablobn.biblioteca.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public static void guardarPrestamo(Prestamo prestamo, int idUsuario, int idLibro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Usuario usuario = session.get(Usuario.class, idUsuario);
            Libro libro = session.get(Libro.class, idLibro);

            prestamo.setUsuario(usuario);
            prestamo.setLibro(libro);

            session.save(prestamo);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finalizarPrestamo(int idPrestamo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Prestamo prestamo = session.get(Prestamo.class, idPrestamo);
            if (prestamo != null) {
                prestamo.setEstado(EstadoPrestamo.FINALIZADO);
                prestamo.setFechaDevolucionReal(new Date(System.currentTimeMillis()));
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Prestamo> obtenerTodosPrestamos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Prestamo", Prestamo.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void actualizarPrestamo(Prestamo prestamo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(prestamo);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarPrestamo(Prestamo prestamo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(prestamo);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

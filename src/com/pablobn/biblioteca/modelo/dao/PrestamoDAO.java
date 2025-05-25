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

/**
 * Clase DAO para la entidad Prestamo.
 * Proporciona métodos estáticos para operaciones CRUD y gestión específica
 * de préstamos, utilizando Hibernate para la interacción con la base de datos.
 */
public class PrestamoDAO {

    /**
     * Guarda un nuevo préstamo en la base de datos, vinculando un usuario y un libro por sus IDs.
     * @param prestamo Objeto Prestamo a persistir.
     * @param idUsuario ID del usuario que realiza el préstamo.
     * @param idLibro ID del libro que se presta.
     */
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

    /**
     * Marca un préstamo como finalizado, estableciendo el estado y la fecha real de devolución.
     * @param idPrestamo ID del préstamo a finalizar.
     */
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

    /**
     * Obtiene la lista completa de préstamos almacenados en la base de datos.
     * @return Lista de objetos Prestamo; vacía si ocurre algún error.
     */
    public static List<Prestamo> obtenerTodosPrestamos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Prestamo", Prestamo.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza la información de un préstamo existente en la base de datos.
     * @param prestamo Objeto Prestamo con los cambios a persistir.
     */
    public static void actualizarPrestamo(Prestamo prestamo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(prestamo);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un préstamo de la base de datos.
     * @param prestamo Objeto Prestamo a eliminar.
     */
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

package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Clase DAO para la entidad Autor.
 * Proporciona métodos estáticos para operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * utilizando Hibernate para interactuar con la base de datos.
 */
public class AutorDAO {

    /**
     * Guarda un nuevo autor en la base de datos.
     * @param autor Objeto Autor a persistir.
     */
    public static void guardarAutor(Autor autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(autor);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la lista completa de autores almacenados en la base de datos.
     * @return Lista de objetos Autor.
     */
    public static List<Autor> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Autor", Autor.class).list();
        }
    }

    /**
     * Actualiza la información de un autor existente en la base de datos.
     * @param autor Objeto Autor con los cambios a persistir.
     */
    public static void actualizarAutor(Autor autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(autor);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un autor de la base de datos.
     * @param autor Objeto Autor a eliminar.
     */
    public static void eliminarAutor(Autor autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(autor);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

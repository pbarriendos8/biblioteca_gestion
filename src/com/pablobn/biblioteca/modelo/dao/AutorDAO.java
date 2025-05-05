package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AutorDAO {

    public static void guardarAutor(Autor autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(autor);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Autor> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Autor", Autor.class).list();
        }
    }

    public static void actualizarAutor(Autor autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(autor);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

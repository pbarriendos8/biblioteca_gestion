package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.HibernateUtil;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

public class UsuarioDAO {

    public static void registrarUsuario(Usuario nuevoUsuario) {
        // Establecer el tipo de usuario por defecto como "consulta"
        nuevoUsuario.setTipoUsuario(TipoUsuario.CONSULTA); // Tipo por defecto
        nuevoUsuario.setFechaRegistro(new Date(System.currentTimeMillis())); // Fecha actual

        // Guardar el usuario en la base de datos
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(nuevoUsuario);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void crearUsuario(Usuario nuevoUsuario) {
        nuevoUsuario.setFechaRegistro(new Date(System.currentTimeMillis())); // Fecha actual

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(nuevoUsuario);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Usuario autenticar(String nombreUsuario, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuario WHERE nombreUsuario = :nombreUsuario AND password = :password";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario);
            query.setParameter("password", password);
            Usuario usuario = query.uniqueResult();
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Usuario> obtenerTodosUsuarios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuario";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

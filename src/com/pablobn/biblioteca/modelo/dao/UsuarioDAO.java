package com.pablobn.biblioteca.modelo.dao;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.util.HashUtil;
import com.pablobn.biblioteca.util.HibernateUtil;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

/**
 * Clase DAO para la entidad Usuario.
 * Proporciona métodos estáticos para el manejo de usuarios en la base de datos,
 * incluyendo registro, autenticación, actualización y eliminación.
 */
public class UsuarioDAO {

    /**
     * Registra un nuevo usuario con tipo CONSULTA y fecha de registro actual.
     * @param nuevoUsuario Objeto Usuario a registrar.
     */
    public static void registrarUsuario(Usuario nuevoUsuario) {
        nuevoUsuario.setTipoUsuario(TipoUsuario.CONSULTA);
        nuevoUsuario.setFechaRegistro(new Date(System.currentTimeMillis()));
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(nuevoUsuario);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Crea un usuario con fecha de registro actual sin modificar su tipo.
     * @param nuevoUsuario Objeto Usuario a crear.
     */
    public static void crearUsuario(Usuario nuevoUsuario) {
        nuevoUsuario.setFechaRegistro(new Date(System.currentTimeMillis()));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(nuevoUsuario);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Autentica un usuario verificando su nombre y contraseña.
     * @param nombreUsuario Nombre de usuario para autenticación.
     * @param password Contraseña en texto plano para verificar.
     * @return Usuario autenticado si las credenciales son correctas, o null si no.
     */
    public static Usuario autenticar(String nombreUsuario, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuario WHERE nombreUsuario = :nombreUsuario";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario);
            Usuario usuario = query.uniqueResult();

            if (usuario != null && HashUtil.verificarPassword(password, usuario.getPassword())) {
                return usuario;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados en la base de datos.
     * @return Lista de usuarios o null si ocurre un error.
     */
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

    /**
     * Elimina un usuario de la base de datos.
     * @param usuario Objeto Usuario a eliminar.
     */
    public static void eliminarUsuario(Usuario usuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(usuario);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     * @param nuevoUsuario Objeto Usuario con los datos actualizados.
     */
    public static void actualizarUsuario(Usuario nuevoUsuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(nuevoUsuario);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

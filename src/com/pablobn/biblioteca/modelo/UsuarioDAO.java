package com.pablobn.biblioteca.modelo;

import com.pablobn.biblioteca.util.HibernateUtil;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;

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
}

package com.pablobn.biblioteca.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(com.pablobn.biblioteca.modelo.Usuario.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Libro.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Prestamo.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Autor.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al iniciar la sessionFactory" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}

package com.pablobn.biblioteca.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase utilitaria para gestionar la configuración de Hibernate y proporcionar sesiones.
 * <p>
 * Esta clase se encarga de inicializar la {@link SessionFactory} a partir del archivo
 * de configuración {@code hibernate.cfg.xml} y de las clases anotadas del modelo.
 * </p>
 */
public class HibernateUtil {

    /** La fábrica de sesiones de Hibernate, inicializada una vez al cargar la clase. */
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Usuario.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Libro.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Prestamo.class)
                    .addAnnotatedClass(com.pablobn.biblioteca.modelo.Autor.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al iniciar la sessionFactory" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Obtiene la única instancia de {@link SessionFactory}.
     *
     * @return la instancia de {@code SessionFactory}
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Abre y devuelve una nueva sesión de Hibernate.
     *
     * @return una nueva instancia de {@link Session}
     */
    public static Session getSession() {
        return sessionFactory.openSession();
    }

    /**
     * Cierra la {@link SessionFactory} y libera los recursos utilizados por Hibernate.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}

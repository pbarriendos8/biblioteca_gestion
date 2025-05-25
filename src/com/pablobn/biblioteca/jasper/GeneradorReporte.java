package com.pablobn.biblioteca.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

/**
 * Clase utilitaria para la generación y visualización de informes JasperReports.
 * Contiene métodos para mostrar informes específicos usando una conexión JDBC obtenida
 * a partir de una sesión de Hibernate.
 */
public class GeneradorReporte {

    /**
     * Muestra el informe "Libros por Autor" cargando el archivo .jasper correspondiente.
     * @param session Sesión Hibernate para obtener la conexión a la base de datos.
     */
    public static void mostrarInformeLibrosPorAutor(Session session) {
        try {
            InputStream jasperStream = GeneradorReporte.class.getResourceAsStream("/informes/InformeLibrosPorAutor.jasper");
            if (jasperStream == null) {
                throw new RuntimeException("No se encontró el archivo del informe .jasper");
            }

            Connection conexion = session.doReturningWork(conn -> conn);
            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint print = JasperFillManager.fillReport(jasperStream, parametros, conexion);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Libros por Autor");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el informe "Préstamos por usuario" cargando el archivo .jasper correspondiente.
     * @param session Sesión Hibernate para obtener la conexión a la base de datos.
     */
    public static void mostrarInformePrestamosPorUsuario(Session session) {
        try {
            InputStream jasperStream = GeneradorReporte.class.getResourceAsStream("/informes/InformePrestamosPorUsuario.jasper");
            if (jasperStream == null) {
                throw new RuntimeException("No se encontró el archivo del informe .jasper");
            }

            Connection conexion = session.doReturningWork(conn -> conn);
            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint print = JasperFillManager.fillReport(jasperStream, parametros, conexion);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Préstamos por usuario");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el informe gráfico "Préstamos Activos/Finalizados" cargando el archivo .jasper correspondiente.
     * @param session Sesión Hibernate para obtener la conexión a la base de datos.
     */
    public static void mostrarInformeGraficoPrestamos(Session session) {
        try {
            InputStream jasperStream = GeneradorReporte.class.getResourceAsStream("/informes/GraficoPrestamos.jasper");
            if (jasperStream == null) {
                throw new RuntimeException("No se encontró el archivo del informe .jasper");
            }

            Connection conexion = session.doReturningWork(conn -> conn);
            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint print = JasperFillManager.fillReport(jasperStream, parametros, conexion);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Préstamos Activos/Finalizados");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el gráfico "Libros por Autor" cargando el archivo .jasper correspondiente.
     * @param session Sesión Hibernate para obtener la conexión a la base de datos.
     */
    public static void mostrarGraficoLibrosPorAutor(Session session) {
        try {
            InputStream jasperStream = GeneradorReporte.class.getResourceAsStream("/informes/GraficoLibrosPorAutor.jasper");
            if (jasperStream == null) {
                throw new RuntimeException("No se encontró el archivo del informe .jasper");
            }

            Connection conexion = session.doReturningWork(conn -> conn);
            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint print = JasperFillManager.fillReport(jasperStream, parametros, conexion);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Préstamos Activos/Finalizados");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

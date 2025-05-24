package com.pablobn.biblioteca.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;



import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

public class GeneradorReporte {

    public static void mostrarInformeLibrosPorAutor(Session session) {
        try {
            // Cargar desde el classpath
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
    public static void mostrarInformePrestamosPorUsuario(Session session) {
        try {
            // Cargar desde el classpath
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

    public static void mostrarInformeGraficoPrestamos(Session session) {
        try {
            // Cargar desde el classpath
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

    public static void mostrarGraficoLibrosPorAutor(Session session) {
        try {
            // Cargar desde el classpath
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



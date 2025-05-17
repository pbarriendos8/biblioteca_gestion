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
            InputStream jasperStream = GeneradorReporte.class.getResourceAsStream("/informes/LibrosPorAutor.jasper");
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
}

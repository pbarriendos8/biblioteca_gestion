package com.pablobn.biblioteca.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BBDDUtil {

    public static void initDatabase() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/?user=root&password=";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(BBDDUtil.class.getResourceAsStream("/init.sql"))
             )) {

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    statement.execute(sql.toString());
                    sql.setLength(0);
                }
            }

            System.out.println("Base de datos inicializada correctamente.");

        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

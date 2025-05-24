package com.pablobn.biblioteca;

import com.formdev.flatlaf.FlatLightLaf;
import com.pablobn.biblioteca.vista.LoginPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Fuente.aplicarFuentePersonalizada();
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        try {
            frame.setContentPane(new LoginPanel());
        } catch (Exception e) {
            e.printStackTrace(); // Muestra si hay error al crear el panel
        }

        frame.setVisible(true);
    }

}

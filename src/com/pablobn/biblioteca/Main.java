package com.pablobn.biblioteca;

import com.formdev.flatlaf.FlatLightLaf;
import com.pablobn.biblioteca.util.Fuente;
import com.pablobn.biblioteca.vista.LoginPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
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
            e.printStackTrace();
        }

        frame.setVisible(true);
    }

}


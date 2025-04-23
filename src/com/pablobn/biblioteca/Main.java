package com.pablobn.biblioteca;

import com.formdev.flatlaf.FlatLightLaf;
import com.pablobn.biblioteca.util.Fuente;
import com.pablobn.biblioteca.vista.LoginPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Fuente.aplicarFuentePersonalizada();
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Gestión Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        frame.setContentPane(new LoginPanel());
        frame.setVisible(true);

    }
}

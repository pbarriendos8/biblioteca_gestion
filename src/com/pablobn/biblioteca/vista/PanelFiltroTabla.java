package com.pablobn.biblioteca.vista;

import javax.swing.*;
import java.awt.*;

public class PanelFiltroTabla extends JPanel {
    private JComboBox<String> comboFiltro;

    public PanelFiltroTabla(String[] opciones) {
        comboFiltro = new JComboBox<>(opciones);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Ver:"));
        add(comboFiltro);
    }

    public JComboBox<String> getComboBox() {
        return comboFiltro;
    }

    public String getSeleccion() {
        return (String) comboFiltro.getSelectedItem();
    }
}

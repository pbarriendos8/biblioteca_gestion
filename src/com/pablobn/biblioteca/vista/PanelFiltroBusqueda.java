package com.pablobn.biblioteca.vista;

import javax.swing.*;
import java.awt.*;

public class PanelFiltroBusqueda extends JPanel {
    private final JComboBox<String> comboCampo;
    private final JTextField campoBusqueda;

    public PanelFiltroBusqueda() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        comboCampo = new JComboBox<>();
        campoBusqueda = new JTextField(20);

        add(new JLabel("Buscar por:"));
        add(comboCampo);
        add(campoBusqueda);
    }

    public void setOpcionesCampo(String[] campos) {
        comboCampo.removeAllItems();
        for (String campo : campos) {
            comboCampo.addItem(campo);
        }
        comboCampo.setSelectedIndex(0);
    }

    public String getCampoSeleccionado() {
        return (String) comboCampo.getSelectedItem();
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }
}

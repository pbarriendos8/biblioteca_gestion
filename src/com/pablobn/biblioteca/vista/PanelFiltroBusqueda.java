package com.pablobn.biblioteca.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que contiene un filtro de búsqueda para tablas u otros componentes,
 * con un combo para seleccionar el campo por el cual buscar y un campo de texto
 * para ingresar el texto a filtrar.
 * <p>
 * Permite configurar dinámicamente las opciones del combo para elegir el campo
 * y obtener el campo seleccionado y el texto ingresado.
 * </p>
 */
public class PanelFiltroBusqueda extends JPanel {
    private final JComboBox<String> comboCampo;
    private final JTextField campoBusqueda;

    /**
     * Crea un panel con un combo para seleccionar el campo y un campo de texto para buscar.
     * El diseño es un FlowLayout alineado a la izquierda.
     */
    public PanelFiltroBusqueda() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        comboCampo = new JComboBox<>();
        campoBusqueda = new JTextField(20);

        add(new JLabel("Buscar por:"));
        add(comboCampo);
        add(campoBusqueda);
    }

    /**
     * Establece las opciones del combo para seleccionar el campo de búsqueda.
     *
     * @param campos Array de strings con los nombres de los campos disponibles.
     */
    public void setOpcionesCampo(String[] campos) {
        comboCampo.removeAllItems();
        for (String campo : campos) {
            comboCampo.addItem(campo);
        }
        comboCampo.setSelectedIndex(0);
    }

    /**
     * Obtiene el campo actualmente seleccionado en el combo de campos.
     *
     * @return El nombre del campo seleccionado.
     */
    public String getCampoSeleccionado() {
        return (String) comboCampo.getSelectedItem();
    }

    /**
     * Obtiene el campo de texto donde se ingresa el texto para filtrar.
     *
     * @return El JTextField para ingresar texto de búsqueda.
     */
    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }
}

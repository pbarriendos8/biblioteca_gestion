package com.pablobn.biblioteca.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que contiene un combo box para filtrar o seleccionar una opción
 * relacionada con la visualización o filtrado de una tabla u otro componente.
 * <p>
 * Muestra una etiqueta "Ver:" seguida de un combo con las opciones proporcionadas.
 * Permite obtener el combo completo o la opción seleccionada.
 * </p>
 */
public class PanelFiltroTabla extends JPanel {
    private JComboBox<String> comboFiltro;

    /**
     * Constructor que crea un panel con un combo box con las opciones recibidas.
     *
     * @param opciones Array de cadenas con las opciones a mostrar en el combo.
     */
    public PanelFiltroTabla(String[] opciones) {
        comboFiltro = new JComboBox<>(opciones);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Ver:"));
        add(comboFiltro);
    }

    /**
     * Devuelve el JComboBox utilizado para seleccionar la opción de filtro.
     *
     * @return El JComboBox de opciones.
     */
    public JComboBox<String> getComboBox() {
        return comboFiltro;
    }

    /**
     * Obtiene la opción actualmente seleccionada en el combo box.
     *
     * @return La opción seleccionada como String.
     */
    public String getSeleccion() {
        return (String) comboFiltro.getSelectedItem();
    }
}

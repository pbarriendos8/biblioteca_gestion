package com.pablobn.biblioteca.util;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Clase que define un formateador personalizado para campos de texto formateados
 * en componentes Swing, especialmente útil para manejar fechas en formularios.
 * <p>
 * El patrón de fecha utilizado es "yyyy-MM-dd".
 * </p>
 * Esta clase se utiliza comúnmente en conjunto con JDatePicker o componentes similares
 * para formatear la fecha al mostrarla o al obtenerla desde un campo de texto.
 */
public class DateFormat extends JFormattedTextField.AbstractFormatter {

    /**
     * Patrón de formato de fecha utilizado (por ejemplo, 2025-05-25).
     */
    private final String datePattern = "yyyy-MM-dd";

    /**
     * Instancia de {@link SimpleDateFormat} usada para el formateo y análisis de fechas.
     */
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    /**
     * Convierte una cadena en un objeto {@link java.util.Date}.
     *
     * @param text la cadena de entrada con el formato esperado (yyyy-MM-dd)
     * @return el objeto Date resultante del análisis de la cadena
     * @throws ParseException si el texto no cumple con el formato esperado
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    /**
     * Convierte un objeto {@link Calendar} en su representación en cadena.
     *
     * @param value el objeto que contiene la fecha a formatear
     * @return una cadena representando la fecha en el formato yyyy-MM-dd
     * @throws ParseException si ocurre un error al formatear la fecha
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}

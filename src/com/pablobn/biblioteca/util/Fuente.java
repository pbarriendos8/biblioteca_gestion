package com.pablobn.biblioteca.util;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class Fuente {
    public static void aplicarFuentePersonalizada() {
        try {
            InputStream is = new FileInputStream("fonts/Roboto-VariableFont_wdth,wght.ttf");
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f);
            UIManager.put("Label.font", fuente);
            UIManager.put("Button.font", fuente);
            UIManager.put("TextField.font", fuente);
            UIManager.put("PasswordField.font", fuente);
            UIManager.put("TextArea.font", fuente);
            UIManager.put("ComboBox.font", fuente);
            UIManager.put("Table.font", fuente);
            UIManager.put("List.font", fuente);
            UIManager.put("TabbedPane.font", fuente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

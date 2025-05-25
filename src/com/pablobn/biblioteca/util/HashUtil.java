package com.pablobn.biblioteca.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase utilitaria para el manejo de contraseñas utilizando el algoritmo BCrypt.
 * <p>
 * Proporciona métodos para generar un hash seguro a partir de una contraseña en texto plano
 * y para verificar contraseñas comparando el texto plano con un hash almacenado.
 * </p>
 */
public class HashUtil {

    /**
     * Genera un hash seguro de la contraseña utilizando BCrypt con un factor de coste de 12.
     *
     * @param plainPassword la contraseña en texto plano a hashear
     * @return la contraseña hasheada como cadena de texto
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña hasheada.
     *
     * @param plainPassword  la contraseña en texto plano proporcionada por el usuario
     * @param hashedPassword la contraseña previamente hasheada almacenada
     * @return true si las contraseñas coinciden; false en caso contrario
     */
    public static boolean verificarPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

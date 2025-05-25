package com.pablobn.biblioteca.util;

/**
 * Enumeración que representa los posibles estados de un préstamo en la biblioteca.
 * <p>
 * Los valores posibles son:
 * <ul>
 *     <li>{@code ACTIVO}: El préstamo está actualmente en curso.</li>
 *     <li>{@code FINALIZADO}: El préstamo ha concluido y el libro ha sido devuelto.</li>
 * </ul>
 */
public enum EstadoPrestamo {
    ACTIVO,
    FINALIZADO;
}

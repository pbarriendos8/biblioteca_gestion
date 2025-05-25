package com.pablobn.biblioteca.util;

/**
 * Enumeración que representa los distintos tipos de usuario en el sistema.
 * <ul>
 *   <li>{@code ADMIN}: Usuario con privilegios completos, incluyendo gestión total del sistema.</li>
 *   <li>{@code EDITOR}: Usuario con permisos para modificar contenido, pero con acceso limitado a configuraciones.</li>
 *   <li>{@code CONSULTA}: Usuario con permisos solo de lectura.</li>
 * </ul>
 */
public enum TipoUsuario {
    ADMIN,
    EDITOR,
    CONSULTA;
}

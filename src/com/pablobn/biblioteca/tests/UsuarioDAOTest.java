package com.pablobn.biblioteca.tests;

import com.pablobn.biblioteca.modelo.Usuario;
import com.pablobn.biblioteca.modelo.dao.UsuarioDAO;
import com.pablobn.biblioteca.util.HashUtil;
import com.pablobn.biblioteca.util.TipoUsuario;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioDAOTest {

    static Usuario usuario;

    @BeforeAll
    static void setup() {
        usuario = new Usuario();
        usuario.setNombreUsuario("testuser");
        usuario.setNombreCompleto("Test");
        usuario.setPassword(HashUtil.hashPassword("1234")); // usa la misma lógica de tu app
        usuario.setCorreo("testuser@example.com");
    }

    @Test
    @Order(1)
    void testRegistrarUsuario() {
        UsuarioDAO.registrarUsuario(usuario);
        List<Usuario> lista = UsuarioDAO.obtenerTodosUsuarios();
        assertTrue(lista.stream().anyMatch(u -> u.getNombreUsuario().equals("testuser")));
        assertEquals(TipoUsuario.CONSULTA, usuario.getTipoUsuario());
        assertNotNull(usuario.getFechaRegistro());
    }

    @Test
    @Order(2)
    void testAutenticar() {
        Usuario autenticado = UsuarioDAO.autenticar("testuser", "1234");
        assertNotNull(autenticado);
        assertEquals("testuser", autenticado.getNombreUsuario());
    }

    @Test
    @Order(3)
    void testActualizarUsuario() {
        usuario.setNombreCompleto("UsuarioActualizado");
        UsuarioDAO.actualizarUsuario(usuario);
        List<Usuario> lista = UsuarioDAO.obtenerTodosUsuarios();
        Usuario actualizado = lista.stream()
                .filter(u -> u.getNombreUsuario().equals("testuser"))
                .findFirst()
                .orElse(null);
        assertNotNull(actualizado);
        assertEquals("UsuarioActualizado", actualizado.getNombreCompleto());
    }

    @Test
    @Order(4)
    void testObtenerTodosUsuarios() {
        List<Usuario> lista = UsuarioDAO.obtenerTodosUsuarios();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

    @Test
    @Order(5)
    void testEliminarUsuario() {
        UsuarioDAO.eliminarUsuario(usuario);
        List<Usuario> lista = UsuarioDAO.obtenerTodosUsuarios();
        assertFalse(lista.stream().anyMatch(u -> u.getNombreUsuario().equals("testuser")));
    }
}

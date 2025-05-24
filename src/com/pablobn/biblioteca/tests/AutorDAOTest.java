package com.pablobn.biblioteca.tests;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutorDAOTest {

    static Autor autor;

    @BeforeAll
    static void setup() {
        autor = new Autor();
        autor.setNombre("Gabriel García Márquez");
        autor.setNacionalidad("Colombiana");
    }

    @Test
    @Order(1)
    void testGuardarAutor() {
        AutorDAO.guardarAutor(autor);
        List<Autor> lista = AutorDAO.obtenerTodos();
        assertTrue(lista.contains(autor));
    }

    @Test
    @Order(2)
    void testActualizarAutor() {
        autor.setNacionalidad("Mexicana");
        AutorDAO.actualizarAutor(autor);

        List<Autor> lista = AutorDAO.obtenerTodos();
        Autor actualizado = lista.stream()
                .filter(a -> a.getIdAutor() == (autor.getIdAutor()))
                .findFirst()
                .orElse(null);

        assertNotNull(actualizado);
        assertEquals("Mexicana", actualizado.getNacionalidad());
    }

    @Test
    @Order(3)
    void testEliminarAutor() {
        AutorDAO.eliminarAutor(autor);
        List<Autor> lista = AutorDAO.obtenerTodos();
        assertFalse(lista.contains(autor));
    }
}

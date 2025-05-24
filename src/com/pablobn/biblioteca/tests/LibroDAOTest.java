package com.pablobn.biblioteca.tests;

import com.pablobn.biblioteca.modelo.Autor;
import com.pablobn.biblioteca.modelo.Libro;
import com.pablobn.biblioteca.modelo.dao.AutorDAO;
import com.pablobn.biblioteca.modelo.dao.LibroDAO;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibroDAOTest {

    static Libro libro;
    static Autor autor;

    @BeforeAll
    static void setup() {
        autor = new Autor();
        autor.setNombre("J.K.");
        autor.setApellidos("Rowling");
        autor.setNacionalidad("Británica");
        AutorDAO.guardarAutor(autor);

        libro = new Libro();
        libro.setTitulo("Harry Potter y la piedra filosofal");
        libro.setIsbn("111-1-11-111111-1");
        libro.setFechaPublicacion(Date.valueOf("1997-06-26"));
        libro.setAutor(autor);
        libro.setDescripcion("Primer libro de la saga.");
    }

    @Test
    @Order(1)
    void testGuardarLibro() {
        LibroDAO.guardarLibro(libro);
        List<Libro> lista = LibroDAO.obtenerTodosLibros();
        assertTrue(lista.stream().anyMatch(l -> l.getIdLibro() == libro.getIdLibro()));
    }

    @Test
    @Order(2)
    void testActualizarLibro() {
        libro.setDescripcion("Libro actualizado");
        LibroDAO.actualizarLibro(libro);

        List<Libro> lista = LibroDAO.obtenerTodosLibros();
        Libro actualizado = lista.stream()
                .filter(l -> l.getIdLibro() == libro.getIdLibro())
                .findFirst()
                .orElse(null);

        assertNotNull(actualizado);
        assertEquals("Libro actualizado", actualizado.getDescripcion());
    }

    @Test
    @Order(3)
    void testExisteLibroPorTitulo() {
        assertTrue(LibroDAO.existeLibroPorTitulo("Harry Potter y la piedra filosofal"));
        assertFalse(LibroDAO.existeLibroPorTitulo("Prueba, no existe este libro"));
    }

    @Test
    @Order(4)
    void testExisteLibroPorIsbn() {
        assertTrue(LibroDAO.existeLibroPorIsbn("111-1-11-111111-1"));
        assertFalse(LibroDAO.existeLibroPorIsbn("111-1-11-111111-0"));
    }

    @Test
    @Order(5)
    void testExisteOtroLibroConTituloEIsbn() {
        Libro otroLibro = new Libro();
        otroLibro.setTitulo("Harry Potter y la piedra filosofal");
        otroLibro.setIsbn("111-1-11-111111-1");
        otroLibro.setAutor(autor);
        otroLibro.setFechaPublicacion(Date.valueOf("1997-06-26"));
        LibroDAO.guardarLibro(otroLibro);

        assertTrue(LibroDAO.existeOtroLibroConTitulo(libro.getTitulo(), otroLibro.getIdLibro()));
        assertTrue(LibroDAO.existeOtroLibroConIsbn(libro.getIsbn(), otroLibro.getIdLibro()));

        LibroDAO.eliminarLibro(otroLibro);
    }

    @Test
    @Order(6)
    void testEliminarLibro() {
        LibroDAO.eliminarLibro(libro);
        List<Libro> lista = LibroDAO.obtenerTodosLibros();
        assertFalse(lista.stream().anyMatch(l -> l.getIdLibro() == libro.getIdLibro()));
    }
}

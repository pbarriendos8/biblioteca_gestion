package com.pablobn.biblioteca.tests;

import com.pablobn.biblioteca.modelo.Prestamo;
import com.pablobn.biblioteca.modelo.dao.PrestamoDAO;
import com.pablobn.biblioteca.util.EstadoPrestamo;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrestamoDAOTest {

    static Prestamo prestamo;
    static int idUsuario = 1;
    static int idLibro = 1;

    @BeforeAll
    static void setup() {
        prestamo = new Prestamo();
        prestamo.setFechaInicio(new Date(System.currentTimeMillis()));
        prestamo.setFechaFin(Date.valueOf("2025-12-31"));
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
    }

    @Test
    @Order(1)
    void testGuardarPrestamo() {
        PrestamoDAO.guardarPrestamo(prestamo, idUsuario, idLibro);
        List<Prestamo> lista = PrestamoDAO.obtenerTodosPrestamos();
        assertTrue(lista.stream().anyMatch(p -> p.getIdPrestamo() == prestamo.getIdPrestamo()));
    }

    @Test
    @Order(2)
    void testActualizarPrestamo() {
        prestamo.setEstado(EstadoPrestamo.FINALIZADO);
        PrestamoDAO.actualizarPrestamo(prestamo);

        List<Prestamo> lista = PrestamoDAO.obtenerTodosPrestamos();
        Prestamo actualizado = lista.stream()
                .filter(p -> p.getIdPrestamo() == prestamo.getIdPrestamo())
                .findFirst()
                .orElse(null);

        assertNotNull(actualizado);
        assertEquals(EstadoPrestamo.FINALIZADO, actualizado.getEstado());
    }

    @Test
    @Order(3)
    void testFinalizarPrestamo() {
        PrestamoDAO.finalizarPrestamo(prestamo.getIdPrestamo());

        List<Prestamo> lista = PrestamoDAO.obtenerTodosPrestamos();
        Prestamo finalizado = lista.stream()
                .filter(p -> p.getIdPrestamo() == prestamo.getIdPrestamo())
                .findFirst()
                .orElse(null);

        assertNotNull(finalizado);
        assertEquals(EstadoPrestamo.FINALIZADO, finalizado.getEstado());
        assertNotNull(finalizado.getFechaDevolucionReal());
    }

    @Test
    @Order(4)
    void testEliminarPrestamo() {
        PrestamoDAO.eliminarPrestamo(prestamo);
        List<Prestamo> lista = PrestamoDAO.obtenerTodosPrestamos();
        assertFalse(lista.stream().anyMatch(p -> p.getIdPrestamo() == prestamo.getIdPrestamo()));
    }
}

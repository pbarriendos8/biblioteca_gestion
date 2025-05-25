package com.pablobn.biblioteca.modelo;

import com.pablobn.biblioteca.util.EstadoPrestamo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * Entidad que representa un préstamo de un libro en la base de datos.
 * Contiene información sobre fechas del préstamo, estado, observaciones,
 * y relaciones con el usuario que realiza el préstamo y el libro prestado.
 */
@Entity
@Table(name = "prestamos", schema = "biblioteca_pbarriendos", catalog = "")
public class Prestamo {
    /**
     * Identificador único del préstamo.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_prestamo")
    private int idPrestamo;

    /**
     * Fecha de inicio del préstamo.
     */
    @Basic
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    /**
     * Fecha límite para la devolución del libro.
     */
    @Basic
    @Column(name = "fecha_fin")
    private Date fechaFin;

    /**
     * Fecha real en que se devolvió el libro.
     */
    @Basic
    @Column(name = "fecha_devolucion_real")
    private Date fechaDevolucionReal;

    /**
     * Estado actual del préstamo (ACTIVO, FINALIZADO).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPrestamo estado;

    /**
     * Observaciones o comentarios adicionales sobre el préstamo.
     */
    @Basic
    @Column(name = "observaciones")
    private String observaciones;

    /**
     * Usuario que realizó el préstamo.
     * Relación ManyToOne con la entidad Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    /**
     * Libro prestado.
     * Relación OneToOne con la entidad Libro.
     */
    @OneToOne
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro")
    private Libro libro;

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return idPrestamo == prestamo.idPrestamo && Objects.equals(fechaInicio, prestamo.fechaInicio) && Objects.equals(fechaFin, prestamo.fechaFin) && Objects.equals(fechaDevolucionReal, prestamo.fechaDevolucionReal) && Objects.equals(estado, prestamo.estado) && Objects.equals(observaciones, prestamo.observaciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo, fechaInicio, fechaFin, fechaDevolucionReal, estado, observaciones);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}

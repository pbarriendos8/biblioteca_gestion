package com.pablobn.biblioteca.modelo;

import com.pablobn.biblioteca.util.EstadoPrestamo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "prestamos", schema = "biblioteca_pbarriendos", catalog = "")
public class Prestamo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_prestamo")
    private int idPrestamo;
    @Basic
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Basic
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Basic
    @Column(name = "fecha_devolucion_real")
    private Date fechaDevolucionReal;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPrestamo estado;
    @Basic
    @Column(name = "observaciones")
    private String observaciones;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;
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

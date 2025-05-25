package com.pablobn.biblioteca.modelo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

/**
 * Entidad que representa un libro en la base de datos.
 * Contiene información sobre el libro, como título, descripción,
 * fecha de publicación, portada, archivo PDF, ISBN, disponibilidad,
 * autor asociado, y el préstamo actual si existe.
 */
@Entity
@Table(name = "libros", schema = "biblioteca_pbarriendos", catalog = "")
public class Libro {
    /**
     * Identificador único del libro.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_libro")
    private int idLibro;

    /**
     * Título del libro.
     */
    @Basic
    @Column(name = "titulo")
    private String titulo;

    /**
     * Descripción o sinopsis del libro.
     */
    @Basic
    @Column(name = "descripcion")
    private String descripcion;

    /**
     * Fecha de publicación del libro.
     */
    @Basic
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;

    /**
     * Imagen de portada del libro almacenada como arreglo de bytes.
     */
    @Basic
    @Column(name = "portada")
    private byte[] portada;

    /**
     * Archivo PDF del libro almacenado como arreglo de bytes.
     */
    @Basic
    @Column(name = "archivo_pdf")
    private byte[] archivoPdf;

    /**
     * Código ISBN del libro.
     */
    @Basic
    @Column(name = "isbn")
    private String isbn;

    /**
     * Estado de disponibilidad del libro.
     * True si está disponible, false si no.
     */
    @Basic
    @Column(name = "disponible")
    private boolean disponible;

    /**
     * Autor asociado a este libro.
     * Relación ManyToOne con la entidad Autor.
     */
    @ManyToOne
    @JoinColumn(name = "id_autor", referencedColumnName = "id_autor")
    private Autor autor;

    /**
     * Préstamo actual asociado al libro.
     * Relación OneToOne con la entidad Prestamo.
     */
    @OneToOne
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro", nullable = false)
    private Prestamo prestamo;

    /**
     * Nombre del archivo PDF asociado al libro.
     */
    @Basic
    @Column(name = "nombre_archivo_pdf")
    private String nombreArchivoPdf;

    public String getNombreArchivoPdf() {
        return nombreArchivoPdf;
    }

    public void setNombreArchivoPdf(String nombreArchivoPdf) {
        this.nombreArchivoPdf = nombreArchivoPdf;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public byte[] getPortada() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada = portada;
    }

    public byte[] getArchivoPdf() {
        return archivoPdf;
    }

    public void setArchivoPdf(byte[] archivoPdf) {
        this.archivoPdf = archivoPdf;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return idLibro == libro.idLibro && disponible == libro.disponible && Objects.equals(titulo, libro.titulo) && Objects.equals(descripcion, libro.descripcion) && Objects.equals(fechaPublicacion, libro.fechaPublicacion) && Arrays.equals(portada, libro.portada) && Arrays.equals(archivoPdf, libro.archivoPdf) && Objects.equals(isbn, libro.isbn) && Objects.equals(nombreArchivoPdf, libro.nombreArchivoPdf);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idLibro, titulo, descripcion, fechaPublicacion, isbn, disponible, nombreArchivoPdf);
        result = 31 * result + Arrays.hashCode(portada);
        result = 31 * result + Arrays.hashCode(archivoPdf);
        return result;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    @Override
    public String toString() {
        return this.titulo;
    }
}

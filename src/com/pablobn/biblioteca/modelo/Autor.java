package com.pablobn.biblioteca.modelo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa un autor en la base de datos.
 * Contiene información personal y biográfica del autor,
 * así como la lista de libros asociados a él.
 */
@Entity
@Table(name = "autores", schema = "biblioteca_pbarriendos", catalog = "")
public class Autor {
    /**
     * Identificador único del autor.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_autor")
    private int idAutor;

    /**
     * Nombre del autor.
     */
    @Basic
    @Column(name = "nombre")
    private String nombre;

    /**
     * Apellidos del autor.
     */
    @Basic
    @Column(name = "apellidos")
    private String apellidos;

    /**
     * Fecha de nacimiento del autor.
     */
    @Basic
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    /**
     * Nacionalidad del autor.
     */
    @Basic
    @Column(name = "nacionalidad")
    private String nacionalidad;

    /**
     * Biografía del autor.
     */
    @Basic
    @Column(name = "biografia")
    private String biografia;

    /**
     * Foto del autor almacenada como arreglo de bytes.
     */
    @Basic
    @Column(name = "foto")
    private byte[] foto;

    /**
     * Lista de libros asociados a este autor.
     * Relación OneToMany con la entidad Libro.
     */
    @OneToMany(mappedBy = "autor")
    private List<Libro> libros;

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return idAutor == autor.idAutor && Objects.equals(nombre, autor.nombre) && Objects.equals(apellidos, autor.apellidos) && Objects.equals(fechaNacimiento, autor.fechaNacimiento) && Objects.equals(nacionalidad, autor.nacionalidad) && Objects.equals(biografia, autor.biografia) && Arrays.equals(foto, autor.foto);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idAutor, nombre, apellidos, fechaNacimiento, nacionalidad, biografia);
        result = 31 * result + Arrays.hashCode(foto);
        return result;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return this.nombre + this.apellidos;
    }
}

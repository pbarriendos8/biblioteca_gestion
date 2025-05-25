package com.pablobn.biblioteca.modelo;

import com.pablobn.biblioteca.util.TipoUsuario;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa un usuario de la biblioteca.
 * Contiene datos personales, credenciales de acceso y tipo de usuario.
 * Además, mantiene la relación con los préstamos realizados por el usuario.
 */
@Entity
@Table(name = "usuarios", schema = "biblioteca_pbarriendos", catalog = "")
public class Usuario {
    /**
     * Identificador único del usuario.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_usuario")
    private int idUsuario;

    /**
     * Nombre de usuario para login.
     */
    @Basic
    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    /**
     * Correo electrónico del usuario.
     */
    @Basic
    @Column(name = "correo")
    private String correo;

    /**
     * Contraseña cifrada del usuario.
     */
    @Basic
    @Column(name = "password")
    private String password;

    /**
     * Tipo de usuario (ADMIN, EDITOR, CONSULTA).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    private TipoUsuario tipoUsuario;

    /**
     * Nombre completo real del usuario.
     */
    @Basic
    @Column(name = "nombre_completo")
    private String nombreCompleto;

    /**
     * Fecha en que se registró el usuario.
     */
    @Basic
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    /**
     * Dirección física del usuario.
     */
    @Basic
    @Column(name = "direccion")
    private String direccion;

    /**
     * Número telefónico del usuario.
     */
    @Basic
    @Column(name = "telefono")
    private String telefono;

    /**
     * Lista de préstamos realizados por el usuario.
     * Relación OneToMany con la entidad Prestamo.
     */
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos;

    public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public TipoUsuario getTipoUsuario() {
            return tipoUsuario;
        }

        public void setTipoUsuario(TipoUsuario tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public Date getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(Date fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Usuario usuario = (Usuario) o;
            return idUsuario == usuario.idUsuario && Objects.equals(nombreUsuario, usuario.nombreUsuario) && Objects.equals(correo, usuario.correo) && Objects.equals(password, usuario.password) && Objects.equals(tipoUsuario, usuario.tipoUsuario) && Objects.equals(nombreCompleto, usuario.nombreCompleto) && Objects.equals(fechaRegistro, usuario.fechaRegistro) && Objects.equals(direccion, usuario.direccion) && Objects.equals(telefono, usuario.telefono);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idUsuario, nombreUsuario, correo, password, tipoUsuario, nombreCompleto, fechaRegistro, direccion, telefono);
        }

        public List<Prestamo> getPrestamos() {
            return prestamos;
        }

        public void setPrestamos(List<Prestamo> prestamos) {
            this.prestamos = prestamos;
        }

        @Override
        public String toString() {
            return this.nombreUsuario;
        }
    }

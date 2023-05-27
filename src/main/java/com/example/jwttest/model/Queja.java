package com.example.jwttest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "queja")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Queja {
    @Id
    @Column(name = "correlativo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long correlativo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "oficina")
    private String oficina;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "id_empleado_atendio")
    private String idEmpleadoAtendio;

    @Column(name = "id_tipo_queja")
    private Long idTipoQueja;

    @Column(name = "id_tipo_ingreso")
    private Long idTipoIngreso;

    @Column(name = "filename")
    private String filename;

    @Column(name = "id_medio_ingreso")
    private Long idMedioIngreso;

    @Column(name = "id_estado")
    private Long idEstado;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "fecha_modifica")
    private Date fechaModifica;

    @Column(name = "id_punto_asignado")
    private Long idPuntoAsignado;

    public Long getCorrelativo() {
        return this.correlativo;
    }

    public void setCorrelativo(Long correlativo) {
        this.correlativo = correlativo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getOficina() {
        return this.oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdEmpleadoAtendio() {
        return this.idEmpleadoAtendio;
    }

    public void setIdEmpleadoAtendio(String idEmpleadoAtendio) {
        this.idEmpleadoAtendio = idEmpleadoAtendio;
    }

    public Long getIdTipoQueja() {
        return this.idTipoQueja;
    }

    public void setIdTipoQueja(Long idTipoQueja) {
        this.idTipoQueja = idTipoQueja;
    }

    public Long getIdTipoIngreso() {
        return this.idTipoIngreso;
    }

    public void setIdTipoIngreso(Long idTipoIngreso) {
        this.idTipoIngreso = idTipoIngreso;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getIdMedioIngreso() {
        return this.idMedioIngreso;
    }

    public void setIdMedioIngreso(Long idMedioIngreso) {
        this.idMedioIngreso = idMedioIngreso;
    }

    public Long getIdEstado() {
        return this.idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Long getIdPuntoAsignado() {return idPuntoAsignado;}

    public void setIdPuntoAsignado(Long idPuntoAsignado) {this.idPuntoAsignado = idPuntoAsignado;}
}

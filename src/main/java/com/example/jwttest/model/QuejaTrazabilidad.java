package com.example.jwttest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "queja_trazabilidad")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuejaTrazabilidad {
    @Id
    @Column(name = "id_trazabilidad")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrazabilidad;

    @Column(name = "id_queja")
    private Long idQueja;

    @Column(name = "id_etapa")
    private Long idEtapa;

    @Column(name = "id_etapa_anterior")
    private Long idEtapaAnterior;

    @Column(name = "hora_ingreso")
    private Date horaIngreso;

    @Column(name = "id_empleado_asignacion")
    private Long idEmpleadoAsignacion;

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

    public Long getIdTrazabilidad() {
        return this.idTrazabilidad;
    }

    public void setIdTrazabilidad(Long idTrazabilidad) {
        this.idTrazabilidad = idTrazabilidad;
    }

    public Long getIdQueja() {
        return this.idQueja;
    }

    public void setIdQueja(Long idQueja) {
        this.idQueja = idQueja;
    }

    public Long getIdEtapa() {
        return this.idEtapa;
    }

    public void setIdEtapa(Long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public Long getIdEtapaAnterior() {
        return this.idEtapaAnterior;
    }

    public void setIdEtapaAnterior(Long idEtapaAnterior) {
        this.idEtapaAnterior = idEtapaAnterior;
    }

    public Date getHoraIngreso() {
        return this.horaIngreso;
    }

    public void setHoraIngreso(Date horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public Long getIdEmpleadoAsignacion() {
        return this.idEmpleadoAsignacion;
    }

    public void setIdEmpleadoAsignacion(Long idEmpleadoAsignacion) {
        this.idEmpleadoAsignacion = idEmpleadoAsignacion;
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
}

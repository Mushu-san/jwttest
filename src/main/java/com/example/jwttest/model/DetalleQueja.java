package com.example.jwttest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_queja")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleQueja {
    @Id
    @Column(name = "id_detalle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @Column(name = "id_queja")
    private Long idQueja;

    @Column(name = "comentario")
    private String comentario;

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

    public Long getIdDetalle() {
        return this.idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdQueja() {
        return this.idQueja;
    }

    public void setIdQueja(Long idQueja) {
        this.idQueja = idQueja;
    }

    public String getComentario() {
        return this.comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

package com.example.jwttest.model;

import com.example.jwttest.repository.RolRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Entity
@Table(name = "empleado")
@Builder
@AllArgsConstructor
public class Empleado{
    @Id
    @Column(name = "dpi")
    private Long dpi;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "pass")
    private String pass;

    @Column(name = "id_perfil")
    private Long idPerfil;

    @Column(name = "id_punto")
    private Long idPunto;

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

    public Empleado() {
        super();
    }

    public Long getDpi() {
        return this.dpi;
    }

    public void setDpi(Long dpi) {
        this.dpi = dpi;
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

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Long getIdPerfil() {
        return this.idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Long getIdPunto() {
        return this.idPunto;
    }

    public void setIdPunto(Long idPunto) {
        this.idPunto = idPunto;
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

    public UserInfo getUserInfo(){
        return UserInfo.builder()
                .name(this.usuario)
                .password(this.pass)
                .email(this.email)
                .roles(String.valueOf(this.idPerfil))
                .build();
    }

}

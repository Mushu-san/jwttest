package com.example.jwttest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historial_operaciones")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialOperaciones {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_registro")
    private String idRegistro;

    @Column(name = "tabla_operacion")
    private String tablaOperacion;

    @Column(name = "registro")
    private String registro;

    @Column(name = "id_tipo_operacion")
    private Long idTipoOperacion;

    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Column(name = "fecha_modifica")
    private String fechaModifica;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdRegistro() {
        return this.idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getTablaOperacion() {
        return this.tablaOperacion;
    }

    public void setTablaOperacion(String tablaOperacion) {
        this.tablaOperacion = tablaOperacion;
    }

    public String getRegistro() {
        return this.registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Long getIdTipoOperacion() {
        return this.idTipoOperacion;
    }

    public void setIdTipoOperacion(Long idTipoOperacion) {
        this.idTipoOperacion = idTipoOperacion;
    }

    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(String fechaModifica) {
        this.fechaModifica = fechaModifica;
    }
}

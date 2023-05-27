package com.example.jwttest.projection;

import java.util.Date;

/**
 * User: Angelo
 * Date: 24/05/2023
 * Time: 17:29
 */
public interface QuejasCentralizadorProjection {

    String getIdQueja();

    Date getFechaCreacion();

    String getCorrelativoVisual();

    String getFilename();

    String getNombreEstadoExterno();

    String getComentario();

    String getIdEtapa();

    String getUsuarioCreacion();

    String getNombreEstado();

    String getNombreEtapa();
    String getIdEmpleado();

    String getDetalleQueja();

    String getNombrePunto();

    String getObservacionQueja();

    String getIdEstado();

    Date getFechaModifica();

}

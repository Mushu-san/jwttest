package com.example.jwttest.repository;

import com.example.jwttest.model.Queja;
import com.example.jwttest.projection.EmpleadosForEmailProjection;
import com.example.jwttest.projection.QuejasCentralizadorProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * User: Angelo
 * Date: 23/05/2023
 * Time: 10:09
 */
public interface QuejaRepository extends CrudRepository<Queja, Long> {

    @Query(value = "select sb.numero_registro ro\n" +
            "from (select cd2.nombre || '-' ||\n" +
            "       ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion)  AS numero_registro,\n" +
            "       cd.correlativo as id\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja) sb\n" +
            "order by sb.id desc limit 1", nativeQuery = true)
    String getCorrelativeQuery();

    @Query(value = "select q.correlativo as idQueja, q.filename as filename, q.fecha_creacion as fechaCreacion, secuencia.numero_registro as correlativoVisual\n" +
            "from bd_2.queja q\n" +
            "inner join bd_2.queja_trazabilidad qt on q.correlativo = qt.id_queja \n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.id_estado = 21\n" +
            "and qt.id_etapa = 22\n" +
            "and qt.id_estado = 23", nativeQuery = true)
    List<QuejasCentralizadorProjection> getQuejasForCentralizadorMaster();

    @Query(value = "select q.correlativo as idQueja, q.fecha_creacion as fechaCreacion, secuencia.numero_registro as correlativoVisual\n" +
            "from bd_2.queja q\n" +
            "inner join bd_2.queja_trazabilidad qt on q.correlativo = qt.id_queja \n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.id_estado = 21\n" +
            "and qt.id_etapa = 22\n" +
            "and qt.id_estado = 23\n" +
            "and q.correlativo = :idQueja", nativeQuery = true)
    Optional<QuejasCentralizadorProjection> getCorrelativoQuejaById(@Param("idQueja") Long idQueja);

    @Query(value = "select q.correlativo as idQueja, q.fecha_creacion as fechaCreacion, secuencia.numero_registro as correlativoVisual\n" +
            "from bd_2.queja q\n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.correlativo = :idQueja", nativeQuery = true)
    Optional<QuejasCentralizadorProjection> getCorrelativoQuejaByIdNotRestriction(@Param("idQueja") Long idQueja);

    @Query(value = "select e.email as correo,\n" +
            "e.nombre as nombre\n" +
            "from bd_2.empleado e \n" +
            "inner join bd_2.punto_acceso pa \n" +
            "on pa.id = e.id_punto \n" +
            "where e.id_estado = 9\n" +
            "and e.id_perfil in (4, 5, 6, 7)\n" +
            "and pa.id_estado = 3\n" +
            "and pa.id = :idPunto", nativeQuery = true)
    List<EmpleadosForEmailProjection> getEmpleadosForAnounceQuejaByPunto(@Param("idPunto") Long idPunto);

    @Query(value = "select q.correlativo as idQueja, \n" +
            "secuencia.numero_registro as correlativoVisual,\n" +
            "cd3.nombre as nombreEstadoExterno,\n" +
            "qt.id_etapa as idEtapa,\n" +
            "qt.id_empleado_asignacion as idEmpleado,\n" +
            "q.filename as filename,\n" +
            "q.descripcion as comentario \n" +
            "from bd_2.queja_trazabilidad qt \n" +
            "inner join bd_2.queja q \n" +
            "on qt.id_queja = q.correlativo \n" +
            "inner join bd_2.catalogo_dato cd3\n" +
            "on cd3.id = qt.id_etapa \n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.id_estado = 28\n" +
            "and qt.id_etapa in (29, 35, 38)\n" +
            "and q.id_punto_asignado = (select d.id_punto from bd_2.empleado d where d.dpi = :dpi )\n" +
            "and qt.id_trazabilidad in (\n" +
            "select max(qt.id_trazabilidad)\n" +
            "from bd_2.queja_trazabilidad qt \n" +
            "inner join bd_2.queja q on q.correlativo = qt.id_queja \n" +
            "where q.correlativo in (\n" +
            "\tselect q2.correlativo \n" +
            "\tfrom bd_2.queja q2\n" +
            "\twhere q2.id_estado = 28\n" +
            ")\n" +
            "group by q.correlativo\n" +
            ")", nativeQuery = true)
    List<QuejasCentralizadorProjection> getQuejasForFollowPoint(@Param("dpi") Long dpi);

    @Query(value = "select q.correlativo as idQueja, \n" +
            "secuencia.numero_registro as correlativoVisual,\n" +
            "cd3.nombre as nombreEstadoExterno,\n" +
            "qt.id_etapa as idEtapa,\n" +
            "qt.id_empleado_asignacion as idEmpleado,\n" +
            "q.filename as filename,\n" +
            "q.descripcion as comentario \n" +
            "from bd_2.queja_trazabilidad qt \n" +
            "inner join bd_2.queja q \n" +
            "on qt.id_queja = q.correlativo \n" +
            "inner join bd_2.catalogo_dato cd3\n" +
            "on cd3.id = qt.id_etapa \n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.id_estado = 28\n" +
            "and q.id_punto_asignado = (select d.id_punto from bd_2.empleado d where d.dpi = :dpi )\n" +
            "and qt.id_etapa in (36, 37)\n" +
            "and qt.id_trazabilidad in (\n" +
            "select max(qt.id_trazabilidad)\n" +
            "from bd_2.queja_trazabilidad qt \n" +
            "inner join bd_2.queja q on q.correlativo = qt.id_queja \n" +
            "where q.correlativo in (\n" +
            "\tselect q2.correlativo \n" +
            "\tfrom bd_2.queja q2\n" +
            "\twhere q2.id_estado = 28\n" +
            ")\n" +
            "group by q.correlativo\n" +
            ")", nativeQuery = true)
    List<QuejasCentralizadorProjection> getQuejasForFollowCentralizador(@Param("dpi") Long dpi);

    @Query(value = "select q.correlativo as idQueja,\n" +
            "secuencia.numero_registro as correlativoVisual,\n" +
            "cd.nombre as nombreEstado,\n" +
            "cd2.nombre as nombreEtapa,\n" +
            "q.descripcion as detalleQueja,\n" +
            "pa.nombre as nombrePunto,\n" +
            "q.fecha_creacion as fechaCreacion,\n" +
            "q.usuario_creacion as usuarioCreacion,\n" +
            "q.filename as filename,\n" +
            "(select max(dq.comentario) \n" +
            "from bd_2.detalle_queja dq \n" +
            "where dq.id_queja  = q.correlativo\n" +
            "and dq.id_estado = 34) as observacionQueja\n" +
            "from bd_2.queja q\n" +
            "inner join bd_2.queja_trazabilidad qt on qt.id_queja = q.correlativo \n" +
            "inner join bd_2.catalogo_dato cd on cd.id = q.id_estado \n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = qt.id_etapa \n" +
            "left join bd_2.punto_acceso pa on pa.id = q.id_punto_asignado \n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "where q.correlativo = :idQueja\n" +
            "order by qt.id_trazabilidad desc limit 1", nativeQuery = true)
    QuejasCentralizadorProjection getDetalleForQuejaById(@Param("idQueja") Long idQueja);

    @Query(value = "select e.dpi \n" +
            "from bd_2.queja q \n" +
            "inner join bd_2.punto_acceso pa on pa.id = q.id_punto_asignado \n" +
            "inner join bd_2.empleado e on e.id_punto = pa.id \n" +
            "where q.correlativo = :idQueja \n" +
            "and e.id_perfil = 9\n" +
            "and e.id_estado = 9", nativeQuery = true)
    String getCentralizadorByAsignacionQueja(Long idQueja);

    @Query(value = "select dq.comentario as comentario \n" +
            "from bd_2.detalle_queja dq \n" +
            "where dq.id_queja  = :idQueja\n" +
            "and dq.id_estado = 34\n" +
            "and dq.id_detalle = (select max(dq2.id_detalle) from bd_2.detalle_queja dq2 where dq2.id_queja = :idQueja )", nativeQuery = true)
    String getComentarioByIdQueja(Long idQueja);

    @Query(value = "select secuencia.numero_registro as correlativoVisual, \n" +
            "cd3.nombre as nombreEstado, q.id_estado as idEstado,\n" +
            "q.fecha_creacion as fechaCreacion, q.fecha_modifica as fechaModifica\n" +
            "from bd_2.queja q\n" +
            "inner join (select\n" +
            "cd.correlativo as correlativo,\n" +
            "cd2.nombre || '-' || ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM cd.fecha_creacion) ORDER BY cd.fecha_creacion) || '-' || EXTRACT(YEAR FROM cd.fecha_creacion) as numero_registro\n" +
            "from bd_2.queja cd\n" +
            "inner join bd_2.catalogo_dato cd2 on cd2.id = cd.id_tipo_queja\n" +
            ") secuencia on secuencia.correlativo = q.correlativo\n" +
            "inner join bd_2.catalogo_dato cd3 on cd3.id = q.id_estado \n" +
            "where secuencia.numero_registro = :correlativo", nativeQuery = true)
    QuejasCentralizadorProjection getInfoQuejaByCorrelativo(@Param("correlativo") String correlativo);
}

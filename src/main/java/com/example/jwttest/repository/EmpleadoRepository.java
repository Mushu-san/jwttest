package com.example.jwttest.repository;

import com.example.jwttest.model.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * User: Angelo
 * Date: 6/05/2023
 * Time: 08:42
 */
public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {
    Optional<Empleado> findByUsuario(String user);

    Optional<Empleado> findByUsuarioAndPassNotNull(String user);

    List<Empleado> findByIdPuntoAndIdEstadoNot(Long idPunto, Long idEstadoNot);

    @Query(value = "select * \n" +
            "from bd_2.empleado e \n" +
            "where e.id_perfil = 9\n" +
            "and e.id_estado = 9\n" +
            "and e.id_punto = :idPunto\n" +
            "limit 1", nativeQuery=true)
    Optional<Empleado> findCentralizadorByPunto(Long idPunto);

    @Query(value = "select * \n" +
            "from bd_2.empleado e \n" +
            "where e.id_perfil = 9\n" +
            "and e.id_estado = 9\n" +
            "limit 1", nativeQuery=true)
    Optional<Empleado> findCentralizadorMaster();

    List<Empleado> findByIdPuntoAndIdEstadoAndIdPerfilNotIn(Long idPunto, Long idEstado, List<Long> idPerfil);
}

package com.example.jwttest.repository;

import com.example.jwttest.model.PuntoAcceso;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 18:59
 */
public interface PuntoAccesoRepository extends CrudRepository<PuntoAcceso, Long> {

    @Override
    List<PuntoAcceso> findAll();

    List<PuntoAcceso> findByIdRegionAndIdEstado(Long idRegion, Long idEstado);

    List<PuntoAcceso> findByIdEstado(Long idEstado);
}

package com.example.jwttest.repository;

import com.example.jwttest.model.CatalogoDato;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 19:11
 */
public interface CatalogoDatoRepository extends CrudRepository<CatalogoDato, Long> {

    List<CatalogoDato> findByIdCatalogoAndIdEstado(Long idCatalogo, Long idEstado);

    List<CatalogoDato> findByIdCatalogo(Long idCatalogo);
}


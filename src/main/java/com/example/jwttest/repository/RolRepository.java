package com.example.jwttest.repository;

import com.example.jwttest.model.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: Angelo
 * Date: 6/05/2023
 * Time: 08:44
 */
public interface RolRepository extends CrudRepository<Rol, Long> {

    List<Rol> findByIdEstado(Long idEstado);
}

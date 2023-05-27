package com.example.jwttest.repository;

import com.example.jwttest.model.Cuentahabiente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * User: Angelo
 * Date: 15/05/2023
 * Time: 21:17
 */
public interface CuentahabienteRepository extends CrudRepository<Cuentahabiente, Long> {
    Optional<Cuentahabiente> findByCorreo(String correo);
    Optional<Cuentahabiente> findByCorreoAndPassNotNull(String correo);

}

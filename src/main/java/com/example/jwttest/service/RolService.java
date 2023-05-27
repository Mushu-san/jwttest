package com.example.jwttest.service;

import com.example.jwttest.model.Rol;
import com.example.jwttest.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Angelo
 * Date: 19/05/2023
 * Time: 20:42
 */
@Service
@Slf4j
public class RolService {
    @Autowired
    RolRepository rolRepository;

    public List<Rol> getAllRolesByEstado(Long idEstado) {
        return rolRepository.findByIdEstado(idEstado);
    }

}

package com.example.jwttest.controller;

import com.example.jwttest.model.Rol;
import com.example.jwttest.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: Angelo
 * Date: 19/05/2023
 * Time: 20:43
 */
@RestController
@RequestMapping("rol")
@CrossOrigin(origins = "*")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("getAllRolesByEstado/{idEstado}")
    public ResponseEntity<List<Rol>> getAllRolesByEstado(@PathVariable  Long idEstado){
        return ResponseEntity.ok(rolService.getAllRolesByEstado(idEstado));
    }

}

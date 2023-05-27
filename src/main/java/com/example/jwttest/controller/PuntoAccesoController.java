package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.PuntoAcceso;
import com.example.jwttest.service.PuntoAccesoService;
import com.example.jwttest.utils.ApiIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 19:24
 */
@RestController
@RequestMapping("punto-acceso")
@CrossOrigin(origins = "https://mi-pistio-front.herokuapp.com")
public class PuntoAccesoController {

    @Autowired
    private PuntoAccesoService puntoAccesoService;

    @GetMapping("All")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PuntoAcceso>> getAllPuntosAcceso(){
        return ResponseEntity.ok(puntoAccesoService.getAllPuntosAcceso());
    }

    @GetMapping("AllByRegionAndEstado/{idRegion}/{idEstado}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_ADMIN') " +
            "|| hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<List<PuntoAcceso>> getPuntosAccesoByRegion(@PathVariable Long idRegion, @PathVariable Long idEstado){
        return ResponseEntity.ok(puntoAccesoService.getPuntosAccesoByRegion(idRegion, idEstado));
    }

    @PostMapping("save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_ADMIN')")
    public ResponseEntity<Object> savePuntoAcceso(@RequestBody Map<String, Object> dto, @ApiIgnore UserDto user){
        return ResponseEntity.ok(puntoAccesoService.savePuntoAcceso(dto, user));
    }

    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_ADMIN')")
    public ResponseEntity<Object> updatePuntoAcceso(@RequestBody Map<String, Object> dto, @ApiIgnore UserDto user){
        return ResponseEntity.ok(puntoAccesoService.updatePuntoAcceso(dto, user));
    }

    @GetMapping("getPuntosForQueja")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_RECEPTOR') || hasAnyAuthority('ROLE_CUENTAHABIENTE')")
    public ResponseEntity<?> getPuntosForQueja(){
        return ResponseEntity.ok(puntoAccesoService.getPuntosForQueja());
    }

}

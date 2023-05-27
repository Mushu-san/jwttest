package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.service.CatalogoService;
import com.example.jwttest.utils.ApiIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * User: Angelo
 * Date: 18/05/2023
 * Time: 22:17
 */
@RestController
@RequestMapping("catalogo")
@CrossOrigin(origins = "http://test.mydomain.com:4200")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("{idCatalogo}/{idEstado}")
    public ResponseEntity<?> getCatalogo(@PathVariable("idCatalogo") Long idCatalogo, @PathVariable("idEstado") Long idEstado){
        return ResponseEntity.ok().body(catalogoService.findByCatalogoAndEstado(idCatalogo, idEstado));
    }

    @GetMapping("tipo-queja")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTipoQueja(){
        return ResponseEntity.ok().body(catalogoService.findTipoQueja());
    }

    @PostMapping("saveTipoQueja")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveTipoQueja(@RequestBody Map<String, Object> dto, @ApiIgnore UserDto user){
        return ResponseEntity.ok().body(catalogoService.saveTipoQueja(dto, user));
    }

    @PutMapping("updateTipoQueja")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateTipoQueja(@RequestBody Map<String, Object> dto, @ApiIgnore UserDto user){
        return ResponseEntity.ok().body(catalogoService.updateTipoQueja(dto, user));
    }
}

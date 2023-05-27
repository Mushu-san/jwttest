package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.service.QuejaService;
import com.example.jwttest.storage.StorageFileNotFoundException;
import com.example.jwttest.utils.ApiIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Nullable;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Angelo
 * Date: 23/05/2023
 * Time: 15:43
 */
@RestController
@RequestMapping("queja")
@CrossOrigin(origins = "http://test.mydomain.com:4200")
public class QuejaController {

    @Autowired
    QuejaService quejaService;


    @PostMapping("create")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_CUENTAHABIENTE') || hasRole('ROLE_EMPLEADO_RECEPTOR')")
    public ResponseEntity<?> createQueja(@RequestPart("file") @Nullable MultipartFile file, @RequestPart("dto") String dto, UserDto user) throws IOException {
        Map<String, Object> map = new Gson().fromJson(dto, new TypeToken<Map<String, Object>>() {}.getType());
        Object response = new Object();

        try{
            response = quejaService.createQueja(map, user, file);
        }catch (Exception e){
            System.out.println("error " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/centralizador/master")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER')")
    public ResponseEntity<?> getQuejasForCentralizadorMaster(){
        return ResponseEntity.ok(quejaService.getQuejasForCentralizadorMaster());
    }

    @PostMapping("/asignar")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> asignarQueja(@RequestBody Map<String, Object> dto, UserDto user){
        return ResponseEntity.ok(quejaService.asignarQuejaCentralizadorMaster(dto, user));
    }

    @PostMapping("/rechazar")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER')")
    public ResponseEntity<?> rechazarQueja(@RequestBody Map<String, Object> dto, UserDto user){
        return ResponseEntity.ok(quejaService.rechazarQuejaCentralizadorMaster(dto, user));
    }

    @PostMapping("/detalle")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER')")
    public ResponseEntity<?> ingresoDetalleQuejaMaster(@RequestBody Map<String, Object> dto, UserDto user){
        return ResponseEntity.ok(quejaService.ingresarDetalleQuejaCentralizadorMaster(dto, user));
    }

    @GetMapping(value = "/files/{filename}", produces = "application/pdf")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR') || hasRole('ROLE_EMPLEADO_OPERADOR')")
    public ResponseEntity<Resource> getQuejaFile(@PathVariable String filename) {

        Resource file = quejaService.getQuejaFile(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/followpoint")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR')")
    public ResponseEntity<?> getQuejasForFollowPoint(@ApiIgnore UserDto user){
        return ResponseEntity.ok(quejaService.getQuejasForFollowPoint(user));
    }

    @GetMapping("/detalle/{idQueja}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> getDetalleForQuejaById(@PathVariable String idQueja){
        return ResponseEntity.ok(quejaService.getDetalleForQuejaById(idQueja));
    }

    @PutMapping("/proceder/{idQueja}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR')")
    public ResponseEntity<?> procederQueja(@PathVariable Long idQueja, @ApiIgnore UserDto user){
        return ResponseEntity.ok(quejaService.generateFollowUp(idQueja, Long.valueOf("35"), Long.valueOf("29"), user));
    }

    @PostMapping("resolver")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR')")
    public ResponseEntity<?> resolverQuejaOperador(@RequestPart("file") @Nullable  MultipartFile file, @RequestPart("data") String data, UserDto user){
        Map<String, Object> dto = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
        return ResponseEntity.ok(quejaService.resolverQuejaOperador(dto, user, file));
    }

    @PostMapping("rechazo/operador")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR')")
    public ResponseEntity<?> rechazoQuejaOperador(@RequestPart("file") @Nullable  MultipartFile file, @RequestPart("data") String data, UserDto user){
        Map<String, Object> dto = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
        return ResponseEntity.ok(quejaService.rechazoQuejaOperador(dto, user, file));
    }

    @GetMapping("/comentario/{idQueja}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_OPERADOR') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR_MASTER') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> getComentarioByIdQueja(@PathVariable String idQueja){
        return ResponseEntity.ok(quejaService.getComentarioByIdQueja(idQueja));
    }

    @PostMapping("detalle/centralizador")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> comentarioCentralizador(@RequestPart("file") @Nullable  MultipartFile file, @RequestPart("data") String data, UserDto user){
        Map<String, Object> dto = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
        return ResponseEntity.ok(quejaService.ingresarDetalleQuejaCentralizador(dto, user, file));
    }

    @GetMapping("/followcentralizador")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> getQuejasForFollowCentralizador(@ApiIgnore UserDto user){
        return ResponseEntity.ok(quejaService.getQuejasForFollowCentralizador(user));
    }

    @PostMapping("/resolver/centralizador")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> resolverQuejaCentralizador(@RequestBody Map<String, Object> dto, UserDto user){
        return ResponseEntity.ok(quejaService.resolverQuejaCentralizador(dto, user));
    }

    @PostMapping("/rechazo/centralizador")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLEADO_CENTRALIZADOR')")
    public ResponseEntity<?> rechazoQuejaCentralizador(@RequestBody Map<String, Object> dto, UserDto user){
        return ResponseEntity.ok(quejaService.rechazarQuejaCentralizador(dto, user));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleQuejasError(RuntimeException exc) {
        return ResponseEntity.notFound().build();
    }
}

package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.service.CorreoService;
import com.example.jwttest.storage.StorageFileNotFoundException;
import com.example.jwttest.storage.StorageService;
import com.example.jwttest.utils.ApiIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Angelo
 * Date: 18/05/2023
 * Time: 00:27
 */
@RestController
@RequestMapping("prueba")
@CrossOrigin(origins = "*")
public class PruebaController {

    private final StorageService storageService;

    private final CorreoService correoService;

    @Autowired
    public PruebaController(StorageService storageService, CorreoService correoService) {
        this.storageService = storageService;
        this.correoService = correoService;
    }

    @GetMapping("hola")
    public ResponseEntity<?> prueba(@ApiIgnore UserDto user){
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        String body = "Su archivo: " + file.getOriginalFilename() + " fue subido exitosamente!";
        correoService.sendEmail("sacanoes@sat.gob.gt", "Prueba", body);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

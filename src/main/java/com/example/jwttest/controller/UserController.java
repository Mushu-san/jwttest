package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.security.JwtService;
import com.example.jwttest.service.CatalogoService;
import com.example.jwttest.service.QuejaService;
import com.example.jwttest.service.UserService;
import com.example.jwttest.utils.ApiIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Angelo
 * Date: 13/05/2023
 * Time: 16:50
 */
@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    QuejaService quejaService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getToken(@RequestBody Map<String, Object> dto){
        String username = (String)  dto.get("username");
        String password = (String) dto.get("password");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password ) );
            if(auth.isAuthenticated()){
                Map<String, String> tokenObject = new HashMap<>();
                tokenObject.put("token", jwtService.generateToken(username));
                return ResponseEntity.ok(tokenObject);
            }
            else{
                throw new UsernameNotFoundException("No se pudo autenticar el usuario");
            }
        } catch (Exception e){
            System.out.println("excepcion: " + e);
        }
        return null;
    }

    @GetMapping("userlogged")
    public ResponseEntity<UserDto> getUserLogged(@ApiIgnore UserDto user){

        return ResponseEntity.ok(user);
    }

    @PostMapping("validatepassword")
    public ResponseEntity<Boolean> validatePassword(@RequestBody Map<String, Object> dto){
        String username = (String) dto.get("username");
        Boolean isEmpleado = (Boolean) dto.get("isEmpleado");
        return ResponseEntity.ok(userService.validatePassword(username, isEmpleado));
    }

    @PostMapping("validateUsername")
    public ResponseEntity<Boolean> validateUsername(@RequestBody Map<String, Object> dto){
        String username = (String) dto.get("username");
        Boolean isEmpleado = (Boolean) dto.get("isEmpleado");
        return ResponseEntity.ok(userService.validateUsername(username, isEmpleado));
    }

    @PostMapping(value = "changepassword")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, Object> dto){
        return ResponseEntity.ok(userService.changePassword(dto));
    }

    @PostMapping(value = "signCuentaHabiente")
    public ResponseEntity<Map<String, Object>> signCuentaHabiente(@RequestBody Map<String, Object> dto){
        return ResponseEntity.ok(userService.signAccountHolder(dto));
    }

    @GetMapping("/getInfoQueja/{correlativo}")
    public ResponseEntity<?> getInfoQuejabyCorrelativo(@PathVariable String correlativo){
        return ResponseEntity.ok(quejaService.getQuejaByCorrelativo(correlativo));
    }

    @GetMapping("tipo-queja")
    public ResponseEntity<?> getTipoQueja(){
        return ResponseEntity.ok().body(catalogoService.findTipoQueja());
    }

}

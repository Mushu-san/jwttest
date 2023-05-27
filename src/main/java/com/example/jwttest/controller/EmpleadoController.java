package com.example.jwttest.controller;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.Empleado;
import com.example.jwttest.service.EmpleadoService;
import com.example.jwttest.service.UserService;
import com.example.jwttest.utils.ApiIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User: Angelo
 * Date: 15/05/2023
 * Time: 23:01
 */
@RestController
@RequestMapping("empleado")
@CrossOrigin(origins = "http://test.mydomain.com:4200")
public class EmpleadoController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("signEmployee")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_ADMIN')")
    public ResponseEntity<Map<String, Object>> signEmployee(@RequestBody Map<String, Object> dto){
        return ResponseEntity.ok(userService.signEmployee(dto));
    }

    @GetMapping("getAllEmpleadosByPuntoAndEstadoNot/{idPunto}/{idEstadoNot}")
    public ResponseEntity<?> getAllEmpleadosByPuntoAndEstadoNot(@PathVariable Long idPunto, @PathVariable Long idEstadoNot){
        return ResponseEntity.ok(empleadoService.getAllEmpleadosByPuntoAndEstadoNot(idPunto, idEstadoNot));
    }

    @PutMapping("updateEmpleado")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_EMPLEADO_ADMIN')")
    public ResponseEntity<?> updateEmpleado(@RequestBody Map<String, Object> dto, @ApiIgnore UserDto user){
        return ResponseEntity.ok(empleadoService.updateEmployee(dto, user));
    }

    @GetMapping("getEmployeeForQueja/{idPunto}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_CUENTAHABIENTE') || hasRole('ROLE_EMPLEADO_RECEPTOR')")
    public ResponseEntity<?> getEmployeeForQueja(@PathVariable Long idPunto){
        return ResponseEntity.ok(empleadoService.getEmployeeForQueja(idPunto));
    }

}

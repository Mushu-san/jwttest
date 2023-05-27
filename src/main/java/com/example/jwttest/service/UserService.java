package com.example.jwttest.service;

import com.example.jwttest.model.Cuentahabiente;
import com.example.jwttest.model.Empleado;
import com.example.jwttest.repository.CuentahabienteRepository;
import com.example.jwttest.repository.EmpleadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Angelo
 * Date: 13/05/2023
 * Time: 16:32
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    CuentahabienteRepository cuentaHabienteRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Map<String, Object> signEmployee(Map<String, Object> dto){
        Empleado saved = empleadoRepository.save(Empleado.builder()
                .dpi(longParser(dto.get("dpi")))
                .email((String) dto.get("email"))
                .idEstado(Long.parseLong("9"))
                .nombre((String) dto.get("nombre"))
                .usuario((String) dto.get("email"))
                .idPunto(longParser(dto.get("idPunto")))
                .idPerfil(longParser(dto.get("idPerfil")))
                .usuarioCreacion((String) dto.get("dpi"))
                .usuarioModifica((String) dto.get("dpi"))
                .fechaCreacion(new Date())
                .fechaModifica(new Date())
                .build());

        Map<String, Object> dao = new HashMap<>();
        dao.put("username", saved.getUsuario());
        dao.put("name", saved.getNombre());

        return dao;
    }

    public Map<String, Object> signAccountHolder(Map<String, Object> dto){
        Cuentahabiente saved = cuentaHabienteRepository.save(Cuentahabiente.builder()
                .dpi(longParser(dto.get("dpi")))
                .nombre((String) dto.get("nombre"))
                .apellido((String) dto.get("apellido"))
                .contacto((String) dto.get("contacto"))
                .correo((String) dto.get("correo"))
                .idEstado(Long.parseLong("4"))
                .pass(passwordEncoder.encode((String) dto.get("password")))
                .usuarioCreacion((String) dto.get("dpi"))
                .usuarioModifica((String) dto.get("dpi"))
                .fechaCreacion(new Date())
                .fechaModifica(new Date())
                .build());

        Map<String, Object> dao = new HashMap<>();
        dao.put("username", saved.getCorreo());
        dao.put("name", saved.getNombre());

        return dao;
    }

    public Boolean validateUsername(String username, Boolean isEmpleado){
        AtomicReference<Boolean> isValid = new AtomicReference<>(false);
        if(isEmpleado){
            Optional<Empleado> empleado = empleadoRepository.findByUsuario(username);
            empleado.ifPresent(value -> isValid.set(true));
        }
        else{
            Optional<Cuentahabiente> cuentahabiente = cuentaHabienteRepository.findByCorreo(username);
            cuentahabiente.ifPresent(value -> isValid.set(true));
        }
        return isValid.get();
    }

    public Boolean validatePassword(String username, Boolean isEmpleado){
        AtomicReference<Boolean> isValid = new AtomicReference<>(false);
        if(isEmpleado){
            Optional<Empleado> empleado = empleadoRepository.findByUsuarioAndPassNotNull(username);
            empleado.ifPresent(value ->

                    isValid.set(!value.getPass().equals("")));
        }
        else{
            Optional<Cuentahabiente> cuentahabiente = cuentaHabienteRepository.findByCorreoAndPassNotNull(username);
            cuentahabiente.ifPresent(value -> isValid.set(!value.getPass().equals("")));
        }
        return isValid.get();
    }

    public Map<String, String> changePassword(Map<String, Object> dto){
        Boolean isEmpleado = (Boolean) dto.get("isEmpleado");
        Map<String, String> dao = new HashMap<>();
        if(isEmpleado){
            Optional<Empleado> empleado = empleadoRepository.findByUsuario((String) dto.get("username"));
            empleado.ifPresent(value -> {
                value.setPass(passwordEncoder.encode((String) dto.get("password")));
                value.setFechaModifica(new Date());
                value.setUsuarioModifica(String.valueOf(value.getDpi()));
                empleadoRepository.save(value);
            });
            dao.put("message", "Contraseña cambiada con éxito");
        }
        else{
            Optional<Cuentahabiente> cuentahabiente = cuentaHabienteRepository.findByCorreo((String) dto.get("username"));
            cuentahabiente.ifPresent(value -> {
                value.setPass(passwordEncoder.encode((String) dto.get("password")));
                value.setFechaModifica(new Date());
                value.setUsuarioModifica(String.valueOf(value.getDpi()));
                cuentaHabienteRepository.save(value);
            });
            dao.put("message", "Contraseña cambiada con éxito");
        }

        return dao;
    }



    private Long longParser(Object k){
        String object = (String) k;
       return Long.valueOf(object);
    }

}

package com.example.jwttest.security;

import com.example.jwttest.model.Cuentahabiente;
import com.example.jwttest.model.Empleado;
import com.example.jwttest.model.Rol;
import com.example.jwttest.model.UserInfo;
import com.example.jwttest.repository.CuentahabienteRepository;
import com.example.jwttest.repository.EmpleadoRepository;
import com.example.jwttest.repository.RolRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * User: Angelo
 * Date: 13/05/2023
 * Time: 12:12
 */
@Component
@Slf4j
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    EmpleadoRepository repository;

    @Autowired
    CuentahabienteRepository cuentahabienteRepository;

    @Autowired
    RolRepository rolRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> userAndType = List.of(username.split(","));
        String login = userAndType.get(0);
        String type = new String(Base64.getDecoder().decode(userAndType.get(1)));
        boolean isEmpleado = type.equals("empleado");
        boolean isCuentahabiente = type.equals("cuentahabiente");

        Optional<UserInfo> userInfo = isEmpleado ?
                repository.findByUsuario(login).map(Empleado::getUserInfo) :
                isCuentahabiente ?
                cuentahabienteRepository.findByCorreo(login).map(Cuentahabiente::getUserInfo)
                : Optional.empty();


        userInfo.ifPresent(info -> {
            info.setRoles(
                    this.rolRepository.findById(Long.parseLong(info.getRoles())).get().getNombre()
            );
            info.setName(info.getName().concat(",").concat(isEmpleado ? "ZW1wbGVhZG8=" : isCuentahabiente ? "Y3VlbnRhaGFiaWVudGU=" : ""));
        });

        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }

}

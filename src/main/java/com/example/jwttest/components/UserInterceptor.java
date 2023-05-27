package com.example.jwttest.components;


import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.Cuentahabiente;
import com.example.jwttest.model.Empleado;
import com.example.jwttest.repository.CuentahabienteRepository;
import com.example.jwttest.repository.EmpleadoRepository;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 11:48
 */
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private CuentahabienteRepository cuentahabienteRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("entra al prehandle");
        UserDto user = this.getAuthenticatedUser();
        request.setAttribute("user_attribute", user);

        return true;
    }

    private UserDto getAuthenticatedUser() {
        System.out.println("entra al get ");

        final UserDto user = new UserDto();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            String username= Arrays.stream(authentication.getName().split(",")).findFirst().orElse("");

            if(authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_EMPLEADO_ADMIN") ||
                    authorities.contains("ROLE_EMPLEADO_TITULAR") || authorities.contains("ROLE_EMPLEADO_SUPLENTE")
                    || authorities.contains("ROLE_EMPLEADO_JEFEIN") || authorities.contains("ROLE_EMPLEADO_ENCARGADO")
                    || authorities.contains("ROLE_EMPLEADO_RECEPTOR") || authorities.contains("ROLE_EMPLEADO_CENTRALIZADOR") || authorities.contains("ROLE_EMPLEADO_CENTRALIZADOR_MASTER") ||
                    authorities.contains("ROLE_EMPLEADO_OPERADOR")
            ){
                Empleado employee = this.empleadoRepository.findByUsuario(username).orElse(new Empleado());
                user.setUsername(authentication.getName());
                user.setDpi(String.valueOf(employee.getDpi()));
                user.setNombre(employee.getNombre());
                user.setEmail(employee.getEmail());
                user.setIsEmpleado(true);
                user.setRoles(authorities);
            }

            if(authorities.contains("ROLE_CUENTAHABIENTE")){
                Cuentahabiente cuentahabiente = this.cuentahabienteRepository.findByCorreo(username).orElse(new Cuentahabiente());
                user.setUsername(authentication.getName());
                user.setDpi(String.valueOf(cuentahabiente.getDpi()));
                user.setNombre(cuentahabiente.getNombre());
                user.setEmail(cuentahabiente.getCorreo());
                user.setIsEmpleado(false);
                user.setRoles(authorities);
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            final String message = e instanceof IllegalArgumentException ? e.getMessage() : "Not authenticated";
            throw new IllegalArgumentException(message);
        }

        /* if (user.getLogin().equals(Constants.USER_UNKNOWN)) {
            throw BusinessException.unauthorized(Message.UNKNOWN_USER.getText());
        } */

        return user;
    }

}

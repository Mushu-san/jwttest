package com.example.jwttest.dto;

import lombok.*;

import java.util.List;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 11:36
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private String username;
    private String email;
    private String nombre;
    private String dpi;
    private Boolean isEmpleado;
    private List<String> roles;

}

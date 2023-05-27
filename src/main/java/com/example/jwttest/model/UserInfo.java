package com.example.jwttest.model;

import lombok.*;

import java.io.Serializable;

/**
 * User: Angelo
 * Date: 13/05/2023
 * Time: 12:02
 */
@Data
@Getter
@Setter
@Builder
public class UserInfo implements Serializable {
    private String name;
    private String email;
    private String password;
    private String roles;

    public UserInfo(String name, String email, String password, String roles){
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserInfo() {
        
    }
}

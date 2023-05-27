package com.example.jwttest;

import com.example.jwttest.components.UserInterceptor;
import com.example.jwttest.components.UserLocator;
import com.example.jwttest.security.JwtAuthFilter;
import com.example.jwttest.security.UserInfoUserDetailsService;
import com.example.jwttest.storage.FileSystemStorageService;
import com.example.jwttest.storage.StorageProperties;
import com.example.jwttest.storage.StorageService;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * User: Angelo
 * Date: 6/05/2023
 * Time: 08:45
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    //authentication
    public UserDetailsService userDetailsService() {
        UserDetailsService detail = new UserInfoUserDetailsService();
        return detail;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("user/**").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/empleado/**", "/prueba/**",
                        "/punto-acceso/**", "/catalogo/**", "/rol/**", "/queja/**")
                .authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserInterceptor userInterceptor(){
        return new UserInterceptor();
    }

    @Bean
    public UserLocator userLocator(){
        return new UserLocator();
    }

    @Bean
    public StorageService storageService(StorageProperties properties){
        return new FileSystemStorageService(properties);
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                // Agrega el resolutor personalizado a la lista de resolutores
                resolvers.add(userLocator());
            }

            @Override
            public void addInterceptors(InterceptorRegistry interceptorRegistry){
                interceptorRegistry.addInterceptor(userInterceptor());
            }

        };
    }
}

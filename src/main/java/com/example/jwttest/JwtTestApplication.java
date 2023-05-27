package com.example.jwttest;

import com.example.jwttest.storage.StorageProperties;
import com.example.jwttest.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableAspectJAutoProxy
public class JwtTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtTestApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

}

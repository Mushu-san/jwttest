package com.example.jwttest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * User: Angelo
 * Date: 22/05/2023
 * Time: 13:04
 */
@Service
@Slf4j
public class CorreoService {

    @Autowired
    private JavaMailSender correoSender;

    public void sendEmail(String to, String subject, String body){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sergiocan130@hotmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            correoSender.send(message);
        } catch (Exception e){
            log.error("Error al enviar correo", e);
        }
    }

}

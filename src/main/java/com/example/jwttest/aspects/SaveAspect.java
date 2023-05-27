package com.example.jwttest.aspects;

import com.example.jwttest.service.HistorialOperacionesService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Angelo
 * Date: 26/05/2023
 * Time: 19:49
 */
@Aspect
@Component
public class SaveAspect {

    @Autowired
    private HistorialOperacionesService historialOperacionesService;

    @AfterReturning(pointcut = "execution(* com.example.jwttest.repository.*.save(..))", returning = "result")
    public void afterSave(JoinPoint joinPoint, Object result) {
        historialOperacionesService.createOperationHistory(result, "1");
    }

}

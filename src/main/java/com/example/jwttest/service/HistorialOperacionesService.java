package com.example.jwttest.service;

import com.example.jwttest.model.HistorialOperaciones;
import com.example.jwttest.aspects.HistorialOperacionesRepository;
import com.google.gson.Gson;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.capitalize;

/**
 * User: Angelo
 * Date: 26/05/2023
 * Time: 19:52
 */
@Service
@Slf4j
public class HistorialOperacionesService {

    @Autowired
    private HistorialOperacionesRepository repository;

    @Transactional(rollbackFor = Exception.class)
    public <T> void createOperationHistory(T entity, String type) {
        final List<String> ids = new ArrayList<>();
        final Table table = entity.getClass().getAnnotation(Table.class);
        for (Method method : entity.getClass().getMethods()) {
            Id id = method.getAnnotation(Id.class);
            if (id != null) {
                try {
                    ids.add(String.valueOf(method.invoke(entity)));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
        if (ids.isEmpty()) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                Id id = field.getAnnotation(Id.class);
                if (id != null) {
                    try {
                        Method method = entity.getClass().getDeclaredMethod("get" + capitalize(field.getName()));
                        ids.add(String.valueOf(method.invoke(entity)));
                    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                        log.error(ex.getMessage());
                        throw new IllegalStateException("Unable to find getter for field " + field.getName());
                    }
                }
            }
        }
        if (table == null) {
            throw new IllegalStateException("No javax.persistence.Table annotation found in entity");
        }
        final String id = String.join(",", ids);

        repository.save(
                HistorialOperaciones.builder()
                        .idRegistro(id)
                        .fechaModifica(new Date().toString())
                        .registro(new Gson().toJson(entity))
                        .usuarioModifica("System")
                        .tablaOperacion(table.name())
                        .idTipoOperacion(Long.parseLong(type))
                        .build()
        );
    }


}

package com.example.jwttest.service;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.CatalogoDato;
import com.example.jwttest.model.PuntoAcceso;
import com.example.jwttest.repository.CatalogoDatoRepository;
import com.example.jwttest.repository.PuntoAccesoRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 19:00
 */
@Service
@Slf4j
public class PuntoAccesoService {
    @Autowired
    PuntoAccesoRepository puntoAccesoRepository;

    @Autowired
    CatalogoDatoRepository catalogoDatoRepository;

    @Transactional
    public Object savePuntoAcceso(Map<String, Object> dto, UserDto user) {
        System.out.println("dto: " + new Gson().toJson(dto.values()));
        PuntoAcceso save = puntoAccesoRepository.save(
                PuntoAcceso.builder()
                        .nombre((String) dto.get("nombre"))
                        .descripcion((String) dto.get("descripcion"))
                        .idEstado(Long.parseLong("3"))
                        .idRegion(longParser(dto.get("region")))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        Map<String, Object> dao = new HashMap<>();
        dao.put("nombre", save.getNombre());
        dao.put("descripcion", save.getDescripcion());
        dao.put("region", catalogoDatoRepository.findById(save.getIdRegion()).map(CatalogoDato::getDescripcion).orElse("No existe"));

        return dao;
    }

    public Object updatePuntoAcceso(Map<String, Object> dto, UserDto user) {
        System.out.println("dto: " + new Gson().toJson(dto.values()));
        Optional<PuntoAcceso> puntoAcceso = puntoAccesoRepository.findById(longParser(dto.get("idPuntoAcceso")));
        Map<String, Object> dao = new HashMap<>();
        puntoAcceso.ifPresent(k -> {
            k.setNombre((String) dto.get("nombre"));
            k.setDescripcion((String) dto.get("descripcion"));
            k.setIdEstado(longParser(dto.get("idEstado")));
            k.setFechaModifica(new Date());
            k.setUsuarioModifica(user.getDpi());
            this.puntoAccesoRepository.save(k);
            dao.put("nombre", k.getNombre());
            dao.put("descripcion", k.getDescripcion());
        });
        return dao;
    }

    public List<PuntoAcceso> getAllPuntosAcceso() {
        return puntoAccesoRepository.findAll();
    }

    public List<PuntoAcceso> getPuntosAccesoByRegion(Long idRegion, Long idEstado) {
        return puntoAccesoRepository.findByIdRegionAndIdEstado(idRegion, idEstado);
    }

    public Object getPuntosForQueja(){
        return puntoAccesoRepository.findByIdEstado(longParser("3"));
    }

    private Long longParser(Object k) {
        String object = (String) k;
        return Long.valueOf(object);
    }
}

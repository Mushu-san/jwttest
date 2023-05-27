package com.example.jwttest.service;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.CatalogoDato;
import com.example.jwttest.repository.CatalogoDatoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.catalog.Catalog;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Angelo
 * Date: 18/05/2023
 * Time: 22:16
 */
@Service
@Slf4j
public class CatalogoService {

    @Autowired
    CatalogoDatoRepository catalogoDatoRepository;

    public List<CatalogoDato> findByCatalogoAndEstado(Long idCatalogo, Long idEstado){
        System.out.println("idCatalogo: " + idCatalogo);
        System.out.println("idEstado: " + idEstado);
        return catalogoDatoRepository.findByIdCatalogoAndIdEstado(idCatalogo, idEstado);
    }

    public Object findTipoQueja(){
        return catalogoDatoRepository.findByIdCatalogo(Long.parseLong("6")).stream().map(k -> {
            Map<String, Object> dao = new HashMap<>();
            dao.put("idTipoQueja", k.getId());
            dao.put("nombre", k.getNombre());
            dao.put("descripcion", k.getDescripcion());
            dao.put("estado", k.getIdEstado().equals(Long.parseLong("1")) ? "Activo" : "Inactivo");
            dao.put("idEstado", k.getIdEstado());
            return dao;
        }).collect(Collectors.toList());
    }

    public Object saveTipoQueja(Map<String, Object> dto, UserDto user){
       CatalogoDato saved = this.catalogoDatoRepository.save(CatalogoDato.builder()
                .nombre((String) dto.get("nombre"))
                .descripcion((String) dto.get("descripcion"))
                .idCatalogo(Long.parseLong("6"))
                .idEstado(Long.parseLong("1"))
                .usuarioCreacion(user.getDpi())
                .usuarioModifica(user.getDpi())
                .fechaCreacion(new Date())
                .fechaModifica(new Date())
                .build());
       Map<String, Object> dao = new HashMap<>();
         dao.put("siglas", saved.getNombre());
         dao.put("descripcion", saved.getDescripcion());
        return dao;
    }

    public Object updateTipoQueja(Map<String, Object> dto, UserDto user){
        System.out.println("dto: " + dto);
        Optional<CatalogoDato> catalogoDato = catalogoDatoRepository.findById(longParser(dto.get("idTipoQueja")));
        Map<String, Object> dao = new HashMap<>();

        catalogoDato.ifPresent(k -> {
            k.setDescripcion((String) dto.get("descripcion"));
            k.setIdEstado(longParser(dto.get("idEstado")));
            k.setUsuarioModifica(user.getDpi());
            k.setFechaModifica(new Date());
            catalogoDatoRepository.save(k);

            dao.put("siglas", k.getNombre());
            dao.put("descripcion", k.getDescripcion());
        });

        return dao;
    }

    private Long longParser(Object k){
        String object = (String) k;
        return Long.valueOf(object);
    }
}

package com.example.jwttest.service;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.Empleado;
import com.example.jwttest.repository.EmpleadoRepository;
import com.example.jwttest.repository.RolRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Angelo
 * Date: 19/05/2023
 * Time: 20:09
 */
@Service
@Slf4j
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    RolRepository rolRepository;

    @Transactional()
    public List<Map<String, Object>> getAllEmpleadosByPuntoAndEstadoNot(Long idPunto, Long idEstadoNot) {
        List<Map<String, Object>> list = empleadoRepository.findByIdPuntoAndIdEstadoNot(idPunto, idEstadoNot).stream().map(k -> {
            Map<String, Object> dao = new HashMap<>();
            dao.put("dpi", k.getDpi());
            dao.put("correo", k.getEmail());
            dao.put("nombre", k.getNombre());
            dao.put("cargo", rolRepository.findById(k.getIdPerfil()).get().getDescripcion());
            dao.put("idCargo", k.getIdPerfil());
            dao.put("idEstado", k.getIdEstado());
            return dao;
        }).collect(Collectors.toList());

        if(list.isEmpty()){
            Map<String, Object> dao = new HashMap<>();
            dao.put("dpi", "");
            dao.put("correo", "");
            dao.put("nombre", "");
            dao.put("cargo", "");
            dao.put("idCargo", "");
            list.add(dao);
        }

        return list;
    }

    public Map<String, Object> updateEmployee(Map<String, Object> dto, UserDto user){
        Optional<Empleado> empleado = empleadoRepository.findById(longParser(dto.get("dpi")));
        Map<String, Object> dao = new HashMap<>();
        empleado.ifPresent(k -> {
            k.setNombre((String) dto.get("nombre"));
            k.setIdPerfil(longParser(dto.get("idPerfil")));
            k.setEmail((String) dto.get("email"));
            k.setUsuario((String) dto.get("email"));
            k.setIdEstado(longParser(dto.get("idEstado")));
            k.setUsuarioModifica(user.getDpi());
            k.setFechaModifica(new Date());
            dao.put("username", k.getUsuario());
            dao.put("name", k.getNombre());
            empleadoRepository.save(k);
        });

        return dao;
    }

    public Object getEmployeeForQueja(Long idPunto){
        return empleadoRepository.findByIdPuntoAndIdEstadoAndIdPerfilNotIn(idPunto, longParser("9"),
                longParser(Arrays.asList("1", "2", "9"))).stream().map(k -> {
            Map<String, Object> dao = new HashMap<>();
            dao.put("dpi", k.getDpi());
            dao.put("nombre", k.getNombre());
            return dao;
        }).collect(Collectors.toList());
    }

    private Long longParser(Object k){
        String object = (String) k;
        return Long.valueOf(object);
    }

    private List<Long> longParser(List<Object> k){
        return k.stream().map(f-> Long.valueOf((String) f)).collect(Collectors.toList());
    }
}

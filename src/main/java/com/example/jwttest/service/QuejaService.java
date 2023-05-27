package com.example.jwttest.service;

import com.example.jwttest.dto.UserDto;
import com.example.jwttest.model.*;
import com.example.jwttest.projection.EmpleadosForEmailProjection;
import com.example.jwttest.projection.QuejasCentralizadorProjection;
import com.example.jwttest.repository.*;
import com.example.jwttest.storage.StorageService;
import com.example.jwttest.utils.FileUtils;
import com.google.gson.Gson;
import jakarta.annotation.Nullable;
import jakarta.mail.Multipart;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Angelo
 * Date: 23/05/2023
 * Time: 10:12
 */
@Service
@Slf4j
public class QuejaService {

    @Autowired
    QuejaRepository quejaRepository;

    @Autowired
    DetalleQuejaRepository detalleQuejaRepository;

    @Autowired
    QuejaTrazabilidadRepository quejaTrazabilidadRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    PuntoAccesoRepository puntoAccesoRepository;

    @Autowired
    CorreoService correoService;

    @Autowired
    StorageService storageService;

    @Transactional(rollbackFor = {Exception.class})
    public Object createQueja(Map<String, Object> dto, UserDto user, MultipartFile file) throws IOException {

        Boolean hasAttended = (String) dto.get("idEmpleadoAtendio") != null;

        Boolean isNotAName = false;

        String empleadoString = "";

        Empleado empleado = new Empleado();

        Empleado centralizador = empleadoRepository.findCentralizadorMaster().orElseThrow(
                () -> new IOException("Centralizador Master no encontrado"));

        if (!user.getRoles().contains("ROLE_CUENTAHABIENTE")) {
            empleado = empleadoRepository.findById(longParser(user.getDpi())).orElseThrow(
                    () -> new IOException("Empleado no encontrado"));

            centralizador = empleadoRepository.findCentralizadorByPunto(empleado.getIdPunto()).orElseThrow(
                    () -> new IOException("Centralizador no encontrado"));
        }

        System.out.println("llega aca");
        if (hasAttended) {
            empleadoString = (String) dto.get("idEmpleadoAtendio");

            if (empleadoString.matches("-?\\d+(\\.\\d+)?")) {
                isNotAName = true;

                empleado = empleadoRepository.findById(longParser(dto.get("idEmpleadoAtendio"))).orElseThrow(
                        () -> new RuntimeException("Empleado no encontrado")
                );
                System.out.println("pasa el if");
                centralizador = empleadoRepository.findCentralizadorByPunto(empleado.getIdPunto()).orElseThrow(
                        () -> new RuntimeException("Centralizador no encontrado"));
                System.out.println("truena por aca");
            }
        }

        System.out.println("se ingresa la queja");
        Map<String, Object> map = dto;
        Queja build = Queja.builder()
                .idMedioIngreso(longParser(user.getRoles().contains("ROLE_CUENTAHABIENTE") ? "25" : (String) map.get("idMedioIngreso")))
                .nombre((String) map.get("nombre"))
                .email((String) map.get("email"))
                .telefono((String) map.get("telefono"))
                .idTipoQueja(longParser("11"))
                .idEmpleadoAtendio(hasAttended && isNotAName ? empleado.getDpi().toString() : empleadoString)
                .idTipoIngreso(longParser(user.getRoles().contains("ROLE_CUENTAHABIENTE") ? "26" : "27"))
                .oficina((String) map.get("idPuntoAcceso"))
                .descripcion((String) map.get("descripcion"))
                .idEstado(longParser("21"))
                .usuarioCreacion(user.getDpi())
                .usuarioModifica(user.getDpi())
                .fechaCreacion(new Date())
                .fechaModifica(new Date())
                .build();
        Queja queja = quejaRepository.save(build);

        String qms = quejaRepository.getCorrelativeQuery();

        System.out.println("va por el archivo");

        if (file != null) {
            if (file.isEmpty()) {
                throw new RuntimeException("Archivo vacio");
            }
            System.out.println("entra a guardar el archivo");
            queja.setFilename(qms.concat(FileUtils.getExtension(file.getOriginalFilename())));
            storageService.store(FileUtils.renameTo(file, qms));
        }

        System.out.println("se ingresa la trazabilidad de la queja");

        quejaTrazabilidadRepository.save(
                QuejaTrazabilidad.builder()
                        .idQueja(queja.getCorrelativo())
                        .idEtapa(longParser("22"))
                        .horaIngreso(new Date())
                        .idEstado(longParser("23"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        System.out.println("se crea el dao");
        Map<String, Object> dao = new HashMap<>();

        String bodyMessage = user.getRoles().contains("ROLE_CUENTAHABIENTE") ? "Señor cuentahabiente,  agradecemos su comunicación, " +
                " le informamos que su queja ha sido recibida exitosamente. " +
                "Para el seguimiento o cualquier consulta relacionada, no olvide que el número de su queja es: " + qms + "."
                : "La queja  " + qms + ", ha sido ingresada exitosamente al sistema de control de quejas";
        dao.put("message", bodyMessage);

        System.out.println("se ingresan los respectivos correos");
        correoService.sendEmail(queja.getEmail(), "Queja ingresada",
                "Señor cuentahabiente,  agradecemos su comunicación,  le informamos que su queja ha sido recibida exitosamente. " +
                        "Para el seguimiento o cualquier consulta relacionada, no olvide que el número de su queja es " + qms);
        correoService.sendEmail(centralizador.getEmail(), "Nueva Queja Ingresada",
                "El sistema de quejas le informa que se ha recibido una queja, la cual debe ser asignada dentro de las próximas 24 horas");
        System.out.println("finaliza");
        return dao;

    }

    public Object getQuejasForCentralizadorMaster() {
        return quejaRepository.getQuejasForCentralizadorMaster();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object asignarQuejaCentralizadorMaster(Map<String, Object> dto, UserDto user) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        String correlativo = quejaRepository.getCorrelativoQuejaById(queja.getCorrelativo())
                .map(QuejasCentralizadorProjection::getCorrelativoVisual).orElse("");
        ;

        queja.setIdPuntoAsignado(longParser(dto.get("idPunto")));
        queja.setIdEstado(longParser("28"));
        queja.setFechaModifica(new Date());
        queja.setUsuarioModifica(user.getDpi());

        quejaTrazabilidadRepository.save(
                QuejaTrazabilidad.builder()
                        .idQueja(queja.getCorrelativo())
                        .idEtapa(longParser("29"))
                        .idEtapaAnterior(longParser("22"))
                        .horaIngreso(new Date())
                        .idEstado(longParser("23"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        detalleQuejaRepository.save(
                DetalleQueja.builder()
                        .comentario("Trasladada al Administrador del punto de atención correspondiente para su análisis.")
                        .idQueja(queja.getCorrelativo())
                        .idEstado(longParser("30"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        List<EmpleadosForEmailProjection> empleados =  quejaRepository.getEmpleadosForAnounceQuejaByPunto(longParser(dto.get("idPunto")));

        if(!empleados.isEmpty()) {
            empleados.stream().forEach(empleado -> {
                correoService.sendEmail(empleado.getCorreo(), "Asignación de Queja",
                        "Estimado(a) " + empleado.getNombre() + " \n" +
                                "\n" +
                                "El sistema para control de quejas por mal servicio o servicio no conforme le informa que se le asignó la queja No." + correlativo + " ”\n" +
                                "Para su atención tiene un plazo máximo de 5 días hábiles, según normativa vigente.\n");
            });
        }

        correoService.sendEmail(queja.getEmail(), "Seguimiendo de la Queja",
                "Señor(a) Cuentahabiente, " +
                        "su queja ha sido trasladada al administrador del punto de atención correspondiente para su análisis");

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "Queja fue asignada con exito");

        return dao;
    }

    public Object rechazarQuejaCentralizadorMaster(Map<String, Object> dto, UserDto user) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        String comentario = (String) dto.get("comentario");

        queja.setIdEstado(longParser("33"));
        queja.setFechaModifica(new Date());
        queja.setUsuarioModifica(user.getDpi());

        quejaTrazabilidadRepository.save(
                QuejaTrazabilidad.builder()
                        .idQueja(queja.getCorrelativo())
                        .idEtapa(longParser("32"))
                        .idEtapaAnterior(longParser("22"))
                        .horaIngreso(new Date())
                        .idEstado(longParser("23"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        detalleQuejaRepository.save(
                DetalleQueja.builder()
                        .comentario(comentario)
                        .idQueja(queja.getCorrelativo())
                        .idEstado(longParser("31"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        correoService.sendEmail(queja.getEmail(), "Seguimiendo de la Queja",
                "Señor(a) Cuentahabiente, la atención a su queja no procede, por el siguiente motivo: " +
                        comentario);


        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "Queja fue rechazada con exito");

        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object ingresarDetalleQuejaCentralizador(Map<String, Object> dto, UserDto user, MultipartFile file) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        String correlativo = this.quejaRepository.getCorrelativoQuejaByIdNotRestriction(longParser(dto.get("idQueja")))
                .map(QuejasCentralizadorProjection::getCorrelativoVisual).orElse("");

        queja.setFechaModifica(new Date());
        queja.setUsuarioModifica(user.getDpi());

        if (file != null) {

            MultipartFile fileSave = null;
            try {
                fileSave = FileUtils.renameTo(file, "comentario_" + correlativo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.storageService.store(fileSave);
        }

        detalleQuejaRepository.save(
                DetalleQueja.builder()
                        .comentario((String) dto.get("comentario"))
                        .idQueja(queja.getCorrelativo())
                        .idEstado(longParser("34"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "Comentario guardado con exito");

        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object ingresarDetalleQuejaCentralizadorMaster(Map<String, Object> dto, UserDto user) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        queja.setFechaModifica(new Date());
        queja.setUsuarioModifica(user.getDpi());



        detalleQuejaRepository.save(
                DetalleQueja.builder()
                        .comentario((String) dto.get("comentario"))
                        .idQueja(queja.getCorrelativo())
                        .idEstado(longParser("34"))
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "Comentario guardado con exito");

        return dao;
    }

    public Resource getQuejaFile(String idQueja) { return storageService.loadAsResource(idQueja);
    }

    public Object getQuejasForFollowPoint(UserDto user) {
        return quejaRepository.getQuejasForFollowPoint(longParser(user.getDpi())).stream().filter(queja -> ( queja.getIdEtapa().equals("38") || queja.getIdEtapa().equals("29") ||
                (queja.getIdEtapa().equals("35") && queja.getIdEmpleado().equals(user.getDpi()))
        )).collect(Collectors.toList());
    }

    public Object getQuejasForFollowCentralizador(UserDto user){
        return quejaRepository.getQuejasForFollowCentralizador(longParser(user.getDpi()));
    }

    public Object getDetalleForQuejaById(String idQueja) {
        return quejaRepository.getDetalleForQuejaById(longParser(idQueja));
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object generateFollowUp(Long idQueja, Long idEtapa, Long idEtapaAnterior, UserDto user) {
        Queja queja = quejaRepository.findById(idQueja).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        Long idEmpleadoAsignacion = idEtapa == longParser("35") ? longParser(user.getDpi()) :
                longParser(quejaRepository.getCentralizadorByAsignacionQueja(queja.getCorrelativo()));

        quejaTrazabilidadRepository.save(
                QuejaTrazabilidad.builder()
                        .idQueja(queja.getCorrelativo())
                        .idEtapa(idEtapa)
                        .idEtapaAnterior(idEtapaAnterior)
                        .horaIngreso(new Date())
                        .idEstado(longParser("23"))
                        .idEmpleadoAsignacion(idEmpleadoAsignacion)
                        .usuarioCreacion(user.getDpi())
                        .usuarioModifica(user.getDpi())
                        .fechaCreacion(new Date())
                        .fechaModifica(new Date())
                        .build()
        );

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "La etapa fue cambiada con exito");
        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object resolverQuejaOperador(Map<String, Object> map, UserDto user, MultipartFile file) {
        Map<String, Object> dto = map;
        String correlativo = this.quejaRepository.getCorrelativoQuejaByIdNotRestriction(longParser(dto.get("idQueja")))
                .map(QuejasCentralizadorProjection::getCorrelativoVisual).orElse("");
        this.ingresarDetalleQuejaCentralizadorMaster(dto, user);
        this.generateFollowUp(longParser(dto.get("idQueja")), longParser("36"), longParser("35"), user);
        if (file != null) {

            MultipartFile fileSave = null;
            try {
                fileSave = FileUtils.renameTo(file, "qt_" + correlativo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.storageService.store(fileSave);
        }

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "La queja fue resuelta con exito");
        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object rechazoQuejaOperador(Map<String, Object> map, UserDto user, MultipartFile file) {
        Map<String, Object> dto = map;
        String correlativo = this.quejaRepository.getCorrelativoQuejaByIdNotRestriction(longParser(dto.get("idQueja")))
                .map(QuejasCentralizadorProjection::getCorrelativoVisual).orElse("");

        this.ingresarDetalleQuejaCentralizadorMaster(dto, user);
        this.generateFollowUp(longParser(dto.get("idQueja")), longParser("37"), longParser("29"), user);

        if (file != null) {

            MultipartFile fileSave = null;
            try {
                fileSave = FileUtils.renameTo(file, "qt_" + correlativo +"_rechazo");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.storageService.store(fileSave);
        }

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "La queja fue rechazada exitosamente");
        return dao;
    }

    public Object getComentarioByIdQueja(String idQueja) {
        Map<String, Object> dao = new HashMap<>();
        dao.put("comentario", quejaRepository.getComentarioByIdQueja(longParser(idQueja)));
        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object resolverQuejaCentralizador(Map<String, Object> dto, UserDto user) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        String comentario = (String) dto.get("comentario");

        queja.setUsuarioModifica(user.getDpi());
        queja.setFechaModifica(new Date());
        queja.setIdEstado(longParser("33"));

        this.ingresarDetalleQuejaCentralizadorMaster(dto, user);
        this.generateFollowUp(longParser(dto.get("idQueja")), longParser("39"), longParser(dto.get("idEtapa")), user);

        correoService.sendEmail(queja.getEmail(), "Queja resuelta",
                "Su queja fue resuelta con el siguiente resultado: " + comentario);

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "La queja fue resuelta con exito");
        return dao;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Object rechazarQuejaCentralizador(Map<String, Object> dto, UserDto user) {
        Queja queja = quejaRepository.findById(longParser(dto.get("idQueja"))).orElseThrow(
                () -> new RuntimeException("Queja no encontrada")
        );

        String correlativo = quejaRepository.getCorrelativoQuejaByIdNotRestriction(queja.getCorrelativo())
                .map(QuejasCentralizadorProjection::getCorrelativoVisual).orElse("");

        this.generateFollowUp(longParser(dto.get("idQueja")), longParser("38"), longParser(dto.get("idEtapa")), user);
        this.ingresarDetalleQuejaCentralizadorMaster(dto, user);

        List<EmpleadosForEmailProjection> empleados = quejaRepository.getEmpleadosForAnounceQuejaByPunto(queja.getIdPuntoAsignado());

        if (!empleados.isEmpty()) {
            empleados.stream().forEach(empleado -> {
                correoService.sendEmail(empleado.getCorreo(), "Asignación de Queja",
                        "Estimado(a) " + empleado.getNombre() + " \n" +
                                "\n" +
                                "El sistema para control de quejas por mal servicio o servicio no conforme le informa que se le asignó la queja No." + correlativo + " ”\n" +
                                "Para su atención tiene un plazo máximo de 5 días hábiles, según normativa vigente.\n");
            });
        }

        Map<String, Object> dao = new HashMap<>();
        dao.put("message", "La queja fue resuelta con exito");
        return dao;
    }


    public Object getQuejaByCorrelativo(String correlativo){
        return quejaRepository.getInfoQuejaByCorrelativo(correlativo);
    }

    private Long longParser(Object k) {
        String object = (String) k;
        return Long.valueOf(object);
    }

}

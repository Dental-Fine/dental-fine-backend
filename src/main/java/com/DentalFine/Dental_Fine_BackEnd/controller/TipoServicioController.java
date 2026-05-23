package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarServicioRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.ServicioDTO;
import com.DentalFine.Dental_Fine_BackEnd.service.TipoServiciosService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TipoServicioController {

    private final TipoServiciosService tipoServiciosService;

    public TipoServicioController(TipoServiciosService tipoServiciosService) {
        this.tipoServiciosService = tipoServiciosService;
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioDTO>> obtenerTodos() {
        return ResponseEntity.ok(tipoServiciosService.obtenerTodos());
    }

    @GetMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoServiciosService.obtenerPorId(id));
    }

    @PostMapping("/servicios")
    public ResponseEntity<ServicioDTO> registrar(@RequestBody RegistrarServicioRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoServiciosService.crearServicio(datos));
    }

    @PutMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id,
            @RequestBody RegistrarServicioRequest datos) {
        return ResponseEntity.ok(tipoServiciosService.actualizarServicio(id, datos));
    }

    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        tipoServiciosService.eliminarServicioLogico(id);
        return ResponseEntity.noContent().build();
    }
}

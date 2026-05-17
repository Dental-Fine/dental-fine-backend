package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.ServicioDTO;
import com.DentalFine.Dental_Fine_BackEnd.service.TipoServiciosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}

package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.DentistaDTO;
import com.DentalFine.Dental_Fine_BackEnd.service.DentistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DentistaController {

    private final DentistaService dentistaService;

    public DentistaController(DentistaService dentistaService) {
        this.dentistaService = dentistaService;
    }

    @GetMapping("/dentistas")
    public ResponseEntity<List<DentistaDTO>> obtenerTodos() {
        return ResponseEntity.ok(dentistaService.obtenerTodos());
    }

    @GetMapping("/dentistas/{id}")
    public ResponseEntity<DentistaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(dentistaService.obtenerPorId(id));
    }
}

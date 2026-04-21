package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteBusquedaResponse;
import com.DentalFine.Dental_Fine_BackEnd.service.PacienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    /**
     * Contrato: GET /api/pacientes/buscar → GET /pacientes/buscar
     */
    @GetMapping("/pacientes/buscar")
    public List<PacienteBusquedaResponse> buscar(@RequestParam("q") String q) {
        return pacienteService.buscar(q);
    }

    @GetMapping("/pacientes")
    public org.springframework.http.ResponseEntity<List<com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO>> obtenerTodos() {
        return org.springframework.http.ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    @GetMapping("/pacientes/{id}")
    public org.springframework.http.ResponseEntity<com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO> obtenerPorId(@org.springframework.web.bind.annotation.PathVariable Long id) {
        return org.springframework.http.ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }
}

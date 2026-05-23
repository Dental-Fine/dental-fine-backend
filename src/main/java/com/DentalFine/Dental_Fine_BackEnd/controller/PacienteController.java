package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarPacienteRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteBusquedaResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO;
import com.DentalFine.Dental_Fine_BackEnd.service.PacienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/pacientes/buscar")
    public List<PacienteBusquedaResponse> buscar(@RequestParam("q") String q) {
        return pacienteService.buscar(q);
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> obtenerTodos() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<PacienteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    @PostMapping("/pacientes")
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody RegistrarPacienteRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.registrarPacienteYRetornarDto(datos));
    }

    @PutMapping("/pacientes/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id,
            @RequestBody RegistrarPacienteRequest datos) {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, datos));
    }

    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPacienteLogico(id);
        return ResponseEntity.noContent().build();
    }
}

package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.HorarioDisponibilidadResponse;
import com.DentalFine.Dental_Fine_BackEnd.service.CitaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    /**
     * Contrato: GET /citas/disponibilidad
     */
    @GetMapping("/citas/disponibilidad")
    public List<HorarioDisponibilidadResponse> disponibilidad(
            @RequestParam("dentistaId") Long dentistaId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.disponibilidad(dentistaId, fecha);
    }

    /**
     * Contrato: POST /citas/agendar
     */
    @PostMapping("/citas/agendar")
    public ResponseEntity<CitaAgendarResponse> agendar(@jakarta.validation.Valid @RequestBody AgendarCitaRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.agendarCita(body));
    }

    @GetMapping("/citas")
    public ResponseEntity<List<com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaResumenDTO>> obtenerTodos() {
        return ResponseEntity.ok(citaService.obtenerTodos());
    }

    @GetMapping("/citas/{id}")
    public ResponseEntity<com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaResumenDTO> obtenerPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @PutMapping("/citas/{id}/cancelar")
    public ResponseEntity<CitaAgendarResponse> cancelar(@PathVariable Long id,
            @RequestBody com.DentalFine.Dental_Fine_BackEnd.dto.requests.CancelarCitaRequest body) {
        return ResponseEntity.ok(citaService.cancelarCita(id, body));
    }

    @GetMapping("/citas/paciente/{pacienteId}")
    public ResponseEntity<List<com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaResumenDTO>> obtenerPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.obtenerPorPaciente(pacienteId));
    }

    @PutMapping("citas/{id}/editar")
    public ResponseEntity<CitaAgendarResponse> editar(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody AgendarCitaRequest body) {
        return ResponseEntity.ok(citaService.editarCita(id, body));
    }
}

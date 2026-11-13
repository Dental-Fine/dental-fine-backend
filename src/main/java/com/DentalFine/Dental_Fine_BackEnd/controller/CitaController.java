package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.CancelarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaResumenDTO;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.HorarioDisponibilidadResponse;
import com.DentalFine.Dental_Fine_BackEnd.service.CitaService;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@RestController
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    /**
     * Contrato: GET /api/citas/disponibilidad y /citas/disponibilidad
     */
    @GetMapping({"/api/citas/disponibilidad", "/citas/disponibilidad"})
    public List<HorarioDisponibilidadResponse> disponibilidad(
            @RequestParam("dentistaId") Long dentistaId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.disponibilidad(dentistaId, fecha);
    }

    /**
     * Contrato: POST /api/citas/agendar y /citas/agendar
     */
    @PostMapping({"/api/citas/agendar", "/citas/agendar"})
    public ResponseEntity<CitaAgendarResponse> agendar(@jakarta.validation.Valid @RequestBody AgendarCitaRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.agendarCita(body));
    }

    @GetMapping({"/api/citas", "/citas"})
    public ResponseEntity<List<CitaResumenDTO>> obtenerTodos() {
        return ResponseEntity.ok(citaService.obtenerTodos());
    }

    @GetMapping({"/api/citas/{id}", "/citas/{id}"})
    public ResponseEntity<CitaResumenDTO> obtenerPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @PutMapping({"/api/citas/{id}/cancelar", "/citas/{id}/cancelar"})
    public ResponseEntity<CitaAgendarResponse> cancelar(@PathVariable Long id,
            @RequestBody(required = false) CancelarCitaRequest body) {
        return ResponseEntity.ok(citaService.cancelarCita(id, body));
    }

    /**
     * HU05-T01: Marcar cita como INASISTENCIA (borrado lógico / inasistencia)
     */
    @PutMapping({"/api/citas/{id}/inasistencia", "/citas/{id}/inasistencia", "/api/citas/{id}/no-asistio", "/citas/{id}/no-asistio"})
    public ResponseEntity<CitaAgendarResponse> marcarInasistencia(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.marcarInasistencia(id));
    }

    /**
     * HU05-T01: Borrado lógico y archivado de cita
     */
    @DeleteMapping({"/api/citas/{id}", "/citas/{id}"})
    public ResponseEntity<Void> eliminarCitaLogica(@PathVariable Long id) {
        citaService.eliminarCitaLogica(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping({"/api/citas/paciente/{pacienteId}", "/citas/paciente/{pacienteId}"})
    public ResponseEntity<List<CitaResumenDTO>> obtenerPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.obtenerPorPaciente(pacienteId));
    }

    /**
     * HU08-T02: Reagendar cita verificando disponibilidad previa
     */
    @PutMapping({"/api/citas/{id}/reagendar", "/citas/{id}/reagendar", "/api/citas/{id}/editar", "/citas/{id}/editar"})
    public ResponseEntity<CitaAgendarResponse> reagendar(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody AgendarCitaRequest body) {
        return ResponseEntity.ok(citaService.reagendarCita(id, body));
    }
}

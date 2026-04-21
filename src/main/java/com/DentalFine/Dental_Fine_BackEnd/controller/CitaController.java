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
     * Contrato: GET /api/citas/disponibilidad → GET /citas/disponibilidad
     */
    @GetMapping("/citas/disponibilidad")
    public List<HorarioDisponibilidadResponse> disponibilidad(
            @RequestParam("dentistaId") Long dentistaId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return citaService.disponibilidad(dentistaId, fecha);
    }

    /**
     * Contrato: POST /api/citas/agendar → POST /citas/agendar
     */
    @PostMapping("/citas/agendar")
    public ResponseEntity<CitaAgendarResponse> agendar(@RequestBody AgendarCitaRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.agendarCita(body));
    }
}

package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.EvolucionAgregarRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.OdontogramaActualizarRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.SaludGeneralRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.EvolucionTratamiento;
import com.DentalFine.Dental_Fine_BackEnd.models.ExpedienteClinico;
import com.DentalFine.Dental_Fine_BackEnd.models.Odontograma;
import com.DentalFine.Dental_Fine_BackEnd.service.ExpedienteClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpedienteController {

    private final ExpedienteClinicoService expedienteService;

    public ExpedienteController(ExpedienteClinicoService expedienteService) {
        this.expedienteService = expedienteService;
    }

    @GetMapping("/expedientes/paciente/{pacienteId}")
    public ResponseEntity<ExpedienteClinico> obtenerPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(expedienteService.obtenerPorPacienteId(pacienteId));
    }

    @PutMapping("/expedientes/{id}/odontograma")
    public ResponseEntity<Odontograma> actualizarOdontograma(@PathVariable Long id,
            @RequestBody OdontogramaActualizarRequest body) {
        return ResponseEntity.ok(expedienteService.actualizarOdontograma(id, body));
    }

    @PostMapping("/expedientes/{id}/evolucion")
    public ResponseEntity<EvolucionTratamiento> agregarEvolucion(@PathVariable Long id,
            @RequestBody EvolucionAgregarRequest body) {
        return ResponseEntity.ok(expedienteService.agregarEvolucion(id, body));
    }

    @PutMapping("/expedientes/paciente/{pacienteId}/salud-general")
    public ResponseEntity<ExpedienteClinico> actualizarSaludGeneral(
            @PathVariable Long pacienteId,
            @RequestBody SaludGeneralRequest body) {
        return ResponseEntity.ok(expedienteService.actualizarSaludGeneral(pacienteId, body));
    }
}

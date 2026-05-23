package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Cuerpo POST /citas/agendar (ContratoApi). Montos derivados del tipo de servicio se expresan en {@link com.DentalFine.Dental_Fine_BackEnd.dto.MonedaCodigo#MXN}.
 */
public record AgendarCitaRequest(
        @NotNull(message = "El pacienteId es obligatorio")
        @JsonProperty("pacienteId") Long pacienteId,
        
        @NotNull(message = "El dentistaId es obligatorio")
        @JsonProperty("dentistaId") Long dentistaId,
        
        @NotNull(message = "La fechaHoraInicio es obligatoria")
        @JsonProperty("fechaHoraInicio") LocalDateTime fechaHoraInicio,
        
        @NotNull(message = "La fechaHoraFin es obligatoria")
        @JsonProperty("fechaHoraFin") LocalDateTime fechaHoraFin
) {
}

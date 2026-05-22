package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Cuerpo POST /citas/agendar (ContratoApi). Montos derivados del tipo de servicio se expresan en {@link com.DentalFine.Dental_Fine_BackEnd.dto.MonedaCodigo#MXN}.
 */
public record AgendarCitaRequest(
        @JsonProperty("pacienteId") Long pacienteId,
        @JsonProperty("dentistaId") Long dentistaId,
        @JsonProperty("fechaHoraInicio") LocalDateTime fechaHoraInicio,
        @JsonProperty("fechaHoraFin") LocalDateTime fechaHoraFin
) {
}

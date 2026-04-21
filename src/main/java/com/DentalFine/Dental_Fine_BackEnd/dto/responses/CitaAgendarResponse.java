package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Respuesta POST /citas/agendar y payload publicado en /topic/agenda (STOMP).
 * Importes asociados a la cita usan {@link com.DentalFine.Dental_Fine_BackEnd.dto.MonedaCodigo#MXN} en capas de dominio.
 */
public record CitaAgendarResponse(
        @JsonProperty("idCita") Long idCita,
        @JsonProperty("estado") String estado,
        @JsonProperty("mensaje") String mensaje
) {
}

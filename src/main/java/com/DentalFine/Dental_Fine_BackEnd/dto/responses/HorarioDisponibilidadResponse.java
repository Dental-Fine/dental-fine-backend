package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HorarioDisponibilidadResponse(
        @JsonProperty("horaInicio") String horaInicio,
        @JsonProperty("horaFin") String horaFin,
        @JsonProperty("disponible") boolean disponible
) {
}

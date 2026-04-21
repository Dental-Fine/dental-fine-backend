package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PacienteBusquedaResponse(
        @JsonProperty("idPaciente") Long idPaciente,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("apellidos") String apellidos,
        @JsonProperty("telefono") String telefono
) {
}

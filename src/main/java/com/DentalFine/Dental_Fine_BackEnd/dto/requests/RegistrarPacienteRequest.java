package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistrarPacienteRequest(
        @JsonProperty("nombre") String nombre,
        @JsonProperty("apellidos") String apellidos,
        @JsonProperty("telefono") String telefono,
        @JsonProperty("correo") String correo
) {
}

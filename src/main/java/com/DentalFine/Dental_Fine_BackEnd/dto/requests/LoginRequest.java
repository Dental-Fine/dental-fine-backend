package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty("correo") String correo,
        @JsonProperty("contrasena") String contrasena
) {
}

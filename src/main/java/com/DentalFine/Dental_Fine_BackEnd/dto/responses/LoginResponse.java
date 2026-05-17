package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("token") String token,
        @JsonProperty("usuario") UsuarioLoginResponse usuario
) {
}

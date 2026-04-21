package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuarioLoginResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("rol") String rol
) {
}

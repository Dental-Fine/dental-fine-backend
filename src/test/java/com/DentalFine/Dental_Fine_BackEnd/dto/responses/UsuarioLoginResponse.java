package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuarioLoginResponse(
        @JsonProperty("id") long id,
        @JsonProperty("rol") String rol
) {
}

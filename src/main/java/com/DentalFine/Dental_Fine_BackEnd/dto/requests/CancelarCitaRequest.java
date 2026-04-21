package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

public record CancelarCitaRequest(
        String rolUsuario,
        String motivoCancelacion
) {}

package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

public record RegistrarServicioRequest(
        String nombre,
        Double precio,
        Double duracion) {

}

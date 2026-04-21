package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

public record ServicioDTO(
        Long id,
        String nombre,
        Double precio,
        Double duracion
) {}

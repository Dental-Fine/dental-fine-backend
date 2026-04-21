package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

public record PacienteDTO(
        Long id,
        String nombre,
        String apellidos,
        String telefono,
        String correo
) {}

package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import java.time.LocalDate;

public record PacienteDTO(
        Long id,
        String nombre,
        String apellidos,
        String telefono,
        String correo,
        LocalDate fechaNacimiento,
        String sexo,
        String direccion,
        String contactoEmergenciaNombre,
        String contactoEmergenciaTelefono
) {
    public PacienteDTO(Long id, String nombre, String apellidos, String telefono, String correo) {
        this(id, nombre, apellidos, telefono, correo, null, null, null, null, null);
    }
}

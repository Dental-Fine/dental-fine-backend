package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CitaResumenDTO(
        Long id,
        DentistaDTO dentista,
        PacienteDTO paciente,
        ServicioDTO tipoServicio,
        LocalDateTime fecha,
        String nombre,
        float monto,
        String estado,
        LocalDate fechaCreacion
) {}

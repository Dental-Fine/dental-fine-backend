package com.DentalFine.Dental_Fine_BackEnd.dto.responses;

import java.time.LocalDate;

public record EvolucionTratamientoDTO(
        Long id,
        Long expedienteClinicoId,
        Long citaId,
        Long dentistaId,
        String dentistaNombre,
        String notasClinicas,
        String piezaDental,
        String procedimiento,
        LocalDate fechaRegistro
) {
}

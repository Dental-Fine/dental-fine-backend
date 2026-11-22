package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record EvolucionAgregarRequest(
        Long citaId,
        Long dentistaId,
        @NotBlank(message = "Las notas clínicas no pueden estar vacías")
        String notasClinicas,
        String piezaDental,
        String procedimiento,
        LocalDate fechaRegistro
) {
}

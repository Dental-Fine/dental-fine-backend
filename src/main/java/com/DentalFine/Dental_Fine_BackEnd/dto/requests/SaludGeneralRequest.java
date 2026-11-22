package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import java.time.LocalDate;

public record SaludGeneralRequest(
        String alergias,
        String enfermedadesCronicas,
        String tipoSanguineo,
        // Alertas Médicas Estomatológicas (NOM-013-SSA2-2015)
        Boolean alergiaAnestesia,
        Boolean alergiaLatex,
        Boolean hipertension,
        Boolean diabetes,
        Boolean cardiopatias,
        Boolean problemasCoagulacion,
        Boolean embarazo,
        String medicamentosActuales,
        // Ficha de Identificación (NOM-004-SSA3-2012)
        LocalDate fechaNacimiento,
        String sexo,
        String direccion,
        String contactoEmergenciaNombre,
        String contactoEmergenciaTelefono
) {
}

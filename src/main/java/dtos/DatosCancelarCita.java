package dtos;

import models.Estado;

public record DatosCancelarCita(
        long idCita,
        Estado estado
        /*
        nombre paciente
        fecha de cambio
        doc
         */
) {
}

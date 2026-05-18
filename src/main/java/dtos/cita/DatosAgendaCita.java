package dtos.cita;

import java.time.LocalDateTime;

public record DatosAgendaCita(
    Long idDentista,
    Long idPaciente,
    LocalDateTime fecha
) {

}

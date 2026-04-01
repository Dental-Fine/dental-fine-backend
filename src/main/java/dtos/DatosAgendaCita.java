package dtos;

import models.Paciente;
import models.PersonalClinica;

import java.time.LocalDateTime;

public record DatosAgendaCita(
    Long idDentista,
    Long idPaciente,
    LocalDateTime fecha,
    boolean personal
) {

}

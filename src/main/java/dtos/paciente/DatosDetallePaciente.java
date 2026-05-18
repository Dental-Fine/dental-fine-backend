package dtos.paciente;

import models.Paciente;

import java.time.LocalDateTime;

public record DatosDetallePaciente (
    long id,
    String nombre,
    String correo,
    LocalDateTime fechaDeCreacion
){
    public DatosDetallePaciente(Paciente paciente){
        this(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getCorreo(),
                LocalDateTime.now()
        );
    }
}

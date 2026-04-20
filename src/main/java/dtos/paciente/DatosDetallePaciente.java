package dtos.paciente;

import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;

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

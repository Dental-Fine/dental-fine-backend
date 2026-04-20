package dtos.cita;

import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.Estado;

import java.time.LocalDateTime;

public record DatosActualizarCita(
        long idCita,
        long idPaciente,
        long idDentista,
        LocalDateTime fechaDeCambio,
        Estado estado
) {
    public DatosActualizarCita(Cita cita){
        this(
                cita.getId(),
                cita.getIdPaciente(),
                cita.getIdDentista(),
                LocalDateTime.now(),
                cita.getEstado()
        );
    }
}

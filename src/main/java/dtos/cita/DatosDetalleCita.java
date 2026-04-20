package dtos.cita;

import com.DentalFine.Dental_Fine_BackEnd.models.Cita;

import java.time.LocalDateTime;

public record DatosDetalleCita(
        Long id,
        Long idDentista,
        Long idPaciente,
        LocalDateTime fecha
) {
    public DatosDetalleCita(Cita cita){
        this(
                cita.getId(),
                cita.getIdDentista(),
                cita.getIdPaciente(),
                cita.getFecha()
        );
    }
}

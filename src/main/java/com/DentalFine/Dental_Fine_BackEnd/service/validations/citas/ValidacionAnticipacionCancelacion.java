package com.DentalFine.Dental_Fine_BackEnd.service.validations.citas;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.CancelarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidacionAnticipacionCancelacion implements ValidadorCancelacionDeCitas {

    @Override
    public void validar(Cita cita, CancelarCitaRequest request) {
        if ("PACIENTE".equalsIgnoreCase(request.rolUsuario())) {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime limiteCancelacion = cita.getFechaHoraInicio().minusHours(24);
            
            if (ahora.isAfter(limiteCancelacion)) {
                throw new ValidationException("Los pacientes no pueden cancelar citas con menos de 24 horas de anticipación.");
            }
        }
    }
}

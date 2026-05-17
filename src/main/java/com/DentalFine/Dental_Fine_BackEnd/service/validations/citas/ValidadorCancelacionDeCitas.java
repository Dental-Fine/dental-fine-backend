package com.DentalFine.Dental_Fine_BackEnd.service.validations.citas;

import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.CancelarCitaRequest;

public interface ValidadorCancelacionDeCitas {
    void validar(Cita cita, CancelarCitaRequest request);
}

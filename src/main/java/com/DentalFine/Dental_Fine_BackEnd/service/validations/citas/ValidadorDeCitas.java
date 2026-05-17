package com.DentalFine.Dental_Fine_BackEnd.service.validations.citas;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;

public interface ValidadorDeCitas {
    void validar(AgendarCitaRequest datos);
}

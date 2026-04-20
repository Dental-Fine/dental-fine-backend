package com.DentalFine.Dental_Fine_BackEnd.service.validations.citas;

import dtos.cita.DatosAgendaCita;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.ValidationException;

@Component
public class ValidacionDiaPaciente implements ValidadorDeCitas{
    @Autowired
    CitaRepository repository;

    @Override
    public void validar(DatosAgendaCita datos) {
        if(repository.citaConAnticipacion(datos.fecha()))
            throw new ValidationException("El paciente ya cuenta con una cita en este mismo dia");
    }
}
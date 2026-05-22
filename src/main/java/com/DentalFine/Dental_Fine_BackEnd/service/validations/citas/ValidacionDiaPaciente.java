package com.DentalFine.Dental_Fine_BackEnd.service.validations.citas;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidacionDiaPaciente implements ValidadorDeCitas {

    @Autowired
    private CitaRepository repository;

    @Override
    public void validar(AgendarCitaRequest datos) {
        LocalDateTime inicio = datos.fechaHoraInicio().toLocalDate().atStartOfDay();
        LocalDateTime finExclusivo = inicio.plusDays(1);
        if (repository.existsCitaPacienteEnRango(datos.pacienteId(), inicio, finExclusivo)) {
            throw new ValidationException("El paciente ya cuenta con una cita en este mismo dia");
        }
    }
}

package service.validations.citas;

import dtos.DatosAgendaCita;
import org.springframework.beans.factory.annotation.Autowired;
import respositories.CitaRepository;
import service.validations.ValidationException;

public class ValidacionDiaPaciente implements ValidadorDeCitasPaciente{
    @Autowired
    CitaRepository repository;

    @Override
    public void validar(DatosAgendaCita datos) {
        if(repository.citaConAnticipacion(datos.fecha()))
            throw new ValidationException("El paciente ya cuenta con una cita en este mismo dia");
    }
}
package service.validations.citas;

import dtos.cita.DatosAgendaCita;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.CitaRepository;
import service.validations.ValidationException;

public class ValidacionDiaPaciente implements ValidadorDeCitas{
    @Autowired
    CitaRepository repository;

    @Override
    public void validar(DatosAgendaCita datos) {
        if(repository.citaConAnticipacion(datos.fecha()))
            throw new ValidationException("El paciente ya cuenta con una cita en este mismo dia");
    }
}
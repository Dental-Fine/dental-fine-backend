package service;

import dtos.cita.DatosDetalleCita;
import dtos.cita.DatosAgendaCita;
import models.Cita;
import models.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CitaRepository;
import repositories.DentistaRepository;
import repositories.PacienteRepository;
import service.validations.ValidationException;
import service.validations.citas.ValidadorDeCitas;

import java.time.LocalDate;
import java.util.List;

@Service
public class CitaService {
    @Autowired
    CitaRepository citaRepo;
    @Autowired
    PacienteRepository pacienteRepo;
    @Autowired
    DentistaRepository dentistaRepository;

    @Autowired
    private List<ValidadorDeCitas> validadores;

    public DatosDetalleCita agendarCita(DatosAgendaCita datos){
        if(!pacienteRepo.existsById(datos.idPaciente()))
            throw new ValidationException("No existe un paciente con este id.");
        if(!dentistaRepository.existsById(datos.idDentista()))
            throw new ValidationException("No existe un dentista con este ID");

        validadores.forEach(v -> v.validar(datos));

        Cita cita = citaRepo.save(
                new Cita(
                        datos.idDentista(),
                        datos.idPaciente(),
                        datos.fecha(),
                        Estado.ACTIVA,
                        LocalDate.now()
                )
        );

        return new DatosDetalleCita(cita);
    }

    // Aqui falta ver cómo se haría la penalización
    public DatosDetalleCita ActualizarEstadoCita(DatosDetalleCita datos, Estado estado){
        Cita cita = citaRepo.getReferenceById(datos.id());

        //Aqui meteria varios casos depende de cuál es el nuevo estado

        cita.actualizarEstado(estado);

        return new DatosDetalleCita(cita);
    }

}

package service;

import dtos.DatosCancelarCita;
import dtos.DatosDetalleCita;
import dtos.DatosAgendaCita;
import models.Cita;
import models.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import respositories.CitaRepository;
import respositories.DentistaRepository;
import respositories.PacienteRepository;
import service.validations.ValidationException;
import service.validations.citas.ValidadorDeCitas;
import service.validations.citas.ValidadorDeCitasPaciente;

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
    @Autowired
    private List<ValidadorDeCitasPaciente> validadoresPaciente;

    public DatosDetalleCita agendarCita(DatosAgendaCita datos){
        if(!pacienteRepo.existsById(datos.idPaciente()))
            throw new ValidationException("No existe un paciente con este id.");
        if(!dentistaRepository.existsById(datos.idDentista()))
            throw new ValidationException("No existe un dentista con este ID");

        // keloke mi loco esto no importa que sea paciente o personal
        if(datos.personal()){
            validadoresPaciente.forEach(v -> v.validar(datos));
        }else {
            validadores.forEach(v -> v.validar(datos));
        }

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

    // Aqui falta ver qué es lo que pasaría si quien cancela la cita es el paciente
    public DatosCancelarCita ActualizarEstadoCita(DatosCancelarCita datos){
        Cita cita = citaRepo.getReferenceById(datos.idCita());

        if(datos.personal()){
            cita.actualizarEstado(datos.estado());
        } else {

        }
    }

}

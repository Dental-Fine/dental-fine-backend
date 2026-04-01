package service;

import dtos.paciente.DatosDetallePaciente;
import dtos.paciente.DatosRegistrarPaciente;
import models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.PacienteRepository;

@Service
public class PacienteService {
    @Autowired
    PacienteRepository pacienteRepo;

    public DatosDetallePaciente registrarPaciente(DatosRegistrarPaciente datos){
        Paciente paciente = pacienteRepo.save(
                new Paciente(datos)
        );

        return new DatosDetallePaciente(paciente);
    }
}

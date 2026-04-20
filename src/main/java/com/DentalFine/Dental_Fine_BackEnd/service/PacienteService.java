package com.DentalFine.Dental_Fine_BackEnd.service;

import dtos.paciente.DatosDetallePaciente;
import dtos.paciente.DatosRegistrarPaciente;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;

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

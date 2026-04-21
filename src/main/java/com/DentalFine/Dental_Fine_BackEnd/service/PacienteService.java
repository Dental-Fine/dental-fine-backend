package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarPacienteRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteBusquedaResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepo;

    public List<PacienteBusquedaResponse> buscar(String q) {
        if (q == null || q.isBlank()) {
            return List.of();
        }
        String like = "%" + q.trim() + "%";
        return pacienteRepo.buscarPorNombreApellidosOTelefono(like).stream()
                .map(p -> new PacienteBusquedaResponse(
                        p.getId(),
                        p.getNombre(),
                        p.getApellidos(),
                        p.getTelefono()
                ))
                .toList();
    }

    public Paciente registrarPaciente(RegistrarPacienteRequest datos) {
        return pacienteRepo.save(new Paciente(datos));
    }

    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepo.findAll().stream()
                .map(p -> new PacienteDTO(p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo()))
                .toList();
    }

    public PacienteDTO obtenerPorId(Long id) {
        Paciente p = pacienteRepo.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        return new PacienteDTO(p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo());
    }
}

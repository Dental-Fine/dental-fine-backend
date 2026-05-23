package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarPacienteRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteBusquedaResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepo;
    @Autowired
    private ExpedienteClinicoService expedienteClinicoService;

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
                        p.getTelefono()))
                .toList();
    }

    public Paciente registrarPaciente(RegistrarPacienteRequest datos) {
        Paciente p = pacienteRepo.save(new Paciente(datos));
        expedienteClinicoService.crearExpediente(p);
        return p;
    }

    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepo.findByActivoTrue().stream()
                .map(p -> new PacienteDTO(p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo()))
                .toList();
    }

    public PacienteDTO obtenerPorId(Long id) {
        Paciente p = pacienteRepo.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        return new PacienteDTO(p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo());
    }

    @Transactional
    public PacienteDTO registrarPacienteYRetornarDto(RegistrarPacienteRequest datos) {
        Paciente p = pacienteRepo.save(new Paciente(datos));
        expedienteClinicoService.crearExpediente(p);
        return new PacienteDTO(
                p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo());
    }

    @Transactional
    public PacienteDTO actualizarPaciente(Long id, RegistrarPacienteRequest datos) {
        Paciente p = pacienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        p.setNombre(datos.nombre());
        p.setApellidos(datos.apellidos() != null ? datos.apellidos() : "");
        p.setTelefono(datos.telefono());
        p.setCorreo(datos.correo());
        pacienteRepo.save(p);
        return new PacienteDTO(
                p.getId(), p.getNombre(), p.getApellidos(), p.getTelefono(), p.getCorreo());
    }

    @Transactional
    public void eliminarPacienteLogico(Long id) {
        Paciente p = pacienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        p.setActivo(false);
        pacienteRepo.save(p);
    }
}

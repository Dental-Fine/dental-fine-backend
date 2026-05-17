package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.DentistaDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.Dentista;
import com.DentalFine.Dental_Fine_BackEnd.repository.DentistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;

    public DentistaService(DentistaRepository dentistaRepository) {
        this.dentistaRepository = dentistaRepository;
    }

    public List<DentistaDTO> obtenerTodos() {
        return dentistaRepository.findAll().stream()
                .map(d -> new DentistaDTO(d.getId(), d.getNombre()))
                .toList();
    }

    public DentistaDTO obtenerPorId(Long id) {
        Dentista d = dentistaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dentista no encontrado"));
        return new DentistaDTO(d.getId(), d.getNombre());
    }
}

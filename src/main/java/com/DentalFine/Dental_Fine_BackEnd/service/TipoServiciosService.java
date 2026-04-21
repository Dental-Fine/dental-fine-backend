package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.ServicioDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TipoServiciosService {

    private final TipoServiciosRepository tipoServiciosRepository;

    public TipoServiciosService(TipoServiciosRepository tipoServiciosRepository) {
        this.tipoServiciosRepository = tipoServiciosRepository;
    }

    public List<ServicioDTO> obtenerTodos() {
        return tipoServiciosRepository.findAll().stream()
                .map(s -> new ServicioDTO(s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion()))
                .toList();
    }

    public ServicioDTO obtenerPorId(Long id) {
        TipoServicios s = tipoServiciosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        return new ServicioDTO(s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion());
    }
}

package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarServicioRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.ServicioDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TipoServiciosService {

    private final TipoServiciosRepository tipoServiciosRepository;

    public TipoServiciosService(TipoServiciosRepository tipoServiciosRepository) {
        this.tipoServiciosRepository = tipoServiciosRepository;
    }

    public List<ServicioDTO> obtenerTodos() {
        return tipoServiciosRepository.findByActivoTrue().stream()
                .map(s -> new ServicioDTO(s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion()))
                .toList();
    }

    public ServicioDTO obtenerPorId(Long id) {
        TipoServicios s = tipoServiciosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        return new ServicioDTO(s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion());
    }

    @Transactional
    public ServicioDTO crearServicio(RegistrarServicioRequest datos) {
        TipoServicios s = new TipoServicios();
        s.setNombre(datos.nombre());
        s.setPrecio(datos.precio());
        s.setDuracion(datos.duracion());
        s.setActivo(true);
        s = tipoServiciosRepository.save(s);
        return new ServicioDTO(
                s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion());
    }

    @Transactional
    public ServicioDTO actualizarServicio(Long id, RegistrarServicioRequest datos) {
        TipoServicios s = tipoServiciosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        s.setNombre(datos.nombre());
        s.setPrecio(datos.precio());
        s.setDuracion(datos.duracion());
        tipoServiciosRepository.save(s);
        return new ServicioDTO(
                s.getId(), s.getNombre(), s.getPrecio(), s.getDuracion());
    }

    @Transactional
    public void eliminarServicioLogico(Long id) {
        TipoServicios s = tipoServiciosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        s.setActivo(false);
        tipoServiciosRepository.save(s);
    }

}

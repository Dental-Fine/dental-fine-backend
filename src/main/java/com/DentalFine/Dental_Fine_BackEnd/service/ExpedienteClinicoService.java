package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.EvolucionAgregarRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.OdontogramaActualizarRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.SaludGeneralRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.EvolucionTratamiento;
import com.DentalFine.Dental_Fine_BackEnd.models.ExpedienteClinico;
import com.DentalFine.Dental_Fine_BackEnd.models.Odontograma;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.EvolucionTratamientoRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.ExpedienteClinicoRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.OdontogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class ExpedienteClinicoService {

    @Autowired
    private ExpedienteClinicoRepository expedienteRepository;
    @Autowired
    private OdontogramaRepository odontogramaRepository;
    @Autowired
    private EvolucionTratamientoRepository evolucionRepository;
    @Autowired
    private CitaRepository citaRepository;

    @Transactional
    public ExpedienteClinico obtenerPorPacienteId(Long pacienteId) {
        ExpedienteClinico expediente = expedienteRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado para el paciente"));

        inicializarColecciones(expediente);
        return expediente;
    }

    @Transactional
    public ExpedienteClinico crearExpediente(Paciente paciente) {
        ExpedienteClinico exp = new ExpedienteClinico();
        exp.setPaciente(paciente);
        exp.setFechaCreacion(LocalDate.now());

        Odontograma odontograma = new Odontograma();
        odontograma.setExpedienteClinico(exp);
        odontograma.setEstadoDientes(new HashMap<>());

        exp.setOdontograma(odontograma);
        return expedienteRepository.save(exp);
    }

    @Transactional
    public Odontograma actualizarOdontograma(Long expedienteId, OdontogramaActualizarRequest request) {
        ExpedienteClinico exp = expedienteRepository.findById(expedienteId)
                .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado"));

        Odontograma odontograma = exp.getOdontograma();
        if (odontograma == null) {
            odontograma = new Odontograma();
            odontograma.setExpedienteClinico(exp);
            exp.setOdontograma(odontograma);
        }
        odontograma.setEstadoDientes(request.estadoDientes());
        return odontogramaRepository.save(odontograma);
    }

    @Transactional
    public EvolucionTratamiento agregarEvolucion(Long expedienteId, EvolucionAgregarRequest request) {
        ExpedienteClinico exp = expedienteRepository.findById(expedienteId)
                .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado"));

        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        EvolucionTratamiento ev = new EvolucionTratamiento();
        ev.setExpedienteClinico(exp);
        ev.setCita(cita);
        ev.setNotasClinicas(request.notasClinicas());
        ev.setFechaRegistro(LocalDate.now());

        return evolucionRepository.save(ev);
    }

    @Transactional
    public ExpedienteClinico actualizarSaludGeneral(Long pacienteId, SaludGeneralRequest request) {
        ExpedienteClinico expediente = expedienteRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado"));

        expediente.setAlergias(request.alergias());
        expediente.setEnfermedadesCronicas(request.enfermedadesCronicas());
        expediente.setTipoSanguineo(request.tipoSanguineo());

        expediente = expedienteRepository.save(expediente);
        inicializarColecciones(expediente);
        return expediente;
    }

    private void inicializarColecciones(ExpedienteClinico expediente) {
        if (expediente != null) {
            org.hibernate.Hibernate.initialize(expediente.getEvoluciones());
            if (expediente.getOdontograma() != null) {
                org.hibernate.Hibernate.initialize(expediente.getOdontograma());
                org.hibernate.Hibernate.initialize(expediente.getOdontograma().getEstadoDientes());
            }
        }
    }
}

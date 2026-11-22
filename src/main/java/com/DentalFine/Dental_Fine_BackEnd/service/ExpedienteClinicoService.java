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
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
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
    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public ExpedienteClinico obtenerPorPacienteId(Long pacienteId) {
        ExpedienteClinico expediente = expedienteRepository.findByPacienteId(pacienteId)
                .orElseGet(() -> {
                    Paciente paciente = pacienteRepository.findById(pacienteId)
                            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
                    return crearExpediente(paciente);
                });

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
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        // 1. Actualizar Ficha de Identificación del Paciente (NOM-004) si se reciben
        if (request.fechaNacimiento() != null) paciente.setFechaNacimiento(request.fechaNacimiento());
        if (request.sexo() != null) paciente.setSexo(request.sexo());
        if (request.direccion() != null) paciente.setDireccion(request.direccion());
        if (request.contactoEmergenciaNombre() != null) paciente.setContactoEmergenciaNombre(request.contactoEmergenciaNombre());
        if (request.contactoEmergenciaTelefono() != null) paciente.setContactoEmergenciaTelefono(request.contactoEmergenciaTelefono());
        pacienteRepository.save(paciente);

        // 2. Obtener o crear expediente clínico
        ExpedienteClinico expediente = expedienteRepository.findByPacienteId(pacienteId)
                .orElseGet(() -> crearExpediente(paciente));

        // 3. Actualizar antecedentes generales
        if (request.alergias() != null) expediente.setAlergias(request.alergias());
        if (request.enfermedadesCronicas() != null) expediente.setEnfermedadesCronicas(request.enfermedadesCronicas());
        if (request.tipoSanguineo() != null) expediente.setTipoSanguineo(request.tipoSanguineo());

        // 4. Actualizar Alertas Médicas Estomatológicas (NOM-013)
        if (request.alergiaAnestesia() != null) expediente.setAlergiaAnestesia(request.alergiaAnestesia());
        if (request.alergiaLatex() != null) expediente.setAlergiaLatex(request.alergiaLatex());
        if (request.hipertension() != null) expediente.setHipertension(request.hipertension());
        if (request.diabetes() != null) expediente.setDiabetes(request.diabetes());
        if (request.cardiopatias() != null) expediente.setCardiopatias(request.cardiopatias());
        if (request.problemasCoagulacion() != null) expediente.setProblemasCoagulacion(request.problemasCoagulacion());
        if (request.embarazo() != null) expediente.setEmbarazo(request.embarazo());
        if (request.medicamentosActuales() != null) expediente.setMedicamentosActuales(request.medicamentosActuales());

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

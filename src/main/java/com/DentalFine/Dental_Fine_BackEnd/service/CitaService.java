package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.CancelarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaResumenDTO;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.HorarioDisponibilidadResponse;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.Dentista;
import com.DentalFine.Dental_Fine_BackEnd.models.Estado;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.DentistaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.ValidationException;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.citas.ValidadorCancelacionDeCitas;
import com.DentalFine.Dental_Fine_BackEnd.service.validations.citas.ValidadorDeCitas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepo;
    @Autowired
    private PacienteRepository pacienteRepo;
    @Autowired
    private DentistaRepository dentistaRepository;
    @Autowired
    private TipoServiciosRepository tipoServiciosRepository;
    @Autowired
    private List<ValidadorDeCitas> validadores;
    @Autowired
    private List<ValidadorCancelacionDeCitas> validadoresCancelacion;
    @Autowired
    private AgendaEventPublisher agendaEventPublisher;

    public List<HorarioDisponibilidadResponse> disponibilidad(Long dentistaId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime finExclusivo = fecha.plusDays(1).atStartOfDay();
        List<Cita> citas = citaRepo.findCitasDentistaEnRango(dentistaId, inicio, finExclusivo);
        List<HorarioDisponibilidadResponse> horarios = new ArrayList<>();
        for (int h = 8; h < 17; h++) {
            LocalDateTime slotInicio = fecha.atTime(h, 0);
            LocalDateTime slotFin = slotInicio.plusHours(1);
            boolean ocupado = citas.stream().anyMatch(c -> !(c.getFechaHoraFin().isBefore(slotInicio)
                    || c.getFechaHoraInicio().isAfter(slotFin) || c.getFechaHoraInicio().isEqual(slotFin)));
            horarios.add(new HorarioDisponibilidadResponse(
                    String.format("%02d:00", h),
                    String.format("%02d:00", h + 1),
                    !ocupado));
        }
        return horarios;
    }

    @Transactional
    public CitaAgendarResponse agendarCita(AgendarCitaRequest datos) {
        if (!pacienteRepo.existsById(datos.pacienteId())) {
            throw new ValidationException("No existe un paciente con este id.");
        }
        if (!dentistaRepository.existsById(datos.dentistaId())) {
            throw new ValidationException("No existe un dentista con este ID");
        }

        // Validate time overlap for the dentist
        List<Cita> citasDentista = citaRepo.findCitasDentistaEnRango(datos.dentistaId(),
                datos.fechaHoraInicio().toLocalDate().atStartOfDay(),
                datos.fechaHoraInicio().toLocalDate().plusDays(1).atStartOfDay());
        boolean dentistaOcupado = citasDentista.stream()
                .anyMatch(c -> !(c.getFechaHoraFin().isBefore(datos.fechaHoraInicio())
                        || c.getFechaHoraFin().isEqual(datos.fechaHoraInicio()) ||
                        c.getFechaHoraInicio().isAfter(datos.fechaHoraFin())
                        || c.getFechaHoraInicio().isEqual(datos.fechaHoraFin())));
        if (dentistaOcupado) {
            throw new ValidationException("El dentista ya tiene una cita asignada en ese horario.");
        }

        validadores.forEach(v -> v.validar(datos));

        Paciente paciente = pacienteRepo.getReferenceById(datos.pacienteId());
        Dentista dentista = dentistaRepository.getReferenceById(datos.dentistaId());

        Cita cita = new Cita();
        cita.setDentista(dentista);
        cita.setPaciente(paciente);
        cita.setFechaHoraInicio(datos.fechaHoraInicio());
        cita.setFechaHoraFin(datos.fechaHoraFin());
        cita.setEstado(Estado.PENDIENTE);
        cita.setFechaCreacion(LocalDate.now());

        Cita guardada = citaRepo.save(cita);

        CitaAgendarResponse respuesta = new CitaAgendarResponse(
                guardada.getId(),
                guardada.getEstado().name(),
                "Cita agendada correctamente");
        agendaEventPublisher.publicarCitaConfirmada(respuesta);
        return respuesta;
    }

    @Transactional
    public CitaAgendarResponse actualizarEstadoCita(Long idCita, Estado estado) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new ValidationException("No existe la cita indicada."));
        cita.actualizarEstado(estado);
        citaRepo.save(cita);
        return new CitaAgendarResponse(cita.getId(), cita.getEstado().name(), "Estado actualizado");
    }

    @Transactional
    public CitaAgendarResponse cancelarCita(Long idCita, CancelarCitaRequest request) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "No existe la cita indicada."));

        if (cita.getEstado() == Estado.CANCELADA) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "La cita ya se encuentra cancelada.");
        }

        if ("PACIENTE".equalsIgnoreCase(request.rolUsuario())) {
            validadoresCancelacion.forEach(v -> v.validar(cita, request));
        }

        cita.actualizarEstado(Estado.CANCELADA);
        Cita guardada = citaRepo.save(cita);

        CitaAgendarResponse respuesta = new CitaAgendarResponse(
                guardada.getId(),
                guardada.getEstado().name(),
                "Cita cancelada correctamente");
        agendaEventPublisher.publicarCitaConfirmada(respuesta);
        return respuesta;
    }

    @Transactional(readOnly = true)
    public List<CitaResumenDTO> obtenerTodos() {
        return citaRepo.findByActivoTrue().stream()
                .map(this::mapearACitaResumenDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResumenDTO obtenerPorId(Long id) {
        Cita cita = citaRepo.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Cita no encontrada"));
        return mapearACitaResumenDTO(cita);
    }

    @Transactional(readOnly = true)
    public List<CitaResumenDTO> obtenerPorPaciente(Long pacienteId) {
        return citaRepo.findByActivoTrue().stream()
                .filter(c -> c.getPaciente().getId().equals(pacienteId))
                .map(this::mapearACitaResumenDTO)
                .toList();
    }

    private CitaResumenDTO mapearACitaResumenDTO(Cita cita) {
        return new CitaResumenDTO(
                cita.getId(),
                new com.DentalFine.Dental_Fine_BackEnd.dto.responses.DentistaDTO(cita.getDentista().getId(),
                        cita.getDentista().getNombre()),
                new com.DentalFine.Dental_Fine_BackEnd.dto.responses.PacienteDTO(cita.getPaciente().getId(),
                        cita.getPaciente().getNombre(), cita.getPaciente().getApellidos(),
                        cita.getPaciente().getTelefono(), cita.getPaciente().getCorreo()),
                null, // TipoServicio is removed from Cita
                cita.getFechaHoraInicio(),
                "Cita Programada", // Default name
                0f, // Default amount, ticket handles real amounts
                cita.getEstado() != null ? cita.getEstado().name() : null,
                cita.getFechaCreacion());
    }

    @Transactional
    public CitaAgendarResponse editarCita(Long idCita, AgendarCitaRequest datos) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (!pacienteRepo.existsById(datos.pacienteId())) {
            throw new ValidationException("No existe un paciente con este id.");
        }
        if (!dentistaRepository.existsById(datos.dentistaId())) {
            throw new ValidationException("No existe un dentista con este ID");
        }
        Paciente paciente = pacienteRepo.getReferenceById(datos.pacienteId());
        Dentista dentista = dentistaRepository.getReferenceById(datos.dentistaId());
        cita.setPaciente(paciente);
        cita.setDentista(dentista);
        cita.setFechaHoraInicio(datos.fechaHoraInicio());
        cita.setFechaHoraFin(datos.fechaHoraFin());
        Cita actualizada = citaRepo.save(cita);
        return new CitaAgendarResponse(
                actualizada.getId(),
                actualizada.getEstado().name(),
                "Cita reprogramada correctamente");
    }
}

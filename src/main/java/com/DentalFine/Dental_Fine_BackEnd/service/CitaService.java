package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
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
import com.DentalFine.Dental_Fine_BackEnd.service.validations.citas.ValidadorDeCitas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AgendaEventPublisher agendaEventPublisher;

    public List<HorarioDisponibilidadResponse> disponibilidad(Long dentistaId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime finExclusivo = fecha.plusDays(1).atStartOfDay();
        List<Cita> citas = citaRepo.findCitasDentistaEnRango(dentistaId, inicio, finExclusivo);
        List<HorarioDisponibilidadResponse> horarios = new ArrayList<>();
        for (int h = 8; h < 17; h++) {
            LocalDateTime slotInicio = fecha.atTime(h, 0);
            LocalDateTime slotFin = slotInicio.plusHours(1);
            boolean ocupado = citas.stream().anyMatch(c ->
                    !c.getFecha().isBefore(slotInicio) && c.getFecha().isBefore(slotFin));
            horarios.add(new HorarioDisponibilidadResponse(
                    String.format("%02d:00", h),
                    String.format("%02d:00", h + 1),
                    !ocupado
            ));
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
        if (!tipoServiciosRepository.existsById(datos.tipoServicioId())) {
            throw new ValidationException("No existe un tipo de servicio con este id.");
        }

        validadores.forEach(v -> v.validar(datos));

        Paciente paciente = pacienteRepo.getReferenceById(datos.pacienteId());
        Dentista dentista = dentistaRepository.getReferenceById(datos.dentistaId());
        TipoServicios tipo = tipoServiciosRepository.getReferenceById(datos.tipoServicioId());

        float monto = tipo.getPrecio() != null ? tipo.getPrecio().floatValue() : 0f;

        Cita cita = new Cita();
        cita.setDentista(dentista);
        cita.setPaciente(paciente);
        cita.setTipoServicio(tipo);
        cita.setFecha(datos.fechaHora());
        cita.setNombre(tipo.getNombre());
        cita.setMonto(monto);
        cita.setEstado(Estado.PENDIENTE);
        cita.setFechaCreacion(LocalDate.now());

        Cita guardada = citaRepo.save(cita);

        CitaAgendarResponse respuesta = new CitaAgendarResponse(
                guardada.getId(),
                guardada.getEstado().name(),
                "Cita agendada correctamente"
        );
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
}

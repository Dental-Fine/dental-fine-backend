package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock private CitaRepository citaRepo;
    @Mock private PacienteRepository pacienteRepo;
    @Mock private DentistaRepository dentistaRepository;
    @Mock private TipoServiciosRepository tipoServiciosRepository;
    @Mock private List<ValidadorDeCitas> validadores;
    @Mock private AgendaEventPublisher agendaEventPublisher;

    @InjectMocks
    private CitaService citaService;

    @Test
    void agendarCita_exitoso() {
        AgendarCitaRequest request = new AgendarCitaRequest(1L, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        Mockito.when(pacienteRepo.existsById(1L)).thenReturn(true);
        Mockito.when(dentistaRepository.existsById(1L)).thenReturn(true);

        Paciente paciente = new Paciente(); paciente.setId(1L);
        Dentista dentista = new Dentista(); dentista.setId(1L);
        TipoServicios tipoServicio = new TipoServicios();
        tipoServicio.setId(1L);
        tipoServicio.setPrecio(100.00);
        tipoServicio.setNombre("Limpieza");

        Mockito.when(pacienteRepo.getReferenceById(1L)).thenReturn(paciente);
        Mockito.when(dentistaRepository.getReferenceById(1L)).thenReturn(dentista);

        Cita citaGuardada = new Cita();
        citaGuardada.setId(1L);
        citaGuardada.setEstado(Estado.PENDIENTE);
        Mockito.when(citaRepo.save(any(Cita.class))).thenReturn(citaGuardada);

        CitaAgendarResponse response = citaService.agendarCita(request);

        assertNotNull(response);
        assertEquals(1L, response.idCita());
        assertEquals("PENDIENTE", response.estado());
        Mockito.verify(agendaEventPublisher).publicarCitaConfirmada(any(CitaAgendarResponse.class));
    }

    @Test
    void agendarCita_lanzaValidationException_siNoExistePaciente() {
        AgendarCitaRequest request = new AgendarCitaRequest(99L, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        Mockito.when(pacienteRepo.existsById(99L)).thenReturn(false);

        assertThrows(ValidationException.class, () -> citaService.agendarCita(request));
    }

    @Test
    void reagendarCita_exitoso_cuandoHayDisponibilidad() {
        LocalDateTime nuevoInicio = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        LocalDateTime nuevoFin = nuevoInicio.plusHours(1);
        AgendarCitaRequest request = new AgendarCitaRequest(1L, 1L, nuevoInicio, nuevoFin);

        Cita citaExistente = new Cita();
        citaExistente.setId(10L);
        citaExistente.setEstado(Estado.PENDIENTE);
        citaExistente.setActivo(true);

        Paciente paciente = new Paciente(); paciente.setId(1L);
        Dentista dentista = new Dentista(); dentista.setId(1L);

        Mockito.when(citaRepo.findById(10L)).thenReturn(Optional.of(citaExistente));
        Mockito.when(pacienteRepo.existsById(1L)).thenReturn(true);
        Mockito.when(dentistaRepository.existsById(1L)).thenReturn(true);
        Mockito.when(citaRepo.existeSolapamientoDentistaExcluyendoCita(eq(1L), eq(10L), eq(nuevoInicio), eq(nuevoFin))).thenReturn(false);
        Mockito.when(citaRepo.findCitasDentistaEnRango(eq(1L), any(), any())).thenReturn(List.of(citaExistente));
        Mockito.when(citaRepo.existsOtraCitaPacienteEnRango(eq(1L), eq(10L), any(), any())).thenReturn(false);
        Mockito.when(pacienteRepo.getReferenceById(1L)).thenReturn(paciente);
        Mockito.when(dentistaRepository.getReferenceById(1L)).thenReturn(dentista);
        Mockito.when(citaRepo.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaAgendarResponse response = citaService.reagendarCita(10L, request);

        assertNotNull(response);
        assertEquals(10L, response.idCita());
        assertEquals("PENDIENTE", response.estado());
        assertEquals(nuevoInicio, citaExistente.getFechaHoraInicio());
        assertEquals(nuevoFin, citaExistente.getFechaHoraFin());
        Mockito.verify(agendaEventPublisher).publicarCitaConfirmada(any(CitaAgendarResponse.class));
    }

    @Test
    void reagendarCita_lanzaValidationException_siDentistaOcupadoEnNuevoHorario() {
        LocalDateTime nuevoInicio = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0);
        LocalDateTime nuevoFin = nuevoInicio.plusHours(1);
        AgendarCitaRequest request = new AgendarCitaRequest(1L, 1L, nuevoInicio, nuevoFin);

        Cita citaExistente = new Cita();
        citaExistente.setId(10L);
        citaExistente.setEstado(Estado.PENDIENTE);
        citaExistente.setActivo(true);

        Mockito.when(citaRepo.findById(10L)).thenReturn(Optional.of(citaExistente));
        Mockito.when(pacienteRepo.existsById(1L)).thenReturn(true);
        Mockito.when(dentistaRepository.existsById(1L)).thenReturn(true);
        Mockito.when(citaRepo.existeSolapamientoDentistaExcluyendoCita(eq(1L), eq(10L), eq(nuevoInicio), eq(nuevoFin))).thenReturn(true);

        assertThrows(ValidationException.class, () -> citaService.reagendarCita(10L, request));
    }

    @Test
    void reagendarCita_lanzaValidationException_siCitaEstaCanceladaOInasistencia() {
        LocalDateTime nuevoInicio = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0);
        AgendarCitaRequest request = new AgendarCitaRequest(1L, 1L, nuevoInicio, nuevoInicio.plusHours(1));

        Cita citaInasistencia = new Cita();
        citaInasistencia.setId(15L);
        citaInasistencia.setEstado(Estado.INASISTENCIA);
        citaInasistencia.setActivo(true);

        Mockito.when(citaRepo.findById(15L)).thenReturn(Optional.of(citaInasistencia));

        assertThrows(ValidationException.class, () -> citaService.reagendarCita(15L, request));
    }

    @Test
    void marcarInasistencia_cambiaEstadoYPublicaWebSocket() {
        Cita cita = new Cita();
        cita.setId(20L);
        cita.setEstado(Estado.PENDIENTE);
        cita.setActivo(true);

        Mockito.when(citaRepo.findById(20L)).thenReturn(Optional.of(cita));
        Mockito.when(citaRepo.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaAgendarResponse response = citaService.marcarInasistencia(20L);

        assertEquals(20L, response.idCita());
        assertEquals("INASISTENCIA", response.estado());
        assertEquals(Estado.INASISTENCIA, cita.getEstado());
        Mockito.verify(agendaEventPublisher).publicarCitaConfirmada(any(CitaAgendarResponse.class));
    }

    @Test
    void eliminarCitaLogica_archivaCitaYCambiaEstadoACancelada() {
        Cita cita = new Cita();
        cita.setId(25L);
        cita.setEstado(Estado.PENDIENTE);
        cita.setActivo(true);

        Mockito.when(citaRepo.findById(25L)).thenReturn(Optional.of(cita));
        Mockito.when(citaRepo.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        citaService.eliminarCitaLogica(25L);

        assertFalse(cita.getActivo());
        assertEquals(Estado.CANCELADA, cita.getEstado());
        Mockito.verify(agendaEventPublisher).publicarCitaConfirmada(any(CitaAgendarResponse.class));
    }
}

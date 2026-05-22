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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
        // Arrange
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

        // Act
        CitaAgendarResponse response = citaService.agendarCita(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.idCita());
        assertEquals("PENDIENTE", response.estado());
        Mockito.verify(agendaEventPublisher).publicarCitaConfirmada(any(CitaAgendarResponse.class));
    }

    @Test
    void agendarCita_lanzaValidationException_siNoExistePaciente() {
        // Arrange
        AgendarCitaRequest request = new AgendarCitaRequest(99L, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        Mockito.when(pacienteRepo.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ValidationException.class, () -> citaService.agendarCita(request));
    }
}

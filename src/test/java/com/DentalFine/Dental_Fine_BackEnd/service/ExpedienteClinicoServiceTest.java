package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.EvolucionAgregarRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.EvolucionTratamientoDTO;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.Dentista;
import com.DentalFine.Dental_Fine_BackEnd.models.EvolucionTratamiento;
import com.DentalFine.Dental_Fine_BackEnd.models.ExpedienteClinico;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.DentistaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.EvolucionTratamientoRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.ExpedienteClinicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpedienteClinicoServiceTest {

    @Mock
    private ExpedienteClinicoRepository expedienteRepository;

    @Mock
    private EvolucionTratamientoRepository evolucionRepository;

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private DentistaRepository dentistaRepository;

    @InjectMocks
    private ExpedienteClinicoService expedienteService;

    private ExpedienteClinico expediente;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Carlos");
        paciente.setApellidos("Moreno");

        expediente = new ExpedienteClinico();
        expediente.setId(10L);
        expediente.setPaciente(paciente);
    }

    @Test
    @DisplayName("DFI-33: Debe agregar evolución clínica completa con pieza dental, procedimiento y dentista")
    void debeAgregarEvolucionCompleta() {
        Dentista dentista = new Dentista();
        dentista.setId(5L);
        dentista.setNombre("Dra. Anel Gómez");

        Cita cita = new Cita();
        cita.setId(100L);

        when(expedienteRepository.findById(10L)).thenReturn(Optional.of(expediente));
        when(dentistaRepository.findById(5L)).thenReturn(Optional.of(dentista));
        when(citaRepository.findById(100L)).thenReturn(Optional.of(cita));

        when(evolucionRepository.save(any(EvolucionTratamiento.class))).thenAnswer(invocation -> {
            EvolucionTratamiento ev = invocation.getArgument(0);
            ev.setId(1L);
            return ev;
        });

        EvolucionAgregarRequest request = new EvolucionAgregarRequest(
                100L,
                5L,
                "Se realizó obturación estética clase II con resina compuesta.",
                "16",
                "Obturación con Resina",
                LocalDate.of(2026, 11, 22)
        );

        EvolucionTratamientoDTO resultado = expedienteService.agregarEvolucion(10L, request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(10L, resultado.expedienteClinicoId());
        assertEquals(100L, resultado.citaId());
        assertEquals(5L, resultado.dentistaId());
        assertEquals("Dra. Anel Gómez", resultado.dentistaNombre());
        assertEquals("16", resultado.piezaDental());
        assertEquals("Obturación con Resina", resultado.procedimiento());
        assertEquals("Se realizó obturación estética clase II con resina compuesta.", resultado.notasClinicas());
        assertEquals(LocalDate.of(2026, 11, 22), resultado.fechaRegistro());

        verify(evolucionRepository).save(any(EvolucionTratamiento.class));
    }

    @Test
    @DisplayName("DFI-33: Debe agregar evolución clínica independiente sin requerir citaId ni dentistaId obligatorios")
    void debeAgregarEvolucionIndependienteSinCita() {
        when(expedienteRepository.findById(10L)).thenReturn(Optional.of(expediente));
        when(evolucionRepository.save(any(EvolucionTratamiento.class))).thenAnswer(invocation -> {
            EvolucionTratamiento ev = invocation.getArgument(0);
            ev.setId(2L);
            return ev;
        });

        EvolucionAgregarRequest request = new EvolucionAgregarRequest(
                null,
                null,
                "Paciente refiere ligera sensibilidad en zona molar. Se prescribe enjuague con clorhexidina.",
                "General",
                "Valoración y Diagnóstico",
                null
        );

        EvolucionTratamientoDTO resultado = expedienteService.agregarEvolucion(10L, request);

        assertNotNull(resultado);
        assertEquals(2L, resultado.id());
        assertEquals(10L, resultado.expedienteClinicoId());
        assertNull(resultado.citaId());
        assertNull(resultado.dentistaId());
        assertNull(resultado.dentistaNombre());
        assertEquals("General", resultado.piezaDental());
        assertEquals("Valoración y Diagnóstico", resultado.procedimiento());
        assertEquals(LocalDate.now(), resultado.fechaRegistro());

        verify(citaRepository, never()).findById(anyLong());
        verify(dentistaRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("DFI-33: Debe lanzar excepción si el expediente no existe al registrar evolución")
    void debeLanzarExcepcionSiExpedienteNoExiste() {
        when(expedienteRepository.findById(999L)).thenReturn(Optional.empty());

        EvolucionAgregarRequest request = new EvolucionAgregarRequest(
                null,
                null,
                "Nota clínica de prueba",
                "11",
                "Revisión",
                null
        );

        assertThrows(IllegalArgumentException.class, () ->
                expedienteService.agregarEvolucion(999L, request)
        );
    }

    @Test
    @DisplayName("DFI-33: Debe listar todas las evoluciones del expediente ordenadas cronológicamente descendente")
    void debeListarEvolucionesOrdenadas() {
        when(expedienteRepository.existsById(10L)).thenReturn(true);

        EvolucionTratamiento ev1 = new EvolucionTratamiento();
        ev1.setId(201L);
        ev1.setExpedienteClinico(expediente);
        ev1.setNotasClinicas("Segunda sesión");
        ev1.setPiezaDental("21");
        ev1.setProcedimiento("Endodoncia");
        ev1.setFechaRegistro(LocalDate.of(2026, 11, 22));

        EvolucionTratamiento ev2 = new EvolucionTratamiento();
        ev2.setId(200L);
        ev2.setExpedienteClinico(expediente);
        ev2.setNotasClinicas("Primera sesión");
        ev2.setPiezaDental("21");
        ev2.setProcedimiento("Apertura cameral");
        ev2.setFechaRegistro(LocalDate.of(2026, 11, 15));

        when(evolucionRepository.findByExpedienteClinicoIdOrderByFechaRegistroDescIdDesc(10L))
                .thenReturn(List.of(ev1, ev2));

        List<EvolucionTratamientoDTO> lista = expedienteService.listarEvoluciones(10L);

        assertEquals(2, lista.size());
        assertEquals(201L, lista.get(0).id());
        assertEquals("Segunda sesión", lista.get(0).notasClinicas());
        assertEquals(200L, lista.get(1).id());
        assertEquals("Primera sesión", lista.get(1).notasClinicas());
    }
}

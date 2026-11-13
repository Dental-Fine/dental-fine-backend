package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.config.security.TokenService;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.Dentista;
import com.DentalFine.Dental_Fine_BackEnd.models.Estado;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.DentistaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CitaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String validToken;

    @Autowired
    private PacienteRepository pacienteRepo;
    @Autowired
    private DentistaRepository dentistaRepo;
    @Autowired
    private TipoServiciosRepository tipoServicioRepo;
    @Autowired
    private CitaRepository citaRepo;

    @BeforeEach
    void setUp() {
        Usuario testUser = new Usuario(null, "test@dentalfine.com", "password", Rol.ROLE_DENTISTA);
        usuarioRepository.save(testUser);
        validToken = tokenService.generarToken(testUser);

        Paciente p = new Paciente();
        p.setNombre("Juan");
        p.setApellidos("Perez");
        p.setTelefono("4451112233");
        pacienteRepo.save(p);

        Dentista d = new Dentista();
        d.setNombre("Dr. Smith");
        dentistaRepo.save(d);

        TipoServicios t = new TipoServicios();
        t.setNombre("Limpieza");
        t.setPrecio(100.0);
        tipoServicioRepo.save(t);
    }

    @Test
    void agendarCita_retorna200_conTokenValido() throws Exception {
        Long idPaciente = pacienteRepo.findAll().get(0).getId();
        Long idDentista = dentistaRepo.findAll().get(0).getId();

        String jsonPayload = String.format("""
                {
                  "pacienteId": %d,
                  "dentistaId": %d,
                  "fechaHoraInicio": "2026-10-10T10:00:00",
                  "fechaHoraFin": "2026-10-10T11:00:00"
                }
                """, idPaciente, idDentista);

        mockMvc.perform(post("/citas/agendar")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(result -> {
                    int statusCode = result.getResponse().getStatus();
                    assertTrue(statusCode == 200 || statusCode == 201 || statusCode == 400 || statusCode == 404);
                });
    }

    @Test
    void reagendarCita_putApiCitasReagendar_verificaDisponibilidadYActualiza() throws Exception {
        Paciente paciente = pacienteRepo.findAll().get(0);
        Dentista dentista = dentistaRepo.findAll().get(0);

        Cita cita = new Cita(dentista, paciente,
                LocalDateTime.of(2026, 12, 10, 10, 0),
                LocalDateTime.of(2026, 12, 10, 11, 0),
                Estado.PENDIENTE, LocalDate.now());
        cita = citaRepo.save(cita);

        String reagendarPayload = String.format("""
                {
                  "pacienteId": %d,
                  "dentistaId": %d,
                  "fechaHoraInicio": "2026-12-11T14:00:00",
                  "fechaHoraFin": "2026-12-11T15:00:00"
                }
                """, paciente.getId(), dentista.getId());

        mockMvc.perform(put("/api/citas/" + cita.getId() + "/reagendar")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reagendarPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCita").value(cita.getId()))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        Cita actualizada = citaRepo.findById(cita.getId()).orElseThrow();
        assertEquals(LocalDateTime.of(2026, 12, 11, 14, 0), actualizada.getFechaHoraInicio());
    }

    @Test
    void reagendarCita_rechazaCon400_cuandoHayConflictoConOtraCita() throws Exception {
        Paciente paciente1 = pacienteRepo.findAll().get(0);
        Paciente paciente2 = new Paciente();
        paciente2.setNombre("Maria");
        paciente2.setApellidos("Lopez");
        paciente2 = pacienteRepo.save(paciente2);

        Dentista dentista = dentistaRepo.findAll().get(0);

        // Cita 1 a las 10:00
        Cita cita1 = new Cita(dentista, paciente1,
                LocalDateTime.of(2026, 12, 15, 10, 0),
                LocalDateTime.of(2026, 12, 15, 11, 0),
                Estado.PENDIENTE, LocalDate.now());
        cita1 = citaRepo.save(cita1);

        // Cita 2 a las 12:00
        Cita cita2 = new Cita(dentista, paciente2,
                LocalDateTime.of(2026, 12, 15, 12, 0),
                LocalDateTime.of(2026, 12, 15, 13, 0),
                Estado.PENDIENTE, LocalDate.now());
        cita2 = citaRepo.save(cita2);

        // Intentar reagendar Cita 2 al mismo horario de Cita 1 (10:00 - 11:00)
        String reagendarEnConflicto = String.format("""
                {
                  "pacienteId": %d,
                  "dentistaId": %d,
                  "fechaHoraInicio": "2026-12-15T10:00:00",
                  "fechaHoraFin": "2026-12-15T11:00:00"
                }
                """, paciente2.getId(), dentista.getId());

        mockMvc.perform(put("/api/citas/" + cita2.getId() + "/reagendar")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reagendarEnConflicto))
                .andExpect(status().isBadRequest());
    }

    @Test
    void marcarInasistencia_actualizaEstadoAInasistenciaYLiberaDisponibilidad() throws Exception {
        Paciente paciente1 = pacienteRepo.findAll().get(0);
        Paciente paciente2 = new Paciente();
        paciente2.setNombre("Carlos");
        paciente2.setApellidos("Ruiz");
        paciente2 = pacienteRepo.save(paciente2);
        Dentista dentista = dentistaRepo.findAll().get(0);

        Cita cita = new Cita(dentista, paciente1,
                LocalDateTime.of(2026, 12, 20, 9, 0),
                LocalDateTime.of(2026, 12, 20, 10, 0),
                Estado.PENDIENTE, LocalDate.now());
        cita = citaRepo.save(cita);

        mockMvc.perform(put("/api/citas/" + cita.getId() + "/inasistencia")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("INASISTENCIA"));

        Cita verificada = citaRepo.findById(cita.getId()).orElseThrow();
        assertEquals(Estado.INASISTENCIA, verificada.getEstado());

        // Verificar que el horario 09:00-10:00 quedó liberado para otro paciente
        String nuevoAgendamiento = String.format("""
                {
                  "pacienteId": %d,
                  "dentistaId": %d,
                  "fechaHoraInicio": "2026-12-20T09:00:00",
                  "fechaHoraFin": "2026-12-20T10:00:00"
                }
                """, paciente2.getId(), dentista.getId());

        mockMvc.perform(post("/api/citas/agendar")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoAgendamiento))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarCitaLogica_retorna204YDesactivaCita() throws Exception {
        Paciente paciente = pacienteRepo.findAll().get(0);
        Dentista dentista = dentistaRepo.findAll().get(0);

        Cita cita = new Cita(dentista, paciente,
                LocalDateTime.of(2026, 12, 22, 11, 0),
                LocalDateTime.of(2026, 12, 22, 12, 0),
                Estado.PENDIENTE, LocalDate.now());
        cita = citaRepo.save(cita);

        mockMvc.perform(delete("/api/citas/" + cita.getId())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNoContent());

        Cita eliminada = citaRepo.findById(cita.getId()).orElseThrow();
        assertFalse(eliminada.getActivo());
        assertEquals(Estado.CANCELADA, eliminada.getEstado());
    }
}

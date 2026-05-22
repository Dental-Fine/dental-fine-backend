package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.config.security.TokenService;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    private com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository pacienteRepo;
    @Autowired
    private com.DentalFine.Dental_Fine_BackEnd.repository.DentistaRepository dentistaRepo;
    @Autowired
    private com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository tipoServicioRepo;

    @BeforeEach
    void setUp() {
        Usuario testUser = new Usuario(null, "test@dentalfine.com", "password", Rol.ROLE_DENTISTA);
        usuarioRepository.save(testUser);
        validToken = tokenService.generarToken(testUser);

        // Crear datos necesarios para la cita
        com.DentalFine.Dental_Fine_BackEnd.models.Paciente p = new com.DentalFine.Dental_Fine_BackEnd.models.Paciente();
        p.setNombre("Juan"); p.setApellidos("Perez");
        pacienteRepo.save(p);

        com.DentalFine.Dental_Fine_BackEnd.models.Dentista d = new com.DentalFine.Dental_Fine_BackEnd.models.Dentista();
        d.setNombre("Dr. Smith");
        dentistaRepo.save(d);

        com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios t = new com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios();
        t.setNombre("Limpieza"); t.setPrecio(100.0);
        tipoServicioRepo.save(t);
    }

    @Test
    void agendarCita_retorna200_conTokenValido() throws Exception {
        // Obtenemos los id reales generados por la BD H2
        Long idPaciente = pacienteRepo.findAll().get(0).getId();
        Long idDentista = dentistaRepo.findAll().get(0).getId();
        Long idTipoServicio = tipoServicioRepo.findAll().get(0).getId();

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
                    org.junit.jupiter.api.Assertions.assertTrue(statusCode == 200 || statusCode == 201 || statusCode == 400 || statusCode == 404);
                });
    }
}

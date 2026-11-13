package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.config.security.TokenService;
import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.PacienteRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PacienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepo;

    private String validToken;
    private Paciente pacienteActivo;

    @BeforeEach
    void setUp() {
        Usuario testUser = new Usuario(null, "recepcion@dentalfine.com", "password", Rol.ROLE_RECEPCIONISTA);
        usuarioRepository.save(testUser);
        validToken = tokenService.generarToken(testUser);

        Paciente p = new Paciente();
        p.setNombre("Mariana");
        p.setApellidos("Garza");
        p.setTelefono("4459876543");
        p.setCorreo("mariana@correo.com");
        p.setActivo(true);
        pacienteActivo = pacienteRepo.save(p);
    }

    @Test
    void buscarPacientes_porParametroGeneralQ_encuentraPorNombreOTelefono() throws Exception {
        mockMvc.perform(get("/api/pacientes/buscar")
                        .param("q", "Mariana")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$[0].nombre").value("Mariana"))
                .andExpect(jsonPath("$[0].telefono").value("4459876543"));

        mockMvc.perform(get("/api/pacientes/buscar")
                        .param("q", "9876543")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(pacienteActivo.getId()));
    }

    @Test
    void buscarPacientes_porParametrosEspecificosNombreYTelefono() throws Exception {
        mockMvc.perform(get("/api/pacientes/buscar")
                        .param("nombre", "Garza")
                        .param("telefono", "445987")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$[0].apellidos").value("Garza"));
    }

    @Test
    void obtenerPacientePorId_retorna200SiActivo_y404SiEliminadoLogicamente() throws Exception {
        mockMvc.perform(get("/api/pacientes/" + pacienteActivo.getId())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$.nombre").value("Mariana"));

        // Eliminar lógicamente
        mockMvc.perform(delete("/api/pacientes/" + pacienteActivo.getId())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNoContent());

        // Verificar que la consulta por ID devuelve 404 tras borrado lógico
        mockMvc.perform(get("/api/pacientes/" + pacienteActivo.getId())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNotFound());
    }
}

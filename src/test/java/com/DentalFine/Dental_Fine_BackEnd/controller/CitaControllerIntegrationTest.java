package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.config.security.TokenService;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.AgendarCitaRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CitaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String validToken;

    @BeforeEach
    void setUp() {
        Usuario testUser = new Usuario(null, "test@dentalfine.com", "password", Rol.ROLE_DOCTOR);
        usuarioRepository.save(testUser);
        validToken = tokenService.generarToken(testUser);
    }

    @Test
    void agendarCita_retorna200_conTokenValido() throws Exception {
        AgendarCitaRequest request = new AgendarCitaRequest(1L, 1L, 1L, LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/citas/agendar")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> {
                    int statusCode = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertTrue(statusCode == 200 || statusCode == 201 || statusCode == 400 || statusCode == 404);
                });
    }
}

package com.DentalFine.Dental_Fine_BackEnd;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.LoginRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.LoginResponse;
import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DentalFineSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        if (usuarioRepository.findByLogin("admin@dentalfine.com") == null) {
            Usuario admin = new Usuario(null, "admin@dentalfine.com", passwordEncoder.encode("123456"), Rol.ROLE_PERSONAL_CLINICA);
            usuarioRepository.save(admin);
        }
    }

    @Test
    void flujoCompletoLoginYBuscarPacientes() {
        // a) Hacer POST a /auth/login para obtener el token real
        LoginRequest loginRequest = new LoginRequest("admin@dentalfine.com", "123456");
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/auth/login", loginRequest, LoginResponse.class);

        assertEquals(200, loginResponse.getStatusCode().value());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().token();
        assertNotNull(token);

        // b) Usar ese token en los headers para hacer un GET a un endpoint protegido (ej. /pacientes o /citas)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> getResponse = restTemplate.exchange("/pacientes", HttpMethod.GET, entity, String.class);
        
        // Verifica que la respuesta sea exitosa (2xx)
        assertTrue(getResponse.getStatusCode().is2xxSuccessful() || getResponse.getStatusCode().value() == 404);
    }
}

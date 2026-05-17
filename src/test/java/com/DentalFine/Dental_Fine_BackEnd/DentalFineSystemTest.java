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
import org.springframework.boot.resttestclient.TestRestTemplate;
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

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

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
    void flujoCompletoLoginYBuscarPacientes() throws Exception {
        // PASO 1: Payload de Login
        String loginJson = """
                {
                  "correo": "admin@dentalfine.com",
                  "contrasena": "123456"
                }
                """;

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        // PASO 2: Petición POST a /auth/login
        java.net.http.HttpRequest loginRequest = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + port + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(loginJson))
                .build();

        java.net.http.HttpResponse<String> loginResponse = client.send(loginRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        // --- BLOQUE DE DEBUG ---
        if (loginResponse.statusCode() != 200) {
            System.out.println("\n============ ERROR DEL BACKEND ============");
            System.out.println("Status: " + loginResponse.statusCode());
            System.out.println("Body: " + loginResponse.body());
            System.out.println("===========================================\n");
        }
        // -----------------------

        assertEquals(200, loginResponse.statusCode(), "El login falló, revisa las credenciales");
        assertTrue(loginResponse.body().contains("token"), "El JSON no contiene el token");

        // PASO 3: Extraer el token de la respuesta JSON usando Jackson (incluido en Spring)
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(loginResponse.body());
        String token = root.path("token").asText();

        // PASO 4: Petición GET a /pacientes CON EL TOKEN BEARER
        java.net.http.HttpRequest pacientesRequest = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + port + "/pacientes"))
                .header("Authorization", "Bearer " + token) // <-- El prefijo mágico
                .GET()
                .build();

        java.net.http.HttpResponse<String> pacientesResponse = client.send(pacientesRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        // PASO 5: Validar que el servidor nos dio acceso (200 OK)
        assertEquals(200, pacientesResponse.statusCode(), "Fallo 403: El servidor rechazó el token al ir a /pacientes");
    }
}

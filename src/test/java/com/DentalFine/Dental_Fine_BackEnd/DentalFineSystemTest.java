package com.DentalFine.Dental_Fine_BackEnd;

import com.DentalFine.Dental_Fine_BackEnd.models.Rol;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import com.DentalFine.Dental_Fine_BackEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DentalFineSystemTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Garantizamos que la base de datos de prueba tenga al usuario antes del Login
        usuarioRepository.deleteAll();
        Usuario admin = new Usuario(null, "admin@dentalfine.com", passwordEncoder.encode("123456"), Rol.ROLE_RECEPCIONISTA);
        usuarioRepository.save(admin);
    }

    @Test
    void flujoCompletoLoginYBuscarPacientes() throws Exception {
        // 1. Payload de Login
        String loginJson = """
                {
                  "correo": "admin@dentalfine.com",
                  "contrasena": "123456"
                }
                """;

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        // 2. Petición POST a /auth/login
        java.net.http.HttpRequest loginRequest = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + port + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(loginJson))
                .build();

        java.net.http.HttpResponse<String> loginResponse = client.send(loginRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (loginResponse.statusCode() == 500) {
            System.out.println("\n>>>> EL BACKEND EXPLOTÓ POR ESTO: <<<<");
            System.out.println(loginResponse.body());
        }

        assertEquals(200, loginResponse.statusCode(), "El login fallo a pesar de inyectar el usuario en H2");
        assertTrue(loginResponse.body().contains("token"), "No se devolvió el token");

        // 3. Extraer el token
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String token = mapper.readTree(loginResponse.body()).path("token").asText();

        // 4. Petición GET a /pacientes con el Token
        java.net.http.HttpRequest pacientesRequest = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + port + "/pacientes"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> pacientesResponse = client.send(pacientesRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        // 5. Validar acceso a ruta protegida
        assertEquals(200, pacientesResponse.statusCode(), "Acceso denegado a /pacientes");
    }
}
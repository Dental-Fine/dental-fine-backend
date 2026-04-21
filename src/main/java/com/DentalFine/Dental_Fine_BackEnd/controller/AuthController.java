package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.LoginRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.LoginResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.UsuarioLoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    /**
     * Contrato: POST /api/auth/login → MVP sin prefijo: POST /auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse body = new LoginResponse(
                "mock-jwt-token-123",
                new UsuarioLoginResponse(1L, "PERSONAL_CLINICA")
        );
        return ResponseEntity.ok(body);
    }
}

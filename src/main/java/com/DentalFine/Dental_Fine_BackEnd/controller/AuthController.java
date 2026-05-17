package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.config.security.TokenService;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.LoginRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.LoginResponse;
import com.DentalFine.Dental_Fine_BackEnd.dto.responses.UsuarioLoginResponse;
import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena());
        var usuarioAutenticado = authenticationManager.authenticate(authenticationToken);

        var usuario = (Usuario) usuarioAutenticado.getPrincipal();
        var jwtToken = tokenService.generarToken(usuario);

        LoginResponse body = new LoginResponse(
                jwtToken,
                new UsuarioLoginResponse(usuario.getId(), usuario.getRol().name())
        );
        return ResponseEntity.ok(body);
    }
}

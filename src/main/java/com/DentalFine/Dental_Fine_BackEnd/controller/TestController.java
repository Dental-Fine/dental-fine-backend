package com.DentalFine.Dental_Fine_BackEnd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String probarConexion() {
        return "Hola, esta funcionando correctamente el cors y spring security";
    }
}

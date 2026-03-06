package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Personal")
public class PersonalClinicaController {
    @GetMapping("/saludo")
    public String saludo() {
        return "ola";
    }
}

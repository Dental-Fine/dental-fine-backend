package com.DentalFine.Dental_Fine_BackEnd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Async
    public void enviarConfirmacionCita(String destinatario,String nombrePaciente,String doctor,String fechaHora){

    }
}

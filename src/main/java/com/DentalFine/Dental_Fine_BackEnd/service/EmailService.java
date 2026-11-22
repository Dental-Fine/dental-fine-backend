package com.DentalFine.Dental_Fine_BackEnd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Async
    public void enviarConfirmacionCita(String destinatario, String nombrePaciente, String doctor, String fechaHora) {
        System.out.println("[ASYNC - Hilo: " + Thread.currentThread().getName() + "] Preparando notificación para: " + destinatario);

        String asunto = "Confirmación de Cita - Dental Fine";
        String cuerpo = "Hola " + nombrePaciente + ",\n\n"
                + "Tu cita ha sido agendada con éxito en Dental Fine.\n\n"
                + "Detalles de tu cita:\n"
                + "• Especialista: " + doctor + "\n"
                + "• Fecha y Hora: " + fechaHora + "\n\n"
                + "Puedes consultar los detalles desde tu Portal de Pacientes.\n\n"
                + "¡Te esperamos!\n"
                + "Equipo de Dental Fine";

        if (mailSender != null && destinatario != null && !destinatario.isBlank()) {
            try {
                SimpleMailMessage mensaje = new SimpleMailMessage();
                mensaje.setTo(destinatario);
                mensaje.setSubject(asunto);
                mensaje.setText(cuerpo);
                mailSender.send(mensaje);
                System.out.println("[ASYNC - Hilo: " + Thread.currentThread().getName() + "] ¡Correo enviado exitosamente a: " + destinatario + "!");
            } catch (Exception e) {
                System.err.println("[ASYNC ERROR] No se pudo enviar por SMTP (" + e.getMessage() + "), pero la cita quedó agendada.");
            }
        } else {
            System.out.println("[ASYNC MOCK] Notificación simulada (sin SMTP configurado):\n" + cuerpo);
        }
    }
}

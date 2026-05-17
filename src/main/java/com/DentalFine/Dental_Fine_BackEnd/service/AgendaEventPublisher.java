package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.responses.CitaAgendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendaEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publicarCitaConfirmada(CitaAgendarResponse payload) {
        messagingTemplate.convertAndSend("/topic/agenda", payload);
    }
}

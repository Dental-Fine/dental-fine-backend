package com.DentalFine.Dental_Fine_BackEnd.controller;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.TicketGenerarRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Ticket;
import com.DentalFine.Dental_Fine_BackEnd.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets/cita/{citaId}")
    public ResponseEntity<Ticket> generarTicket(@PathVariable Long citaId, @RequestBody TicketGenerarRequest body) {
        return ResponseEntity.ok(ticketService.generarTicket(citaId, body));
    }
}

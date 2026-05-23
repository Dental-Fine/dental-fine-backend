package com.DentalFine.Dental_Fine_BackEnd.service;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.DetalleTicketRequest;
import com.DentalFine.Dental_Fine_BackEnd.dto.requests.TicketGenerarRequest;
import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import com.DentalFine.Dental_Fine_BackEnd.models.DetalleTicket;
import com.DentalFine.Dental_Fine_BackEnd.models.ExpedienteClinico;
import com.DentalFine.Dental_Fine_BackEnd.models.Ticket;
import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import com.DentalFine.Dental_Fine_BackEnd.repository.CitaRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.DetalleTicketRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.ExpedienteClinicoRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.TicketRepository;
import com.DentalFine.Dental_Fine_BackEnd.repository.TipoServiciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private DetalleTicketRepository detalleTicketRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private TipoServiciosRepository tipoServiciosRepository;
    @Autowired
    private ExpedienteClinicoRepository expedienteClinicoRepository;

    @Transactional
    public Ticket generarTicket(Long citaId, TicketGenerarRequest request) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        ExpedienteClinico expediente = expedienteClinicoRepository.findByPacienteId(cita.getPaciente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado para este paciente"));

        Ticket ticket = new Ticket();
        ticket.setCita(cita);
        ticket.setExpedienteClinico(expediente);
        ticket.setEstadoPago("PENDIENTE");
        ticket.setDetalles(new ArrayList<>());

        BigDecimal total = BigDecimal.ZERO;

        ticket = ticketRepository.save(ticket); // save to get ID

        for (DetalleTicketRequest detReq : request.detalles()) {
            TipoServicios tipo = tipoServiciosRepository.findById(detReq.tipoServicioId())
                    .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));

            DetalleTicket detalle = new DetalleTicket();
            detalle.setTicket(ticket);
            detalle.setTipoServicio(tipo);
            detalle.setCantidad(detReq.cantidad());
            BigDecimal precio = BigDecimal.valueOf(tipo.getPrecio());
            detalle.setPrecioUnitario(precio);
            
            BigDecimal subtotal = precio.multiply(new BigDecimal(detReq.cantidad()));
            detalle.setSubtotal(subtotal);

            detalleTicketRepository.save(detalle);

            ticket.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        ticket.setTotal(total);
        return ticketRepository.save(ticket);
    }
}

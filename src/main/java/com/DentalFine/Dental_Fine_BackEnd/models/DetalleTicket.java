package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ToString.Exclude
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "tipo_servicio_id")
    private TipoServicios tipoServicio;

    private Integer cantidad;

    /**
     * Precio unitario en MXN al momento de generar el ticket.
     */
    private BigDecimal precioUnitario;

    /**
     * Subtotal en MXN (cantidad * precioUnitario).
     */
    private BigDecimal subtotal;
}

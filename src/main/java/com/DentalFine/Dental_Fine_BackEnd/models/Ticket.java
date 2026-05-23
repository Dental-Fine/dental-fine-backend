package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ToString.Exclude
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "expediente_clinico_id")
    private ExpedienteClinico expedienteClinico;

    @ToString.Exclude
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<DetalleTicket> detalles;

    /**
     * Total acumulado en MXN.
     */
    private BigDecimal total;

    private String estadoPago; // PENDIENTE, PAGADO
}

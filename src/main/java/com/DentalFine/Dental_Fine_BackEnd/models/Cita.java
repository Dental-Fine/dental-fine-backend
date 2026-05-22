package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @ToString.Exclude
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDate fechaCreacion;

    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private Ticket ticket;

    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private EvolucionTratamiento evolucionTratamiento;

    public Cita(Dentista dentista, Paciente paciente, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Estado estado, LocalDate fechaCreacion) {
        this.dentista = dentista;
        this.paciente = paciente;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public void actualizarEstado(Estado estado) {
        this.estado = estado;
    }
}

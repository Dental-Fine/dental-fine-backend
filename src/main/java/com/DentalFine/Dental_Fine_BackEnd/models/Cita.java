package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private LocalDateTime fecha;
    private String nombre;
    private float monto;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDate fechaCreacion;

    public Cita(Dentista dentista, Paciente paciente, LocalDateTime fecha, Estado estado, LocalDate fechaCreacion) {
        this.dentista = dentista;
        this.paciente = paciente;
        this.fecha = fecha;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public void actualizarEstado(Estado estado) {
        this.estado = estado;
    }
}

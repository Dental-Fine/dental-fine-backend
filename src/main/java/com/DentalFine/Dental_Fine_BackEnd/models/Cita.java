package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long idDentista;
    private Long idPaciente;
    private LocalDateTime fecha;
    private String nombre;
    private float monto;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDate fechaCreacion;

    public Cita(Long idDentista, Long idPaciente, LocalDateTime fecha, Estado estado, LocalDate fechaCreacion) {
        this.idDentista = idDentista;
        this.idPaciente = idPaciente;
        this.fecha = fecha;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public void actualizarEstado(Estado estado) {
        this.estado = estado;
    }
}

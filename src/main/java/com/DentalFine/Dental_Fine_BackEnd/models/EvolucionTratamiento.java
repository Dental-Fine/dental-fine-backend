package com.DentalFine.Dental_Fine_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvolucionTratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notasClinicas;
    private LocalDate fechaRegistro;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "expediente_clinico_id")
    private ExpedienteClinico expedienteClinico;

    @OneToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;
}

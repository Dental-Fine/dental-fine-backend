package com.DentalFine.Dental_Fine_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String piezaDental;
    private String procedimiento;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "dentista_id")
    private Dentista dentista;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "expediente_clinico_id")
    private ExpedienteClinico expedienteClinico;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;
}

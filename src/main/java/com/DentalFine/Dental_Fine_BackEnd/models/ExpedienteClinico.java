package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpedienteClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alergias;
    private String enfermedadesCronicas;
    private LocalDate fechaCreacion;

    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToOne(mappedBy = "expedienteClinico", cascade = CascadeType.ALL)
    private Odontograma odontograma;

    @OneToMany(mappedBy = "expedienteClinico", cascade = CascadeType.ALL)
    private List<EvolucionTratamiento> evoluciones;
}

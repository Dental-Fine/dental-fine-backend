package com.DentalFine.Dental_Fine_BackEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoServicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    /**
     * Precio del servicio en {@link com.DentalFine.Dental_Fine_BackEnd.dto.MonedaCodigo#MXN} (peso mexicano).
     */
    private Double precio;

    private Double duracion;
}

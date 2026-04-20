package com.DentalFine.Dental_Fine_BackEnd.models;

import dtos.paciente.DatosRegistrarPaciente;
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
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String telefono;
    private String correo;

    public Paciente(DatosRegistrarPaciente datos) {
        this.nombre = datos.nombre();
        this.telefono = datos.telefono();
        this.correo = datos.correo();
    }
}

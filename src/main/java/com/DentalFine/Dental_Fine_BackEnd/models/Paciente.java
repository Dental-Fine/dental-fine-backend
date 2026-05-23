package com.DentalFine.Dental_Fine_BackEnd.models;

import com.DentalFine.Dental_Fine_BackEnd.dto.requests.RegistrarPacienteRequest;
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
    private String apellidos;
    private String telefono;
    private String correo;

    @jakarta.persistence.Column(columnDefinition = "boolean default true")
    private Boolean activo = true;

    @jakarta.persistence.OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @jakarta.persistence.JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public Paciente(RegistrarPacienteRequest datos) {
        this.nombre = datos.nombre();
        this.apellidos = datos.apellidos() != null ? datos.apellidos() : "";
        this.telefono = datos.telefono();
        this.correo = datos.correo();
    }
}

package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record RegistrarPacienteRequest(
        @JsonProperty("nombre") String nombre,
        @JsonProperty("apellidos") String apellidos,
        @JsonProperty("telefono") String telefono,
        @JsonProperty("correo") String correo,
        @JsonProperty("fechaNacimiento") LocalDate fechaNacimiento,
        @JsonProperty("sexo") String sexo,
        @JsonProperty("direccion") String direccion,
        @JsonProperty("contactoEmergenciaNombre") String contactoEmergenciaNombre,
        @JsonProperty("contactoEmergenciaTelefono") String contactoEmergenciaTelefono
) {
    public RegistrarPacienteRequest(String nombre, String apellidos, String telefono, String correo) {
        this(nombre, apellidos, telefono, correo, null, null, null, null, null);
    }
}

package models;

import dtos.paciente.DatosRegistrarPaciente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {
    private long id;
    private String nombre;
    private String telefono;
    private String correo;

    public Paciente(DatosRegistrarPaciente datos){
        this.nombre = datos.nombre();
        this.telefono = datos.telefono();
        this.correo = datos.telefono();
    }
}

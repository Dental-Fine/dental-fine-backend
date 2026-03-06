package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalClinica {
    private String nombre;
    private String telefono;
    private String correo;
    private Rol rol;
}

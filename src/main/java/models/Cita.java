package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cita {
    private long id;
    private long idDentista;
    private long idPaciente;

    private LocalDateTime fecha;
    private Estado estado;
    private LocalDate fechaCreacion;

    // en lo mientras para el dinero
    private float monto;

    public Cita(long idDentista, long idPaciente, LocalDateTime fecha, Estado estado, LocalDate fechaCreacion) {
        this.idDentista = idDentista;
        this.idPaciente = idPaciente;
        this.fecha = fecha;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public void actualizarEstado(Estado estado){
        this.estado = switch (estado) {
            case CANCELADA -> Estado.CANCELADA;
            case INASISTENCIA -> Estado.INASISTENCIA;
            case FINALIZADA -> Estado.CANCELADA;
            case ACTIVA -> Estado.ACTIVA;
            case PENDIENTE -> Estado.PENDIENTE;
        };
    }
}

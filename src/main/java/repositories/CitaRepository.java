package repositories;

import models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    @Query(value = """
            Consulta que checa en la db alguna coincidencia con un dia de anticipación jeje
            """)
    boolean citaConAnticipacion(LocalDateTime fecha);
}

package respository;

import models.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRespository extends JpaRepository<Dentista, Long> {
}

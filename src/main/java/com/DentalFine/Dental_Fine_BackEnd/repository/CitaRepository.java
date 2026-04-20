package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM cita c WHERE DATE(c.fecha) = DATE(:fecha))", nativeQuery = true)
    boolean citaConAnticipacion(LocalDateTime fecha);
}

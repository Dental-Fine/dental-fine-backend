package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.EvolucionTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvolucionTratamientoRepository extends JpaRepository<EvolucionTratamiento, Long> {
    List<EvolucionTratamiento> findByExpedienteClinicoIdOrderByFechaRegistroDescIdDesc(Long expedienteClinicoId);
    List<EvolucionTratamiento> findByExpedienteClinicoId(Long expedienteClinicoId);
}

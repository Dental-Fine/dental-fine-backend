package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("""
            SELECT p FROM Paciente p
            WHERE (LOWER(CONCAT(COALESCE(p.nombre, ''), ' ', COALESCE(p.apellidos, ''))) LIKE LOWER(:like)
               OR (p.telefono IS NOT NULL AND p.telefono LIKE :like))
               AND p.activo = true
            """)
    List<Paciente> buscarPorNombreApellidosOTelefono(@Param("like") String like);

    List<Paciente> findByActivoTrue();
}

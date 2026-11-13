package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("""
            SELECT p FROM Paciente p
            WHERE (LOWER(CONCAT(COALESCE(p.nombre, ''), ' ', COALESCE(p.apellidos, ''))) LIKE LOWER(:like)
               OR (p.telefono IS NOT NULL AND p.telefono LIKE :like))
               AND (p.activo IS NULL OR p.activo = true)
            """)
    List<Paciente> buscarPorNombreApellidosOTelefono(@Param("like") String like);

    @Query("""
            SELECT p FROM Paciente p
            WHERE (:nombre IS NULL OR LOWER(CONCAT(COALESCE(p.nombre, ''), ' ', COALESCE(p.apellidos, ''))) LIKE LOWER(:nombre))
              AND (:telefono IS NULL OR (p.telefono IS NOT NULL AND p.telefono LIKE :telefono))
              AND (p.activo IS NULL OR p.activo = true)
            """)
    List<Paciente> buscarPorNombreYTelefono(
            @Param("nombre") String nombre,
            @Param("telefono") String telefono);

    List<Paciente> findByActivoTrue();

    Optional<Paciente> findByIdAndActivoTrue(Long id);
}

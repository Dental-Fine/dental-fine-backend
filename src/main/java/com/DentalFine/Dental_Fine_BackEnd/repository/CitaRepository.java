package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("""
            select case when count(c) > 0 then true else false end
            from Cita c
            where c.paciente.id = :pacienteId
              and c.fecha >= :inicio
              and c.fecha < :finExclusivo
            """)
    boolean existsCitaPacienteEnRango(
            @Param("pacienteId") Long pacienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("finExclusivo") LocalDateTime finExclusivo
    );

    @Query("""
            select c from Cita c
            where c.dentista.id = :dentistaId
              and c.fecha >= :inicio
              and c.fecha < :finExclusivo
            order by c.fecha
            """)
    List<Cita> findCitasDentistaEnRango(
            @Param("dentistaId") Long dentistaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("finExclusivo") LocalDateTime finExclusivo
    );
}

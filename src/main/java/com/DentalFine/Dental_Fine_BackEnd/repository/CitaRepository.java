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
                          and c.fechaHoraInicio >= :inicio
                          and c.fechaHoraInicio < :finExclusivo
                          and c.estado != 'CANCELADA'
                          and c.estado != 'INASISTENCIA'
                          and (c.activo is null or c.activo = true)
                        """)
        boolean existsCitaPacienteEnRango(
                        @Param("pacienteId") Long pacienteId,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("finExclusivo") LocalDateTime finExclusivo);

        @Query("""
                        select case when count(c) > 0 then true else false end
                        from Cita c
                        where c.paciente.id = :pacienteId
                          and c.id != :idCita
                          and c.fechaHoraInicio >= :inicio
                          and c.fechaHoraInicio < :finExclusivo
                          and c.estado != 'CANCELADA'
                          and c.estado != 'INASISTENCIA'
                          and (c.activo is null or c.activo = true)
                        """)
        boolean existsOtraCitaPacienteEnRango(
                        @Param("pacienteId") Long pacienteId,
                        @Param("idCita") Long idCita,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("finExclusivo") LocalDateTime finExclusivo);

        @Query("""
                        select c from Cita c
                        where c.dentista.id = :dentistaId
                          and c.fechaHoraInicio >= :inicio
                          and c.fechaHoraInicio < :finExclusivo
                          and c.estado != 'CANCELADA'
                          and c.estado != 'INASISTENCIA'
                          and (c.activo is null or c.activo = true)
                        order by c.fechaHoraInicio
                        """)
        List<Cita> findCitasDentistaEnRango(
                        @Param("dentistaId") Long dentistaId,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("finExclusivo") LocalDateTime finExclusivo);

        @Query("""
                        select case when count(c) > 0 then true else false end
                        from Cita c
                        where c.dentista.id = :dentistaId
                          and c.id != :idCita
                          and c.fechaHoraInicio < :fin
                          and c.fechaHoraFin > :inicio
                          and c.estado != 'CANCELADA'
                          and c.estado != 'INASISTENCIA'
                          and (c.activo is null or c.activo = true)
                        """)
        boolean existeSolapamientoDentistaExcluyendoCita(
                        @Param("dentistaId") Long dentistaId,
                        @Param("idCita") Long idCita,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin);

        List<Cita> findByActivoTrue();
}

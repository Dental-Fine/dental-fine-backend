package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.TipoServicios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoServiciosRepository extends JpaRepository<TipoServicios, Long> {
    List<TipoServicios> findByActivoTrue();
}

package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

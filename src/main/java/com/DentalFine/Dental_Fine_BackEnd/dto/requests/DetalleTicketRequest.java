package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

public record DetalleTicketRequest(
        Long tipoServicioId,
        Integer cantidad
) {
}

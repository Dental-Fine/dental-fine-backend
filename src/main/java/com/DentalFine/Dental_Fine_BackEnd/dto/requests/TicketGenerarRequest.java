package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import java.util.List;

public record TicketGenerarRequest(
        List<DetalleTicketRequest> detalles
) {
}

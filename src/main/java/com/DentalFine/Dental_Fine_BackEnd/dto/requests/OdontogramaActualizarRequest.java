package com.DentalFine.Dental_Fine_BackEnd.dto.requests;

import java.util.Map;

public record OdontogramaActualizarRequest(
        Map<String, Object> estadoDientes
) {
}

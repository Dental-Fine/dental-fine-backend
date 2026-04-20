package com.DentalFine.Dental_Fine_BackEnd.service.validations;

public class ValidationException extends RuntimeException {
    public ValidationException(String mensaje) {
        super(mensaje);
    }
}

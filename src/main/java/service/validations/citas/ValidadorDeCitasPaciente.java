package service.validations.citas;

import dtos.DatosAgendaCita;

public interface ValidadorDeCitasPaciente extends ValidadorDeCitas {
    public void validar(DatosAgendaCita datos);
}

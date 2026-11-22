-- =============================================================================
-- Migración V9: Ampliación de Paciente y Expediente Clínico (NOM-004 / NOM-013)
-- Ticket Jira: DFI-41 (Subtarea de DFI-40)
-- =============================================================================

-- 1. Ficha de Identificación del Paciente (NOM-004-SSA3-2012, Numeral 5.1)
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE;
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS sexo VARCHAR(20);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS direccion VARCHAR(255);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS contacto_emergencia_nombre VARCHAR(150);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS contacto_emergencia_telefono VARCHAR(20);

-- 2. Cuestionario de Salud y Alertas Médicas Odontológicas (NOM-004 y NOM-013)
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS alergia_anestesia BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS alergia_latex BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS hipertension BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS diabetes BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS cardiopatias BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS problemas_coagulacion BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS embarazo BOOLEAN DEFAULT FALSE;
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS medicamentos_actuales TEXT;

-- 3. Evolución del Tratamiento Odontológico (NOM-013-SSA2-2015, Numeral 5.2)
ALTER TABLE evolucion_tratamiento ADD COLUMN IF NOT EXISTS pieza_dental VARCHAR(10);
ALTER TABLE evolucion_tratamiento ADD COLUMN IF NOT EXISTS procedimiento VARCHAR(150);
ALTER TABLE evolucion_tratamiento ADD COLUMN IF NOT EXISTS dentista_id BIGINT;

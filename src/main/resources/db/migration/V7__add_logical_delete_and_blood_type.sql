-- Agregar borrado logico a paciente, cita y tipo_servicios
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE;
ALTER TABLE cita ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE;
ALTER TABLE tipo_servicios ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE;

-- Agregar tipo_sanguineo a expediente_clinico
ALTER TABLE expediente_clinico ADD COLUMN IF NOT EXISTS tipo_sanguineo VARCHAR(255);

ALTER TABLE paciente ADD COLUMN IF NOT EXISTS apellidos VARCHAR(255);

ALTER TABLE cita ADD COLUMN IF NOT EXISTS tipo_servicios_id BIGINT;

ALTER TABLE cita
    DROP CONSTRAINT IF EXISTS fk_cita_tipo_servicio;

ALTER TABLE cita
    ADD CONSTRAINT fk_cita_tipo_servicio
        FOREIGN KEY (tipo_servicios_id) REFERENCES tipo_servicios (id);

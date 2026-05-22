-- Agregar relaciones de usuario a entidades principales
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS usuario_id BIGINT UNIQUE;
ALTER TABLE paciente ADD CONSTRAINT fk_paciente_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id);

ALTER TABLE dentista ADD COLUMN IF NOT EXISTS usuario_id BIGINT UNIQUE;
ALTER TABLE dentista ADD CONSTRAINT fk_dentista_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id);

ALTER TABLE personal_clinica ADD COLUMN IF NOT EXISTS usuario_id BIGINT UNIQUE;
ALTER TABLE personal_clinica ADD CONSTRAINT fk_personal_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id);
ALTER TABLE personal_clinica DROP COLUMN IF EXISTS rol;

-- Expediente Clinico
CREATE TABLE IF NOT EXISTS expediente_clinico (
    id BIGSERIAL PRIMARY KEY,
    alergias TEXT,
    enfermedades_cronicas TEXT,
    fecha_creacion DATE,
    paciente_id BIGINT UNIQUE,
    CONSTRAINT fk_expediente_paciente FOREIGN KEY (paciente_id) REFERENCES paciente (id)
);

-- Odontograma
CREATE TABLE IF NOT EXISTS odontograma (
    id BIGSERIAL PRIMARY KEY,
    expediente_clinico_id BIGINT UNIQUE,
    estado_dientes JSONB,
    CONSTRAINT fk_odontograma_expediente FOREIGN KEY (expediente_clinico_id) REFERENCES expediente_clinico (id)
);

-- Modificar tabla Cita existente
ALTER TABLE cita DROP COLUMN IF EXISTS fecha;
ALTER TABLE cita DROP COLUMN IF EXISTS monto;
ALTER TABLE cita DROP COLUMN IF EXISTS nombre;
ALTER TABLE cita DROP CONSTRAINT IF EXISTS fk_cita_tipo_servicio;
ALTER TABLE cita DROP COLUMN IF EXISTS tipo_servicios_id;

ALTER TABLE cita ADD COLUMN IF NOT EXISTS fecha_hora_inicio TIMESTAMP;
ALTER TABLE cita ADD COLUMN IF NOT EXISTS fecha_hora_fin TIMESTAMP;

-- Evolucion Tratamiento
CREATE TABLE IF NOT EXISTS evolucion_tratamiento (
    id BIGSERIAL PRIMARY KEY,
    notas_clinicas TEXT,
    fecha_registro DATE,
    expediente_clinico_id BIGINT,
    cita_id BIGINT UNIQUE,
    CONSTRAINT fk_evolucion_expediente FOREIGN KEY (expediente_clinico_id) REFERENCES expediente_clinico (id),
    CONSTRAINT fk_evolucion_cita FOREIGN KEY (cita_id) REFERENCES cita (id)
);

-- Ticket
CREATE TABLE IF NOT EXISTS ticket (
    id BIGSERIAL PRIMARY KEY,
    cita_id BIGINT UNIQUE,
    expediente_clinico_id BIGINT,
    total NUMERIC(19, 2),
    estado_pago VARCHAR(255),
    CONSTRAINT fk_ticket_cita FOREIGN KEY (cita_id) REFERENCES cita (id),
    CONSTRAINT fk_ticket_expediente FOREIGN KEY (expediente_clinico_id) REFERENCES expediente_clinico (id)
);

-- Detalle Ticket
CREATE TABLE IF NOT EXISTS detalle_ticket (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT,
    tipo_servicio_id BIGINT,
    cantidad INT,
    precio_unitario NUMERIC(19, 2),
    subtotal NUMERIC(19, 2),
    CONSTRAINT fk_detalle_ticket FOREIGN KEY (ticket_id) REFERENCES ticket (id),
    CONSTRAINT fk_detalle_tipo_servicio FOREIGN KEY (tipo_servicio_id) REFERENCES tipo_servicios (id)
);

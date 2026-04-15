-- Esquema inicial alineado con entidades JPA (nombres físicos snake_case por defecto de Spring Boot)

CREATE TABLE paciente (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    telefono VARCHAR(255),
    correo VARCHAR(255)
);

CREATE TABLE dentista (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255)
);

CREATE TABLE clinica (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    ubicacion VARCHAR(255)
);

CREATE TABLE personal_clinica (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    telefono VARCHAR(255),
    correo VARCHAR(255),
    rol VARCHAR(255)
);

CREATE TABLE tipo_servicios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    precio DOUBLE PRECISION,
    duracion DOUBLE PRECISION
);

CREATE TABLE cita (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    estado VARCHAR(255),
    fecha_creacion TIMESTAMP
);

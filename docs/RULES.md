# SPEC API: Dental Fine (MVP)

## 1. Login Básico (MVP)
**POST** `/api/auth/login`
- **Request Body:**
  {
  "correo": "admin@clinica.com",
  "contrasena": "12345"
  }
- **Response (200 OK):**
  {
  "token": "mock-jwt-token-123",
  "usuario": { "id": 1, "rol": "PERSONAL_CLINICA" }
  }

## 2. Búsqueda de Pacientes
El endpoint es consumido por la interfaz cuando el personal ingresa un nombre o teléfono[cite: 1, 103].
**GET** `/api/pacientes/buscar?q={datos}` [cite: 1]
- **Response (200 OK):** Retorna un arreglo de PacienteDTO[cite: 1, 104].
  [
  {
  "idPaciente": 1,
  "nombre": "Juan",
  "apellidos": "Pérez",
  "telefono": "4451234567"
  }
  ]

## 3. Consulta de Disponibilidad de Citas
Se utiliza para consultar los horarios libres de un dentista en una fecha específica[cite: 1, 105, 106].
**GET** `/api/citas/disponibilidad?dentistaId={id}&fecha={YYYY-MM-DD}` [cite: 1]
- **Response (200 OK):** Retorna un arreglo de HorarioDTO con los espacios calculados[cite: 1, 106].
  [
  {
  "horaInicio": "10:00",
  "horaFin": "11:00",
  "disponible": true
  },
  {
  "horaInicio": "11:00",
  "horaFin": "12:00",
  "disponible": false
  }
  ]

## 4. Agendar Cita
Registra la cita tras validar reglas de negocio y disponibilidad[cite: 1, 107].
**POST** `/api/citas/agendar` [cite: 1]
- **Request Body (CitaDTO):** [cite: 107]
  {
  "pacienteId": 1,
  "dentistaId": 2,
  "tipoServicioId": 3,
  "fechaHora": "2026-04-15T10:00:00"
  }
- **Response (201 Created):** Retorna la confirmación en formato CitaResponseDTO[cite: 1, 110].
  {
  "idCita": 105,
  "estado": "PENDIENTE",
  "mensaje": "Cita agendada correctamente"
  }

# Backend Specification: Dental Fine (Spring Boot)

## 1. Project Overview & Architecture
[cite_start]**Dental Fine** es el backend central de una plataforma SaaS para clínicas dentales[cite: 25].
- [cite_start]**Patrón Arquitectónico:** Arquitectura multicapa (Controller, Service, Repository)[cite: 28].
- **Principios de Diseño:** SOLID (alta cohesión, bajo acoplamiento, inyección de dependencias vía Spring).
- [cite_start]**Control de Concurrencia:** Crítico para evitar conflictos en la agenda[cite: 529].

## 2. Tech Stack & Dependencies
- **Framework Core:** Java + Spring Boot (Spring Web).
- [cite_start]**Base de Datos:** PostgreSQL para integridad transaccional ACID[cite: 33].
- **Migraciones de BD:** Flyway (control de versiones de base de datos).
- [cite_start]**Seguridad:** Spring Security + JWT (`io.jsonwebtoken`) para autenticación stateless[cite: 32].
- [cite_start]**Tiempo Real:** Spring WebSockets con protocolo STOMP para sincronización de agenda[cite: 60].
- **Mapeo ORM:** Spring Data JPA (Hibernate).

## 3. Security & CORS configuration
- **Autenticación:** Basada en tokens JWT. [cite_start]El API Gateway / Filtro validará la firma y expiración del token en cada petición[cite: 57].
- [cite_start]**CORS:** Configuración global y estricta en Spring Security para permitir solicitudes únicamente desde el dominio del frontend (ej. `http://localhost:5173` en desarrollo)[cite: 31, 119].
- [cite_start]**RBAC (Role-Based Access Control):** Basado en el enumerador `Rol`[cite: 539]:
    - `ROLE_ADMIN`
    - `ROLE_USUARIO`
    - `ROLE_DOCTOR`
    - `ROLE_PERSONAL_CLINICA`

## 4. Core Entities & Data Model
[cite_start]Mapeo principal de entidades JPA[cite: 539]:
- **`Paciente`**: `id`, `nombre`, `telefono`, `correo` (Relación 1:N con Cita).
- **`Dentista`**: `id`, `nombre`, `especialidad` (Relación 1:N con Cita).
- **`TipoServicios`**: `id`, `nombre`, `precio`, `duracion` (Relación 1:N con Cita).
- **`Cita`**: `id`, `fechaHora`, Enum `Estado` (`ACTIVA`, `PENDIENTE`, `FINALIZADA`).

## 5. Implementation Rules & Best Practices
1. **Flyway Migrations:** Ninguna tabla se creará mediante `spring.jpa.hibernate.ddl-auto=update`. Todo cambio en la BD debe realizarse mediante scripts SQL (ej. `V1__Initial_Schema.sql`).
2. **Data Transfer Objects (DTOs):** Las entidades nunca deben exponerse directamente en los controladores. [cite_start]Usar DTOs para solicitudes y respuestas[cite: 119].
3. [cite_start]**Manejo de Excepciones:** Uso de `@ControllerAdvice` (`GlobalExceptionHandler`) para devolver respuestas de error consistentes y en formato JSON[cite: 119].
4. [cite_start]**WebSockets:** Al confirmar una nueva cita o cancelación en `CitaService`, se debe emitir un evento a través del broker de STOMP para actualizar el frontend de otros usuarios conectados[cite: 109, 119].

## 6. Milestone 1: Initial Setup
- [ ] Inicializar proyecto Spring Boot y configurar `pom.xml` / `build.gradle`.
- [ ] Configurar conexión a PostgreSQL y estructura base de Flyway.
- [ ] Configurar CORS y esqueleto de Spring Security (sin filtros JWT todavía).
- [ ] Crear Entidades base y Repositorios.
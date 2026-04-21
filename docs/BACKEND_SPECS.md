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
- **Autenticación:** Basada en tokens JWT. El API Gateway / Filtro validará la firma y expiración del token en cada petición.
  > [!NOTE]
  > MVP Status: Actualmente se encuentra en estado `permitAll()`. La validación de JWT está agregada en el roadmap.
- **CORS:** Configuración global y estricta en Spring Security para permitir solicitudes únicamente desde el dominio del frontend (ej. `http://localhost:5173` en desarrollo).
  > [!NOTE]
  > MVP Status: Actualmente CORS está abierto para origen `*` para facilitar integración temprana.[cite: 31, 119].
- [cite_start]**RBAC (Role-Based Access Control):** Basado en el enumerador `Rol`[cite: 539]:
  - `ROLE_ADMIN`
  - `ROLE_USUARIO`
  - `ROLE_DOCTOR`
  - `ROLE_PERSONAL_CLINICA`

## 4. Core Entities & Data Model
[cite_start]Mapeo principal de entidades JPA[cite: 539]:
- **`Paciente`**: `id`, `nombre`, `apellidos`, `telefono`, `correo` (Relación 1:N con Cita).
- **`Dentista`**: `id`, `nombre`, `especialidad` (Relación 1:N con Cita).
  > [!NOTE]
  > MVP Status: El campo `especialidad` no está en base de datos.
- **`TipoServicios`**: `id`, `nombre`, `precio`, `duracion` (Relación 1:N con Cita).
- **`Cita`**: `id`, `fecha`, `monto`, Enum `Estado` (`ACTIVA`, `PENDIENTE`, `FINALIZADA`).

## 5. Implementation Rules & Best Practices
1. **Flyway Migrations:** Ninguna tabla se creará mediante `spring.jpa.hibernate.ddl-auto=update`. Todo cambio en la BD debe realizarse mediante scripts SQL (ej. `V1__Initial_Schema.sql`).
2. **Data Transfer Objects (DTOs):** Las entidades nunca deben exponerse directamente en los controladores. [cite_start]Usar DTOs para solicitudes y respuestas[cite: 119].
3. [cite_start]**Manejo de Excepciones:** Uso de `@ControllerAdvice` (`GlobalExceptionHandler`) para devolver respuestas de error consistentes y en formato JSON[cite: 119].
4. [cite_start]**WebSockets:** Al confirmar una nueva cita o cancelación en `CitaService`, se debe emitir un evento a través del broker de STOMP para actualizar el frontend de otros usuarios conectados[cite: 109, 119].

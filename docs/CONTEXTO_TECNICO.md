# Documento de Contexto Técnico - Dental Fine (Backend)

Este documento describe en profundidad el estado actual del backend de **Dental Fine**, consolidado a partir de la inspección del código fuente, configuración, pruebas y la documentación en `docs/`.

---

## 1. Stack Tecnológico

El proyecto está construido sobre las siguientes tecnologías y librerías clave (`pom.xml`):

| Componente | Tecnología | Versión / Detalle |
| :--- | :--- | :--- |
| **Lenguaje** | Java | 17 (código compilado con JDK 21) |
| **Framework Core** | Spring Boot | 4.0.3 (Spring Framework 7.x, Spring Web MVC) |
| **Seguridad** | Spring Security + JWT | `io.jsonwebtoken` / `com.auth0:java-jwt` v4.4.0, `BCryptPasswordEncoder` |
| **Acceso a Datos** | Spring Data JPA + Hibernate | Hibernate 7.2.4 Final, `@Entity`, relaciones JPA |
| **Base de Datos Principal** | PostgreSQL | Driver oficial `org.postgresql:postgresql` |
| **Control de Versiones BD** | Flyway | `flyway-database-postgresql`, migraciones V1 a V8 |
| **Tiempo Real** | Spring WebSocket + STOMP | `spring-boot-starter-websocket`, Broker Simple en `/topic` |
| **Mensajería / Correos** | Spring Boot Mail | `spring-boot-starter-mail` con `@Async` |
| **Validación** | Jakarta Bean Validation | `spring-boot-starter-validation` (`@NotNull`, `@Valid`) |
| **Testing** | JUnit 5 + Mockito + MockMvc + H2 | `spring-boot-starter-test`, H2 en modo compatibilidad PostgreSQL |
| **Productividad** | Lombok | Anotaciones `@Data`, `@AllArgsConstructor`, etc. |

---

## 2. Arquitectura y Estructura del Proyecto

El backend implementa una **arquitectura en capas desacopladas (Controller - Service - Repository)** bajo principios SOLID:

```
src/main/java/com/DentalFine/Dental_Fine_BackEnd/
├── config/                  # Seguridad, CORS, WebSocket, Manejador Global de Errores
│   ├── security/            # TokenService (JWT), SecurityFilter, AutenticacionService
│   ├── GlobalExceptionHandler.java
│   ├── SecurityConfig.java
│   └── WebSocketConfig.java
├── controller/              # Endpoints REST (Controladores HTTP)
│   ├── AuthController.java
│   ├── CitaController.java
│   ├── DentistaController.java
│   ├── ExpedienteController.java
│   ├── PacienteController.java
│   ├── PersonalClinicaController.java
│   ├── TestController.java
│   ├── TicketController.java
│   └── TipoServicioController.java
├── dto/                     # Clases Record para Request y Response (sin exponer entidades)
│   ├── requests/            # AgendarCita, CancelarCita, Login, Pacientes, Expediente, etc.
│   └── responses/           # CitaAgendar, PacienteBusqueda, Horarios, Dentistas, etc.
├── models/                  # Entidades JPA (Mapeo a tablas PostgreSQL)
│   ├── Cita.java
│   ├── Clinica.java
│   ├── Dentista.java
│   ├── DetalleTicket.java
│   ├── Estado.java          (Enum: ACTIVA, PENDIENTE, FINALIZADA, CANCELADA, INASISTENCIA)
│   ├── EvolucionTratamiento.java
│   ├── ExpedienteClinico.java
│   ├── Odontograma.java
│   ├── Paciente.java
│   ├── PersonalClinica.java
│   ├── Rol.java             (Enum: ROLE_ADMIN, ROLE_DOCTOR, ROLE_PACIENTE, ROLE_RECEPCIONISTA...)
│   ├── Ticket.java
│   ├── TipoServicios.java
│   └── Usuario.java
├── repository/              # Interfaces Spring Data JPA con consultas JPQL/SQL
│   ├── CitaRepository.java
│   ├── PacienteRepository.java
│   ├── ExpedienteClinicoRepository.java
│   └── ... (11 repositorios)
└── service/                 # Reglas de negocio transaccionales y validaciones
    ├── validations/citas/   # Validadores modulares (ValidadorDeCitas, Cancelación)
    ├── AgendaEventPublisher.java (STOMP WebSockets)
    ├── CitaService.java
    ├── PacienteService.java
    ├── ExpedienteClinicoService.java
    ├── TicketService.java
    └── ...
```

---

## 3. Estado de la Base de Datos y Migraciones Flyway

Las tablas se gestionan estrictamente a través de Flyway en `src/main/resources/db/migration/` (con `spring.jpa.hibernate.ddl-auto=validate`):

1. **`V1__Initial_Schema.sql`**: Tablas iniciales `paciente`, `dentista`, `clinica`, `personal_clinica`, `tipo_servicios`, `cita`.
2. **`V2__mvp_paciente_apellidos_cita_tipo_servicio.sql`**: Agrega campo `apellidos` a paciente y llave foránea `tipo_servicios_id` a cita.
3. **`V3__create_table_usuarios.sql`**: Tabla `usuarios` (`id`, `correo`, `contrasena`, `rol`).
4. **`V4__insert_admin_default.sql`**: Usuario administrador por defecto.
5. **`V5__crear_tablas_clinicas_y_tickets.sql`**:
   - Tablas `expediente_clinico`, `odontograma` (campo `estado_dientes JSONB`), `evolucion_tratamiento`, `ticket`, `detalle_ticket`.
   - Modificación de `cita` agregando `fecha_hora_inicio` y `fecha_hora_fin`, removiendo campos primitivos de monto/servicio para derivarlos a tickets.
6. **`V6__insert_test_users.sql`**: Inserción de usuarios semilla con contraseñas encriptadas BCrypt.
7. **`V7__add_logical_delete_and_blood_type.sql`**:
   - Borrado lógico: Agrega columna `activo BOOLEAN DEFAULT TRUE` a `paciente`, `cita` y `tipo_servicios`.
   - Agrega columna `tipo_sanguineo VARCHAR(255)` a `expediente_clinico`.
8. **`V8__drop_unique_constraint_evolucion_cita.sql`**: Permite registrar múltiples notas de evolución clínica para una misma cita.

---

## 4. Funcionalidades Ya Implementadas (Lo que NO debe reimplementarse)

### A. Autenticación y Seguridad
- **Login REST**: Autenticación vía `POST /auth/login` validando credenciales encriptadas con BCrypt y retornando token JWT firmado (`AuthController.java`).
- **Filtro de Seguridad Stateless**: `SecurityFilter` intercepta y valida el encabezado `Authorization: Bearer <token>` mediante `TokenService` y fija la autenticación en el `SecurityContextHolder`.
- **CORS Configurado**: Habilitado para métodos `GET, POST, PUT, DELETE, OPTIONS, PATCH` y credenciales activas.

### B. Gestión de Citas y Agenda en Tiempo Real
- **Cálculo de Disponibilidad**: `GET /citas/disponibilidad?dentistaId={id}&fecha={YYYY-MM-DD}` evalúa intervalos de 1 hora (8:00 a 17:00) verificando si existen citas solapadas no canceladas (`CitaService.java`).
- **Agendamiento con Validaciones**: `POST /citas/agendar` valida existencia de paciente, dentista, ausencia de traslape horario, regla de máximo una cita por paciente por día (`ValidacionDiaPaciente`), guarda la cita y notifica inmediatamente por WebSocket a `/topic/agenda`.
- **Consulta de Citas**: `GET /citas` (citas activas), `GET /citas/{id}`, y `GET /citas/paciente/{pacienteId}`.
- **Cancelación de Citas**: `PUT /citas/{id}/cancelar` cambia el estado a `CANCELADA`, ejecuta `ValidacionAnticipacionCancelacion` (mínimo 24 horas antes si el rol es PACIENTE) y emite evento en tiempo real.
- **Edición Básica de Cita**: `PUT /citas/{id}/editar` actualiza fecha inicio/fin y entidades relacionadas, aunque carece actualmente de verificación de disponibilidad y emisión en tiempo real.

### C. Gestión de Pacientes
- **Búsqueda Dinámica**: `GET /pacientes/buscar?q={cadena}` busca mediante JPQL coincidencia insensible a mayúsculas/minúsculas en `nombre`, `apellidos` o `telefono`, filtrando solo registros activos (`p.activo = true`).
- **Consulta Global y Detallada**: `GET /pacientes` (todos los activos) y `GET /pacientes/{id}`.
- **Creación de Paciente con Expediente Automático**: `POST /pacientes` registra al paciente y genera de inmediato su `ExpedienteClinico` asociado con un `Odontograma` vacío.
- **Actualización y Borrado Lógico**: `PUT /pacientes/{id}` y `DELETE /pacientes/{id}` (marca `activo = false`).

### D. Expediente Clínico y Odontograma
- **Consulta por Paciente**: `GET /expedientes/paciente/{pacienteId}` recupera el expediente, odontograma y lista de notas clínicas (con inicialización lazy defensiva).
- **Actualización de Odontograma**: `PUT /expedientes/{id}/odontograma` almacena el mapa de 32 dientes en columna nativa `jsonb`.
- **Evolución del Tratamiento**: `POST /expedientes/{id}/evolucion` asocia notas clínicas a la cita y al expediente clínico.
- **Ficha de Salud General**: `PUT /expedientes/paciente/{pacienteId}/salud-general` actualiza alergias, enfermedades crónicas y tipo de sangre.

### E. Tickets y Catálogos
- **Generación de Cobros**: `POST /tickets/cita/{citaId}` calcula importes unitarios y subtotales en MXN a partir de los `tipo_servicios` agregados y crea el ticket.
- **Catálogo de Servicios**: `GET /servicios`, `GET /servicios/{id}`, `POST /servicios`, `PUT /servicios/{id}`, `DELETE /servicios/{id}` (con borrado lógico `activo = false`).
- **Catálogo de Dentistas**: `GET /dentistas`, `GET /dentistas/{id}`.

---

## 5. Matriz de Endpoints y Contratos Actuales

| Módulo | Método | Ruta Actual en Código | DTO Request | DTO / Formato Response |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | POST | `/auth/login` | `LoginRequest` | `LoginResponse` (token + rol) |
| **Citas** | GET | `/citas/disponibilidad` | Query Params: `dentistaId`, `fecha` | `List<HorarioDisponibilidadResponse>` |
| **Citas** | POST | `/citas/agendar` | `AgendarCitaRequest` | `CitaAgendarResponse` (201 Created) |
| **Citas** | GET | `/citas` | Ninguno | `List<CitaResumenDTO>` |
| **Citas** | GET | `/citas/{id}` | Path: `id` | `CitaResumenDTO` |
| **Citas** | GET | `/citas/paciente/{pacienteId}` | Path: `pacienteId` | `List<CitaResumenDTO>` |
| **Citas** | PUT | `/citas/{id}/cancelar` | `CancelarCitaRequest` | `CitaAgendarResponse` |
| **Citas** | PUT | `/citas/{id}/editar` | `AgendarCitaRequest` | `CitaAgendarResponse` |
| **Pacientes** | GET | `/pacientes/buscar` | Query Param: `q` | `List<PacienteBusquedaResponse>` |
| **Pacientes** | GET | `/pacientes` | Ninguno | `List<PacienteDTO>` |
| **Pacientes** | GET | `/pacientes/{id}` | Path: `id` | `PacienteDTO` |
| **Pacientes** | POST | `/pacientes` | `RegistrarPacienteRequest` | `PacienteDTO` (201 Created) |
| **Pacientes** | PUT | `/pacientes/{id}` | `RegistrarPacienteRequest` | `PacienteDTO` |
| **Pacientes** | DELETE | `/pacientes/{id}` | Path: `id` | `204 No Content` (`activo = false`) |
| **Expedientes**| GET | `/expedientes/paciente/{pacienteId}` | Path: `pacienteId` | `ExpedienteClinico` |
| **Expedientes**| PUT | `/expedientes/{id}/odontograma` | `OdontogramaActualizarRequest` | `Odontograma` |
| **Expedientes**| POST | `/expedientes/{id}/evolucion` | `EvolucionAgregarRequest` | `EvolucionTratamiento` |
| **Expedientes**| PUT | `/expedientes/paciente/{pacienteId}/salud-general` | `SaludGeneralRequest` | `ExpedienteClinico` |
| **Tickets** | POST | `/tickets/cita/{citaId}` | `TicketGenerarRequest` | `Ticket` |
| **Servicios** | GET/POST/PUT/DEL | `/servicios/**` | `RegistrarServicioRequest` | `ServicioDTO` |
| **Dentistas** | GET | `/dentistas/**` | Ninguno | `DentistaDTO` |

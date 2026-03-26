# Dental Fine – Backend Specification

> **Enfoque:** Spec-Driven Development  
> **Estado:** Borrador inicial  
> **Última actualización:** 2026-03-26

---

## 1. Visión general

**Dental Fine** es una plataforma de gestión clínica odontológica. Este documento describe las especificaciones funcionales y técnicas del backend, que servirá como fuente de verdad para generar las tareas de desarrollo (`task.md`) y su seguimiento (`task_track.md`).

---

## 2. Stack tecnológico

| Componente        | Tecnología                      |
|-------------------|---------------------------------|
| Lenguaje          | Java 17                         |
| Framework         | Spring Boot 4.0.x               |
| Persistencia      | Spring Data JPA                 |
| Base de datos     | Por definir (MySQL / PostgreSQL) |
| Build             | Maven                           |
| Utilidades        | Lombok                          |
| Seguridad         | Por definir (Spring Security + JWT) |

---

## 3. Dominio del negocio

### 3.1 Entidades principales

#### `Clinica`
Representa la clínica dental.

| Campo      | Tipo   | Descripción                  |
|------------|--------|------------------------------|
| `id`       | Long   | Identificador único          |
| `nombre`   | String | Nombre de la clínica         |
| `ubicacion`| String | Dirección / ubicación física |

---

#### `Dentista`
Profesional odontológico que atiende citas.

| Campo    | Tipo   | Descripción         |
|----------|--------|---------------------|
| `id`     | Long   | Identificador único |
| `nombre` | String | Nombre completo     |

> **Pendiente:** especialidad, licencia profesional, horario disponible.

---

#### `Paciente`
Persona que recibe atención dental.

| Campo     | Tipo   | Descripción              |
|-----------|--------|--------------------------|
| `id`      | Long   | Identificador único      |
| `nombre`  | String | Nombre completo          |
| `telefono`| String | Teléfono de contacto     |
| `correo`  | String | Correo electrónico       |

> **Pendiente:** fecha de nacimiento, historial clínico, expediente.

---

#### `PersonalClinica`
Personal administrativo y de apoyo de la clínica.

| Campo     | Tipo   | Descripción                                          |
|-----------|--------|------------------------------------------------------|
| `id`      | Long   | Identificador único                                  |
| `nombre`  | String | Nombre completo                                      |
| `telefono`| String | Teléfono de contacto                                 |
| `correo`  | String | Correo electrónico                                   |
| `rol`     | `Rol`  | Rol asignado (`ROLE_ADMIN`, `ROLE_PERSONAL_CLINICA`) |

---

#### `Cita`
Registro de una consulta o tratamiento agendado.

| Campo          | Tipo     | Descripción                                        |
|----------------|----------|----------------------------------------------------|
| `id`           | Long     | Identificador único                                |
| `fechaCita`    | Date     | Fecha y hora de la cita                            |
| `estado`       | `Estado` | Estado de la cita (`ACTIVA`, `PENDIENTE`, `FINALIZADA`) |
| `fechaCreacion`| Date     | Fecha de registro en el sistema                    |

> **Pendiente:** relación con `Paciente`, `Dentista`, y `TipoServicios`.

---

#### `TipoServicios`
Catálogo de servicios ofrecidos por la clínica.

| Campo      | Tipo   | Descripción              |
|------------|--------|--------------------------|
| `id`       | Long   | Identificador único      |
| `nombre`   | String | Nombre del servicio      |
| `precio`   | Double | Precio del servicio      |
| `duracion` | Double | Duración en minutos      |

---

### 3.2 Enumeraciones

#### `Estado` (estado de una cita)
```
ACTIVA | PENDIENTE | FINALIZADA
```

#### `Rol` (rol de usuario en el sistema)
```
ROLE_ADMIN | ROLE_USUARIO | ROLE_DOCTOR | ROLE_PERSONAL_CLINICA
```

---

## 4. Especificaciones de API REST

> Prefijo base: `/api/v1`

### 4.1 Pacientes – `/api/v1/paciente`

| Método | Endpoint          | Descripción                        | Estado     |
|--------|-------------------|------------------------------------|------------|
| GET    | `/`               | Listar todos los pacientes         | Pendiente  |
| GET    | `/{id}`           | Obtener paciente por ID            | Pendiente  |
| POST   | `/`               | Crear nuevo paciente               | Pendiente  |
| PUT    | `/{id}`           | Actualizar datos de un paciente    | Pendiente  |
| DELETE | `/{id}`           | Eliminar paciente                  | Pendiente  |

---

### 4.2 Dentistas – `/api/v1/dentista`

| Método | Endpoint  | Descripción                      | Estado    |
|--------|-----------|----------------------------------|-----------|
| GET    | `/`       | Listar todos los dentistas       | Pendiente |
| GET    | `/{id}`   | Obtener dentista por ID          | Pendiente |
| POST   | `/`       | Registrar nuevo dentista         | Pendiente |
| PUT    | `/{id}`   | Actualizar datos de un dentista  | Pendiente |
| DELETE | `/{id}`   | Eliminar dentista                | Pendiente |

---

### 4.3 Citas – `/api/v1/cita`

| Método | Endpoint       | Descripción                          | Estado    |
|--------|----------------|--------------------------------------|-----------|
| GET    | `/`            | Listar todas las citas               | Pendiente |
| GET    | `/{id}`        | Obtener cita por ID                  | Pendiente |
| GET    | `/paciente/{id}` | Listar citas de un paciente        | Pendiente |
| GET    | `/dentista/{id}` | Listar citas de un dentista        | Pendiente |
| POST   | `/`            | Crear nueva cita                     | Pendiente |
| PUT    | `/{id}`        | Actualizar cita                      | Pendiente |
| PATCH  | `/{id}/estado` | Cambiar estado de una cita           | Pendiente |
| DELETE | `/{id}`        | Cancelar / eliminar cita             | Pendiente |

---

### 4.4 Personal de Clínica – `/api/v1/personal`

| Método | Endpoint  | Descripción                           | Estado    |
|--------|-----------|---------------------------------------|-----------|
| GET    | `/`       | Listar todo el personal               | Pendiente |
| GET    | `/{id}`   | Obtener personal por ID               | Pendiente |
| POST   | `/`       | Registrar nuevo personal              | Pendiente |
| PUT    | `/{id}`   | Actualizar datos del personal         | Pendiente |
| DELETE | `/{id}`   | Eliminar personal                     | Pendiente |

---

### 4.5 Servicios – `/api/v1/servicios`

| Método | Endpoint  | Descripción                         | Estado    |
|--------|-----------|-------------------------------------|-----------|
| GET    | `/`       | Listar todos los servicios          | Pendiente |
| GET    | `/{id}`   | Obtener servicio por ID             | Pendiente |
| POST   | `/`       | Crear nuevo tipo de servicio        | Pendiente |
| PUT    | `/{id}`   | Actualizar servicio                 | Pendiente |
| DELETE | `/{id}`   | Eliminar servicio                   | Pendiente |

---

### 4.6 Clínica – `/api/v1/clinica`

| Método | Endpoint  | Descripción                         | Estado    |
|--------|-----------|-------------------------------------|-----------|
| GET    | `/`       | Obtener datos de la clínica         | Pendiente |
| PUT    | `/`       | Actualizar datos de la clínica      | Pendiente |

---

## 5. Reglas de negocio

### Citas
- Una cita debe tener asignado un `Paciente`, un `Dentista` y un `TipoServicio`.
- No se pueden agendar dos citas para el mismo dentista en el mismo bloque horario.
- Una cita solo puede pasar de `PENDIENTE` → `ACTIVA` → `FINALIZADA`.
- Una cita `FINALIZADA` no puede modificarse.

### Pacientes
- El correo electrónico debe ser único en el sistema.
- Un paciente puede tener múltiples citas en distintas fechas.

### Dentistas
- Un dentista está vinculado a la clínica y tiene un rol `ROLE_DOCTOR`.
- Puede tener disponibilidad horaria configurada (a definir).

### Personal
- Solo usuarios con `ROLE_ADMIN` pueden gestionar (crear, editar, eliminar) personal y dentistas.
- El personal con `ROLE_PERSONAL_CLINICA` puede agendar y modificar citas.

---

## 6. Seguridad y autenticación

> Estado: **Por definir / pendiente de implementación**

- Autenticación mediante **JWT (JSON Web Token)**.
- Autorización basada en roles (`Rol`).
- Endpoints protegidos según rol:
  - `ROLE_ADMIN`: acceso total.
  - `ROLE_DOCTOR`: gestión de sus propias citas y pacientes.
  - `ROLE_PERSONAL_CLINICA`: agendar y administrar citas.
  - `ROLE_USUARIO`: consulta de sus propias citas (futuro portal de pacientes).

---

## 7. Estructura de capas del proyecto

```
src/main/java/
├── com.DentalFine.Dental_Fine_BackEnd/
│   └── DentalFineBackEndApplication.java   ← Punto de entrada
├── controller/       ← Controladores REST (capa de presentación)
├── service/          ← Lógica de negocio
├── respository/      ← Interfaces JPA (acceso a datos)
├── models/           ← Entidades y enumeraciones de dominio
└── (pendiente)
    ├── dto/          ← Data Transfer Objects
    ├── exception/    ← Manejo de errores global
    └── security/     ← Configuración de Spring Security + JWT
```

---

## 8. Decisiones de diseño pendientes

| # | Tema                              | Opciones                           | Decisión |
|---|-----------------------------------|------------------------------------|----------|
| 1 | Motor de base de datos            | MySQL / PostgreSQL                 | TBD      |
| 2 | Autenticación                     | JWT / OAuth2 / Session             | TBD      |
| 3 | Formato de respuesta de error     | RFC 7807 Problem Details / custom  | TBD      |
| 4 | Paginación por defecto            | 10 / 20 / 50 registros por página  | TBD      |
| 5 | Versionado de API                 | URL (`/v1`) / Header               | URL /v1  |
| 6 | Gestión de migraciones DB         | Flyway / Liquibase                 | TBD      |
| 7 | Historial clínico de pacientes    | Modelo propio / adjuntos           | TBD      |

---

## 9. Próximos pasos (Roadmap de especificación)

- [ ] Completar atributos faltantes en entidades (`Dentista`, `Cita`).
- [ ] Definir DTOs de request y response para cada endpoint.
- [ ] Especificar contratos de error (códigos HTTP y mensajes).
- [ ] Definir motor de base de datos y configurar `application.properties`.
- [ ] Diseñar modelo de seguridad con Spring Security + JWT.
- [ ] Generar `task.md` con las tareas derivadas de este spec.
- [ ] Generar `task_track.md` para el seguimiento de progreso de tareas.

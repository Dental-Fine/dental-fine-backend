# SPEC API: Dental Fine (MVP + Historias de Usuario HU-05, HU-06, HU-08)

## 1. Login Básico (MVP)
**POST** `/api/auth/login` (también `/auth/login`)
- **Request Body:**
  ```json
  {
    "correo": "admin@clinica.com",
    "contrasena": "12345"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "token": "mock-jwt-token-123",
    "usuario": { "id": 1, "rol": "PERSONAL_CLINICA" }
  }
  ```

## 2. Búsqueda y Consulta de Pacientes (HU-06 / HU06-T02)
El endpoint es consumido por la interfaz cuando el personal ingresa un nombre o teléfono (mediante el parámetro unificado `q` o mediante parámetros específicos `nombre` y `telefono`).
- **GET** `/api/pacientes/buscar?q={datos}` (o `?nombre={nombre}&telefono={telefono}`)
- **Response (200 OK):** Retorna un arreglo de `PacienteBusquedaResponse`:
  ```json
  [
    {
      "idPaciente": 1,
      "nombre": "Juan",
      "apellidos": "Pérez",
      "telefono": "4451234567"
    }
  ]
  ```
- **GET** `/api/pacientes/{id}`
- **Response (200 OK):** Retorna `PacienteDTO` (o `404 Not Found` si no existe o fue dado de baja lógicamente):
  ```json
  {
    "id": 1,
    "nombre": "Juan",
    "apellidos": "Pérez",
    "telefono": "4451234567",
    "correo": "juan@correo.com"
  }
  ```

## 3. Consulta de Disponibilidad de Citas
Se utiliza para consultar los horarios libres de un dentista en una fecha específica.
**GET** `/api/citas/disponibilidad?dentistaId={id}&fecha={YYYY-MM-DD}`
- **Response (200 OK):** Retorna un arreglo de `HorarioDisponibilidadResponse` con los espacios calculados:
  ```json
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
  ```

## 4. Agendar Cita
Registra la cita tras validar reglas de negocio y disponibilidad.
**POST** `/api/citas/agendar`
- **Request Body (`AgendarCitaRequest`):**
  ```json
  {
    "pacienteId": 1,
    "dentistaId": 2,
    "fechaHoraInicio": "2026-04-15T10:00:00",
    "fechaHoraFin": "2026-04-15T11:00:00"
  }
  ```
- **Response (201 Created):** Retorna la confirmación en formato `CitaAgendarResponse`:
  ```json
  {
    "idCita": 105,
    "estado": "PENDIENTE",
    "mensaje": "Cita agendada correctamente"
  }
  ```

## 5. Reagendar Cita con Verificación de Disponibilidad (HU-08 / HU08-T02)
Reprograma una cita existente validando previamente que no esté cancelada/finalizada/inasistencia, que el dentista esté libre en el nuevo horario (excluyendo la cita actual) y que el paciente no tenga otra cita el mismo día.
**PUT** `/api/citas/{id}/reagendar`
- **Request Body (`AgendarCitaRequest`):**
  ```json
  {
    "pacienteId": 1,
    "dentistaId": 2,
    "fechaHoraInicio": "2026-04-16T12:00:00",
    "fechaHoraFin": "2026-04-16T13:00:00"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "idCita": 105,
    "estado": "PENDIENTE",
    "mensaje": "Cita reprogramada correctamente"
  }
  ```

## 6. Cancelación Lógica y Archivado de Citas (HU-05 / HU05-T01)
- **PUT** `/api/citas/{id}/cancelar` — Cambia el estado a `CANCELADA` (valida anticipación de 24h para rol `PACIENTE`) y libera el horario.
- **PUT** `/api/citas/{id}/inasistencia` (también `/api/citas/{id}/no-asistio`) — Marca la cita con estado `INASISTENCIA`, libera el espacio de agenda y notifica por WebSocket `/topic/agenda`.
- **DELETE** `/api/citas/{id}` — Realiza borrado lógico (`activo = false` y estado `CANCELADA`), retornando `204 No Content`.

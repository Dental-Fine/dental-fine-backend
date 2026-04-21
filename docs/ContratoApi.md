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
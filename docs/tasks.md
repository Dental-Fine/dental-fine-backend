# Backend Roadmap - Tareas Pendientes

Después de consolidar el MVP de Dental Fine (CRUD base, WebSockets, Validaciones), este es el hilo de las siguientes prioridades a abordar tras la validación básica del frontend:

- `[ ]` **Refactorizar Seguridad (JWT):** Implementar el flujo real de autenticación. Pasar del `permitAll()` actual a filtros de JWT para proteger los datos de los pacientes y los endpoints. (Reflejado como pendiente en `BACKEND_SPECS.md`).
- `[ ]` **Módulo de Expediente Clínico:** Crear las tablas y entidades para los antecedentes médicos. Esta será una relación 1:1 con `Paciente`.
- `[ ]` **Sistema de Odontograma:** Implementar la lógica para procesar un JSON anidado que represente los 32 dientes y sus estados (Restaurado, Caries, Sano, etc.).
- `[ ]` **Gestión de Archivos:** Configurar un sistema de almacenamiento (ej. AWS S3, local, u otro proveedor) para poder adjuntar radiografías y otros documentos médicos.
- `[ ]` **Cálculos Financieros Estrictos:** Asegurar y refactorizar en la capa de servicios que todos los montos sigan operando estrictamente en Pesos Mexicanos (MXN) con redondeo forzoso a dos decimales, protegiendo las conversiones flotantes.
- `[ ]` **Refinar Modelo Dentista:** Agregar la persistencia del campo `especialidad` en la entidad y esquema de `Dentista`.
- `[ ]` **CORS Estricto:** Revertir el origen `*` de CORS y restringir las llamadas únicamente a la URL oficial del frontend.

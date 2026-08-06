# HU-11 - Consultar mis citas como paciente autenticado

> Documento de implementación de la Historia de Usuario HU-11.

| Campo | Valor |
|--------|-------|
| Historia | HU-11 |
| Nombre | Consultar mis citas como paciente autenticado |
| Estado | ✅ Completada |
| Sprint | Sprint 4 |

---

# 1. Objetivo

Permitir que un paciente autenticado consulte únicamente sus propias citas médicas desde el Portal del Paciente, reutilizando el contexto autenticado y manteniendo el aislamiento por consultorio y paciente.

# 2. Alcance implementado

- Consulta exclusiva de las citas del paciente autenticado.
- Aislamiento por `clinicId` y `patientId`.
- Orden cronológico de resultados.
- Clasificación visual de citas:
  - Próximas
  - Anteriores
  - Canceladas
  - Todas
- Navegación desde el Portal del Paciente.
- Acceso directo para reservar una nueva cita.

# 3. Backend

## Endpoint

`GET /api/v1/appointments/patient`

### Seguridad

- JWT Stateless.
- Rol `PATIENT`.
- Contexto autenticado.
- Authorization por dominio.

### Implementación

Se reutilizó la arquitectura existente del módulo de citas incorporando una consulta especializada para el paciente autenticado, sin exponer identificadores de paciente en la API.

# 4. Frontend

Se incorporó una nueva página:

`features/patientAppointments/pages/PatientAppointmentsPage.tsx`

Capacidades:

- Consulta automática de citas.
- Estados de carga.
- Estado vacío.
- Manejo homogéneo de errores.
- Diseño Responsive.
- Mobile First.
- Navegación al Portal del Paciente.
- Navegación a la reserva de una nueva cita.

# 5. Componentes reutilizados

- AppointmentService
- SessionService
- AppCard
- AppButton
- Componentes de autenticación
- Layout del Portal del Paciente

# 6. Reglas de negocio

- El paciente únicamente visualiza sus propias citas.
- No es posible consultar citas de otros pacientes.
- El filtrado es exclusivamente visual; la seguridad permanece en el backend.
- Las citas canceladas permanecen disponibles para consulta histórica.

# 7. Pruebas realizadas

## Backend

- 143 pruebas automáticas.
- BUILD SUCCESS.

## Frontend

- ESLint sin errores.
- TypeScript Build exitoso.
- Vite Build exitoso.

## Validación funcional

Se verificó exitosamente:

- Consulta de citas.
- Filtros.
- Navegación.
- Integración con HU-09.
- Flujo End-to-End.

# 8. Impacto arquitectónico

La implementación amplía el Portal del Paciente reutilizando la arquitectura existente, sin duplicar lógica de negocio y manteniendo los principios del proyecto:

- Arquitectura modular.
- DTOs mediante Records.
- Constructor Injection.
- Seguridad basada en JWT.
- Domain Authorization.
- Componentes reutilizables.
- Tipado estricto.
- Responsive Design.

# 9. Resultado

La HU-11 queda implementada y validada funcionalmente, fortaleciendo el Portal del Paciente y preparando la base para futuras funcionalidades como la cancelación de citas y el historial clínico.

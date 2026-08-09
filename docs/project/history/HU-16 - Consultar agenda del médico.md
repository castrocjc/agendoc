# HU-16 — Consultar agenda del médico

**Proyecto:** AgenDoc  
**Historia de Usuario:** HU-16  
**Sprint:** Sprint 5  
**Estado:** ✅ Completada  
**Fecha:** Agosto 2026

## 1. Objetivo
Implementar el primer módulo funcional del Portal del Médico, permitiendo que un médico autenticado consulte únicamente las citas que le pertenecen dentro de su consultorio, reutilizando la arquitectura existente del módulo de citas.

## 2. Objetivo de negocio
La historia inaugura el Portal del Médico y habilita la consulta autónoma de la agenda diaria mediante autenticación JWT y aislamiento por dominio.

## 3. Alcance implementado
- Consulta de agenda del médico autenticado.
- Filtros por fecha y estado.
- Carga automática de la agenda del día.
- Integración con JWT y AuthenticatedUserContext.
- Diseño Responsive y Mobile First.

## 4. Arquitectura de la solución
Se reutilizó la arquitectura del módulo Appointment sin crear un nuevo módulo de backend.

Reutilización principal:
- AppointmentController
- AppointmentService
- AppointmentRepository
- AppointmentAgendaResponse

## 5. Backend
### Endpoint
`GET /api/v1/appointments/doctor`

### Servicio
Se incorporó `findDoctorAppointments(...)`, utilizando automáticamente `doctorId` y `clinicId` del contexto autenticado.

### Repositorio
Nueva consulta especializada filtrando por:
- Consultorio
- Médico autenticado
- Fecha
- Estado (opcional)

## 6. Seguridad
- JWT Stateless.
- Role Based Authorization.
- Domain Authorization.
- Uso de `AuthenticatedUserContext`.
- Aislamiento por consultorio y médico.

## 7. Frontend
Nuevo módulo:

`features/doctorAgenda`

Incluye:
- DoctorAgendaPage
- services
- types

Capacidades:
- Consulta automática.
- Filtro por fecha.
- Filtro por estado.
- Estados de carga, vacío y error.
- Responsive.

## 8. Navegación
Se incorporó la ruta protegida:

`/doctor/agenda`

Disponible únicamente para `ROLE_DOCTOR`.

También se actualizó `AccountHomePage` para mostrar la experiencia específica del médico.

## 9. Flujo funcional
1. Inicio de sesión.
2. Validación JWT.
3. Obtención del contexto autenticado.
4. Resolución automática del doctorId.
5. Consulta de la agenda.
6. Aplicación de filtros.
7. Visualización únicamente de citas propias.

## 10. Reglas de negocio
- Solo médicos autenticados.
- Solo citas del propio médico.
- Solo citas del mismo consultorio.
- Fecha obligatoria.
- Estado opcional.
- Orden cronológico.

## 11. Componentes creados
Backend:
- Endpoint de agenda del médico.
- Método de servicio.
- Consulta especializada.

Frontend:
- features/doctorAgenda

## 12. Componentes modificados
Backend:
- AppointmentController
- AppointmentService
- AppointmentServiceImpl
- AppointmentRepository

Frontend:
- App.tsx
- AccountHomePage
- AppointmentService
- appointment.types

## 13. Reutilización
Se reutilizaron:
- AppointmentEntity
- AppointmentAgendaResponse
- AppointmentService
- AppointmentRepository
- AuthenticatedUserContext
- Componentes compartidos del frontend.

## 14. Pruebas
Backend:
- 41 pruebas del módulo: BUILD SUCCESS.
- 148 pruebas totales: BUILD SUCCESS.

Frontend:
- ESLint sin errores.
- Build TypeScript/Vite exitoso.

## 15. Resultado funcional
El Portal del Médico queda operativo y el proyecto pasa a disponer de cuatro experiencias funcionales:
- Recepción Digital
- Portal de Recepción
- Portal del Paciente
- Portal del Médico

## 16. Impacto arquitectónico
La historia valida la reutilización de la arquitectura modular existente, evitando duplicación de lógica y manteniendo el aislamiento por dominio.

## 17. Estado final
**HU-16 — Consultar agenda del médico**

Estado: **✅ Completada**

Con esta historia inicia oficialmente el Sprint 5 y el Portal del Médico pasa a formar parte de las funcionalidades operativas de AgenDoc.

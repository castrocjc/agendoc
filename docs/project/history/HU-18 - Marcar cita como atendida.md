# HU-18 - Marcar cita como atendida

## Información general

- **Épica:** EP-06 – Atención médica básica
- **Sprint:** Sprint 5
- **Estado:** ✅ Completada
- **Story Points:** 3

## Objetivo

Permitir que el médico cierre el ciclo básico de atención marcando una cita confirmada como **ATENDIDA**, validando que exista una observación médica y respetando las reglas de seguridad y autorización del proyecto.

## Alcance implementado

### Backend

- Nuevo endpoint:

```http
PATCH /api/v1/appointments/{appointmentId}/attend
```

- Validación de autenticación mediante JWT.
- Autorización por rol `DOCTOR`.
- Validación de consultorio y médico asignado.
- Transición permitida únicamente:

```text
CONFIRMADA → ATENDIDA
```

- Validación de observación médica obligatoria.
- Reutilización de Repository, DTOs y autorización existentes.
- Sin cambios en la base de datos.
- Sin nuevas migraciones Flyway.

### Frontend

- Integración de la acción **Marcar como atendida** dentro del `MedicalObservationDialog`.
- Confirmación previa antes del cierre de la atención.
- Actualización automática de la agenda médica.
- Manejo homogéneo de errores de negocio.
- Diseño responsive conservado.

## Validaciones funcionales

### Flujo exitoso

1. Médico autenticado.
2. Cita en estado `CONFIRMADA`.
3. Observación médica registrada.
4. Confirmación del cierre.
5. Cambio de estado a `ATENDIDA`.
6. Actualización inmediata de la agenda.

### Casos rechazados

- Cita `PROGRAMADA`.
- Cita `CANCELADA`.
- Cita `NO_ASISTIO`.
- Cita `ATENDIDA`.
- Cita sin observación médica.
- Médico no asignado.
- Usuario sin rol `DOCTOR`.

## Calidad

### Backend

- 163 pruebas automáticas exitosas.
- BUILD SUCCESS.

### Frontend

- ESLint SUCCESS.
- Production Build SUCCESS.

### Prueba funcional

Flujo end-to-end validado satisfactoriamente.

## Archivos modificados

### Backend

- AppointmentController.java
- AppointmentService.java
- AppointmentServiceImpl.java
- AppointmentServiceImplTest.java

### Frontend

- appointmentService.ts
- MedicalObservationDialog.tsx
- MedicalObservationDialog.css
- DoctorAgendaPage.tsx

## Resultado

La HU-18 quedó implementada end-to-end, manteniendo la arquitectura existente, reutilizando la infraestructura desarrollada en la HU-17 y cerrando el ciclo básico de atención médica del Portal del Médico.

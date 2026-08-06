# HU-21 - Cancelar cita desde el Portal del Paciente

> Historia funcional del Portal del Paciente que incorpora la capacidad
> de cancelar citas médicas futuras reutilizando la lógica de negocio
> existente del backend.

  Campo      Valor
  ---------- --------------------------------------------
  Historia   HU-21
  Nombre     Cancelar cita desde el Portal del Paciente
  Estado     ✅ Completada
  Sprint     Sprint 4

------------------------------------------------------------------------

# 1. Objetivo

Permitir que un paciente autenticado pueda cancelar sus propias citas
médicas futuras desde el Portal del Paciente, manteniendo las mismas
reglas de negocio, seguridad y autorización implementadas previamente en
el backend.

------------------------------------------------------------------------

# 2. Alcance funcional

Se incorporó al Portal del Paciente la posibilidad de:

-   Visualizar la acción **Cancelar cita** únicamente cuando la cita es
    elegible.
-   Confirmar la operación mediante un diálogo de confirmación.
-   Registrar un motivo opcional de cancelación.
-   Actualizar automáticamente el historial de citas al finalizar la
    operación.

------------------------------------------------------------------------

# 3. Situación inicial

La lógica de negocio de cancelación ya existía desde la HU-13 para
Recepción y posteriormente fue reforzada con TS-04 y TS-05.

Esta historia reutiliza dicha capacidad sin duplicar lógica de negocio.

------------------------------------------------------------------------

# 4. Reutilización del backend

Se reutilizó el endpoint:

`PATCH /api/v1/appointments/{appointmentId}/cancel`

La historia aprovechó las validaciones existentes de:

-   JWT.
-   Aislamiento por consultorio.
-   Autorización por dominio.
-   Propiedad de la cita.
-   Estado PROGRAMADA.
-   Liberación automática del bloque de agenda.

No fue necesario modificar la lógica del servicio.

------------------------------------------------------------------------

# 5. Diseño técnico

Se incorporaron componentes reutilizables para mantener la consistencia
del Portal del Paciente:

-   PatientAppointmentCard
-   CancelPatientAppointmentDialog

El flujo reutiliza AppointmentService y la infraestructura existente de
consumo de APIs.

------------------------------------------------------------------------

# 6. Reglas de negocio

-   Solo el propietario de la cita puede cancelarla.
-   Solo pueden cancelarse citas en estado PROGRAMADA.
-   Las citas cuyo horario ya inició no muestran la acción de
    cancelación.
-   El motivo de cancelación es opcional.
-   Después de cancelar, la cita permanece en el historial con estado
    CANCELADA.

------------------------------------------------------------------------

# 7. Seguridad

Se mantiene:

-   JWT.
-   Role PATIENT.
-   Domain Authorization.
-   Validación de propiedad de la cita.
-   Validación por consultorio.

------------------------------------------------------------------------

# 8. Experiencia de usuario

Se implementó:

-   Botón contextual "Cancelar cita".
-   Diálogo de confirmación.
-   Contador de caracteres para el motivo.
-   Mensajes homogéneos de éxito y error.
-   Actualización automática del listado sin recargar la página.
-   Diseño responsive y Mobile First.

------------------------------------------------------------------------

# 9. Pruebas ejecutadas

## Backend

-   Pruebas unitarias exitosas.
-   BUILD SUCCESS.

## Frontend

-   ESLint sin errores.
-   TypeScript Build exitoso.
-   Vite Build exitoso.

## Validación End-to-End

Se verificó satisfactoriamente:

-   Visualización condicional del botón.
-   Cancelación con y sin motivo.
-   Actualización de filtros y contadores.
-   Persistencia de la cita cancelada en el historial.
-   Consistencia visual y funcional del Portal del Paciente.

------------------------------------------------------------------------

# 10. Archivos principales modificados

Frontend:

-   features/patientAppointments/pages/PatientAppointmentsPage.tsx
-   features/patientAppointments/components/PatientAppointmentCard.tsx
-   features/patientAppointments/components/CancelPatientAppointmentDialog.tsx
-   AppointmentService
-   Tipos asociados

Documentación:

-   AgenDoc Project Summary
-   HU-21 - Cancelar cita desde el Portal del Paciente

------------------------------------------------------------------------

# 11. Resultado final

La cancelación de citas quedó disponible desde el Portal del Paciente
reutilizando completamente la lógica de negocio existente, manteniendo
la consistencia arquitectónica del proyecto y sin duplicación de
responsabilidades entre frontend y backend.

------------------------------------------------------------------------

# 12. Estado de cierre

-   Backend: ✅
-   Frontend: ✅
-   Pruebas técnicas: ✅
-   Validación funcional End-to-End: ✅
-   Documentación: ✅

**Historia cerrada.**

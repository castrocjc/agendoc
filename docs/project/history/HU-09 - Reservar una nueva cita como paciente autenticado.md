# HU-09 --- Reservar una nueva cita como paciente autenticado

> Historia funcional que incorpora el Portal del Paciente dentro de
> AgenDoc, permitiendo que un paciente autenticado reserve nuevas citas
> médicas utilizando su propio contexto de seguridad, sin intervención
> de la recepción.

  Campo      Valor
  ---------- ---------------------------------------------------
  Proyecto   AgenDoc
  Historia   HU-09
  Nombre     Reservar una nueva cita como paciente autenticado
  Sprint     Sprint 4
  Estado     ✅ Completada
  Tipo       Historia Funcional

## 1. Objetivo

Permitir que un paciente autenticado pueda reservar una nueva cita
médica utilizando su propia cuenta dentro de AgenDoc, reutilizando la
infraestructura de agenda existente y respetando todas las reglas de
negocio del consultorio.

## 2. Problema de negocio

Antes de esta historia únicamente existía la reserva realizada por
recepción. Con HU-09 el paciente puede autogestionar nuevas reservas
desde su portal autenticado.

## 3. Alcance funcional

-   Consultar especialidades.
-   Consultar médicos.
-   Consultar disponibilidad.
-   Seleccionar fecha y horario.
-   Registrar motivo y observaciones.
-   Reservar una nueva cita.
-   Mostrar confirmación de la reserva.

## 4. Arquitectura Backend

Se reutilizó la infraestructura existente incorporando el endpoint:

``` text
POST /api/v1/appointments/patient
```

El paciente y el consultorio se obtienen desde el
`AuthenticatedUserContext`, eliminando la necesidad de enviar
`patientId` desde el frontend.

## 5. Arquitectura Frontend

Se creó el nuevo módulo:

``` text
frontend/src/features/patientAppointment
```

Componentes:

-   pages/PatientAppointmentPage
-   types/patientAppointment.types
-   validation/patientAppointmentValidation

Se reutilizaron:

-   DoctorService
-   AgendaService
-   AppointmentService
-   Componentes AppButton, AppCard, AppInput y AppSelect.

## 6. Seguridad

-   JWT
-   AuthenticatedUserContext
-   Role PATIENT
-   Authorization por dominio
-   Validación de pertenencia al consultorio

## 7. Validaciones

Frontend:

-   Especialidad obligatoria.
-   Médico obligatorio.
-   Fecha obligatoria.
-   Horario obligatorio.

Backend:

-   Médico válido.
-   Agenda válida.
-   Bloque disponible.
-   Fecha futura.
-   Sin conflictos de horario.
-   Paciente perteneciente al consultorio.

## 8. Pruebas realizadas

Backend:

-   140 pruebas.
-   BUILD SUCCESS.

Frontend:

-   ESLint sin errores.
-   TypeScript Build exitoso.
-   Validación funcional completa del flujo.

## 9. Resultado

HU-09 incorpora oficialmente el Portal del Paciente como tercera
experiencia funcional de AgenDoc, reutilizando la arquitectura existente
y manteniendo consistencia visual con la Recepción Digital.

## 10. Historias relacionadas

Dependencias:

-   TS-02
-   TS-03
-   TS-04
-   TS-05
-   HU-04
-   HU-07
-   HU-20

Historias habilitadas:

-   HU-11 --- Consultar mis citas.
-   Evolución del Portal del Paciente.

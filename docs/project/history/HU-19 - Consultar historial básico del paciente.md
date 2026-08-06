# HU-19 - Consultar historial básico del paciente

## Información general

| Campo | Valor |
|-------|-------|
| Épica | EP-06 – Atención médica básica |
| Sprint | Sprint 5 |
| Estado | ✅ Completada |
| Story Points | 5 |

---

# Objetivo

Permitir que el médico consulte las observaciones médicas registradas en atenciones anteriores de un paciente antes de iniciar una nueva atención, reutilizando la infraestructura implementada en las HU-16, HU-17 y HU-18 y manteniendo el aislamiento por consultorio y médico autenticado.

---

# Alcance implementado

## Backend

Se incorporó el endpoint:

```http
GET /api/v1/appointments/{appointmentId}/patient-history
```

La implementación incluye:

- Autenticación mediante JWT.
- Validación del rol `DOCTOR`.
- Aislamiento por consultorio (Clinic Isolation).
- Validación de que la cita pertenece al médico autenticado.
- Resolución del paciente desde la cita seleccionada.
- Consulta únicamente de atenciones anteriores.
- Inclusión exclusiva de citas con estado `ATENDIDA`.
- Exclusión de observaciones vacías.
- Orden cronológico descendente (más reciente primero).
- Reutilización de `AppointmentAuthorizationPolicy`.

### Componentes incorporados

- `PatientMedicalHistoryResponse`
- Nuevo método en `AppointmentRepository`
- Nuevo caso de uso en `AppointmentService`
- Nuevo endpoint en `AppointmentController`

## Frontend

Se incorporó el componente:

```text
PatientHistoryDialog
```

Capacidades:

- Acción **Ver historial** integrada en la agenda del médico.
- Consulta del historial desde cualquier cita asignada.
- Visualización de:
  - fecha;
  - hora;
  - médico tratante;
  - especialidad;
  - observación médica;
  - fecha y usuario de registro.
- Estado de carga.
- Estado vacío.
- Manejo homogéneo de errores.
- Responsive Design.

---

# Evolución de UX

Durante la implementación se refinó la experiencia de usuario para soportar historiales con múltiples registros.

La versión final incorpora:

- Encabezado fijo.
- Información del paciente fija.
- Footer fijo.
- Botón **Cerrar** siempre visible.
- Scroll independiente para el contenido.
- Presentación mediante una línea de tiempo clínica compacta.
- Registros más densos visualmente, dando mayor protagonismo a la observación médica.

---

# Reglas funcionales

1. Solo usuarios con rol `DOCTOR` pueden consultar el historial.
2. El historial parte de una cita válida.
3. La cita debe pertenecer al consultorio autenticado.
4. La cita debe estar asignada al médico autenticado.
5. El paciente se obtiene desde la cita seleccionada.
6. Solo se muestran citas `ATENDIDA`.
7. Solo se muestran observaciones registradas.
8. No se muestran la cita actual ni citas posteriores.
9. El historial es de solo lectura.
10. Los resultados se ordenan del más reciente al más antiguo.

---

# Validaciones realizadas

## Backend

- ✅ 63 pruebas unitarias del módulo Appointment.
- ✅ 167 pruebas automáticas.
- ✅ BUILD SUCCESS.

## Frontend

- ✅ ESLint SUCCESS.
- ✅ Production Build SUCCESS.
- ✅ Validación funcional:
  - paciente sin antecedentes;
  - paciente con múltiples antecedentes;
  - orden cronológico;
  - diseño responsive;
  - timeline clínica;
  - botón de cierre persistente.

---

# Archivos incorporados

## Backend

- `PatientMedicalHistoryResponse.java`

## Frontend

- `PatientHistoryDialog.tsx`
- `PatientHistoryDialog.css`

---

# Resultado

La HU-19 quedó implementada end-to-end.

Con esta historia el Portal del Médico completa el flujo básico de atención:

- HU-16 – Consultar agenda.
- HU-17 – Registrar observación médica.
- HU-18 – Marcar cita como atendida.
- HU-19 – Consultar historial básico del paciente.

La implementación mantiene la arquitectura existente, reutiliza la infraestructura de seguridad por dominio y mejora la experiencia clínica mediante un historial de observaciones compacto, escalable y consistente con el resto del producto.

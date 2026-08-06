# AgenDoc Project Summary

> Documento maestro que resume el estado actual del proyecto AgenDoc.
> Su propósito es proporcionar el contexto necesario para iniciar cualquier nueva sesión de desarrollo sin necesidad de revisar la documentación histórica completa.

| Campo | Valor |
|--------|-------|
| Proyecto | AgenDoc |
| Documento | Project Summary |
| Versión | v1.3 |
| Estado | Vigente |
| Ubicación | docs/project/AgenDoc Project Summary.md |

---

# Historial del documento

| Versión | Fecha | Descripción |
|----------|--------|-------------|
| v1.0 | Agosto 2026 | Creación del documento maestro del proyecto. |
| v1.1 | Agosto 2026 | Incorporación del Product Backlog Maestro, resumen ejecutivo y reorganización del documento para soportar documentación modular por historias. |
| v1.2 | Agosto 2026 | Incorporación de la HU-09 — Reservar una nueva cita como paciente autenticado y actualización del estado funcional del Portal del Paciente. |
| v1.3 | Agosto 2026 | Incorporación de la HU-11 — Consultar mis citas como paciente autenticado y actualización del Portal del Paciente. |

---

# 1. Propósito

Este documento constituye el punto único de entrada al proyecto.

Resume el estado funcional, técnico y arquitectónico de AgenDoc y sirve como referencia para iniciar nuevas sesiones de desarrollo.

Toda la información detallada de implementación se registra individualmente en:

```
docs/project/history/
```

---

# 2. Estado general del proyecto

## Resumen ejecutivo

| Indicador | Valor |
|-----------|------:|
| Foundation | ✅ Completada |
| Sprint actual | Sprint 4 |
| Sprints completados | 3 |
| Historias funcionales completadas | 15 / 20 |
| Habilitadores técnicos | 5 / 5 |
| Estado general | Desarrollo activo |

---

## Última historia completada

**HU-11 — Consultar mis citas como paciente autenticado**

Estado

✅ Completada

---

## Próxima historia

**HU-13 — Cancelar cita desde el Portal del Paciente (Interfaz)**

Estado

⏳ Pendiente

---

# 3. Visión del producto

AgenDoc es una plataforma SaaS para la gestión integral de consultorios médicos.

Su objetivo es digitalizar completamente la operación clínica mediante una arquitectura moderna, segura y escalable.

El desarrollo sigue una estrategia incremental basada en Historias de Usuario.

---

# 4. Product Backlog Maestro

## EP-01 — Acceso, usuarios y roles

| Historia | Estado |
|----------|--------|
| HU-01 — Iniciar sesión | ✅ |
| HU-02 — Cerrar sesión | ✅ |

---

## EP-02 — Gestión base del consultorio

| Historia | Estado |
|----------|--------|
| HU-03 — Disponer del consultorio inicial | ✅ |
| HU-04 — Registrar médico | ✅ |

---

## EP-03 — Gestión de pacientes

| Historia | Estado |
|----------|--------|
| HU-05 — Registrar paciente desde recepción | ✅ |
| HU-06 — Buscar paciente | ✅ |
| HU-20 — Explorar el consultorio y reservar una primera cita desde la Recepción Digital | ✅ |

---

## EP-04 — Agenda médica y disponibilidad

| Historia | Estado |
|----------|--------|
| HU-07 — Crear bloques de agenda médica | ✅ |
| HU-08 — Consultar disponibilidad médica | ✅ |

---

## EP-05 — Gestión de citas médicas

| Historia | Estado |
|----------|--------|
| HU-09 — Reservar una nueva cita como paciente autenticado | ✅ |
| HU-10 — Crear cita desde recepción | ✅ |
| HU-11 — Consultar mis citas como paciente | ✅ |
| HU-12 — Consultar agenda del consultorio | ✅ |
| HU-13 — Cancelar cita | ✅ |
| HU-14 — Reprogramar cita | ✅ |
| HU-15 — Registrar resultado de asistencia | ✅ |

---

## EP-06 — Atención médica básica

| Historia | Estado |
|----------|--------|
| HU-16 — Consultar agenda del médico | ⏳ |
| HU-17 — Registrar observación médica básica | ⏳ |
| HU-18 — Marcar cita como atendida | ⏳ |
| HU-19 — Consultar historial básico del paciente | ⏳ |

---

## Habilitadores técnicos

| Historia | Estado |
|----------|--------|
| TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente | ✅ |
| TS-02 — Completar autenticación JWT End-to-End | ✅ |
| TS-03 — Implementar contexto del usuario autenticado | ✅ |
| TS-04 — Implementar autorización por dominio | ✅ |
| TS-05 — Endurecer seguridad y manejo de accesos no autorizados | ✅ |

---

# 5. Roadmap

| Sprint | Estado |
|---------|--------|
| Foundation | ✅ |
| Sprint 1 | ✅ |
| Sprint 2 | ✅ |
| Sprint 3 | ✅ |
| Sprint 4 | 🚧 En ejecución |
| Sprint 5 | ⏳ Planificado |

---

# 6. Arquitectura

## Frontend

- React
- TypeScript
- Vite
- React Router
- Arquitectura Feature Based
- Componentes reutilizables
- Diseño Responsive
- Mobile First
- Portal del Paciente autenticado
- Reutilización de componentes entre experiencias públicas y autenticadas

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Validation
- Spring Data JPA
- JWT
- Arquitectura Modular
- DTOs mediante Records
- Constructor Injection
- Authorization Policies
- Domain Authorization

## Persistencia

- PostgreSQL
- Flyway
- Hibernate

## Seguridad

- JWT Stateless
- Role Based Authorization
- Clinic Isolation
- Domain Authorization
- Public Endpoints Controlados
- Contexto autenticado por dominio
- Autorización por consultorio

# 7. Estado funcional

## Recepción Digital (Pública)

Estado

✅ Completamente implementada

Capacidades disponibles:

- Landing pública del consultorio mediante URL personalizada.
- Visualización de información pública del consultorio.
- Catálogo público de especialidades.
- Catálogo público de médicos.
- Consulta de disponibilidad médica.
- Registro del primer paciente.
- Creación automática del usuario.
- Reserva automática de la primera cita.
- Inicio del ciclo de vida del paciente dentro de AgenDoc.

---

## Portal de Recepción

Estado

✅ Operativo

Capacidades disponibles:

- Registro de pacientes.
- Búsqueda de pacientes.
- Registro de médicos.
- Creación de agendas médicas.
- Consulta de disponibilidad.
- Creación de citas.
- Consulta de agenda diaria.
- Cancelación de citas.
- Reprogramación de citas.
- Confirmación de llegada.
- Registro de inasistencia.

---

## Portal del Paciente

Estado

🚧 Parcialmente implementado

Capacidades disponibles:

- Inicio de sesión.
- Acceso mediante JWT.
- Contexto autenticado.
- Consulta de médicos.
- Consulta de disponibilidad.
- Reserva de nuevas citas utilizando el paciente autenticado.
- Consulta del historial de citas del paciente autenticado.
- Filtros por próximas, anteriores, canceladas y todas.
- Navegación propia del paciente.
- Experiencia visual consistente con la Recepción Digital.

Pendiente:

- HU-13 — Cancelación desde el portal del paciente (backend disponible, interfaz pendiente).
- Mejoras futuras sobre historial y seguimiento.

---

## Portal del Médico

Estado

⏳ No iniciado

Historias planificadas:

- HU-16
- HU-17
- HU-18
- HU-19

---

# 8. Arquitectura funcional implementada

Actualmente existen cuatro experiencias claramente diferenciadas.

## Recepción Digital

Acceso público.

No requiere autenticación.

Permite convertir visitantes en pacientes registrados.

---

## Recepción

Acceso autenticado.

Rol:

RECEPTIONIST

Funciones administrativas del consultorio.

---

## Paciente

Acceso autenticado.

Rol:

PATIENT

Actualmente permite:

- consultar médicos,
- consultar disponibilidad,
- reservar nuevas citas,
- consultar sus propias citas.

---

## Médico

Reservado para Sprint 5.

---

# 9. Principios arquitectónicos

Durante todo el desarrollo del proyecto se mantienen las siguientes reglas.

## Backend

- Arquitectura modular.
- Servicios desacoplados.
- DTOs mediante Records.
- Constructor Injection.
- Domain Authorization.
- Authorization Policies.
- Validaciones de negocio dentro del Service.
- Controladores delgados.
- Repositorios exclusivamente para acceso a datos.
- Sin lógica de negocio en Controllers.

---

## Frontend

- Arquitectura Feature Based.
- Separación estricta entre:
  - Pages
  - Components
  - Services
  - Types
  - Validation
- Componentes reutilizables.
- Diseño Responsive.
- Mobile First.
- Consistencia visual entre módulos.
- Manejo homogéneo de errores.
- Consumo tipado de APIs.

---

# 10. Estado de calidad

## Backend

Estado

✅ Estable

- 143 pruebas automáticas.
- BUILD SUCCESS.
- Sin errores.
- Seguridad endurecida.
- Autorización por dominio implementada.

---

## Frontend

Estado

✅ Estable

- ESLint sin errores.
- Build exitoso.
- TypeScript limpio.
- Navegación validada.
- Componentes reutilizables.

---

# 11. Funcionalidades incorporadas durante HU-11

La historia HU-11 incorporó la consulta de citas para el paciente autenticado.

Backend:

- nuevo endpoint GET /api/v1/appointments/patient.
- reutilización del contexto autenticado (clinicId y patientId).
- aislamiento por dominio y paciente.
- listado cronológico de citas.

Frontend:

Nuevo módulo:

```
features/patientAppointments
```

Capacidades:

- listado de citas.
- filtros por próximas, anteriores, canceladas y todas.
- navegación hacia la reserva de una nueva cita.
- reutilización de AppointmentService.
- integración con el Portal del Paciente.

---

# 12. Organización documental

La documentación oficial del proyecto está compuesta por:

## Documentos estratégicos

- AgenDoc Project Blueprint
- AgenDoc Development Playbook
- UI Design Guide
- AgenDoc Codebase Guide
- AgenDoc Project Summary

---

## Historial técnico

Cada historia funcional completada posee su propia documentación dentro de:

```

docs/project/history/

```

Esto permite mantener el documento maestro compacto y facilitar la trazabilidad histórica del proyecto.

---

# 13. Próximos pasos

La siguiente historia planificada es:

## HU-13

Incorporar la cancelación de citas directamente desde el Portal del Paciente.

---

# 14. Estado final

El proyecto mantiene un alto nivel de consistencia arquitectónica entre backend y frontend.

Actualmente se dispone de tres experiencias funcionales:

- Recepción Digital pública.
- Portal de Recepción.
- Portal del Paciente.

La seguridad se basa en autenticación JWT, autorización por rol y aislamiento por dominio de consultorio.

La reutilización de servicios y componentes permite acelerar el desarrollo de nuevas funcionalidades manteniendo una experiencia de usuario uniforme y una arquitectura escalable.


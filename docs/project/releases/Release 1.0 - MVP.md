# Release 1.0 — MVP

> Documento oficial de cierre de la primera Release de AgenDoc.
>
> Esta Release representa la culminación del Producto Mínimo Viable (MVP), validando la arquitectura, el modelo multi-tenant y el flujo funcional completo del ciclo de atención médica definido para la primera etapa del proyecto.

| Campo           | Valor                                      |
|-----------------|--------------------------------------------|
| Proyecto        | AgenDoc                                    |
| Documento       | Release 1.0 — MVP                          |
| Versión         | v1.0                                       |
| Estado          | ✅ Cerrada                                 |
| Release         | 1.0                                        |
| Fecha de cierre | Agosto 2026                                |
| Ubicación       | docs/project/releases/Release 1.0 - MVP.md |

---

# Release Overview

## Estado

✅ **RELEASE CERRADA**

---

## Objetivo

Validar la arquitectura funcional y técnica de AgenDoc mediante un Producto Mínimo Viable capaz de soportar el ciclo completo de atención médica de un consultorio.

---

## Resumen ejecutivo

| Indicador                  | Valor                            |
|----------------------------|----------------------------------|
| Foundation                 | ✅                               |
| Sprint 1                   | ✅                               |
| Sprint 2                   | ✅                               |
| Sprint 3                   | ✅                               |
| Sprint 4                   | ✅                               |
| Sprint 5                   | ✅                               |
| Historias Funcionales      | **21**                           |
| Habilitadores Técnicos     | **5**                            |
| Épicas completadas         | **6**                            |
| Experiencias implementadas | **4**                            |
| Backend                    | BUILD SUCCESS                    |
| Frontend                   | Production Build SUCCESS         |
| Próxima Release            | **Release 1.1 — Administration** |

---

## Capacidades entregadas

✅ Recepción Digital

✅ Portal de Recepción

✅ Portal del Paciente

✅ Portal del Médico

---

## Arquitectura validada

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway

### Frontend

- React
- TypeScript
- Vite
- React Router
- Arquitectura Feature Based

### Seguridad

- JWT Stateless
- Role Based Authorization
- Domain Authorization
- Clinic Isolation

### Arquitectura

- Arquitectura Modular
- Multi-Tenant
- DTOs mediante Records
- Constructor Injection

---

## Resultado

La Release 1.0 cumplió completamente el alcance definido para el MVP y estableció la base funcional y arquitectónica para la evolución futura de AgenDoc.

---

# Historial del documento

| Versión  | Fecha       | Descripción                                                       |
|----------|-------------|-------------------------------------------------------------------|
| v1.0     | Agosto 2026 | Creación del documento oficial de cierre de la Release 1.0 (MVP). |

---

# 1. Propósito

Este documento formaliza el cierre oficial de la Release 1.0.

Su objetivo es registrar el alcance alcanzado por el Producto Mínimo Viable, consolidar las capacidades implementadas, documentar el estado de la arquitectura y establecer el punto de partida para las Releases posteriores.

Este documento representa el cierre ejecutivo de la primera etapa del producto y complementa la documentación técnica existente en el historial de Historias de Usuario.

---

# 2. Visión del MVP

La Release 1.0 tuvo como objetivo demostrar que AgenDoc podía soportar completamente el ciclo operativo básico de un consultorio médico utilizando una arquitectura moderna, segura, escalable y preparada para evolucionar.

El alcance del MVP estuvo orientado a validar el flujo completo de atención médica:

Recepción Digital

↓

Recepción

↓

Paciente

↓

Médico

↓

Cierre de atención

No formó parte del objetivo desarrollar una plataforma clínica completa ni una solución SaaS comercial.

---

# 3. Objetivos alcanzados

Durante la Release 1.0 se validó satisfactoriamente:

- Arquitectura SaaS basada en consultorios.
- Multi-Tenant.
- Clinic Isolation.
- JWT Stateless.
- Role Based Authorization.
- Domain Authorization.
- Arquitectura modular.
- Arquitectura Feature Based.
- Flujo completo de atención médica.
- Reutilización de componentes.
- Base tecnológica preparada para evolucionar.

---

# 4. Alcance funcional entregado

## Recepción Digital

- Landing pública.
- Información del consultorio.
- Consulta pública de médicos.
- Consulta pública de especialidades.
- Consulta de disponibilidad.
- Registro del primer paciente.
- Creación automática del usuario.
- Primera reserva.

---

## Portal de Recepción

- Registro de pacientes.
- Registro de médicos.
- Agenda médica.
- Creación de citas.
- Consulta de agenda.
- Reprogramación.
- Cancelación.
- Confirmación de llegada.
- Registro de inasistencia.

---

## Portal del Paciente

- Inicio de sesión.
- Consulta de disponibilidad.
- Reserva de nuevas citas.
- Consulta del historial.
- Cancelación de citas futuras.

---

## Portal del Médico

- Consulta de agenda.
- Registro de observaciones.
- Consulta del historial.
- Marcar atención como completada.

---

# 5. Backlog ejecutado

## Épicas

- EP-01 — Acceso, usuarios y roles.
- EP-02 — Gestión base del consultorio.
- EP-03 — Gestión de pacientes.
- EP-04 — Agenda médica.
- EP-05 — Gestión de citas.
- EP-06 — Atención médica básica.

---

## Historias

21 de 21 completadas.

---

## Habilitadores Técnicos

TS-01
TS-02
TS-03
TS-04
TS-05

Todos completados.

---

# 6. Arquitectura consolidada

## Backend

...

## Frontend

...

## Persistencia

...

## Seguridad

...

(Esta sección puede reutilizar el contenido del Summary para evitar inconsistencias futuras.)

---

# 7. Métricas de la Release

(igual que la portada, pero con mayor detalle)

---

# 8. Principales logros

- Flujo completo de atención médica.
- Arquitectura escalable.
- Seguridad consolidada.
- Reutilización de componentes.
- Cuatro experiencias funcionales completas.

---

# 9. Funcionalidades deliberadamente fuera del alcance

## Administración

- Portal Administración.
- Gestión de usuarios.
- Gestión de roles.
- Configuración del consultorio.

## Agenda

- Agenda masiva.
- Excepciones.
- Configuración avanzada.

## Clínico

- Historia clínica.
- Diagnósticos.
- Recetas.
- Adjuntos.

## Plataforma

- Demo pública.
- Vercel.
- Railway.
- Integraciones.

Estas funcionalidades no representan deuda técnica del MVP.

Representan la evolución natural del producto.

---

# 10. Lecciones aprendidas

Registrar aquí las principales conclusiones obtenidas durante el desarrollo del MVP.

---

# 11. Criterios oficiales de cierre

La Release 1.0 se declara oficialmente completada cuando:

- Foundation completada.
- Sprint 1 al Sprint 5 completados.
- 21 Historias finalizadas.
- 5 Habilitadores completados.
- Arquitectura consolidada.
- Seguridad implementada.
- Backend BUILD SUCCESS.
- Frontend Production Build SUCCESS.
- Documentación sincronizada.

---

# 12. Evolución del producto

La siguiente etapa corresponde a:

**Release 1.1 — Administration**

Objetivo:

Transformar AgenDoc de un MVP funcional a un producto administrable para un consultorio real.

---

# 13. Declaración oficial de cierre

Con la aprobación de este documento se declara oficialmente concluida la Release 1.0 — MVP.

A partir de este momento, la evolución funcional del producto continuará mediante Releases independientes, comenzando por la Release 1.1 — Administration, manteniendo la arquitectura, principios y estándares definidos durante el desarrollo del MVP.
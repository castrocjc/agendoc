# AgenDoc Project Summary

> Documento maestro que resume el estado actual del proyecto AgenDoc.
> Su propósito es proporcionar el contexto necesario para iniciar cualquier nueva sesión de desarrollo sin necesidad de revisar la documentación histórica completa.

| Campo | Valor |
|--------|-------|
| Proyecto | AgenDoc |
| Documento | Project Summary |
| Versión | v1.1 |
| Estado | Vigente |
| Ubicación | docs/project/AgenDoc Project Summary.md |

---

# Historial del documento

| Versión | Fecha | Descripción |
|----------|--------|-------------|
| v1.0 | Agosto 2026 | Creación del documento maestro del proyecto. |
| v1.1 | Agosto 2026 | Incorporación del Product Backlog Maestro, resumen ejecutivo y reorganización del documento para soportar documentación modular por historias. |

---

# 1. Propósito

Este documento constituye el punto único de entrada al proyecto.

Resume el estado funcional, técnico y arquitectónico de AgenDoc y sirve como referencia para iniciar nuevas sesiones de desarrollo.

Toda la información detallada de implementación se registra individualmente en:
docs/project/history/


---

# 2. Estado general del proyecto

## Resumen ejecutivo

| Indicador | Valor |
|-----------|------:|
| Foundation | ✅ Completada |
| Sprint actual | Sprint 4 |
| Sprints completados | 4 |
| Historias funcionales completadas | 13 / 20 |
| Habilitadores técnicos | 5 / 5 |
| Estado general | Desarrollo activo |

---

## Última historia completada

**HU-20 — Explorar el consultorio y reservar una primera cita desde la Recepción Digital**

Estado

✅ Completada

---

## Próxima historia

**HU-09 — Reservar una nueva cita como paciente autenticado**

Estado

⏳ Lista para iniciar

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
| HU-09 — Reservar una nueva cita como paciente autenticado | ⏳ |
| HU-10 — Crear cita desde recepción | ✅ |
| HU-11 — Consultar mis citas como paciente | ⏳ |
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

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Validation
- Spring Data JPA
- JWT
- Arquitectura Modular

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

---

# 7. Estado funcional

## Autenticación

✅ Completa

Incluye Login, Logout, JWT, Protected Routes, manejo de sesión y redirección por rol.

## Gestión de Médicos

✅ Completa

## Gestión de Pacientes

✅ Completa

## Agenda Médica

✅ Completa

## Gestión de Citas

✅ Completa para recepción.

Pendiente el Portal del Paciente.

## Recepción Digital

✅ HU-20 completada.

Incluye:

- Portal público
- Identidad pública del consultorio
- Especialidades
- Médicos
- Disponibilidad
- Wizard de reserva
- Registro automático del paciente
- Creación automática del usuario
- Primera cita
- Responsive
- Mobile Ready

## Portal del Paciente

🚧 En construcción

Incluye actualmente:

- Home del paciente
- Redirección automática por rol

Pendiente:

- Mis citas
- Reservar citas
- Cancelar citas
- Perfil
- Cambio de contraseña

---

# 8. Estado técnico

## Backend

- Arquitectura modular
- Controllers
- Services
- DTO Records
- Authorization Policies
- Exception Handling
- Seguridad por dominio

## Frontend

- Arquitectura Feature Based
- Componentes reutilizables
- Diseño Responsive
- Servicios desacoplados
- Wizard de navegación
- Tipado completo

---

# 9. Base de datos

Motor

PostgreSQL

Migración vigente

V12

---

# 10. Calidad

Backend

- 140 pruebas automatizadas exitosas.

Frontend

- ESLint
- TypeScript
- Production Build

Pruebas funcionales

- Flujo End-to-End validado para HU-20.

---

# 11. Documentación oficial

Documentación permanente

- AgenDoc Project Summary
- AgenDoc Development Playbook
- AgenDoc Codebase Guide
- UI Design Guide

Documentación evolutiva
docs/

architecture/
product/
ux/

project/

AgenDoc Project Summary.md

history/

HU-01 - ...
HU-02 - ...
...
HU-20 - Public Digital Reception.md
HU-09 - Reservar una nueva cita como paciente autenticado.md


---

# 15. Flujo documental

Al finalizar cada Historia de Usuario:

1. Actualizar Project Summary.
2. Crear o actualizar el documento de la historia.
3. Ejecutar pruebas.
4. Sincronizar documentación oficial.
5. Versionar.

---

# 16. Observaciones

Este documento resume únicamente el estado vigente del proyecto.

Las decisiones de implementación, pruebas, incidencias, cambios arquitectónicos y resultados de cada incremento se documentan exclusivamente en el archivo correspondiente de cada Historia de Usuario.

El objetivo es mantener este documento como el punto de entrada oficial al proyecto, facilitando el inicio de nuevas sesiones de desarrollo sin depender de la documentación histórica completa.


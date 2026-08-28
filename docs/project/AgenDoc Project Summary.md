# AgenDoc Project Summary

> Documento ejecutivo que resume el estado actual del proyecto AgenDoc.
>
> Constituye el punto de entrada oficial a la documentación del producto y presenta una visión consolidada de la arquitectura, el estado de desarrollo, la organización documental y el roadmap evolutivo.
>
> La planificación detallada de cada etapa del producto se mantiene en los documentos de Release correspondientes.

---

# Estado General del Proyecto

## Resumen Ejecutivo

| Indicador | Estado |
|-----------|---------|
| Foundation | ✅ Completada |
| Sprint 1 | ✅ Completado |
| Sprint 2 | ✅ Completado |
| Sprint 3 | ✅ Completado |
| Sprint 4 | ✅ Completado |
| Sprint 5 | ✅ Completado |
| Sprint 6 | 🚧 En ejecución |
| Historias Funcionales | **21 / 21** |
| Habilitadores Técnicos | **5 / 5** |
| Release anterior | ✅ Release 1.0 — MVP |
| Release actual | 🚧 Release 1.1 — Administration |
| Estado del proyecto | Evolución Post-MVP |

---

# Estado de Releases

| Release | Estado | Documento |
|----------|--------|-----------|
| Release 1.0 — MVP | ✅ Cerrada | `docs/project/releases/Release 1.0 - MVP.md` |
| Release 1.1 — Administration | 🚧 Sprint 6 en ejecución | `docs/project/releases/Release 1.1 — Administration.md` |

---

# Roadmap del Producto

| Release | Estado |
|----------|---------|
| Release 1.0 — MVP | ✅ Finalizada |
| Release 1.1 — Administration | 🚧 En ejecución |
| Release 1.2 — Operational Excellence | 📋 Planificada |
| Release 2.0 — Clinical Platform | 🔮 Futuro |

---

# Estado Funcional

La Release 1.0 consolidó el Producto Mínimo Viable (MVP) de AgenDoc.

Actualmente el producto soporta completamente el flujo operativo básico de un consultorio médico mediante cuatro experiencias principales:

- Recepción Digital.
- Workspace de Recepción.
- Workspace del Paciente.
- Workspace del Médico.

La siguiente etapa corresponde a la Release 1.1, cuyo objetivo consiste en transformar AgenDoc en una plataforma administrable y operable de forma autónoma por un consultorio médico.

---

# Arquitectura Consolidada

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Arquitectura Modular
- DTOs mediante Records
- Constructor Injection

---

## Seguridad

- JWT Stateless
- Role Based Authorization
- Domain Authorization
- Clinic Isolation

---

## Frontend

- React
- TypeScript
- Vite
- React Router
- Arquitectura Feature Based
- Responsive Design
- Mobile First

---

# Estado de Calidad

## Backend

- 169 pruebas automáticas exitosas.
- BUILD SUCCESS.

## Frontend

- ESLint SUCCESS.
- TypeScript SUCCESS.
- Production Build SUCCESS.

---

# Organización Documental

```
docs/project/

AgenDoc Project Blueprint.md
AgenDoc Development Playbook.md
AgenDoc Codebase Guide.md
AgenDoc UI Design Guide.md
AgenDoc Project Summary.md

releases/

Release 1.0 - MVP.md
Release 1.1 — Administration.md

history/

HU-...
TS-...
```

---

# Gobierno del Producto

A partir de la Release 1.1, AgenDoc adopta un modelo de evolución basado en Releases.

La trazabilidad oficial del producto será:

```
Blueprint
      │
      ▼
Release
      │
      ▼
Sprint
      │
      ▼
Historia de Usuario
      │
      ▼
History
```

Toda Historia de Usuario deberá pertenecer explícitamente a una Release antes de ser incorporada a un Sprint.

---

# Próximos Pasos

El Product Backlog de la Release 1.1 se encuentra refinado y el Sprint 6 está oficialmente en ejecución. TS-07 y HU-22 quedaron implementados y validados como fundación técnica y funcional de la experiencia administrativa inicial.

El objetivo inmediato consiste en:

- continuar con HU-24 — Gestionar especialidades médicas como primer vertical slice administrativo de negocio;
- preservar el aislamiento entre ADMIN y los roles existentes;
- continuar evolucionando los componentes administrativos de TS-12 de forma incremental;
- mantener las capacidades administrativas delegables dentro del alcance de TS-09 sin adelantar TS-08.

---

# Estado Final

AgenDoc ha concluido exitosamente la Release 1.0, correspondiente al Producto Mínimo Viable (MVP).

El proyecto se encuentra ejecutando su primera etapa evolutiva mediante la Release 1.1 — Administration. El Sprint 6 está en curso. TS-07 estableció la fundación inicial para acceso ADMIN seguro y HU-22 convirtió esa base en un Workspace administrativo funcional, manteniendo autenticación, autorización, Clinic Isolation y los principios arquitectónicos establecidos durante el MVP.
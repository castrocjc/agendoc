# AgenDoc Codebase Guide

> Documento oficial que describe el estado actual de la implementación del proyecto AgenDoc.
> La organización física del código descrita en este documento complementa la estructura del repositorio definida en el Development Playbook.

| Campo     | Valor                                       |
|-----------|---------------------------------------------|
| Proyecto  | AgenDoc                                     |
| Documento | Codebase Guide                              |
| Versión   | v1.7                                        |
| Estado    | Vigente                                     |
| Ubicación | docs/architecture/AgenDoc Codebase Guide.md |

---

Historial del Documento

| Versión  | Fecha      | Descripción                          |
|----------|------------|--------------------------------------|
| v1.0     | 2026-07-14 | Creación inicial del Codebase Guide. |
| v1.1     | 2026-07-29 | Actualización Codebase Guide completa HU-10, creación de citas médicas desde recepción. |
| v1.2     | 2026-07-30 | Actualización Codebase Guide completa HU-12, consulta de la agenda del consultorio. |
| v1.3     | 2026-07-30 | Actualización Codebase Guide completa HU-13 (Cancelar cita) y HU-14 (Reprogramar cita). |
| v1.4     | 2026-07-31 | Actualización Codebase Guide completa HU-15, confirmación de llegada y registro de inasistencia del paciente, y cierre funcional del Sprint 3. |
| v1.5     | 2026-08-01 | Actualización Codebase Guide completa TS-02, autenticación JWT End-to-End, protección de APIs y cierre técnico de la autenticación. |
| v1.6     | 2026-08-02 | Actualización Codebase Guide completa TS-03, implementación del contexto del usuario autenticado mediante SecurityContext y resolución del usuario autenticado en el Backend. |
| v1.7 | 2026-08-03 | Sincronización completa de la documentación posterior al cierre de TS-03. Se actualizó el estado del proyecto para iniciar TS-04 y se alinearon referencias internas del Codebase Guide. |

---

Relación con la documentación oficial

| Documento            | Responsabilidad                       |
|----------------------|---------------------------------------|
| Blueprint            | Define el producto.                   |
| Development Playbook | Define el proceso de desarrollo.      |
| UI Design Guide      | Define la experiencia visual.         |
| Codebase Guide       | Describe la implementación existente. |

---

# Índice

1. Propósito
2. Estado Actual de la Implementación
3. Tecnologías
4. Estructura del Repositorio
5. Arquitectura Implementada
6. Backend
7. Frontend Web
8. Mobile
9. Base de Datos
10. APIs Implementadas
11. Componentes Reutilizables
12. Inventario Funcional
13. Technical Stories
14. Configuración del Proyecto
15. Checklist Técnico
16. Mapa de Navegación del Código
17. Mapa de Dependencias
18. Próximo Incremento
19. Historial de Implementación
20. Convenciones de Actualización

---

# 1. Propósito

## Objetivo

El AgenDoc Codebase Guide es el documento oficial que describe el estado actual de la implementación del proyecto AgenDoc.

Su objetivo es proporcionar una referencia técnica rápida sobre la estructura del repositorio, los módulos implementados, las APIs disponibles, los componentes reutilizables y el avance funcional del producto.

Este documento complementa al AgenDoc Project Blueprint, al AgenDoc Development Playbook y al UI Design Guide, actuando como la referencia oficial del código implementado.

## Alcance

Este documento registra exclusivamente información correspondiente a la implementación existente en el repositorio.

Incluye como mínimo:

- estructura del repositorio;
- organización del Backend, Frontend Web y Mobile;
- estructura de la base de datos implementada;
- APIs disponibles;
- componentes reutilizables;
- inventario funcional implementado;
- estado de las Technical Stories;
- estado técnico del proyecto.

No contiene:

- visión del producto;
- decisiones funcionales;
- modelo de dominio;
- modelo de datos conceptual;
- decisiones arquitectónicas;
- reglas de UX/UI;
- backlog del producto;
- planificación de Sprints.

Toda esa información pertenece exclusivamente al AgenDoc Project Blueprint, al AgenDoc Development Playbook y al UI Design Guide.

## Principios

El Codebase Guide se rige por los siguientes principios:

- Reflejar únicamente funcionalidades implementadas.
- Mantener una única fuente de verdad sobre la implementación.
- Evitar duplicar información existente en otros documentos oficiales.
- Facilitar la incorporación de nuevos desarrolladores al proyecto.
- Reducir el tiempo de análisis antes de iniciar una nueva sesión de desarrollo.
- Mantener sincronizada la documentación con el código fuente.

---

# 2. Estado Actual de la Implementación

## Información General

| Campo               | Valor                       |
|---------------------|-----------------------------|
| Estado del proyecto | En desarrollo               |
| Sprint actual       | Sprint 4                    |
| Foundation          | Completada                  |
| Sprint 1            | Completado                  |
| Sprint 2            | Completado                  |
| Sprint 3            | Completado                  |

Sprint 4: 🚧 En ejecución
Incremento activo:
TS-04 — Implementar autorización por dominio
Historia funcional objetivo: HU-09 — Reservar cita como paciente

---

## Documentación Oficial

| Documento                    | Versión | Estado         |
|------------------------------|---------|----------------|
| AgenDoc Project Blueprint    | v1.9    | Aprobado       |
| AgenDoc Development Playbook | v1.4    | Aprobado       |
| UI Design Guide              | v1.1    | Aprobado       |
| AgenDoc Codebase Guide       | v1.7    | Vigente        |

---

## Estado del Repositorio

| Elemento                                | Estado                |
|-----------------------------------------|-----------------------|
| Rama principal de trabajo               | develop               |
| develop sincronizada con origin/develop | Sí                    |
| Working Tree                            | Limpio                |
| Estrategia de ramas                     | Git Flow simplificado |

---

## Foundation

Estado: **Completada**

Componentes habilitados:

- Backend Spring Boot.
- Frontend Web con React + TypeScript + Vite.
- Aplicación Mobile con React Native + Expo.
- PostgreSQL.
- Flyway.
- Spring Security.
- JWT (implementación inicial).
- GitHub Actions.
- GitHub CLI.
- Logging.
- Manejo global de excepciones.
- Configuración por perfiles.
- Estructura oficial del repositorio.

---

## Historias de Usuario Implementadas

| Historia | Nombre                            | Estado                           |
|----------|-----------------------------------|----------------------------------|
| HU-01    | Iniciar sesión                    | ✅ Completada                    |
| HU-02    | Cerrar sesión                     | ✅ Completada                    |
| HU-03    | Consultorio base                  | ✅ Satisfecha durante Foundation |
| HU-04    | Registrar médico                  | ✅ Completada                    |
| HU-05    | Registrar paciente                | ✅ Completada                    |
| HU-06    | Buscar paciente                   | ✅ Completada                    |
| HU-07    | Crear bloques de agenda médica    | ✅ Completada                    |
| HU-08    | Consultar disponibilidad médica   | ✅ Completada                    |
| HU-10    | Crear cita desde recepción        | ✅ Completada                    |
| HU-12    | Consultar agenda del consultorio  | ✅ Completada                    |
| HU-13    | Cancelar cita                     | ✅ Completada                    |
| HU-14    | Reprogramar cita                  | ✅ Completada                    |
| HU-15    | Registrar resultado de asistencia | ✅ Completada                    |

Total implementado:

- Foundation completada.
- Sprint 1 completado.
- HU-08 completada.
- HU-10 completada de extremo a extremo.
- HU-12 completada de extremo a extremo.
- HU-13 completada de extremo a extremo.
- HU-14 completada de extremo a extremo.
- HU-15 completada de extremo a extremo.
- Sprint 3 completado funcionalmente.

---

## Technical Stories

| ID    | Nombre                                                             | Estado     |
|-------|--------------------------------------------------------------------|------------|
| TS-01 | Configurar entorno de desarrollo multidispositivo y multiambiente  | Completada |
| TS-02 | Completar autenticación JWT End-to-End                             | Completada |
| TS-03 | Implementar contexto del usuario autenticado                       | Completada |
| TS-04 | Implementar autorización por dominio                               | Completada |
| TS-05 | Endurecer seguridad y manejo de accesos no autorizados             | Pendiente  |

---

## Próximo Incremento

Historia objetivo

TS-04 — Implementar autorización por dominio.

Historia funcional objetivo:
HU-09 — Reservar cita como paciente.

Estado actual

- Sprint 3 completado funcionalmente.
- HU-13, HU-14 y HU-15 completadas.
- Backend compilando correctamente.
- Frontend compilando correctamente.
- Pruebas automatizadas exitosas.
- Validación funcional de extremo a extremo completada.

Objetivo

Iniciar el Sprint 4 con el flujo de autogestión del paciente, conforme al Product Backlog vigente.

---

# 3. Tecnologías

## Objetivo

Esta sección documenta las tecnologías utilizadas actualmente en la implementación de AgenDoc.

Su propósito es proporcionar una visión rápida del stack tecnológico del proyecto y servir como referencia para el desarrollo y mantenimiento del software.

Únicamente se documentan tecnologías incorporadas al repositorio.

---

## Backend

| Tecnología      | Propósito                                            |
|-----------------|------------------------------------------------------|
| Java            | Lenguaje principal del Backend.                      |
| Spring Boot     | Framework para el desarrollo de la API REST.         |
| Spring Security | Seguridad y protección de los endpoints.             |
| Spring Data JPA | Persistencia de datos.                               |
| Hibernate       | Implementación JPA utilizada por Spring Data.        |
| Maven           | Gestión de dependencias y construcción del proyecto. |
| Flyway          | Control de versiones del esquema de Base de Datos.   |

---

## Frontend Web

| Tecnología   | Propósito                                |
|--------------|------------------------------------------|
| React        | Construcción de la interfaz de usuario.  |
| TypeScript   | Desarrollo tipado del Frontend.          |
| Vite         | Herramienta de desarrollo y empaquetado. |
| React Router | Navegación entre pantallas.              |
| Fetch API    | Consumo centralizado de la API REST mediante apiClient.ts. |
| Lucide React | Biblioteca oficial de iconografía.       |

---

## Mobile

| Tecnología   | Propósito                                    |
|--------------|----------------------------------------------|
| React Native | Desarrollo de la aplicación móvil.           |
| Expo         | Plataforma de desarrollo y ejecución Mobile. |
| TypeScript   | Desarrollo tipado de la aplicación móvil.    |

---

## Base de Datos

| Tecnología | Propósito                              |
|------------|----------------------------------------|
| PostgreSQL | Base de datos relacional del proyecto. |

---

## Control de Versiones

| Tecnología     | Propósito                                |
|----------------|------------------------------------------|
| Git            | Control de versiones distribuido.        |
| GitHub         | Repositorio remoto del proyecto.         |
| GitHub CLI     | Gestión del flujo Git desde la terminal. |
| GitHub Actions | Automatización de integración continua.  |

---

## Herramientas de Desarrollo

| Herramienta        | Propósito                                                 |
|--------------------|-----------------------------------------------------------|
| IntelliJ IDEA      | Desarrollo del Backend.                                   |
| Visual Studio Code | Desarrollo del Frontend y documentación.                  |
| Postman            | Pruebas funcionales de la API REST.                       |
| DBeaver            | Administración y consulta de la Base de Datos PostgreSQL. |
| GitHub CLI         | Gestión del flujo Git desde la terminal.                  |
| Maven              | Compilación y ejecución del Backend.                      |

---

## Estado Actual

El stack tecnológico se encuentra alineado con la arquitectura definida para el MVP.

La incorporación de nuevas tecnologías durante el desarrollo deberá justificarse técnicamente y reflejarse tanto en este documento como en el AgenDoc Development Playbook cuando implique cambios en el proceso de desarrollo.

---

**Última actualización**

Sprint:
Sprint 1

Sesión:
Cierre del Sprint 1

---

# 4. Estructura del Repositorio

## Objetivo

Esta sección describe la organización física oficial del repositorio de AgenDoc.

Su propósito es facilitar la localización de los distintos componentes del proyecto y mantener una visión actualizada de la estructura implementada.

La estructura aquí documentada deberá reflejar siempre el estado real del repositorio.

---

## Estructura General

```text
agendoc/
│
├── backend/
│
├── frontend/
│
├── mobile/
│
├── database/
│
├── docker/
│
├── scripts/
│
├── docs/
│   │
│   ├── architecture/
│   │   └── AgenDoc Codebase Guide.md
│   │
│   ├── product/
│   │   ├── AgenDoc Project Blueprint.md
│   │   └── AgenDoc Development Playbook.md
│   │
│   └── ux/
│       └── UI Design Guide.md
│
├── .github/
│
├── README.md
├── .gitignore
├── .editorconfig
├── .gitattributes
└── .env.example
```

---

## Responsabilidad de cada Directorio

| Directorio   | Responsabilidad                                                           |
|--------------|---------------------------------------------------------------------------|
| backend      | Backend API desarrollado con Spring Boot.                                 |
| frontend     | Aplicación Web desarrollada con React + TypeScript + Vite.                |
| mobile       | Aplicación Mobile desarrollada con React Native + Expo.                   |
| database     | Migraciones Flyway, scripts y recursos relacionados con la base de datos. |
| docker       | Recursos reservados para una futura incorporación de contenedores.        |
| scripts      | Scripts auxiliares del proyecto.                                          |
| docs         | Documentación oficial del proyecto.                                       |
| .github      | Configuración de GitHub Actions y automatizaciones.                       |

---

## Organización de la Documentación

La documentación oficial del proyecto se organiza de la siguiente manera:

| Carpeta           | Contenido                                                 |
|-------------------|-----------------------------------------------------------|
| docs/product      | Documentación funcional y de proceso del proyecto.        |
| docs/architecture | Documentación técnica de la implementación.               |
| docs/ux           | Documentación del diseño visual y experiencia de usuario. |

---

## Regla de Organización

Toda nueva carpeta de primer nivel deberá responder a una necesidad permanente del proyecto y mantener una única responsabilidad.

Se evitará incorporar estructuras paralelas o duplicadas que dificulten la navegación del repositorio.

---

**Última actualización**

Sprint 1 — Cierre del Sprint 1

---

# 5. Arquitectura Implementada

## Objetivo

Esta sección describe cómo se encuentra implementada actualmente la arquitectura de AgenDoc en el código fuente.

No reemplaza la arquitectura conceptual definida en el AgenDoc Project Blueprint. Su propósito es documentar la materialización de dicha arquitectura dentro del repositorio.

---

## Visión General

La implementación actual sigue una arquitectura de Monolito Modular, donde el Backend concentra la lógica de negocio y expone una API REST consumida por los clientes Web y Mobile.

Cada módulo encapsula una responsabilidad funcional del dominio y mantiene una separación clara entre sus componentes internos.

La comunicación entre los clientes y el Backend se realiza mediante servicios HTTP REST.

---

## Arquitectura del Producto

```text
                    +----------------------+
                    |    Frontend Web      |
                    | React + TypeScript   |
                    +----------+-----------+
                               |
                               |
                               | HTTP REST
                               |
                    +----------v-----------+
                    |       Backend        |
                    |    Spring Boot API   |
                    +----------+-----------+
                               |
                               |
                        Spring Data JPA
                               |
                    +----------v-----------+
                    |     PostgreSQL       |
                    +----------------------+

                    Mobile (React Native + Expo)
                             │
                             └──────► API REST
```

---

## Organización del Backend

El Backend implementa una arquitectura modular organizada por contexto funcional.

La estructura principal se divide en:

- Componentes transversales.
- Módulos de negocio.

Los componentes transversales contienen funcionalidades compartidas por toda la aplicación, mientras que los módulos encapsulan la lógica propia de cada contexto del dominio.

---

## Organización del Frontend Web

El Frontend Web implementa una arquitectura basada en componentes reutilizables.

La solución se organiza en:

- páginas;
- componentes reutilizables;
- servicios de integración;
- utilitarios;
- recursos estáticos.

La navegación se realiza mediante React Router y el consumo de servicios se centraliza mediante `apiClient.ts`, construido sobre Fetch API

---

## Organización de la Aplicación Mobile

La aplicación Mobile se desarrolla utilizando React Native y Expo.

La aplicación Mobile mantiene actualmente su estructura base y permanece pendiente de integración funcional con el Backend.

---

## Persistencia

La persistencia de datos se implementa utilizando PostgreSQL como base de datos relacional.

El control de versiones del esquema se realiza mediante Flyway, garantizando que todos los entornos compartan la misma estructura de base de datos.

---

## Seguridad

La autenticación se implementa mediante Spring Security y JSON Web Token (JWT).

El Backend protege los endpoints mediante autenticación basada en JWT y el Frontend Web administra la sesión del usuario almacenando el token de acceso para consumir las APIs protegidas.

La autenticación utiliza Spring Security, JWT y el SecurityContext para resolver el usuario autenticado durante la ejecución de cada solicitud protegida.

El contexto autenticado constituye la base para las reglas de autorización por dominio y las futuras funcionalidades de autogestión del paciente.

La autorización por dominio se encuentra implementada mediante dos niveles complementarios:

- autorización declarativa de endpoints utilizando `@PreAuthorize`;
- validaciones de dominio basadas en rol, consultorio y propiedad del recurso.

El usuario autenticado se resuelve desde `SecurityContext` y proporciona el identificador de usuario, rol, consultorio y perfil de negocio asociado cuando corresponde.

La infraestructura de autorización evita depender de identificadores enviados libremente por el Frontend cuando estos pueden derivarse del contexto autenticado.

La siguiente etapa de evolución corresponde a:

- TS-05: Endurecer seguridad y manejo de accesos no autorizados.

---

## Principios de Implementación

La implementación actual sigue los siguientes principios:

- separación por módulos funcionales;
- responsabilidad única por componente;
- desacoplamiento entre capas;
- reutilización de componentes;
- persistencia centralizada;
- configuración por perfiles;
- migraciones versionadas mediante Flyway;
- integración continua mediante GitHub Actions.

---

## Estado Actual

La arquitectura implementada soporta las funcionalidades desarrolladas durante Foundation, Sprint 1, Sprint 2 y Sprint 3.

La evolución de la arquitectura deberá mantenerse alineada con las decisiones registradas en el AgenDoc Project Blueprint.

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Cierre funcional de HU-15

---

# 6. Backend

## 6.1 Objetivo

Esta sección describe la estructura real y el estado actual de la implementación del Backend de AgenDoc.

El Backend centraliza las reglas de negocio, validaciones, seguridad, persistencia y contratos REST utilizados por los clientes de la plataforma.

La información documentada en esta sección se basa exclusivamente en los archivos existentes dentro de:

```text
backend/src/main/java/com/agendoc
```

---

## 6.2 Estructura General

El Backend se encuentra organizado en dos grupos principales:

```text
com.agendoc
├── common
├── config
├── modules
└── security
```

### Paquetes transversales

| Paquete  | Responsabilidad                                                              |
|----------|------------------------------------------------------------------------------|
| common   | Entidades base, auditoría, estados comunes y manejo global de excepciones.   |
| config   | Espacio reservado para configuraciones generales de la aplicación.           |
| security | Configuración de contraseñas, Spring Security y autenticación basada en JWT. |

### Módulos funcionales

```text
modules
├── agenda
├── appointment
├── authentication
├── clinic
├── doctor
├── patient
├── role
└── user
```

Los módulos funcionales agrupan los componentes relacionados con cada contexto del producto.

---

## 6.3 Aplicación Principal

### BackendApplication.java

Ubicación:

```text
backend/src/main/java/com/agendoc/BackendApplication.java
```

Responsabilidad:

- Inicializar la aplicación Spring Boot.
- Configurar el escaneo de componentes del Backend.
- Actuar como punto de entrada de la aplicación.

---

## 6.4 Componentes Transversales

### 6.4.1 Common

Ubicación:

```text
backend/src/main/java/com/agendoc/common
```

#### Entidades comunes

```text
common/entity
├── AuditableEntity.java
├── BaseEntity.java
└── RecordStatus.java
```

Responsabilidades:

- Proporcionar atributos comunes a las entidades persistentes.
- Centralizar los campos de auditoría.
- Definir el estado lógico de los registros.
- Evitar duplicación entre entidades del dominio.

#### Manejo de excepciones

```text
common/exception
├── ApiError.java
├── BadRequestException.java
├── ConflictException.java
├── GlobalExceptionHandler.java
└── ResourceNotFoundException.java
```

Responsabilidades:

- Mantener una estructura uniforme para los errores de la API.
- Gestionar excepciones funcionales y de validación.
- Evitar la exposición de trazas internas al Frontend.
- Traducir excepciones a respuestas HTTP consistentes.

Estado:

**Implementado**

---

### 6.4.2 Config

Ubicación:

```text
backend/src/main/java/com/agendoc/config
```

Estado actual:

El paquete existe como parte de la estructura oficial del Backend.

No se identificaron clases dentro del paquete durante la revisión posterior al Sprint 1.

Estado:

**Estructura disponible**

---

### 6.4.3 Security

Ubicación:

```text
backend/src/main/java/com/agendoc/security
```

Estructura actual:

```text
security
├── authorization
│   ├── AuthenticatedUserAuthorization.java
│   ├── AuthorizationDeniedException.java
│   └── SecurityRoleCode.java
├── config
│   ├── PasswordConfiguration.java
│   └── SecurityConfiguration.java
├── context
│   ├── AuthenticatedUserContext.java
│   ├── AuthenticatedUserContextProvider.java
│   └── SecurityAuthenticatedUserContextProvider.java
└── jwt
```

authorization

- Validar roles requeridos para cada caso de uso.
- Validar uno entre varios roles autorizados.
- Centralizar los códigos de rol reconocidos por la seguridad.
- Generar errores uniformes cuando el usuario no tiene autorización.

context

- Resolver el usuario autenticado desde Spring Security.
- Exponer usuario, rol, consultorio, paciente y médico asociados.
- Rechazar contextos incompletos o inconsistentes.

#### PasswordConfiguration.java

Responsabilidad:

- Proporcionar la configuración para el tratamiento seguro de contraseñas.
- Exponer el componente utilizado para verificar y codificar contraseñas.

#### SecurityConfiguration.java

Responsabilidad:

- Configurar Spring Security.
- Definir endpoints públicos y protegidos.
- Centralizar las reglas iniciales de acceso.
- Mantener la configuración CORS relacionada con seguridad cuando corresponda.

#### jwt

Estado actual:

El paquete contiene la implementación utilizada para la generación, validación y procesamiento de JSON Web Tokens (JWT), integrada con Spring Security para proteger las APIs del sistema.

Estado:

Implementado

La infraestructura de seguridad incorpora además la resolución del usuario autenticado utilizando SecurityContext y componentes compartidos para obtener la identidad del usuario durante la ejecución de los servicios de negocio.

Esta infraestructura será utilizada por las reglas de autorización implementadas en TS-04.

---

## 6.5 Módulos Funcionales

### 6.5.1 Authentication

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/authentication
```

Estructura:

```text
authentication
├── controller
│   └── AuthenticationController.java
├── dto
│   ├── AuthenticatedUser.java
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── exception
│   └── InvalidCredentialsException.java
├── security
└── service
    ├── AuthenticationService.java
    └── AuthenticationServiceImpl.java
```

Responsabilidad:

- Recibir credenciales de acceso.
- Validar usuarios y contraseñas.
- Construir la respuesta de autenticación.
- Representar la información básica del usuario autenticado.
- Gestionar errores por credenciales inválidas.

Historia relacionada:

- HU-01 — Iniciar sesión.
- HU-02 — Cerrar sesión.

Estado:

Implementado

La autenticación proporciona el contexto del usuario autenticado para los servicios del Backend mediante Spring Security y SecurityContext.

Estado:

Implementado.

---

### 6.5.2 Clinic

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/clinic
```

Estructura:

```text
clinic
├── entity
│   └── ClinicEntity.java
└── repository
    └── ClinicRepository.java
```

Responsabilidad:

- Representar el consultorio como contexto organizacional del sistema.
- Permitir la asociación de los registros del dominio con el consultorio base.
- Proporcionar acceso a la persistencia del consultorio.

Historia relacionada:

- HU-03 — Disponer del consultorio inicial.

Estado:

**Implementación fundacional**

No existe actualmente un Controller ni un Service para administrar consultorios, debido a que el MVP utiliza un consultorio base configurado durante la Foundation.

---

### 6.5.3 Doctor

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/doctor
```

Estructura:

```text
doctor
├── controller
│   ├── DoctorController.java
│   └── MedicalSpecialtyController.java
├── dto
│   ├── CreateDoctorRequest.java
│   ├── DoctorResponse.java
│   └── MedicalSpecialtyResponse.java
├── entity
│   ├── DoctorEntity.java
│   └── MedicalSpecialtyEntity.java
├── repository
│   ├── DoctorRepository.java
│   └── MedicalSpecialtyRepository.java
└── service
    ├── DoctorService.java
    ├── DoctorServiceImpl.java
    ├── MedicalSpecialtyService.java
    └── MedicalSpecialtyServiceImpl.java
```

Responsabilidad:

- Registrar médicos del consultorio.
- Validar los datos requeridos para su creación.
- Consultar médicos disponibles para la operación.
- Gestionar la consulta de especialidades médicas.
- Asociar al médico con su consultorio y especialidad.
- Restringir el registro de médicos al rol Recepcionista.
- Permitir la consulta de médicos a Paciente y Recepcionista.
- Limitar registros y consultas al consultorio autenticado.

Historia relacionada:

- HU-04 — Registrar médico.

Estado:

**Implementado**

Observación:

La especialidad médica no constituye un módulo independiente. Su implementación forma parte del módulo `doctor`.

---

### 6.5.4 Patient

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/patient
```

Estructura:

```text
patient
├── controller
│   └── PatientController.java
├── dto
│   ├── CreatePatientRequest.java
│   └── PatientResponse.java
├── entity
│   └── PatientEntity.java
├── repository
│   └── PatientRepository.java
└── service
    ├── PatientService.java
    └── PatientServiceImpl.java
```

Responsabilidad:

- Registrar pacientes dentro del consultorio.
- Validar la información de identificación y contacto.
- Consultar pacientes registrados.
- Buscar pacientes mediante datos básicos.
- Proporcionar pacientes para los futuros flujos de creación de citas.
- Restringir el registro y la búsqueda de pacientes al rol Recepcionista.
- Limitar registros y búsquedas al consultorio autenticado.
- Exponer la búsqueda mediante GET /api/v1/patients/search.

Historias relacionadas:

- HU-05 — Registrar paciente desde recepción.
- HU-06 — Buscar paciente.

Estado:

**Implementado**

---

### 6.5.5 Agenda

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/agenda
```

Estructura:

```text
agenda
├── controller
│   └── AgendaController.java
├── dto
│   ├── AgendaBlockItemRequest.java
│   ├── AgendaBlockResponse.java
│   ├── CreateAgendaBlocksRequest.java
│   └── CreateAgendaBlocksResponse.java
├── entity
│   ├── AgendaBlockEntity.java
│   └── MedicalAgendaEntity.java
├── repository
│   ├── AgendaBlockRepository.java
│   └── MedicalAgendaRepository.java
└── service
    ├── AgendaService.java
    └── AgendaServiceImpl.java
```

Responsabilidad:

- Gestionar la agenda médica asociada a un médico.
- Crear bloques de atención.
- Validar fechas y rangos horarios.
- Persistir los bloques disponibles.
- Consultar disponibilidad médica por médico y fecha.
- Restringir la creación de bloques al rol Recepcionista.
- Permitir consultar disponibilidad a Paciente y Recepcionista.
- Limitar médicos, agendas y bloques al consultorio autenticado.

Historia relacionada:

- HU-07 — Crear bloques de agenda médica.

Estado:

Implementado.

Funcionalidades disponibles:

- creación de bloques de agenda;
- consulta de bloques disponibles por médico y fecha;
- validación de fechas pasadas;
- consulta ordenada por hora de inicio;
- filtrado exclusivo de bloques disponibles (`available = true`).

Historias relacionadas:

- HU-07 — Crear bloques de agenda médica.
- HU-08 — Consultar disponibilidad médica (Backend).

---

### 6.5.6 Appointment

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/appointment
```

Estructura:

```text
appointment
├── authorization
│   └── AppointmentAuthorizationPolicy.java
├── controller
│   └── AppointmentController.java
├── dto
│   ├── AppointmentAgendaResponse.java
│   ├── AppointmentResponse.java
│   ├── CancelAppointmentRequest.java
│   ├── CreateAppointmentRequest.java
│   ├── RegisterAppointmentNoShowRequest.java
│   └── RescheduleAppointmentRequest.java
├── entity
│   ├── AppointmentEntity.java
│   ├── AppointmentRescheduleHistoryEntity.java
│   ├── AppointmentStatusCode.java
│   └── AppointmentStatusEntity.java
├── repository
│   ├── AppointmentRepository.java
│   ├── AppointmentRescheduleHistoryRepository.java
│   └── AppointmentStatusRepository.java
└── service
    ├── AppointmentService.java
    └── AppointmentServiceImpl.java
```

Responsabilidad:

- Crear citas médicas desde recepción.
- Consultar la agenda general del consultorio.
- Filtrar las citas por fecha obligatoria.
- Filtrar opcionalmente las citas por médico.
- Filtrar opcionalmente las citas por estado.
- Validar la existencia y vigencia del paciente.
- Validar la existencia y vigencia del médico.
- Validar la pertenencia del paciente, médico y agenda al consultorio.
- Validar el bloque de agenda seleccionado.
- Impedir el registro de citas en fechas u horarios pasados.
- Asignar el estado inicial PROGRAMADA.
- Marcar el bloque reservado como no disponible.
- Bloquear pesimistamente el bloque de agenda durante la creación.
- Prevenir la doble reserva del mismo bloque.
- Impedir que un paciente tenga citas activas con horarios superpuestos, incluso cuando correspondan a médicos diferentes.
- Cancelar citas médicas.
- Reprogramar citas médicas.
- Liberar automáticamente bloques de agenda cuando una cita es cancelada.
- Liberar el bloque anterior y reservar el nuevo bloque durante una reprogramación.
- Validar estados permitidos para cancelación y reprogramación.
- Impedir reprogramaciones hacia bloques ocupados.
- Mantener el identificador original de la cita durante la reprogramación.
- Confirmar la llegada de pacientes para citas Programadas.
- Cambiar el estado de la cita de PROGRAMADA a CONFIRMADA.
- Registrar fecha, hora y usuario responsable de la confirmación.
- Registrar la inasistencia de pacientes.
- Permitir la inasistencia únicamente cuando el horario de la cita haya comenzado o transcurrido.
- Cambiar el estado de la cita de PROGRAMADA a NO_ASISTIO.
- Registrar fecha, hora, usuario responsable y comentario opcional de inasistencia.
- Mantener ocupado el bloque de agenda al confirmar llegada o registrar inasistencia.
- Bloquear pesimistamente la cita durante transiciones de estado.
- Impedir transiciones duplicadas o incompatibles.
- Validar que la cita pertenezca al consultorio del usuario autenticado.
- Validar que el paciente autenticado sea propietario de la cita cuando corresponda.
- Restringir las operaciones según el rol autenticado.
- Obtener y bloquear citas utilizando también el identificador del consultorio.
- Registrar al usuario autenticado como responsable de confirmaciones e inasistencias.

Estados funcionales implementados:
PROGRAMADA
CONFIRMADA
ATENDIDA
CANCELADA
NO_ASISTIO

La validación de superposición considera como citas activas para ocupación de horario los estados:

PROGRAMADA
CONFIRMADA

Historias relacionadas:

HU-10
HU-12
HU-13
HU-14
HU-15

Estado:

Implementado

---

### 6.5.7 Role

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/role
```

Estructura:

```text
role
├── entity
│   └── RoleEntity.java
└── repository
    └── RoleRepository.java
```

Responsabilidad:

- Representar los roles disponibles en el sistema.
- Relacionar los usuarios con su perfil de acceso.
- Proporcionar acceso a los roles persistidos.

Estado:

**Implementación fundacional**

No existe actualmente un Controller ni un Service para administrar roles. Los roles funcionan como datos maestros utilizados por autenticación y seguridad.

---

### 6.5.8 User

Ubicación:

```text
backend/src/main/java/com/agendoc/modules/user
```

Estructura:

```text
user
├── entity
│   └── UserEntity.java
└── repository
    └── UserRepository.java
```

Responsabilidad:

- Representar las cuentas de acceso al sistema.
- Persistir credenciales, rol y asociación con el consultorio.
- Proporcionar información de usuarios al módulo de autenticación.

Estado:

**Implementación fundacional**

No existe actualmente un Controller ni un Service de administración general de usuarios. El módulo es utilizado internamente por autenticación y seguridad.

---

## 6.6 Módulos Pendientes

Los siguientes módulos definidos en el producto todavía no existen en el código fuente:

| Módulo | Sprint previsto |
|--------|-----------------|
| Observaciones Médicas Básicas | Sprint 5 |
| Recepcionistas | No implementado como módulo independiente |

El módulo de citas médicas ya forma parte de la implementación mediante el paquete `appointment`.

La ausencia de los módulos restantes refleja el estado actual del desarrollo y no representa un defecto del Backend.

---

## 6.7 Recursos y Configuración

Ubicación:

```text
backend/src/main/resources
```

Estructura:

```text
resources
├── application-dev.yml
├── application-local.yml
├── application-prod.yml
├── application.yml
├── db
│   └── migration
├── static
└── templates
```

Perfiles disponibles:

- Common.
- Local.
- Development.
- Production.

Los cambios de esquema se administran mediante migraciones Flyway ubicadas en:

```text
backend/src/main/resources/db/migration
```

---

## 6.8 Pruebas Automatizadas

Ubicación:

```text
backend/src/test/java/com/agendoc
```

Estructura actual:

```text
com/agendoc
├── common
│   └── exception
│       └── GlobalExceptionHandlerTest.java
├── modules
│   ├── agenda/service/AgendaServiceImplTest.java
│   ├── appointment
│   │   ├── authorization/AppointmentAuthorizationPolicyTest.java
│   │   └── service/AppointmentServiceImplTest.java
│   ├── doctor/service/DoctorServiceImplTest.java
│   └── patient/service/PatientServiceImplTest.java
└── security
    ├── authorization
    │   ├── AuthenticatedUserAuthorizationTest.java
    │   ├── AuthorizationDeniedExceptionTest.java
    │   └── SecurityRoleCodeTest.java
    ├── context/SecurityAuthenticatedUserContextProviderTest.java
    └── jwt/JwtTokenServiceImplTest.java
```

Pruebas identificadas:

| Archivo                           | Cobertura principal                                        |
|-----------------------------------|------------------------------------------------------------|
| BackendApplicationTests.java      | Validación de carga del contexto de Spring Boot.           |
| DoctorServiceImplTest.java        | Validación del servicio de médicos.                        |
| AgendaServiceImplTest.java        | Validación del servicio de agenda y disponibilidad médica. |
| AppointmentServiceImplTest.java   | Validación de creación, consulta, cancelación, reprogramación, confirmación de llegada y registro de inasistencia de citas. |

La prueba del servicio de citas cubre actualmente:

- creación exitosa de una cita válida;
- asociación correcta con clínica, paciente, médico, bloque y estado;
- cambio del bloque de agenda a no disponible;
- rechazo de una cita cuando el paciente ya tiene una cita activa superpuesta;
- confirmación de que no se persiste la cita ni se ocupa el bloque cuando existe conflicto;
- consulta de citas por fecha;
- consulta de citas filtradas por médico;
- consulta de citas filtradas por estado;
- consulta de citas combinando médico y estado;
- construcción de la respuesta de agenda con la información de paciente, médico, horario y estado.
- cancelación de citas Programadas y Confirmadas;
- exigencia de motivo al cancelar citas Confirmadas;
- rechazo de cancelaciones en estados finales;
- reprogramación exitosa y conservación del identificador;
- liberación del bloque anterior y reserva del nuevo bloque;
- trazabilidad de reprogramaciones;
- rechazo de reprogramaciones en estados inválidos;
- confirmación de llegada para citas Programadas;
- registro de `confirmedAt` y `confirmedBy`;
- rechazo de confirmaciones en estados incompatibles;
- registro exitoso de inasistencia con y sin comentario;
- normalización del comentario opcional;
- validación temporal para impedir inasistencias anticipadas;
- registro de `noShowAt`, `noShowBy` y `noShowComment`;
- conservación del bloque ocupado al registrar inasistencia;
- rechazo de inasistencia en estados incompatibles;
- tratamiento de citas inexistentes.

Estado:

Cobertura ampliada y validada.

Resultado actual:
95 pruebas ejecutadas
0 fallos
0 errores
0 omitidas

La cobertura automatizada deberá ampliarse progresivamente en las nuevas Historias de Usuario.

---

## 6.9 Estado General del Backend

| Elemento                               | Estado       |
|----------------------------------------|--------------|
| Aplicación Spring Boot                 | Operativa    |
| Compilación Maven                      | Exitosa      |
| PostgreSQL                             | Integrado    |
| Flyway                                 | Operativo    |
| Manejo global de excepciones           | Implementado |
| Autenticación inicial                  | Implementada |
| JWT End-to-End                         | Implementado |
| Contexto autenticado                   | Implementado |
| Registro de médicos                    | Implementado |
| Registro y búsqueda de pacientes       | Implementado |
| Creación de bloques de agenda          | Implementada |
| Consulta de disponibilidad médica      | Implementada |
| Gestión de citas                       | Implementada |
| Consulta de agenda del consultorio     | Implementada |
| Cancelación de citas                   | Implementada |
| Reprogramación de citas                | Implementada |
| Confirmación de llegada                | Implementada |
| Registro de inasistencia               | Implementado |
| Pruebas automatizadas                  | Exitosas     |
| Autorización declarativa por rol       | Implementada |
| Autorización por consultorio           | Implementada |
| Autorización por propiedad de cita     | Implementada |
| Infraestructura reutilizable de acceso | Implementada |

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15 — Registrar resultado de asistencia

---

# 7. Frontend Web

## 7.1 Objetivo

Esta sección describe la estructura actual del Frontend Web de AgenDoc.

El Frontend Web implementa la interfaz de usuario del sistema y consume los servicios expuestos por el Backend mediante una API REST.

La información documentada en esta sección refleja exclusivamente la implementación existente en:

```text
frontend/src
```

---

## 7.2 Organización General

El Frontend Web se organiza utilizando una arquitectura basada en módulos funcionales y componentes reutilizables.

La estructura principal es la siguiente:

```text
src
├── assets
├── components
├── features
├── routes
├── shared
├── styles
├── App.tsx
├── App.css
├── index.css
└── main.tsx
```

Los elementos de infraestructura compartida permanecen separados de la lógica funcional del producto.

---

## 7.3 Componentes Compartidos

Los componentes reutilizables implementados hasta el cierre del Sprint 1 son:

```text
components
├── AppButton
├── AppCard
├── AppInput
└── AppSelect
```

Estos componentes constituyen la base del Design System definido para AgenDoc.

Su evolución deberá mantenerse alineada con el UI Design Guide.

---

## 7.4 Módulos Funcionales

Actualmente el Frontend implementa los siguientes módulos:

```text
features
├── agenda
├── appointment
├── auth
├── dashboard
├── doctor
└── patient
```

Cada módulo concentra los elementos necesarios para implementar una funcionalidad del producto.

Cuando aplica, cada módulo mantiene la siguiente organización:

- pages
- services
- types
- validation
- components

No todos los módulos requieren todas las carpetas.

Cada módulo implementa únicamente los componentes necesarios para su responsabilidad.

---

## 7.5 Módulo Authentication

Ubicación:

```text
features/auth
```

Estructura:

```text
auth
├── components
│   └── ProtectedRoute.tsx
├── pages
│   ├── LoginPage.tsx
│   └── LoginPage.css
├── services
│   ├── authService.ts
│   └── sessionService.ts
└── types
    └── auth.types.ts
```

Responsabilidad:

- Inicio de sesión.
- Protección de rutas.
- Administración de la sesión del usuario.
- Integración con el Backend de autenticación.

Historias relacionadas:

- HU-01
- HU-02

Estado:

**Implementado**

---

## 7.6 Módulo Dashboard

Ubicación:

```text
features/dashboard
```

Responsabilidad:

- Presentar el panel principal del sistema después de la autenticación.

Historia relacionada:

- Foundation.

Estado:

**Implementado**

---

## 7.7 Módulo Doctor

Ubicación:

```text
features/doctor
```

Estructura principal:

- pages
- services
- types
- validation

Responsabilidad:

- Registro de médicos.
- Validación del formulario.
- Consumo de los servicios del Backend.

Historia relacionada:

- HU-04

Estado:

**Implementado**

---

## 7.8 Módulo Patient

Ubicación:

```text
features/patient
```

Estructura principal:

- components
- pages
- services
- types
- validation

Responsabilidad:

- Registro de pacientes.
- Búsqueda de pacientes.
- Validación del formulario.
- Componentes reutilizables asociados al paciente.

Historias relacionadas:

- HU-05
- HU-06

Estado:

**Implementado**

---

## 7.9 Módulo Agenda

Ubicación:

```text
features/agenda
```

Estructura principal:

- pages
- services
- types
- validation

Responsabilidad:

- Gestión de la agenda médica.
- Creación de bloques de agenda.
- Consulta de disponibilidad médica por médico y fecha.
- Presentación de horarios disponibles.
- Gestión de estados de carga, error y ausencia de disponibilidad.
- Integración con los servicios del Backend.

Historias relacionadas:

- HU-07 — Crear bloques de agenda médica.
- HU-08 — Consultar disponibilidad médica.

Estado:

**Implementado**

---

## 7.10 Módulo Appointment

Ubicación:

```text
features/appointment
```

Estructura principal:

pages
services
types
validation

Responsabilidad:

- Crear citas médicas desde recepción.
- Buscar y seleccionar un paciente.
- Seleccionar el médico.
- Consultar y seleccionar un bloque disponible.
- Registrar el motivo y las observaciones de la cita.
- Consumir la API de creación de citas médicas.
- Consultar la agenda general del consultorio.
- Filtrar las citas por fecha, médico y estado.
- Mostrar la información de paciente, médico, horario y estado de cada cita.
- Gestionar estados de carga, error y ausencia de resultados.
- Mostrar mensajes de éxito y error.
- Limpiar el formulario después de una creación exitosa.
- Presentar al usuario los conflictos de horario detectados por el Backend.
- Cancelar citas médicas.
- Reprogramar citas médicas.
- Actualizar automáticamente la agenda luego de cancelar.
- Actualizar automáticamente la agenda luego de reprogramar.
- Mostrar mensajes de éxito y error durante ambos procesos.
- Confirmar la llegada del paciente.
- Registrar la inasistencia del paciente.
- Aplicar validación visual para mostrar la inasistencia cuando la cita ya comenzó.
- Solicitar un comentario opcional al registrar la inasistencia.
- Actualizar automáticamente la agenda luego de confirmar llegada.
- Actualizar automáticamente la agenda luego de registrar inasistencia.
- Bloquear operaciones concurrentes durante las transiciones de estado.

Pantallas implementadas:

AppointmentPage: creación de citas médicas desde recepción.
AppointmentAgendaPage: consulta de la agenda del consultorio.

Servicios implementados:

- createAppointment()
- findAppointments()
- cancelAppointment()
- confirmAppointmentArrival()
- registerAppointmentNoShow()
- rescheduleAppointment()

Contratos principales:

- AppointmentAgendaFilters.
- AppointmentAgendaResponse.
- AppointmentResponse.
- CreateAppointmentRequest.
- CancelAppointmentRequest.
- RegisterAppointmentNoShowRequest.
- RescheduleAppointmentRequest.

Historias relacionadas:

- HU-10
- HU-12
- HU-13
- HU-14
- HU-15

Estado:

Implementado

---

## 7.11 Infraestructura Compartida

### Shared

Ubicación:

```text
shared
```

Estructura actual:

```text
shared
└── api
    └── apiClient.ts
```

Responsabilidad:

- Centralizar el acceso a la API REST.
- Compartir infraestructura entre los módulos funcionales.

---

### Routes

Ubicación:

```text
routes
```

Responsabilidad:

- Centralizar la configuración de navegación del Frontend.

---

### Styles

Ubicación:

```text
styles
```

Estructura actual:

```text
styles
└── tokens.css
```

Responsabilidad:

- Centralizar los Design Tokens utilizados por la aplicación.

---

### Assets

Ubicación:

```text
assets
```

Responsabilidad:

- Mantener los recursos gráficos utilizados durante el desarrollo.

---

## 7.12 Recursos Públicos

Ubicación:

```text
frontend/public
```

Estructura actual:

```text
public
├── branding
├── images
├── favicon.svg
└── icons.svg
```

Responsabilidad:

- Almacenar los recursos públicos utilizados por la aplicación.

Observación:

La estructura de branding deberá mantenerse alineada con el UI Design Guide.

---

## 7.13 Estado General

| Elemento                           | Estado        |
|------------------------------------|---------------|
| React                              | Operativo     |
| TypeScript                         | Operativo     |
| Vite                               | Operativo     |
| React Router                       | Operativo     |
| Fetch API mediante apiClient.ts    | Operativo     |
| Componentes base                   | Implementados |
| Autenticación                      | Implementada  |
| Dashboard                          | Implementado  |
| Gestión de médicos                 | Implementada  |
| Gestión de pacientes               | Implementada  |
| Creación de bloques de agenda      | Implementada  |
| Consulta de disponibilidad médica  | Implementada  |
| Creación de citas médicas          | Implementada  |
| Consulta de agenda del consultorio | Implementada  |
| Cancelación de citas               | Implementada  |
| Reprogramación de citas            | Implementada  |
| Confirmación de llegada            | Implementada  |
| Registro de inasistencia           | Implementado  |

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15

---

# 8. Mobile

## 8.1 Objetivo

Esta sección describe el estado actual de la aplicación Mobile de AgenDoc.

La aplicación Mobile constituye el cliente nativo del producto y compartirá la misma lógica funcional implementada en el Backend mediante el consumo de la API REST.

---

## 8.2 Estado Actual

Al cierre del Sprint 1 la aplicación Mobile cuenta con la estructura base del proyecto y la configuración inicial para su desarrollo.

La implementación funcional de las Historias de Usuario comenzará en los Sprints definidos por el Roadmap del producto.

---

## 8.3 Tecnologías

| Tecnología   | Propósito                             |
|--------------|---------------------------------------|
| React Native | Desarrollo de la aplicación móvil.    |
| Expo         | Plataforma de desarrollo y ejecución. |
| TypeScript   | Desarrollo tipado de la aplicación.   |

---

## 8.4 Arquitectura

La aplicación Mobile seguirá la misma organización funcional utilizada por el Frontend Web.

Los módulos funcionales consumirán la misma API REST expuesta por el Backend.

Esta estrategia busca mantener consistencia funcional entre ambas plataformas y maximizar la reutilización del conocimiento del dominio.

---

## 8.5 Estado General

| Elemento                | Estado      |
|-------------------------|-------------|
| Proyecto Expo           | Configurado |
| TypeScript              | Configurado |
| Integración con Backend | Pendiente   |
| Funcionalidades del MVP | Pendientes  |

---

**Última actualización**

Sprint:
Sprint 1

Sesión:
Cierre del Sprint 1

---

# 9. Base de Datos

## 9.1 Objetivo

Esta sección documenta la implementación actual de la Base de Datos de AgenDoc.

Su propósito es proporcionar una visión general del esquema implementado, las migraciones existentes y la estrategia utilizada para mantener sincronizados los distintos entornos del proyecto.

La información registrada refleja exclusivamente la implementación existente en el repositorio.

---

## 9.2 Tecnología

La persistencia de datos de AgenDoc se implementa utilizando PostgreSQL como base de datos relacional.

La evolución del esquema se administra mediante Flyway.

---

## 9.3 Estructura

Las migraciones se encuentran ubicadas en:

```text
backend/src/main/resources/db/migration
```

Estructura actual:

```text
migration
├── V1__initial_schema.sql
├── V2__create_authentication_schema.sql
├── V3__seed_authentication_data.sql
├── V4__create_doctors_schema.sql
├── V5__seed_medical_specialties.sql
├── V6__create_patients_schema.sql
├── V7__create_medical_agenda_schema.sql
├── V8__create_appointments_schema.sql
├── V9__add_appointment_cancellation_fields.sql
├── V10__create_appointment_reschedule_history.sql
└── V11__add_appointment_attendance_fields.sql
```

---

## 9.4 Estrategia de Versionado

El esquema de Base de Datos evoluciona mediante migraciones incrementales de Flyway.

Cada cambio estructural se incorpora mediante una nueva migración versionada.

Las migraciones existentes no deben modificarse una vez integradas al repositorio.

Las modificaciones posteriores deberán implementarse mediante nuevas versiones.

---

## 9.5 Estado Actual del Esquema

Al cierre del Sprint 3, el esquema de Base de Datos implementa los elementos necesarios para soportar:

- autenticación;
- usuarios;
- roles;
- consultorio;
- médicos;
- especialidades médicas;
- pacientes;
- agenda médica;
- bloques de agenda;
- citas médicas;
- estados del ciclo de vida de las citas;
- trazabilidad de cancelaciones;
- historial de reprogramaciones;
- confirmación de llegada;
- registro de inasistencia.

Las estructuras correspondientes a citas médicas ya forman parte del esquema mediante la migración V8.

La migración V11 incorporó a `appointments` los campos de trazabilidad para confirmación de llegada e inasistencia:

- confirmed_at;
- confirmed_by;
- no_show_at;
- no_show_by;
- no_show_comment.

---

## 9.6 Estado General

| Elemento                   | Estado        |
|----------------------------|---------------|
| PostgreSQL                 | Operativo     |
| Flyway                     | Operativo     |
| Migraciones                | Versionadas   |
| Seed inicial               | Implementado  |
| Esquema Foundation         | Implementado  |
| Esquema Sprint 1           | Implementado  |
| Esquema Sprint 2           | Implementado  |
| Citas médicas              | Implementadas |
| Esquema Sprint 3           | Implementado  |
| Migraciones aplicadas      | V1 a V11      |
| Trazabilidad de asistencia | Implementada  |

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15

---

# 10. APIs Implementadas

## 10.1 Objetivo

Esta sección documenta el inventario de APIs REST implementadas por el Backend de AgenDoc.

Su propósito es proporcionar una referencia rápida de los servicios disponibles y su relación con los módulos funcionales y las Historias de Usuario del producto.

No reemplaza la documentación OpenAPI/Swagger. Únicamente registra los endpoints implementados.

---

## 10.2 Estado General

Las APIs REST del proyecto se implementan utilizando Spring Boot y siguen una organización modular basada en los distintos contextos funcionales del dominio.

Cada Controller representa el punto de entrada oficial para un módulo del sistema.

---

## 10.3 Inventario de Controllers

| Controller                 | Módulo         | Estado       |
|----------------------------|----------------|--------------|
| AuthenticationController   | Authentication | Implementado |
| DoctorController           | Doctor         | Implementado |
| MedicalSpecialtyController | Doctor         | Implementado |
| PatientController          | Patient        | Implementado |
| AgendaController           | Agenda         | Implementado |
| AppointmentController      | Appointment    | Implementado |

---

## 10.4 Inventario de Endpoints

| Módulo         | Endpoint                         | Historia | Estado        |
|----------------|----------------------------------|----------|---------------|
| Authentication | Login                            | HU-01    | Implementado  |
| Authentication | Logout                           | HU-02    | Implementado  |
| Doctor         | Registrar médico                 | HU-04    | Implementado  |
| Doctor         | Consultar especialidades médicas | HU-04    | Implementado  |
| Patient        | Registrar paciente               | HU-05    | Implementado  |
| Patient        | Buscar paciente                  | HU-06    | Implementado  |
| Agenda         | Crear bloques de agenda          | HU-07    | Implementado  |
| Agenda         | Consultar disponibilidad médica  | HU-08    | Implementado  |
| Appointment    | Crear cita médica                | HU-10    | Implementado  |
| Appointment    | Consultar agenda del consultorio | HU-12    | Implementado  |
| Appointment    | Cancelar cita                    | HU-13    | Implementado  |
| Appointment    | Reprogramar cita                 | HU-14    | Implementado  |
| Appointment    | Confirmar llegada del paciente   | HU-15    | Implementado  |
| Appointment    | Registrar inasistencia           | HU-15    | Implementado  |

PATCH /api/v1/appointments/{appointmentId}/confirm-arrival
PATCH /api/v1/appointments/{appointmentId}/no-show
POST  /api/v1/doctors                         RECEPTIONIST
GET   /api/v1/doctors                         PATIENT, RECEPTIONIST
POST  /api/v1/patients                        RECEPTIONIST
GET   /api/v1/patients/search                 RECEPTIONIST
POST  endpoints de bloques de agenda          RECEPTIONIST
GET   endpoints de disponibilidad             PATIENT, RECEPTIONIST
POST  /api/v1/appointments                    RECEPTIONIST
GET   /api/v1/appointments                    RECEPTIONIST
PATCH /appointments/{id}/cancel               RECEPTIONIST, PATIENT
PATCH /appointments/{id}/confirm-arrival      RECEPTIONIST
PATCH /appointments/{id}/no-show              RECEPTIONIST
PATCH /appointments/{id}/reschedule           RECEPTIONIST

---

## 10.5 APIs Pendientes

No existen APIs funcionales pendientes dentro de las Historias de Usuario completadas hasta HU-15.

Las próximas APIs serán incorporadas conforme avance el backlog oficial del producto.

---

## 10.6 Estado General

| Elemento | Estado |
|----------|--------|
| Controllers implementados | 6 |
| APIs Foundation | Implementadas |
| APIs Sprint 1   | Implementadas |
| APIs Sprint 2   | Implementadas |
| APIs Sprint 3   | Implementadas hasta HU-15 |

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15

---

# 11. Componentes Reutilizables

## 11.1 Objetivo

Esta sección documenta los componentes reutilizables implementados en el Frontend Web de AgenDoc.

Su propósito es facilitar la reutilización de componentes, mantener la consistencia visual del producto y evitar la duplicación de implementaciones.

La definición funcional y visual de estos componentes se encuentra en el UI Design Guide.

---

## 11.2 Componentes Base Implementados

| Componente | Estado | Observaciones |
|------------|--------|---------------|
| AppButton | ✅ Implementado | Botón reutilizable del sistema. |
| AppCard | ✅ Implementado | Contenedor visual reutilizable. |
| AppInput | ✅ Implementado | Campo de entrada estándar. |
| AppSelect | ✅ Implementado | Lista desplegable reutilizable. |

---

## 11.3 Componentes Pendientes

Los siguientes componentes forman parte del Design System definido para AgenDoc y serán incorporados progresivamente conforme lo requieran las Historias de Usuario.

| Componente | Estado |
|------------|--------|
| AppBadge | Pendiente |
| AppAlert | Pendiente |
| AppToast | Pendiente |
| AppModal | Pendiente |
| AppAvatar | Pendiente |
| AppLoader | Pendiente |
| AppEmptyState | Pendiente |
| AppErrorState | Pendiente |
| AppNavbar | Pendiente |
| AppSidebar | Pendiente |
| AppCalendar | Pendiente |
| AppTable | Pendiente |

---

## 11.4 Criterios de Evolución

Todo nuevo componente reutilizable deberá:

- responder a una necesidad funcional recurrente;
- mantener coherencia con el UI Design Guide;
- utilizar los Design Tokens oficiales;
- documentarse en esta sección una vez implementado.

---

## 11.5 Estado General

| Elemento | Estado |
|----------|--------|
| Componentes base | Implementados |
| Design System | En evolución |
| Componentes especializados | Pendientes |

---

**Última actualización**

Sprint:
Sprint 1

Sesión:
Cierre del Sprint 1

---

# 12. Inventario Funcional

## 12.1 Objetivo

Esta sección documenta el estado de implementación de las Historias de Usuario del proyecto desde la perspectiva del código fuente.

Su propósito es proporcionar una visión rápida del avance funcional del producto y del estado de implementación de cada capa de la solución.

La planificación funcional continúa siendo responsabilidad del AgenDoc Project Blueprint.

---

## 12.2 Foundation

| Historia   | Backend | Frontend Web | Mobile | QA  | Estado     |
|------------|---------|--------------|--------|-----|------------|
| Foundation | ✅      | ✅           | ⚪     | ✅  | Completada |

---

## 12.3 Sprint 1

| Historia                               | Backend | Frontend | Mobile | QA  | Estado     |
|----------------------------------------|---------|----------|--------|-----|------------|
| HU-01 — Iniciar sesión                 | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-02 — Cerrar sesión                  | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-03 — Consultorio base               | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-04 — Registrar médico               | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-05 — Registrar paciente             | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-06 — Buscar paciente                | ✅      | ✅       | ⚪     | ✅  | Completada |
| HU-07 — Crear bloques de agenda médica | ✅      | ✅       | ⚪     | ✅  | Completada |

---

## 12.4 Sprint 2

| Historia                                 | Backend | Frontend | Mobile | QA | Estado             |
|------------------------------------------|---------|----------|--------|----|--------------------|
| HU-08 — Consultar disponibilidad médica  | ✅      | ✅       | ⚪     | ✅ | Completada         |
| HU-10 — Crear cita desde recepción       | ✅      | ✅       | ⚪     | ✅ | Completada         |
| HU-12 — Consultar agenda del consultorio | ✅      | ✅       | ⚪     | ✅ | Completada         |

---

## 12.5 Sprint 3

| Historia                                  | Backend | Frontend | Mobile | QA | Estado      |
|-------------------------------------------|---------|----------|--------|----|-------------|
| HU-13 — Cancelar cita desde recepción     | ✅      | ✅       | ⚪     | ✅ | Completada  |
| HU-14 — Reprogramar cita desde recepcion  | ✅      | ✅       | ⚪     | ✅ | Completada  |
| HU-15 — Registrar resultado de asistencia | ✅      | ✅       | ⚪     | ✅ | Completada  |

---

## 12.6 Leyenda

| Símbolo | Significado |
|----------|-------------|
| ✅ | Implementado |
| ⚪ | Pendiente |
| 🟡 | Implementación parcial |
| 🔄 | En desarrollo |

---

## 12.7 Estado General

Historias implementadas:

- Foundation
- HU-01
- HU-02
- HU-03
- HU-04
- HU-05
- HU-06
- HU-07
- HU-08
- HU-10
- HU-12
- HU-13
- HU-14
- HU-15

Historias pendientes:
- HU-09 — Reservar cita como paciente.
- HU-11 — Consultar mis citas como paciente.
- HU-16 a HU-19 — Atención médica básica.

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15

---

# 13. Technical Stories

## 13.1 Objetivo

Esta sección documenta el estado de implementación de las Technical Stories del proyecto.

Su propósito es proporcionar una visión rápida del avance de los habilitadores técnicos que soportan las Historias de Usuario del producto.

La definición funcional y la planificación de las Technical Stories se mantiene en el AgenDoc Project Blueprint.

---

## 13.2 Estado de Implementación

| Technical Story | Descripción                                                       | Estado     |
|-----------------|-------------------------------------------------------------------|------------|
| TS-01           | Configurar entorno de desarrollo multidispositivo y multiambiente | Completada |
| TS-02           | Completar autenticación JWT End-to-End                            | Completada |
| TS-03           | Implementar contexto del usuario autenticado                      | Completada |
| TS-04           | Implementar autorización por dominio                              | Completada |
| TS-05           | Endurecer seguridad y manejo de accesos no autorizados            | Pendiente  |

---

## 13.3 Relación con el Producto

Las Technical Stories no entregan valor funcional directamente al usuario final.

Su propósito es fortalecer la arquitectura, la seguridad, la mantenibilidad y la calidad técnica del producto.

---

## 13.4 Estado General

| Elemento                        | Estado |
|---------------------------------|--------|
| Technical Stories implementadas | 4      |
| Technical Stories en desarrollo | 0      |
| Technical Stories pendientes    | 1      |

---

**Última actualización**

Sprint:
Sprint 1

Sesión:
Cierre del Sprint 1

---

# 14. Configuración del Proyecto

## 14.1 Objetivo

Esta sección documenta la organización general de la configuración del proyecto AgenDoc.

Su propósito es facilitar la localización de los archivos de configuración utilizados por el Backend, Frontend Web y Mobile, evitando describir parámetros específicos que pertenecen al propio código fuente.

---

## 14.2 Backend

La configuración del Backend se encuentra principalmente en:

```text
backend/src/main/resources
├── application.yml
├── application-local.yml
├── application-dev.yml
├── application-prod.yml
└── db/
    └── migration/
```

Los perfiles permiten adaptar la configuración a los distintos entornos del proyecto.

Las modificaciones de configuración deberán realizarse mediante los perfiles correspondientes y nunca directamente sobre el código fuente.

---

## 14.3 Frontend Web

La configuración del Frontend Web se encuentra distribuida entre:

```text
frontend/
├── vite.config.ts
├── package.json
├── tsconfig*.json
└── .env*
```

Las variables de entorno permiten configurar la integración con el Backend sin modificar el código de la aplicación.

---

## 14.4 Mobile

La configuración de la aplicación Mobile se administra mediante Expo y los archivos propios del proyecto.

Su evolución se documentará conforme avance la implementación del cliente móvil.

---

## 14.5 Control de Versiones

La configuración del repositorio incluye:

- Git.
- GitHub.
- GitHub Actions.
- GitHub CLI.

La estrategia de ramas y versionado se encuentra documentada en el AgenDoc Development Playbook.

---

## 14.6 Estado General

| Elemento | Estado |
|----------|--------|
| Perfiles Backend | Implementados |
| Migraciones Flyway | Implementadas |
| Configuración Frontend | Implementada |
| Configuración Mobile | Inicial |
| Integración Continua | Operativa |

---

**Última actualización**

Sprint:
Sprint 1

Sesión:
Cierre del Sprint 1

---

# 15. Checklist Técnico

## 15.1 Objetivo

Esta sección proporciona una lista de verificación rápida del estado técnico del proyecto.

Su propósito es confirmar que la solución se encuentra en condiciones de continuar el desarrollo y facilitar la detección temprana de problemas de integración.

Este checklist deberá revisarse al cierre de cada Sprint y actualizarse cuando cambie el estado técnico del proyecto.

---

## 15.2 Backend

| Elemento                           | Estado       |
|------------------------------------|--------------|
| Compilación Maven                  | ✅           |
| Spring Boot inicia correctamente   | ✅           |
| PostgreSQL accesible               | ✅           |
| Flyway ejecuta correctamente       | ✅           |
| API REST operativa                 | ✅           |
| Manejo global de excepciones       | ✅           |
| Spring Security                    | ✅           |
| Consulta de disponibilidad médica  | ✅           |
| JWT End-to-End                     | ✅           |
| Contexto autenticado               | ✅           |
| SecurityContext                    | ✅           |
| Creación de citas médicas          | ✅           |
| Conflicto de horario del paciente  | ✅           |
| Consulta de agenda del consultorio | ✅           |
| Cancelación de citas               | ✅           |
| Reprogramación de citas            | ✅           |
| Confirmación de llegada            | ✅           |
| Registro de inasistencia           | ✅           |
| Trazabilidad de asistencia         | ✅           |
| Pruebas automatizadas              | ✅           |

---

## 15.3 Frontend Web

| Elemento                           | Estado |
|------------------------------------|--------|
| Proyecto compila                   | ✅     |
| Vite inicia correctamente          | ✅     |
| React Router                       | ✅     |
| Integración con Backend            | ✅     |
| Design Tokens                      | ✅     |
| Componentes base                   | ✅     |
| Flujo de creación de citas         | ✅     |
| Consulta de agenda del consultorio | ✅     |
| Cancelación de citas               | ✅     |
| Reprogrmación de citas             | ✅     |
| Confirmación de llegada            | ✅     |
| Registro de inasistencia           | ✅     |

---

## 15.4 Mobile

| Elemento                | Estado       |
|-------------------------|--------------|
| Proyecto Expo           | ✅           |
| Compilación inicial     | ✅           |
| Integración con Backend | ⚪ Pendiente |
| Funcionalidades del MVP | ⚪ Pendiente |

---

## 15.5 Base de Datos

| Elemento                 | Estado |
|--------------------------|--------|
| PostgreSQL               | ✅     |
| Migraciones Flyway       | ✅     |
| Seed inicial             | ✅     |
| Esquema Sprint 1         | ✅     |
| Esquema de citas médicas | ✅     |
| Esquema Sprint 3         | ✅     |
| Migración V11            | ✅     |

---

## 15.6 Repositorio

| Elemento                  | Estado |
|---------------------------|--------|
| Git                       | ✅     |
| GitHub                    | ✅     |
| GitHub Actions            | ✅     |
| GitHub CLI                | ✅     |
| Rama develop sincronizada | Sí     |
| Working Tree limpio       | ✅     |

---

## 15.7 Estado General

El proyecto mantiene un estado técnico estable y cuenta con HU-08, HU-10, HU-12, HU-13, HU-14 y HU-15 implementadas y validadas. El Sprint 3 se encuentra funcionalmente completado.

---

**Última actualización**

Sprint:
Sprint 2

Sesión:
Implementación completa de HU-12 — Consultar agenda del consultorio

---

# 16. Mapa de Navegación del Código

## 16.1 Objetivo

Esta sección documenta el recorrido principal del código para cada Historia de Usuario implementada.

Su propósito es facilitar la localización de los componentes involucrados en una funcionalidad determinada y reducir el tiempo de análisis antes de realizar modificaciones.

Cada flujo representa una vista simplificada de la implementación y no reemplaza la estructura detallada descrita en las secciones de Backend y Frontend Web.

---

## 16.2 Foundation

### HU-03 — Consultorio Base

```text
Base de Datos
        │
        ▼
ClinicEntity
        │
        ▼
ClinicRepository
```

---

## 16.3 Sprint 1

### HU-01 — Iniciar sesión

```text
LoginPage
        │
        ▼
authService.ts
        │
        ▼
AuthenticationController
        │
        ▼
AuthenticationService
        │
        ▼
UserRepository
```

---

### HU-02 — Cerrar sesión

```text
Logout
        │
        ▼
sessionService.ts
        │
        ▼
ProtectedRoute
```

---

### HU-04 — Registrar médico

```text
DoctorPage
        │
        ▼
doctorService.ts
        │
        ▼
DoctorController
        │
        ▼
DoctorService
        │
        ▼
DoctorRepository
        │
        ▼
DoctorEntity
```

---

### HU-05 — Registrar paciente

```text
PatientPage
        │
        ▼
patientService.ts
        │
        ▼
PatientController
        │
        ▼
PatientService
        │
        ▼
PatientRepository
        │
        ▼
PatientEntity
```

---

### HU-06 — Buscar paciente

```text
PatientPage
        │
        ▼
patientService.ts
        │
        ▼
PatientController
        │
        ▼
PatientService
        │
        ▼
PatientRepository
```

---

### HU-07 — Crear bloques de agenda médica

```text
MedicalAgendaPage
        │
        ▼
agendaService.ts
        │
        ▼
AgendaController
        │
        ▼
AgendaService
        │
        ▼
MedicalAgendaRepository
        │
        ▼
MedicalAgendaEntity
        │
        ▼
AgendaBlockEntity
```

---

### HU-08 — Consultar disponibilidad médica

```text
MedicalAgendaPage
        │
        ▼
agendaService.ts
        │
        ▼
AgendaController
        │
        ▼
AgendaService
        │
        ▼
AgendaBlockRepository
        │
        ▼
AgendaBlockEntity
```

---

### HU-10 — Crear cita desde recepción

```text
AppointmentPage
        │
        ├── patientService.ts
        ├── doctorService.ts
        ├── agendaService.ts
        └── appointmentService.ts
                    │
                    ▼
        AppointmentController
                    │
                    ▼
        AppointmentService
                    │
                    ├── ClinicRepository
                    ├── PatientRepository
                    ├── DoctorRepository
                    ├── AgendaBlockRepository
                    ├── AppointmentStatusRepository
                    └── AppointmentRepository
                                │
                                ▼
                    AppointmentEntity
```

---

### HU-12 — Consultar agenda del consultorio

```text
AppointmentAgendaPage
        │
        ├── doctorService.ts
        └── appointmentService.ts
                    │
                    ▼
        AppointmentController
                    │
                    ▼
        AppointmentService
                    │
                    ▼
        AppointmentRepository
                    │
                    ▼
        AppointmentEntity
                    │
                    ├── PatientEntity
                    ├── DoctorEntity
                    ├── AgendaBlockEntity
                    └── AppointmentStatusEntity
```

---

### HU-13 — Cancelar cita desde recepción

```text
AppointmentAgendaPage

↓

appointmentService.ts

↓

AppointmentController

↓

AppointmentService

↓

AppointmentRepository

↓

AppointmentEntity

↓

AgendaBlockRepository
```

---

### HU-14 — Reprogramar cita desde recepción

```text
AppointmentAgendaPage

↓

appointmentService.ts

↓

AppointmentController

↓

AppointmentService

↓

AgendaBlockRepository

↓

AppointmentRepository

↓

AppointmentEntity
```

---

### HU-15 — Confirmar llegada del paciente

AppointmentAgendaPage

↓

appointmentService.ts

↓

AppointmentController

↓

AppointmentService

↓

AppointmentRepository

↓

AppointmentEntity

↓

AppointmentStatusRepository

---

### HU-15 — Registrar inasistencia

AppointmentAgendaPage

↓

appointmentService.ts

↓

AppointmentController

↓

AppointmentService

↓

AppointmentRepository

↓

AppointmentEntity

↓

AppointmentStatusRepository

---


## 16.4 Convenciones

Todos los nuevos flujos deberán documentarse siguiendo el mismo patrón:

Frontend

↓

Service

↓

Controller

↓

Service

↓

Repository

↓

Entity

Cuando una Historia de Usuario involucre varios módulos, el flujo deberá mostrar únicamente el recorrido principal.

---

## 16.5 Estado General

Las Historias implementadas durante Foundation, Sprint 1 y los incrementos completados del Sprint 2 cuentan con un mapa simplificado de navegación del código.

Los nuevos flujos serán incorporados conforme avance el desarrollo del producto.

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Implementación completa de HU-15

---

# 17. Mapa de Dependencias

## 17.1 Objetivo

Esta sección documenta las principales dependencias funcionales entre los módulos implementados en AgenDoc.

Su propósito es facilitar el análisis de impacto antes de realizar modificaciones sobre el código y ayudar a comprender las relaciones entre los distintos contextos del dominio.

Las dependencias descritas corresponden al estado actual de la implementación y deberán actualizarse conforme evolucione el producto.

---

## 17.2 Dependencias Actuales

### Authentication

```text
Authentication
        │
        ├── User
        └── Role
```

El módulo de autenticación depende de la información de usuarios y roles para validar el acceso al sistema.

---

### Doctor

```text
Doctor
        │
        ├── Clinic
        └── Medical Specialty
```

El módulo Doctor depende del consultorio al que pertenece el profesional y de la especialidad médica asociada.

---

### Patient

```text
Patient
        │
        └── Clinic
```

Cada paciente pertenece a un consultorio.

---

### Agenda

```text
Agenda
        │
        ├── Doctor
        └── Clinic
```

La agenda médica se encuentra asociada a un médico y al consultorio correspondiente.

### Appointment

```text
Appointment
        │
        ├── Patient
        ├── Doctor
        ├── Agenda
        └── Clinic
```

El módulo Appointment utiliza información del paciente, médico, consultorio y bloque de agenda para registrar y validar una cita médica.

También consulta el estado funcional de la cita para determinar si un paciente ya posee una cita activa superpuesta.

---

## 17.3 Dependencias Pendientes de Implementación

Conforme avance el MVP se incorporarán nuevas relaciones entre módulos.

```text
Medical Notes
        │
        ├── Appointment
        ├── Doctor
        └── Patient
```

Esta dependencia corresponde al roadmap aprobado y todavía no forma parte de la implementación.

---

## 17.4 Principios

Las dependencias entre módulos deberán cumplir los siguientes principios:

- minimizar el acoplamiento entre contextos funcionales;
- mantener responsabilidades claramente definidas;
- evitar dependencias circulares;
- reutilizar únicamente contratos públicos entre módulos.

---

## 17.5 Estado General

Al cierre del Sprint 1 la arquitectura mantiene un bajo nivel de acoplamiento entre módulos y las dependencias implementadas son coherentes con el diseño definido para el MVP.

---

**Última actualización**

Sprint:
Sprint 2

Sesión:
Implementación completa de HU-10 — Crear cita desde recepción

---

# 18. Próximo Incremento

## 18.1 Objetivo

Esta sección documenta el siguiente incremento planificado para el proyecto desde la perspectiva de la implementación.

Su propósito es proporcionar un punto de partida para la siguiente sesión de desarrollo sin reemplazar la planificación oficial definida en el AgenDoc Project Blueprint.

Esta información deberá actualizarse al cierre de cada Sprint.

---

## 18.2 Estado Actual

Sprint 4

Incremento completado:
TS-04 — Implementar autorización por dominio

Siguiente historia funcional:
HU-09 — Reservar cita como paciente

---

## 18.3 Historias de Usuario

| Historia | Estado |
|----------|--------|
| HU-08 — Consultar disponibilidad médica  | Completada |
| HU-10 — Crear cita desde recepción       | Completada |
| HU-12 — Consultar agenda del consultorio | Completada |
| HU-13 — Cancelar cita desde recepción        | Completada |
| HU-14 — Reprogramar cita desde recepción     | Completada |
| HU-15 — Registrar resultado de asistencia    | Completada |
| HU-09 — Reservar cita como paciente           | Siguiente historia |

---

## 18.4 Technical Stories

| Technical Story                                                           | Estado     |
|---------------------------------------------------------------------------|------------|
| TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente | Completada |
| TS-02 — Completar autenticación JWT End-to-End                            | Completada |
| TS-03 — Implementar contexto del usuario autenticado                      | Completada |
| TS-04 — Implementar autorización por dominio                              | Completada |
| TS-05 — Endurecer seguridad y manejo de accesos no autorizados            | Pendiente  |

---

## 18.5 Dependencias

Para iniciar el Sprint 4 deberán mantenerse las siguientes condiciones:

- rama `develop` sincronizada con `origin/develop`;
- Working Tree limpio;
- Backend compilando correctamente;
- Frontend Web compilando correctamente;
- migraciones Flyway aplicadas;
- HU-12 validada funcionalmente;
- documentación oficial sincronizada.
- HU-15 validada funcionalmente;
- Sprint 3 documentado;
- Pruebas Backend exitosas;
- cambios del Sprint 3 versionados en `develop`.

---

## 18.6 Objetivo del Incremento

Sprint 4: 🚧 En ejecución
Incremento completado:
TS-04 — Implementar autorización por dominio

Siguiente historia funcional:
HU-09 — Reservar cita como paciente

---

## 18.7 Estado General

El proyecto cuenta con Sprint 3 funcionalmente completado y está preparado para iniciar el Sprint 4 con el flujo de autogestión del paciente.

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Cierre de HU-15

---

# 19. Historial de Implementación

## 19.1 Objetivo

Esta sección registra los principales hitos de implementación del proyecto AgenDoc.

Su propósito es proporcionar una visión ejecutiva de la evolución del código fuente durante el desarrollo del producto.

El historial se mantiene a nivel de Sprint y no pretende reemplazar el historial del repositorio Git ni el registro de cambios (changelog).

---

## 19.2 Historial

| Incremento | Estado | Resumen |
|------------|--------|---------|
| Foundation | ✅ Completado | Configuración inicial del proyecto, arquitectura base, Backend, Frontend Web, Mobile, Base de Datos, seguridad, Flyway, GitHub Actions y estructura oficial del repositorio. |
| Sprint 1 | ✅ Completado | Implementación de autenticación inicial, gestión de médicos, gestión de pacientes y creación de bloques de agenda médica. |
| Sprint 2 | ✅ Completado | Consulta de disponibilidad médica, creación de citas desde recepción y consulta de la agenda del consultorio implementadas y validadas. |
| Sprint 3 | ✅ Completado | Implementación de cancelación, reprogramación, confirmación de llegada y registro de inasistencia. Se incorporó trazabilidad de reprogramaciones y asistencia, actualización automática de la agenda y validación funcional de extremo a extremo. |

---

## 19.3 Convención

Al cierre de cada Sprint deberá incorporarse una nueva entrada al historial indicando:

- Sprint o incremento finalizado.
- Estado.
- Resumen ejecutivo de las funcionalidades implementadas.

No deberán registrarse actividades intermedias ni sesiones individuales de desarrollo.

---

## 19.4 Estado General

El historial refleja la evolución funcional y técnica del proyecto desde la Foundation hasta el Sprint actualmente implementado.

---

**Última actualización**

Sprint:
Sprint 3

Sesión:
Cierre funcional del Sprint 3

---

# 20. Convenciones de Actualización

## 20.1 Objetivo

Esta sección define las reglas oficiales para mantener actualizado el AgenDoc Codebase Guide.

Su propósito es garantizar que el documento refleje permanentemente el estado real de la implementación y continúe siendo una fuente confiable de consulta para el equipo de desarrollo.

---

## 20.2 Principios

El Codebase Guide se rige por los siguientes principios:

- Documentar únicamente elementos implementados en el repositorio.
- Mantener una única fuente de verdad sobre la implementación.
- Evitar duplicar información existente en el AgenDoc Project Blueprint, el AgenDoc Development Playbook y el UI Design Guide.
- Mantener una organización consistente con la estructura real del código.
- Priorizar la simplicidad y la facilidad de mantenimiento.

---

## 20.3 Cuándo actualizar

El Codebase Guide deberá actualizarse en los siguientes casos:

- al finalizar una sesión de desarrollo que modifique la estructura del proyecto;
- antes del versionado de cambios relacionados con la implementación;
- al cierre de cada Sprint;
- cuando se incorpore un nuevo módulo funcional;
- cuando se agreguen nuevas APIs públicas;
- cuando se incorporen componentes reutilizables al Design System;
- cuando se modifique la estructura del repositorio.

No deberá actualizarse por cambios menores que no alteren la organización o el estado general de la implementación.

---

## 20.4 Flujo de Actualización

Toda actualización del Codebase Guide deberá seguir el siguiente flujo:

1. Finalizar la implementación.
2. Ejecutar las pruebas correspondientes.
3. Confirmar el cumplimiento de la Definition of Done.
4. Actualizar las secciones impactadas del Codebase Guide.
5. Revisar la consistencia con el resto de la documentación oficial.
6. Versionar los cambios en el repositorio.

---

## 20.5 Alcance de las Actualizaciones

Cada sección deberá actualizarse únicamente cuando existan cambios en su ámbito de responsabilidad.

| Sección | Evento que requiere actualización |
|----------|-----------------------------------|
| Estado Actual de la Implementación | Cambio de Sprint o estado del proyecto. |
| Tecnologías | Incorporación o retiro de tecnologías oficiales. |
| Estructura del Repositorio | Cambios en la organización del repositorio. |
| Arquitectura Implementada | Cambios en la implementación de la arquitectura. |
| Backend | Nuevos módulos o cambios estructurales. |
| Frontend Web | Nuevos módulos, componentes o cambios estructurales. |
| Mobile | Incorporación de nuevas funcionalidades o estructura. |
| Base de Datos | Nuevas migraciones o cambios relevantes del esquema. |
| APIs Implementadas | Nuevos Controllers o endpoints públicos. |
| Componentes Reutilizables | Nuevos componentes del Design System. |
| Inventario Funcional | Cambio de estado de Historias de Usuario. |
| Technical Stories | Cambio de estado de Technical Stories. |
| Configuración del Proyecto | Cambios en la organización de la configuración. |
| Checklist Técnico | Cambio del estado técnico del proyecto. |
| Mapa de Navegación del Código | Nuevas Historias implementadas o cambios relevantes en los flujos. |
| Mapa de Dependencias | Incorporación o modificación de dependencias entre módulos. |
| Próximo Incremento | Cierre de Sprint o cambio en la planificación inmediata. |
| Historial de Implementación | Cierre de cada Sprint. |

---

## 20.6 Responsabilidad

El Codebase Guide deberá mantenerse sincronizado con el repositorio principal del proyecto.

Toda modificación incorporada al código fuente que afecte la estructura, organización o estado de la implementación deberá reflejarse en este documento antes del versionado correspondiente.

---

## 20.7 Estado del Documento

Este documento forma parte de la documentación oficial de AgenDoc y complementa al:

- AgenDoc Project Blueprint.
- AgenDoc Development Playbook.
- UI Design Guide.

Su contenido describe exclusivamente la implementación existente y no sustituye la planificación funcional, las decisiones arquitectónicas ni las definiciones de experiencia de usuario.

---

**Última actualización**

Sprint:
Sprint 2

Sesión:
Cierre de HU-10 — Crear cita desde recepción

---


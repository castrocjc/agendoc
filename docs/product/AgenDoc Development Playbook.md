# AgenDoc Development Playbook

> Documento oficial que define la forma de trabajo del proyecto AgenDoc.
> Complementa al AgenDoc Project Blueprint y establece el marco operativo que seguirá el equipo durante todo el desarrollo del producto.

| Campo     | Valor                |
|-----------|----------------------|
| Proyecto  | AgenDoc              |
| Documento | Development Playbook |
| Versión   | v1.2                 |
| Estado    | Aprobado             |
| Vigencia  | Desde Sprint 1       |

---

# 1. Propósito del Playbook

## Objetivo

Definir el marco oficial de trabajo que seguirá el equipo durante el desarrollo de AgenDoc.

El Playbook establece cómo se construirá el producto, mientras que el Blueprint define qué se construirá.

## Alcance

Este documento regula:

- forma de trabajo
- responsabilidades
- flujo de desarrollo
- estándares generales
- calidad
- versionado
- gestión de sesiones

No contiene:

- funcionalidades
- arquitectura del producto
- modelo de datos
- UX/UI
- backlog

Toda esa información pertenece exclusivamente al Blueprint.

## Relación con el Blueprint

El Blueprint es la fuente oficial del producto.

El Playbook es la fuente oficial del proceso de desarrollo.

Ambos documentos son complementarios.

---

# 2. Principios de Trabajo

Todo Sprint deberá respetar los siguientes principios.

## Blueprint First

Toda decisión parte del Blueprint.

## MVP First

Solo se desarrolla lo aprobado para el MVP.

## Vertical Slice

Cada incremento debe entregar funcionalidad completa de extremo a extremo.

## Entrega incremental

Cada Sprint debe generar valor funcional.

## Simplicidad

La solución más simple que resuelva correctamente el problema será la preferida.

## Calidad antes que velocidad

Nunca se comprometerá la calidad para entregar más rápido.

## No duplicar decisiones

Una decisión aprobada no debe volver a discutirse sin justificación.

## Backend como fuente de verdad

Las reglas del negocio pertenecen al Backend.

## Consistencia Web y Mobile

Ambos clientes compartirán el mismo comportamiento funcional.

---

# 3. Roles y Responsabilidades

## Product Manager

- Define dirección del producto.
- Prioriza valor de negocio.
- Valida alineación con la visión.

## Product Owner

- Gestiona Product Backlog.
- Refina Historias de Usuario.
- Acepta funcionalidades.

## Scrum Master

- Facilita el proceso Scrum.
- Elimina impedimentos.
- Garantiza cumplimiento del Playbook.

## Software Architect

- Protege la arquitectura aprobada.
- Evalúa impacto técnico.
- Evita deuda arquitectónica.

## Tech Lead

- Lidera decisiones técnicas de implementación.
- Define estrategia de desarrollo.
- Realiza revisiones técnicas.

## QA Lead

- Define estrategia de calidad.
- Valida criterios de aceptación.
- Autoriza paso a Done.

## Engineering Manager

- Coordina ejecución del Sprint.
- Gestiona capacidad del equipo.
- Monitorea avance.

## Developer

- Implementa funcionalidades.
- Escribe código mantenible.
- Ejecuta pruebas antes de integrar.

---

# 4. Artefactos Oficiales y Organización Documental

Los únicos artefactos oficiales del proyecto son:

- AgenDoc Project Blueprint
- AgenDoc Development Playbook
- Repositorio Git

No se crearán documentos paralelos fuera del repositorio.

Toda decisión permanente deberá quedar registrada únicamente en el Blueprint o en el Playbook.

## Ubicación oficial de documentos

La documentación oficial del proyecto deberá almacenarse dentro de la carpeta:

```text
docs/
├── product/
├── architecture/
├── database/
├── ux/
├── api/
├── decisions/
└── releases/

```

## Ubicación de documentos principales:

docs/product/AgenDoc Project Blueprint.md
docs/product/AgenDoc Development Playbook.md

## Reglas:

- El código fuente vive únicamente en backend, frontend-web y mobile.
- La documentación vive en docs.
- Los scripts y recursos de base de datos viven en database.
- La infraestructura vive en docker y .github.
- Los documentos Markdown no deberán quedar dispersos en carpetas de código, salvo README específicos de cada módulo.

---

# 5. Flujo Oficial de Trabajo

Todo desarrollo seguirá el siguiente flujo.

Product Backlog

↓

Sprint Planning

↓

Historia seleccionada

↓

Análisis

↓

Desarrollo

↓

Code Review

↓

QA

↓

Integración

↓

Done

Cada Historia deberá recorrer completamente este flujo.

---

# 6. Ciclo de Vida de una Historia de Usuario

## Draft

Historia identificada pero aún no refinada.

## Ready

Historia preparada para ingresar al Sprint.

## In Progress

Historia en desarrollo.

## Code Review

Código terminado pendiente de revisión técnica.

## QA

Funcionalidad validándose funcionalmente.

## Done

Historia completamente integrada y aceptada.

No se permitirán estados adicionales.

---

# 7. Estándares de Desarrollo

Todo desarrollo deberá respetar los siguientes principios.

- Clean Architecture.
- Separación de responsabilidades.
- Alta cohesión.
- Bajo acoplamiento.
- Reutilización de componentes.
- Respeto del Design System.
- Backend como fuente de reglas.
- Código mantenible.
- Seguridad desde el diseño.
- Convenciones consistentes.
- Evitar duplicación de lógica.
- Refactorizar cuando agregue claridad sin ampliar alcance.

El Playbook no define estándares específicos de código.

---

# 8. Estrategia de Versionado

## Git Flow Simplificado

main

Producto estable.

develop

Integración continua.

feature/*

Desarrollo de cada Historia o conjunto pequeño de Historias relacionadas.

## Flujo

feature

↓

develop

↓

main

No se desarrollará directamente sobre main.

## Versionado del Producto

Formato:

MAJOR.MINOR.PATCH

Durante el MVP normalmente evolucionará únicamente MINOR y PATCH.


---

## 8.1 Convención de Commits

El proyecto utilizará Conventional Commits.

Tipos permitidos:

- feat
- fix
- refactor
- docs
- style
- test
- build
- ci
- perf
- chore

Ejemplos:

```text
chore: initialize backend project
build: configure maven wrapper
ci: add github actions workflow
feat: implement login endpoint
fix: validate duplicated appointment
docs: update development playbook
```

---

# 9. Calidad

## Definition of Ready

Una Historia podrá ingresar al Sprint únicamente si:

- está refinada
- tiene criterios de aceptación
- tiene prioridad
- tiene estimación
- no presenta bloqueos

## Definition of Done

Una Historia estará terminada cuando:

- cumple criterios de aceptación
- código revisado
- pruebas ejecutadas
- integrada en develop
- sin defectos críticos
- documentación oficial actualizada cuando corresponda

## QA mínimo

Toda Historia deberá validar como mínimo:

- comportamiento esperado
- casos negativos
- permisos
- validaciones
- consistencia visual
- comportamiento Web y Mobile cuando aplique

---

## 9.1 Definition of Ready para una nueva rama

Antes de comenzar cualquier desarrollo en una nueva rama:

- Rama creada desde `develop`.
- Repositorio sincronizado.
- `git pull` ejecutado.
- Proyecto compila.
- Backend inicia cuando aplique.
- Frontend Web inicia cuando aplique.
- Mobile inicia cuando aplique.
- `git status` limpio.

## 9.2 Definition of Done para cerrar una sesión

Una sesión podrá cerrarse cuando:

- Código compila.
- Validaciones ejecutadas.
- Commits realizados con Conventional Commits.
- Push realizado.
- Rama sincronizada con GitHub.
- No existen archivos temporales o innecesarios versionados.
- Blueprint actualizado si corresponde.
- Playbook actualizado si corresponde.
- Prompt preparado para la siguiente sesión cuando aplique.

---

# 10. Gestión del Blueprint

El Blueprint únicamente se actualizará cuando exista una decisión aprobada que modifique:

- visión
- alcance
- dominio
- modelo de datos
- arquitectura
- UX/UI
- backlog
- ADR
- roadmap

No se actualizará por:

- avances de código
- tareas técnicas
- refactorizaciones
- bugs
- detalles de implementación

Esto mantiene el Blueprint como documento estratégico.

---

# 11. Estructura Oficial de las Sesiones

Todas las sesiones seguirán el mismo flujo.

1. Revisar Blueprint.
2. Revisar Playbook.
3. Identificar Historia objetivo.
4. Analizar impacto.
5. Diseñar solución.
6. Implementar.
7. Validar.
8. Versionar.
9. Actualizar Blueprint únicamente si corresponde.
10. Preparar siguiente sesión.

Este flujo será utilizado durante todo el proyecto.

---

# 12. Reglas del Proyecto

El equipo deberá respetar permanentemente las siguientes reglas.

- No salir del alcance del MVP.
- No crear documentación innecesaria.
- No modificar decisiones aprobadas sin justificación.
- Mantener el Blueprint como fuente oficial del producto.
- Mantener el Playbook como fuente oficial del proceso.
- Cada Sprint debe entregar valor.
- Mantener el producto potencialmente desplegable.
- Mantener consistencia entre producto, arquitectura y código.
- Resolver primero la causa antes que el síntoma.
- Priorizar simplicidad sobre complejidad.
- Favorecer reutilización antes que duplicación.
- Toda funcionalidad debe poder trazarse hasta una Historia de Usuario.
- Toda Historia debe poder trazarse hasta el Blueprint.
- Ningún cambio funcional será considerado oficial hasta quedar reflejado en el Blueprint cuando corresponda.

---

# 13. Estrategia Oficial de Inicio del Desarrollo

A partir del Sprint 1, el desarrollo del producto seguirá una estrategia única de construcción.

El objetivo es establecer una base técnica sólida antes de implementar funcionalidades de negocio, reduciendo deuda técnica y garantizando consistencia durante todo el MVP.

---

## Objetivo del Sprint 1

Construir la plataforma base de AgenDoc preparada para iniciar el desarrollo funcional del MVP.

Durante este Sprint se establecerá la infraestructura técnica, la organización física del proyecto y los componentes habilitadores necesarios para desarrollar las Historias de Usuario aprobadas de forma incremental.

---

## Organización del Sprint

El Sprint 1 se divide oficialmente en dos fases.

### Fase A — Foundation

Objetivo:

Construir toda la infraestructura técnica del proyecto sin desarrollar funcionalidades de negocio.

Incluye como mínimo:

- creación del repositorio Git
- configuración de Git Flow
- estructura física del repositorio
- Backend Spring Boot
- Frontend React + TypeScript
- Mobile React Native + Expo
- PostgreSQL
- Flyway
- Docker
- GitHub Actions
- variables de entorno
- configuración inicial de JWT
- configuración inicial de Spring Security
- logging
- manejo global de errores
- configuración compartida
- estructura modular
- convenciones de paquetes
- convenciones de carpetas

Durante esta fase no se desarrollarán:

- APIs funcionales
- entidades del dominio
- migraciones del modelo de negocio
- componentes funcionales
- pantallas del MVP
- Historias de Usuario

El objetivo exclusivo consiste en dejar preparada la plataforma para iniciar el desarrollo incremental.

---

### Fase B — Desarrollo Funcional

Una vez concluida la Foundation comenzará el desarrollo de las Historias de Usuario aprobadas para el Sprint.

El orden oficial de implementación será:

1. HU-01 — Iniciar sesión
2. HU-02 — Cerrar sesión
3. HU-04 — Registrar médico
4. HU-05 — Registrar paciente
5. HU-06 — Buscar paciente
6. HU-07 — Crear bloques de agenda médica

Cada Historia deberá recorrer completamente el flujo definido en este Playbook:

Ready

↓

In Progress

↓

Code Review

↓

QA

↓

Done

No se implementarán Historias fuera del Sprint Backlog aprobado.

---

## Orden Oficial de Construcción

Todo el proyecto deberá construirse siguiendo el siguiente orden:

1. Crear el repositorio Git.
2. Configurar la estrategia de ramas.
3. Crear la estructura física del repositorio.
4. Inicializar el Backend Spring Boot.
5. Inicializar el Frontend React + TypeScript.
6. Inicializar la aplicación Mobile con React Native y Expo.
7. Configurar Docker.
8. Configurar PostgreSQL.
9. Configurar Flyway.
10. Configurar variables de entorno.
11. Configurar logging.
12. Configurar el manejo global de errores.
13. Configurar JWT.
14. Configurar Spring Security.
15. Configurar GitHub Actions.
16. Validar la compilación completa del proyecto.
17. Iniciar la implementación de las Historias de Usuario.

Este orden deberá mantenerse durante toda la construcción del MVP salvo que una decisión arquitectónica aprobada indique lo contrario.

---

## Organización Física del Repositorio

El repositorio del proyecto mantendrá una estructura estable durante todo el MVP.

La estructura oficial será:

```text
agendoc/
├── backend/
├── frontend-web/
├── mobile/
├── database/
├── docker/
├── scripts/
├── docs/
├── .github/
├── README.md
├── .gitignore
├── .editorconfig
├── .gitattributes
└── .env.example
```

Responsabilidades:
- backend: Backend API con Java Spring Boot.
- frontend-web: Aplicación Web con React y TypeScript.
- mobile: Aplicación Mobile con React Native y Expo.
- database: migraciones, seeds y scripts de base de datos.
- docker: configuración de contenedores.
- scripts: scripts de soporte del proyecto.
- docs: documentación oficial del producto y del proceso.
- .github: configuración de GitHub Actions y automatizaciones.

Cada componente será responsable únicamente de su contexto y no deberá duplicar responsabilidades de otros módulos.

README por módulo

Cada módulo principal podrá tener un README específico:

backend/README.md
frontend-web/README.md
mobile/README.md
database/README.md
docker/README.md
docs/README.md

Estos README deberán ser breves y explicar únicamente:
- propósito del módulo
- cómo ejecutarlo
- tecnologías principales
- documentación relacionada

---

## Ubicación de la documentación oficial

Toda la documentación estratégica del proyecto deberá mantenerse dentro de la carpeta `docs`.

Los documentos principales serán:

- docs/product/AgenDoc Project Blueprint.md
- docs/product/AgenDoc Development Playbook.md

Se evitará mantener copias del Blueprint o del Playbook fuera de esta ubicación para garantizar una única fuente de verdad.

---

## Regla Oficial de Foundation

Mientras la Fase Foundation no haya sido completada oficialmente:

- no se desarrollarán funcionalidades de negocio;
- no se implementarán APIs funcionales;
- no se crearán entidades del dominio;
- no se desarrollarán pantallas del MVP;
- no se implementarán Historias de Usuario.

Toda sesión deberá priorizar la construcción de la plataforma base hasta completar esta fase.

Solo una vez finalizada la Foundation podrá iniciarse el desarrollo funcional del Sprint.

---

# Validación de Consistencia

Este Playbook:

- No modifica la Product Vision.
- No modifica el MVP.
- No modifica el Modelo de Dominio.
- No modifica el Modelo de Datos.
- No modifica la Arquitectura.
- No modifica UX/UI.
- No modifica el Product Backlog.

El documento define exclusivamente la forma oficial de trabajo del proyecto.

---

# Cierre del Sprint 0

Con la aprobación del Blueprint y del Development Playbook se declara concluido oficialmente el Sprint 0.

A partir de este punto el proyecto entra en fase de construcción del producto.

Todos los Sprints futuros deberán seguir este Playbook.

---

# Próxima Sesión

Sprint 1 Planning

Objetivos:

- Revisar Blueprint y Playbook.
- Confirmar capacidad del Sprint.
- Refinar las Historias seleccionadas.
- Validar dependencias.
- Confirmar objetivos del Sprint.
- Elaborar Sprint Backlog.
- Definir estrategia de implementación del primer Vertical Slice.
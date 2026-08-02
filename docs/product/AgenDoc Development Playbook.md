# AgenDoc Development Playbook

> Documento oficial que define la forma de trabajo del proyecto AgenDoc.
> Complementa al AgenDoc Project Blueprint y establece el marco operativo que seguirá el equipo durante todo el desarrollo del producto.

| Campo     | Valor                |
|-----------|----------------------|
| Proyecto  | AgenDoc              |
| Documento | Development Playbook |
| Versión   | v1.4                 |
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

- El código fuente vive únicamente en backend, frontend y mobile.
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

Refinamiento funcional

↓

Diseño UX/UI mínimo

↓

Contrato API

↓

Modelo de datos (si aplica)

↓

Backend

↓

Pruebas Backend

↓

Frontend

↓

Pruebas Integradas

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

# 7.1 Estrategia de Implementación de Historias de Usuario

AgenDoc adopta oficialmente una estrategia de desarrollo basada en Vertical Slice.

Cada Historia de Usuario deberá entregar valor funcional completo y potencialmente desplegable.

Sin embargo, la implementación técnica de cada Historia seguirá una construcción progresiva desde las capas inferiores hacia las superiores.

Esta estrategia permite mantener una arquitectura sólida sin perder la entrega incremental de valor.

## Principios

- La Historia de Usuario es la unidad oficial de entrega.
- No se implementarán capas completas del sistema de forma aislada.
- Cada Historia incorporará únicamente los cambios técnicos necesarios para cumplir su objetivo.
- El Backend continuará siendo la fuente oficial de reglas de negocio.
- El Frontend consumirá siempre contratos definidos por el Backend.
- Ninguna Historia será considerada terminada utilizando datos simulados (mocks) permanentes.

## Flujo Oficial de Implementación

Cada Historia seguirá preferentemente el siguiente orden técnico:

1. Refinamiento funcional.
2. Diseño UX/UI mínimo necesario.
3. Definición del contrato API.
4. Modelo de datos y migraciones mínimas.
5. Implementación del Backend.
6. Pruebas unitarias e integración del Backend.
7. Validación de APIs.
8. Implementación del Frontend Web.
9. Implementación Mobile cuando aplique.
10. Pruebas funcionales integradas.
11. Code Review.
12. QA.
13. Done.

Este flujo busca reducir retrabajo, mantener consistencia técnica y validar cada incremento de extremo a extremo.

Las Technical Stories podrán ejecutarse cuando soporten directamente una Historia de Usuario del Sprint o constituyan trabajo habilitador previamente aprobado.

Las Technical Stories no representan funcionalidades visibles para el usuario final y no sustituyen Historias de Usuario del Product Backlog.

La definición funcional, alcance, dependencias, estado y planificación de las Technical Stories se mantiene exclusivamente en el AgenDoc Project Blueprint.

### Planificación de Technical Stories

El Sprint Backlog podrá estar compuesto por Historias de Usuario y Technical Stories.

Durante la planificación del Sprint deberán evaluarse las dependencias técnicas necesarias para soportar el incremento funcional comprometido.

Cuando una Historia de Usuario requiera capacidades técnicas aún no implementadas, la Technical Story correspondiente deberá incorporarse al Sprint.

Las Technical Stories seguirán el mismo ciclo de refinamiento, implementación, pruebas, documentación y versionado que las Historias de Usuario.

---

## 7.2 Configuración por Tecnología

Cada componente del proyecto deberá utilizar el mecanismo de configuración nativo de la tecnología correspondiente.

### Backend (Spring Boot)

La configuración se gestionará mediante perfiles de Spring Boot.

Archivos oficiales:

```text
application.yml
application-local.yml
application-dev.yml
application-prod.yml
```

Reglas:

- `application.yml` contendrá únicamente la configuración común.
- Cada perfil contendrá exclusivamente la configuración específica de su ambiente.
- No se utilizarán archivos `.env` para la configuración del Backend.
- No se incorporarán librerías adicionales para interpretar archivos `.env`.
- La configuración de los ambientes de integración y producción utilizará variables de entorno estándar de Spring Boot cuando corresponda.
- Las propiedades sensibles, como secretos JWT y credenciales de base de datos, deberán obtenerse exclusivamente mediante variables de entorno o mecanismos equivalentes del ambiente de ejecución.

### Frontend Web (React + Vite)

La configuración seguirá las convenciones oficiales de Vite mediante archivos `.env`.

Ejemplos:

```text
.env.local
.env.development
.env.production
```

### Aplicación Mobile (React Native + Expo)

La configuración seguirá las convenciones oficiales de Expo mediante archivos `.env`.

### Principio

Cada tecnología utilizará su mecanismo oficial de configuración.

Se evitará adaptar una tecnología para seguir las convenciones de otra cuando ello agregue complejidad innecesaria.

### Uso de Docker

Durante el desarrollo del MVP no se utilizará Docker como parte del entorno local de desarrollo.

El proyecto utilizará PostgreSQL instalado localmente y la ejecución nativa de las tecnologías seleccionadas.

La estructura del repositorio conservará la carpeta `docker/` para facilitar una futura incorporación de contenedores cuando el proyecto lo requiera.

### Configuración CORS

Toda API expuesta por AgenDoc para consumo desde Frontend Web o Mobile deberá incluir configuración CORS desde la fase Foundation.

La configuración deberá:

- habilitar únicamente los orígenes autorizados para el ambiente correspondiente;
- permitir los métodos HTTP requeridos por la API;
- permitir los encabezados necesarios para la autenticación;
- mantenerse centralizada dentro de la configuración de seguridad.

Esto evita problemas de integración durante el desarrollo y mantiene una política consistente entre ambientes.

---

## 7.3 Puertos oficiales para desarrollo local

Durante el desarrollo local del MVP se utilizarán los siguientes puertos oficiales:

| Componente      | Puerto | URL local             |
|-----------------|-------:|-----------------------|
| Frontend Web    | 5173   | http://localhost:5173 |
| Backend API     | 8081   | http://localhost:8081 |
| PostgreSQL      | 5432   | localhost:5432        |
| Mobile Expo Web | 8082   | http://localhost:8082 |

Reglas:

- El Backend API se ejecutará en el puerto 8081.
- El Frontend Web utilizará el puerto 5173 por defecto de Vite.
- PostgreSQL utilizará el puerto 5432.
- Mobile con Expo Web utilizará el puerto 8082 cuando aplique.
- No se utilizará el puerto 8080 para el Frontend Web porque puede generar conflictos en entornos locales.
- El Frontend Web consumirá el Backend mediante la variable `VITE_API_BASE_URL`.

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

## 8.2 Herramienta Oficial de Git

Todas las operaciones habituales de GitHub deberán ejecutarse mediante GitHub CLI (gh).

La interfaz web de GitHub se reservará únicamente para actividades excepcionales, como administración del repositorio o configuración de permisos.

Las operaciones oficiales incluyen:

- autenticación
- creación de Pull Requests
- revisión de Pull Requests
- consulta de checks
- merge
- eliminación de ramas

---

## 8.3 Flujo Oficial de Versionado

Preparación

1. git checkout develop
2. git pull origin develop
3. git fetch --prune
4. git status
5. git branch
6. git branch -r
7. git checkout -b feature/<nombre>

Durante el desarrollo

8. Build
9. Pruebas
10. git add
11. git commit
12. git push
13. gh pr create
14. gh pr checks
15. gh pr view
16. gh pr merge --delete-branch

Después del merge

17. git checkout develop
18. git pull origin develop
19. git fetch --prune
20. git status
21. git branch
22. git branch -r

Nota:
Durante el desarrollo individual del MVP, el proyecto podrá integrar directamente los cambios sobre develop, omitiendo la creación de Pull Requests. Cuando exista más de un desarrollador o se establezcan revisiones formales de código, se retomará el flujo completo mediante Pull Requests.

---

## 8.4 Política de Gestión de Ramas

Con el fin de mantener el repositorio limpio y facilitar la colaboración, se adoptan las siguientes reglas:

- Toda Historia de Usuario se desarrollará en una rama `feature/*`.
- Toda rama `feature/*` deberá eliminarse inmediatamente después de completar el merge hacia `develop`.
- Las ramas locales también deberán eliminarse una vez sincronizado `develop`.
- No deberán mantenerse ramas cerradas o sin uso en el repositorio remoto.
- Después de eliminar ramas remotas deberá ejecutarse:

git fetch --prune

para sincronizar las referencias locales.

---

## 8.5 Estándar de Pull Request

Todo Pull Request deberá seguir una estructura uniforme para facilitar la revisión y mantener la trazabilidad de las Historias de Usuario.

Como mínimo deberá incluir:

- Resumen de la implementación.
- Cambios realizados en Backend.
- Cambios realizados en Frontend.
- Validaciones y pruebas ejecutadas.
- Historia de Usuario asociada.
- Confirmación del cumplimiento de los criterios de aceptación.

La creación y gestión de Pull Requests se realizará utilizando GitHub CLI (`gh`) como herramienta oficial del proyecto.

---

## 8.6 Estrategia de Merge

- Todo Pull Request deberá aprobar todos los GitHub Actions antes del merge.
- El merge oficial será mediante GitHub CLI.
- Se eliminará automáticamente la rama remota utilizando --delete-branch.
- Finalizado el merge, el desarrollador sincronizará develop y limpiará las referencias remotas con git fetch --prune.

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
- Cambios integrados correctamente en develop.
- todos los GitHub Actions finalizan exitosamente
- integrada en develop
- ramas feature eliminadas local y remotamente
- develop sincronizada con origin/develop
- working tree limpio
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
- Ejecutar `git fetch --prune` para limpiar referencias remotas obsoletas.
- Verificar las ramas locales mediante `git branch`.
- Verificar las ramas remotas mediante `git branch -r`.
- Confirmar que `develop` se encuentra sincronizada con el repositorio remoto antes de crear una nueva rama `feature/*`.

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
- APIs integradas con el Frontend cuando corresponda.
- No existen dependencias de datos simulados (mocks) para el funcionamiento normal.
- La funcionalidad fue validada de extremo a extremo.

---

## 9.3 Definition of Done para una Historia con Backend

Cuando una Historia de Usuario incluya implementación en el Backend, deberá validarse como mínimo lo siguiente antes de considerarla técnicamente estable:

- Migraciones Flyway implementadas y validadas cuando corresponda.
- Entidades persistentes implementadas cuando corresponda.
- Repositorios implementados y validados.
- Servicios de aplicación y reglas de negocio implementados.
- Controlador REST implementado.
- Contrato de entrada y salida definido mediante DTOs.
- Bean Validation implementada para los datos de entrada.
- Configuración de Spring Security actualizada cuando corresponda.
- El flujo de autenticación JWT fue validado cuando la Historia involucre endpoints protegidos.
- Manejo global de excepciones actualizado cuando corresponda.
- Respuestas HTTP y estructura de errores consistentes.
- Caso funcional exitoso validado.
- Casos negativos relevantes validados.
- El proyecto compila correctamente.
- Las pruebas automatizadas pasan correctamente.
- La API fue validada antes de integrarse con el Frontend.
- No existen excepciones temporales, código de prueba ni utilitarios innecesarios.
- No se exponen contraseñas, hashes, tokens, trazas internas ni información sensible.
- El incremento quedó versionado mediante Conventional Commits.

Esta validación complementa la Definition of Done general y no reemplaza la validación funcional de extremo a extremo de la Historia.

---

## 9.4 Checklist Operacional

## Antes del Pull Request

□ Build Backend exitoso
□ Build Frontend exitoso
□ QA funcional completado
□ git status limpio
□ git diff revisado
□ Conventional Commit realizado
□ Push ejecutado
□ Pull Request creado
□ Checks aprobados

## Después del Merge

□ Merge realizado
□ Rama remota eliminada
□ Rama local eliminada
□ git checkout develop ejecutado
□ git pull origin develop ejecutado
□ git fetch --prune ejecutado
□ git branch validado
□ git branch -r validado
□ Working tree limpio verificado mediante git status
□ Documentación revisada
□ Prompt preparado para la siguiente sesión

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

Una vez completada la Foundation, todas las Historias de Usuario se implementarán siguiendo la Estrategia de Implementación de Historias de Usuario definida en este Playbook.

La construcción técnica de cada Historia seguirá un enfoque progresivo desde el modelo de datos mínimo requerido hasta la integración completa con Frontend Web y Mobile, manteniendo el principio de Vertical Slice.

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
- carpeta `docker/` reservada para futuras configuraciones
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
7. Reservar carpeta `docker/` para futuras configuraciones.
8. Configurar PostgreSQL local.
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
├── frontend/
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
- frontend: Aplicación Web con React y TypeScript.
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
frontend/README.md
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

# Sesión actual

Sprint 4 — Autogestión del paciente

Objetivo:

Implementar la autorización por dominio para habilitar de forma segura HU-09 — Reservar cita como paciente y HU-11 — Consultar mis citas.

Technical Story completada:

TS-03 — Implementar contexto del usuario autenticado.

Resultado:

- Se implementó un contexto centralizado del usuario autenticado.
- El Backend puede obtener el usuario, rol y consultorio desde Spring Security.
- Los perfiles de negocio Paciente y Médico pueden resolverse desde el usuario autenticado cuando corresponda.
- El rol Recepcionista utiliza el contexto de usuario y consultorio sin requerir actualmente una entidad de negocio independiente.
- La implementación fue validada con 65 pruebas automatizadas exitosas.
- El flujo JWT End-to-End permanece operativo.
- Backend y Frontend fueron validados sin regresiones.

Technical Story activa:

TS-04 — Implementar autorización por dominio.

Siguientes incrementos:

- HU-09 — Reservar cita como paciente.
- HU-11 — Consultar mis citas como paciente.

Antes de iniciar:

- Validar que `develop` se encuentre sincronizada con `origin/develop`.
- Confirmar que Backend y Frontend compilan correctamente.
- Confirmar que todas las pruebas automatizadas finalizan exitosamente.
- Confirmar que el flujo JWT End-to-End permanece operativo.
- Revisar el uso de `AuthenticatedUserContextProvider` en los casos de uso del dominio.
- Analizar las restricciones por rol, consultorio y propiedad del recurso.
- Mantener el desarrollo exclusivamente dentro del alcance de TS-04.

Estado:

En ejecución.

---

# 14. Reglas de Ejecución de Historias

Durante todo el desarrollo del MVP deberán respetarse las siguientes reglas:

- La Historia de Usuario constituye la unidad oficial de entrega.
- No se desarrollarán módulos completos de Base de Datos, Backend o Frontend de forma aislada.
- Cada Historia incorporará únicamente los cambios mínimos necesarios en el modelo de datos.
- Toda API deberá validarse antes de integrarse con el Frontend.
- El Frontend consumirá contratos reales definidos por el Backend.
- Los datos simulados (mocks) solo podrán utilizarse temporalmente durante el desarrollo y deberán eliminarse antes de declarar una Historia como Done.
- Ninguna Historia será considerada terminada hasta validar correctamente el flujo completo de extremo a extremo.
- Las mejoras visuales deberán mantenerse dentro del alcance de la Historia y no deberán retrasar indefinidamente la implementación funcional.
- Cualquier excepción a estas reglas deberá justificarse y documentarse mediante una decisión aprobada.

---

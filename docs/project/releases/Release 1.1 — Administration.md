# Release 1.1 — Administration

> Documento rector y Product Backlog oficial de la segunda Release de AgenDoc.
>
> Esta Release evoluciona AgenDoc desde un Producto Mínimo Viable funcional hacia una plataforma que pueda ser administrada y operada de forma autónoma por un consultorio médico, preservando la arquitectura multi-tenant, JWT Stateless, Role Based Authorization, Domain Authorization y Clinic Isolation.

| Campo | Valor |
|---|---|
| Proyecto | AgenDoc |
| Documento | Release 1.1 — Administration |
| Versión | v1.1 |
| Estado | ✅ Backlog refinado |
| Release | 1.1 |
| Release anterior | Release 1.0 — MVP |
| Sprints previstos | Sprint 6 a Sprint 10 |
| Próximo incremento | Sprint 6 |
| Primera historia recomendada | HU-22 — Acceder al Workspace de Administración |
| Primer habilitador recomendado | TS-07 — Incorporar soporte para ADMIN y capacidades administrativas |

---

# Historial del documento

| Versión | Fecha | Autor | Descripción |
|---|---|---|---|
| v1.0 | Agosto 2026 | Equipo AgenDoc | Creación de la planificación inicial. |
| v1.1 | Agosto 2026 | Equipo AgenDoc | Refinamiento de épicas, Product Backlog, habilitadores técnicos y planificación preliminar de Sprints 6 a 10. |

---

# 1. Release Overview

## Estado

✅ **PRODUCT BACKLOG REFINADO**

La Release se encuentra lista para iniciar la planificación detallada y ejecución del Sprint 6.

## Objetivo estratégico

Permitir que un consultorio médico administre y opere su propia instancia de AgenDoc sin depender del equipo de desarrollo para actividades administrativas y operativas propias del negocio.

## Resultado esperado

Al finalizar la Release, un consultorio podrá:

- acceder a un Workspace de Administración;
- administrar usuarios, roles y capacidades;
- mantener perfiles administrativos de médicos, pacientes y personal;
- configurar datos básicos del consultorio y sus especialidades;
- configurar agendas recurrentes;
- generar disponibilidad de forma masiva;
- registrar excepciones de disponibilidad;
- revisar una auditoría básica de acciones administrativas;
- operar las capacidades funcionales incluidas sin intervención del equipo de desarrollo.

## Criterio rector

Toda Historia de Usuario de esta Release deberá responder afirmativamente:

> ¿Esta funcionalidad acerca a AgenDoc al objetivo de que un consultorio pueda operar de forma autónoma?

---

# 2. Problema de negocio

La Release 1.0 permite ejecutar el ciclo funcional básico de una cita médica, pero el consultorio todavía depende del equipo de desarrollo para mantener usuarios, perfiles, catálogos, configuración operativa y disponibilidad futura.

La Release 1.1 elimina esa dependencia sin incorporar capacidades clínicas avanzadas ni funciones propias de la administración comercial de una plataforma SaaS.

---

# 3. Objetivos

- Incorporar capacidades administrativas dentro del contexto de un consultorio.
- Permitir la gestión autónoma de usuarios y perfiles de dominio.
- Permitir la configuración básica de la operación del consultorio.
- Mejorar la administración de médicos, pacientes y personal autorizado.
- Permitir la configuración recurrente y excepcional de agendas.
- Mantener trazabilidad básica de operaciones administrativas.
- Fortalecer calidad, seguridad y mantenibilidad para futuras Releases.

---

# 4. Principios

1. El Consultorio continúa siendo el tenant funcional.
2. Toda operación se limita mediante Clinic Isolation.
3. Se mantiene JWT Stateless.
4. Se mantiene Role Based Authorization.
5. Se mantiene Domain Authorization.
6. Los roles oficiales son ADMIN, RECEPTIONIST, DOCTOR y PATIENT.
7. No se crearán roles adicionales para representar variantes operativas.
8. Las diferencias funcionales se resolverán mediante capacidades específicas.
9. Una identidad de usuario puede mantener varios roles y perfiles de dominio.
10. La autenticación, autorización y perfiles de negocio permanecen desacoplados.
11. El Backend continúa siendo la fuente de verdad.
12. La autonomía operativa tiene prioridad sobre nuevas funciones clínicas.
13. La Release administra consultorios existentes, no provisiona nuevos tenants SaaS.

---

# 5. Alcance definitivo

## Incluido

- Administration Workspace.
- Gestión de usuarios.
- Asignación de múltiples roles a una identidad.
- Gestión de capacidades administrativas delegables.
- Activación y desactivación de cuentas.
- Cambio de contraseña propia.
- Restablecimiento administrativo de acceso.
- Gestión administrativa de médicos.
- Gestión administrativa de pacientes.
- Gestión de datos básicos del consultorio.
- Gestión de especialidades.
- Reglas recurrentes de agenda.
- Generación masiva de disponibilidad.
- Excepciones de disponibilidad.
- Consulta y ajuste de disponibilidad futura.
- Auditoría administrativa básica.
- Refactorizaciones estrictamente necesarias.
- Componentes reutilizables para experiencias administrativas.
- Fortalecimiento de pruebas y regresión.

## Fuera del alcance

- Historia clínica estructurada.
- Diagnósticos, recetas y adjuntos.
- Notificaciones.
- Dashboards analíticos y KPIs operativos.
- Automatización avanzada de operaciones.
- Integraciones externas.
- Provisionamiento de nuevos consultorios.
- Administración global de tenants.
- Suscripciones, planes, facturación o cobros SaaS.
- Despliegue público, Vercel, Railway o demo comercial.
- Marketplace.
- Dominios personalizados.
- Identidad global transversal entre consultorios.

---

# 6. Criterios de éxito

## Negocio

- Las actividades administrativas incluidas pueden ejecutarse sin intervención del equipo de desarrollo.
- Un administrador puede mantener usuarios, médicos, pacientes, especialidades y configuración básica.
- La agenda futura puede construirse mediante reglas recurrentes y excepciones.
- Personal autorizado puede recibir capacidades operativas sin convertirse en ADMIN.

## Seguridad

- Toda operación administrativa se ejecuta dentro del consultorio autenticado.
- Ningún usuario puede consultar o modificar datos de otro consultorio.
- Las capacidades delegadas no conceden acceso administrativo completo.
- La administración de acceso mantiene trazabilidad básica.
- No se introducen regresiones en autenticación ni autorización.

## Calidad

- Todas las historias tienen pruebas automatizadas apropiadas.
- Backend mantiene BUILD SUCCESS.
- Frontend mantiene ESLint, TypeScript y Production Build SUCCESS.
- Las pruebas de regresión cubren las capacidades de Release 1.0 afectadas.
- La documentación oficial permanece sincronizada.

---

# 7. Definition of Done de la Release

La Release 1.1 estará Done cuando:

- todas las Historias de Usuario comprometidas estén aceptadas;
- todos los habilitadores técnicos obligatorios estén completados;
- el Workspace de Administración esté disponible;
- usuarios, roles, capacidades y estados de cuenta puedan administrarse;
- médicos, pacientes, especialidades y datos básicos del consultorio puedan mantenerse;
- agendas recurrentes, generación masiva y excepciones estén operativas;
- exista auditoría administrativa básica;
- Clinic Isolation, RBAC y Domain Authorization estén validados;
- no existan regresiones críticas de la Release 1.0;
- Backend y Frontend mantengan sus controles de calidad;
- los documentos de Release, Summary e History estén sincronizados.

---

# 8. Épicas oficiales

## EP-07 — Administración del Consultorio

### Objetivo

Proporcionar el punto central desde el cual un consultorio administra su configuración y catálogos básicos.

### Problema de negocio

El consultorio no dispone de una experiencia administrativa propia y depende del equipo de desarrollo para mantener información operativa básica.

### Valor entregado

- Acceso centralizado a funciones administrativas.
- Configuración autónoma del consultorio.
- Mantenimiento autónomo de especialidades.
- Base de navegación para las demás épicas.

### Dependencias

- TS-07.
- Contexto autenticado existente.
- Clinic Isolation existente.
- Componentes administrativos reutilizables.

### Riesgos

- Convertir el Workspace en un dashboard analítico fuera de alcance.
- Exponer opciones para las que el usuario no tiene capacidad.
- Mezclar administración del consultorio con administración global SaaS.

### Definition of Done

- El acceso está limitado a ADMIN y usuarios con capacidades delegadas.
- La navegación muestra exclusivamente opciones autorizadas.
- Los datos modificables pertenecen al consultorio autenticado.
- La configuración y las especialidades pueden mantenerse sin soporte técnico.

---

## EP-08 — Gestión de Usuarios, Perfiles y Seguridad

### Objetivo

Permitir que el consultorio gestione identidades, roles, capacidades, estados de cuenta y perfiles asociados.

### Problema de negocio

Las cuentas y relaciones entre usuario, rol y perfil de dominio requieren intervención técnica y el modelo actual no cubre adecuadamente identidades con múltiples funciones.

### Valor entregado

- Alta y mantenimiento autónomo de usuarios.
- Soporte de múltiples roles por identidad.
- Delegación controlada de capacidades.
- Gestión segura del acceso.
- Separación explícita entre identidad y perfiles de negocio.

### Dependencias

- TS-07.
- TS-08.
- TS-09.
- Módulos existentes de autenticación, usuarios, médicos y pacientes.

### Riesgos

- Escalada de privilegios.
- Inconsistencias entre roles y perfiles.
- Duplicación de identidades.
- Bloqueo accidental del último administrador activo.
- Complejidad excesiva en permisos.

### Definition of Done

- Una identidad puede tener uno o más roles válidos.
- Los perfiles de dominio se vinculan sin duplicar la cuenta.
- Las capacidades delegadas se validan en Backend.
- Las cuentas pueden activarse o desactivarse con reglas seguras.
- El acceso puede restablecerse sin exponer contraseñas.
- Todas las operaciones respetan Clinic Isolation.

---

## EP-09 — Gestión Operativa del Consultorio

### Objetivo

Permitir el mantenimiento cotidiano de médicos, pacientes y personal relacionado con la operación.

### Problema de negocio

Los registros operativos existen para soportar el MVP, pero no pueden administrarse integralmente desde el producto.

### Valor entregado

- Incorporación y mantenimiento de médicos.
- Mantenimiento administrativo de pacientes.
- Actualización de perfiles por sus propietarios cuando corresponda.
- Menor dependencia del equipo técnico para corregir o completar información.

### Dependencias

- EP-08.
- Gestión de especialidades.
- TS-08 y TS-09.
- Módulos Doctor y Patient existentes.

### Riesgos

- Duplicación de médicos o pacientes.
- Eliminación de perfiles con historia transaccional.
- Confusión entre desactivar cuenta y desactivar perfil operativo.
- Modificación indebida de datos sensibles.

### Definition of Done

- Médicos y pacientes pueden mantenerse dentro del consultorio.
- La creación reutiliza una identidad existente cuando corresponde.
- Los registros con historia no se eliminan físicamente.
- Los estados operativos se aplican sin romper citas históricas.
- Los pacientes pueden mantener sus datos administrativos autorizados.

---

## EP-10 — Gestión Avanzada de Agenda

### Objetivo

Permitir que el consultorio configure disponibilidad médica futura mediante reglas recurrentes, generación masiva y excepciones.

### Problema de negocio

La disponibilidad debe generarse manualmente y no existe una forma consistente de registrar ausencias, bloqueos o cambios excepcionales.

### Valor entregado

- Menor esfuerzo para construir agendas.
- Mayor control sobre disponibilidad futura.
- Reducción de errores y conflictos.
- Operación autónoma de cambios de horario.

### Dependencias

- Médicos activos.
- TS-11.
- Módulos Agenda y Appointment existentes.
- Reglas actuales de no superposición y protección de citas.

### Riesgos

- Sobrescribir bloques con citas.
- Generar disponibilidad duplicada.
- Introducir reglas recurrentes ambiguas.
- Incrementar el acoplamiento del módulo Appointment.
- Aplicar excepciones retroactivas.

### Definition of Done

- Se pueden definir reglas recurrentes por médico.
- Se puede generar disponibilidad para un rango futuro.
- Se detectan duplicados y superposiciones.
- Se pueden registrar excepciones.
- Los bloques con citas no se eliminan ni invalidan silenciosamente.
- Toda operación permanece aislada por consultorio.

---

## EP-11 — Calidad y Fortalecimiento de Plataforma

### Objetivo

Fortalecer seguridad, trazabilidad, mantenibilidad y experiencia administrativa para soportar la Release sin degradar el MVP.

### Problema de negocio

La ampliación de usuarios, permisos, perfiles y agendas incrementa el riesgo de regresiones, duplicación y deuda técnica.

### Valor entregado

- Controles de seguridad consistentes.
- Auditoría básica.
- Componentes reutilizables.
- Menor deuda técnica.
- Mayor cobertura de regresión.

### Dependencias

- Todas las épicas funcionales.
- TS-09 a TS-13.

### Riesgos

- Convertirse en una iniciativa de refactorización abierta.
- Incorporar infraestructura no requerida por las historias.
- Posponer valor funcional por perfeccionamiento técnico.

### Definition of Done

- Los habilitadores obligatorios están completados.
- Existe auditoría básica para acciones críticas.
- Los componentes administrativos principales son reutilizables.
- Se conserva la arquitectura modular.
- La suite de regresión valida la Release 1.0 y la nueva funcionalidad.

---

# 9. Product Backlog oficial

## Escala de estimación

Story Points Fibonacci: 1, 2, 3, 5, 8, 13.

Prioridades:

- P0: indispensable para completar la Release.
- P1: alta, necesaria para autonomía operativa.
- P2: deseable, puede retirarse sin invalidar el objetivo principal.

---

## HU-22 — Acceder al Workspace de Administración

| Campo | Definición |
|---|---|
| Épica | EP-07 |
| Objetivo | Proporcionar una entrada administrativa segura y contextualizada. |
| Actor | ADMIN y usuario con capacidades administrativas delegadas |
| Prioridad | P0 |
| Estimación | 5 SP |
| Dependencias | TS-07 |

### Descripción

Como usuario autorizado del consultorio, quiero acceder a un Workspace de Administración para gestionar las funciones administrativas que me corresponden.

### Criterios de aceptación

1. El acceso requiere autenticación.
2. ADMIN puede ingresar al Workspace.
3. Un usuario no ADMIN puede ingresar únicamente cuando posee al menos una capacidad administrativa delegada.
4. La navegación muestra solo módulos autorizados.
5. Un usuario sin autorización recibe una respuesta y experiencia de acceso denegado consistente.
6. El Workspace opera en el contexto del consultorio autenticado.
7. No se solicita ni permite seleccionar libremente otro consultorio.

### Reglas de negocio

- El rol ADMIN concede acceso administrativo general dentro de su consultorio.
- Las capacidades delegadas habilitan exclusivamente las funciones asignadas.
- El acceso al Workspace no concede por sí mismo permisos sobre todas las operaciones.
- No existe administración global de múltiples consultorios en esta Release.

---

## HU-23 — Mantener la configuración básica del consultorio

| Campo | Definición |
|---|---|
| Épica | EP-07 |
| Objetivo | Permitir que el consultorio mantenga su información operativa básica. |
| Actor | ADMIN |
| Prioridad | P1 |
| Estimación | 5 SP |
| Dependencias | HU-22 |

### Descripción

Como administrador, quiero consultar y actualizar los datos básicos de mi consultorio para mantener vigente la información utilizada por AgenDoc.

### Criterios de aceptación

1. Se muestran únicamente datos del consultorio autenticado.
2. El administrador puede actualizar nombre público, datos de contacto, dirección y descripción básica.
3. Los campos obligatorios se validan.
4. El slug no puede modificarse desde esta historia.
5. Los cambios se reflejan en las experiencias que consumen dicha información.
6. Se registra auditoría básica de la modificación.

### Reglas de negocio

- No se puede cambiar el tenant asociado.
- No se gestionan branding avanzado, dominio personalizado ni constructor visual.
- El slug permanece inmutable en Release 1.1.
- Solo ADMIN puede modificar la configuración general.

---

## HU-24 — Gestionar especialidades médicas

| Campo | Definición |
|---|---|
| Épica | EP-07 |
| Objetivo | Administrar el catálogo de especialidades utilizado por el consultorio. |
| Actor | ADMIN o usuario con capacidad MANAGE_SPECIALTIES |
| Prioridad | P0 |
| Estimación | 5 SP |
| Dependencias | HU-22, TS-09 |

### Descripción

Como usuario autorizado, quiero crear, editar, activar y desactivar especialidades para mantener actualizado el catálogo médico del consultorio.

### Criterios de aceptación

1. Se listan las especialidades del consultorio.
2. Se puede crear una especialidad con nombre único dentro del consultorio.
3. Se puede editar su información.
4. Se puede activar o desactivar.
5. Una especialidad asociada históricamente no se elimina físicamente.
6. Una especialidad inactiva no puede asignarse a nuevos médicos.
7. La información de otros consultorios no es visible.
8. Las acciones críticas quedan auditadas.

### Reglas de negocio

- La unicidad del nombre es contextual al consultorio.
- La desactivación no modifica registros históricos.
- La eliminación física queda fuera del alcance.
- Las especialidades inactivas no se muestran como opción para nuevas asignaciones.

---

## HU-25 — Consultar y buscar usuarios del consultorio

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Proporcionar visibilidad administrativa de las cuentas del consultorio. |
| Actor | ADMIN o usuario con capacidad VIEW_USERS |
| Prioridad | P0 |
| Estimación | 5 SP |
| Dependencias | HU-22, TS-07, TS-08 |

### Descripción

Como usuario autorizado, quiero consultar y buscar usuarios para administrar las cuentas que pertenecen al consultorio.

### Criterios de aceptación

1. Se listan exclusivamente usuarios vinculados al consultorio.
2. La búsqueda permite localizar por nombre, correo y estado.
3. Se muestran roles, estado de cuenta y perfiles asociados.
4. La lista permite distinguir cuentas activas e inactivas.
5. No se muestran credenciales ni información de seguridad sensible.
6. La consulta respeta la capacidad asignada.

### Reglas de negocio

- El correo es el identificador visible.
- El username técnico permanece oculto.
- La respuesta no expone contraseñas, hashes ni tokens.
- Los perfiles se muestran como relaciones de la identidad.

---

## HU-26 — Crear un usuario y asignar roles

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Permitir el alta autónoma de identidades del consultorio. |
| Actor | ADMIN o usuario con capacidad MANAGE_USERS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-25, TS-08, TS-09 |

### Descripción

Como usuario autorizado, quiero crear una cuenta y asignarle uno o más roles para incorporar personal o pacientes sin intervención técnica.

### Criterios de aceptación

1. Se registra una identidad única.
2. Se asigna al menos un rol válido.
3. Se pueden asignar varios roles compatibles a la misma identidad.
4. Si el correo ya corresponde a una identidad del consultorio, se evita crear un duplicado y se ofrece continuar sobre la cuenta existente.
5. La creación no exige generar inmediatamente todos los perfiles de dominio.
6. La cuenta queda asociada al consultorio autenticado.
7. Se aplica un mecanismo seguro de acceso inicial.
8. La operación queda auditada.

### Reglas de negocio

- No se crean nuevos tipos de rol.
- La cuenta pertenece al consultorio en el alcance de Release 1.1.
- Un rol no crea automáticamente privilegios adicionales fuera de su definición.
- La contraseña no puede ser consultada por el administrador.
- La gestión de invitaciones por correo o notificaciones queda fuera del alcance.

---

## HU-27 — Administrar roles y capacidades de un usuario

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Ajustar responsabilidades sin crear roles adicionales. |
| Actor | ADMIN |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-26, TS-09 |

### Descripción

Como administrador, quiero asignar o retirar roles y capacidades para adaptar el acceso de cada usuario a sus responsabilidades.

### Criterios de aceptación

1. Se pueden asignar o retirar roles oficiales.
2. Se pueden asignar o retirar capacidades delegables.
3. Los cambios toman efecto en solicitudes posteriores sin modificar el modelo JWT Stateless.
4. No se permite retirar el último ADMIN activo del consultorio.
5. No se permite asignar capacidades fuera del catálogo aprobado.
6. Las operaciones requieren autorización de dominio.
7. Los cambios quedan auditados.
8. La interfaz explica la diferencia entre rol y capacidad.

### Reglas de negocio

- ADMIN representa administración completa del consultorio.
- Las capacidades no crean nuevos roles.
- Las capacidades solo amplían funciones explícitas dentro del consultorio.
- Un usuario puede conservar varios roles.
- Debe existir al menos un ADMIN activo.

---

## HU-28 — Activar o desactivar una cuenta de usuario

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Controlar el acceso sin eliminar información histórica. |
| Actor | ADMIN o usuario con capacidad MANAGE_USERS |
| Prioridad | P0 |
| Estimación | 5 SP |
| Dependencias | HU-25, TS-09 |

### Descripción

Como usuario autorizado, quiero activar o desactivar una cuenta para controlar su acceso al consultorio.

### Criterios de aceptación

1. Una cuenta activa puede desactivarse.
2. Una cuenta inactiva puede reactivarse.
3. Una cuenta inactiva no puede iniciar sesión ni usar tokens posteriores a la aplicación de la política definida.
4. La desactivación no elimina perfiles ni registros históricos.
5. No se puede desactivar al último ADMIN activo.
6. El usuario no puede desactivar su propia cuenta mediante esta operación.
7. La acción queda auditada.

### Reglas de negocio

- Desactivar no equivale a eliminar.
- La desactivación de la identidad no cancela automáticamente citas.
- El impacto sobre agendas o asignaciones debe resolverse mediante sus propias operaciones.
- La seguridad debe validar el estado de la cuenta en el flujo autenticado.

---

## HU-29 — Restablecer el acceso de un usuario

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Recuperar el acceso de una cuenta sin intervención del equipo técnico. |
| Actor | ADMIN o usuario con capacidad MANAGE_USERS |
| Prioridad | P1 |
| Estimación | 5 SP |
| Dependencias | HU-25, TS-09 |

### Descripción

Como usuario autorizado, quiero iniciar un restablecimiento seguro de acceso para ayudar a un usuario que no puede ingresar.

### Criterios de aceptación

1. Se puede iniciar el restablecimiento para una cuenta del mismo consultorio.
2. El administrador nunca visualiza la contraseña actual.
3. El mecanismo obliga a establecer una credencial válida de forma segura.
4. No se revela si una cuenta pertenece a otro consultorio.
5. La operación invalida el acceso anterior según la política definida.
6. La acción queda auditada.

### Reglas de negocio

- No se envían notificaciones automáticas en esta Release.
- El mecanismo no almacena contraseñas en texto plano.
- La solución debe seguir siendo compatible con JWT Stateless.
- El restablecimiento administrativo no cambia roles ni perfiles.

---

## HU-30 — Cambiar mi contraseña

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Permitir que cada usuario mantenga segura su credencial. |
| Actor | Usuario autenticado |
| Prioridad | P1 |
| Estimación | 3 SP |
| Dependencias | Seguridad existente |

### Descripción

Como usuario autenticado, quiero cambiar mi contraseña para mantener segura mi cuenta.

### Criterios de aceptación

1. Se requiere la contraseña actual.
2. La nueva contraseña cumple la política vigente.
3. La confirmación debe coincidir.
4. El cambio no altera roles, capacidades ni perfiles.
5. Las credenciales anteriores dejan de ser válidas.
6. Los mensajes no exponen información sensible.

### Reglas de negocio

- El usuario solo cambia su propia contraseña.
- La contraseña se almacena mediante el mecanismo seguro vigente.
- La política debe aplicarse en Backend.
- Esta historia no implementa “olvidé mi contraseña” por correo.

---

## HU-31 — Vincular múltiples perfiles a una identidad

| Campo | Definición |
|---|---|
| Épica | EP-08 |
| Objetivo | Soportar personas que desempeñan más de una función. |
| Actor | ADMIN o usuario con capacidad MANAGE_USERS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-26, TS-08 |

### Descripción

Como usuario autorizado, quiero vincular perfiles de médico, paciente o recepción a una identidad existente para evitar cuentas duplicadas cuando una persona cumple varias funciones.

### Criterios de aceptación

1. Se puede consultar qué perfiles están vinculados a una identidad.
2. Se puede vincular un perfil compatible con sus roles.
3. Se evita crear un perfil duplicado de la misma clase dentro del consultorio.
4. Un médico puede mantener también un perfil de paciente.
5. Una recepcionista puede mantener también un perfil de paciente.
6. Retirar un rol no elimina automáticamente el perfil histórico.
7. La vinculación se limita al consultorio autenticado.
8. La operación queda auditada.

### Reglas de negocio

- User, Role y perfiles de dominio son conceptos independientes.
- La existencia de un perfil no sustituye la autorización.
- No se elimina físicamente un perfil con historia.
- La identidad global entre distintos consultorios queda fuera del alcance.

---

## HU-32 — Gestionar médicos del consultorio

| Campo | Definición |
|---|---|
| Épica | EP-09 |
| Objetivo | Incorporar y mantener médicos sin intervención técnica. |
| Actor | ADMIN o usuario con capacidad MANAGE_DOCTORS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-24, HU-31 |

### Descripción

Como usuario autorizado, quiero crear, consultar, actualizar, activar y desactivar médicos para mantener el equipo profesional del consultorio.

### Criterios de aceptación

1. Se listan médicos del consultorio.
2. Se puede crear un perfil médico vinculado a una identidad nueva o existente.
3. Se asigna una especialidad activa.
4. Se pueden actualizar datos administrativos y profesionales básicos.
5. Se puede activar o desactivar el perfil médico.
6. Un médico inactivo no recibe nueva disponibilidad ni nuevas citas.
7. La desactivación no elimina citas ni atenciones históricas.
8. No se accede a médicos de otros consultorios.
9. Las acciones críticas quedan auditadas.

### Reglas de negocio

- La identidad y el perfil médico permanecen separados.
- La desactivación del perfil no desactiva automáticamente otros roles de la identidad.
- Solo se asignan especialidades activas.
- El tratamiento de citas futuras existentes debe ser explícito y no destructivo.

---

## HU-33 — Gestionar pacientes del consultorio

| Campo | Definición |
|---|---|
| Épica | EP-09 |
| Objetivo | Mantener datos administrativos de pacientes y corregir información operativa. |
| Actor | ADMIN o usuario con capacidad MANAGE_PATIENTS |
| Prioridad | P1 |
| Estimación | 8 SP |
| Dependencias | HU-31 |

### Descripción

Como usuario autorizado, quiero consultar, crear y actualizar pacientes para mantener correcta la información administrativa utilizada en la atención.

### Criterios de aceptación

1. Se buscan pacientes dentro del consultorio.
2. Se puede crear un paciente vinculado a una identidad nueva o existente.
3. Se pueden actualizar datos administrativos permitidos.
4. Se detectan posibles duplicados por correo y otros identificadores vigentes.
5. Los registros con citas no se eliminan físicamente.
6. La información clínica avanzada no forma parte del formulario.
7. Las modificaciones relevantes quedan auditadas.

### Reglas de negocio

- Un paciente pertenece al contexto del consultorio en esta Release.
- La cuenta y el perfil de paciente pueden existir en estados diferentes.
- No se fusionan pacientes automáticamente.
- Historia clínica, diagnósticos y documentos están fuera del alcance.

---

## HU-34 — Mantener mi perfil de paciente

| Campo | Definición |
|---|---|
| Épica | EP-09 |
| Objetivo | Permitir que el paciente mantenga sus datos administrativos. |
| Actor | PATIENT |
| Prioridad | P1 |
| Estimación | 5 SP |
| Dependencias | Perfil de paciente existente |

### Descripción

Como paciente, quiero consultar y actualizar mis datos administrativos para mantener mi perfil completo y vigente.

### Criterios de aceptación

1. El paciente consulta exclusivamente su propio perfil.
2. Puede actualizar los campos administrativos autorizados.
3. Los campos no editables se muestran de forma clara.
4. Los datos obligatorios se validan.
5. La actualización no modifica citas ni historial.
6. El perfil permanece contextualizado al consultorio.
7. No se incluyen datos clínicos avanzados.

### Reglas de negocio

- El usuario no puede cambiar roles ni estado de cuenta.
- Los identificadores sensibles pueden requerir restricciones adicionales.
- Los cambios se validan en Backend.
- No se permite editar perfiles de terceros.

---

## HU-35 — Consultar auditoría administrativa básica

| Campo | Definición |
|---|---|
| Épica | EP-11 |
| Objetivo | Proporcionar trazabilidad mínima de acciones administrativas críticas. |
| Actor | ADMIN |
| Prioridad | P1 |
| Estimación | 5 SP |
| Dependencias | TS-10 |

### Descripción

Como administrador, quiero consultar un registro básico de acciones administrativas para conocer quién realizó cambios relevantes y cuándo.

### Criterios de aceptación

1. Se consultan eventos del consultorio autenticado.
2. Cada evento muestra fecha, actor, tipo de acción y recurso afectado.
3. Se registran como mínimo cambios de roles, capacidades, estados de cuenta, perfiles, especialidades, configuración y agenda.
4. No se muestran secretos ni valores sensibles.
5. Los registros no pueden modificarse desde la interfaz.
6. Se permite filtrar por fecha, actor y tipo de acción.
7. No se accede a eventos de otros consultorios.

### Reglas de negocio

- La auditoría es funcional y básica.
- No reemplaza observabilidad técnica ni logs de infraestructura.
- No se implementan reportes analíticos.
- La retención avanzada se difiere a una Release posterior.

---

## HU-36 — Definir reglas recurrentes de agenda

| Campo | Definición |
|---|---|
| Épica | EP-10 |
| Objetivo | Configurar patrones habituales de disponibilidad por médico. |
| Actor | ADMIN o usuario con capacidad MANAGE_AGENDAS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-32, TS-11 |

### Descripción

Como usuario autorizado, quiero definir reglas recurrentes de disponibilidad para representar los horarios habituales de cada médico.

### Criterios de aceptación

1. Se selecciona un médico activo del consultorio.
2. Se definen días de semana, hora de inicio, hora de fin y duración de atención.
3. Las reglas inválidas o superpuestas se rechazan.
4. Se puede activar, editar o desactivar una regla.
5. La regla no crea disponibilidad retroactiva.
6. Los cambios no alteran citas ya registradas.
7. Las reglas pertenecen al consultorio autenticado.
8. Las acciones quedan auditadas.

### Reglas de negocio

- Una regla representa un patrón, no una cita.
- Las reglas se aplican únicamente a rangos futuros.
- La duración debe respetar los límites funcionales vigentes.
- Un médico inactivo no puede recibir nuevas reglas activas.

---

## HU-37 — Generar disponibilidad de agenda de forma masiva

| Campo | Definición |
|---|---|
| Épica | EP-10 |
| Objetivo | Materializar disponibilidad futura a partir de reglas recurrentes. |
| Actor | ADMIN o usuario con capacidad MANAGE_AGENDAS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-36, TS-11 |

### Descripción

Como usuario autorizado, quiero generar bloques de agenda para un rango futuro para evitar la creación manual de cada horario.

### Criterios de aceptación

1. Se selecciona médico y rango futuro.
2. La generación utiliza reglas recurrentes activas.
3. Se muestra un resumen antes de confirmar.
4. No se crean bloques duplicados.
5. Se respetan excepciones existentes.
6. No se sobrescriben bloques con citas.
7. Se informa cuántos bloques se crearon, omitieron o rechazaron.
8. La operación es atómica según las reglas definidas.
9. La acción queda auditada.

### Reglas de negocio

- Solo se generan fechas futuras.
- La generación pertenece a un único consultorio y médico.
- Los conflictos se resuelven sin eliminar información existente.
- La agenda generada continúa utilizando el modelo de disponibilidad aprobado.

---

## HU-38 — Registrar excepciones de disponibilidad

| Campo | Definición |
|---|---|
| Épica | EP-10 |
| Objetivo | Representar ausencias, bloqueos o disponibilidad extraordinaria. |
| Actor | ADMIN o usuario con capacidad MANAGE_AGENDAS |
| Prioridad | P0 |
| Estimación | 8 SP |
| Dependencias | HU-36, TS-11 |

### Descripción

Como usuario autorizado, quiero registrar excepciones para ajustar la disponibilidad habitual de un médico en fechas específicas.

### Criterios de aceptación

1. Se puede registrar una indisponibilidad total o parcial.
2. Se puede registrar disponibilidad extraordinaria.
3. Las excepciones solo se aplican a fechas futuras.
4. Se detectan conflictos con citas existentes.
5. Una excepción no cancela una cita silenciosamente.
6. Se informa cuando se requiere resolver citas afectadas por separado.
7. La excepción se considera en futuras generaciones masivas.
8. Se puede consultar, editar o desactivar una excepción cuando no afecta historia protegida.
9. La acción queda auditada.

### Reglas de negocio

- Las citas existentes tienen prioridad de integridad.
- Las excepciones no modifican el pasado.
- La cancelación o reprogramación de citas sigue las historias ya existentes.
- Una disponibilidad extraordinaria debe respetar no superposición.

---

## HU-39 — Consultar y ajustar disponibilidad futura

| Campo | Definición |
|---|---|
| Épica | EP-10 |
| Objetivo | Permitir control operativo sobre los bloques futuros generados. |
| Actor | ADMIN o usuario con capacidad MANAGE_AGENDAS |
| Prioridad | P1 |
| Estimación | 5 SP |
| Dependencias | HU-37, HU-38 |

### Descripción

Como usuario autorizado, quiero consultar y ajustar la disponibilidad futura para corregir bloques específicos sin redefinir toda la agenda.

### Criterios de aceptación

1. Se consulta disponibilidad por médico y rango de fechas.
2. Se distinguen bloques libres, ocupados y afectados por excepción.
3. Se puede desactivar o ajustar un bloque libre futuro.
4. Un bloque ocupado no puede eliminarse ni alterarse de forma destructiva.
5. Los cambios validan superposición.
6. Se mantiene Clinic Isolation.
7. La acción queda auditada.

### Reglas de negocio

- Los bloques con citas se protegen.
- La gestión de la cita se realiza mediante sus historias correspondientes.
- Los ajustes puntuales no cambian automáticamente la regla recurrente.
- No se modifican bloques pasados.

---

# 10. Habilitadores Técnicos definitivos

## TS-07 — Incorporar soporte para ADMIN y capacidades administrativas

### Propósito

Habilitar el rol ADMIN y el acceso inicial al Workspace de Administración manteniendo los controles actuales.

### Justificación

Es la base mínima para cualquier funcionalidad administrativa.

### Impacto arquitectónico

- Seguridad y autorización.
- Contexto autenticado.
- Navegación protegida.
- Sin cambio del modelo JWT Stateless.

### Dependencias

- Autenticación JWT existente.
- Role Based Authorization.
- Domain Authorization.
- Clinic Isolation.

---

## TS-08 — Desacoplar identidad, roles y perfiles de dominio

### Propósito

Permitir múltiples roles y perfiles vinculados a una única identidad.

### Justificación

El modelo aprobado requiere que una persona pueda ser, por ejemplo, médico y paciente sin duplicar su cuenta.

### Impacto arquitectónico

- Modelo de dominio User–Role–Profile.
- Persistencia y migraciones.
- Servicios de usuarios, médicos y pacientes.
- Compatibilidad con datos existentes.

### Dependencias

- TS-07.
- Modelo actual de User, Doctor y Patient.
- Estrategia de migración de datos.

---

## TS-09 — Implementar capacidades administrativas delegables

### Propósito

Autorizar funciones específicas sin crear nuevos roles.

### Justificación

Permite que una recepcionista gestione médicos, pacientes o agendas sin adquirir privilegios totales de ADMIN.

### Impacto arquitectónico

- Catálogo cerrado de capacidades.
- Autorización centralizada en Backend.
- Contexto de seguridad.
- Presentación condicional en Frontend.

### Dependencias

- TS-07.
- TS-08.
- Domain Authorization existente.

---

## TS-10 — Incorporar auditoría administrativa básica

### Propósito

Registrar acciones críticas de administración con actor, fecha, acción y recurso.

### Justificación

La autonomía requiere trazabilidad mínima de cambios sensibles.

### Impacto arquitectónico

- Componente transversal de auditoría.
- Persistencia de eventos.
- Integración controlada con módulos administrativos.
- Protección de datos sensibles.

### Dependencias

- TS-07.
- Contexto autenticado.
- Clinic Isolation.

---

## TS-11 — Fortalecer el dominio de Agenda para recurrencia y excepciones

### Propósito

Separar claramente reglas recurrentes, disponibilidad materializada, excepciones y citas.

### Justificación

La generación masiva y las excepciones no deben incrementar de forma descontrolada el módulo Appointment ni romper sus invariantes.

### Impacto arquitectónico

- Módulo Agenda.
- Contratos con Appointment.
- Migraciones.
- Reglas de no superposición y protección de citas.

### Dependencias

- Agenda y Appointment actuales.
- TS-10 para auditoría.
- Reglas de negocio existentes.

---

## TS-12 — Crear componentes reutilizables para administración

### Propósito

Evitar duplicación de tablas, formularios, filtros, estados y mensajes en el Frontend.

### Justificación

La Release incorpora varias experiencias CRUD con patrones comunes.

### Impacto arquitectónico

- Arquitectura Feature Based.
- Componentes compartidos.
- Consistencia con UI Design Guide.
- Sin crear un framework interno sobredimensionado.

### Dependencias

- HU-22 como primer caso real.
- UI Design Guide vigente.

---

## TS-13 — Fortalecer pruebas de autorización y regresión

### Propósito

Validar combinaciones de roles, capacidades, perfiles y aislamiento entre consultorios.

### Justificación

La complejidad de seguridad aumenta significativamente en esta Release.

### Impacto arquitectónico

- Pruebas unitarias, integración y extremo a extremo.
- Datos de prueba multi-consultorio.
- Matriz de autorización.
- Regresión de flujos del MVP.

### Dependencias

- TS-07, TS-08 y TS-09.
- Historias funcionales de cada Sprint.

---

# 11. Priorización consolidada

## P0 — Indispensables

- HU-22 — Acceder al Workspace de Administración.
- HU-24 — Gestionar especialidades médicas.
- HU-25 — Consultar y buscar usuarios.
- HU-26 — Crear usuario y asignar roles.
- HU-27 — Administrar roles y capacidades.
- HU-28 — Activar o desactivar cuenta.
- HU-31 — Vincular múltiples perfiles.
- HU-32 — Gestionar médicos.
- HU-36 — Definir reglas recurrentes.
- HU-37 — Generar disponibilidad masiva.
- HU-38 — Registrar excepciones.
- TS-07 a TS-13.

## P1 — Alta prioridad

- HU-23 — Configuración básica del consultorio.
- HU-29 — Restablecer acceso.
- HU-30 — Cambiar mi contraseña.
- HU-33 — Gestionar pacientes.
- HU-34 — Mantener mi perfil de paciente.
- HU-35 — Consultar auditoría.
- HU-39 — Consultar y ajustar disponibilidad futura.

## P2

No se aprueban historias P2 en el backlog inicial. Cualquier capacidad meramente conveniente deberá competir con Releases posteriores y no ingresar por defecto.

---

# 12. Mapa de dependencias

```text
TS-07
  ├── HU-22
  │    ├── HU-23
  │    └── HU-24
  ├── TS-08
  │    ├── HU-25
  │    ├── HU-26
  │    │    ├── HU-27
  │    │    └── HU-31
  │    │         ├── HU-32
  │    │         └── HU-33
  │    └── TS-09
  └── TS-10
       └── HU-35

HU-32
  └── HU-36
       ├── HU-37
       └── HU-38
            └── HU-39

TS-11 soporta HU-36 a HU-39.
TS-12 evoluciona incrementalmente desde HU-22.
TS-13 acompaña todos los Sprints.
```

---

# 13. Organización preliminar por Sprint

## Sprint 6 — Fundación administrativa y acceso seguro

### Objetivo

Habilitar la experiencia administrativa inicial y establecer el modelo de autorización necesario para continuar la Release.

### Alcance

- TS-07 — Soporte para ADMIN y capacidades administrativas iniciales.
- HU-22 — Acceder al Workspace de Administración.
- TS-09 — Primera versión de capacidades delegables.
- TS-12 — Componentes base del Workspace.
- HU-24 — Gestionar especialidades médicas.
- TS-13 — Matriz inicial de pruebas de autorización.

### Justificación

El Sprint crea un vertical slice administrativo real. Evita iniciar con una refactorización interna sin valor visible y valida desde el comienzo ADMIN, capacidades delegadas, Clinic Isolation y navegación protegida.

### Resultado esperado

Un administrador puede ingresar al Workspace y gestionar especialidades. Un usuario con una capacidad específica puede ejecutar únicamente esa función.

---

## Sprint 7 — Identidad, usuarios y seguridad de cuentas

### Objetivo

Construir la administración autónoma de cuentas y el modelo multirol.

### Alcance

- TS-08 — Desacoplar identidad, roles y perfiles.
- HU-25 — Consultar y buscar usuarios.
- HU-26 — Crear usuario y asignar roles.
- HU-27 — Administrar roles y capacidades.
- HU-28 — Activar o desactivar cuenta.
- HU-30 — Cambiar mi contraseña.
- TS-13 — Regresión de seguridad.

### Justificación

La gestión de usuarios debe estabilizarse antes de administrar médicos y pacientes, porque dichos perfiles dependerán de una identidad y roles consistentes.

### Resultado esperado

El consultorio administra el ciclo esencial de cuentas sin intervención técnica.

---

## Sprint 8 — Perfiles y operación del consultorio

### Objetivo

Permitir la administración de perfiles de dominio y configuración operativa.

### Alcance

- HU-31 — Vincular múltiples perfiles a una identidad.
- HU-32 — Gestionar médicos.
- HU-33 — Gestionar pacientes.
- HU-34 — Mantener mi perfil de paciente.
- HU-23 — Mantener configuración básica del consultorio.
- HU-29 — Restablecer acceso.
- TS-10 — Base de auditoría administrativa.
- TS-13 — Regresión.

### Justificación

Una vez consolidado User–Role–Capability, pueden construirse perfiles sin duplicar identidades. La configuración básica y el restablecimiento completan la autonomía administrativa cotidiana.

### Resultado esperado

El consultorio administra usuarios, médicos, pacientes y su configuración básica.

---

## Sprint 9 — Reglas recurrentes y generación masiva de agenda

### Objetivo

Eliminar la generación manual de disponibilidad habitual.

### Alcance

- TS-11 — Fortalecer dominio de Agenda.
- HU-36 — Definir reglas recurrentes.
- HU-37 — Generar disponibilidad masiva.
- Integración de auditoría.
- TS-13 — Regresión de agenda y citas.

### Justificación

Las reglas y la materialización masiva deben implementarse juntas para validar el ciclo funcional completo sin mezclar todavía las excepciones más complejas.

### Resultado esperado

El consultorio configura horarios habituales y genera disponibilidad futura de manera segura.

---

## Sprint 10 — Excepciones, ajustes y cierre de Release

### Objetivo

Completar el control operativo de agenda y cerrar calidad, auditoría y documentación.

### Alcance

- HU-38 — Registrar excepciones de disponibilidad.
- HU-39 — Consultar y ajustar disponibilidad futura.
- HU-35 — Consultar auditoría administrativa básica.
- Consolidación de TS-10.
- Consolidación de TS-12.
- Consolidación de TS-13.
- Regresión integral de Release 1.0 y Release 1.1.
- Sincronización documental y cierre.

### Justificación

Las excepciones dependen de reglas y bloques ya estabilizados. La consulta de auditoría se completa al final para incluir todas las acciones críticas de la Release.

### Resultado esperado

El consultorio puede administrar su operación cotidiana y su agenda sin apoyo del equipo de desarrollo.

---

# 14. Validación de alcance por Release

## Se mantienen en Release 1.1

- Workspace administrativo.
- Usuarios, roles, capacidades y estados.
- Perfiles de médicos y pacientes.
- Configuración básica.
- Especialidades.
- Contraseñas y recuperación administrativa.
- Agenda recurrente, generación masiva y excepciones.
- Auditoría básica.
- Calidad y refactorización estrictamente necesaria.

## Se mueven o permanecen en Release 1.2 — Operational Excellence

- Notificaciones automáticas.
- Recordatorios.
- Dashboards operativos.
- Métricas y analítica.
- Automatización avanzada.
- Gestión de colas o productividad.
- Reportes operativos.
- Flujos masivos distintos de agenda.
- Observabilidad funcional avanzada.

## Se mantienen en Release 2.0 — Clinical Platform

- Historia clínica estructurada.
- Diagnósticos.
- Recetas.
- Adjuntos clínicos.
- Evolución médica avanzada.
- Interoperabilidad clínica.

## Se reservan para SaaS Platform

- Provisionamiento de consultorios.
- Administración global de tenants.
- Planes, suscripciones y facturación.
- Dominios personalizados.
- Marketplace.
- Identidad transversal global.
- Operador global de plataforma.
- Autoservicio de alta y baja del tenant.

---

# 15. Validación del criterio rector

Todas las historias aprobadas responden afirmativamente al criterio de autonomía:

| Historia | Contribución directa a autonomía |
|---|---|
| HU-22 | Habilita el punto de operación administrativa. |
| HU-23 | Evita soporte técnico para actualizar el consultorio. |
| HU-24 | Evita soporte técnico para mantener especialidades. |
| HU-25 a HU-31 | Permiten administrar identidades, acceso y perfiles. |
| HU-32 | Permite incorporar y mantener médicos. |
| HU-33 y HU-34 | Permiten mantener información de pacientes. |
| HU-35 | Proporciona control básico sobre cambios administrativos. |
| HU-36 a HU-39 | Permiten administrar disponibilidad y excepciones. |

No se incorpora ninguna historia cuya finalidad principal sea analítica, clínica avanzada, comercial o de operación global SaaS.

---

# 16. Primera Historia de Usuario recomendada

## HU-22 — Acceder al Workspace de Administración

La implementación del Sprint 6 deberá comenzar con:

1. TS-07 — Incorporar soporte para ADMIN y capacidades administrativas iniciales.
2. HU-22 — Acceder al Workspace de Administración.
3. HU-24 — Gestionar especialidades médicas como primer vertical slice administrativo completo.

### Razón

HU-22 es el punto de entrada funcional de toda la Release, pero requiere una base mínima de seguridad. La combinación TS-07 + HU-22 + HU-24 permite validar temprano:

- rol ADMIN;
- capacidades delegadas;
- navegación autorizada;
- Clinic Isolation;
- componentes administrativos;
- auditoría inicial;
- una operación real de negocio de extremo a extremo.

No se recomienda iniciar Sprint 6 con TS-08, porque sería una modificación estructural amplia sin valor funcional visible inmediato.

---

# 17. Gobierno del backlog

- Ninguna historia ingresará a un Sprint sin refinamiento final de alcance y criterios.
- La estimación es preliminar y podrá ajustarse durante Sprint Planning.
- Una historia de 13 SP deberá dividirse antes de ingresar a un Sprint.
- Los habilitadores deberán estar vinculados a historias concretas.
- Las refactorizaciones no podrán convertirse en objetivos abiertos.
- Todo cambio de alcance deberá indicar qué historia se retira o qué Release posterior lo recibirá.
- Toda implementación deberá generar o actualizar su documento en `docs/project/history`.
- El Project Summary deberá sincronizarse al iniciar y cerrar cada Sprint.
- La Release permanecerá abierta hasta completar su Definition of Done.

---

# 18. Estado final del refinamiento

## Épicas

- EP-07 — Administración del Consultorio.
- EP-08 — Gestión de Usuarios, Perfiles y Seguridad.
- EP-09 — Gestión Operativa del Consultorio.
- EP-10 — Gestión Avanzada de Agenda.
- EP-11 — Calidad y Fortalecimiento de Plataforma.

## Historias de Usuario

- 18 historias oficiales, HU-22 a HU-39.

## Habilitadores Técnicos

- 7 habilitadores oficiales, TS-07 a TS-13.

## Sprints

- Sprint 6: Fundación administrativa.
- Sprint 7: Usuarios y seguridad.
- Sprint 8: Perfiles y operación.
- Sprint 9: Agenda recurrente y masiva.
- Sprint 10: Excepciones, auditoría y cierre.

## Próximo paso

Iniciar una nueva sesión dedicada exclusivamente al Sprint 6, comenzando por el refinamiento de implementación de TS-07 y HU-22, sin reabrir la estrategia de la Release.

# AgenDoc Project Blueprint

> Documento vivo del proyecto. Toda decisión funcional o técnica aprobada deberá quedar registrada en este documento antes de considerarse oficial.

| Campo                | Valor                                             |
|----------------------|---------------------------------------------------|
| Proyecto             | AgenDoc                                           |
| Tipo                 | Plataforma Web + Mobile                           |
| Metodología          | Scrum                                             |
| Blueprint Version    | v1.10                                             |
| Sprint Actual        | Sprint 4                                          |
| Estado               | Sprint 4 en ejecución                             |
| Última actualización | Sprint 4 — Cierre de TS-03                        |

---

# Convenciones del Blueprint

- Este documento es la única fuente oficial de información del proyecto.
- Toda decisión aprobada deberá registrarse en este documento.
- Las secciones pendientes se completarán únicamente cuando sean aprobadas durante una sesión.
- Ninguna decisión será eliminada. Si cambia, se registrará mediante un ADR o en el historial de sesiones.
- Cada sesión actualizará este documento.
- El Blueprint evolucionará junto con el producto durante todo el proyecto.
- La documentación será ejecutiva, orientada al desarrollo y evitará información redundante.

---

# Tabla de Contenido

1. Product Vision
2. MVP Scope
3. Modelo de Dominio
4. Modelo de Datos
5. Arquitectura del Producto
6. UX/UI
7. Product Backlog
8. Architecture Decision Records (ADR)
9. Roadmap
10. Historial de Sesiones

---

# Glosario

Esta sección contendrá la definición de términos funcionales y técnicos utilizados en el proyecto (por ejemplo: Consultorio, Agenda Médica, Bloque de Agenda, Cita Médica, Design Token, MVP, Sprint, ADR, etc.).

El objetivo es mantener una terminología consistente en toda la documentación y facilitar la incorporación de nuevos miembros al equipo.

## Convención de Identificadores

Con el fin de mantener una nomenclatura uniforme durante todo el proyecto, se adoptan los siguientes prefijos:

| Prefijo | Significado                  |
|---------|------------------------------|
| EP      | Épica                        |
| FE      | Feature                      |
| HU      | Historia de Usuario          |
| TS      | Technical Story              |
| ADR     | Architecture Decision Record |

Estos identificadores deberán mantenerse consistentes en toda la documentación y durante la ejecución de los Sprints.

Las Technical Stories representan trabajo técnico habilitador necesario para soportar la seguridad, configuración, mantenibilidad, arquitectura o evolución del producto. No sustituyen Historias de Usuario ni representan funcionalidad directa para el usuario final.

---

# 1. Product Vision

## Visión

AgenDoc es una plataforma Web y Mobile para la gestión de consultorios médicos, diseñada para facilitar la administración de citas, pacientes y agendas médicas.

El producto nace con una arquitectura escalable que permitirá operar desde un médico independiente hasta consultorios con múltiples médicos.

El objetivo es construir un producto simple, sólido y preparado para evolucionar de manera incremental.

---

## Principios del Producto

Los siguientes principios guiarán todas las decisiones funcionales, técnicas y de experiencia de usuario durante el desarrollo de AgenDoc.

### 1. Simplicidad primero

Cada funcionalidad debe ser intuitiva y fácil de utilizar. Si una característica requiere una explicación extensa para el usuario, probablemente deba simplificarse.

---

### 2. Construir solo lo necesario

El MVP debe resolver el problema principal del consultorio sin incorporar funcionalidades que no aporten valor inmediato.

Toda nueva funcionalidad deberá justificar claramente el beneficio que aporta al usuario.

---

### 3. Escalabilidad desde el diseño

Aunque el MVP será pequeño, todas las decisiones de arquitectura y modelo de datos deberán permitir el crecimiento futuro del producto sin rediseños importantes.

---

### 4. El Consultorio es el centro del dominio

Todas las funcionalidades deberán diseñarse considerando que pertenecen a un consultorio.

El consultorio será la unidad organizacional principal del sistema.

---

### 5. La cita médica es el núcleo del negocio

El principal objetivo de AgenDoc es gestionar eficientemente las citas médicas.

Las nuevas funcionalidades deberán fortalecer este proceso o integrarse naturalmente con él.

---

### 6. Una única fuente de verdad

Cada dato tendrá un único propietario dentro del dominio.

Se evitará duplicar información o generar inconsistencias entre módulos.

---

### 7. Consistencia en la experiencia de usuario

Las pantallas deberán mantener una estructura, navegación y comportamiento uniforme.

El usuario no debe aprender nuevamente cómo utilizar cada módulo.

---

### 8. Mobile y Web como un solo producto

La aplicación web y la aplicación móvil compartirán las mismas reglas de negocio y ofrecerán una experiencia coherente, adaptada a las capacidades de cada plataforma.

---

### 9. Seguridad desde el inicio

La autenticación, autorización y protección de los datos médicos se considerarán requisitos fundamentales desde las primeras iteraciones del proyecto.

---

### 10. Evolución incremental

AgenDoc crecerá mediante incrementos pequeños y funcionales.

Cada Sprint deberá entregar valor real y mantener el producto potencialmente desplegable.

---

## Objetivo del MVP

Permitir que un consultorio gestione completamente su agenda médica mediante una plataforma moderna, sencilla y eficiente.

El MVP se centrará en la gestión de citas médicas.

---

# 2. MVP Scope

## Roles

### Paciente

- Registro
- Inicio de sesión
- Consultar médicos
- Consultar disponibilidad
- Reservar cita
- Visualizar citas
- Cancelar cita

---

### Recepcionista

- Registrar pacientes
- Buscar pacientes
- Crear citas
- Reprogramar citas
- Cancelar citas
- Registrar asistencia o inasistencia del paciente
- Visualizar agenda del consultorio

---

### Médico

- Consultar agenda
- Consultar datos básicos del paciente
- Registrar observaciones
- Marcar cita como atendida
- Consultar historial básico

---

## Incluido

- Gestión de Consultorios
- Gestión de Usuarios
- Gestión de Roles
- Gestión de Médicos
- Gestión de Pacientes
- Agenda Médica
- Gestión de Citas

---

## Fuera del MVP

- Historia Clínica
- Recetas
- Pagos
- Facturación
- Laboratorio
- Seguros Médicos
- Videoconsultas
- Notificaciones avanzadas

---

# 3. Modelo de Dominio

## Objetivo

Definir el modelo conceptual del negocio de AgenDoc, identificando las entidades principales, sus responsabilidades, relaciones, reglas de negocio, agregados, invariantes y eventos relevantes del dominio.

El modelo de dominio se define desde el punto de vista del negocio, sin considerar todavía diseño de base de datos, APIs o pantallas.

---

## Principios del Modelo de Dominio

El modelo de dominio de AgenDoc se construye siguiendo los siguientes principios:

- El Consultorio es el contexto organizacional principal.
- La Cita Médica es el núcleo funcional del negocio.
- Cada entidad posee una responsabilidad claramente definida.
- Las reglas de negocio pertenecen al dominio y no a la interfaz de usuario.
- El modelo debe soportar la evolución futura del producto sin romper el MVP.

---

## Entidades principales

### Consultorio

Representa la unidad organizacional principal de AgenDoc.

Responsabilidades:

- Agrupar médicos, recepcionistas, pacientes y citas.
- Definir el contexto donde ocurre la atención médica.
- Ser el centro operativo del producto.

Relaciones:

- Tiene médicos.
- Tiene recepcionistas.
- Tiene pacientes.
- Tiene agendas médicas.
- Tiene citas médicas.

---

### Usuario

Representa una persona que accede al sistema.

Responsabilidades:

- Autenticarse.
- Tener un rol dentro del sistema.
- Acceder únicamente a funcionalidades permitidas.

Relaciones:

- Tiene un rol.
- Puede asociarse a uno de los roles de negocio definidos para el sistema.

---

### Rol

Representa el perfil funcional del usuario.

Responsabilidades:

- Definir permisos de negocio.
- Limitar acciones según el tipo de usuario.

Roles del MVP:

- Paciente
- Recepcionista
- Médico

---

### Paciente

Representa a la persona que recibe atención médica.

Responsabilidades:

- Consultar disponibilidad.
- Reservar citas.
- Visualizar sus citas.
- Cancelar citas.
- Mantener datos básicos de identificación y contacto.

Relaciones:

- Pertenece al contexto de un consultorio.
- Tiene citas médicas.
- Puede ser registrado por sí mismo o por una recepcionista.

---

### Médico

Representa al profesional que atiende pacientes.

Responsabilidades:

- Tener agenda médica.
- Atender citas.
- Consultar datos básicos del paciente.
- Registrar observaciones básicas.
- Marcar citas como atendidas.

Relaciones:

- Pertenece a un consultorio.
- Tiene una agenda médica.
- Atiende citas médicas.

---

### Recepcionista

Representa al usuario operativo del consultorio.

Responsabilidades:

- Registrar pacientes.
- Buscar pacientes.
- Crear citas.
- Reprogramar citas.
- Cancelar citas.
- Confirmar la llegada del paciente.
- Registrar la inasistencia del paciente.
- Visualizar la agenda del consultorio.

Relaciones:

- Pertenece a un consultorio.
- Gestiona pacientes y citas del consultorio.

---

### Agenda Médica

Representa la disponibilidad del médico para atender citas.
La Agenda Médica representa la configuración general de disponibilidad del médico y agrupa los bloques de atención definidos para la gestión de citas.

Responsabilidades:

- Organizar bloques de atención.
- Permitir validar disponibilidad.
- Evitar conflictos de horario.

Relaciones:

- Pertenece a un médico.
- Está dentro del contexto de un consultorio.
- Se relaciona con citas médicas.

---

### Cita Médica

Representa el evento central del negocio.

Responsabilidades:

- Registrar la intención de atención médica.
- Asociar paciente, médico, fecha, hora y estado.
- Permitir cambios controlados de estado.
- Registrar observaciones básicas al finalizar la atención.

Relaciones:

- Pertenece a un consultorio.
- Tiene un paciente.
- Tiene un médico.
- Ocurre dentro de una agenda médica.

Estados oficiales del MVP:

- Programada
- Confirmada
- Atendida
- Cancelada
- No asistió

Interpretación de los estados:

- Programada: la cita fue creada con paciente, médico, fecha y horario asignados.
- Confirmada: la recepcionista registró que el paciente llegó al consultorio.
- Atendida: el médico completó la atención y registró la observación médica básica.
- Cancelada: la cita no se realizará y el motivo de cancelación quedó registrado.
- No asistió: la recepcionista registró explícitamente que el paciente no se presentó.
- Para las reglas de disponibilidad, se consideran citas activas aquellas que se encuentran en estado Programada o Confirmada.

En el MVP:

- Solicitada no se utilizará porque las reservas no requieren aprobación posterior.
- Reprogramada representa una acción sobre una cita Programada, no un estado permanente.
- En atención no se utilizará para evitar una transición operativa que no aporta valor suficiente al MVP.

---

### Observación Médica Básica

Representa una nota simple registrada por el médico durante o después de la atención.

Responsabilidades:

- Registrar información básica de la atención.
- Formar parte del historial básico del paciente.

Relaciones:

- Pertenece a una cita médica.
- Es registrada por un médico.

La Observación Médica Básica no representa una historia clínica completa.

---

## Ciclo de Vida de la Cita Médica

El ciclo de vida oficial de una cita médica dentro del MVP será:

```text
Creación o reserva
        │
        ▼
   Programada
        │
        ├───────────────► Cancelada
        │
        ├───────────────► No asistió
        │
        ▼
   Confirmada
        │
        ├───────────────► Cancelada
        │
        ▼
    Atendida
```

Transiciones permitidas:

| Estado actual | Acción                    | Nuevo estado | Actor autorizado         |
|---------------|---------------------------|--------------|--------------------------|
| Sin cita      | Crear o reservar cita     | Programada   | Paciente o recepcionista |
| Programada    | Reprogramar               | Programada   | Recepcionista            |
| Programada    | Cancelar                  | Cancelada    | Paciente o recepcionista |
| Programada    | Confirmar llegada         | Confirmada   | Recepcionista            |
| Programada    | Registrar inasistencia    | No asistió   | Recepcionista            |
| Confirmada    | Cancelar excepcionalmente | Cancelada    | Recepcionista            |
| Confirmada    | Marcar como atendida      | Atendida     | Médico                   |

Estados finales:

- Atendida.
- Cancelada.
- No asistió.

Una cita ubicada en un estado final no podrá cambiar posteriormente de estado dentro del MVP.

---

## Modelo conceptual inicial

```text
Consultorio
├── Usuario
│   └── Rol
├── Paciente
├── Médico
│   ├── Especialidad Médica
│   └── Agenda Médica
│       └── Bloque de Agenda
├── Recepcionista
└── Cita Médica
    ├── Estado de Cita
    └── Observación Médica Básica
```

---

## Relaciones principales

- Un Consultorio agrupa Médicos, Recepcionistas, Pacientes y Citas Médicas.
- Un Médico pertenece a un Consultorio.
- Un Médico tiene una Agenda Médica.
- Un Paciente puede tener múltiples Citas Médicas.
- Una Cita Médica pertenece a un Consultorio.
- Una Cita Médica relaciona un Paciente con un Médico.
- Una Cita Médica ocurre dentro de una Agenda Médica.
- Un Usuario tiene un Rol.
- Un Usuario puede asociarse a uno de los roles de negocio del sistema.
- Una Observación Médica Básica pertenece a una Cita Médica.

---

## Reglas de negocio

### Reglas generales

- Toda cita médica debe pertenecer a un consultorio.
- Toda cita médica debe tener un paciente asignado.
- Toda cita médica debe tener un médico asignado.
- Toda cita médica debe tener una fecha, hora de inicio y hora de fin definidas.
- Toda cita médica debe asociarse a un bloque de agenda válido.
- Los usuarios únicamente podrán ejecutar acciones permitidas por su rol y consultorio.

### Disponibilidad médica

- La disponibilidad médica es información derivada de los bloques de agenda y de las citas activas.
- No se persistirá una entidad independiente denominada Disponibilidad.
- Solo podrán mostrarse bloques activos, disponibles y pertenecientes al consultorio del usuario autenticado.
- No deberán mostrarse horarios anteriores a la hora actual cuando se consulte la fecha del día.
- Los horarios disponibles deberán mostrarse ordenados cronológicamente.
- Un bloque de agenda podrá asociarse como máximo a una cita activa.
- Un horario ocupado no deberá aparecer como disponible.
- Cuando una cita se cancela, el bloque podrá volver a considerarse disponible siempre que su fecha y hora no hayan transcurrido y no exista otra cita activa asociada.

### Creación de citas

- Toda cita deberá crearse inicialmente en estado Programada.
- Una cita solo podrá crearse sobre un bloque de agenda disponible.
- La creación de una cita deberá impedir dobles reservas, incluso ante solicitudes concurrentes.
- La cita creada por el paciente utilizará al paciente autenticado.
- La cita creada por recepción utilizará al paciente seleccionado y al consultorio de la recepcionista autenticada.
- El consultorio no deberá confiar en identificadores enviados libremente por el Frontend cuando pueda obtenerlos del contexto autenticado.

### Reprogramación

- Solo una cita Programada podrá reprogramarse.
- La reprogramación deberá utilizar un nuevo bloque disponible.
- La cita conservará el mismo identificador después de ser reprogramada.
- Después de reprogramarse, la cita permanecerá en estado Programada.
- La fecha, hora y bloque anteriores deberán conservar trazabilidad básica mediante auditoría.

### Cancelación

- Una cita Programada podrá cancelarse por el paciente propietario o por la recepcionista del consultorio.
- Una cita Confirmada podrá cancelarse excepcionalmente por la recepcionista.
- La cancelación de una cita Confirmada requerirá un motivo obligatorio.
- Una cita Atendida no podrá cancelarse.
- Una cita Cancelada no podrá ser atendida ni reprogramada.

### Asistencia e inasistencia

- Solo la recepcionista podrá confirmar la llegada del paciente.
- La confirmación de llegada cambiará la cita de Programada a Confirmada.
- Solo la recepcionista podrá marcar una cita como No asistió.
- La inasistencia deberá registrarse mediante una acción explícita.
- Una cita solo podrá marcarse como No asistió cuando su hora de inicio haya comenzado o transcurrido.
- El registro de inasistencia deberá conservar la fecha, hora, usuario responsable y comentario cuando exista.
- La política de tolerancia por tardanza será gestionada operativamente por el consultorio y no será configurable durante el MVP.

### Atención médica

- El médico solo podrá visualizar las citas asignadas a él.
- Solo el médico asignado podrá registrar la observación médica básica.
- Una observación médica solo podrá registrarse sobre una cita Confirmada.
- En el MVP solo existirá una observación médica básica por cita.
- La observación podrá editarse mientras la cita permanezca Confirmada.
- Después de marcar la cita como Atendida, la observación quedará bloqueada.
- Una cita solo podrá pasar de Confirmada a Atendida.
- Una cita deberá tener una observación médica básica antes de marcarse como Atendida.
- El historial básico mostrará únicamente observaciones correspondientes a citas Atendidas.

---

## Agregados del dominio

### Consultorio

Es el agregado organizacional principal del dominio.

Agrupa toda la operación del negocio.

### Cita Médica

Es el agregado transaccional principal.

Centraliza la interacción entre paciente, médico, agenda y observaciones.

---

## Invariantes del dominio

- Toda cita pertenece a un consultorio.
- Toda cita tiene un médico asignado.
- Toda cita tiene un paciente asignado.
- No pueden existir dos citas activas para un mismo médico en el mismo horario.
- Una observación médica siempre pertenece a una cita.
- Los usuarios únicamente pueden ejecutar acciones permitidas por su rol.

---

## Eventos relevantes del dominio

- PacienteRegistrado
- CitaProgramada
- CitaReprogramada
- CitaCancelada
- LlegadaPacienteConfirmada
- PacienteNoAsistio
- ObservacionMedicaRegistrada
- CitaAtendida

---

# 4. Modelo de Datos

## 4.1 Objetivo

Definir el modelo de datos conceptual que soportará el MVP de AgenDoc, manteniendo alineación con la Product Vision, el alcance del MVP y el Modelo de Dominio aprobado.

El modelo se enfoca en persistir la información necesaria para gestionar consultorios, usuarios, roles, médicos, pacientes, recepcionistas, agendas médicas, citas médicas y observaciones médicas básicas.

---

## 4.2 Principios del Modelo de Datos

- El Consultorio será la unidad organizacional principal del modelo.
- La Cita Médica será la entidad transaccional principal.
- El modelo evitará redundancia de datos.
- Las entidades de negocio estarán separadas de las entidades de acceso.
- Los catálogos se utilizarán para valores controlados.
- El modelo deberá permitir evolución futura sin ampliar el alcance del MVP.
- No se incluirán entidades fuera del MVP como Historia Clínica, Recetas, Pagos, Facturación, Laboratorio, Seguros Médicos o Videoconsultas.

---

## 4.3 Modelo Conceptual (ERD)

```text
Consultorio
├── Usuario
│   └── Rol
├── Paciente
├── Médico
│   ├── Especialidad Médica
│   └── Agenda Médica
│       └── Bloque de Agenda
├── Recepcionista
└── Cita Médica
    ├── Paciente
    ├── Médico
    ├── Bloque de Agenda
    ├── Estado de Cita
    └── Observación Médica Básica
```

---

## 4.4 Entidades Persistentes

### Consultorio

Representa la unidad organizacional principal del sistema.

Atributos principales:
- id
- nombre
- telefono
- correo
- direccion
- estado_registro

Relaciones:
- Tiene médicos.
- Tiene pacientes.
- Tiene recepcionistas.
- Tiene usuarios.
- Tiene agendas médicas.
- Tiene citas médicas.

### Usuario

Representa la cuenta de acceso al sistema.

Atributos principales:
- id
- consultorio_id
- rol_id
- username
- email
- password_hash
- activo
- ultimo_acceso
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Tiene un rol.
- Puede asociarse a un perfil de negocio del sistema.

### Rol

Catálogo de perfiles funcionales del sistema.

Valores iniciales:
- Paciente
- Recepcionista
- Médico

Atributos principales:
- id
- nombre
- descripcion
- estado_registro

Relaciones:
- Tiene usuarios asociados.

### Paciente

Representa a la persona que recibe atención médica.

Atributos principales:
- id
- consultorio_id
- usuario_id
- nombres
- apellidos
- tipo_documento
- numero_documento
- fecha_nacimiento
- telefono
- correo
- direccion
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Puede tener un usuario asociado.
- Tiene citas médicas.

### Médico

Representa al profesional que atiende pacientes.

Atributos principales:
- id
- consultorio_id
- usuario_id
- especialidad_id
- nombres
- apellidos
- tipo_documento
- numero_documento
- numero_colegiatura
- telefono
- correo
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Puede tener un usuario asociado.
- Tiene una especialidad médica.
- Tiene una agenda médica.
- Atiende citas médicas.

### Recepcionista

Representa al usuario operativo del consultorio.

Atributos principales:
- id
- consultorio_id
- usuario_id
- nombres
- apellidos
- tipo_documento
- numero_documento
- telefono
- correo
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Tiene un usuario asociado.

### Especialidad Médica

Catálogo de especialidades de los médicos.

Atributos principales:
- id
- nombre
- descripcion
- estado_registro

Relaciones:
- Puede estar asociada a múltiples médicos.

### Agenda Médica

Representa la agenda configurada para un médico.

Atributos principales:
- id
- consultorio_id
- medico_id
- nombre
- activa
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Pertenece a un médico.
- Tiene bloques de agenda.

### Bloque de Agenda

Representa un horario específico disponible para atención.

Atributos principales:
- id
- agenda_medica_id
- fecha
- hora_inicio
- hora_fin
- disponible
- estado_registro

Relaciones:
- Pertenece a una agenda médica.
- Puede relacionarse históricamente con múltiples citas, pero solo con una cita activa a la vez.

### Cita Médica

Representa el evento central del negocio.

Atributos principales:
- id
- consultorio_id
- paciente_id
- medico_id
- bloque_agenda_id
- estado_cita_id
- fecha_cita
- hora_inicio
- hora_fin
- motivo
- fecha_cancelacion
- motivo_cancelacion
- fecha_confirmacion
- usuario_confirmacion
- fecha_inasistencia
- usuario_inasistencia
- comentario_inasistencia
- estado_registro

Relaciones:
- Pertenece a un consultorio.
- Tiene un paciente.
- Tiene un médico.
- Puede estar asociada a un bloque de agenda.
- Tiene un estado de cita.
- Puede tener una observación médica básica.

### Estado de Cita

Catálogo de estados de la cita médica.

Valores iniciales:
- Programada
- Confirmada
- Atendida
- Cancelada
- No asistió

Atributos principales:
- id
- nombre
- descripcion
- es_estado_final
- estado_registro

Relaciones:
- Puede estar asociado a múltiples citas médicas.

### Observación Médica Básica

Representa una nota simple registrada por el médico durante o después de la atención.

No representa una historia clínica completa.

Atributos principales:
- id
- cita_medica_id
- medico_id
- observacion
- fecha_registro
- estado_registro

Relaciones:
- Pertenece a una cita médica.
- Es registrada por un médico.

---

## 4.5 Relaciones y Cardinalidades

| Relación                                | Cardinalidad |
| --------------------------------------- | ------------ |
| Consultorio → Usuario                   | 1:N          |
| Consultorio → Paciente                  | 1:N          |
| Consultorio → Médico                    | 1:N          |
| Consultorio → Recepcionista             | 1:N          |
| Consultorio → Agenda Médica             | 1:N          |
| Consultorio → Cita Médica               | 1:N          |
| Usuario → Rol                           | N:1          |
| Usuario → Paciente                      | 1:0..1       |
| Usuario → Médico                        | 1:0..1       |
| Usuario → Recepcionista                 | 1:0..1       |
| Especialidad Médica → Médico            | 1:N          |
| Médico → Agenda Médica                  | 1:1          |
| Agenda Médica → Bloque de Agenda        | 1:N          |
| Bloque de Agenda → Cita Médica          | 1:0..N       |
| Paciente → Cita Médica                  | 1:N          |
| Médico → Cita Médica                    | 1:N          |
| Estado de Cita → Cita Médica            | 1:N          |
| Cita Médica → Observación Médica Básica | 1:0..1       |

---

## 4.6 Entidades de Referencia

Rol

Valores iniciales:
- Paciente
- Recepcionista
- Médico

Estado de Cita

Valores iniciales:
- Programada
- Confirmada
- Atendida
- Cancelada
- No asistió

Especialidad Médica

Valores iniciales referenciales:
- Medicina General
- Pediatría
- Cardiología
- Dermatología
- Ginecología
- Traumatología

El catálogo podrá ampliarse sin modificar el modelo de datos.

---

## 4.7 Reglas de Integridad
- Toda cita médica debe pertenecer a un consultorio.
- Toda cita médica debe tener un paciente asignado.
- Toda cita médica debe tener un médico asignado.
- Toda cita médica debe tener fecha, hora de inicio, hora de fin y estado.
- No puede existir más de una cita activa para el mismo médico en el mismo horario.
- Una cita cancelada no puede ser atendida.
- Una cita atendida no puede ser reprogramada.
- Una cita atendida no puede ser cancelada.
- Una observación médica básica siempre debe pertenecer a una cita médica.
- En el MVP solo existirá una observación médica básica por cita.
- Todo usuario debe tener un rol.
- Todo médico, paciente y recepcionista debe pertenecer a un consultorio.
- Todo bloque de agenda debe pertenecer a una agenda médica.
- Los estados de cita deben ser gestionados como catálogo.
- Los catálogos no deben ser eliminados físicamente si tienen información relacionada.
- En el MVP, Paciente, Médico y Recepcionista podrán estar asociados a un Usuario cuando requieran autenticación en la plataforma.
- Toda cita nueva deberá crearse en estado Programada.
- Una cita Programada podrá cambiar a Confirmada, Cancelada o No asistió.
- Una cita Confirmada podrá cambiar a Atendida o, excepcionalmente, a Cancelada.
- Una cita Atendida, Cancelada o No asistió será considerada final.
- Una cita Confirmada solo podrá cancelarse con un motivo obligatorio.
- Una cita solo podrá marcarse como Atendida cuando tenga una observación médica básica.
- El registro de inasistencia deberá identificar fecha, hora y usuario responsable.
- La disponibilidad será derivada de bloques de agenda y citas activas; no será una entidad persistente.
- Para el control de disponibilidad, una cita activa será aquella que se encuentre en estado Programada o Confirmada.

---

## 4.8 Convenciones del Modelo
- Todas las entidades tendrán una clave primaria.
- Las claves foráneas utilizarán el sufijo _id.
- Las entidades principales utilizarán estado_registro.
- No se realizará borrado físico durante el MVP.
- Se utilizará baja lógica mediante estado_registro.
- Todas las entidades principales incluirán campos de auditoría.
- Los catálogos serán entidades independientes.
- El modelo deberá mantenerse normalizado.
- Se evitará almacenar información derivada.
- Las reglas de negocio críticas deberán protegerse desde el modelo y no solo desde la interfaz.
- Las citas médicas deberán poder consultarse eficientemente por consultorio, médico, paciente, fecha y estado.
- La agenda médica deberá poder consultarse eficientemente por médico, fecha y disponibilidad.
- Las entidades de referencia deberán administrarse mediante datos maestros y no mediante valores codificados en la aplicación.

---

## 4.9 Auditoría

Las entidades principales deberán considerar los siguientes campos comunes:
- id
- estado_registro
- fecha_creacion
- usuario_creacion
- fecha_actualizacion
- usuario_actualizacion

La auditoría permitirá trazabilidad básica sobre la creación y modificación de registros dentro del sistema.

---

## 4.10 Escalabilidad del Modelo

El modelo queda preparado para evolucionar hacia futuras funcionalidades sin incorporarlas al MVP:

- Historia Clínica podrá relacionarse posteriormente con Paciente, Médico y Cita Médica.
- Recetas podrán relacionarse posteriormente con Cita Médica y Médico.
- Pagos podrán relacionarse posteriormente con Cita Médica.
- Facturación podrá relacionarse posteriormente con Cita Médica, Paciente y Consultorio.
- Laboratorio podrá relacionarse posteriormente con Cita Médica y Paciente.
- Seguros Médicos podrán relacionarse posteriormente con Paciente y Cita Médica.
- Notificaciones podrán relacionarse posteriormente con Usuario y Cita Médica.

Estas extensiones no forman parte del MVP y deberán evaluarse en iteraciones futuras.


---

# 5. Arquitectura del Producto

## 5.1 Objetivo

Definir la arquitectura base del producto AgenDoc para soportar el desarrollo del MVP, manteniendo alineación con la Product Vision, el Modelo de Dominio y el Modelo de Datos aprobados.

La arquitectura deberá permitir construir una plataforma Web y Mobile para la gestión de consultorios médicos, usuarios, roles, médicos, pacientes, recepcionistas, agendas médicas, citas médicas y observaciones médicas básicas.

---

## 5.2 Estilo Arquitectónico

AgenDoc utilizará una arquitectura de Monolito Modular aplicando principios de Clean Architecture.

Esta decisión permite iniciar el MVP con una estructura simple, mantenible y preparada para crecer, evitando la complejidad operativa de una arquitectura de microservicios en una etapa temprana.

---

## 5.3 Principios Arquitectónicos

- Separación de responsabilidades.
- Bajo acoplamiento.
- Alta cohesión.
- Seguridad desde el diseño.
- Desarrollo incremental.
- Reutilización de reglas de negocio desde el backend.
- Evolución futura sin sobrearquitectura.
- API única para clientes Web y Mobile.
- Persistencia relacional consistente con el modelo de datos aprobado.

---

## 5.4 Arquitectura General

La arquitectura estará compuesta por:

- Frontend Web.
- Aplicación Mobile.
- Backend API.
- Base de Datos Relacional.
- Infraestructura de despliegue.
- Pipeline CI/CD.

El Backend API será el responsable de centralizar las reglas de negocio, seguridad, validaciones y persistencia.

Frontend Web y Mobile consumirán la misma API mediante REST sobre HTTPS.

---

## 5.5 Frontend Web

El Frontend Web se desarrollará con React y TypeScript.

Responsabilidades:

- Presentar la interfaz web del sistema.
- Consumir los servicios expuestos por el Backend API.
- Gestionar navegación, formularios y validaciones de presentación.
- Adaptar la experiencia para roles Paciente, Recepcionista y Médico.
- No contener reglas de negocio críticas.

Organización recomendada:

- components
- pages
- services
- hooks
- routes
- layouts
- shared

---

## 5.6 Aplicación Mobile

La aplicación Mobile se desarrollará con React Native y Expo.

Responsabilidades:

- Presentar la experiencia mobile para los usuarios del sistema.
- Consumir la misma API REST utilizada por el Frontend Web.
- Permitir una experiencia optimizada para uso desde dispositivos móviles.
- Mantener consistencia funcional con la plataforma web.

Estrategia de reutilización:

- Compartir criterios visuales, contratos de API y reglas de presentación.
- Mantener reglas de negocio críticas exclusivamente en el Backend.
- Evitar duplicar lógica sensible entre Web y Mobile.

---

## 5.7 Backend

El Backend se desarrollará con Java Spring Boot.

Responsabilidades:

- Exponer servicios REST.
- Implementar reglas de negocio.
- Gestionar autenticación y autorización.
- Validar invariantes del dominio.
- Orquestar la persistencia de datos.
- Proteger el acceso a información por rol y consultorio.

Organización por capas:

- Controller
- Application Service
- Domain
- Repository
- Infrastructure
- Security
- Config

Organización por módulos:

- Consultorios
- Usuarios y Roles
- Pacientes
- Médicos
- Recepcionistas
- Agenda Médica
- Citas Médicas
- Observaciones Médicas Básicas

---

## 5.7.1 Manejo de Errores

El Backend implementará una estrategia uniforme para el manejo de errores.

Todas las respuestas de error deberán mantener una estructura consistente que facilite su consumo por las aplicaciones Web y Mobile.

La respuesta incluirá como mínimo:

- código de error
- mensaje
- timestamp
- detalle cuando corresponda

No se expondrá información sensible del servidor ni trazas internas al cliente.

---

## 5.7.2 Convenciones de API

Las APIs REST utilizarán versionado desde la primera versión del producto.

Convención inicial:

/api/v1/

Esto permitirá evolucionar la plataforma sin romper la compatibilidad con clientes existentes.

---

## 5.8 Base de Datos

El motor recomendado para el MVP será PostgreSQL.
Las modificaciones del esquema se gestionarán mediante migraciones versionadas utilizando Flyway.

Justificación:

- Es una base de datos relacional robusta.
- Soporta integridad referencial.
- Permite consultas eficientes para agenda y citas.
- Escala adecuadamente para el crecimiento futuro del producto.
- Se alinea con el Modelo de Datos aprobado.

Estrategia de persistencia:

- Uso de entidades persistentes alineadas al modelo aprobado.
- Uso de claves foráneas para proteger relaciones críticas.
- Uso de índices para consultas frecuentes.
- Uso de baja lógica mediante estado_registro.
- Uso de campos de auditoría en entidades principales.

Consultas críticas a optimizar:

- Citas por consultorio.
- Citas por médico.
- Citas por paciente.
- Citas por fecha.
- Citas por estado.
- Bloques de agenda por médico, fecha y disponibilidad.

---

## 5.9 Autenticación y Seguridad

El modelo de autenticación será basado en JWT.

Principios:

- Autenticación mediante usuario y contraseña.
- Contraseñas almacenadas con hash seguro.
- Tokens JWT para sesiones stateless.
- Comunicación mediante HTTPS.
- Autorización basada en roles.
- Restricción de acceso por consultorio.
- Validación de permisos en backend.
- Protección de datos sensibles desde el diseño.

Roles del MVP:

- Paciente.
- Recepcionista.
- Médico.

Reglas de autorización iniciales:

- El paciente solo podrá gestionar sus propias citas.
- La recepcionista podrá gestionar pacientes y citas del consultorio.
- El médico solo podrá visualizar sus citas asignadas y registrar observaciones básicas.
- Solo el médico podrá marcar una cita como atendida.
- Solo el médico podrá registrar observaciones médicas básicas.

---

## 5.10 Infraestructura

Ambientes iniciales:

- Local.
- Desarrollo.
- Producción.

Contenedores:

- Durante el MVP no se utilizará Docker como parte del entorno local de desarrollo.
- El entorno local utilizará PostgreSQL instalado directamente en el equipo del desarrollador.
- La carpeta `docker/` quedará reservada para futuras configuraciones de contenedores.
- El uso de Docker será evaluado cuando el producto requiera ambientes reproducibles, integración compartida o despliegues mediante contenedores.

Despliegue:

- Frontend Web desplegado en plataforma compatible con aplicaciones React.
- Backend desplegado en plataforma compatible con Java Spring Boot.
- Base de Datos PostgreSQL administrada o desplegada como servicio.

CI/CD:

- Uso de GitHub Actions.
- Build automático.
- Ejecución de pruebas.
- Validación antes de despliegue.
- Despliegue automatizado por ambiente cuando corresponda.

Configuración:

- Uso de variables de entorno.
- Separación de configuración por ambiente.
- No almacenar secretos en el repositorio.
- Manejo seguro de credenciales.

---

## 5.10.1 Logging y Observabilidad

El Backend deberá registrar eventos relevantes para facilitar el monitoreo y diagnóstico del sistema.

Como mínimo deberán registrarse:

- autenticaciones
- errores de aplicación
- excepciones no controladas
- operaciones críticas del negocio

Los registros no deberán contener contraseñas, tokens ni información médica sensible.

---

## 5.11 Integraciones Futuras

La arquitectura deberá quedar preparada para soportar en futuras iteraciones:

- Notificaciones.
- Pagos.
- Historia Clínica.
- Facturación.
- Laboratorio.
- APIs externas.
- Seguros Médicos.
- Videoconsultas.

Estas capacidades no forman parte del MVP y no deberán implementarse durante esta etapa.

La arquitectura permitirá incorporarlas posteriormente mediante nuevos módulos, integraciones externas o servicios especializados cuando el producto lo requiera.

---

## 5.12 Diagrama de Arquitectura de Alto Nivel

```text
Usuario Web
   │
   ▼
Frontend Web
   │
   │ REST / HTTPS
   ▼
Backend API
   │
   ├── Módulo Consultorios
   ├── Módulo Usuarios y Roles
   ├── Módulo Pacientes
   ├── Módulo Médicos
   ├── Módulo Recepcionistas
   ├── Módulo Agenda Médica
   ├── Módulo Citas Médicas
   └── Módulo Observaciones Básicas
   │
   ▼
Base de Datos PostgreSQL

Aplicación Mobile
   │
   └── REST / HTTPS ─────► Backend API
```
---

## 5.13 Decisiones Arquitectónicas
- Se utilizará Monolito Modular para el MVP.
- Se aplicarán principios de Clean Architecture.
- Se utilizará React con TypeScript para el Frontend Web.
- Se utilizará React Native con Expo para la aplicación Mobile.
- Se utilizará Java Spring Boot para el Backend.
- Se utilizará PostgreSQL como base de datos.
- Se utilizará API REST como mecanismo de comunicación.
- Se utilizará JWT para autenticación.
- La autorización será basada en roles.
- El backend será la fuente oficial de reglas de negocio.
- Las integraciones futuras no serán implementadas en el MVP, pero la arquitectura quedará preparada para incorporarlas.
- Durante el MVP no se utilizará Docker en el entorno local de desarrollo.
- PostgreSQL se ejecutará localmente durante el MVP.
- Docker quedará reservado para una futura etapa de integración, pruebas o despliegue.

---

## 5.14 Estándares de Desarrollo

Durante el desarrollo del MVP se adoptarán los siguientes estándares:

- Arquitectura basada en paquetes por módulo funcional.
- Convenciones de nomenclatura consistentes.
- Separación entre DTO, Entidades y Objetos de Dominio.
- Uso de inyección de dependencias.
- Validaciones de negocio en el Backend.
- Validaciones de experiencia de usuario en Frontend.
- Cobertura de pruebas para componentes críticos.
- Uso de migraciones versionadas para Base de Datos.

---

# 6. UX/UI

## 6.1 Objetivo

Definir el estándar oficial de experiencia de usuario (UX), interfaz de usuario (UI) y Design System del MVP de AgenDoc.

El objetivo es garantizar una experiencia consistente entre la plataforma Web y la aplicación Mobile, estableciendo principios, componentes reutilizables y convenciones visuales que sirvan como base para todo el desarrollo del Frontend.

El Design System deberá:

- Reducir la curva de aprendizaje del usuario.
- Mantener consistencia entre módulos.
- Facilitar la reutilización de componentes.
- Permitir la evolución futura del producto.
- Compartirse entre React Web y React Native.

---

## 6.2 Principios de UX

La experiencia de usuario de AgenDoc estará guiada por los siguientes principios.

### Simplicidad

Cada tarea deberá completarse utilizando la menor cantidad posible de pasos.

---

### Velocidad

Las operaciones más frecuentes deberán poder realizarse rápidamente.

Especialmente:

- reservar cita
- registrar paciente
- consultar agenda
- registrar asistencia o inasistencia
- registrar observaciones

---

### Consistencia

Todos los módulos utilizarán los mismos patrones visuales y de interacción.

El usuario no deberá aprender nuevamente cómo utilizar una pantalla diferente.

---

### Visibilidad

El estado del sistema deberá comunicarse permanentemente.

Ejemplos:

- estado de la cita
- disponibilidad
- errores
- confirmaciones
- carga

---

### Prevención de errores

La interfaz deberá impedir acciones inválidas antes de enviarlas al Backend.

Ejemplos:

- evitar doble reserva
- impedir guardar formularios incompletos
- impedir cancelar una cita atendida

---

### Mobile First

Todas las funcionalidades deberán diseñarse inicialmente para dispositivos móviles.

La versión Web aprovechará el espacio adicional sin modificar el flujo funcional.

---

### Accesibilidad

El producto buscará cumplir las recomendaciones WCAG AA.

Como mínimo deberá considerar:

- navegación mediante teclado
- foco visible
- contraste adecuado
- etiquetas accesibles
- mensajes comprensibles

---

---

## 6.3 Identidad Visual

La identidad visual de AgenDoc deberá transmitir confianza, simplicidad, profesionalismo y cercanía.

El diseño evitará la complejidad visual de los sistemas hospitalarios tradicionales y priorizará una experiencia moderna, limpia y fácil de utilizar.

### Personalidad visual

El producto deberá percibirse como:

- Profesional.
- Confiable.
- Moderno.
- Simple.
- Ágil.
- Ordenado.

---

### Paleta de colores

#### Color Primario

Azul

Uso:

- Botones principales.
- Enlaces.
- Navegación.
- Elementos destacados.

Color sugerido:

```
#2563EB
```

---

#### Color Secundario

Verde

Uso:

- Confirmaciones.
- Estados positivos.
- Disponibilidad.

Color sugerido:

```
#10B981
```

---

#### Colores Neutros

Escala de grises desde Gris 50 hasta Gris 900.

Uso:

- Fondos.
- Bordes.
- Textos.
- Divisiones.

---

#### Colores de Estado

Éxito

```
#10B981
```

Advertencia

```
#F59E0B
```

Error

```
#EF4444
```

Información

```
#3B82F6
```

---

### Tipografía

Fuente oficial del producto:

**Inter**

Jerarquía recomendada:

- Heading 1
- Heading 2
- Heading 3
- Body
- Caption

Se priorizará una alta legibilidad tanto en dispositivos móviles como en escritorio.

---

### Espaciado

El sistema utilizará una escala basada en múltiplos de 4 px.

Valores oficiales:

- 4
- 8
- 12
- 16
- 24
- 32
- 48
- 64

Todos los componentes deberán construirse utilizando esta escala.

---

### Bordes y radios

Inputs

8 px

Botones

8 px

Cards

12 px

Modales

16 px

---

### Grid Responsive

Desktop

12 columnas

Tablet

8 columnas

Mobile

4 columnas

El contenido deberá adaptarse automáticamente sin modificar el flujo funcional.

---

### Iconografía

Biblioteca oficial:

Lucide Icons

Criterios:

- estilo lineal
- alta legibilidad
- peso visual uniforme
- compatibilidad con React y React Native

---

## 6.4 Tokens de Diseño

Con el fin de compartir el mismo Design System entre React Web y React Native, AgenDoc utilizará un conjunto de Design Tokens.

### Tokens de Color

- Primary
- Secondary
- Success
- Warning
- Danger
- Info
- Neutral

---

### Tokens Tipográficos

- Font Family
- Font Size
- Font Weight
- Line Height

---

### Tokens de Espaciado

- XS
- S
- M
- L
- XL

---

### Tokens de Radios

- Small
- Medium
- Large

---

### Tokens de Elevación

Se definirán tres niveles de elevación:

- Low
- Medium
- High

Estos niveles se utilizarán principalmente en:

- Cards.
- Modales.
- Menús flotantes.
- Dropdowns.

---

---

## 6.5 Design System v1

El Design System de AgenDoc define el conjunto oficial de componentes reutilizables para las aplicaciones Web y Mobile.

Todos los componentes deberán construirse utilizando los Design Tokens definidos en este Blueprint.

El objetivo es mantener consistencia visual, reducir duplicidad de código y facilitar la evolución del producto.

### Botones

Tipos oficiales:

- Primary
- Secondary
- Outline
- Ghost
- Danger

Estados soportados:

- Default
- Hover (Web)
- Focus
- Active
- Disabled
- Loading

Los botones principales deberán utilizar el color primario del producto.

---

### Campos de Entrada (Input)

Todos los campos de entrada deberán compartir la misma estructura visual.

Elementos obligatorios:

- Label
- Placeholder
- Valor
- Helper Text (opcional)
- Mensaje de error
- Icono (opcional)

Estados:

- Default
- Focus
- Error
- Disabled
- Read Only

---

### Select

Los componentes Select deberán reutilizar el mismo estilo visual definido para los Inputs.

Deberán soportar:

- búsqueda (cuando aplique)
- selección única
- estado vacío
- estado deshabilitado

---

### Date Picker y Calendario

El calendario será uno de los componentes principales del MVP.

Deberá permitir:

- selección de fecha
- navegación mensual
- visualización de disponibilidad
- identificación visual de citas existentes

Las citas deberán representarse mediante colores asociados a su estado.

---

### Cards

Las Cards serán el componente principal para mostrar información resumida.

Casos de uso:

- Médico
- Paciente
- Agenda
- Cita
- Indicadores
- Información resumida

---

### Tablas

Las tablas serán utilizadas únicamente en la versión Web.

Deberán soportar:

- ordenamiento
- búsqueda
- paginación
- acciones por fila
- estado vacío

En dispositivos móviles la información equivalente se presentará mediante Cards.

---

### Modales

Los modales deberán utilizarse únicamente para acciones rápidas.

Ejemplos:

- Confirmaciones
- Información
- Edición simple

No deberán utilizarse para procesos largos.

Se permitirá un máximo de dos niveles de modales.

---

### Alertas

Tipos oficiales:

- Success
- Warning
- Error
- Info

Las alertas deberán mostrar información persistente hasta que el usuario las cierre o desaparezca la condición que las originó.

---

### Toasts

Los mensajes temporales deberán utilizar Toasts.

Duración recomendada:

4 segundos.

Ubicación:

- Web: esquina superior derecha.
- Mobile: parte superior de la pantalla.

---

### Badges

Los Badges se utilizarán principalmente para representar estados.

Estados oficiales de cita:

- Programada
- Confirmada
- Atendida
- Cancelada
- No asistió

Cada estado deberá mantener siempre el mismo color.

---

### Chips

Los Chips se utilizarán para:

- filtros rápidos
- etiquetas
- selección de opciones

---

### Avatares

Los Avatares mostrarán inicialmente las iniciales del usuario.

El soporte para fotografía quedará preparado para futuras versiones.

---

### Navbar

La barra superior deberá contener:

- Logo
- Nombre del consultorio
- Usuario autenticado
- Menú de usuario
- Cerrar sesión

---

### Sidebar

La navegación lateral será específica para cada rol.

Características:

- colapsable
- iconografía consistente
- agrupación por módulos
- indicador de opción activa

---

### Breadcrumb

Disponible únicamente para la versión Web.

Permitirá indicar la ubicación actual dentro de la navegación.

---

### Estados de Carga

Las pantallas deberán utilizar Skeleton Loading para cargas de contenido.

Los indicadores de carga tipo Spinner deberán reservarse únicamente para operaciones breves.

---

### Estado Vacío

Cuando un módulo no tenga información disponible deberá mostrarse:

- ilustración o icono
- mensaje descriptivo
- acción recomendada

---

### Estado de Error

Cuando ocurra un error se mostrará:

- mensaje claro
- descripción breve
- acción para reintentar cuando corresponda

Nunca se mostrarán mensajes técnicos provenientes del Backend.

---

### Componentes Reutilizables

Como mínimo el proyecto deberá implementar los siguientes componentes reutilizables:

- AppButton
- AppInput
- AppSelect
- AppDatePicker
- AppCalendar
- AppCard
- AppTable
- AppModal
- AppToast
- AppAlert
- AppBadge
- AppChip
- AppAvatar
- AppNavbar
- AppSidebar
- AppBreadcrumb
- AppLoader
- AppEmptyState
- AppErrorState

---

---

## 6.6 Arquitectura de Navegación

La navegación de AgenDoc deberá mantenerse consistente entre la aplicación Web y Mobile.

Cada rol visualizará únicamente las funcionalidades que le corresponden.

La navegación deberá minimizar la cantidad de pasos necesarios para completar las tareas más frecuentes.

### Navegación del Paciente

Flujo principal:

Inicio

↓

Especialidades

↓

Médicos

↓

Disponibilidad

↓

Reservar cita

↓

Mis citas

↓

Perfil

Las funcionalidades principales del paciente serán:

- consultar médicos
- consultar disponibilidad
- reservar citas
- visualizar citas
- cancelar citas
- administrar su perfil

---

### Navegación de la Recepcionista

Flujo principal:

Dashboard

↓

Agenda del Consultorio

↓

Pacientes

↓

Nueva cita

↓

Reprogramar cita

↓

Registrar asistencia

↓

Perfil

Las funcionalidades principales serán:

- registrar pacientes
- buscar pacientes
- gestionar agenda
- crear citas
- reprogramar citas
- cancelar citas
- confirmar llegada o registrar inasistencia

---

### Navegación del Médico

Flujo principal:

Dashboard

↓

Agenda del día

↓

Detalle de cita

↓

Registrar observación

↓

Historial básico

↓

Perfil

Las funcionalidades principales serán:

- consultar agenda
- visualizar información del paciente
- registrar observaciones
- marcar cita como atendida
- consultar historial básico

---

### Navegación Global

Todas las aplicaciones deberán mantener elementos comunes de navegación.

Elementos permanentes:

- Logo de AgenDoc
- Nombre del consultorio
- Usuario autenticado
- Perfil
- Cerrar sesión

La versión Web utilizará Sidebar y Navbar.

La versión Mobile utilizará Bottom Navigation y navegación jerárquica cuando corresponda.

---

## 6.7 Catálogo Oficial de Pantallas del MVP

### Pantallas comunes

| Pantalla             | Objetivo                       | Función principal                   |
|----------------------|--------------------------------|-------------------------------------|
| Login                | Autenticar usuario             | Inicio de sesión                    |
| Recuperar contraseña | Recuperar acceso               | Restablecimiento de credenciales    |
| Perfil               | Gestionar información personal | Actualización de datos y contraseña |

---

### Rol Paciente

| Pantalla       | Objetivo                       | Función principal          |
|----------------|--------------------------------|----------------------------|
| Inicio         | Punto de entrada del usuario   | Accesos rápidos            |
| Especialidades | Explorar oferta médica         | Selección de especialidad  |
| Médicos        | Consultar médicos disponibles  | Selección de médico        |
| Disponibilidad | Consultar horarios             | Selección de fecha y hora  |
| Reservar cita  | Crear una cita                 | Confirmación de reserva    |
| Mis citas      | Gestionar citas                | Consultar y cancelar citas |

---

### Rol Recepcionista

| Pantalla               | Objetivo          | Función principal              |
|------------------------|-------------------|--------------------------------|
| Dashboard              | Resumen operativo | Indicadores rápidos            |
| Agenda del consultorio | Gestión diaria    | Calendario general             |
| Pacientes              | Administración    | Registro y búsqueda            |
| Nueva cita             | Registrar cita    | Asignación de médico y horario |
| Reprogramar cita       | Modificar cita    | Cambio de fecha u hora         |
| Registrar asistencia   | Gestionar llegada | Confirmar llegada o registrar inasistencia |

---

### Rol Médico

| Pantalla           | Objetivo               | Función principal        |
|--------------------|------------------------|--------------------------|
| Dashboard          | Resumen diario         | Agenda e indicadores     |
| Agenda del día     | Consultar citas        | Lista diaria             |
| Detalle de cita    | Consultar información  | Datos del paciente       |
| Observación médica | Registrar atención     | Nota médica básica       |
| Historial básico   | Consultar antecedentes | Observaciones anteriores |

---

### Pantallas Transversales

El MVP podrá incorporar las siguientes pantallas reutilizables:

- Acceso denegado.
- Error inesperado.
- Página no encontrada.
- Sin conexión.
- Estado vacío.
- Cargando información.

Estas pantallas deberán reutilizar los componentes definidos en el Design System.

---

---

## 6.8 Convenciones de Diseño

Las siguientes convenciones deberán respetarse en toda la plataforma Web y Mobile.

### Formularios

- Los labels deberán permanecer siempre visibles.
- Los campos obligatorios deberán identificarse claramente.
- Los formularios Mobile utilizarán una única columna.
- En Desktop podrán utilizarse dos columnas cuando mejore la lectura.
- El foco deberá avanzar siguiendo un orden lógico.

---

### Validaciones

- Las validaciones deberán ejecutarse tan pronto como sea posible.
- Los mensajes deberán mostrarse junto al campo correspondiente.
- Nunca se utilizarán mensajes técnicos provenientes del Backend.
- Un formulario con errores no podrá enviarse.

---

### Confirmaciones

Se solicitará confirmación únicamente para acciones destructivas o irreversibles.

Ejemplos:

- cancelar cita
- eliminar registro
- cerrar sesión

Las operaciones de consulta no requerirán confirmación.

---

### Mensajes

Los mensajes deberán utilizar un lenguaje claro y orientado al usuario.

No deberán incluir:

- códigos de error
- excepciones
- nombres técnicos

---

### Tablas

Las tablas del MVP deberán soportar:

- búsqueda
- ordenamiento
- paginación
- acciones por fila
- estado vacío

En dispositivos móviles las tablas deberán reemplazarse por Cards.

---

### Calendarios

El calendario será uno de los componentes principales del producto.

Deberá mostrar:

- disponibilidad
- citas registradas
- estado de cada cita

La información deberá representarse mediante colores y badges definidos en el Design System.

---

### Manejo de Errores

Ante un error el sistema deberá indicar:

- qué ocurrió
- cómo puede solucionarlo el usuario
- acción para reintentar cuando corresponda

Nunca deberán mostrarse mensajes internos del servidor.

---

### Accesibilidad

Como mínimo deberá cumplirse:

- contraste WCAG AA
- navegación mediante teclado
- foco visible
- etiquetas accesibles
- tamaño adecuado de controles táctiles

---

### Responsive Design

El mismo producto deberá funcionar correctamente en:

- Mobile
- Tablet
- Desktop

Las diferencias serán únicamente de presentación.

Las reglas de negocio deberán mantenerse idénticas.

---

### Consistencia Visual

Todos los módulos deberán reutilizar exclusivamente los componentes definidos en el Design System.

No deberán crearse variantes visuales innecesarias.

---

### Nomenclatura de Componentes

Se adopta la siguiente convención para componentes reutilizables:

- AppButton
- AppInput
- AppSelect
- AppDatePicker
- AppCalendar
- AppCard
- AppTable
- AppModal
- AppToast
- AppAlert
- AppBadge
- AppChip
- AppAvatar
- AppNavbar
- AppSidebar
- AppBreadcrumb
- AppLoader
- AppEmptyState
- AppErrorState

---

## 6.9 Validación de Consistencia

El estándar de UX/UI aprobado mantiene alineación con todos los artefactos definidos previamente en el proyecto.

| Artefacto                 | Estado      |
|---------------------------|-------------|
| Product Vision            | ✅ Alineado |
| MVP Scope                 | ✅ Alineado |
| Modelo de Dominio         | ✅ Alineado |
| Modelo de Datos           | ✅ Alineado |
| Arquitectura del Producto | ✅ Alineado |

Los ajustes realizados en UX/UI no introducen entidades adicionales ni modifican la arquitectura aprobada. La representación visual queda alineada con el ciclo de vida refinado de la cita médica.

---

# 7. Product Backlog

## 7.1 Objetivo

Definir el Product Backlog oficial del MVP de AgenDoc, manteniendo trazabilidad con la Product Vision, MVP Scope, Modelo de Dominio, Modelo de Datos, Arquitectura del Producto y UX/UI.

El backlog prioriza la gestión de citas médicas como núcleo del negocio y organiza la evolución incremental del MVP a través de los Sprints definidos en el Roadmap.

---

## 7.2 Estrategia de construcción del Backlog

El Product Backlog se construye bajo los siguientes criterios:

- MVP First.
- Vertical Slice.
- Entrega incremental de valor.
- Historias pequeñas.
- Dependencias claras.
- Reutilización del Design System.
- Backend como fuente oficial de reglas de negocio.
- Web y Mobile consumiendo la misma lógica funcional.

El backlog no incluye tareas de implementación detalladas, diseño de APIs, diseño de base de datos ni código fuente. El trabajo técnico habilitador se gestiona mediante Technical Stories.

---

## 7.3 Épicas del MVP

| ID | Épica | Objetivo | Valor de negocio | Alcance |
|----|-------|----------|------------------|---------|
| EP-01 | Acceso, usuarios y roles | Permitir acceso seguro según rol | Habilita el uso controlado de la plataforma | Login, sesión, rol, navegación por perfil |
| EP-02 | Gestión base del consultorio | Configurar la operación mínima del consultorio | Permite operar con médicos, recepcionistas y pacientes | Consultorio, usuarios, médicos y recepcionistas |
| EP-03 | Gestión de pacientes | Registrar y consultar pacientes | Facilita la operación diaria de recepción y atención | Registro, búsqueda, visualización básica |
| EP-04 | Agenda médica y disponibilidad | Gestionar disponibilidad médica | Evita conflictos y permite reservar citas | Agenda, bloques, disponibilidad |
| EP-05 | Gestión de citas médicas | Crear, consultar, reprogramar, cancelar y confirmar citas | Es el núcleo funcional del MVP | Citas para paciente, recepcionista y médico |
| EP-06 | Atención médica básica | Permitir al médico atender una cita | Cierra el ciclo operativo de la cita | Agenda del médico, observación básica, marcar atendida |

---

## 7.4 Features

| ID    | Épica | Feature                               | Descripción                                                   | Dependencias    | Prioridad   |
|-------|-------|---------------------------------------|---------------------------------------------------------------|-----------------|-------------|
| FE-01 | EP-01 | Inicio de sesión                      | Autenticación de usuarios por credenciales                    | Usuarios, roles | Must Have   |
| FE-02 | EP-01 | Navegación por rol                    | Mostrar opciones según Paciente, Recepcionista o Médico       | FE-01           | Must Have   |
| FE-03 | EP-02 | Configuración inicial del consultorio | Disponer de los datos mínimos del consultorio base            | Ninguna         | Must Have   |
| FE-04 | EP-02 | Gestión inicial de médicos            | Registrar médicos asociados al consultorio                    | FE-03           | Must Have   |
| FE-05 | EP-02 | Gestión inicial de recepcionistas     | Registrar recepcionistas del consultorio                      | FE-03           | Should Have |
| FE-06 | EP-03 | Registro de pacientes                 | Crear pacientes desde recepción o autogestión                 | FE-01, FE-03    | Must Have   |
| FE-07 | EP-03 | Búsqueda de pacientes                 | Buscar pacientes por datos básicos                            | FE-06           | Must Have   |
| FE-08 | EP-04 | Configuración de agenda médica        | Crear agenda y bloques de disponibilidad                      | FE-04           | Must Have   |
| FE-09 | EP-04 | Consulta de disponibilidad            | Visualizar horarios disponibles por médico                    | FE-08           | Must Have   |
| FE-10 | EP-05 | Reserva de cita por paciente          | Permitir que el paciente reserve una cita                     | FE-06, FE-09    | Must Have   |
| FE-11 | EP-05 | Creación de cita por recepcionista    | Permitir que recepción cree citas para pacientes              | FE-06, FE-09    | Must Have   |
| FE-12 | EP-05 | Consulta de citas                     | Visualizar citas según rol                                    | FE-10, FE-11    | Must Have   |
| FE-13 | EP-05 | Cancelación de cita                   | Cancelar citas bajo reglas del dominio                        | FE-12           | Must Have   |
| FE-14 | EP-05 | Reprogramación de cita                | Cambiar fecha y hora de una cita válida                       | FE-09, FE-12    | Should Have |
| FE-15 | EP-05 | Registro de asistencia                | Confirmar la llegada o registrar la inasistencia del paciente | FE-12           | Should Have |
| FE-16 | EP-06 | Agenda del médico                     | Mostrar citas asignadas al médico                             | FE-12           | Must Have   |
| FE-17 | EP-06 | Registro de observación básica        | Registrar una nota simple de atención                         | FE-16           | Must Have   |
| FE-18 | EP-06 | Marcar cita como atendida             | Cerrar la atención médica                                     | FE-17           | Must Have   |
| FE-19 | EP-06 | Historial básico                      | Consultar observaciones anteriores del paciente               | FE-17           | Should Have |

---

## 7.5 Historias de Usuario

### EP-01 — Acceso, usuarios y roles

#### HU-01 — Iniciar sesión

Como usuario  
Quiero iniciar sesión con mis credenciales  
Para acceder a las funcionalidades de AgenDoc según mi rol.

Descripción:

Permite que Paciente, Recepcionista y Médico ingresen al sistema de forma segura.

Criterios de aceptación:

- Given un usuario activo con credenciales válidas  
  When ingresa usuario y contraseña  
  Then el sistema permite el acceso.

- Given un usuario con credenciales inválidas  
  When intenta iniciar sesión  
  Then el sistema muestra un mensaje claro sin exponer información técnica.

- Given un usuario autenticado  
  When accede al sistema  
  Then visualiza únicamente las opciones permitidas para su rol.

Prioridad: Must Have  
Dependencias: Usuarios, roles, consultorio  
Estimación: 5 Story Points  
Definition of Done:

- Funciona en Web y Mobile.
- Aplica el Design System.
- Valida errores de forma clara.
- Respeta autorización por rol.
- Está probado funcionalmente.

---

#### HU-02 — Cerrar sesión

Como usuario  
Quiero cerrar sesión  
Para proteger mi información cuando deje de usar la plataforma.

Criterios de aceptación:

- Given un usuario autenticado  
  When selecciona cerrar sesión  
  Then el sistema finaliza la sesión.

- Given una sesión finalizada  
  When el usuario intenta acceder a una pantalla protegida  
  Then el sistema solicita autenticación.

Prioridad: Must Have  
Dependencias: HU-01  
Estimación: 2 Story Points  
Definition of Done:

- Funciona en Web y Mobile.
- Redirige correctamente al login.
- Limpia la sesión activa.
- Respeta el Design System.

---

### EP-02 — Gestión base del consultorio

#### HU-03 — Disponer del consultorio inicial

Como responsable del producto  
Quiero contar con un consultorio base configurado  
Para operar todas las funcionalidades dentro de una unidad organizacional.

Criterios de aceptación:

- Given la inicialización del entorno del MVP  
  When se aplican las migraciones y datos iniciales  
  Then existe un consultorio base disponible para operar.

- Given médicos, pacientes y usuarios registrados  
  When se crean dentro del MVP  
  Then quedan asociados al consultorio base correspondiente.

Prioridad: Must Have  
Dependencias: Ninguna  
Estimación: 3 Story Points  

Definition of Done:

- Existe un consultorio base persistido.
- El consultorio funciona como contexto organizacional.
- Los usuarios y perfiles de negocio pueden asociarse al consultorio.
- La configuración fue validada durante la Foundation.

Estado: Done

Implementación:

La necesidad funcional de contar con un consultorio inicial quedó satisfecha durante la Foundation mediante la creación y configuración del consultorio base del MVP.

Decisión:

Durante el MVP no se desarrollará una pantalla adicional para registrar consultorios. La historia se conserva para mantener la trazabilidad del Product Backlog y del contexto organizacional del sistema.

---

#### HU-04 — Registrar médico

Como recepcionista  
Quiero registrar médicos del consultorio  
Para que puedan tener agenda y atender citas.

Criterios de aceptación:

- Given un consultorio existente  
  When se registran los datos válidos del médico  
  Then el médico queda asociado al consultorio.

- Given un médico registrado  
  When se consulta la lista de médicos  
  Then aparece disponible para la gestión de agenda y citas.

Prioridad: Must Have  
Dependencias: HU-03  
Estimación: 5 Story Points  
Definition of Done:

- Médico asociado a consultorio.
- Especialidad registrada o seleccionada.
- Validaciones aplicadas.
- Disponible para agenda y citas.

---

### EP-03 — Gestión de pacientes

#### HU-05 — Registrar paciente desde recepción

Como recepcionista  
Quiero registrar pacientes  
Para poder crear citas en nombre del paciente.

Criterios de aceptación:

- Given datos válidos del paciente  
  When la recepcionista registra al paciente  
  Then el paciente queda asociado al consultorio.

- Given datos obligatorios incompletos  
  When intenta guardar  
  Then el sistema muestra validaciones claras.

Prioridad: Must Have  
Dependencias: HU-01, HU-03  
Estimación: 5 Story Points  
Definition of Done:

- Paciente asociado a consultorio.
- Formulario consistente con UX/UI.
- Validaciones claras.
- Disponible para creación de citas.

---

#### HU-06 — Buscar paciente

Como recepcionista  
Quiero buscar pacientes por datos básicos  
Para encontrar rápidamente a una persona antes de crear o gestionar una cita.

Criterios de aceptación:

- Given pacientes registrados  
  When la recepcionista busca por nombre o documento  
  Then el sistema muestra coincidencias.

- Given una búsqueda sin resultados  
  When no existe coincidencia  
  Then el sistema muestra un estado vacío claro.

Prioridad: Must Have  
Dependencias: HU-05  
Estimación: 3 Story Points  
Definition of Done:

- Permite búsqueda básica.
- Muestra resultados claros.
- Incluye estado vacío.
- Respeta el Design System.

---

### EP-04 — Agenda médica y disponibilidad

#### HU-07 — Crear bloques de agenda médica

Como recepcionista  
Quiero crear bloques de disponibilidad para un médico  
Para que el consultorio pueda ofrecer horarios de atención.

Criterios de aceptación:

- Given un médico registrado  
  When se crean bloques válidos de agenda  
  Then los horarios quedan disponibles para citas.

- Given bloques con datos inválidos  
  When se intenta guardar  
  Then el sistema muestra validaciones claras.

Prioridad: Must Have  
Dependencias: HU-04  
Estimación: 5 Story Points  
Definition of Done:

- Bloques asociados a médico y consultorio.
- Validación de fecha y hora.
- Bloques disponibles para reserva.
- Los bloques creados pueden ser consultados mediante HU-08.
- Compatible con Web y Mobile.

---

#### HU-08 — Consultar disponibilidad médica

Como paciente o recepcionista  
Quiero consultar horarios disponibles por médico  
Para seleccionar una fecha y hora de atención.

Criterios de aceptación:

- Given un médico con bloques disponibles  
  When se consulta su disponibilidad  
  Then el sistema muestra horarios disponibles.

- Given un horario ya reservado  
  When se consulta disponibilidad  
  Then ese horario no aparece como disponible.

- Given una consulta para la fecha actual  
  When existen bloques cuya hora ya transcurrió  
  Then esos horarios no se muestran como disponibles.

- Given múltiples bloques disponibles para una fecha  
  When se consulta la disponibilidad  
  Then los horarios se muestran ordenados cronológicamente.

- Given un médico sin disponibilidad para la fecha seleccionada  
  When se realiza la consulta  
  Then el sistema muestra un estado vacío claro.

Prioridad: Must Have  
Dependencias: HU-07  
Estimación: 5 Story Points  
Definition of Done:

- Muestra disponibilidad real.
- Evita doble reserva desde la experiencia.
- Usa calendario, cards o lista según plataforma.
- Respeta reglas del backend.
- Solo muestra bloques activos y disponibles.
- No muestra horarios pasados para la fecha actual.
- No muestra información de otros consultorios.

---

### EP-05 — Gestión de citas médicas

#### HU-09 — Reservar cita como paciente

Como paciente  
Quiero reservar una cita con un médico disponible  
Para asegurar una atención médica en una fecha y hora.

Criterios de aceptación:

- Given un paciente autenticado y un bloque disponible  
  When confirma la reserva  
  Then se crea una cita en estado Programada asociada al paciente autenticado, médico, consultorio y bloque seleccionados.

- Given un paciente autenticado  
  When intenta reservar utilizando datos de otro paciente o consultorio  
  Then el sistema impide la operación.

- Given un horario no disponible  
  When intenta reservar  
  Then el sistema impide la reserva.

Prioridad: Must Have  
Dependencias: HU-01, HU-08  
Estimación: 8 Story Points  
Definition of Done:

- Cita asociada a paciente, médico, consultorio y bloque.
- Estado inicial correcto.
- Previene doble reserva.
- Confirmación visible al usuario.
- Funciona en Web y Mobile.

---

#### HU-10 — Crear cita desde recepción

Como recepcionista  
Quiero crear una cita para un paciente  
Para gestionar la agenda del consultorio desde recepción.

Criterios de aceptación:

- Given un paciente registrado en el consultorio y un bloque disponible  
  When la recepcionista confirma la cita  
  Then se crea una cita en estado Programada asociada al paciente, médico, consultorio y bloque seleccionados.

- Given una recepcionista autenticada  
  When intenta crear una cita para un paciente o médico de otro consultorio  
  Then el sistema impide la operación.

- Given un horario ocupado  
  When intenta crear la cita  
  Then el sistema bloquea la operación.

Prioridad: Must Have  
Dependencias: HU-06, HU-08  
Estimación: 8 Story Points  
Definition of Done:

- Cita creada desde recepción.
- Paciente, médico y horario correctamente asociados.
- Validaciones claras.
- Respeta reglas de consultorio.

---

#### HU-11 — Consultar mis citas como paciente

Como paciente  
Quiero consultar mis citas  
Para conocer mis próximas atenciones y su estado.

Criterios de aceptación:

- Given un paciente con citas registradas  
  When ingresa a Mis citas  
  Then visualiza sus citas.

- Given un paciente sin citas  
  When ingresa a Mis citas  
  Then visualiza un estado vacío.

Prioridad: Must Have  
Dependencias: HU-09  
Estimación: 3 Story Points  
Definition of Done:

- Solo muestra citas del paciente autenticado.
- Muestra estado de cita.
- Usa badges definidos en UX/UI.
- Funciona en Web y Mobile.

---

#### HU-12 — Consultar agenda del consultorio

Como recepcionista  
Quiero visualizar la agenda del consultorio  
Para gestionar las citas del día.

Criterios de aceptación:

- Given citas registradas en el consultorio  
  When la recepcionista consulta la agenda  
  Then visualiza las citas por fecha, médico y estado.

- Given una fecha sin citas  
  When consulta la agenda  
  Then visualiza un estado vacío.

Prioridad: Must Have  
Dependencias: HU-10  
Estimación: 5 Story Points  
Definition of Done:

- Vista por consultorio.
- Filtros básicos por fecha, médico y estado.
- Estados representados con badges.
- No muestra datos de otros consultorios.

---

#### HU-13 — Cancelar cita

Como paciente o recepcionista  
Quiero cancelar una cita válida  
Para liberar el horario cuando la atención no se realizará.

Criterios de aceptación:

- Given una cita Programada  
  When el paciente propietario o la recepcionista confirma la cancelación  
  Then la cita cambia a estado Cancelada.

- Given una cita Confirmada  
  When la recepcionista registra una cancelación excepcional con motivo  
  Then la cita cambia a estado Cancelada.

- Given una cita Confirmada  
  When se intenta cancelar sin indicar el motivo  
  Then el sistema impide la operación.

- Given una cita Atendida, Cancelada o No asistió  
  When se intenta cancelar  
  Then el sistema impide la acción.

- Given una cita cancelada cuyo bloque aún no ha transcurrido  
  When finaliza la operación  
  Then el bloque vuelve a estar disponible.

Prioridad: Must Have  
Dependencias: HU-11, HU-12  
Estimación: 5 Story Points  
Definition of Done:

- Aplica las transiciones permitidas del estado.
- Solicita confirmación antes de cancelar.
- Exige motivo cuando la cita está Confirmada.
- Libera el bloque cuando corresponde.
- Restringe la operación por rol, consultorio y propietario.
- Muestra un mensaje claro.

---

#### HU-14 — Reprogramar cita

Como recepcionista  
Quiero reprogramar una cita  
Para cambiar la fecha u hora cuando el paciente lo requiera.

Criterios de aceptación:

- Given una cita reprogramable y un nuevo horario disponible  
  When confirma el cambio  
  Then la cita conserva su identificador, cambia al nuevo bloque y permanece en estado Programada.

- Given una cita Confirmada, Atendida, Cancelada o No asistió
  When se intenta reprogramar  
  Then el sistema impide la acción.

Prioridad: Should Have  
Dependencias: HU-08, HU-12  
Estimación: 8 Story Points  
Definition of Done:

- Valida disponibilidad.
- Aplica reglas de estado.
- Mantiene trazabilidad básica del cambio.
- Muestra confirmación clara.

---

#### HU-15 — Registrar resultado de asistencia

Como recepcionista  
Quiero registrar si el paciente llegó o no asistió a su cita  
Para mantener actualizado el estado operativo de la agenda.

Criterios de aceptación:

- Given una cita Programada y el paciente presente  
  When la recepcionista confirma su llegada  
  Then la cita cambia a estado Confirmada.

- Given una cita Programada cuya hora de inicio comenzó o transcurrió  
  When la recepcionista registra la inasistencia  
  Then la cita cambia a estado No asistió.

- Given una cita cuya hora aún no comienza  
  When se intenta marcar como No asistió  
  Then el sistema impide la acción.

- Given una cita Confirmada, Atendida, Cancelada o No asistió  
  When se intenta volver a registrar asistencia  
  Then el sistema impide la acción.

- Given una inasistencia registrada  
  When finaliza la operación  
  Then el sistema conserva la fecha, hora, usuario responsable y comentario cuando exista.

Prioridad: Should Have  
Dependencias: HU-12  
Estimación: 5 Story Points  

Definition of Done:

- Permite confirmar la llegada del paciente.
- Permite registrar la inasistencia.
- Solo recepción puede ejecutar las acciones.
- Aplica las reglas temporales y de estado.
- Conserva trazabilidad básica.
- Muestra confirmación visual.
- Respeta las reglas del dominio.

---

### EP-06 — Atención médica básica

#### HU-16 — Consultar agenda del médico

Como médico  
Quiero consultar mis citas asignadas  
Para organizar mi atención diaria.

Criterios de aceptación:

- Given un médico autenticado con citas asignadas  
  When consulta su agenda  
  Then visualiza únicamente sus citas.

- Given una fecha sin citas  
  When consulta su agenda  
  Then visualiza un estado vacío.

Prioridad: Must Have  
Dependencias: HU-10  
Estimación: 5 Story Points  
Definition of Done:

- Solo muestra citas del médico autenticado.
- Permite ver información básica del paciente.
- Usa componentes del Design System.
- Funciona en Web y Mobile.

---

#### HU-17 — Registrar observación médica básica

Como médico  
Quiero registrar una observación básica en una cita  
Para dejar constancia simple de la atención realizada.

Criterios de aceptación:

- Given un usuario que no es médico  
  When intenta registrar observación  
  Then el sistema impide la acción.

- Given una cita Confirmada asignada al médico autenticado  
  When registra una observación válida  
  Then la observación queda asociada a la cita.

- Given una cita que no se encuentra Confirmada  
  When el médico intenta registrar una observación  
  Then el sistema impide la acción.

- Given una cita Atendida  
  When se intenta modificar la observación  
  Then el sistema impide la acción.

Prioridad: Must Have  
Dependencias: HU-15, HU-16
Estimación: 5 Story Points  
Definition of Done:

- Solo médico puede registrar observación.
- La observación pertenece a una cita.
- No representa historia clínica.
- Valida contenido mínimo.
- Solo se registra sobre citas Confirmadas.
- La observación queda bloqueada cuando la cita se marca como Atendida.

---

#### HU-18 — Marcar cita como atendida

Como médico  
Quiero marcar una cita como atendida  
Para cerrar el ciclo de atención médica.

Criterios de aceptación:

- Given una cita Confirmada con observación médica registrada  
  When el médico asignado marca la cita como atendida  
  Then la cita cambia a estado Atendida.

- Given una cita Confirmada sin observación médica  
  When se intenta marcar como atendida  
  Then el sistema impide la acción.

- Given una cita cancelada  
  When intenta marcarla como atendida  
  Then el sistema impide la acción.

Prioridad: Must Have  
Dependencias: HU-17  
Estimación: 3 Story Points  
Definition of Done:

- Solo médico puede ejecutar la acción.
- Aplica reglas de estado.
- Muestra confirmación.
- Cierra el ciclo básico de cita.

---

#### HU-19 — Consultar historial básico del paciente

Como médico  
Quiero consultar observaciones básicas previas del paciente  
Para tener contexto mínimo antes de atenderlo.

Criterios de aceptación:

- Given un paciente con observaciones anteriores  
  When el médico consulta historial básico  
  Then visualiza las observaciones registradas.

- Given un paciente sin observaciones  
  When consulta historial básico  
  Then visualiza un estado vacío.

- Given un paciente con citas Canceladas, Programadas o No asistió  
  When el médico consulta el historial básico  
  Then esas citas no aportan observaciones al historial.

Prioridad: Should Have  
Dependencias: HU-17  
Estimación: 5 Story Points  
Definition of Done:

- Solo muestra observaciones básicas.
- No incorpora historia clínica completa.
- Respeta seguridad por médico y consultorio.
- Usa estado vacío cuando corresponde.
- Solo muestra observaciones de citas Atendidas.

---

## 7.6 Technical Stories

Las Technical Stories representan trabajo técnico que habilita o fortalece la plataforma sin incorporar funcionalidad visible para el usuario final.

Podrán ejecutarse dentro de un Sprint cuando soporten directamente una Historia de Usuario aprobada o una decisión arquitectónica registrada mediante ADR.

---

### 7.6.1 Regla de planificación de Technical Stories

Las Technical Stories forman parte del Product Backlog oficial del proyecto y se planifican dentro de los Sprints al igual que las Historias de Usuario.

Una Technical Story solo podrá marcarse como Completada cuando se implemente íntegramente el alcance definido para ella.

Cuando una Historia de Usuario dependa de una capacidad técnica pendiente, la Technical Story correspondiente deberá planificarse antes o dentro del mismo Sprint.

Las Technical Stories podrán coexistir con Historias de Usuario dentro del mismo Sprint y formarán parte del incremento comprometido.

---

### TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente

Objetivo

Configurar el entorno de desarrollo para permitir la ejecución y validación del sistema desde múltiples dispositivos conectados a la red local (computadora, teléfono y tablet), utilizando una estrategia de configuración por ambientes (Local, Desarrollo y Producción) basada en variables de entorno.

Esta Technical Story habilita un flujo de trabajo donde el desarrollo continúa ejecutándose localmente mientras el producto puede desplegarse posteriormente en entornos de desarrollo o producción (por ejemplo Railway y Vercel), sin requerir modificaciones en el código fuente.

Alcance

- Backend accesible desde la red local.
- Frontend Web accesible desde la red local.
- Orígenes CORS configurables por ambiente.
- Validación desde dispositivos móviles o tabletas.

Dependencias

- Spring Security.
- Configuración por perfiles.
- Frontend Web con Vite.

Estado

Completada.

Sprint previsto

Sprint 4.

Sprint ejecutado

Sprint 4.

Resultado

- Configuración por ambientes implementada.
- Backend accesible mediante localhost e IP local.
- Frontend accesible mediante localhost e IP local.
- Configuración CORS externalizada mediante perfiles.
- Uso de variables de entorno para URL del Backend.
- Validación funcional desde computadora, teléfono y tablet.
- Base preparada para despliegues en Railway y Vercel sin modificar el código.

---

### TS-02 — Completar autenticación JWT End-to-End

Objetivo

Completar el flujo de autenticación para que el Backend genere un JWT y el Frontend lo utilice en todas las solicitudes protegidas.

Alcance

- Generación del JWT durante el login.
- Almacenamiento controlado del token en el Frontend.
- Inclusión automática de `Authorization: Bearer <token>`.
- Validación del token mediante Spring Security.
- Disponibilidad del usuario autenticado en el SecurityContext.
- Eliminación de autenticación temporal o bypass de seguridad.

Fuera del alcance del MVP

- Refresh Tokens.
- Rotación de tokens.
- Blacklist de tokens.
- OAuth2.
- Single Sign-On.

Dependencias

- HU-01.
- Spring Security.

Estado

Completada.

Sprint previsto

Sprint 4.

Sprint ejecutado

Sprint 4.

Resultado

- Backend genera JWT firmado durante el proceso de autenticación.
- El Frontend almacena la sesión autenticada utilizando Session Storage.
- Todas las solicitudes protegidas incluyen automáticamente el encabezado Authorization Bearer.
- Spring Security valida el JWT en cada solicitud protegida.
- El usuario autenticado queda disponible mediante el SecurityContext.
- Se eliminaron los mecanismos temporales de autenticación utilizados durante el desarrollo inicial.
- La autenticación quedó preparada para soportar TS-03 y TS-04.

---

### TS-03 — Implementar contexto del usuario autenticado

Objetivo

Permitir que las operaciones del dominio obtengan desde el contexto autenticado la información del usuario, rol y consultorio.

Alcance

- Obtener usuario autenticado.
- Obtener rol.
- Obtener consultorio.
- Obtener perfil de negocio asociado cuando corresponda.
- Evitar recibir desde el Frontend identificadores que puedan derivarse del usuario autenticado.

Dependencias

- TS-02.

Estado

Completada.

Sprint previsto

Sprint 4.

Sprint ejecutado

Sprint 4.

Resultado

- Se implementó un servicio centralizado para obtener el contexto del usuario autenticado desde Spring Security.
- El Backend puede obtener el usuario autenticado, su rol y su consultorio sin recibir dichos identificadores desde el Frontend.
- El contexto autenticado quedó disponible para ser reutilizado por los módulos funcionales del sistema.
- Se incorporó validación para detectar usuarios sin rol o sin consultorio asociado.
- La infraestructura quedó preparada para implementar la autorización por dominio mediante TS-04.

---

### TS-04 — Implementar autorización por dominio

Objetivo

Aplicar controles de acceso según rol, consultorio y propiedad del recurso.

Alcance

- El paciente solo podrá gestionar sus propias citas.
- La recepcionista solo podrá operar sobre recursos de su consultorio.
- El médico solo podrá consultar sus citas y registrar observaciones sobre ellas.
- El Backend validará autorización antes de ejecutar las reglas de negocio.

Dependencias

- TS-02.
- TS-03.

Estado

Pendiente.

Sprint previsto

Sprint 4.

---

### TS-05 — Endurecer seguridad y manejo de accesos no autorizados

Objetivo

Uniformizar el comportamiento de autenticación y autorización del Backend y los clientes.

Alcance

- Respuestas 401 consistentes.
- Respuestas 403 consistentes.
- Manejo uniforme de errores de seguridad.
- Revisión de endpoints públicos y protegidos.
- Eliminación de configuraciones temporales.
- Validación de no exposición de tokens ni datos sensibles.

Dependencias

- TS-02.
- TS-03.
- TS-04.

Estado

Pendiente.

Sprint previsto

Sprint 5.

---

## 7.7 Priorización MoSCoW

### Must Have

| Elemento | Justificación                                        |
|----------|------------------------------------------------------|
| HU-01    | Sin autenticación no existe operación segura.        |
| HU-02    | Necesario para seguridad básica de sesión.           |
| HU-03    | El consultorio es el contexto principal del dominio. |
| HU-04    | Sin médicos no puede existir agenda ni citas.        |
| HU-05    | Sin pacientes no puede existir cita médica.          |
| HU-06    | Recepción necesita ubicar pacientes para operar.     |
| HU-07    | Sin bloques de agenda no existe disponibilidad.      |
| HU-08    | La disponibilidad es requisito para reservar citas.  |
| HU-09    | Permite el flujo central del paciente.               |
| HU-10    | Permite el flujo central de recepción.               |
| HU-11    | El paciente debe visualizar sus citas.               |
| HU-12    | Recepción debe gestionar la agenda del consultorio.  |
| HU-13    | Cancelar citas es necesario para liberar horarios.   |
| HU-16    | El médico debe consultar sus citas.                  |
| HU-17    | La observación básica está incluida en el MVP.       |
| HU-18    | Permite cerrar el ciclo de atención.                 |

### Should Have

| Elemento | Justificación                                                                          |
|----------|----------------------------------------------------------------------------------------|
| HU-14    | Reprogramar es importante, pero puede implementarse después de crear y cancelar citas. |
| HU-15    | Registrar asistencia o inasistencia completa el control operativo de la agenda, pero no bloquea la creación inicial de citas. |
| HU-19    | Aporta valor médico, pero depende de tener observaciones previas.                      |

### Could Have

| Elemento                            | Justificación                                                             |
|-------------------------------------|---------------------------------------------------------------------------|
| Recuperación de contraseña completa | Útil, pero no crítica para iniciar el MVP operativo.                      |
| Dashboard con indicadores avanzados | Aporta visibilidad, pero no es necesario para validar el flujo principal. |
| Perfil avanzado de usuario          | Puede evolucionar luego del flujo base.                                   |

### Won't Have MVP

| Elemento                  | Justificación                    |
|---------------------------|----------------------------------|
| Historia clínica completa | Está fuera del alcance aprobado. |
| Recetas                   | Está fuera del alcance aprobado. |
| Pagos                     | Está fuera del alcance aprobado. |
| Facturación               | Está fuera del alcance aprobado. |
| Laboratorio               | Está fuera del alcance aprobado. |
| Seguros médicos           | Está fuera del alcance aprobado. |
| Videoconsultas            | Está fuera del alcance aprobado. |
| Notificaciones avanzadas  | Está fuera del alcance aprobado. |

---

## 7.8 Dependencias funcionales

Secuencia lógica de implementación:

1. Consultorio base.
2. Usuarios, roles e inicio de sesión.
3. Registro de médicos.
4. Registro y búsqueda de pacientes.
5. Creación de agenda y bloques.
6. Consulta de disponibilidad.
7. Creación de citas.
8. Consulta de citas por rol.
9. Cancelación y reprogramación.
10. Registro de asistencia o inasistencia.
11. Agenda del médico.
12. Observación médica básica.
13. Cita atendida.
14. Historial básico.

Dependencias principales:

| Historia | Depende de                         |
|----------|------------------------------------|
| HU-01    | Usuarios, roles, consultorio       |
| HU-03    | Ninguna                            |
| HU-04    | HU-03                              |
| HU-05    | HU-01, HU-03                       |
| HU-06    | HU-05                              |
| HU-07    | HU-04                              |
| HU-08    | HU-07                              |
| HU-09    | HU-01, HU-08, TS-02, TS-03, TS-04  |
| HU-10    | HU-06, HU-08, TS-02, TS-03         |
| HU-11    | HU-09, TS-04                       |
| HU-12    | HU-10, TS-03                       |
| HU-13    | HU-11, HU-12, TS-04                |
| HU-14    | HU-08, HU-12                       |
| HU-15    | HU-12                              |
| HU-16    | HU-10, TS-04                       |
| HU-17    | HU-15, HU-16                       |
| HU-18    | HU-17                              |
| HU-19    | HU-17                              |

---

## 7.9 Resultado del Sprint 1

### Objetivo del Sprint 1

Construir el primer incremento funcional de AgenDoc, permitiendo que un usuario recepcionista acceda al sistema, opere dentro de un consultorio base, registre médicos, registre pacientes y deje preparada la base funcional para crear agendas y citas en el siguiente incremento.

### Historias completadas en Sprint 1

| Historia | Nombre                             | Story Points |
|----------|------------------------------------|-------------:|
| HU-01    | Iniciar sesión                     | 5            |
| HU-02    | Cerrar sesión                      | 2            |
| HU-04    | Registrar médico                   | 5            |
| HU-05    | Registrar paciente desde recepción | 5            |
| HU-06    | Buscar paciente                    | 3            |
| HU-07    | Crear bloques de agenda médica     | 5            |

Estado del Sprint:

✅ Completado

Resultado:

- HU-01 completada.
- HU-02 completada.
- HU-04 completada.
- HU-05 completada.
- HU-06 completada.
- HU-07 completada.
- HU-03 satisfecha mediante la Foundation.
- Repositorio integrado en `develop`.
- Ramas feature eliminadas.
- Working tree limpio.

Total completado: **25 Story Points funcionales**

HU-03 fue satisfecha durante la Foundation y no forma parte de los 25 Story Points funcionales ejecutados durante la Fase B del Sprint 1.

### Justificación

El Sprint 1 entregó el primer incremento funcional del dominio del negocio.

Al finalizar el Sprint quedó disponible:

- Autenticación inicial de usuarios.
- Registro de médicos.
- Registro de pacientes.
- Búsqueda de pacientes.
- Configuración inicial de bloques de disponibilidad.

Este incremento constituyó el primer Vertical Slice funcional del producto y dejó preparada la base para iniciar la gestión de citas médicas en el Sprint 2.

---

## 7.10 Validación de consistencia

| Artefacto                 | Validación                                                                                                                  |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Product Vision            | El backlog mantiene foco en gestión simple y eficiente de citas médicas.                                                    |
| MVP Scope                 | Solo incluye roles y funcionalidades aprobadas para Paciente, Recepcionista y Médico.                                       |
| Modelo de Dominio         | Utiliza Consultorio, Usuario, Rol, Paciente, Médico, Recepcionista, Agenda Médica, Cita Médica y Observación Médica Básica. |
| Modelo de Datos           | Mantiene alineación con las entidades persistentes y catálogos aprobados.                                                   |
| Arquitectura del Producto | Respeta Backend como fuente de reglas, Web y Mobile consumiendo la misma API y autorización por rol.                        |
| UX/UI                     | Considera Design System, navegación por rol, estados vacíos, badges, formularios y accesibilidad.                           |

No se identifican historias duplicadas.

El backlog mantiene una secuencia incremental viable.

El Product Backlog refinado mantiene una distribución incremental viable para completar el MVP durante los Sprints definidos en el Roadmap.

---

## 7.10.1 Matriz de Trazabilidad

La siguiente matriz resume la relación entre la visión del producto y los elementos del Product Backlog.

| Product Vision              | Épica  | Features principales | Historias relacionadas  |
|-----------------------------|--------|----------------------|-------------------------|
| Gestión segura del acceso   | EP-01  | FE-01, FE-02         | HU-01, HU-02            |
| Operación del consultorio   | EP-02  | FE-03, FE-04, FE-05  | HU-03, HU-04            |
| Administración de pacientes | EP-03  | FE-06, FE-07         | HU-05, HU-06            |
| Gestión de disponibilidad   | EP-04  | FE-08, FE-09         | HU-07, HU-08            |
| Gestión de citas médicas    | EP-05  | FE-10 a FE-15        | HU-09 a HU-15           |
| Atención médica básica      | EP-06  | FE-16 a FE-19        | HU-16 a HU-19           |

Esta matriz permitirá mantener la trazabilidad entre la estrategia del producto y los incrementos desarrollados durante los Sprints.

---

## 7.11 Decisión de la Sesión 6

Se aprueba el Product Backlog oficial del MVP de AgenDoc como base para la planificación del Sprint 1.

El backlog podrá ser refinado durante la ejecución del proyecto, pero cualquier cambio relevante deberá registrarse en el Blueprint.

---

## 7.12 Definition of Ready

Una Historia de Usuario podrá incorporarse a un Sprint únicamente cuando cumpla los siguientes criterios:

- Está claramente redactada.
- Tiene objetivo de negocio definido.
- Tiene criterios de aceptación en formato Given / When / Then.
- Tiene prioridad asignada.
- Tiene Story Points estimados.
- Tiene dependencias identificadas.
- No presenta bloqueos funcionales conocidos.

El cumplimiento de estos criterios garantiza que el equipo pueda iniciar el desarrollo sin incertidumbres relevantes.

---

# 8. Architecture Decision Records (ADR)

## ADR-001

### Decisión

El Consultorio será la entidad principal del dominio.

### Justificación

Permite escalar desde un médico independiente hasta consultorios con múltiples médicos sin modificar el modelo conceptual.

### Estado

✅ Aprobado

---

## ADR-002

### Decisión

El MVP tendrá únicamente tres roles.

- Paciente
- Recepcionista
- Médico

### Estado

✅ Aprobado

---

## ADR-003

### Decisión

La gestión de citas será la funcionalidad central del MVP.

### Estado

✅ Aprobado

---

## ADR-004

### Decisión

El proyecto utilizará un único documento vivo denominado **AgenDoc Project Blueprint**.

Toda decisión aprobada deberá registrarse en este documento.

### Estado

✅ Aprobado

---

## ADR-005

### Decisión

El desarrollo seguirá Scrum.

Antes de iniciar el Sprint 1 se completará un Sprint 0 dedicado al descubrimiento, diseño y planificación del producto.

### Estado

✅ Aprobado

---

## ADR-006

### Decisión

La Cita Médica será el agregado transaccional principal del dominio.

### Justificación

La mayor parte de los procesos del MVP giran alrededor de la gestión de citas médicas.

### Estado

✅ Aprobado

---

## ADR-007

### Decisión

La Observación Médica Básica será parte del MVP, pero no constituye una Historia Clínica.

### Justificación

Permite registrar información mínima de atención sin ampliar el alcance funcional del MVP.

### Estado

✅ Aprobado

---

## ADR-008

### Decisión

El Modelo de Datos del MVP separará Agenda Médica y Bloque de Agenda.

### Justificación

Permite representar la agenda general del médico y sus horarios disponibles de forma ordenada, evitando ambigüedad en la validación de disponibilidad y conflictos de citas.

### Estado

✅ Aprobado

---

## ADR-009

### Decisión

El MVP de AgenDoc utilizará una arquitectura de Monolito Modular aplicando principios de Clean Architecture.

### Justificación

Permite iniciar el desarrollo con una estructura simple, mantenible y escalable, evitando la complejidad operativa de microservicios durante el MVP.

### Estado

✅ Aprobado

---

## ADR-010

### Decisión

El Backend API será la fuente oficial de reglas de negocio, seguridad y validaciones del dominio.

### Justificación

Permite que Frontend Web y Mobile consuman una misma lógica centralizada, evitando duplicidad de reglas y reduciendo inconsistencias.

### Estado

✅ Aprobado

---

## ADR-011

### Decisión

El MVP utilizará PostgreSQL como motor de base de datos relacional.

### Justificación

PostgreSQL permite gestionar integridad referencial, consultas eficientes y crecimiento futuro, manteniendo alineación con el Modelo de Datos aprobado.

### Estado

✅ Aprobado

---

## ADR-012

### Decisión

La autenticación se implementará mediante JWT y la autorización será basada en roles.

### Justificación

Permite un modelo stateless compatible con Web y Mobile, manteniendo control de acceso por rol y consultorio.

### Estado

✅ Aprobado

---

## ADR-013

### Decisión

El MVP utilizará un Design System propio inspirado en Material Design y Human Interface Guidelines.

### Justificación

Permite mantener una experiencia consistente entre la plataforma Web y la aplicación Mobile, facilita la reutilización de componentes y proporciona una identidad visual propia para AgenDoc.

### Estado

✅ Aprobado

---

## ADR-014

### Decisión

La plataforma Web y la aplicación Mobile compartirán un único Design System basado en Design Tokens.

### Justificación

Permite reutilizar componentes, reducir inconsistencias visuales y simplificar la evolución futura del producto.

### Estado

✅ Aprobado

---

## ADR-015

### Decisión

El ciclo de vida de la Cita Médica del MVP utilizará únicamente los estados Programada, Confirmada, Atendida, Cancelada y No asistió.

Solicitada y En atención quedan fuera del MVP.

Reprogramada será considerada una acción sobre una cita Programada y no un estado permanente.

### Justificación

- El MVP no requiere aprobación posterior de reservas.
- La reprogramación modifica fecha, hora y bloque sin cambiar la naturaleza de la cita.
- El estado En atención introduce una acción adicional que no aporta valor suficiente al flujo básico.
- El modelo reducido simplifica reglas, transiciones, experiencia de usuario y pruebas.

### Estado

✅ Aprobado

---

## ADR-016

### Decisión

La disponibilidad médica será calculada como información derivada de los bloques de agenda y de las citas activas.

No se creará una entidad ni tabla independiente de disponibilidad.

La disponibilidad será calculada únicamente para citas activas (Programada y Confirmada).

### Justificación

- Evita duplicidad de información.
- Mantiene el modelo de datos normalizado.
- Reduce el riesgo de inconsistencias.
- Permite calcular la disponibilidad real en función de los bloques y citas existentes.

### Estado

✅ Aprobado

---

# 9. Roadmap

## Sprint 0 — Descubrimiento y Diseño del Producto

### Sesión 1 — Product Vision y definición del MVP

**Objetivo**

Definir el propósito del producto, el alcance del MVP y las funcionalidades que quedan fuera de esta primera versión.

Las Technical Stories podrán ejecutarse dentro del Sprint correspondiente cuando habiliten directamente la implementación de las Historias de Usuario planificadas.

**Estado**

✅ Completada

---

### Sesión 2 — Modelo de Dominio

**Objetivo**

Identificar las entidades del negocio, sus relaciones y las principales reglas de negocio.

**Estado**

✅ Completada

---

### Sesión 3 — Modelo de Datos (ERD)

**Objetivo**

Diseñar el modelo de datos optimizado para soportar el MVP.

**Estado**

✅ Completada

---

### Sesión 4 — Arquitectura del Producto

**Objetivo**

Definir:

- Frontend Web
- Aplicación Mobile
- Backend
- Base de datos
- Infraestructura
- Despliegue
- Seguridad
- Integraciones

**Estado**

✅ Completada

---

### Sesión 5 — UX/UI

**Objetivo**

Definir:

- Principios de UX.
- Identidad visual.
- Design System.
- Arquitectura de navegación.
- Catálogo de pantallas.
- Convenciones de diseño.

**Estado**

✅ Completada

---

### Sesión 6 — Product Backlog

**Objetivo**

Construir:

- Épicas
- Features
- Historias de Usuario
- Criterios de aceptación
- Priorizización inicial

**Estado**

✅ Completada

---

## Sprint 1 — Base operativa del consultorio

### Sprint Goal

Permitir que una recepcionista acceda al sistema y configure los elementos mínimos necesarios para iniciar la operación del consultorio.

### Sprint Backlog

- HU-01 — Iniciar sesión.
- HU-02 — Cerrar sesión.
- HU-04 — Registrar médico.
- HU-05 — Registrar paciente desde recepción.
- HU-06 — Buscar paciente.
- HU-07 — Crear bloques de agenda médica.

### Resultado

- Consultorio base disponible.
- Autenticación funcional inicial.
- Médicos registrados.
- Pacientes registrados y consultables.
- Bloques de agenda configurados.

### Estado

✅ Completado

---

## Sprint 2 — Creación segura de citas desde recepción

### Sprint Goal

Permitir que la recepcionista consulte disponibilidad médica, cree citas y visualice la agenda del consultorio, preparando la transición hacia una autenticación JWT completa y el uso del contexto del usuario autenticado.

### Product Backlog Items previstos

Product Backlog Items del Sprint

✅ HU-08 — Consultar disponibilidad médica

✅ HU-10 — Crear cita desde recepción

✅ HU-12 — Consultar agenda del consultorio

### Technical Stories asociadas

- TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente.
- TS-02 — Completar autenticación JWT End-to-End.
- TS-03 — Implementar contexto del usuario autenticado.

### Estimación funcional referencial

18 Story Points funcionales
más las Technical Stories TS-01, TS-02 y TS-03.

### Estado

✅ Completado.

Resultado

Historias completadas

✅ HU-08 — Consultar disponibilidad médica

✅ HU-10 — Crear cita desde recepción

✅ HU-12 — Consultar agenda del consultorio

Resultado funcional

- Consulta de disponibilidad implementada.
- Creación de citas desde recepción implementada.
- Agenda del consultorio implementada.
- Integración Backend–Frontend completada.
- Flujo de creación de citas validado de extremo a extremo.

---

## Sprint 3 — Gestión operativa del ciclo de la cita

### Sprint Goal

Permitir que la recepcionista gestione las principales situaciones operativas de una cita después de su creación.

### Product Backlog Items previstos

- HU-13 — Cancelar cita.
- HU-14 — Reprogramar cita.
- HU-15 — Registrar resultado de asistencia.

### Technical Stories previstas

- TS-04 — Implementar autorización por dominio.
- TS-05 — Endurecer seguridad y manejo de accesos no autorizados.

### Estimación funcional referencial

18 Story Points funcionales
más TS-04 y TS-05.

Avance

✅ HU-13 — Cancelar cita

✅ HU-14 — Reprogramar cita

✅ HU-15 — Registrar resultado de asistencia

### Estado

✅ Completado.

Resultado funcional

- Cancelación de citas implementada.
- Reprogramación de citas implementada.
- Confirmación de llegada del paciente implementada.
- Registro de inasistencia implementado respetando las reglas temporales del dominio.
- Agenda del consultorio actualizada automáticamente después de cada operación.
- Flujo operativo completo de la recepcionista validado de extremo a extremo.

---

## Sprint 4 — Autogestión del paciente

### Sprint Goal

Permitir que el paciente consulte disponibilidad, reserve citas y utilice las capacidades de consulta y cancelación previamente implementadas.

### Product Backlog Items previstos

Sprint 4

- TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente
- TS-02 — Completar autenticación JWT End-to-End
- TS-03 — Implementar contexto del usuario autenticado
- TS-04 — Implementar autorización por dominio
- HU-09 — Reservar cita como paciente
- HU-11 — Consultar mis citas como paciente

### Consideración funcional

La capacidad del paciente para cancelar sus propias citas utilizará la funcionalidad implementada en HU-13, respetando autorización por propietario y estado de la cita.

### Estimación funcional referencial

11 Story Points.

### Estado

🚧 En ejecución.

Avance

- ✅ TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente
- ✅ TS-02 — Completar autenticación JWT End-to-End
- ✅ TS-03 — Implementar contexto del usuario autenticado
- ⏳ TS-04 — Implementar autorización por dominio
- ⏳ HU-09 — Reservar cita como paciente
- ⏳ HU-11 — Consultar mis citas como paciente

---

## Sprint 5 — Atención médica básica

### Sprint Goal

Permitir que el médico complete el ciclo de atención de una cita mediante la consulta de su agenda, el registro de observaciones, el cierre de la atención y la consulta del historial básico.

### Product Backlog Items previstos

- HU-16 — Consultar agenda del médico.
- HU-17 — Registrar observación médica básica.
- HU-18 — Marcar cita como atendida.
- HU-19 — Consultar historial básico del paciente.

### Estimación funcional referencial

18 Story Points.

### Estado

Planificado.

---

# 10. Historial de Sesiones

## Sprint 0

### Sesión 1 — Product Vision y definición del MVP

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se definió la visión del producto.
- Se definió el alcance del MVP.
- Se aprobó el Consultorio como entidad principal del dominio.
- Se establecieron tres roles iniciales: Paciente, Recepcionista y Médico.
- Se definió la gestión de citas como funcionalidad central del MVP.
- Se excluyeron del MVP la historia clínica, pagos, facturación, recetas y funcionalidades avanzadas.
- Se adoptó un único documento vivo denominado **AgenDoc Project Blueprint**.
- Se aprobó el Sprint 0 como fase de descubrimiento y diseño.
- Se definió el roadmap oficial del proyecto.

---

### Sesión 2 — Modelo de Dominio

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se definieron las entidades principales del dominio.
- Se confirmó al Consultorio como agregado organizacional principal.
- Se definió a la Cita Médica como agregado transaccional principal.
- Se establecieron las relaciones principales entre Consultorio, Médico, Paciente, Recepcionista, Usuario, Rol, Agenda Médica, Cita Médica y Observación Médica Básica.
- Se definieron las reglas de negocio iniciales del dominio.
- Se establecieron las invariantes principales del dominio.
- Se identificaron los eventos de negocio relevantes para el MVP.
- Se confirmó que la Observación Médica Básica no representa una historia clínica completa.

---

### Sesión 3 — Modelo de Datos (ERD)

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se definió el Modelo de Datos conceptual del MVP.
- Se identificaron las entidades persistentes principales.
- Se definieron los catálogos iniciales: Rol, Estado de Cita y Especialidad Médica.
- Se definieron las cardinalidades principales del modelo.
- Se aprobaron las reglas de integridad del modelo de datos.
- Se aprobaron los campos comunes de auditoría.
- Se separó Agenda Médica de Bloque de Agenda para representar mejor la disponibilidad del médico.
- Se confirmó que el modelo no incorpora entidades fuera del MVP.
- Se documentaron las entidades persistentes del Modelo de Datos.
- Se documentaron las relaciones y cardinalidades del Modelo de Datos.
- Se documentaron las entidades de referencia del sistema.
- Se documentaron las convenciones oficiales del Modelo de Datos.
- Se documentó la estrategia de auditoría básica.
- Se documentaron las consideraciones de escalabilidad futura del modelo.

---

### Sesión 4 — Arquitectura del Producto

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se aprobó una arquitectura de Monolito Modular para el MVP.
- Se aprobó aplicar principios de Clean Architecture.
- Se definió que el Backend API será la fuente oficial de reglas de negocio.
- Se definió React con TypeScript para el Frontend Web.
- Se definió React Native con Expo para la aplicación Mobile.
- Se definió Java Spring Boot para el Backend.
- Se definió PostgreSQL como motor de base de datos.
- Se definió REST sobre HTTPS como mecanismo principal de comunicación.
- Se definió JWT como modelo de autenticación.
- Se definió autorización basada en roles.
- Se definió una infraestructura inicial con ambientes local, desarrollo y producción.
- Se definió el uso de Docker y GitHub Actions como base para despliegue y CI/CD.
- Se confirmó que integraciones como notificaciones, pagos, historia clínica, facturación y laboratorio quedan fuera del MVP, pero la arquitectura deberá permitir incorporarlas posteriormente.

---

### Sesión 5 — UX/UI

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se definieron los principios oficiales de UX del producto.
- Se aprobó la identidad visual de AgenDoc.
- Se definió la paleta de colores, tipografía, espaciado e iconografía oficiales.
- Se aprobó el Design System v1 para Web y Mobile.
- Se definieron los Design Tokens oficiales del producto.
- Se estableció la arquitectura de navegación para Paciente, Recepcionista y Médico.
- Se aprobó el catálogo oficial de pantallas del MVP.
- Se definieron las convenciones oficiales para formularios, validaciones, mensajes, tablas, calendarios, accesibilidad y responsive design.
- Se confirmó que todo el Frontend deberá reutilizar los componentes definidos en el Design System.

---

### Sesión 6 — Product Backlog

**Estado**

✅ Aprobada

### Decisiones tomadas

- Se definió el Product Backlog oficial del MVP.
- Se aprobaron seis épicas principales.
- Se definieron las features principales por épica.
- Se definieron historias de usuario con criterios de aceptación, prioridad, dependencias, estimación y Definition of Done.
- Se priorizó el backlog utilizando MoSCoW.
- Se identificaron dependencias funcionales entre historias.
- Se propuso el alcance inicial del Sprint 1.
- Se confirmó que el backlog mantiene trazabilidad con Product Vision, MVP Scope, Modelo de Dominio, Modelo de Datos, Arquitectura del Producto y UX/UI.

---

## Sprint 1

### Fase A — Foundation

**Estado**

✅ Completada

### Decisiones tomadas

- Se inicializó la estructura oficial del repositorio.
- Se configuró Backend con Spring Boot.
- Se configuró Frontend Web con React, TypeScript y Vite.
- Se configuró Mobile con React Native y Expo.
- Se configuró PostgreSQL local para el MVP.
- Se configuró Flyway para migraciones versionadas.
- Se decidió no utilizar Docker durante el desarrollo local del MVP.
- Se reservó la carpeta `docker/` para futuras configuraciones.
- Se configuró GitHub Actions para validar Backend y Frontend Web.
- Se adoptó GitHub CLI como herramienta de autenticación e interacción con GitHub.
- Se alineó la documentación oficial dentro de `docs/product`.

---

### Actualización de Arquitectura y Backlog Técnico

**Estado**

✅ Aprobada

**Decisiones tomadas**

- Se formaliza el uso de Technical Stories (TS) dentro del Product Backlog.
- Se incorpora TS-01 para externalizar la configuración CORS.
- Se incorpora TS-02 para implementar autenticación JWT y el contexto del usuario autenticado.
- Se establece que las Technical Stories representan trabajo técnico habilitador y no funcionalidad visible para el usuario final.

---

### Cierre del Sprint 1

**Estado**

✅ Completado

**Historias completadas**

- HU-01 — Iniciar sesión.
- HU-02 — Cerrar sesión.
- HU-04 — Registrar médico.
- HU-05 — Registrar paciente desde recepción.
- HU-06 — Buscar paciente.
- HU-07 — Crear bloques de agenda médica.

**Decisiones tomadas**

- Se declaró completado el Sprint 1.
- Se confirmó `develop` sincronizada con `origin/develop`.
- Se confirmó el working tree limpio.
- Se eliminaron las ramas feature locales y remotas.
- Se confirmó el uso de GitHub CLI como herramienta oficial de versionado.
- Se confirmó que HU-03 quedó satisfecha mediante la Foundation.

---

### Product Backlog Refinement posterior al Sprint 1

**Estado**

✅ Completado

**Decisiones tomadas**

- Se revisaron las 19 Historias de Usuario del MVP.
- Se mantuvo el alcance funcional aprobado.
- Se definió un roadmap total de cinco Sprints.
- Se definieron los objetivos funcionales de los Sprints 2 al 5.
- Se refinó HU-15 para registrar llegada o inasistencia.
- Se ajustó HU-15 de 3 a 5 Story Points.
- Se aprobó el ciclo de vida de la cita con cinco estados.
- Se estableció que Reprogramada será una acción y no un estado.
- Se estableció que la disponibilidad será información derivada.
- Se formalizaron TS-01, TS-02, TS-03, TS-04 y TS-05.
- Se identificaron dependencias entre Historias de Usuario y Technical Stories.
- Se declaró el Product Backlog listo para el Sprint Planning del Sprint 2.

---

Sprint 2

Sesión 1 — Disponibilidad médica, creación de citas y agenda del consultorio

Estado

✅ Completada

Historias completadas:

- HU-08 — Consultar disponibilidad médica.
- HU-10 — Crear cita desde recepción.
- HU-12 — Consultar agenda del consultorio.

Decisiones tomadas

Se implementó la consulta de disponibilidad por médico y fecha.
Se implementó la creación de citas desde recepción.
Se incorporó la validación de disponibilidad en el Backend.
Se implementó la consulta de la agenda del consultorio con filtros por fecha, médico y estado.
Se mantuvo la separación entre Agenda Médica y Cita Médica definida en el modelo de dominio.
Se mantuvo el Backend como fuente oficial de reglas de negocio.
Se mantuvo la autenticación temporal prevista hasta completar TS-02 y TS-03.

---

Sprint 3

Sesión 1 — Gestión operativa del ciclo de la cita

Estado

✅ Completada

Historias completadas:

- HU-13 — Cancelar cita.
- HU-14 — Reprogramar cita.
- HU-15 — Registrar resultado de asistencia.

Decisiones tomadas

- Se implementó la cancelación de citas respetando las reglas del dominio.
- Se implementó la reprogramación conservando el identificador de la cita.
- Se implementó la confirmación de llegada del paciente mediante la transición de Programada a Confirmada.
- Se implementó el registro de inasistencia respetando la restricción de que la hora de inicio de la cita debe haber comenzado o transcurrido.
- Se incorporó la validación de estados permitidos para confirmar llegada, registrar inasistencia, cancelar y reprogramar.
- Se liberan automáticamente los bloques de agenda al cancelar una cita cuando corresponde.
- Se actualiza automáticamente la agenda del consultorio después de cada operación.
- Se mantuvo el Backend como fuente oficial de reglas de negocio.
- Se completó la integración Backend–Frontend para el ciclo operativo de la recepcionista.
- Se validó funcionalmente el flujo completo mediante pruebas manuales y pruebas automatizadas.

---

Sprint 4

Sesión 1 — Inicio del Sprint 4

Estado

✅ Aprobada

Decisiones tomadas

- Se declara oficialmente iniciado el Sprint 4.
- Se confirma el cierre completo del Sprint 3.
- Se selecciona HU-09 como primera Historia del Sprint.
- Se inicia el refinamiento funcional de HU-09.
- Se revisa y sincroniza la documentación oficial antes del desarrollo.

---

Sprint 4

Sesión 2 — Cierre de TS-02

Estado

✅ Completada

Technical Stories completadas

- TS-02 — Completar autenticación JWT End-to-End.

Decisiones tomadas

- Se implementó autenticación JWT de extremo a extremo.
- El Backend genera el token durante el inicio de sesión.
- Spring Security valida el JWT en todas las solicitudes protegidas.
- El Frontend almacena la sesión autenticada utilizando Session Storage.
- El cliente incorpora automáticamente el encabezado Authorization Bearer en todas las llamadas protegidas.
- Se eliminaron los mecanismos temporales de autenticación utilizados durante el desarrollo.
- La plataforma quedó preparada para implementar TS-03 (Contexto del usuario autenticado) y TS-04 (Autorización por dominio).
- Se validó el flujo completo mediante pruebas automatizadas y pruebas funcionales.

---

Sprint 4

Sesión 3 — Cierre de TS-03

Estado

✅ Completada

Technical Stories completadas

- TS-03 — Implementar contexto del usuario autenticado.

Decisiones tomadas

- Se implementó un proveedor centralizado del contexto del usuario autenticado.
- El Backend obtiene el usuario autenticado directamente desde Spring Security.
- El contexto autenticado expone el usuario, el rol y el consultorio para reutilización en los módulos del dominio.
- Se incorporaron validaciones para usuarios sin rol o sin consultorio asociado.
- Se eliminaron dependencias de identificadores enviados por el Frontend cuando estos pueden derivarse del contexto autenticado.
- La infraestructura quedó preparada para implementar TS-04 (Autorización por dominio).
- La implementación fue validada mediante pruebas automatizadas y pruebas funcionales.

---
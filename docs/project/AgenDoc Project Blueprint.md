# AgenDoc Project Blueprint

> Documento vivo del proyecto. Toda decisión funcional o técnica aprobada deberá quedar registrada en este documento antes de considerarse oficial.

| Campo                | Valor                                        |
|----------------------|----------------------------------------------|
| Proyecto             | AgenDoc                                      |
| Tipo                 | Plataforma SaaS Web + Mobile                 |
| Metodología          | Scrum                                        |
| Blueprint Version    | v1.13                                        |
| Sprint Actual        | Sprint 4                                     |
| Estado               | Sprint 4 en ejecución                        |
| Última actualización | Sprint 4 — Consolidación de la Recepción Digital |

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

## Definición de Consultorio

En AgenDoc, el término **Consultorio** representa la organización prestadora de servicios de salud que utiliza la plataforma.

Este concepto puede corresponder a:

* un médico independiente;
* un consultorio médico;
* una clínica ambulatoria;
* un centro médico;
* un centro odontológico;
* un centro psicológico;
* un centro de fisioterapia;
* otro establecimiento equivalente de atención ambulatoria.

Dentro del modelo funcional y técnico, esta organización se representa mediante la entidad `Clinic`.

El término Consultorio se mantendrá como denominación oficial del dominio durante el MVP, sin limitar la evolución comercial de AgenDoc hacia distintos tipos de organizaciones de salud.

## Definición de Recepción Digital

La **Recepción Digital del Consultorio** es la experiencia pública mediante la cual una persona puede ingresar virtualmente a un consultorio, conocer su identidad, consultar sus especialidades y médicos, revisar horarios disponibles, reservar una primera cita e iniciar sesión.

La Recepción Digital:

* pertenece visual y funcionalmente al consultorio;
* utiliza la infraestructura tecnológica de AgenDoc;
* mantiene el contexto del consultorio durante toda la navegación;
* constituye el principal punto de entrada para nuevos pacientes;
* prioriza la orientación y atención del paciente sobre el contenido comercial.

El término técnico `Portal Público` podrá utilizarse en arquitectura y código. En la experiencia de producto se utilizará preferentemente el concepto **Recepción Digital del Consultorio**.

## Convención de uso: Recepción Digital y Portal Público

La denominación oficial de producto será **Recepción Digital del Consultorio**.

Este término se utilizará en Product Vision, experiencia de usuario, Product Backlog, Roadmap, mensajes al paciente y descripción funcional de las pantallas.

El término **Portal Público** podrá mantenerse como denominación técnica cuando sea necesario describir módulos de código, paquetes, APIs, rutas públicas, configuración de seguridad o infraestructura.

Ambos términos hacen referencia a la misma capacidad. La documentación funcional priorizará **Recepción Digital del Consultorio** para mantener una visión centrada en la experiencia del paciente.

---

# 1. Product Vision

## Visión

AgenDoc es una plataforma SaaS Web y Mobile para la gestión de consultorios y organizaciones de atención ambulatoria.

La plataforma permite administrar médicos, pacientes, agendas y citas médicas mediante una arquitectura centralizada, segura y preparada para crecer de forma incremental.

AgenDoc se desarrolla inicialmente para validar el MVP con un consultorio base. Sin embargo, su diseño funcional y arquitectónico reconoce desde el inicio que cada consultorio constituye un contexto organizacional independiente.

Cada consultorio dispondrá de:

* una identidad pública propia;
* una Recepción Digital;
* médicos y especialidades;
* pacientes;
* agendas médicas;
* citas;
* usuarios asociados a su operación.

AgenDoc proporciona la infraestructura tecnológica, las reglas de negocio, la seguridad y la gestión de citas.

El paciente interactúa principalmente con la identidad del consultorio al que desea acudir.

---

## Propuesta de valor

AgenDoc permite que un consultorio transforme su atención y gestión de citas en una experiencia digital sencilla, organizada y confiable.

Para el consultorio, AgenDoc proporciona:

* administración de médicos;
* gestión de pacientes;
* configuración de agendas;
* control de disponibilidad;
* creación y seguimiento de citas;
* seguridad y separación de acceso;
* una Recepción Digital propia.

Para el paciente, AgenDoc proporciona:

* acceso digital al consultorio;
* consulta de especialidades;
* consulta de especialistas;
* consulta de horarios disponibles;
* reserva de una primera cita;
* registro simplificado;
* acceso posterior a sus citas y perfil.

---

## Modelo SaaS de AgenDoc

AgenDoc se concibe como una plataforma SaaS multi-consultorio.

Cada consultorio representa un tenant funcional independiente dentro de la plataforma.

Esto significa que cada consultorio mantiene su propio contexto de:

* identidad;
* usuarios;
* médicos;
* pacientes;
* agendas;
* citas;
* contenido público.

El paciente, médico o recepcionista siempre interactúa dentro del contexto de un consultorio determinado.

Durante el MVP se operará inicialmente con un consultorio base, pero las decisiones de producto, dominio y arquitectura deberán evitar dependencias que obliguen a rediseñar el sistema al incorporar nuevos consultorios.

La evolución hacia múltiples consultorios no deberá modificar la responsabilidad central de las entidades del dominio ni la experiencia principal del paciente.

---

## Separación entre AgenDoc y el consultorio

AgenDoc es la plataforma tecnológica.

El consultorio es la organización visible para el paciente.

En la experiencia pública, la identidad principal corresponderá al consultorio.

Por ejemplo, una persona podría ingresar a la Recepción Digital de:

```text
Centro Médico Santa Isabel
```

y visualizar:

* nombre del consultorio;
* logo;
* especialidades;
* médicos;
* dirección;
* teléfono;
* WhatsApp;
* horarios;
* acciones para reservar o iniciar sesión.

La persona no deberá percibir que está navegando en una página genérica de AgenDoc.

La marca AgenDoc podrá aparecer de forma secundaria y discreta como proveedor de la infraestructura tecnológica, sin competir con la identidad del consultorio.

---

## Principios del Producto

Los siguientes principios guiarán todas las decisiones funcionales, técnicas y de experiencia de usuario durante el desarrollo de AgenDoc.

### 1. Simplicidad primero

Cada funcionalidad debe ser intuitiva y fácil de utilizar.

Si una característica requiere una explicación extensa para el usuario, deberá evaluarse una alternativa más simple.

Las tareas frecuentes deberán poder completarse con la menor cantidad razonable de pasos.

---

### 2. Construir únicamente lo necesario

El MVP debe resolver el problema principal del consultorio sin incorporar funcionalidades que no aporten valor inmediato.

Toda nueva funcionalidad deberá justificar claramente su beneficio para el paciente o para la operación del consultorio.

---

### 3. Escalabilidad desde el diseño

Aunque el MVP comenzará con un consultorio base, las decisiones de dominio y arquitectura deberán permitir incorporar nuevos consultorios sin rediseñar el producto.

La escalabilidad no deberá generar sobrearquitectura ni complejidad innecesaria durante el MVP.

---

### 4. El Consultorio es el centro del dominio

Todas las funcionalidades de AgenDoc se ejecutan dentro del contexto de un consultorio.

El consultorio es la unidad organizacional principal del sistema y agrupa:

* usuarios;
* médicos;
* pacientes;
* agendas;
* citas;
* identidad pública.

El paciente no deberá seleccionar manualmente un identificador interno de consultorio cuando el contexto pueda resolverse desde la navegación pública o la sesión autenticada.

---

### 5. La cita médica es el núcleo del negocio

El objetivo funcional principal de AgenDoc es gestionar eficientemente el ciclo de vida de las citas médicas.

Las nuevas funcionalidades deberán fortalecer este proceso o integrarse de forma natural con él.

---

### 6. Una única fuente de verdad

Las reglas de negocio pertenecen al Backend.

Cada dato tendrá un propietario definido dentro del dominio.

El Frontend no deberá reconstruir reglas críticas ni proporcionar identificadores internos cuando estos puedan derivarse del contexto del usuario o del consultorio.

---

### 7. Identidad propia para cada consultorio

Cada consultorio contará con una presencia pública propia dentro de AgenDoc.

La experiencia pública utilizará:

* nombre del consultorio;
* identidad visual;
* información de contacto;
* especialidades;
* médicos;
* disponibilidad.

AgenDoc mantendrá la estructura, seguridad, navegación y consistencia del producto, mientras que el consultorio proporcionará su identidad y contenido público.

---

### 8. La Recepción Digital es el principal punto de entrada

La experiencia pública no será una landing genérica de AgenDoc.

Será la Recepción Digital del consultorio.

Su finalidad será orientar al paciente desde el primer momento y facilitar acciones concretas:

* reservar una cita;
* conocer especialistas;
* consultar horarios;
* iniciar sesión;
* contactar al consultorio;
* consultar su ubicación.

La Recepción Digital priorizará la utilidad y la atención sobre el contenido promocional.

---

### 9. El paciente inicia su relación desde Internet

El primer contacto entre un nuevo paciente y el consultorio podrá realizarse completamente desde la Recepción Digital.

El interesado podrá:

1. ingresar al contexto público del consultorio;
2. consultar sus especialidades;
3. conocer a sus médicos;
4. seleccionar una fecha;
5. seleccionar un horario disponible;
6. proporcionar información mínima;
7. confirmar su primera cita.

La persona podrá explorar la disponibilidad antes de proporcionar sus datos personales.

---

### 10. Registro ultrarrápido

El registro inicial deberá solicitar únicamente la información necesaria para crear la cuenta, identificar al paciente y confirmar la primera cita.

Durante el MVP, el registro inicial solicitará:

* nombre o nombres;
* apellido o apellidos;
* correo electrónico;
* celular;
* contraseña;
* confirmación de contraseña.

No se solicitarán durante esta etapa:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional del perfil.

Estos datos podrán completarse posteriormente.

---

### 11. Perfil progresivo del paciente

El proceso de incorporación del paciente se dividirá en dos momentos.

#### Primera etapa: reserva

El paciente proporciona información mínima y confirma su primera cita.

Como resultado, AgenDoc crea automáticamente:

* la cuenta de usuario;
* el registro de paciente;
* la asociación entre ambos;
* la asociación con el consultorio;
* la primera cita en estado Programada.

#### Segunda etapa: completar perfil

Después de reservar, el sistema invitará al paciente a completar:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional.

La falta de estos datos no impedirá la creación de la primera cita.

Después de confirmar la reserva, el sistema podrá mostrar el siguiente mensaje:

> Tu cita ya está confirmada. Antes del día de tu consulta, por favor completa tu perfil para agilizar tu atención.

El paciente podrá completar el perfil inmediatamente o hacerlo posteriormente desde su cuenta.

---

### 12. Correo electrónico como identificador visible

El paciente utilizará su correo electrónico para iniciar sesión.

La interfaz no solicitará ni mostrará un nombre de usuario independiente.

Cuando la implementación mantenga internamente un campo `username`, este será gestionado automáticamente por AgenDoc y permanecerá oculto para el paciente.

El correo deberá normalizarse antes de utilizarse como identificador de acceso.

Como mínimo se aplicará:

* eliminación de espacios iniciales y finales;
* conversión a minúsculas.

La autenticación deberá validar el correo dentro del contexto del consultorio correspondiente.

---

### 13. Contexto implícito del consultorio

El paciente siempre navegará dentro del contexto de un consultorio.

La Recepción Digital resolverá ese contexto mediante un identificador público, como un `slug`.

Ejemplos:

```text
santa-isabel
clinica-roma
odontokids
dermacenter
```

La experiencia pública podrá representarse inicialmente mediante rutas como:

```text
agendoc.com/santa-isabel
agendoc.com/clinica-roma
```

En una evolución futura podrá soportar subdominios o dominios personalizados:

```text
santa-isabel.agendoc.com
```

El identificador interno del consultorio no deberá ser seleccionado ni conocido por el paciente.

---

### 14. Seguridad desde el inicio

La autenticación, autorización y protección de datos se considerarán requisitos fundamentales.

El Backend deberá:

* resolver el consultorio desde un contexto confiable;
* impedir acceso entre consultorios;
* validar pertenencia de médicos, agendas y citas;
* proteger información personal;
* evitar exposición de identificadores internos innecesarios;
* mantener reglas uniformes para accesos públicos y autenticados.

---

### 15. Consistencia Web y Mobile

La aplicación Web y Mobile compartirán las mismas reglas de negocio y contratos del Backend.

Las diferencias entre plataformas corresponderán únicamente a presentación, navegación y capacidades propias del dispositivo.

---

### 16. Evolución incremental

AgenDoc crecerá mediante incrementos funcionales pequeños.

Cada Sprint deberá entregar valor real, mantener el producto potencialmente desplegable y evitar ampliar el alcance sin una decisión aprobada.

---

## Recepción Digital del Consultorio

La Recepción Digital constituye la presencia pública de cada consultorio dentro de AgenDoc.

Representa una extensión digital de la recepción física y permite que una persona se oriente, conozca la oferta médica e inicie su relación con el consultorio.

La Recepción Digital podrá mostrar:

* identidad del consultorio;
* mensaje de bienvenida;
* especialidades;
* médicos;
* horarios disponibles;
* dirección;
* mapa;
* teléfono;
* WhatsApp;
* correo de contacto;
* acceso para reservar;
* acceso para iniciar sesión.

Su estructura deberá priorizar acciones prácticas.

Ejemplo conceptual:

```text
Centro Médico Santa Isabel

Bienvenido.
¿Cómo podemos ayudarte hoy?

Reservar una cita
Conocer nuestros especialistas
Consultar horarios
Iniciar sesión

Dirección
Mapa
Teléfono
WhatsApp
```

La Recepción Digital no será un constructor genérico de páginas durante el MVP.

AgenDoc proporcionará una plantilla consistente, accesible y responsive. Cada consultorio aportará su identidad y contenido público dentro de los campos habilitados por la plataforma.

---

## Identidad pública del consultorio

Cada consultorio contará con un identificador público único denominado `slug`.

El `slug` permitirá resolver el consultorio desde una URL pública sin exponer su identificador interno.

Ejemplo:

```text
slug: santa-isabel
```

URL pública:

```text
agendoc.com/santa-isabel
```

Flujo conceptual:

```text
Solicitud pública
        │
        ▼
Resolver slug
        │
        ▼
Identificar consultorio
        │
        ├── cargar identidad
        ├── cargar especialidades
        ├── cargar médicos
        ├── cargar información de contacto
        └── cargar disponibilidad
```

El `clinicId` permanecerá como identificador interno.

No deberá ser proporcionado libremente por el paciente ni utilizarse como elemento visible de navegación.

---

## Modelo de incorporación de pacientes

AgenDoc distingue dos situaciones principales.

### Paciente existente

Es una persona que ya cuenta con un registro asociado al consultorio.

Puede:

* iniciar sesión;
* consultar sus citas;
* reservar nuevas citas;
* cancelar citas permitidas;
* completar o actualizar su perfil;
* utilizar las funcionalidades habilitadas para pacientes.

### Nuevo interesado

Es una persona que todavía no pertenece al consultorio.

Puede:

* acceder a la Recepción Digital;
* conocer el consultorio;
* consultar especialidades;
* consultar médicos;
* revisar disponibilidad;
* seleccionar una primera cita;
* registrarse mediante un formulario ultrarrápido.

Al confirmar la reserva, AgenDoc deberá crear en una sola operación funcional:

* Usuario;
* Paciente;
* asociación Usuario-Paciente;
* asociación con el Consultorio;
* primera Cita Médica en estado Programada.

Si la operación no puede completarse, no deberán quedar usuarios, pacientes, citas o reservas parciales.

---

## Evolución del perfil del paciente

El perfil inicial podrá encontrarse incompleto después del registro ultrarrápido.

Los datos pendientes deberán representarse como información todavía no proporcionada y no mediante valores ficticios.

No deberán utilizarse datos artificiales como:

* documentos genéricos;
* fechas de nacimiento ficticias;
* direcciones inexistentes;
* textos como `PENDIENTE` almacenados como información real.

Los campos no proporcionados deberán admitir ausencia de valor hasta que el paciente complete su perfil.

En futuras iteraciones podrán incorporarse:

* seguro médico;
* contacto de emergencia;
* alergias;
* antecedentes;
* medicamentos.

Estas capacidades no forman parte del alcance actual de HU-20 y deberán evaluarse separadamente por su sensibilidad y relación con información médica.

---

## Evolución del modelo de consultorio

El MVP operará inicialmente con un consultorio base.

La arquitectura permanecerá preparada para incorporar múltiples consultorios, cada uno con:

* slug propio;
* identidad pública;
* Recepción Digital;
* médicos;
* pacientes;
* usuarios;
* agendas;
* citas.

Durante el MVP, cada perfil de paciente pertenecerá a un solo consultorio.

La posibilidad de que una misma identidad de paciente participe en varios consultorios no será resuelta durante esta etapa.

Cuando el producto requiera esa capacidad, deberá evaluarse una evolución específica del modelo de identidad y asociación entre pacientes y consultorios.

Esta restricción mantiene el MVP simple y no modifica la separación funcional entre tenants.

---

## Objetivo del MVP

Permitir que un consultorio gestione completamente su agenda médica mediante una plataforma moderna, sencilla y eficiente.

El MVP se centrará en la gestión de citas médicas.

---

# 2. MVP Scope

## Roles

### Paciente

- Registrarse desde la Recepción Digital
- Iniciar sesión
- Completar su perfil
- Consultar médicos
- Consultar disponibilidad
- Reservar cita
- Visualizar sus citas
- Cancelar sus citas

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

- Recepción Digital del Consultorio
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

## Principios de contexto del consultorio

Toda interacción con AgenDoc ocurre dentro del contexto de un Consultorio.

El contexto podrá resolverse de dos maneras:

* mediante un identificador público durante la navegación en la Recepción Digital;
* mediante el usuario autenticado durante la operación interna de la plataforma.

En la experiencia pública, el Consultorio se resolverá utilizando un identificador público único denominado `slug`.

En la experiencia autenticada, el Consultorio se resolverá mediante el usuario y el contexto de seguridad.

El identificador interno del Consultorio no será utilizado como elemento visible de navegación y no deberá ser seleccionado libremente por el paciente.

## Tenant funcional

Cada Consultorio representa un tenant funcional independiente dentro de AgenDoc.

Un tenant agrupa y delimita:

* identidad pública;
* usuarios;
* médicos;
* pacientes;
* agendas;
* bloques de agenda;
* citas;
* información de contacto.

La separación entre tenants deberá validarse en el Backend.

La navegación pública, la autenticación y las operaciones del dominio deberán preservar permanentemente el contexto del Consultorio correspondiente.

## Identidad pública del Consultorio

Cada Consultorio tendrá una identidad pública propia.

La identidad pública permitirá presentar la Recepción Digital con información perteneciente al Consultorio.

Podrá incluir:

* nombre público;
* descripción;
* logotipo;
* teléfono;
* WhatsApp;
* correo de contacto;
* dirección;
* ubicación o mapa;
* horarios generales;
* estado de habilitación del portal.

La identidad pública no modifica la responsabilidad organizacional de la entidad Consultorio.

Forma parte del mismo agregado y representa su presencia externa dentro de AgenDoc.

---

## Entidades principales

### Consultorio

Representa la organización prestadora de servicios de salud que utiliza AgenDoc y constituye la unidad organizacional principal del dominio.

Dentro de la arquitectura SaaS, cada Consultorio representa un tenant funcional independiente.

Responsabilidades:

* Agrupar usuarios, médicos, recepcionistas, pacientes, agendas y citas.
* Definir el contexto donde ocurre la atención médica.
* Mantener su identidad pública.
* Proporcionar el contexto de navegación de la Recepción Digital.
* Determinar el contexto de registro de nuevos pacientes.
* Determinar el contexto de autenticación de sus usuarios.
* Delimitar la información pública y privada de su operación.
* Mantener un identificador público único.
* Actuar como agregado organizacional principal de AgenDoc.

Relaciones:

* Tiene una Recepción Digital.
* Tiene una identidad pública.
* Tiene un identificador público único.
* Tiene usuarios.
* Tiene médicos.
* Tiene recepcionistas.
* Tiene pacientes.
* Tiene agendas médicas.
* Tiene bloques de agenda.
* Tiene citas médicas.

Reglas:

* Todo Consultorio deberá tener un identificador interno.
* Todo Consultorio deberá tener un `slug` público único.
* El `slug` deberá permitir resolver el Consultorio sin exponer su identificador interno.
* La Recepción Digital solo estará disponible cuando el Consultorio se encuentre activo y tenga habilitado su acceso público.
* Todos los recursos del dominio deberán operar dentro del contexto de un Consultorio.
* El identificador interno del Consultorio no será utilizado como elemento visible de navegación.

### Identidad Pública del Consultorio

Representa la información que el Consultorio autoriza mostrar en su Recepción Digital.

No constituye un agregado independiente durante el MVP.

Forma parte de la configuración y responsabilidad del Consultorio.

Información pública inicial:

* nombre público;
* descripción pública;
* logotipo;
* teléfono;
* WhatsApp;
* correo de contacto;
* dirección;
* ubicación o mapa;
* estado de habilitación de la Recepción Digital.

Reglas:

* Solo podrá mostrarse información expresamente definida como pública.
* La ausencia de un dato opcional no deberá impedir el funcionamiento de la Recepción Digital.
* La identidad pública no deberá exponer datos internos, auditoría o información sensible.
* Durante el MVP no existirá un constructor visual de páginas.
* AgenDoc proporcionará una plantilla visual consistente y el Consultorio proporcionará contenido estructurado.

---

### Usuario

Representa la cuenta utilizada para acceder a AgenDoc.

Responsabilidades:

* Autenticarse.
* Mantener un rol.
* Operar dentro del contexto de un Consultorio.
* Acceder únicamente a funcionalidades autorizadas.
* Asociarse con un perfil de negocio cuando corresponda.

Relaciones:

* Pertenece a un Consultorio.
* Tiene un Rol.
* Puede asociarse a un Paciente.
* Puede asociarse a un Médico.
* Puede asociarse a una Recepcionista.

Reglas:

* Todo Usuario deberá pertenecer a un Consultorio.
* Todo Usuario deberá tener un Rol.
* El correo electrónico será el identificador visible de acceso para el paciente.
* La interfaz no solicitará un nombre de usuario independiente al paciente.
* El correo deberá normalizarse antes de almacenarse o compararse.
* El campo técnico `username`, cuando exista, será generado automáticamente.
* El `username` técnico no será visible ni editable por el paciente.
* La autenticación deberá considerar simultáneamente el Consultorio y el correo normalizado.
* La contraseña deberá almacenarse exclusivamente mediante un hash seguro.
* Nunca deberán exponerse contraseñas, hashes o credenciales en respuestas de la API.

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

Representa a la persona que recibe atención médica dentro de un Consultorio.

Responsabilidades:

* Reservar citas.
* Consultar sus citas.
* Cancelar citas permitidas.
* Mantener información básica de identificación y contacto.
* Completar progresivamente su perfil.
* Acceder posteriormente a la plataforma mediante una cuenta de Usuario.

Relaciones:

* Pertenece a un Consultorio.
* Puede tener un Usuario asociado.
* Tiene citas médicas.
* Puede ser registrado por sí mismo desde la Recepción Digital.
* Puede ser registrado por una Recepcionista.

Reglas:

* Todo Paciente deberá pertenecer a un Consultorio.
* Un Paciente creado desde la Recepción Digital deberá asociarse a un Usuario.
* El registro inicial solo requerirá nombres, apellidos, correo y celular.
* Los datos administrativos complementarios podrán permanecer pendientes.
* Los datos pendientes no deberán completarse utilizando valores ficticios.
* Durante el MVP, un perfil de Paciente pertenecerá a un único Consultorio.
* El correo deberá mantenerse alineado con el correo utilizado en la cuenta de acceso.
* Cuando el Paciente complete su documento, deberá validarse su unicidad dentro del Consultorio.

## Incorporación progresiva del Paciente

La incorporación de un nuevo Paciente desde la Recepción Digital se realizará de forma progresiva.

El objetivo inicial no será completar todo el perfil administrativo, sino permitir que la persona reserve su primera cita con la menor fricción posible.

El proceso se dividirá en dos etapas.

### Etapa 1 — Registro ultrarrápido y primera reserva

El nuevo interesado proporcionará únicamente:

* nombre o nombres;
* apellido o apellidos;
* correo electrónico;
* celular;
* contraseña;
* confirmación de contraseña.

Estos datos permitirán:

* crear la cuenta de Usuario;
* crear el registro de Paciente;
* asociar Usuario y Paciente;
* asociar ambos al Consultorio;
* crear la primera Cita Médica;
* permitir el inicio de sesión posterior.

La información administrativa adicional no será obligatoria para completar la primera reserva.

### Etapa 2 — Completar perfil

Después de reservar, el Paciente podrá completar:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional.

La falta de estos datos no invalidará al Paciente ni impedirá la creación de su primera Cita.

El perfil progresivo deberá evitar almacenar valores ficticios para representar información pendiente.

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

## Modelo conceptual del dominio

```text
Consultorio
├── Identidad Pública
├── Recepción Digital
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

La Identidad Pública y la Recepción Digital forman parte del contexto del Consultorio y no representan agregados independientes durante el MVP.

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
- Un Consultorio tiene un identificador público único.
- Un Consultorio tiene una Recepción Digital.
- Un Consultorio mantiene una identidad pública.
- Un Usuario pertenece a un Consultorio.
- Un Usuario puede asociarse a un único perfil de negocio durante el MVP.
- Un Paciente registrado desde la Recepción Digital debe asociarse a un Usuario.
- El Usuario y el Paciente asociados deben pertenecer al mismo Consultorio.
- La primera Cita debe pertenecer al mismo Consultorio que el Usuario, Paciente, Médico, Agenda y Bloque seleccionados.
- El contexto público del Consultorio se resuelve mediante `slug`.
- El contexto autenticado del Consultorio se resuelve mediante SecurityContext.

---

## Reglas de negocio

### Reglas generales

- Toda cita médica debe pertenecer a un consultorio.
- Toda cita médica debe tener un paciente asignado.
- Toda cita médica debe tener un médico asignado.
- Toda cita médica debe tener una fecha, hora de inicio y hora de fin definidas.
- Toda cita médica debe asociarse a un bloque de agenda válido.
- Los usuarios únicamente podrán ejecutar acciones permitidas por su rol y consultorio.

### Contexto público del Consultorio

* Toda Recepción Digital deberá corresponder a un Consultorio activo.
* El Consultorio público deberá resolverse mediante un identificador público único.
* Un Consultorio con su Recepción Digital deshabilitada no deberá exponer información pública.
* El identificador interno del Consultorio no deberá ser solicitado al paciente.
* El paciente no podrá cambiar el contexto del Consultorio durante el proceso de primera reserva.
* Toda especialidad, médico, agenda, bloque y cita utilizados en el flujo público deberán pertenecer al Consultorio resuelto.
* El Backend deberá impedir combinaciones de recursos pertenecientes a consultorios diferentes.
* La ausencia o invalidez del identificador público deberá producir una respuesta de recurso no encontrado.
* El cambio manual de identificadores en una solicitud pública no deberá permitir acceso a información de otro Consultorio.

### Registro público

* El registro público siempre se realizará dentro del contexto de un Consultorio.
* El nuevo Usuario y el nuevo Paciente deberán asociarse al Consultorio resuelto por el identificador público.
* El Consultorio no deberá recibirse como identificador interno proporcionado libremente por el Frontend.
* La cuenta de acceso deberá crearse con rol Paciente.
* El correo electrónico será el identificador visible de acceso.
* La normalización del correo deberá ejecutarse en el Backend.
* El nombre de usuario técnico, cuando exista, deberá generarse automáticamente.
* Usuario, Paciente y primera Cita deberán crearse dentro de una única transacción.
* Si cualquier parte de la operación falla, no deberán persistirse registros parciales.

### Aislamiento entre consultorios

* Un usuario autenticado solo podrá operar dentro del Consultorio asociado a su cuenta.
* Un paciente no podrá consultar ni gestionar información de otro Consultorio.
* Un médico no podrá consultar agendas o citas de otro Consultorio.
* Una recepcionista no podrá operar sobre pacientes, médicos, agendas o citas de otro Consultorio.
* El Backend deberá validar el Consultorio incluso cuando el identificador del recurso sea válido.

### Disponibilidad médica

- La disponibilidad médica es información derivada de los bloques de agenda y de las citas activas.
- No se persistirá una entidad independiente denominada Disponibilidad.
- Solo podrán mostrarse bloques activos y disponibles que pertenezcan al Consultorio resuelto desde el contexto público o autenticado, según corresponda.
- No deberán mostrarse horarios anteriores a la hora actual cuando se consulte la fecha del día.
- Los horarios disponibles deberán mostrarse ordenados cronológicamente.
- Un bloque de agenda podrá asociarse como máximo a una cita activa.
- Un horario ocupado no deberá aparecer como disponible.
- Cuando una cita se cancela, el bloque podrá volver a considerarse disponible siempre que su fecha y hora no hayan transcurrido y no exista otra cita activa asociada.

### Creación de citas

- Toda cita deberá crearse inicialmente en estado Programada.
- Una cita solo podrá crearse sobre un bloque de agenda disponible.
- La creación de una cita deberá impedir dobles reservas, incluso ante solicitudes concurrentes.
- La cita creada por un Paciente autenticado utilizará al Paciente del SecurityContext. La primera reserva pública utilizará al Paciente creado dentro de la misma transacción.
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

### Registro ultrarrápido

* El registro ultrarrápido solo estará disponible dentro de una Recepción Digital válida.
* El Consultorio deberá resolverse mediante el `clinicSlug`.
* El paciente no deberá seleccionar un Consultorio.
* El registro inicial solicitará únicamente nombres, apellidos, correo electrónico, celular y contraseña.
* La confirmación de contraseña será una validación de entrada y no deberá persistirse.
* El correo electrónico deberá normalizarse antes de validarse y almacenarse.
* El celular deberá normalizarse según las reglas definidas por el producto.
* La contraseña deberá cumplir los criterios mínimos de seguridad.
* La contraseña deberá almacenarse exclusivamente mediante hash seguro.
* El Usuario deberá crearse con rol Paciente.
* El `username` técnico deberá generarse automáticamente.
* El `username` no deberá mostrarse ni solicitarse al paciente.
* El Paciente deberá asociarse al Usuario creado.
* Usuario y Paciente deberán pertenecer al mismo Consultorio.
* Los datos no proporcionados durante el registro deberán permanecer sin valor.
* No deberán utilizarse datos ficticios o genéricos para completar campos pendientes.

### Correo electrónico y duplicidad

* El correo electrónico será el identificador visible de acceso.
* El correo deberá compararse después de normalizarse.
* Durante el MVP, la duplicidad deberá evaluarse dentro del contexto del Consultorio.
* Un mismo correo no podrá registrar dos Usuarios Paciente dentro del mismo Consultorio.
* Si el correo ya pertenece a un Paciente del Consultorio, el sistema no deberá crear un nuevo Usuario ni un nuevo Paciente.
* El sistema deberá informar que ya existe una cuenta asociada y orientar al usuario a iniciar sesión.
* El mensaje no deberá exponer información adicional de la cuenta existente.

### Documento de identidad

* El tipo y número de documento no serán obligatorios durante la primera reserva.
* La validación de duplicidad documental se realizará cuando el paciente complete su perfil.
* Cuando se proporcione un documento, su unicidad deberá validarse dentro del Consultorio.
* No deberán generarse documentos temporales ni valores artificiales durante el registro inicial.

## Primera reserva pública

La primera reserva pública representa una única operación funcional mediante la cual un nuevo interesado se convierte en Paciente y obtiene su primera Cita Médica.

La operación deberá incluir:

1. Resolver el Consultorio mediante el `clinicSlug`.
2. Validar que la Recepción Digital esté activa.
3. Validar que el Médico pertenezca al Consultorio.
4. Validar que el Bloque de Agenda pertenezca al Médico y al Consultorio.
5. Validar que el Bloque permanezca disponible.
6. Validar que el Bloque no corresponda a una fecha u hora pasada.
7. Normalizar el correo electrónico.
8. Validar que no exista una cuenta duplicada dentro del Consultorio.
9. Crear el Usuario con rol Paciente.
10. Crear el Paciente.
11. Asociar Usuario, Paciente y Consultorio.
12. Crear la Cita Médica en estado Programada.
13. Asociar la Cita con el Médico y el Bloque seleccionados.
14. Marcar el Bloque como no disponible.
15. Confirmar la transacción.

Toda la operación deberá ejecutarse dentro de una única transacción.

Si alguno de los pasos falla, no deberán quedar persistidos:

* Usuarios parciales;
* Pacientes parciales;
* Citas parciales;
* asociaciones incompletas;
* Bloques ocupados incorrectamente.

---

## Agregados del dominio

### Consultorio

Es el agregado organizacional principal.

Agrupa y delimita:

* identidad pública;
* Recepción Digital;
* usuarios;
* médicos;
* recepcionistas;
* pacientes;
* agendas;
* citas.

El Consultorio constituye el tenant funcional dentro de AgenDoc.

### Cita Médica

Es el agregado transaccional principal.

Centraliza la interacción entre:

* Consultorio;
* Paciente;
* Médico;
* Agenda;
* Bloque;
* Estado;
* Observación médica.

### Primera reserva pública

La primera reserva pública no constituye un agregado independiente.

Representa una operación transaccional que coordina:

* Consultorio;
* Usuario;
* Paciente;
* Médico;
* Bloque de Agenda;
* Cita Médica.

Su consistencia deberá protegerse mediante una única transacción.

---

## Invariantes del dominio

* Todo Consultorio tiene un identificador interno.
* Todo Consultorio público tiene un `slug` único.
* Todo Usuario pertenece a un Consultorio.
* Todo Usuario tiene un Rol.
* Todo Médico pertenece a un Consultorio.
* Todo Paciente pertenece a un Consultorio.
* Toda Agenda Médica pertenece a un Consultorio y a un Médico.
* Todo Bloque pertenece a una Agenda Médica.
* Toda Cita pertenece a un Consultorio.
* Toda Cita tiene un Médico.
* Toda Cita tiene un Paciente.
* La Cita, el Paciente, el Médico, la Agenda y el Bloque deben pertenecer al mismo Consultorio.
* El contexto público del Consultorio debe resolverse desde un `slug` válido.
* El `clinicId` no debe ser seleccionado libremente por el paciente.
* Un correo normalizado no puede crear más de una cuenta de Paciente dentro del mismo Consultorio.
* Un Paciente registrado públicamente debe asociarse a un Usuario del mismo Consultorio.
* Usuario, Paciente y primera Cita deben crearse de manera atómica.
* No pueden existir dos citas activas sobre el mismo Bloque.
* No pueden existir dos citas activas para el mismo Médico en el mismo horario.
* La ausencia de información del perfil progresivo no invalida la primera Cita.
* Una observación médica siempre pertenece a una Cita.
* Los usuarios únicamente pueden ejecutar acciones permitidas por su rol y Consultorio.

---

## Eventos relevantes del dominio

- UsuarioPacienteRegistrado
- PacienteRegistrado
- PrimeraCitaReservada
- CitaProgramada
- CitaReprogramada
- CitaCancelada
- LlegadaPacienteConfirmada
- PacienteNoAsistio
- ObservacionMedicaRegistrada
- CitaAtendida

---

## Unidad transaccional de la primera reserva

La primera reserva pública deberá ejecutarse dentro de una única frontera transaccional en el Backend.

La transacción deberá coordinar:

* validación del Consultorio;
* validación del Médico;
* bloqueo y validación del Bloque;
* validación de correo duplicado;
* creación del Usuario;
* creación del Paciente;
* asociación Usuario-Paciente;
* creación de la Cita;
* actualización de disponibilidad del Bloque.

El orden exacto de ejecución deberá reducir:

* registros parciales;
* ocupación incorrecta de horarios;
* condiciones de carrera;
* cuentas duplicadas;
* inconsistencias entre Usuario y Paciente.

El Bloque deberá bloquearse de forma pesimista o mediante un mecanismo equivalente antes de confirmar la reserva.

La implementación deberá considerar que dos personas pueden intentar reservar el mismo horario simultáneamente.

Solo una operación podrá completarse.

La operación rechazada deberá:

* devolver un conflicto funcional;
* no crear Usuario;
* no crear Paciente;
* no crear Cita;
* no modificar el Bloque.

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
├── Identidad Pública
├── Recepción Digital
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

La Identidad Pública y la Recepción Digital pertenecen conceptualmente al Consultorio. Durante el MVP se implementarán mediante atributos del Consultorio y no como entidades persistentes independientes.

---

## 4.4 Entidades Persistentes

### Consultorio

Representa la unidad organizacional principal y el tenant funcional del sistema.

Atributos principales:

* id
* slug
* nombre
* nombre_publico
* descripcion_publica
* telefono
* whatsapp
* correo
* direccion
* map_url
* logo_url
* portal_publico_activo
* estado_registro

Relaciones:

* Tiene usuarios.
* Tiene médicos.
* Tiene pacientes.
* Tiene recepcionistas.
* Tiene agendas médicas.
* Tiene citas médicas.
* Mantiene su identidad pública.
* Tiene una Recepción Digital.

Reglas de datos:

* `id` será la clave interna.
* `slug` será obligatorio.
* `slug` será único en la plataforma.
* `slug` deberá almacenarse normalizado.
* `slug` utilizará letras minúsculas, números y guiones.
* `slug` no deberá contener espacios.
* `nombre` representa la denominación organizacional.
* `nombre_publico` representa el nombre mostrado en la Recepción Digital.
* Cuando `nombre_publico` no se haya configurado, podrá utilizarse `nombre`.
* `portal_publico_activo` controlará la disponibilidad de la Recepción Digital.
* Los atributos de identidad pública, salvo el nombre, podrán ser opcionales durante el MVP.
* El `id` no deberá utilizarse como identificador público de navegación.

### Usuario

Representa la cuenta de acceso al sistema.

Atributos principales:

* id
* consultorio_id
* rol_id
* username
* email
* password_hash
* activo
* ultimo_acceso
* estado_registro

Relaciones:

* Pertenece a un Consultorio.
* Tiene un Rol.
* Puede asociarse a un perfil de negocio.

Reglas de datos:

* `consultorio_id` será obligatorio.
* `rol_id` será obligatorio.
* `email` será obligatorio.
* `email` deberá almacenarse normalizado.
* `username` continuará existiendo como identificador técnico durante el MVP.
* `username` será generado por el Backend.
* `username` no será solicitado en la interfaz del paciente.
* `username` deberá ser único.
* La unicidad funcional de acceso deberá preservar el contexto del Consultorio.
* `password_hash` será obligatorio.
* `password_hash` nunca será devuelto mediante APIs.
* `activo` determinará si el Usuario puede autenticarse.

Construcción conceptual recomendada para el `username` técnico:

```text
{clinicSlug}:{emailNormalizado}
```

Ejemplo:

```text
santa-isabel:paciente@email.com
```

Esta convención permite mantener un identificador técnico globalmente único sin exponerlo al usuario.

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

* id
* consultorio_id
* usuario_id
* nombres
* apellidos
* tipo_documento
* numero_documento
* fecha_nacimiento
* telefono
* correo
* direccion
* informacion_adicional
* estado_registro

Relaciones:

* Pertenece a un Consultorio.
* Puede tener un Usuario asociado.
* Tiene citas médicas.

Campos obligatorios durante el registro inicial:

* consultorio_id;
* usuario_id;
* nombres;
* apellidos;
* telefono;
* correo;
* estado_registro.

Campos opcionales durante el registro inicial:

* tipo_documento;
* numero_documento;
* fecha_nacimiento;
* direccion;
* informacion_adicional.

Reglas de datos:

* `usuario_id` podrá permanecer nulo para pacientes registrados internamente que todavía no tengan acceso a la plataforma.
* `usuario_id` será obligatorio para pacientes creados mediante la Recepción Digital.
* El Usuario y el Paciente asociados deberán pertenecer al mismo Consultorio.
* `correo` deberá almacenarse normalizado.
* `tipo_documento` y `numero_documento` deberán proporcionarse conjuntamente cuando el paciente complete su perfil.
* El documento deberá ser único dentro del Consultorio cuando exista.
* Los campos pendientes deberán almacenarse como `NULL`.
* No deberán utilizarse valores ficticios para satisfacer restricciones técnicas.
* `informacion_adicional` no deberá utilizarse como historia clínica ni como repositorio de información médica estructurada.


## Información adicional del perfil

El atributo `informacion_adicional` permitirá almacenar información administrativa breve proporcionada voluntariamente por el Paciente.

Durante el MVP no deberá utilizarse para representar una historia clínica ni almacenar información médica estructurada.

No formarán parte del registro inicial:

* seguro médico;
* contacto de emergencia;
* alergias;
* antecedentes;
* medicamentos.

Estas capacidades deberán evaluarse en Historias de Usuario posteriores debido a su sensibilidad, reglas de privacidad y posible impacto en el Modelo de Datos.

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

Reglas adicionales para la primera reserva pública:

* `consultorio_id` deberá corresponder al Consultorio resuelto mediante `clinicSlug`.
* `paciente_id` corresponderá al Paciente creado durante la operación.
* `medico_id` deberá pertenecer al Consultorio resuelto.
* `bloque_agenda_id` deberá pertenecer a una Agenda del mismo Consultorio.
* `estado_cita_id` deberá corresponder al estado Programada.
* La Cita deberá crearse dentro de la misma transacción que el Usuario y el Paciente.
* La Cita no deberá persistirse si falla la creación del Usuario o del Paciente.
* El Bloque no deberá quedar ocupado si la creación de la Cita falla.

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

| Relación                                | Cardinalidad   |
| --------------------------------------- | -------------- |
| Consultorio → Identidad Pública         | 1:1            |
| Consultorio → Recepción Digital         | 1:1 conceptual |
| Consultorio → Usuario                   | 1:N            |
| Consultorio → Paciente                  | 1:N            |
| Consultorio → Médico                    | 1:N            |
| Consultorio → Recepcionista             | 1:N            |
| Consultorio → Agenda Médica             | 1:N            |
| Consultorio → Cita Médica               | 1:N            |
| Usuario → Rol                           | N:1            |
| Usuario → Paciente                      | 1:0..1         |
| Usuario → Médico                        | 1:0..1         |
| Usuario → Recepcionista                 | 1:0..1         |
| Paciente → Cita Médica                  | 1:N            |
| Médico → Agenda Médica                  | 1:1            |
| Agenda Médica → Bloque de Agenda        | 1:N            |
| Bloque de Agenda → Cita Médica          | 1:0..N         |
| Estado de Cita → Cita Médica            | 1:N            |
| Cita Médica → Observación Médica Básica | 1:0..1         |

La Recepción Digital y la Identidad Pública permanecen conceptualmente asociadas uno a uno con el Consultorio, pero durante el MVP podrán implementarse mediante atributos de la misma entidad persistente.

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

* Todo Consultorio deberá tener un `slug` único.
* El `slug` deberá identificar públicamente al Consultorio sin exponer su clave interna.
* Todo Usuario deberá pertenecer a un Consultorio.
* El correo electrónico deberá normalizarse antes de validar duplicidad.
* La unicidad de acceso deberá considerar el contexto del Consultorio.
* Un nuevo Paciente registrado públicamente deberá pertenecer al mismo Consultorio que su Usuario.
* Una primera Cita deberá pertenecer al mismo Consultorio que el Usuario, Paciente, Médico, Agenda y Bloque seleccionados.
* No podrá crearse una primera Cita utilizando recursos de consultorios diferentes.
* Los campos del perfil progresivo podrán permanecer nulos hasta que el paciente complete su información.
* No deberán almacenarse valores ficticios para representar datos pendientes.
* Un Consultorio con el portal público deshabilitado no deberá admitir nuevas reservas públicas.

## Reglas de integridad multi-consultorio

* Todo Consultorio deberá tener un `slug`.
* El `slug` deberá ser único globalmente.
* El `slug` deberá almacenarse en minúsculas.
* Un Usuario deberá pertenecer a un Consultorio.
* Un Paciente deberá pertenecer a un Consultorio.
* Un Médico deberá pertenecer a un Consultorio.
* Una Agenda deberá pertenecer al mismo Consultorio que su Médico.
* Una Cita deberá pertenecer al mismo Consultorio que su Paciente, Médico y Agenda.
* Un Usuario y su perfil de negocio deberán pertenecer al mismo Consultorio.
* Un correo normalizado no podrá repetirse para cuentas de Paciente dentro del mismo Consultorio.
* Un documento, cuando exista, no podrá repetirse dentro del mismo Consultorio.
* La ausencia de documento no deberá impedir el registro inicial.
* Un Consultorio con `portal_publico_activo = false` no deberá aceptar reservas públicas.
* Un `clinicSlug` válido no autoriza el acceso a datos privados.
* Todos los recursos utilizados en una operación pública deberán validarse contra el Consultorio resuelto.

## Reglas de integridad de primera reserva

* La primera reserva deberá crear Usuario, Paciente y Cita en una única transacción.
* La transacción deberá bloquear el Bloque de Agenda antes de confirmar su disponibilidad.
* El Bloque solo podrá quedar no disponible cuando la Cita haya sido creada exitosamente.
* Si existe un conflicto de correo, no deberá crearse ningún registro.
* Si existe un conflicto de disponibilidad, no deberá crearse ningún registro.
* Si falla la persistencia de la Cita, deberán revertirse Usuario y Paciente.
* Si falla la persistencia del Usuario o Paciente, el Bloque deberá permanecer disponible.

---

## Duplicidad de cuentas durante el MVP

La duplicidad del correo se evaluará dentro del contexto del Consultorio.

Regla:

```text
Consultorio + correo normalizado = cuenta única
```

Esto permite mantener aislamiento funcional entre Consultorios.

Durante el MVP no se implementará una identidad global de persona.

Por tanto, una misma dirección de correo podría representar cuentas independientes en Consultorios distintos, siempre que la estrategia física de autenticación y `username` lo permita.

Dentro de un mismo Consultorio:

* el correo no podrá registrarse dos veces;
* el sistema no deberá crear un nuevo Paciente;
* el sistema deberá orientar al usuario a iniciar sesión.

La duplicidad documental se validará cuando el Paciente complete el perfil.

Regla:

```text
Consultorio + tipo de documento + número de documento = Paciente único
```

Los valores nulos del documento no deberán impedir múltiples perfiles incompletos, pero una vez proporcionado el documento deberá aplicarse la restricción correspondiente.

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

## Normalización de identificadores públicos y datos de acceso

### Slug

Antes de persistirse:

* deberá eliminar espacios iniciales y finales;
* deberá convertirse a minúsculas;
* deberá reemplazar espacios internos por guiones cuando corresponda;
* deberá rechazar caracteres no permitidos;
* deberá validarse su unicidad.

Expresión conceptual:

```text
^[a-z0-9]+(?:-[a-z0-9]+)*$
```

### Correo electrónico

Antes de persistirse o compararse:

* deberá eliminar espacios iniciales y finales;
* deberá convertirse a minúsculas.

No se aplicarán transformaciones específicas de proveedores de correo, como eliminar puntos o etiquetas, durante el MVP.

### Username técnico

Se generará exclusivamente en el Backend.

No deberá aceptarse como valor libre desde el Frontend del paciente.

---

## Estado de implementación del modelo progresivo

El modelo de datos definido para la Recepción Digital representa el objetivo funcional aprobado para HU-20.

Antes de implementar deberá revisarse el esquema físico vigente para determinar las restricciones actuales de nulabilidad, unicidad de correo y documento, asociación entre Usuario y Paciente, estructura del Consultorio y estrategia de generación de `username`.

Los cambios físicos se implementarán mediante nuevas migraciones Flyway. Las migraciones existentes no deberán modificarse.

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

## Contexto público y autenticado

La arquitectura distinguirá dos contextos de acceso.

### Contexto público

Se utiliza durante la navegación de la Recepción Digital.

El Consultorio se resuelve mediante un identificador público.

```text
URL pública
    │
    ▼
clinicSlug
    │
    ▼
Backend
    │
    ▼
ClinicRepository
    │
    ▼
Consultorio
```

Una vez resuelto el Consultorio, el Backend limita todas las consultas públicas a dicho contexto.

### Contexto autenticado

Se utiliza después del inicio de sesión.

El Consultorio se resuelve mediante el Usuario autenticado y el SecurityContext.

```text
JWT
    │
    ▼
SecurityContext
    │
    ▼
Usuario autenticado
    │
    ▼
Consultorio
```

El contexto público no reemplaza el contexto autenticado.

El `slug` permite identificar el Consultorio antes de la autenticación. Después del login, el Backend utiliza el Consultorio asociado al Usuario como fuente de verdad.

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
- Consultorio por slug.
- Consultorio activo por slug.
- Usuario por consultorio y correo normalizado.
- Médico activo por consultorio.
- Especialidades disponibles por consultorio.
- Bloques disponibles por consultorio, médico y fecha.

---

## Índices y restricciones conceptuales

La implementación deberá considerar como mínimo los siguientes índices y restricciones.

### Consultorio

* índice único sobre `slug`;
* índice sobre `portal_publico_activo`;
* índice combinado sobre `slug`, `estado_registro` y `portal_publico_activo` cuando mejore la consulta pública.

### Usuario

* índice único sobre `username`;
* índice sobre `consultorio_id`;
* índice sobre `email`;
* restricción o índice único funcional sobre `consultorio_id` y correo normalizado, según las capacidades de PostgreSQL y la estrategia de implementación aprobada.

### Paciente

* índice sobre `consultorio_id`;
* índice sobre `usuario_id`;
* índice sobre `correo`;
* índice para búsqueda por nombres y apellidos;
* restricción de unicidad sobre `consultorio_id`, `tipo_documento` y `numero_documento` cuando el documento exista.

### Médico

* índice sobre `consultorio_id`;
* índice sobre `especialidad_id`;
* índice combinado sobre `consultorio_id`, `especialidad_id` y `estado_registro`.

### Agenda y Bloques

* índice sobre `consultorio_id` y `medico_id`;
* índice sobre fecha y disponibilidad;
* índice combinado para consultar bloques disponibles por Consultorio, Médico y fecha.

### Citas

* índice sobre `consultorio_id`;
* índice sobre `paciente_id`;
* índice sobre `medico_id`;
* índice sobre `bloque_agenda_id`;
* índice sobre fecha y estado;
* restricciones que protejan la asociación de un Bloque con una única Cita activa cuando corresponda.

---

## Evolución conceptual del esquema

La implementación de HU-20 podrá requerir nuevas migraciones Flyway.

Los cambios conceptuales esperados son:

1. Incorporar `slug` al Consultorio.
2. Incorporar atributos de identidad pública.
3. Incorporar `portal_publico_activo`.
4. Permitir valores nulos en campos administrativos del Paciente.
5. Incorporar `informacion_adicional` cuando se apruebe como parte del perfil.
6. Ajustar restricciones de unicidad de Usuario y Paciente.
7. Crear índices para consultas públicas por Consultorio.
8. Actualizar datos iniciales del Consultorio base.

Las migraciones existentes no deberán modificarse.

Toda evolución deberá incorporarse mediante nuevas migraciones versionadas.

Antes de definir la migración exacta deberá revisarse el esquema físico implementado y las restricciones vigentes.

---

## Configuración inicial del Consultorio base

El Consultorio base utilizado durante el MVP deberá contar con:

* `slug` válido;
* nombre público;
* Recepción Digital activa;
* información de contacto mínima;
* médicos activos;
* especialidades asociadas;
* agendas y bloques disponibles.

Ejemplo conceptual:

```text
slug: santa-isabel
nombre_publico: Centro Médico Santa Isabel
portal_publico_activo: true
```

Los valores definitivos deberán corresponder a los datos utilizados en el entorno de desarrollo.

No deberán introducirse datos comerciales ficticios en ambientes productivos.

---

## Clasificación inicial de datos

### Datos públicos del Consultorio

* slug;
* nombre público;
* descripción pública;
* logotipo;
* teléfono público;
* WhatsApp público;
* correo público;
* dirección pública;
* mapa;
* especialidades publicadas;
* médicos publicados;
* disponibilidad.

### Datos privados del Consultorio

* identificador interno;
* usuarios;
* pacientes;
* citas;
* auditoría;
* configuraciones sensibles;
* credenciales;
* información administrativa interna.

### Datos públicos del Médico

* nombre;
* especialidad;
* información profesional aprobada;
* disponibilidad.

### Datos privados del Médico

* documento;
* correo privado;
* teléfono personal;
* usuario;
* auditoría;
* datos internos no aprobados para publicación.

### Datos privados del Paciente

Toda la información del Paciente será privada.

La Recepción Digital no deberá exponer datos de pacientes existentes.

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

## Contraseña durante el registro público

La contraseña será creada durante el registro ultrarrápido.

La interfaz solicitará:

* contraseña;
* confirmación de contraseña.

La confirmación de contraseña:

* será validada antes de enviar o procesar la solicitud;
* no formará parte de la entidad Usuario;
* no deberá persistirse;
* no deberá registrarse en logs.

El Backend deberá:

* validar que contraseña y confirmación coincidan;
* aplicar la política mínima de seguridad;
* generar el hash mediante el componente oficial de contraseñas;
* descartar el valor original después de procesarlo;
* evitar incluir la contraseña en respuestas, excepciones o trazas.

La respuesta de la primera reserva no deberá devolver:

* contraseña;
* confirmación de contraseña;
* hash;
* token de autenticación, salvo que una decisión posterior apruebe inicio de sesión automático.

## Inicio de sesión posterior a la primera reserva

Después de completar la primera reserva, el Paciente podrá iniciar sesión utilizando:

* el contexto del Consultorio;
* su correo electrónico;
* su contraseña.

Durante el MVP, la creación de la primera reserva no iniciará sesión automáticamente.

El sistema mostrará una confirmación y ofrecerá al Paciente:

* completar su perfil;
* iniciar sesión;
* volver a la Recepción Digital.

El inicio de sesión automático podrá evaluarse posteriormente, pero no formará parte de HU-20 mientras no exista una decisión aprobada.

## Autenticación contextualizada por Consultorio

El inicio de sesión deberá ejecutarse dentro del contexto de un Consultorio.

La ruta pública permitirá resolver el Consultorio antes de enviar las credenciales.

Ejemplo:

```text
/santa-isabel/login
```

El usuario proporcionará:

* correo electrónico;
* contraseña.

El Backend deberá:

1. resolver el Consultorio mediante el `slug`;
2. normalizar el correo;
3. localizar al Usuario dentro del Consultorio;
4. validar la contraseña;
5. generar el JWT;
6. incluir el contexto interno necesario para las operaciones protegidas.

Después de la autenticación, el Consultorio asociado al Usuario será la fuente de verdad.

El Backend deberá rechazar el inicio de sesión cuando:

* el Consultorio no exista;
* la cuenta no pertenezca al Consultorio;
* la cuenta esté inactiva;
* las credenciales sean inválidas.

La respuesta no deberá revelar cuál de estos elementos produjo el fallo.

El mensaje visible será uniforme y no expondrá información sensible.

## Identificador visible de acceso

El correo electrónico será el único identificador de acceso solicitado al paciente.

AgenDoc podrá mantener internamente un `username` técnico para conservar compatibilidad con la implementación existente.

El `username`:

* será generado automáticamente;
* no será solicitado al usuario;
* no será mostrado en la interfaz;
* podrá incorporar el contexto del Consultorio;
* no reemplazará al correo como identificador visible.

## Seguridad del contexto público

Las rutas públicas deberán exponer únicamente información necesaria para la Recepción Digital.

No deberán exponer:

* documentos de identidad;
* datos privados de pacientes;
* correos privados de médicos;
* teléfonos personales no publicados;
* agendas completas;
* citas;
* identificadores internos del Consultorio;
* hashes;
* tokens;
* información de auditoría.

Las respuestas públicas deberán utilizar DTOs específicos y no serializar directamente las entidades persistentes.

---

## Convención del username técnico

El `username` técnico será una decisión interna de implementación.

No deberá aparecer en:

* formularios;
* mensajes;
* correos;
* pantallas de perfil;
* respuestas públicas;
* documentación para pacientes.

La convención conceptual será:

```text
{clinicSlug}:{emailNormalizado}
```

Ejemplo:

```text
santa-isabel:paciente@email.com
```

El Backend será responsable de:

* generar el valor;
* validar su unicidad;
* impedir que el Frontend lo proporcione;
* mantenerlo sincronizado con la estrategia de autenticación.

El correo electrónico seguirá siendo la credencial visible.

Una futura modificación del correo deberá considerar el impacto sobre el `username` técnico y la autenticación. La actualización de correo no forma parte de HU-20.

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

## Límites arquitectónicos de la Recepción Digital durante el MVP

La implementación de la Recepción Digital deberá mantenerse dentro de los límites aprobados.

### Incluido

* Resolución del Consultorio mediante `slug`.
* Identidad pública básica.
* Consulta de especialidades.
* Consulta de Médicos.
* Consulta de disponibilidad.
* Registro ultrarrápido.
* Creación automática de Usuario.
* Creación automática de Paciente.
* Creación de la primera Cita.
* Confirmación de reserva.
* Invitación para completar perfil.
* Login contextualizado.
* Aislamiento por Consultorio.
* Diseño responsive.
* Accesibilidad básica WCAG AA.

### No incluido

* Marketplace de Consultorios.
* Búsqueda global de Consultorios.
* Ranking o reseñas.
* Publicidad.
* Pagos.
* Seguros Médicos.
* Dominios personalizados.
* Subdominios.
* Constructor de landing.
* Personalización total de colores.
* Editor de contenido.
* Gestión pública de promociones.
* Integración con WhatsApp.
* Notificaciones automáticas.
* Recuperación completa de contraseña.
* Inicio de sesión automático después del registro.
* Una identidad de Paciente compartida entre varios Consultorios.
* Historia clínica.
* Información médica avanzada.

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

## Diagrama de contexto público

```text
Persona
   │
   ▼
Recepción Digital
/{clinicSlug}
   │
   ▼
Frontend Web
   │
   │ REST / HTTPS
   ▼
Public Portal API
   │
   ▼
Tenant Resolver
   │
   ▼
Consultorio
   │
   ├── Identidad pública
   ├── Especialidades
   ├── Médicos
   ├── Agenda
   └── Primera reserva
   │
   ▼
PostgreSQL
```

## Diagrama de contexto autenticado

```text
Paciente / Médico / Recepcionista
   │
   ▼
/{clinicSlug}/login
   │
   ▼
Authentication API
   │
   ▼
JWT
   │
   ▼
SecurityContext
   │
   ▼
Consultorio del Usuario
   │
   ▼
Servicios protegidos
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

### Recepción Digital y contexto multi-consultorio
- AgenDoc se concibe como una plataforma SaaS multi-consultorio.
- Cada Consultorio representa un tenant funcional.
- El MVP operará inicialmente con un Consultorio base.
- Cada Consultorio tendrá un `slug` público único.
- Durante el MVP se utilizarán rutas basadas en `slug`.
- El `clinicId` permanecerá como identificador interno.
- El paciente no seleccionará ni enviará libremente el `clinicId`.
- El contexto público se resolverá mediante `clinicSlug`.
- El contexto autenticado se resolverá mediante SecurityContext.
- El `clinicSlug` no reemplaza la autorización.
- El Backend validará pertenencia al Consultorio en toda operación.
- La Recepción Digital utilizará la identidad principal del Consultorio.
- AgenDoc aparecerá únicamente como proveedor tecnológico secundario cuando corresponda.
- Las APIs públicas utilizarán DTOs específicos.
- La información pública y privada estará claramente separada.
- El registro inicial solicitará únicamente datos mínimos.
- El perfil del Paciente podrá completarse progresivamente.
- El correo será el identificador visible de acceso.
- El `username` técnico será generado por el Backend y permanecerá oculto.
- La autenticación será contextualizada por Consultorio.
- Usuario, Paciente y primera Cita se crearán en una única transacción.
- La Cita inicial se creará en estado Programada.
- El Bloque se ocupará únicamente cuando la transacción sea exitosa.
- No se utilizarán valores ficticios para completar el perfil.
- La evolución hacia subdominios o dominios propios se postergará.
- La relación de una misma persona con múltiples Consultorios no se resolverá durante el MVP.
- No se implementará un constructor visual de páginas durante HU-20.
- No se incorporará información clínica avanzada al registro público.

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

## 5.15 Arquitectura de la Recepción Digital

La Recepción Digital será implementada como una experiencia pública dentro del Frontend Web.

El Frontend obtendrá el contexto del Consultorio desde la URL y consumirá exclusivamente APIs públicas diseñadas para este propósito.

Flujo general:

```text
Persona
   │
   ▼
/santa-isabel
   │
   ▼
Frontend Web
   │
   ▼
API pública con clinicSlug
   │
   ▼
Resolución del Consultorio
   │
   ├── identidad pública
   ├── especialidades
   ├── médicos
   ├── disponibilidad
   └── primera reserva
```

El Frontend no almacenará un `clinicId` como fuente de verdad.

Podrá mantener temporalmente el `slug` obtenido desde la ruta para realizar las solicitudes públicas.

El Backend deberá resolver el identificador interno del Consultorio y validar todas las relaciones del dominio.

### URLs públicas

Durante el MVP se utilizará una estrategia basada en rutas.

Ejemplo:

```text
/santa-isabel
/santa-isabel/especialidades
/santa-isabel/medicos
/santa-isabel/reservar
/santa-isabel/login
```

Esta estrategia reduce la complejidad inicial de infraestructura.

En una evolución futura podrá utilizarse:

```text
santa-isabel.agendoc.com
```

o un dominio personalizado.

La evolución de rutas a subdominios no deberá modificar las reglas del dominio ni los contratos centrales del Backend.

### Resolución del tenant

La resolución del tenant seguirá este flujo:

1. Recibir el `clinicSlug`.
2. Normalizar el valor.
3. Buscar un Consultorio activo.
4. Validar que la Recepción Digital esté habilitada.
5. Resolver el identificador interno.
6. Ejecutar la operación dentro del contexto resuelto.
7. Rechazar recursos que no pertenezcan al Consultorio.

El `clinicSlug` es un identificador de navegación.

No constituye autorización suficiente por sí mismo.

Todas las operaciones deberán continuar aplicando validaciones de pertenencia y reglas de negocio.

---

## 5.16 Contratos conceptuales de la Recepción Digital

Las APIs públicas utilizarán versionado y una estructura consistente.

Convención propuesta:

```text
/api/v1/public/clinics/{clinicSlug}
```

Operaciones conceptuales:

```http
GET /api/v1/public/clinics/{clinicSlug}
```

Responsabilidad:

* obtener identidad pública;
* obtener información de contacto;
* validar que la Recepción Digital esté activa.

```http
GET /api/v1/public/clinics/{clinicSlug}/specialties
```

Responsabilidad:

* listar especialidades con médicos activos dentro del Consultorio.

```http
GET /api/v1/public/clinics/{clinicSlug}/doctors
```

Responsabilidad:

* listar médicos activos del Consultorio;
* permitir filtro por especialidad cuando corresponda.

```http
GET /api/v1/public/clinics/{clinicSlug}/doctors/{doctorId}/availability
```

Responsabilidad:

* consultar bloques activos y disponibles;
* validar que el Médico pertenezca al Consultorio;
* excluir horarios pasados.

```http
POST /api/v1/public/clinics/{clinicSlug}/first-appointments
```

Responsabilidad:

* registrar al nuevo Usuario;
* crear el Paciente;
* asociar ambos con el Consultorio;
* crear la primera Cita;
* ocupar el Bloque;
* ejecutar toda la operación transaccionalmente.

Los contratos definitivos se establecerán durante el diseño técnico de HU-20.

El Blueprint documenta únicamente sus responsabilidades conceptuales.

### Contrato conceptual de primera reserva

Endpoint conceptual:

```http
POST /api/v1/public/clinics/{clinicSlug}/first-appointments
```

Información de entrada:

* doctorId;
* agendaBlockId;
* nombres;
* apellidos;
* email;
* phone;
* password;
* passwordConfirmation;
* motivo, cuando corresponda.

El contrato no deberá recibir:

* clinicId;
* patientId;
* userId;
* roleId;
* appointmentStatusId;
* username técnico.

El Backend resolverá o generará estos valores.

Información de salida:

* identificador público o funcional de la Cita;
* nombre del Consultorio;
* nombre del Médico;
* especialidad;
* fecha;
* hora de inicio;
* hora de fin;
* estado Programada;
* indicación de perfil pendiente.

El contrato definitivo se establecerá durante el diseño técnico de HU-20.

---

### Respuestas funcionales de primera reserva

La API deberá diferenciar como mínimo:

### Reserva creada

Código HTTP esperado:

```text
201 Created
```

### Datos inválidos

Código HTTP esperado:

```text
400 Bad Request
```

### Consultorio, Médico o recurso público inexistente

Código HTTP esperado:

```text
404 Not Found
```

### Correo ya registrado

Código HTTP esperado:

```text
409 Conflict
```

### Bloque ocupado o modificado durante la reserva

Código HTTP esperado:

```text
409 Conflict
```

### Error inesperado

Código HTTP esperado:

```text
500 Internal Server Error
```

Las respuestas deberán utilizar la estructura uniforme de errores de AgenDoc y no exponer información sensible.

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

## Accesibilidad de la Recepción Digital

La Recepción Digital deberá contemplar:

* jerarquía semántica de encabezados;
* nombre accesible del Consultorio;
* texto alternativo para el logotipo;
* navegación por teclado;
* foco visible;
* orden lógico del foco;
* errores asociados a cada campo;
* mensajes anunciados mediante tecnologías asistivas;
* controles táctiles de tamaño adecuado;
* contraste WCAG AA;
* fechas y horarios comprensibles;
* estados no dependientes únicamente del color.

Las Cards interactivas deberán comportarse como controles accesibles y no como contenedores visuales sin semántica.

Después de un error de formulario:

* el foco deberá dirigirse al resumen de errores o al primer campo inválido;
* el usuario deberá poder identificar qué debe corregir.

Después de una reserva exitosa:

* el foco deberá dirigirse al título de confirmación.

---

### Contexto visible del Consultorio

La persona deberá identificar claramente en todo momento el Consultorio dentro del cual está navegando.

La experiencia pública deberá mostrar de manera consistente:

* nombre del Consultorio;
* logotipo cuando exista;
* información de contacto relevante;
* contexto de la reserva en curso.

El paciente no deberá sentir que fue trasladado a una plataforma genérica o a otro Consultorio durante el proceso.

### Orientación antes que promoción

La Recepción Digital deberá orientar al paciente para completar una acción útil.

Su prioridad será facilitar:

* la reserva de una cita;
* la consulta de especialistas;
* la consulta de disponibilidad;
* el inicio de sesión;
* el contacto con el Consultorio.

El contenido promocional podrá existir de forma breve, pero no deberá desplazar las acciones principales ni dificultar la navegación.

### Progresividad

La experiencia deberá solicitar información únicamente cuando sea necesaria.

El paciente podrá explorar:

* especialidades;
* médicos;
* fechas;
* horarios;

antes de proporcionar información personal.

El registro aparecerá únicamente después de que el paciente haya seleccionado el horario de su primera cita.

### Persistencia del contexto

La selección realizada por el paciente deberá conservarse durante el flujo.

Al avanzar entre pasos, el sistema deberá mantener:

* Consultorio;
* especialidad;
* Médico;
* fecha;
* horario seleccionado.

El usuario no deberá repetir selecciones salvo que decida modificarlas.

### Transparencia de la reserva

Antes de confirmar, el paciente deberá visualizar un resumen claro con:

* nombre del Consultorio;
* especialidad;
* Médico;
* fecha;
* hora;
* datos básicos ingresados.

La confirmación final deberá distinguir claramente entre:

* selección provisional;
* envío en proceso;
* reserva confirmada;
* error de reserva.

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

## Identidad visual en la Recepción Digital

La identidad visual principal de la Recepción Digital corresponderá al Consultorio.

La interfaz podrá mostrar:

* nombre público;
* logotipo;
* descripción breve;
* información de contacto;
* ubicación;
* contenido público aprobado.

AgenDoc conservará el control de:

* estructura de navegación;
* componentes;
* accesibilidad;
* comportamiento responsive;
* jerarquía visual;
* estados;
* consistencia funcional.

Durante el MVP, la identidad del Consultorio no implicará libertad total de personalización.

No se implementará un constructor visual de páginas ni un sistema abierto de temas.

La personalización inicial se limitará a información estructurada y recursos aprobados.

### Jerarquía de marca

La jerarquía visual será:

1. Consultorio.
2. Acción principal del paciente.
3. Información médica y operativa.
4. AgenDoc como proveedor tecnológico secundario.

La marca AgenDoc podrá mostrarse de forma discreta, por ejemplo:

```text
Tecnología proporcionada por AgenDoc
```

No deberá competir visualmente con el nombre o logotipo del Consultorio.

### Uso del logotipo

Cuando el Consultorio tenga logotipo:

* deberá mostrarse con proporciones correctas;
* no deberá deformarse;
* deberá mantener espacio de seguridad;
* deberá contar con texto alternativo;
* no deberá afectar la legibilidad de la navegación.

Cuando no exista logotipo, el nombre público del Consultorio será suficiente para construir la identidad principal.

### Paleta durante el MVP

La Recepción Digital utilizará la paleta oficial de AgenDoc como base.

No se permitirán combinaciones de colores que:

* reduzcan el contraste;
* afecten la accesibilidad;
* modifiquen colores de estados;
* generen inconsistencias entre pantallas.

La personalización avanzada de colores podrá evaluarse en una iteración posterior.

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

## Componentes requeridos por HU-20

HU-20 deberá reutilizar los componentes base existentes:

* AppButton.
* AppCard.
* AppInput.
* AppSelect cuando corresponda.

Durante la implementación se evaluará incorporar:

* AppAlert.
* AppLoader o Skeleton.
* AppEmptyState.
* AppErrorState.
* AppAvatar.
* AppChip o selector de horario.

Un nuevo componente solo deberá crearse cuando:

* exista una necesidad recurrente;
* reduzca duplicación;
* mantenga coherencia visual;
* pueda reutilizarse en posteriores flujos del paciente.

No deberá construirse durante HU-20:

* un editor visual de landing;
* un sistema completo de temas;
* un calendario avanzado sin necesidad funcional;
* un constructor de formularios;
* un CMS genérico.

---

## Lenguaje y contenido

La Recepción Digital utilizará lenguaje:

* cercano;
* directo;
* comprensible;
* profesional;
* no técnico.

Se preferirán expresiones como:

* Reservar una cita.
* Ver especialistas.
* Consultar horarios.
* Completar mi perfil.
* Iniciar sesión.

Se evitarán expresiones como:

* Crear transacción.
* Registrar entidad.
* Seleccionar bloque.
* Autenticarse en el tenant.
* Error de validación del recurso.

Los términos técnicos permanecerán únicamente en Backend, logs y documentación interna.

La interfaz utilizará el nombre público del Consultorio en mensajes relevantes.

Ejemplo:

> Tu cita en Centro Médico Santa Isabel ya está confirmada.

---

## Privacidad en la experiencia pública

La Recepción Digital deberá solicitar datos personales únicamente después de que el paciente haya seleccionado su Cita.

Los formularios públicos no deberán mostrar información de otros pacientes.

Las respuestas de conflicto no deberán revelar:

* nombre de la cuenta existente;
* número telefónico;
* documento;
* fecha de nacimiento;
* citas registradas;
* estado de la cuenta.

La pantalla de confirmación deberá mostrar únicamente la información necesaria para la reserva actual.

Cuando el usuario abandone el flujo antes de confirmar:

* no deberá existir una Cita;
* no deberá crearse un Usuario;
* no deberá crearse un Paciente;
* no deberá considerarse ocupado el horario.

---

## 6.6 Arquitectura de Navegación

La navegación de AgenDoc deberá mantenerse consistente entre la aplicación Web y Mobile.

Cada rol visualizará únicamente las funcionalidades que le corresponden.

La navegación deberá minimizar la cantidad de pasos necesarios para completar las tareas más frecuentes.

### Navegación de la Recepción Digital

La navegación pública se ejecutará siempre dentro del contexto de un Consultorio.

Flujo principal:

```text
Recepción Digital
        ↓
Explorar especialidades
        ↓
Seleccionar especialidad
        ↓
Explorar médicos
        ↓
Seleccionar Médico
        ↓
Seleccionar fecha
        ↓
Seleccionar horario
        ↓
Revisar resumen
        ↓
Registro ultrarrápido
        ↓
Confirmar primera reserva
        ↓
Reserva confirmada
        ↓
Completar perfil o iniciar sesión
```

Rutas conceptuales:

```text
/{clinicSlug}
/{clinicSlug}/especialidades
/{clinicSlug}/medicos
/{clinicSlug}/reservar
/{clinicSlug}/registro
/{clinicSlug}/reserva-confirmada
/{clinicSlug}/login
```

Durante el MVP, la navegación podrá implementarse mediante:

* múltiples rutas;
* una experiencia progresiva dentro de una sola página;
* una combinación de rutas y pasos.

La decisión de implementación deberá preservar:

* el historial de navegación;
* la posibilidad de volver al paso anterior;
* la selección realizada;
* el contexto del Consultorio;
* la compatibilidad con dispositivos móviles.

### Navegación directa

La Recepción Digital deberá permitir acceso directo a:

* reservar una cita;
* conocer especialistas;
* consultar horarios;
* iniciar sesión;
* información de contacto.

La acción principal será:

```text
Reservar una cita
```

Las acciones secundarias serán:

* Conocer nuestros especialistas.
* Consultar horarios.
* Iniciar sesión.

### Navegación posterior a la reserva

Después de una reserva exitosa, el paciente podrá:

* completar su perfil;
* iniciar sesión;
* volver a la Recepción Digital.

El sistema no deberá enviar automáticamente al paciente a una pantalla protegida sin autenticación.


### Navegación del Paciente autenticado

Flujo principal:

Inicio

↓

Mis citas

↓

Reservar cita

↓

Perfil

↓

Cerrar sesión

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

### Recepción Digital del Consultorio

| Pantalla                | Objetivo                                        | Función principal                                                      |
| ----------------------- | ----------------------------------------------- | ---------------------------------------------------------------------- |
| Recepción Digital       | Presentar el Consultorio y orientar al paciente | Acceso a reserva, especialistas, horarios, contacto e inicio de sesión |
| Especialidades          | Mostrar la oferta médica disponible             | Seleccionar una especialidad                                           |
| Médicos                 | Presentar médicos activos del Consultorio       | Seleccionar un Médico                                                  |
| Disponibilidad          | Mostrar fechas y horarios disponibles           | Seleccionar un Bloque de Agenda                                        |
| Resumen de reserva      | Revisar la selección antes del registro         | Confirmar Médico, fecha y horario                                      |
| Registro ultrarrápido   | Crear la cuenta y el perfil inicial             | Capturar datos mínimos                                                 |
| Confirmación de reserva | Comunicar la creación de la primera Cita        | Mostrar detalle e invitar a completar perfil                           |
| Completar perfil        | Incorporar información administrativa pendiente | Completar datos del Paciente                                           |
| Login contextualizado   | Autenticar dentro del Consultorio               | Iniciar sesión con correo y contraseña                                 |

### Pantallas y responsabilidades

#### Recepción Digital

Deberá mostrar:

* identidad del Consultorio;
* saludo o mensaje de bienvenida;
* acción principal para reservar;
* accesos a especialistas y horarios;
* acceso para iniciar sesión;
* dirección;
* teléfono;
* WhatsApp cuando esté disponible;
* mapa o enlace de ubicación cuando esté disponible.

La pantalla deberá ser útil incluso cuando parte de la información pública todavía no haya sido configurada.

#### Especialidades

Deberá mostrar únicamente especialidades asociadas a Médicos activos del Consultorio.

Cada especialidad podrá representarse mediante una Card con:

* nombre;
* descripción breve cuando exista;
* cantidad o disponibilidad de especialistas, cuando corresponda;
* acción para ver Médicos.

No deberá mostrar especialidades sin oferta activa si esto produce una experiencia vacía o engañosa.

#### Médicos

Cada Médico podrá mostrarse mediante una Card con:

* nombre completo;
* especialidad;
* información profesional básica aprobada;
* próxima disponibilidad cuando sea posible;
* acción para consultar horarios.

No deberán mostrarse:

* correo privado;
* teléfono personal;
* documento;
* número de colegiatura cuando no haya sido aprobado para publicación;
* datos internos de auditoría.

#### Disponibilidad

Deberá permitir:

* seleccionar fecha;
* consultar horarios disponibles;
* cambiar de Médico;
* regresar a especialidades;
* identificar claramente el horario seleccionado.

Los horarios:

* deberán mostrarse ordenados;
* no deberán incluir horas pasadas;
* deberán mostrar únicamente Bloques disponibles;
* no deberán depender únicamente del color;
* deberán ser fáciles de seleccionar en dispositivos táctiles.

#### Resumen de reserva

Antes del registro, deberá mostrar:

* Consultorio;
* Médico;
* especialidad;
* fecha;
* hora de inicio;
* hora de fin cuando aporte claridad.

Deberá permitir modificar:

* especialidad;
* Médico;
* fecha;
* horario.

El resumen no representa todavía una reserva confirmada.

#### Registro ultrarrápido

Solicitará:

* nombre o nombres;
* apellido o apellidos;
* correo electrónico;
* celular;
* contraseña;
* confirmación de contraseña.

No solicitará:

* username;
* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información médica.

La pantalla deberá mantener visible un resumen compacto de la Cita seleccionada.

#### Confirmación de reserva

Deberá mostrar:

* confirmación explícita;
* Consultorio;
* Médico;
* especialidad;
* fecha;
* hora;
* estado Programada;
* orientación para completar el perfil.

Mensaje principal:

> Tu cita ya está confirmada.

Mensaje complementario:

> Antes del día de tu consulta, por favor completa tu perfil para agilizar tu atención.

Acciones:

* Completar mi perfil.
* Iniciar sesión.
* Volver al consultorio.

#### Completar perfil

Permitirá ingresar:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional.

El paciente podrá omitir temporalmente este paso después de su primera reserva.

#### Login contextualizado

Mostrará:

* identidad del Consultorio;
* correo electrónico;
* contraseña;
* acción para iniciar sesión.

No mostrará ni solicitará username.

---

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

## Estructura visual de la Recepción Digital

La pantalla inicial utilizará una estructura simple y orientada a acciones.

### Encabezado

El encabezado podrá incluir:

* logotipo del Consultorio;
* nombre público;
* acceso para iniciar sesión;
* menú compacto en Mobile.

No deberá incluir una navegación extensa durante el MVP.

### Sección principal

La sección principal deberá responder inmediatamente:

* qué Consultorio es;
* cómo puede ayudar;
* cuál es la acción principal.

Contenido conceptual:

```text
Centro Médico Santa Isabel

Bienvenido.
¿Cómo podemos ayudarte hoy?

[ Reservar una cita ]

[ Conocer nuestros especialistas ]
[ Consultar horarios ]
[ Iniciar sesión ]
```

### Información del Consultorio

Podrá incluir:

* descripción breve;
* dirección;
* teléfono;
* WhatsApp;
* mapa;
* horario general.

### Especialidades destacadas

Podrá mostrar una selección de especialidades activas mediante Cards.

La sección deberá incluir acceso para consultar todas las especialidades disponibles.

### Médicos destacados

Podrá mostrar Médicos activos cuando exista información suficiente.

No será obligatorio mostrar todos los Médicos en la pantalla inicial.

### Pie de página

Podrá incluir:

* información de contacto;
* dirección;
* acceso al mapa;
* privacidad;
* términos;
* identificación secundaria de AgenDoc.

La estructura deberá adaptarse según la información configurada para el Consultorio.

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

## Responsive de la Recepción Digital

### Mobile

* Navegación en una columna.
* Acción principal visible sin desplazamiento excesivo.
* Cards apiladas.
* Horarios mostrados en una cuadrícula táctil.
* Resumen de reserva compacto.
* Formulario en una columna.
* Botón principal de ancho completo cuando corresponda.

### Tablet

* Cards organizadas en dos columnas cuando exista espacio suficiente.
* Resumen de reserva visible sin desplazar el formulario principal.
* Navegación simplificada.

### Desktop

* Hero y acciones con mayor espacio visual.
* Especialidades y Médicos en grids.
* Formulario y resumen de reserva en dos paneles.
* Información del Consultorio visible sin saturar la pantalla.

### Wide

* El contenido deberá mantener un ancho máximo.
* No deberá extenderse indefinidamente.
* Se priorizará legibilidad sobre ocupación total del espacio.

La secuencia funcional deberá permanecer idéntica en todos los tamaños.

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

## Card de especialidad

La Card de especialidad deberá incluir como mínimo:

* nombre;
* descripción breve opcional;
* acción para consultar Médicos.

Estados:

* default;
* hover;
* focus;
* selected cuando forme parte del flujo;
* disabled cuando corresponda.

La Card completa podrá ser interactiva, siempre que:

* mantenga foco visible;
* pueda activarse mediante teclado;
* tenga un nombre accesible;
* no dependa únicamente de un icono.

## Card de Médico

La Card de Médico deberá incluir:

* nombre completo;
* especialidad;
* información pública breve;
* acción para ver disponibilidad.

Podrá incluir:

* iniciales o avatar;
* próxima fecha disponible;
* modalidad de atención cuando se incorpore al alcance.

No deberá incluir datos privados ni información que no haya sido aprobada para publicación.

Cuando no exista fotografía, se utilizarán inicialmente las iniciales del Médico.

---

## Selección de fecha

El selector de fecha deberá:

* impedir fechas pasadas;
* resaltar la fecha seleccionada;
* permitir cambiar de fecha;
* mostrar el estado de carga de disponibilidad;
* funcionar mediante teclado;
* ser usable en pantallas táctiles.

Durante el MVP podrá utilizarse un control de fecha nativo o un componente simple, siempre que cumpla las necesidades funcionales y de accesibilidad.

No será obligatorio construir un calendario avanzado durante HU-20.

## Selección de horario

Los horarios disponibles se mostrarán mediante botones, Chips seleccionables o controles equivalentes.

Cada opción deberá mostrar:

* hora de inicio;
* hora de fin cuando sea necesaria;
* estado de selección.

Reglas visuales:

* el horario seleccionado deberá distinguirse claramente;
* los horarios no deberán depender únicamente del color;
* los controles deberán tener tamaño táctil adecuado;
* la lista deberá reordenarse cronológicamente;
* un horario que deje de estar disponible deberá retirarse o marcarse claramente antes de una nueva confirmación.

Cuando no exista disponibilidad, deberá mostrarse un estado vacío con acciones para:

* cambiar fecha;
* seleccionar otro Médico;
* volver a especialidades.

---

## Formulario de registro ultrarrápido

El formulario utilizará una columna en Mobile.

En Desktop podrá utilizar dos columnas para:

* nombres y apellidos;
* contraseña y confirmación de contraseña;

siempre que el orden de lectura permanezca claro.

Campos:

* Nombre o nombres.
* Apellido o apellidos.
* Correo electrónico.
* Celular.
* Contraseña.
* Confirmar contraseña.

Reglas:

* los labels permanecerán visibles;
* todos los campos serán obligatorios;
* el correo utilizará teclado y autocompletado apropiados;
* el celular utilizará teclado telefónico en Mobile;
* la contraseña podrá mostrar u ocultar su contenido;
* la confirmación deberá validarse antes del envío;
* no deberá existir un campo username;
* el botón principal permanecerá deshabilitado durante el envío;
* el usuario deberá recibir retroalimentación clara si ocurre un conflicto.

Texto recomendado antes de confirmar:

> Al reservar, crearemos tu cuenta de paciente en este consultorio.

La aceptación de términos y privacidad deberá incorporarse cuando estén definidos los documentos legales correspondientes.

---

## Resumen persistente de la reserva

Durante los pasos finales, la interfaz deberá mantener visible un resumen de la selección.

Contenido:

* Consultorio;
* especialidad;
* Médico;
* fecha;
* horario.

En Desktop podrá mostrarse en una Card lateral.

En Mobile podrá mostrarse:

* al inicio del formulario;
* mediante una Card compacta;
* mediante una sección expandible.

El resumen deberá permitir editar la selección sin eliminar innecesariamente los datos ya ingresados.

Si el usuario cambia el Médico, la fecha o el horario, el sistema deberá volver a validar la disponibilidad.

---

## Estados visuales de la Recepción Digital

### Consultorio no encontrado

Se mostrará cuando el `clinicSlug` no corresponda a un Consultorio disponible.

El mensaje no deberá exponer información técnica.

Acciones posibles:

* volver al inicio general;
* verificar el enlace;
* contactar soporte cuando corresponda.

### Recepción Digital no disponible

Se mostrará cuando el Consultorio exista, pero su portal público esté deshabilitado.

Mensaje sugerido:

> La recepción digital de este consultorio no está disponible temporalmente.

### Cargando información

Se utilizarán Skeletons para:

* identidad del Consultorio;
* especialidades;
* Médicos;
* disponibilidad.

### Estado vacío

Se mostrará cuando:

* no existan especialidades activas;
* no existan Médicos disponibles;
* no existan horarios para una fecha.

El estado vacío deberá incluir una acción útil.

### Conflicto de correo

Mensaje sugerido:

> Ya existe una cuenta asociada a este correo en el consultorio.

Acciones:

* Iniciar sesión.
* Utilizar otro correo.

### Horario ocupado

Mensaje sugerido:

> Este horario acaba de ser reservado. Selecciona otro horario disponible.

La interfaz deberá actualizar la disponibilidad.

### Error inesperado

Mensaje sugerido:

> No pudimos completar la reserva. Tus datos no fueron registrados. Intenta nuevamente.

El mensaje solo podrá asegurar que no hubo registros cuando el Backend confirme el rollback completo.

### Reserva confirmada

La confirmación deberá utilizar una pantalla dedicada.

No deberá depender exclusivamente de un Toast.

---

## Aplicación de HU-20 en Web y Mobile

Las reglas funcionales, contratos de API y criterios de experiencia definidos para HU-20 serán comunes para Web y Mobile.

Durante el Sprint 4, la implementación funcional se realizará inicialmente en el Frontend Web responsive. La implementación nativa en React Native y Expo se realizará cuando el Roadmap active el desarrollo funcional de la aplicación Mobile.

La ausencia temporal de una pantalla nativa Mobile no autoriza reglas diferentes, contratos exclusivos para Web, lógica de negocio en el Frontend ni decisiones que impidan la futura integración Mobile.

La validación responsive deberá cubrir teléfono, tablet y escritorio.

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

| ID    | Épica | Feature                               | Descripción                                                             | Dependencias        | Prioridad   |
|-------|-------|---------------------------------------|-------------------------------------------------------------------------|---------------------|-------------|
| FE-01 | EP-01 | Inicio de sesión                      | Autenticación de usuarios por credenciales                              | Usuarios, roles     | Must Have   |
| FE-02 | EP-01 | Navegación por rol                    | Mostrar opciones según Paciente, Recepcionista o Médico                 | FE-01               | Must Have   |
| FE-03 | EP-02 | Configuración inicial del consultorio | Disponer de los datos mínimos del consultorio base                      | Ninguna             | Must Have   |
| FE-04 | EP-02 | Gestión inicial de médicos            | Registrar médicos asociados al consultorio                              | FE-03               | Must Have   |
| FE-05 | EP-02 | Gestión inicial de recepcionistas     | Registrar recepcionistas del consultorio                                | FE-03               | Should Have |
| FE-06 | EP-03 | Incorporación pública de nuevos pacientes | Permitir que un nuevo interesado se registre progresivamente desde la Recepción Digital durante la reserva de su primera cita | FE-03, FE-04, FE-09, FE-10 | Must Have |
| FE-07 | EP-03 | Registro interno de pacientes         | Permitir que la recepcionista registre pacientes                        | FE-01, FE-03        | Must Have   |
| FE-08 | EP-03 | Búsqueda de pacientes                 | Buscar pacientes por datos básicos                                      | FE-07               | Must Have   |
| FE-09 | EP-04 | Configuración de agenda médica        | Crear agenda y bloques de disponibilidad                                | FE-04               | Must Have   |
| FE-10 | EP-04 | Consulta de disponibilidad            | Visualizar horarios disponibles por médico                              | FE-09               | Must Have   |
| FE-11 | EP-05 | Reserva digital por paciente          | Permitir la primera reserva pública y las reservas posteriores del paciente autenticado | FE-06, FE-09, FE-10 | Must Have |
| FE-12 | EP-05 | Creación de cita por recepcionista    | Permitir que recepción cree citas para pacientes                        | FE-07, FE-10        | Must Have   |
| FE-13 | EP-05 | Consulta de citas                     | Visualizar citas según rol                                              | FE-10, FE-11        | Must Have   |
| FE-14 | EP-05 | Cancelación de cita                   | Cancelar citas bajo reglas del dominio                                  | FE-12               | Must Have   |
| FE-15 | EP-05 | Reprogramación de cita                | Cambiar fecha y hora de una cita válida                                 | FE-09, FE-12        | Should Have |
| FE-16 | EP-05 | Registro de asistencia                | Confirmar la llegada o registrar la inasistencia del paciente           | FE-12               | Should Have |
| FE-17 | EP-06 | Agenda del médico                     | Mostrar citas asignadas al médico                                       | FE-12               | Must Have   |
| FE-18 | EP-06 | Registro de observación básica        | Registrar una nota simple de atención                                   | FE-16               | Must Have   |
| FE-19 | EP-06 | Marcar cita como atendida             | Cerrar la atención médica                                               | FE-17               | Must Have   |
| FE-20 | EP-06 | Historial básico                      | Consultar observaciones anteriores del paciente                         | FE-17               | Should Have |

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

#### HU-20 — Explorar el consultorio y reservar una primera cita desde la Recepción Digital

Como nuevo interesado
Quiero ingresar a la Recepción Digital de un consultorio, conocer sus especialistas, seleccionar un horario y registrarme con información mínima
Para reservar mi primera cita y convertirme automáticamente en paciente del consultorio.

Descripción:

Permite que una persona que todavía no pertenece al Consultorio complete el flujo de incorporación y primera reserva desde la experiencia pública.

El Consultorio se resuelve mediante su identificador público.

La persona podrá explorar especialidades, médicos y disponibilidad antes de proporcionar sus datos personales.

Criterios de aceptación:

* Given una URL con un `clinicSlug` válido
  When la persona ingresa
  Then visualiza la identidad pública del Consultorio correspondiente.

* Given un Consultorio activo
  When consulta la Recepción Digital
  Then visualiza únicamente sus especialidades, médicos y disponibilidad.

* Given una persona que seleccionó un Médico y un Bloque disponible
  When avanza al registro
  Then el sistema solicita únicamente nombres, apellidos, correo, celular, contraseña y confirmación de contraseña.

* Given un registro válido y un Bloque disponible
  When la persona confirma la reserva
  Then el sistema crea automáticamente el Usuario, el Paciente y la primera Cita en estado Programada.

* Given una primera reserva exitosa
  When finaliza la operación
  Then el Usuario, Paciente, Médico, Bloque y Cita pertenecen al mismo Consultorio.

* Given una reserva exitosa
  When la Cita se crea
  Then el Bloque deja de estar disponible.

* Given un correo que ya tiene una cuenta en el Consultorio
  When se intenta registrar nuevamente
  Then el sistema informa el conflicto, no crea registros duplicados y ofrece iniciar sesión.

* Given un Bloque reservado por otra persona durante el proceso
  When se confirma la reserva
  Then el sistema rechaza la operación y no crea registros parciales.

* Given una operación que falla después de iniciar el registro
  When la transacción finaliza con error
  Then no quedan Usuarios, Pacientes, Citas o Bloques inconsistentes.

* Given una reserva exitosa
  When se muestra la confirmación
  Then el sistema invita al Paciente a completar su perfil antes de la consulta.

* Given una cuenta creada
  When el Paciente desea ingresar posteriormente
  Then puede iniciar sesión con su correo y contraseña dentro del contexto del mismo Consultorio.

Prioridad:

Must Have.

Dependencias:

* HU-03.
* HU-04.
* HU-07.
* HU-08.
* TS-02.
* TS-03.
* TS-04.
* TS-05.

Estimación:

13 Story Points.

Definition of Done:

* Recepción Digital contextualizada mediante `clinicSlug`.
* Identidad pública del Consultorio visible.
* Especialidades y Médicos consultados desde el Backend.
* Disponibilidad real.
* Registro ultrarrápido.
* Correo utilizado como identificador visible.
* Usuario creado con rol Paciente.
* Paciente creado y asociado al Usuario.
* Primera Cita creada en estado Programada.
* Bloque ocupado de forma segura.
* Operación transaccional.
* Conflictos de correo y disponibilidad gestionados.
* Confirmación visible.
* Invitación para completar perfil.
* Flujo responsive.
* Validaciones de seguridad y aislamiento por Consultorio.
* Pruebas Backend y Frontend ejecutadas.
* Flujo validado de extremo a extremo.

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

#### HU-09 — Reservar una nueva cita como paciente autenticado

Como paciente autenticado
Quiero reservar una nueva cita con un Médico disponible
Para programar una atención adicional dentro de mi Consultorio.

Descripción:

Permite que un Paciente ya registrado y autenticado reserve citas posteriores sin repetir el proceso de registro público.

Criterios de aceptación:

* Given un Paciente autenticado
  When consulta especialidades y Médicos
  Then visualiza únicamente información de su Consultorio.

* Given un Paciente autenticado y un Bloque disponible
  When confirma la reserva
  Then se crea una Cita en estado Programada asociada al Paciente autenticado.

* Given un Paciente autenticado
  When intenta reservar utilizando recursos de otro Consultorio
  Then el sistema impide la operación.

* Given un Bloque ocupado
  When intenta reservar
  Then el sistema informa el conflicto y no crea la Cita.

* Given un Paciente con una Cita activa superpuesta
  When intenta reservar un horario incompatible
  Then el sistema impide la operación.

Prioridad:

Must Have.

Dependencias:

* HU-01.
* HU-08.
* HU-20.
* TS-02.
* TS-03.
* TS-04.
* TS-05.

Estimación:

5 Story Points.

Definition of Done:

* Utiliza al Paciente autenticado.
* No solicita datos de registro.
* No recibe `patientId` ni `clinicId` libremente.
* Crea la Cita en estado Programada.
* Previene doble reserva.
* Previene superposición de citas activas.
* Respeta el Consultorio del SecurityContext.
* Muestra confirmación visible.
* Funciona en Web y Mobile.

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
Dependencias: HU-09, HU-20  
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

Completada.

Sprint previsto

Sprint 4.

Sprint ejecutado

Sprint 4.

Resultado

- Se implementó autorización por dominio basada en el usuario autenticado.
- La autorización se aplica utilizando Spring Security con anotaciones @PreAuthorize y validaciones de dominio.
- Los pacientes únicamente pueden gestionar recursos que les pertenecen.
- Las recepcionistas únicamente pueden operar sobre recursos del consultorio asociado a su contexto autenticado.
- Los médicos únicamente pueden acceder a sus propias citas y registrar observaciones sobre ellas.
- Se incorporó una capa de autorización reutilizable para validar propiedad del recurso y pertenencia al consultorio.
- Se centralizaron las reglas de autorización para reducir duplicidad y facilitar futuras ampliaciones de seguridad.

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

Completada.

Sprint previsto

Sprint 5.

Sprint ejecutado

Sprint 4.

Resultado

- Se uniformó el manejo de respuestas 401 (Unauthorized) y 403 (Forbidden).
- Se centralizó el manejo de errores de autenticación y autorización mediante componentes especializados de Spring Security.
- Se validó la correcta protección de los endpoints públicos y privados.
- Se eliminaron configuraciones temporales de seguridad utilizadas durante el desarrollo.
- Se verificó que las respuestas de error no expongan información sensible del sistema.
- La plataforma quedó preparada para continuar con las Historias de Usuario del paciente sobre una base de seguridad consistente.

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
| HU-09    | Permite que un paciente autenticado reserve nuevas citas sin repetir su incorporación. |
| HU-10    | Permite el flujo central de recepción.               |
| HU-11    | El paciente debe visualizar sus citas.               |
| HU-12    | Recepción debe gestionar la agenda del consultorio.  |
| HU-13    | Cancelar citas es necesario para liberar horarios.   |
| HU-16    | El médico debe consultar sus citas.                  |
| HU-17    | La observación básica está incluida en el MVP.       |
| HU-18    | Permite cerrar el ciclo de atención.                 |
| HU-20    | Permite captar nuevos pacientes desde la Recepción Digital y completar la primera reserva de extremo a extremo. |

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

Secuencia lógica de implementación actualizada:

1. Consultorio base.
2. Usuarios, roles e inicio de sesión.
3. Registro de médicos.
4. Registro y búsqueda interna de pacientes.
5. Creación de agenda y bloques.
6. Consulta de disponibilidad.
7. Creación y gestión de citas desde recepción.
8. Seguridad y autorización por dominio.
9. Recepción Digital del Consultorio.
10. Registro ultrarrápido y primera reserva pública.
11. Reservas posteriores del paciente autenticado.
12. Consulta de citas por el paciente.
13. Agenda del médico.
14. Observación médica básica.
15. Cita atendida.
16. Historial básico.

Dependencias principales:

| Historia | Depende de                                             |
|----------|--------------------------------------------------------|
| HU-01    | Usuarios, roles, consultorio                           |
| HU-03    | Ninguna                                                |
| HU-04    | HU-03                                                  |
| HU-05    | HU-01, HU-03                                           |
| HU-06    | HU-05                                                  |
| HU-07    | HU-04                                                  |
| HU-08    | HU-07                                                  |
| HU-09    | HU-01, HU-08, HU-20, TS-02, TS-03, TS-04, TS-05        |
| HU-10    | HU-06, HU-08, TS-02, TS-03                             |
| HU-11    | HU-09, HU-20, TS-04, TS-05                             |
| HU-12    | HU-10, TS-03                                           |
| HU-13    | HU-11, HU-12, TS-04, TS-05                             |
| HU-14    | HU-08, HU-12                                           |
| HU-15    | HU-12                                                  |
| HU-16    | HU-10, TS-04, TS-05                                    |
| HU-17    | HU-15, HU-16                                           |
| HU-18    | HU-17                                                  |
| HU-19    | HU-17                                                  |
| HU-20    | HU-03, HU-04, HU-07, HU-08, TS-02, TS-03, TS-04, TS-05 |

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

HU-20 cubre la primera reserva pública de un nuevo interesado. HU-09 cubre las reservas posteriores de un paciente autenticado. No existe duplicidad de responsabilidad entre ambas historias.

El backlog mantiene una secuencia incremental viable.

El Product Backlog refinado mantiene una distribución incremental viable para completar el MVP durante los Sprints definidos en el Roadmap.

---

## 7.10.1 Matriz de Trazabilidad

La siguiente matriz resume la relación entre la visión del producto y los elementos del Product Backlog.

| Product Vision              | Épica  | Features principales | Historias relacionadas  |
|-----------------------------|--------|----------------------|-------------------------|
| Gestión segura del acceso   | EP-01  | FE-01, FE-02         | HU-01, HU-02            |
| Operación del consultorio   | EP-02  | FE-03, FE-04, FE-05  | HU-03, HU-04            |
| Administración de pacientes | EP-03  | FE-06, FE-07, FE-08  | HU-05, HU-06, HU-20     |
| Gestión de disponibilidad   | EP-04  | FE-09, FE-10         | HU-07, HU-08            |
| Gestión de citas médicas    | EP-05  | FE-11 a FE-16        | HU-09 a HU-15, HU-20    |
| Atención médica básica      | EP-06  | FE-17 a FE-20        | HU-16 a HU-19           |

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

## ADR-017 — El Portal Público constituye el punto de entrada oficial del paciente

### Decisión

El proceso de incorporación de nuevos pacientes comenzará desde un Portal Público propio de cada consultorio.

El Portal permitirá presentar el consultorio, consultar especialidades, médicos y registrar nuevos pacientes mediante un proceso simplificado.

### Justificación

Mejora la captación digital de pacientes.
Elimina la dependencia del registro presencial.
Alinea AgenDoc con plataformas modernas de reserva médica.
Prepara la evolución futura hacia un modelo SaaS multiconsultorio.

### Estado

⚠️ Reemplazado por ADR-018.

---


La decisión se conserva por trazabilidad histórica. Su alcance evolucionó y quedó consolidado en ADR-018.

---

## ADR-018 — Recepción Digital multi-consultorio contextualizada mediante slug


### Estado

✅ Aprobado.

### Fecha

2026-08-04.

### Contexto

AgenDoc fue concebido inicialmente para operar el MVP con un consultorio base.

Sin embargo, la visión del producto establece que cada Consultorio deberá contar con una experiencia pública propia desde la cual una persona pueda:

* conocer el Consultorio;
* consultar especialidades;
* conocer Médicos;
* consultar disponibilidad;
* reservar una primera Cita;
* registrarse como Paciente;
* iniciar sesión posteriormente.

El paciente no debe ingresar a una landing genérica de AgenDoc ni seleccionar manualmente el Consultorio al que desea acudir.

La experiencia pública debe sentirse como una extensión digital de la recepción física del Consultorio.

La arquitectura debe soportar inicialmente un Consultorio, pero permanecer preparada para incorporar múltiples Consultorios sin rediseñar el dominio central.

### Problema

La plataforma necesita identificar de forma segura y comprensible el Consultorio al que pertenece una navegación pública.

Utilizar directamente el identificador interno del Consultorio produciría:

* URLs poco legibles;
* exposición innecesaria de identificadores internos;
* mayor riesgo de combinación incorrecta de recursos;
* una experiencia genérica y poco alineada con la identidad del Consultorio;
* dependencia del Frontend para seleccionar correctamente el contexto organizacional.

También es necesario definir cómo se comportarán:

* el registro de nuevos Pacientes;
* el login;
* la identidad pública;
* la asociación de Usuario y Paciente;
* la primera reserva;
* el aislamiento entre Consultorios.

### Decisión

AgenDoc se implementará como una plataforma SaaS multi-consultorio.

Cada Consultorio representará un tenant funcional independiente.

Cada Consultorio tendrá un identificador público único denominado `slug`.

Durante el MVP, el `slug` formará parte de la ruta pública.

Ejemplo:

```text
agendoc.com/santa-isabel
```

El Backend resolverá el Consultorio a partir del `slug` y utilizará internamente su identificador persistente.

El paciente no seleccionará ni enviará libremente el `clinicId`.

El `clinicId` permanecerá como identificador interno del dominio y de persistencia.

### Recepción Digital

La experiencia pública de cada Consultorio se denominará **Recepción Digital del Consultorio**.

La Recepción Digital:

* pertenecerá visualmente al Consultorio;
* utilizará la infraestructura de AgenDoc;
* mostrará la identidad pública del Consultorio;
* permitirá consultar especialidades, Médicos y disponibilidad;
* permitirá reservar la primera Cita;
* permitirá iniciar sesión dentro del mismo contexto.

AgenDoc podrá mostrarse como proveedor tecnológico secundario, sin competir con la identidad principal del Consultorio.

### Resolución del contexto público

La resolución del Consultorio seguirá este flujo:

```text
URL pública
    │
    ▼
clinicSlug
    │
    ▼
Resolver Consultorio activo
    │
    ▼
Validar Recepción Digital habilitada
    │
    ▼
Obtener clinicId interno
    │
    ▼
Ejecutar consultas y operaciones dentro del Consultorio
```

El `slug` permitirá identificar el contexto público, pero no constituirá autorización suficiente.

Toda operación deberá validar que los recursos utilizados pertenezcan al Consultorio resuelto.

### Resolución del contexto autenticado

Después de iniciar sesión, el Consultorio asociado al Usuario y disponible mediante el SecurityContext será la fuente de verdad.

Flujo:

```text
JWT
    │
    ▼
SecurityContext
    │
    ▼
Usuario autenticado
    │
    ▼
Consultorio asociado
```

El `slug` podrá mantenerse como parte de la navegación, pero las reglas de autorización no dependerán exclusivamente de él.

### Registro ultrarrápido

El registro inicial del nuevo Paciente solicitará únicamente:

* nombre o nombres;
* apellido o apellidos;
* correo electrónico;
* celular;
* contraseña;
* confirmación de contraseña.

No solicitará inicialmente:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional;
* seguro médico;
* contacto de emergencia;
* alergias;
* antecedentes;
* medicamentos.

La información pendiente podrá completarse posteriormente desde el perfil.

### Identificador de acceso

El correo electrónico será el identificador visible de acceso para el Paciente.

La interfaz no solicitará un nombre de usuario independiente.

Cuando el modelo mantenga un campo técnico `username`, este será generado automáticamente por el Backend y permanecerá oculto para el usuario.

La construcción técnica podrá incorporar el contexto del Consultorio.

Ejemplo conceptual:

```text
santa-isabel:paciente@email.com
```

La autenticación deberá considerar:

* Consultorio;
* correo normalizado;
* contraseña.

### Primera reserva

La primera reserva pública se ejecutará como una única operación transaccional.

La operación creará:

* Usuario;
* Paciente;
* asociación Usuario-Paciente;
* asociación con el Consultorio;
* primera Cita en estado Programada;
* ocupación del Bloque de Agenda.

Si cualquier paso falla, la operación completa deberá revertirse.

No deberán quedar:

* Usuarios parciales;
* Pacientes parciales;
* Citas parciales;
* asociaciones incompletas;
* Bloques ocupados incorrectamente.

### Perfil progresivo

Después de reservar, el sistema invitará al Paciente a completar:

* tipo de documento;
* número de documento;
* fecha de nacimiento;
* dirección;
* información adicional.

La ausencia temporal de estos datos no invalidará al Paciente ni su primera Cita.

Los campos pendientes deberán almacenarse sin valor y no mediante datos ficticios.

### Aislamiento entre Consultorios

Todo recurso del dominio deberá permanecer dentro de un Consultorio.

El Backend deberá validar que:

* Usuario y Paciente pertenezcan al mismo Consultorio;
* Médico y Agenda pertenezcan al mismo Consultorio;
* Bloque y Médico pertenezcan al mismo Consultorio;
* Cita, Paciente, Médico, Agenda y Bloque pertenezcan al mismo Consultorio.

No se permitirá combinar recursos de Consultorios diferentes, aunque sus identificadores sean válidos.

### Alcance del MVP

Durante el MVP:

* se utilizarán rutas basadas en `slug`;
* no se implementarán subdominios por Consultorio;
* no se implementarán dominios personalizados;
* no se implementará un constructor visual de páginas;
* no se implementará un sistema completo de temas;
* no se resolverá un mismo Paciente compartido entre varios Consultorios;
* no se implementará login automático después de la primera reserva;
* no se incorporará información clínica avanzada al perfil público;
* no se implementará un modelo global de identidad de pacientes.

### Evolución futura

La arquitectura quedará preparada para evolucionar hacia:

```text
santa-isabel.agendoc.com
```

o dominios propios del Consultorio.

También podrá evolucionar hacia un modelo donde una misma persona tenga relación con varios Consultorios.

Estas evoluciones requerirán decisiones adicionales sobre:

* identidad global;
* asociación Paciente-Consultorio;
* autenticación;
* dominios;
* branding;
* privacidad;
* migración de cuentas.

### Consecuencias positivas

* El paciente ingresa directamente al Consultorio.
* La experiencia pública refuerza la identidad del Consultorio.
* Se elimina la selección manual del tenant.
* Las URLs son legibles y fáciles de compartir.
* El `clinicId` permanece protegido como identificador interno.
* El Backend controla el contexto organizacional.
* El registro inicial tiene menos fricción.
* La primera reserva entrega valor completo.
* El modelo queda preparado para múltiples Consultorios.
* La arquitectura autenticada continúa utilizando SecurityContext.
* La evolución hacia subdominios no requiere rediseñar el dominio principal.

### Consecuencias y restricciones

* El `slug` deberá ser único y estable.
* Los cambios de `slug` deberán administrarse de forma controlada.
* Toda API pública deberá recibir o resolver el contexto del Consultorio.
* Todas las consultas públicas deberán filtrar por Consultorio.
* El login deberá conocer el contexto del Consultorio.
* El esquema de Paciente deberá admitir información administrativa pendiente.
* La creación de Usuario, Paciente y Cita requerirá una transacción coordinada.
* Las APIs públicas deberán utilizar DTOs específicos.
* El Frontend deberá preservar el `slug` durante la navegación.
* Será necesario actualizar migraciones, índices y datos iniciales.

### Alternativas evaluadas

#### Usar `clinicId` en la URL

Rechazada porque:

* expone un identificador interno;
* produce URLs poco comprensibles;
* incrementa el riesgo de manipulación;
* debilita la identidad pública del Consultorio.

#### Mostrar una lista para seleccionar Consultorio

Rechazada porque:

* agrega fricción;
* rompe la experiencia de Recepción Digital;
* permite errores de selección;
* no representa la forma natural en que el paciente llega al Consultorio.

#### Crear una landing genérica de AgenDoc

Rechazada como experiencia principal porque:

* desplaza la identidad del Consultorio;
* transforma el flujo en una experiencia de marketplace;
* no representa la visión de recepción digital;
* obliga a introducir navegación y selección adicionales.

#### Usar subdominios desde el MVP

Postergada porque:

* requiere DNS wildcard;
* requiere administración adicional de certificados;
* incrementa la complejidad de despliegue;
* no aporta valor suficiente para validar el primer incremento.

#### Solicitar el perfil completo antes de reservar

Rechazada porque:

* incrementa el abandono;
* solicita datos antes de demostrar disponibilidad;
* no es necesario para crear la primera Cita;
* contradice el principio de registro progresivo.

#### Solicitar username al Paciente

Rechazada porque:

* agrega un dato artificial;
* dificulta el registro;
* el correo ya cumple la función de identificador visible;
* obliga al usuario a recordar una credencial adicional.

### Resultado

AgenDoc adopta oficialmente un modelo SaaS multi-consultorio basado en una Recepción Digital contextualizada mediante `slug`.

La primera reserva será el flujo principal de incorporación del nuevo Paciente y combinará registro ultrarrápido, creación del perfil inicial y creación transaccional de la primera Cita.

---

## Estabilidad del slug

El `slug` será un identificador público estable.

Durante el MVP:

* deberá definirse al configurar el Consultorio;
* deberá ser único;
* deberá almacenarse normalizado;
* no deberá cambiarse desde una interfaz pública;
* no deberá generarse automáticamente en cada consulta;
* no deberá depender del identificador interno.

Un cambio posterior de `slug` podrá afectar:

* enlaces compartidos;
* marcadores;
* campañas;
* rutas del Frontend;
* referencias externas.

La administración de cambios de `slug`, redirecciones y alias no formará parte del MVP.

Cuando dicha capacidad sea necesaria, deberá diseñarse una estrategia de redirección o conservación de slugs históricos.

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

### Technical Stories originalmente asociadas

- TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente.
- TS-02 — Completar autenticación JWT End-to-End.
- TS-03 — Implementar contexto del usuario autenticado.

Estas Technical Stories fueron reprogramadas y ejecutadas durante Sprint 4. El cierre funcional de Sprint 2 no dependió de declararlas completadas.

### Estimación funcional referencial

18 Story Points funcionales. Las Technical Stories se gestionaron separadamente y fueron reprogramadas a Sprint 4.

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

### Technical Stories originalmente previstas

- TS-04 — Implementar autorización por dominio.
- TS-05 — Endurecer seguridad y manejo de accesos no autorizados.

Estas Technical Stories fueron reprogramadas y ejecutadas durante Sprint 4.

### Estimación funcional referencial

18 Story Points funcionales. Las Technical Stories se gestionaron separadamente y fueron reprogramadas a Sprint 4.

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

## Sprint 4 — Recepción Digital y experiencia del paciente

### Sprint Goal

Permitir que un nuevo interesado acceda a la Recepción Digital de un Consultorio, conozca su oferta médica, consulte disponibilidad y reserve su primera Cita mediante un registro ultrarrápido.

Posteriormente, el Paciente podrá iniciar sesión, reservar nuevas Citas y consultar sus propias Citas.

### Product Backlog Items

#### Habilitadores técnicos

- TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente.
- TS-02 — Completar autenticación JWT End-to-End.
- TS-03 — Implementar contexto del usuario autenticado.
- TS-04 — Implementar autorización por dominio.
- TS-05 — Endurecer seguridad y manejo de accesos no autorizados.

#### Historias funcionales

- HU-20 — Explorar el consultorio y reservar una primera cita desde la Recepción Digital.
- HU-09 — Reservar una nueva cita como paciente autenticado.
- HU-11 — Consultar mis citas como paciente.

### Secuencia del Sprint

```text
TS-01
   ↓
TS-02
   ↓
TS-03
   ↓
TS-04
   ↓
TS-05
   ↓
HU-20
   ↓
HU-09
   ↓
HU-11
```

### Consideración funcional

La capacidad del Paciente para cancelar sus propias Citas reutilizará HU-13 y las reglas de autorización por propiedad implementadas durante TS-04 y TS-05.

### Estimación funcional referencial

| Historia | Estimación |
|----------|-----------:|
| HU-20    | 13 Story Points |
| HU-09    | 5 Story Points |
| HU-11    | 3 Story Points |

Total funcional referencial: **21 Story Points**.

Las Technical Stories se gestionan separadamente como trabajo habilitador.

### Estado

🚧 En ejecución.

### Avance

- ✅ TS-01 — Configurar entorno de desarrollo multidispositivo y multiambiente.
- ✅ TS-02 — Completar autenticación JWT End-to-End.
- ✅ TS-03 — Implementar contexto del usuario autenticado.
- ✅ TS-04 — Implementar autorización por dominio.
- ✅ TS-05 — Endurecer seguridad y manejo de accesos no autorizados.
- 🚧 HU-20 — Explorar el consultorio y reservar una primera cita desde la Recepción Digital.
- ⏳ HU-09 — Reservar una nueva cita como paciente autenticado.
- ⏳ HU-11 — Consultar mis citas como paciente.

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
- Se consideró Docker como base futura para despliegue y se definió GitHub Actions para CI/CD. Posteriormente se aprobó no utilizar Docker en el desarrollo local del MVP.
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

Sesión — Refinamiento de la Recepción Digital y reordenamiento del Sprint 4

Estado

✅ Aprobada

Contexto

Durante el refinamiento de HU-09 se identificó que la reserva de un Paciente autenticado dependía previamente de una experiencia completa de incorporación pública. También se recuperaron decisiones funcionales sobre Recepción Digital propia por Consultorio, registro ultrarrápido, perfil progresivo, correo como identificador de acceso, contexto mediante `slug` y creación transaccional de Usuario, Paciente y primera Cita.

Decisiones tomadas

- AgenDoc se define formalmente como plataforma SaaS multi-consultorio.
- Cada Consultorio representa un tenant funcional.
- Se adopta el concepto de Recepción Digital del Consultorio.
- HU-20 se redefine como el flujo integral de exploración, registro y primera reserva.
- HU-09 se redefine como la reserva de nuevas Citas por un Paciente ya autenticado.
- HU-20 pasa a ejecutarse antes de HU-09.
- El registro inicial solicita únicamente nombres, apellidos, correo, celular y contraseña.
- El perfil administrativo se completará progresivamente.
- El correo será el identificador visible de acceso.
- El `username` permanecerá como detalle técnico oculto.
- Cada Consultorio tendrá un `slug` público único.
- Durante el MVP se utilizarán rutas basadas en `slug`.
- La primera reserva creará Usuario, Paciente y Cita dentro de una única transacción.
- El `clinicId` no será seleccionado ni enviado libremente por el paciente.
- Se aprueba ADR-018.

Resultado

El orden funcional del Sprint 4 queda actualizado:

```text
HU-20
   ↓
HU-09
   ↓
HU-11
```

La selección inicial de HU-09 queda reemplazada por este refinamiento aprobado.

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

Sprint 4

Sesión 4 — Cierre de TS-04

Estado

✅ Completada

Technical Stories completadas

- TS-04 — Implementar autorización por dominio.

Decisiones tomadas

- Se implementó autorización basada en roles mediante Spring Security utilizando anotaciones @PreAuthorize.
- Se incorporó una capa de autorización del dominio para validar la pertenencia de los recursos al consultorio del usuario autenticado.
- Las operaciones sensibles dejaron de depender de identificadores enviados por el Frontend cuando estos pueden derivarse del contexto autenticado.
- Se reforzó la protección de los módulos de pacientes, médicos, agendas y citas mediante validaciones de autorización centralizadas.
- La implementación fue validada mediante pruebas automatizadas (95 pruebas exitosas) y pruebas funcionales de extremo a extremo.
- La plataforma quedó preparada para iniciar las Historias de Usuario HU-09 y HU-11 sobre una base de autorización consistente.

---

Sprint 4

Sesión 5 — Cierre de TS-05

Estado

✅ Completada

Technical Stories completadas

- TS-05 — Endurecer seguridad y manejo de accesos no autorizados.

Decisiones tomadas

- Se uniformó el comportamiento de las respuestas 401 (Unauthorized) y 403 (Forbidden).
- Se centralizó el manejo de errores de autenticación y autorización mediante componentes especializados de Spring Security.
- Se revisó la protección de los endpoints públicos y privados del Backend.
- Se eliminaron configuraciones temporales de seguridad utilizadas durante el desarrollo.
- Se verificó que las respuestas de error no expongan información sensible.
- La implementación fue validada mediante pruebas funcionales utilizando usuarios con distintos roles y escenarios de acceso autorizado y no autorizado.
- La plataforma quedó preparada para iniciar HU-20 y continuar posteriormente con HU-09 y HU-11.

---

### Sprint 4 — Evolución de la Recepción Digital y arquitectura multi-consultorio

Estado

✅ Aprobada

Fecha

2026-08-04

Decisiones aprobadas:

* AgenDoc se define formalmente como una plataforma SaaS multi-consultorio.
* Cada Consultorio representa un tenant funcional independiente.
* Se adopta el concepto de Recepción Digital del Consultorio.
* La identidad pública principal corresponde al Consultorio.
* AgenDoc actúa como infraestructura tecnológica secundaria.
* Cada Consultorio tendrá un `slug` público único.
* Durante el MVP se utilizarán rutas basadas en `slug`.
* El `clinicId` permanecerá como identificador interno.
* El registro inicial del Paciente será ultrarrápido.
* El perfil se completará progresivamente.
* El correo será el identificador visible de acceso.
* El `username` será técnico, automático y oculto.
* El login se contextualizará por Consultorio.
* La primera reserva creará Usuario, Paciente y Cita transaccionalmente.
* HU-20 se redefine como el flujo integral de exploración, registro y primera reserva.
* HU-09 queda enfocada en reservas posteriores de un Paciente autenticado.
* Durante el MVP no se implementarán subdominios, dominios personalizados, marketplace, constructor visual ni identidad global de pacientes.

Resultado:

El Blueprint queda preparado para diseñar e implementar HU-20 sobre una arquitectura coherente con la visión SaaS y con la experiencia pública de cada Consultorio.

---

---

## Validación de consistencia v1.13

| Artefacto | Validación |
|---|---|
| Product Vision | AgenDoc se define como SaaS multi-consultorio y la Recepción Digital representa el punto de entrada del nuevo Paciente. |
| MVP Scope | Se mantiene la gestión de Citas como núcleo y no se incorporan pagos, seguros, historia clínica ni funcionalidades avanzadas. |
| Modelo de Dominio | El Consultorio permanece como agregado organizacional y la Cita como agregado transaccional. |
| Modelo de Datos | Se incorporan conceptualmente `slug`, identidad pública, perfil progresivo y unicidad contextualizada. |
| Arquitectura | El contexto público se resuelve mediante `slug` y el autenticado mediante SecurityContext. |
| Seguridad | El `clinicId` permanece interno y el Backend valida pertenencia al Consultorio. |
| UX/UI | La identidad principal corresponde al Consultorio y el registro aparece después de seleccionar disponibilidad. |
| Product Backlog | HU-20 cubre la primera reserva pública y HU-09 las reservas posteriores. |
| Roadmap | HU-20 se ejecuta antes de HU-09 y HU-11. |
| ADR | ADR-018 registra la decisión multi-consultorio y sus consecuencias. |

### Resultado

- No existen dos historias responsables de la primera reserva.
- No existen dos definiciones vigentes de registro público.
- El registro ultrarrápido y el perfil progresivo mantienen responsabilidades distintas.
- El modelo público y el modelo autenticado utilizan fuentes de contexto claramente diferenciadas.
- El alcance se mantiene dentro del MVP.
- Las evoluciones futuras permanecen explícitamente fuera del Sprint 4.


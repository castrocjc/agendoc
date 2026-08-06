# AgenDoc Project Summary

> Documento maestro que resume el estado actual del proyecto AgenDoc. Su
> propósito es proporcionar el contexto necesario para iniciar cualquier
> nueva sesión de desarrollo sin necesidad de revisar la documentación
> histórica completa.

  Campo       Valor
  ----------- -----------------------------------------
  Proyecto    AgenDoc
  Documento   Project Summary
  Versión     v1.8
  Estado      Vigente
  Ubicación   docs/project/AgenDoc Project Summary.md

------------------------------------------------------------------------

# Historial del documento

  -----------------------------------------------------------------------
  Versión                Fecha              Descripción
  ---------------------- ------------------ -----------------------------
  v1.0                   Agosto 2026        Creación del documento
                                            maestro del proyecto.

  v1.1                   Agosto 2026        Incorporación del Product
                                            Backlog Maestro, resumen
                                            ejecutivo y reorganización
                                            del documento para soportar
                                            documentación modular por
                                            historias.

  v1.2                   Agosto 2026        Incorporación de la HU-09 ---
                                            Reservar una nueva cita como
                                            paciente autenticado y
                                            actualización del estado
                                            funcional del Portal del
                                            Paciente.

  v1.3                   Agosto 2026        Incorporación de la HU-11 ---
                                            Consultar mis citas como
                                            paciente autenticado y
                                            actualización del Portal del
                                            Paciente.

  v1.4                   Agosto 2026        Incorporación de la HU-21 ---
                                            Cancelar cita desde el Portal
                                            del Paciente y actualización
                                            del Portal del Paciente.

  v1.5                   Agosto 2026        Incorporación de la HU-16 ---
                                            Consultar agenda del médico e
                                            inicio oficial del Sprint 5.

  v1.6                   Agosto 2026        Incorporación de la HU-17 ---
                                            Registrar observación médica
                                            básica.

  v1.7                   Agosto 2026        Incorporación de la HU-18 ---
                                            Marcar cita como atendida y
                                            actualización del Portal del
                                            Médico.

  v1.8                   Agosto 2026        Incorporación de la HU-19 ---
                                            Consultar historial básico del
                                            paciente, cierre del Sprint 5
                                            del Portal del Médico.
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 1. Propósito

Este documento constituye el punto único de entrada al proyecto.

Resume el estado funcional, técnico y arquitectónico de AgenDoc y sirve
como referencia para iniciar nuevas sesiones de desarrollo.

Toda la información detallada de implementación se registra
individualmente en:

    docs/project/history/

------------------------------------------------------------------------

# 2. Estado general del proyecto

## Resumen ejecutivo

  Indicador                                         Valor
  ----------------------------------- -------------------
  Foundation                                ✅ Completada
  Sprint actual                              Sprint 5 completado
  Sprints completados                                   5
  Historias funcionales completadas               19 / 21
  Habilitadores técnicos                            5 / 5
  Estado general                        Desarrollo activo

------------------------------------------------------------------------

## Última historia completada

**HU-19 --- Consultar historial básico del paciente**

Estado

✅ Completada

------------------------------------------------------------------------

## Próxima historia

La siguiente historia será definida durante la planificación del próximo Sprint.

Estado

⏳ Pendiente

------------------------------------------------------------------------

# 3. Visión del producto

AgenDoc es una plataforma SaaS para la gestión integral de consultorios
médicos.

Su objetivo es digitalizar completamente la operación clínica mediante
una arquitectura moderna, segura y escalable.

El desarrollo sigue una estrategia incremental basada en Historias de
Usuario.

------------------------------------------------------------------------

# 4. Product Backlog Maestro

## EP-01 --- Acceso, usuarios y roles

  Historia                   Estado
  -------------------------- --------
  HU-01 --- Iniciar sesión   ✅
  HU-02 --- Cerrar sesión    ✅

------------------------------------------------------------------------

## EP-02 --- Gestión base del consultorio

  Historia                                     Estado
  -------------------------------------------- --------
  HU-03 --- Disponer del consultorio inicial   ✅
  HU-04 --- Registrar médico                   ✅

------------------------------------------------------------------------

## EP-03 --- Gestión de pacientes

  -----------------------------------------------------------------------
  Historia                                Estado
  --------------------------------------- -------------------------------
  HU-05 --- Registrar paciente desde      ✅
  recepción                               

  HU-06 --- Buscar paciente               ✅

  HU-20 --- Explorar el consultorio y     ✅
  reservar una primera cita desde la      
  Recepción Digital                       
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## EP-04 --- Agenda médica y disponibilidad

  Historia                                    Estado
  ------------------------------------------- --------
  HU-07 --- Crear bloques de agenda médica    ✅
  HU-08 --- Consultar disponibilidad médica   ✅

------------------------------------------------------------------------

## EP-05 --- Gestión de citas médicas

  Historia                                                      Estado
  ------------------------------------------------------------- --------
  HU-09 --- Reservar una nueva cita como paciente autenticado   ✅
  HU-10 --- Crear cita desde recepción                          ✅
  HU-11 --- Consultar mis citas como paciente                   ✅
  HU-12 --- Consultar agenda del consultorio                    ✅
  HU-13 --- Cancelar cita desde Recepción                       ✅
  HU-21 --- Cancelar cita desde el Portal del Paciente          ✅
  HU-14 --- Reprogramar cita                                    ✅
  HU-15 --- Registrar resultado de asistencia                   ✅

------------------------------------------------------------------------

## EP-06 --- Atención médica básica

  Historia                                            Estado
  --------------------------------------------------- --------
  HU-16 --- Consultar agenda del médico               ✅
  HU-17 --- Registrar observación médica básica       ✅
  HU-18 --- Marcar cita como atendida                 ✅
  HU-19 --- Consultar historial básico del paciente   ✅

------------------------------------------------------------------------

## Habilitadores técnicos

  -----------------------------------------------------------------------
  Historia                                Estado
  --------------------------------------- -------------------------------
  TS-01 --- Configurar entorno de         ✅
  desarrollo multidispositivo y           
  multiambiente                           

  TS-02 --- Completar autenticación JWT   ✅
  End-to-End                              

  TS-03 --- Implementar contexto del      ✅
  usuario autenticado                     

  TS-04 --- Implementar autorización por  ✅
  dominio                                 

  TS-05 --- Endurecer seguridad y manejo  ✅
  de accesos no autorizados               
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 5. Roadmap

  Sprint       Estado
  ------------ -----------------
  Foundation   ✅
  Sprint 1     ✅
  Sprint 2     ✅
  Sprint 3     ✅
  Sprint 4     ✅
  Sprint 5     ✅

------------------------------------------------------------------------

# 6. Arquitectura

## Frontend

-   React
-   TypeScript
-   Vite
-   React Router
-   Arquitectura Feature Based
-   Componentes reutilizables
-   Diseño Responsive
-   Mobile First
-   Portal del Paciente autenticado
-   Reutilización de componentes entre experiencias públicas y
    autenticadas

## Backend

-   Java 21
-   Spring Boot
-   Spring Security
-   Spring Validation
-   Spring Data JPA
-   JWT
-   Arquitectura Modular
-   DTOs mediante Records
-   Constructor Injection
-   Authorization Policies
-   Domain Authorization

## Persistencia

-   PostgreSQL
-   Flyway
-   Hibernate

## Seguridad

-   JWT Stateless
-   Role Based Authorization
-   Clinic Isolation
-   Domain Authorization
-   Public Endpoints Controlados
-   Contexto autenticado por dominio
-   Autorización por consultorio

# 7. Estado funcional

## Recepción Digital (Pública)

Estado

✅ Completamente implementada

Capacidades disponibles:

-   Landing pública del consultorio mediante URL personalizada.
-   Visualización de información pública del consultorio.
-   Catálogo público de especialidades.
-   Catálogo público de médicos.
-   Consulta de disponibilidad médica.
-   Registro del primer paciente.
-   Creación automática del usuario.
-   Reserva automática de la primera cita.
-   Inicio del ciclo de vida del paciente dentro de AgenDoc.

------------------------------------------------------------------------

## Portal de Recepción

Estado

✅ Operativo

Capacidades disponibles:

-   Registro de pacientes.
-   Búsqueda de pacientes.
-   Registro de médicos.
-   Creación de agendas médicas.
-   Consulta de disponibilidad.
-   Creación de citas.
-   Consulta de agenda diaria.
-   Cancelación de citas desde Recepción.
-   Reprogramación de citas.
-   Confirmación de llegada.
-   Registro de inasistencia.

------------------------------------------------------------------------

## Portal del Paciente

Estado

🚧 Parcialmente implementado

Capacidades disponibles:

-   Inicio de sesión.
-   Acceso mediante JWT.
-   Contexto autenticado.
-   Consulta de médicos.
-   Consulta de disponibilidad.
-   Reserva de nuevas citas utilizando el paciente autenticado.
-   Consulta del historial de citas del paciente autenticado.
-   Filtros por próximas, anteriores, canceladas y todas.
-   Navegación propia del paciente.
-   Experiencia visual consistente con la Recepción Digital.

Capacidades disponibles:

-   Inicio de sesión.
-   Acceso mediante JWT.
-   Contexto autenticado.
-   Consulta de médicos.
-   Consulta de disponibilidad.
-   Reserva de nuevas citas.
-   Consulta del historial de citas.
-   Cancelación de citas futuras.
-   Actualización automática del historial.

Pendiente:

-   Mejoras futuras sobre historial y seguimiento.

------------------------------------------------------------------------

## Portal del Médico

Estado

✅ Flujo básico de atención completado

Historias implementadas:

-   HU-16
-   HU-17
-   HU-18
-   HU-19

Capacidades disponibles:

- Consulta de agenda médica.
- Registro y edición de observaciones.
- Marcar cita como atendida.
- Consulta del historial básico del paciente.

------------------------------------------------------------------------

# 8. Arquitectura funcional implementada

Actualmente existen cuatro experiencias claramente diferenciadas.

## Recepción Digital

Acceso público.

No requiere autenticación.

Permite convertir visitantes en pacientes registrados.

------------------------------------------------------------------------

## Recepción

Acceso autenticado.

Rol:

RECEPTIONIST

Funciones administrativas del consultorio.

------------------------------------------------------------------------

## Paciente

Acceso autenticado.

Rol:

PATIENT

Actualmente permite:

-   consultar médicos,
-   consultar disponibilidad,
-   reservar nuevas citas,
-   consultar sus propias citas,
-   cancelar sus propias citas programadas.

------------------------------------------------------------------------

## Médico

Actualmente permite:

- iniciar sesión con rol DOCTOR.
- consultar únicamente la agenda del médico autenticado.
- filtrar por fecha.
- filtrar por estado.
- registrar observaciones médicas básicas para citas PROGRAMADA y CONFIRMADA.
- registrar observaciones médicas básicas.
- consultar y editar observaciones médicas.
- marcar citas confirmadas como atendidas.
- cierre del ciclo básico de atención médica.
- aislamiento por consultorio y por médico.


------------------------------------------------------------------------

# 9. Principios arquitectónicos

Durante todo el desarrollo del proyecto se mantienen las siguientes
reglas.

## Backend

-   Arquitectura modular.
-   Servicios desacoplados.
-   DTOs mediante Records.
-   Constructor Injection.
-   Domain Authorization.
-   Authorization Policies.
-   Validaciones de negocio dentro del Service.
-   Controladores delgados.
-   Repositorios exclusivamente para acceso a datos.
-   Sin lógica de negocio en Controllers.

------------------------------------------------------------------------

## Frontend

-   Arquitectura Feature Based.
-   Separación estricta entre:
    -   Pages
    -   Components
    -   Services
    -   Types
    -   Validation
-   Componentes reutilizables.
-   Diseño Responsive.
-   Mobile First.
-   Consistencia visual entre módulos.
-   Manejo homogéneo de errores.
-   Consumo tipado de APIs.

------------------------------------------------------------------------

# 10. Estado de calidad

## Backend

Estado

✅ Estable

-   167 pruebas automáticas.
-   BUILD SUCCESS.
-   Sin errores.
-   Seguridad endurecida.
-   Autorización por dominio implementada.

------------------------------------------------------------------------

## Frontend

Estado

✅ Estable

-   ESLint sin errores.
-   Build exitoso.
-   TypeScript limpio.
-   Navegación validada.
-   Componentes reutilizables.

------------------------------------------------------------------------

# 11. Funcionalidades incorporadas durante HU-11

La historia HU-11 incorporó la consulta de citas para el paciente
autenticado.

Backend:

-   nuevo endpoint GET /api/v1/appointments/patient.
-   reutilización del contexto autenticado (clinicId y patientId).
-   aislamiento por dominio y paciente.
-   listado cronológico de citas.

Frontend:

Nuevo módulo:

    features/patientAppointments

Capacidades:

-   listado de citas.
-   filtros por próximas, anteriores, canceladas y todas.
-   navegación hacia la reserva de una nueva cita.
-   reutilización de AppointmentService.
-   integración con el Portal del Paciente.

------------------------------------------------------------------------

# 12. Funcionalidades incorporadas durante HU-21

La historia HU-21 incorporó la cancelación de citas desde el Portal del
Paciente reutilizando la lógica existente del backend.

Backend:

-   Reutilización del endpoint de cancelación.
-   Validación de propiedad de la cita.
-   Validación por consultorio.
-   Cancelación únicamente para citas PROGRAMADA del propio paciente.
-   Liberación automática del bloque de agenda.

Frontend:

-   Nuevo diálogo de confirmación de cancelación.
-   Componente reutilizable PatientAppointmentCard.
-   Motivo opcional de cancelación.
-   Actualización automática del historial.
-   Reutilización de AppointmentService.

------------------------------------------------------------------------


------------------------------------------------------------------------

# 13. Funcionalidades incorporadas durante HU-16

La historia HU-16 incorporó el primer módulo funcional del Portal del Médico.

Backend:

- Nuevo endpoint para consultar la agenda del médico autenticado.
- Reutilización del AuthenticatedUserContext con doctorId.
- Aislamiento por consultorio y médico autenticado.
- Cobertura total mediante pruebas unitarias.

Frontend:

- Nuevo módulo features/doctorAgenda.
- Nueva página Mi agenda médica.
- Integración con el Portal del Médico.
- Consulta automática de agenda.
- Filtros por fecha y estado.
- Navegación protegida para el rol DOCTOR.

# 14. Funcionalidades incorporadas durante HU-17

La historia HU-17 incorporó el registro de observaciones médicas básicas desde el Portal del Médico.

Backend:

- Nuevos casos de uso para consultar y registrar observaciones médicas.
- Persistencia de notas clínicas reutilizando el campo notes de la cita.
- Validaciones por rol, consultorio, médico asignado y estado de la cita.
- Cobertura completa mediante pruebas unitarias.

Frontend:

- Nuevo componente MedicalObservationDialog.
- Integración con la agenda del médico.
- Consulta automática de la observación existente.
- Registro y actualización de observaciones médicas.
- Validaciones, mensajes y experiencia responsive.

------------------------------------------------------------------------



# 15. Funcionalidades incorporadas durante HU-18

La historia HU-18 incorporó el cierre del ciclo básico de atención médica desde el Portal del Médico.

## Backend

- Nuevo endpoint para marcar una cita como atendida.
- Validación de transición CONFIRMADA → ATENDIDA.
- Validación de observación médica obligatoria.
- Reutilización del contexto autenticado y autorización por dominio.
- Cobertura completa mediante pruebas unitarias.

## Frontend

- Integración de la acción "Marcar como atendida" en el MedicalObservationDialog.
- Confirmación previa al cierre de la atención.
- Actualización automática de la agenda.
- Experiencia responsive reutilizando componentes existentes.


------------------------------------------------------------------------

# 16. Funcionalidades incorporadas durante HU-19

La historia HU-19 incorporó la consulta del historial básico del paciente.

## Backend

- Nuevo endpoint para consultar antecedentes desde una cita asignada.
- Consulta de atenciones anteriores en estado ATENDIDA.
- Orden descendente por fecha y hora.
- Reutilización de Domain Authorization.
- Sin cambios de base de datos.

## Frontend

- Nuevo componente PatientHistoryDialog.
- Acción Ver historial.
- Timeline clínica compacta.
- Header y footer fijos.
- Scroll independiente del contenido.
- Diseño responsive.

# 17. Organización documental


La documentación oficial del proyecto está compuesta por:

## Documentos estratégicos

-   AgenDoc Project Blueprint
-   AgenDoc Development Playbook
-   UI Design Guide
-   AgenDoc Codebase Guide
-   AgenDoc Project Summary

------------------------------------------------------------------------

## Historial técnico

Cada historia funcional completada posee su propia documentación dentro
de:


    docs/project/history/

Esto permite mantener el documento maestro compacto y facilitar la
trazabilidad histórica del proyecto.

------------------------------------------------------------------------

# 18. Próximos pasos

La siguiente historia planificada es:

El Sprint 5 quedó completado. La siguiente historia será definida durante la planificación del siguiente Sprint.

------------------------------------------------------------------------

# 19. Estado final

El proyecto mantiene un alto nivel de consistencia arquitectónica entre
backend y frontend.

Actualmente se dispone de cuatro experiencias funcionales:

-   Recepción Digital pública.
-   Portal de Recepción.
-   Portal del Paciente.
-   Portal del Médico.

La seguridad se basa en autenticación JWT, autorización por rol y
aislamiento por dominio de consultorio.

La reutilización de servicios y componentes permite acelerar el
desarrollo de nuevas funcionalidades manteniendo una experiencia de
usuario uniforme y una arquitectura escalable.

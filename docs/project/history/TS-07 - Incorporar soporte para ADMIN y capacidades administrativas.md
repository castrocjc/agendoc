# TS-07 - Incorporar soporte para ADMIN y capacidades administrativas

> Habilitador técnico de Release 1.1 que establece la fundación inicial
> para autenticación, autorización y navegación del rol ADMIN dentro del
> contexto de un consultorio.

| Campo | Valor |
|---|---|
| Habilitador | TS-07 |
| Nombre | Incorporar soporte para ADMIN y capacidades administrativas |
| Release | Release 1.1 — Administration |
| Sprint | Sprint 6 |
| Estado | ✅ Completado |
| Documento | TS-07 - Incorporar soporte para ADMIN y capacidades administrativas.md |
| Ubicación | docs/project/history/ |

---

# 1. Objetivo

Incorporar soporte técnico para el rol `ADMIN` dentro del modelo actual
de seguridad de AgenDoc y habilitar una entrada administrativa protegida
sin alterar todavía el modelo de identidad, roles y perfiles definido
para etapas posteriores de Release 1.1.

---

# 2. Alcance implementado

TS-07 incorporó:

- rol `ADMIN` en el modelo de seguridad;
- contexto autenticado para ADMIN dentro del consultorio;
- autenticación JWT utilizando el flujo existente;
- ruta protegida `/admin`;
- redirección predeterminada de ADMIN hacia `/admin`;
- shell técnico inicial del Workspace de Administración;
- aislamiento de acceso entre ADMIN y RECEPTIONIST;
- migración Flyway V15 para registrar exclusivamente el rol ADMIN;
- pruebas unitarias, regresión backend y validación End-to-End.

TS-07 no incorporó:

- múltiples roles por identidad;
- capacidades administrativas delegables;
- gestión de usuarios;
- gestión de especialidades;
- perfiles administrativos adicionales;
- administración global de múltiples consultorios.

Las capacidades delegables permanecen bajo TS-09 y el modelo multirol
permanece bajo TS-08.

---

# 3. Situación inicial

Antes de TS-07, AgenDoc soportaba los roles:

- PATIENT;
- RECEPTIONIST;
- DOCTOR.

El modelo de seguridad utilizaba un único rol por identidad, JWT
stateless, autorización basada en rol, autorización de dominio y
aislamiento por consultorio.

No existía un rol ADMIN ni una ruta administrativa dedicada.

---

# 4. Diseño técnico

La implementación mantuvo el modelo de seguridad vigente.

`ADMIN` fue incorporado como un rol válido sin crear una entidad de
dominio independiente.

El contexto autenticado de ADMIN utiliza:

- userId;
- username;
- clinicId;
- role ADMIN.

ADMIN no requiere `Patient`, `Doctor` ni otro perfil de dominio para
autenticarse.

Esta decisión mantiene TS-07 acotado y evita adelantar la
reestructuración prevista por TS-08.

---

# 5. Backend desarrollado

Se incorporó `ADMIN` a `SecurityRoleCode`.

`SecurityAuthenticatedUserContextProvider` reconoce ADMIN y mantiene
para este rol el contexto de identidad y consultorio sin resolver un
perfil de dominio adicional.

Se actualizaron las pruebas de seguridad para:

- reconocer `ADMIN` como rol soportado;
- validar su contexto autenticado;
- preservar el rechazo de roles desconocidos;
- mantener la regresión de roles existentes.

---

# 6. Base de datos

Se creó:

`V15__add_admin_role.sql`

La migración registra únicamente:

- código `ADMIN`;
- nombre `Administrador`;
- descripción del rol;
- estado `ACTIVE`;
- `created_by = FLYWAY`.

V15 no crea usuarios, contraseñas, hashes ni credenciales demo.

Durante el desarrollo se detectó y eliminó antes de versionar una
versión inicial de V15 que creaba un usuario ADMIN fijo. La migración
fue corregida antes de su integración al repositorio para evitar
propagar credenciales a ambientes productivos.

---

# 7. Frontend desarrollado

Se incorporó `ADMIN` al tipo `UserRole`.

`getDefaultRouteForRole` redirige ADMIN hacia:

`/admin`

Se añadió una ruta protegida exclusiva para ADMIN y un shell técnico
inicial de Workspace administrativo.

El Workspace muestra:

- acceso administrativo habilitado;
- contexto del consultorio autenticado;
- sesión administrativa;
- cierre de sesión.

El shell no incorpora todavía módulos funcionales de administración.

---

# 8. Seguridad

La implementación mantiene:

- JWT Stateless;
- Role Based Authorization;
- Domain Authorization existente;
- Clinic Isolation;
- rutas protegidas en frontend.

Se validó que:

- ADMIN puede acceder a `/admin`;
- ADMIN no puede permanecer en `/dashboard`;
- RECEPTIONIST no puede acceder a `/admin`;
- un usuario no autenticado no puede ingresar directamente a `/admin`;
- cerrar sesión invalida el acceso al Workspace.

---

# 9. Validación End-to-End

Para validar el flujo completo se creó temporalmente un usuario ADMIN
exclusivamente en PostgreSQL local.

El usuario temporal:

- no formó parte de Flyway;
- no fue incorporado al repositorio;
- utilizó una contraseña BCrypt generada localmente;
- fue identificado mediante `created_by = LOCAL_TS07_E2E`;
- fue eliminado al finalizar la validación.

Se verificó:

- autenticación real por API;
- HTTP 200;
- generación de JWT;
- `role = ADMIN`;
- `clinicId = 1`;
- ausencia de perfiles Patient y Doctor;
- login desde frontend;
- redirección automática a `/admin`;
- persistencia de sesión después de refresh;
- aislamiento ADMIN frente a `/dashboard`;
- aislamiento RECEPTIONIST frente a `/admin`;
- logout;
- bloqueo de `/admin` sin sesión.

Después de las pruebas se eliminaron el usuario y las credenciales
temporales.

---

# 10. Testing

## Backend

Suite completa:

- 169 pruebas ejecutadas;
- 0 failures;
- 0 errors;
- 0 skipped;
- BUILD SUCCESS.

## Frontend

Se validó:

- ESLint exitoso;
- TypeScript/Vite build exitoso;
- `git diff --check` exitoso.

## End-to-End

Validación manual completa:

- Login ADMIN: ✅
- `/admin`: ✅
- Refresh `/admin`: ✅
- ADMIN → `/dashboard` bloqueado: ✅
- RECEPTIONIST → `/admin` bloqueado: ✅
- Logout + acceso directo `/admin` bloqueado: ✅

---

# 11. Decisiones de arquitectura

## ADMIN no es un perfil de dominio

No se creó `AdminEntity`, `AdminRepository`, `AdminService` ni tabla
específica de administradores.

## TS-08 no se adelantó

El modelo continúa temporalmente con un único rol por identidad.

La evolución hacia múltiples roles y perfiles permanece bajo TS-08.

## TS-09 no se adelantó

No se implementó catálogo ni asignación de capacidades delegables.

## V15 es role-only

Los datos de acceso ADMIN deben aprovisionarse por mecanismos
controlados y no mediante una migración con credenciales estáticas.

---

# 12. Deuda técnica identificada

Se registraron como temas separados del alcance de TS-07:

- advertencias de Mockito/Byte Buddy por carga dinámica de agentes;
- uso de `@MockBean` deprecado en pruebas existentes;
- PostgreSQL 18.3 por encima de la versión oficialmente soportada por
  la versión actual de Flyway;
- revisión futura de datos históricos de acceso creados por migraciones
  anteriores;
- decisión futura sobre exposición o protección de
  `/actuator/health`.

Ninguno de estos puntos bloqueó el cierre de TS-07.

---

# 13. Resultado final

AgenDoc dispone ahora de una fundación administrativa inicial y segura.

El rol ADMIN:

- existe como rol oficial;
- puede autenticarse mediante el flujo JWT existente;
- opera dentro del consultorio autenticado;
- dispone de una ruta administrativa protegida;
- no requiere un perfil de dominio artificial;
- permanece aislado de las rutas exclusivas de RECEPTIONIST.

La siguiente unidad funcional del Sprint 6 es HU-22 — Acceder al
Workspace de Administración.

---

# 14. Estado de cierre

- Backend: ✅
- Frontend: ✅
- Base de datos: ✅
- Seguridad: ✅
- Testing automático: ✅
- Validación End-to-End: ✅
- Credenciales temporales retiradas: ✅
- Documentación: ✅

**TS-07 completado.**

# HU-22 - Acceder al Workspace de Administración

> Historia funcional de Release 1.1 que transforma la entrada técnica
> administrativa creada en TS-07 en un Workspace de Administración
> funcional, contextualizado y protegido.

| Campo | Valor |
|---|---|
| Historia | HU-22 |
| Nombre | Acceder al Workspace de Administración |
| Release | Release 1.1 — Administration |
| Sprint | Sprint 6 |
| Estado | ✅ Completada |
| Documento | HU-22 - Acceder al Workspace de Administración.md |
| Ubicación | docs/project/history/ |

---

# 1. Objetivo

Permitir que un usuario administrativo autorizado acceda a un
Workspace de Administración seguro, contextualizado al consultorio
autenticado y preparado para incorporar progresivamente las funciones
administrativas de Release 1.1.

---

# 2. Alcance funcional implementado

HU-22 incorporó:

- evolución del shell técnico `/admin` hacia un Workspace administrativo;
- encabezado y contexto administrativo explícito;
- identificación del consultorio autenticado;
- navegación inicial de módulos administrativos;
- experiencia explícita de acceso restringido;
- retorno seguro al espacio correspondiente según el rol autenticado;
- cierre de sesión desde el Workspace;
- primer componente reutilizable para módulos administrativos.

El Workspace presenta inicialmente los dominios:

- Especialidades médicas;
- Usuarios;
- Médicos;
- Pacientes.

Estos módulos se muestran como `Próximamente` hasta que sus historias
funcionales correspondientes sean implementadas.

---

# 3. Situación inicial

TS-07 había establecido:

- rol ADMIN;
- autenticación JWT para ADMIN;
- contexto autenticado con `clinicId`;
- ruta protegida `/admin`;
- redirección predeterminada hacia `/admin`;
- shell técnico inicial del Workspace.

HU-22 tomó esa fundación y añadió la primera experiencia administrativa
funcional de Release 1.1.

---

# 4. Diseño técnico

La implementación se mantuvo exclusivamente en frontend.

No fue necesario crear:

- nuevos endpoints;
- nuevas entidades;
- nuevos servicios backend;
- nuevas tablas;
- nuevas migraciones Flyway;
- un perfil de dominio ADMIN;
- un framework administrativo general.

La solución reutiliza:

- `ProtectedRoute`;
- `sessionService`;
- `AppButton`;
- `AppCard`;
- React Router;
- Lucide Icons.

`ProtectedRoute` fue extendido mediante un elemento opcional de acceso
no autorizado.

El comportamiento existente de las demás rutas protegidas se mantiene
sin cambios.

---

# 5. Workspace administrativo

`AdminWorkspacePage` fue evolucionado para mostrar:

- encabezado de Administración;
- título `Workspace administrativo`;
- contexto del consultorio autenticado;
- módulos administrativos iniciales;
- identidad de la sesión administrativa;
- acción de cierre de sesión.

El consultorio se obtiene exclusivamente de la sesión autenticada.

No existe:

- selector de consultorio;
- parámetro libre de `clinicId`;
- navegación multi-clinic;
- administración global.

---

# 6. Componentes reutilizables

Como primer incremento de TS-12 se incorporó:

`AdministrativeModuleCard`

El componente encapsula:

- icono;
- título;
- descripción;
- estado del módulo;
- acción opcional.

Actualmente permite representar estados:

- `available`;
- `coming-soon`.

HU-22 no creó un framework administrativo sobredimensionado.

TS-12 continuará evolucionando de forma incremental conforme aparezcan
casos funcionales reales.

---

# 7. Acceso no autorizado

Se incorporó:

`AdminAccessDenied`

La experiencia informa explícitamente:

`Acceso restringido`

y comunica que la cuenta autenticada no posee autorización para acceder
al Workspace de Administración.

La acción:

`Volver a mi espacio`

utiliza la ruta predeterminada correspondiente al rol autenticado.

---

# 8. Seguridad

Se validó el siguiente comportamiento:

## Usuario sin sesión

`/admin` redirige a `/login`.

## ADMIN

ADMIN puede acceder a `/admin` dentro del contexto de su consultorio.

## RECEPTIONIST

RECEPTIONIST recibe una experiencia explícita de acceso restringido.

La sesión permanece activa y puede regresar a `/dashboard`.

## PATIENT

PATIENT recibe una experiencia explícita de acceso restringido.

La sesión permanece activa y puede regresar a `/account`.

## DOCTOR

DOCTOR recibe una experiencia explícita de acceso restringido.

La sesión permanece activa y puede regresar a `/account`.

---

# 9. Criterios de aceptación

| # | Criterio | Estado |
|---|---|---|
| 1 | El acceso requiere autenticación. | ✅ Validado |
| 2 | ADMIN puede ingresar al Workspace. | ✅ Validado |
| 3 | Un usuario no ADMIN puede ingresar únicamente cuando posee al menos una capacidad administrativa delegada. | ⏳ Diferido a TS-09 |
| 4 | La navegación muestra solo módulos autorizados. | ✅ Validado para el slice ADMIN actual |
| 5 | Un usuario sin autorización recibe una experiencia de acceso denegado consistente. | ✅ Validado |
| 6 | El Workspace opera en el contexto del consultorio autenticado. | ✅ Validado |
| 7 | No se solicita ni permite seleccionar libremente otro consultorio. | ✅ Validado |

El criterio 3 no se considera implementado ni validado dentro de HU-22.

Las capacidades administrativas delegables pertenecen a TS-09 y no se
adelantaron durante esta historia.

---

# 10. Datos locales de validación

Para la validación local se normalizaron las identidades de desarrollo:

- `admin@agendoc.local` → ADMIN;
- `reception@agendoc.local` → RECEPTIONIST.

Estos cambios pertenecen exclusivamente a datos locales de desarrollo.

No se incorporaron:

- credenciales ADMIN en Flyway;
- usuarios ADMIN en migraciones;
- datos estáticos de acceso en producción.

---

# 11. Pruebas ejecutadas

## Frontend

- ESLint: ✅ PASS
- TypeScript Build: ✅ PASS
- Vite Production Build: ✅ PASS

## Validación End-to-End

Se verificó satisfactoriamente:

### 5B.1

Usuario sin sesión:

`/admin` → `/login`

Resultado: ✅ PASS

### 5B.2

ADMIN:

`login` → `/admin` → Workspace administrativo

Resultado: ✅ PASS

### 5B.3

RECEPTIONIST:

`/admin` → Acceso restringido → `/dashboard`

Resultado: ✅ PASS

### 5B.4

ADMIN logout:

`Cerrar sesión` → `/login`

Nuevo acceso a `/admin` → `/login`

Resultado: ✅ PASS

### 5B.5

PATIENT:

`/admin` → Acceso restringido → `/account`

Resultado: ✅ PASS

### 5B.6

DOCTOR:

`/admin` → Acceso restringido → `/account`

Resultado: ✅ PASS

---

# 12. Archivos principales

Frontend:

- `frontend/src/App.tsx`
- `frontend/src/features/auth/components/ProtectedRoute.tsx`
- `frontend/src/features/admin/pages/AdminWorkspacePage.tsx`
- `frontend/src/features/admin/pages/AdminWorkspacePage.css`
- `frontend/src/features/admin/pages/AdminAccessDenied.tsx`
- `frontend/src/features/admin/pages/AdminAccessDenied.css`
- `frontend/src/features/admin/components/AdministrativeModuleCard.tsx`
- `frontend/src/features/admin/components/AdministrativeModuleCard.css`

Documentación:

- `docs/project/history/HU-22 - Acceder al Workspace de Administración.md`
- `docs/project/releases/Release 1.1 — Administration.md`
- `docs/project/AgenDoc Project Summary.md`

---

# 13. Decisiones de alcance

## TS-08 no se adelantó

El modelo continúa utilizando un único rol por identidad.

## TS-09 no se adelantó

No se implementaron capacidades administrativas delegables.

## Backend sin cambios

HU-22 no requirió cambios en backend.

## Clinic Isolation preservado

El Workspace utiliza exclusivamente el `clinicId` proveniente de la
sesión autenticada.

## TS-12 evoluciona incrementalmente

Se creó únicamente el componente reutilizable requerido por el primer
caso administrativo real.

---

# 14. Resultado final

AgenDoc dispone ahora de un Workspace de Administración funcional para
ADMIN.

El Workspace:

- requiere autenticación;
- respeta autorización por rol;
- mantiene Clinic Isolation;
- muestra el contexto administrativo del consultorio;
- presenta los módulos iniciales de Release 1.1;
- ofrece una experiencia explícita de acceso restringido;
- permite cerrar la sesión administrativa;
- establece la primera base reutilizable de UI administrativa.

Las capacidades delegables permanecen pendientes de TS-09.

La siguiente unidad funcional del Sprint 6 es:

**HU-24 — Gestionar especialidades médicas.**

---

# 15. Estado de cierre

- Backend: ➖ Sin cambios requeridos
- Frontend: ✅
- Seguridad: ✅
- Clinic Isolation: ✅
- Pruebas técnicas: ✅
- Validación End-to-End: ✅
- Capacidades delegadas: ⏳ TS-09
- Documentación: ✅

**HU-22 completada.**

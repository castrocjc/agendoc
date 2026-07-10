# AgenDoc UI Design Guide

> Documento oficial de identidad visual, estilo de interfaz y criterios de diseño visual del producto AgenDoc.

| Campo     | Valor                                |
|-----------|--------------------------------------|
| Proyecto  | AgenDoc                              |
| Documento | UI Design Guide                      |
| Versión   | v1.0                                 |
| Estado    | Borrador aprobado para diseño visual |
| Ubicación | docs/ux/UI Design Guide.md           |

---

# 1. Propósito

Definir la identidad visual oficial de AgenDoc para asegurar una experiencia profesional, confiable, moderna, simple, cercana y premium en la plataforma Web y Mobile.

Este documento complementa el AgenDoc Project Blueprint y el AgenDoc Development Playbook.

No reemplaza decisiones funcionales, arquitectónicas, de backlog ni de modelo de datos.

---

# 2. Filosofía Visual

AgenDoc debe sentirse como un software médico profesional, no como una aplicación genérica de formularios.

La interfaz deberá transmitir:

- confianza
- orden
- claridad
- velocidad
- seguridad
- cercanía
- calidad premium

El diseño debe reducir la carga cognitiva del usuario y permitir que médicos, recepcionistas y pacientes completen sus tareas con fluidez.

---

# 3. Personalidad de Marca

AgenDoc se percibirá como:

- profesional, pero no frío
- moderno, pero no experimental
- simple, pero no básico
- premium, pero no recargado
- clínico, pero cercano
- eficiente, pero humano

La experiencia visual debe evitar la apariencia de sistema administrativo antiguo.

---

# 4. Dirección Visual

La identidad visual de AgenDoc seguirá una estética SaaS premium aplicada al contexto médico.

Referencias de inspiración:

- claridad estructural de Linear
- simplicidad modular de Notion
- precisión visual de Stripe
- consistencia de Material Design 3
- madurez empresarial de Microsoft Fluent
- foco en conversión y acceso de Clerk
- calma visual de productos digitales de salud

Estas referencias son inspiración, no modelos a copiar.

---

# 5. Moodboard Conceptual

La interfaz deberá sentirse:

- limpia
- luminosa
- ordenada
- espaciosa
- confiable
- calmada
- profesional
- tecnológica

Palabras clave visuales:

- clinical clarity
- calm productivity
- premium SaaS
- human healthcare
- operational simplicity
- trusted scheduling

---

# 5.1 Logo Oficial de AgenDoc

La identidad visual oficial de AgenDoc se construye mediante un imagotipo compuesto por un isotipo y un wordmark.

## Concepto

El isotipo representa la unión entre la organización de citas médicas y el cuidado de la salud.

Elementos:

- Calendario minimalista.
- Cápsula en estilo outline.
- Punto verde de cita activa.

Significado:

Calendario

- agenda
- organización
- disponibilidad

Cápsula

- salud
- cuidado
- tratamiento

Punto verde

- cita activa
- disponibilidad
- momento importante

## Estilo visual

El logo deberá transmitir:

- SaaS moderno
- minimalista
- profesional
- premium
- atemporal

El isotipo utilizará un lenguaje visual compatible con Lucide Icons.

No se utilizarán símbolos médicos tradicionales como:

- cruz médica
- corazón
- electrocardiograma
- escudos
- iconografía hospitalaria convencional

## Wordmark

Tipografía:

Inter SemiBold (600)

Construcción:

Agen → Neutral 900 (#0F172A)

Doc → Primary 600 (#2563EB)

Nombre oficial:

AgenDoc

## Versiones oficiales

Durante el MVP únicamente existirán las siguientes versiones:

- Logo principal
- Logo blanco
- Logo monocromático
- Isotipo

No se permitirán variantes adicionales.

## Ubicación de los activos

Los archivos oficiales del branding deberán mantenerse en:

frontend-web/
└── public/
    └── branding/
        ├── agendoc-logo.svg
        ├── agendoc-icon.svg
        ├── agendoc-logo-white.svg
        ├── agendoc-logo-mono.svg
        ├── favicon.svg
        └── logo-mark.svg

El SVG maestro será la única fuente autorizada para generar futuras variantes.

---

# 6. Paleta de Colores

## 6.1 Principio de color

AgenDoc utilizará una paleta clara, profesional y calmada.

El azul será el color principal por su asociación con confianza, seguridad y tecnología.

El verde se reservará para disponibilidad, confirmaciones y estados positivos.

Los fondos deberán ser suaves y neutros para evitar fatiga visual.

---

## ## 6.2 Color primario

Primary 600:

```text
#2563EB
```

Uso:

* acciones principales
* botones primarios
* enlaces importantes
* foco visual
* navegación activa

---

## 6.3 Color secundario

Secondary 500:

```text
#10B981
```

Uso:

* disponibilidad
* confirmaciones
* estados positivos
* éxito operativo

---

## 6.4 Colores de estado

```text
Success: #10B981
Warning: #F59E0B
Danger:  #EF4444
Info:    #3B82F6
```

---

## 6.5 Neutros

```text
Neutral 50:  #F8FAFC
Neutral 100: #F1F5F9
Neutral 200: #E2E8F0
Neutral 300: #CBD5E1
Neutral 400: #94A3B8
Neutral 500: #64748B
Neutral 600: #475569
Neutral 700: #334155
Neutral 800: #1E293B
Neutral 900: #0F172A
```

---

# 7. Tipografía

La fuente oficial será:

```text
Inter
```

Jerarquía inicial:

```text
Display: 40px / 48px / 700
H1:      32px / 40px / 700
H2:      24px / 32px / 600
H3:      20px / 28px / 600
Body:    16px / 24px / 400
Small:   14px / 20px / 400
Caption: 12px / 16px / 400
```

---

# 8. Espaciado

AgenDoc utilizará una escala basada en múltiplos de 4 px.

```text
4
8
12
16
20
24
32
40
48
64
80
```

Reglas:

* 16 px será el espaciado base entre elementos relacionados.
* 24 px será el espaciado base entre bloques.
* 32 px o más se usará para separar secciones principales.

---

# 9. Border Radius

```text
XS:   6px
SM:   8px
MD:   12px
LG:   16px
XL:   24px
Full: 999px
```

Uso:

* Inputs: 10px
* Botones: 10px
* Cards: 16px
* Modales: 20px
* Badges: 999px

---

# 10. Sombras y Elevación

```text
Low:
0 1px 2px rgba(15, 23, 42, 0.06)

Medium:
0 4px 12px rgba(15, 23, 42, 0.08)

High:
0 12px 32px rgba(15, 23, 42, 0.12)
```

---

# 11. Iconografía

Biblioteca oficial:

```text
Lucide Icons
```

Reglas:

* Tamaño base: 20px.
* Tamaño pequeño: 16px.
* Tamaño grande: 24px.
* Estilo lineal.
* Peso visual uniforme.
* Los iconos acompañan acciones, no reemplazan texto crítico.

---

# 12. Estilo de Ilustraciones

Las ilustraciones deberán ser mínimas, suaves y funcionales.

Uso permitido:

* estados vacíos
* errores
* onboarding futuro
* confirmaciones relevantes

No se usarán ilustraciones complejas en pantallas operativas.

---

# 13. Componentes Base

Los componentes oficiales deberán evolucionar desde:

* AppButton
* AppInput
* AppCard

Y extenderse progresivamente hacia:

* AppSelect
* AppBadge
* AppAlert
* AppToast
* AppModal
* AppAvatar
* AppLoader
* AppEmptyState
* AppErrorState
* AppNavbar
* AppSidebar
* AppCalendar
* AppTable

Todo componente debe usar Design Tokens.

---

# 14. Estados Visuales

Todo componente interactivo deberá contemplar:

* default
* hover
* focus
* active
* disabled
* loading
* error
* success cuando aplique

El foco visible es obligatorio en Web.

Los estados deben ser claros sin depender solo del color.

---

# 15. Responsive Design

AgenDoc seguirá un enfoque Mobile First.

```text
Mobile:  0 - 767px
Tablet:  768px - 1023px
Desktop: 1024px+
Wide:    1440px+
```

Reglas:

* Mobile usa una columna.
* Tablet puede usar dos columnas.
* Desktop aprovecha layouts de paneles.
* Las tablas en Mobile se transforman en cards.
* La experiencia funcional no cambia por tamaño de pantalla.

---

# 16. Accesibilidad

AgenDoc buscará cumplir WCAG AA.

Reglas mínimas:

* contraste mínimo 4.5:1 para texto normal
* foco visible
* navegación por teclado
* labels visibles
* mensajes de error claros
* no depender solo del color
* controles táctiles cómodos
* textos comprensibles para usuarios no técnicos

---

# 17. Microinteracciones

Las microinteracciones deberán ser sutiles y funcionales.

Duración recomendada:

```text
Fast:   120ms
Normal: 180ms
Slow:   240ms
```

Uso recomendado:

* hover en botones y cards
* feedback al presionar
* transición suave en modales
* aparición de toast
* skeleton durante carga
* validación visual de campos
* cambio visual de estado de cita

---

# 18. Estilo General del Producto

AgenDoc utilizará una interfaz:

* clara
* modular
* con fondos suaves
* con cards bien separadas
* con formularios limpios
* con acciones principales evidentes
* con estados de sistema visibles
* con lenguaje visual consistente

La pantalla de Login debe sentirse como la puerta de entrada a un producto médico premium.

Debe evitar verse como una pantalla técnica, básica o genérica.

---

# 19. Regla de Consistencia

Toda nueva pantalla deberá cumplir:

* usa Design Tokens
* usa componentes base
* respeta la paleta oficial
* respeta tipografía oficial
* contempla estados visuales
* contempla responsive
* contempla accesibilidad
* mantiene coherencia entre Web y Mobile

Si una pantalla requiere un patrón nuevo, primero deberá evaluarse si corresponde crear un componente reutilizable.

---

# 20. Decisión de la Sesión

Se aprueba construir una identidad visual propia para AgenDoc basada en una experiencia SaaS premium para salud.

Esta guía será la referencia oficial para todas las próximas iteraciones visuales del producto.

La dirección visual del Login y los componentes base ha sido validada.

La HU-01 — Iniciar sesión queda habilitada para continuar su implementación funcional, permitiéndose únicamente ajustes visuales menores derivados de la integración con el Backend y las validaciones de uso.
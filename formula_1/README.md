# Formula 1 Simulator 2026

Simulador completo de la temporada de Fórmula 1 2026 con interfaz gráfica profesional desarrollado en JavaFX. Incluye simulación realista de carreras con los 22 pilotos oficiales, sistema de campeonato, historial de resultados y tema visual inspirado en la F1.

---

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Estructura de Paquetes](#estructura-de-paquetes)
- [Módulos Principales](#módulos-principales)
- [Sistema de Simulación](#sistema-de-simulación)
- [Interfaz Gráfica](#interfaz-gráfica)
- [Datos y Persistencia](#datos-y-persistencia)
- [Autor](#autor)

---

## Características

### Simulación de Carreras
- Simulación realista con los **22 pilotos oficiales** de la temporada 2026
- **Calendario completo de 24 carreras** (circuitos reales de la temporada)
- Sistema de clima dinámico: Seco, Lluvioso y Extremo
- Tipos de neumáticos con factores de rendimiento según condiciones
- Eventos aleatorios durante carrera: accidentes, fallos mecánicos, pinchazos, trompos, penalizaciones
- Sistema de DNF (Did Not Finish) con causas realistas
- Cálculo de tiempos basado en velocidad del vehículo, habilidad del piloto y condiciones

### Sistema de Campeonato
- Sistema de puntos oficial de F1: 25-18-15-12-10-8-6-4-2-1
- Tabla de clasificación con desempate por mejor posición
- Seguimiento de estadísticas por piloto: puntos totales, carreras completadas, mejor posición
- Simulación carrera a carrera o temporada completa de una vez

### Historial y Estadísticas
- Persistencia de resultados de carreras en archivo JSON
- Historial completo de sesiones anteriores con fecha, circuito y clima
- Comparación de tiempos entre sesiones
- Estadísticas acumuladas de la temporada

### Interfaz Profesional
- Tema oscuro inspirado en la F1 con color primario `#e10600`
- Efectos glassmorphism en tarjetas y paneles
- Barras de progreso animadas
- Scrollbars personalizados
- Botones con estados hover/active
- Sidebar de navegación con 5 secciones
- Diseño responsive con CSS puro (sin inline styles)

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| JavaFX | 21.0.1 | Interfaz gráfica |
| Gson | 2.11.0 | Serialización/deserialización JSON |
| Maven | 3.x | Build system y gestión de dependencias |

---

## Requisitos

- **JDK 17** o superior
- **Maven 3.6+**
- Conexión a internet (solo para la primera descarga de dependencias)

---

## Instalación y Ejecución

### Clonar el repositorio

```bash
git clone https://github.com/Jsebastiansalas/Formula-1.git
cd Formula-1/formula_1
```

### Compilar y ejecutar con Maven

```bash
mvn clean javafx:run
```

### Compilar solamente

```bash
mvn clean compile
```

### Alternativa: Scripts incluidos (Windows)

```bash
compilar.bat
ejecutar.bat
```

---

## Arquitectura del Proyecto

El proyecto sigue una arquitectura por capas con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────────┐
│                   UI (JavaFX)                    │
│   AppFX, Pantallas, Estilos, Paneles            │
├─────────────────────────────────────────────────┤
│              Simulación / Lógica                 │
│   SimuladorClasificacion, GestorTemporada,      │
│   TablaCampeonato, SistemaPuntos, Clima         │
├─────────────────────────────────────────────────┤
│                  Gestores                        │
│   GestorCircuitos, GestorPilotos,               │
│   GestorVehiculos, GestorEquipos                │
├─────────────────────────────────────────────────┤
│                  Modelo                          │
│   Circuito, Piloto, Vehiculo, Equipo            │
├─────────────────────────────────────────────────┤
│              Almacenamiento                      │
│   HistorialResultados, SesionResultado          │
├─────────────────────────────────────────────────┤
│              Recursos (JSON + CSS)               │
│   circuitos.json, pilotos.json, vehiculos.json  │
└─────────────────────────────────────────────────┘
```

---

## Estructura de Paquetes

```
formula_1/
├── pom.xml
├── compilar.bat
├── ejecutar.bat
├── ejecutar-debug.bat
├── historial/
│   └── resultados.json
├── src/
│   └── main/
│       ├── java/com/formula1/
│       │   ├── Main.java
│       │   ├── modelo/
│       │   │   ├── Circuito.java
│       │   │   ├── Piloto.java
│       │   │   ├── Vehiculo.java
│       │   │   ├── Equipo.java
│       │   │   ├── ConfiguracionVehiculo.java
│       │   │   └── RecordVuelta.java
│       │   ├── gestor/
│       │   │   ├── GestorCircuitos.java
│       │   │   ├── GestorPilotos.java
│       │   │   ├── GestorVehiculos.java
│       │   │   ├── GestorEquipos.java
│       │   │   └── GestorConfiguracion.java
│       │   ├── simulacion/
│       │   │   ├── SimuladorClasificacion.java
│       │   │   ├── SimuladorCarreraRealista.java
│       │   │   ├── GestorTemporada.java
│       │   │   ├── TablaCampeonato.java
│       │   │   ├── SistemaPuntos.java
│       │   │   ├── Clima.java
│       │   │   ├── GeneradorClima.java
│       │   │   ├── TipoNeumatico.java
│       │   │   ├── ModoConduccion.java
│       │   │   ├── EventoCarrera.java
│       │   │   └── Comentarista.java
│       │   ├── almacenamiento/
│       │   │   ├── HistorialResultados.java
│       │   │   └── SesionResultado.java
│       │   └── ui/
│       │       ├── AppFX.java
│       │       ├── Estilos.java
│       │       ├── PantallaSimulacion.java
│       │       ├── PantallaSimulacionMejorada.java
│       │       ├── PantallaTemporadaCompleta.java
│       │       ├── PantallaCalendario.java
│       │       ├── PantallaCampeonato.java
│       │       ├── PantallaHistorial.java
│       │       ├── PantallaEstadisticas.java
│       │       └── PanelEstadisticasCarrera.java
│       └── resources/
│           ├── styles/
│           │   └── f1-theme.css
│           └── data/
│               ├── circuitos.json
│               ├── pilotos.json
│               ├── vehiculos.json
│               └── equipos.json
└── .vscode/
    └── launch.json
```

---

## Módulos Principales

### Modelo (`com.formula1.modelo`)

Clases POJO que representan las entidades del dominio:

| Clase | Descripción |
|---|---|
| `Circuito` | Circuito de F1: nombre, país, longitud en km, número de vueltas |
| `Piloto` | Piloto: nombre, equipo, número, habilidad (0-100) |
| `Vehiculo` | Vehículo con velocidad máxima y mapa de rendimiento por modo de conducción |
| `Equipo` | Escudería con nombre, pilotos y presupuesto |
| `ConfiguracionVehiculo` | Configuración técnica del vehículo (alerón, suspensión, etc.) |
| `RecordVuelta` | Registro de mejor vuelta en un circuito |

### Gestores (`com.formula1.gestor`)

Capa de acceso a datos con operaciones CRUD y almacenamiento en HashMap:

| Clase | Descripción |
|---|---|
| `GestorCircuitos` | CRUD de circuitos. Carga desde `data/circuitos.json` en classpath |
| `GestorPilotos` | CRUD de pilotos. Carga desde `data/pilotos.json` en classpath |
| `GestorVehiculos` | CRUD de vehículos. Asignación piloto-vehículo y comparación |
| `GestorEquipos` | CRUD de equipos con persistencia a disco |
| `GestorConfiguracion` | Gestión de configuraciones de vehículo con persistencia |

### Simulación (`com.formula1.simulacion`)

Motor de simulación con física simplificada y eventos aleatorios:

| Clase | Descripción |
|---|---|
| `SimuladorClasificacion` | Motor principal: calcula tiempos, genera eventos, ordena resultados |
| `SimuladorCarreraRealista` | Variante con simulación más detallada vuelta a vuelta |
| `GestorTemporada` | Gestiona el calendario de 24 carreras y la progresión de temporada |
| `TablaCampeonato` | Acumula puntos y genera clasificación del mundial |
| `SistemaPuntos` | Tabla de puntos oficial F1 (25-18-15-12-10-8-6-4-2-1) |
| `Clima` | Enum: SECO, LLUVIOSO, EXTREMO |
| `GeneradorClima` | Genera condiciones climáticas aleatorias por carrera |
| `TipoNeumatico` | Tipos de neumático con factor de velocidad y compatibilidad por clima |
| `ModoConduccion` | Modos de conducción que afectan rendimiento del vehículo |
| `EventoCarrera` | Enum con todos los eventos posibles durante carrera |
| `Comentarista` | Genera comentarios narrativos durante la simulación |

### Almacenamiento (`com.formula1.almacenamiento`)

Persistencia de historial de carreras en JSON:

| Clase | Descripción |
|---|---|
| `HistorialResultados` | Guarda/carga sesiones desde `historial/resultados.json` |
| `SesionResultado` | Modelo de una sesión guardada: circuito, clima, fecha, clasificación |

### Interfaz Gráfica (`com.formula1.ui`)

Interfaz JavaFX con navegación por sidebar y tema CSS profesional:

| Clase | Descripción |
|---|---|
| `AppFX` | Punto de entrada gráfico. Sidebar con navegación y hero section |
| `Estilos` | Utilidad que aplica `f1-theme.css` a cualquier Scene |
| `PantallaSimulacion` | Simulación de carrera individual con selección de circuito |
| `PantallaSimulacionMejorada` | Versión avanzada con más opciones de simulación |
| `PantallaTemporadaCompleta` | Simulación de las 24 carreras con barra de progreso |
| `PantallaCalendario` | Calendario visual con estado de cada GP (completado/siguiente/pendiente) |
| `PantallaCampeonato` | Tabla de clasificación con colores por posición (oro/plata/bronce) |
| `PantallaHistorial` | Historial de carreras con estadísticas acumuladas |
| `PantallaEstadisticas` | Panel de estadísticas avanzadas |
| `PanelEstadisticasCarrera` | Componente reutilizable de estadísticas post-carrera |

---

## Sistema de Simulación

### Cálculo de Tiempos

El tiempo de vuelta se calcula con la fórmula:

```
tiempo = (distanciaCircuito / velocidadAjustada) * 3600

velocidadAjustada = (velocidadBase * factorNeumatico) / factorClima
```

Donde:
- **velocidadBase** = velocidad promedio del vehículo ajustada por habilidad del piloto y variación aleatoria
- **factorClima**: Seco = 1.0, Lluvioso = 1.12, Extremo = 1.25
- **factorNeumatico**: depende del compuesto seleccionado
- Penalización adicional del 15% si el neumático no es apropiado para el clima

### Eventos de Carrera

Probabilidad base de incidente por clima:
- Seco: 8%
- Lluvioso: 18%
- Extremo: 30%

La habilidad del piloto reduce la probabilidad hasta un 50%. Los eventos posibles incluyen:

| Evento | Efecto |
|---|---|
| Accidente grave | DNF |
| Fallo mecánico | DNF |
| Fallo de motor | DNF |
| Problema de frenos | DNF |
| Accidente leve | Penalización de tiempo |
| Trompo | Penalización de tiempo |
| Salida de pista | Penalización de tiempo |
| Pinchazo | Penalización de tiempo |
| Penalización 5s | +5 segundos |
| Penalización 10s | +10 segundos |

### Pilotos Incluidos (Temporada 2026)

| Piloto | Equipo | Habilidad |
|---|---|---|
| Max Verstappen | Red Bull Racing | 98 |
| Lando Norris | McLaren | 95 |
| Charles Leclerc | Ferrari | 94 |
| Lewis Hamilton | Ferrari | 93 |
| Oscar Piastri | McLaren | 92 |
| George Russell | Mercedes | 91 |
| Fernando Alonso | Aston Martin | 90 |
| Carlos Sainz | Williams | 88 |
| Alexander Albon | Williams | 85 |
| Kimi Antonelli | Mercedes | 85 |
| Sergio Pérez | Cadillac | 84 |
| Pierre Gasly | Alpine | 84 |
| Nico Hülkenberg | Audi | 83 |
| Isack Hadjar | Red Bull Racing | 82 |
| Esteban Ocon | Haas | 82 |
| Valtteri Bottas | Cadillac | 82 |
| Liam Lawson | Racing Bulls | 81 |
| Franco Colapinto | Alpine | 80 |
| Oliver Bearman | Haas | 80 |
| Gabriel Bortoleto | Audi | 79 |
| Lance Stroll | Aston Martin | 78 |
| Arvid Lindblad | Racing Bulls | 76 |

---

## Interfaz Gráfica

### Navegación Principal

La aplicación presenta un sidebar lateral con 5 secciones:

1. **Simular Carrera** - Ejecuta una carrera individual en el circuito seleccionado
2. **Simular Temporada** - Simula las 24 carreras con barra de progreso
3. **Calendario** - Visualiza el calendario con el estado de cada GP
4. **Campeonato** - Tabla de clasificación del mundial de pilotos
5. **Historial** - Resultados de carreras anteriores

### Tema Visual

El tema está definido en `f1-theme.css` (596 líneas) e incluye:

- Fondo oscuro principal: `#1a1a2e`
- Color F1 rojo: `#e10600`
- Tarjetas con efecto glassmorphism (fondo semitransparente + blur)
- Tipografía: sistema sans-serif
- Tablas con filas alternadas y hover
- Scrollbars personalizados delgados
- Botones con transiciones suaves
- Colores de posición: oro (#FFD700), plata (#C0C0C0), bronce (#CD7F32)

---

## Datos y Persistencia

### Carga Inicial

Los datos base se cargan desde archivos JSON en `src/main/resources/data/`:

- `circuitos.json` - Calendario de 24 circuitos con datos reales
- `pilotos.json` - 22 pilotos con equipos y habilidades
- `vehiculos.json` - Vehículos por equipo con modos de rendimiento
- `equipos.json` - Escuderías con información asociada

### Persistencia en Ejecución

- Los resultados de carreras se guardan en `historial/resultados.json`
- Los equipos modificados se persisten a disco
- Las configuraciones de vehículo se guardan por separado
- El historial se recupera automáticamente al reiniciar la aplicación

---

## Autores

**Andrey Julian
**Sebastián Salas**

- GitHub: [@Jsebastiansalas](https://github.com/Jsebastiansalas)

---

## Licencia

Proyecto académico. Todos los derechos reservados.

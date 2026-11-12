# EcoTrack

**Aplicación web para la gestión de recolección de residuos y reciclaje por zonas
de Guayaquil (Ecuador).** API REST en Java (Spring Boot) y cliente en React +
TypeScript con un mapa interactivo en Leaflet.

**Demo:** <https://andreewcore.github.io/EcoTrack/> (modo demo: los datos viven en
tu navegador).

EcoTrack nació como proyecto académico de **Estructuras de Datos** con JavaFX: las
colecciones del paquete `TDA` (lista circular doblemente enlazada, pila, cola,
cola de prioridad, mapas hash y árbol) están escritas a mano y el dominio sigue
apoyándose en ellas. La versión de escritorio original queda en el tag `v0.0.0`.

---

## Qué hace

- **Residuos**: registrar, editar y eliminar residuos (nombre, tipo, peso, zona y
  prioridad ambiental 1–5, sugerida según el tipo); ordenar por peso, tipo o
  prioridad; filtrar por tipo, zona y estado; paginar.
- **Rutas**: mapa de las 18 zonas coloreado por **utilidad ambiental**
  (`u = peso recolectado − peso pendiente`): verde ≥ 0, amarillo hasta −500 kg,
  rojo por debajo. La flota de vehículos está en una **cola de prioridad**: al
  despachar sale el vehículo de la zona más crítica, recoge sus residuos más
  prioritarios hasta su capacidad y los lleva al centro.
- **Centro de reciclaje**: los residuos recogidos esperan en una **pila (LIFO)**;
  *Procesar* toma siempre el del tope.
- **Estadísticas**: peso y cantidad por tipo, pendiente/recolectado y utilidad por
  zona, con gráficos y tablas.
- **Descargar / Subir datos**: exportar los residuos a CSV e importarlos,
  eligiendo en cada subida si se añaden o reemplazan a los actuales.

## Arquitectura

```
EcoTrack/
├── backend/    API REST · Java 25 · Spring Boot 4.1 · Maven (./mvnw)
│   └── src/main/java/dev/andreewcodes/ecotrack/
│       ├── TDA/          estructuras de datos propias
│       ├── dominio/      Residuo, Zona, Vehiculo, Despacho, CentroReciclaje, EstadisticasMap…
│       ├── Utilitaria/   DataManager (CSV), comparadores, ResiduoIterator
│       ├── servicio/     EcoTrackService: fachada sincronizada + persistencia
│       └── api/          controladores REST, DTOs y manejo de errores
└── frontend/   React 19 · TypeScript · Vite · Tailwind · react-leaflet · Recharts (pnpm)
    └── src/
        ├── api/          contrato común + cliente HTTP + motor demo (localStorage)
        ├── pages/        Inicio, Residuos, Rutas, CentroReciclaje, Estadisticas
        ├── components/   piezas de UI con la estética de la versión JavaFX
        └── data/         zonas.geojson (OpenStreetMap) y datos de ejemplo
```

| Estructura propia | Dónde se usa |
|---|---|
| `CircularDoublyLinkedList` (+ iterador bidireccional) | `ListaResiduos`, `ListaZonas` |
| `ColaPrioridad` | Cola de despacho de vehículos (`Despacho`) |
| `Cola` | Carga del vehículo durante un despacho |
| `Pila` | Residuos en espera en `CentroReciclaje` |
| `HashMap` / `TreeMap` | Estadísticas por tipo y por zona (`EstadisticasMap`) |
| `ResiduoIterator` | Paginación de `GET /api/residuos` |

**Persistencia:** archivos CSV en `backend/data/` (`residuos.csv`,
`vehiculos.csv`), sin base de datos. La primera vez se cargan datos de ejemplo.

**Mapa:** los polígonos de las zonas se generan desde límites de OpenStreetMap
(© OpenStreetMap contributors, ODbL) con `pnpm zonas` (`frontend/scripts/zonas-osm.mjs`).

## Requisitos

- **JDK 25 o superior** (Maven llega con el wrapper `./mvnw`).
- **Node.js 22+** y **pnpm** (no usar npm).

## Ejecutar en local

```bash
# Terminal 1: API en http://localhost:8080/api
cd backend
./mvnw spring-boot:run

# Terminal 2: web en http://localhost:5173
cd frontend
pnpm install
pnpm dev
```

Solo el frontend, sin backend: `VITE_MODO=demo pnpm dev`.

### Builds de producción

```bash
cd backend && ./mvnw package                  # target/ecotrack-backend-1.0.0.jar
java -jar target/ecotrack-backend-1.0.0.jar   # API sin modo desarrollo

cd frontend && pnpm build                     # frontend/dist/ contra la API (VITE_API_URL)
VITE_MODO=demo VITE_BASE=/EcoTrack/ pnpm build  # build de la demo de GitHub Pages
```

Configuración de la API (`application.properties` o `--clave=valor`):
`ecotrack.data-dir` (directorio de los CSV), `ecotrack.semilla` (datos de ejemplo)
y `ecotrack.cors.origenes` (orígenes permitidos).

### Pruebas

```bash
cd backend && ./mvnw test     # JUnit: TDA, flujo de recolección, DataManager, API
cd frontend && pnpm test      # Vitest: motor demo y reglas de dominio
cd frontend && pnpm lint      # oxlint
```

## API REST

Errores en formato RFC 9457 (`application/problem+json`) con el motivo en `detail`.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/residuos?tipo&zona&estado&orden&dir&pagina&tamano` | Lista paginada con filtros y orden (`peso`, `tipo`, `prioridad`, `id`) |
| GET/PUT/DELETE | `/api/residuos/{id}` | Consultar, editar (solo pendientes) o eliminar (no si está en el centro) |
| POST | `/api/residuos` | Registrar un residuo |
| GET | `/api/zonas` | GeoJSON de las 18 zonas con sus pesos y utilidad |
| GET | `/api/zonas/estado` | Pesos y utilidad de cada zona |
| GET/POST | `/api/vehiculos` | Flota en orden de despacho / registrar vehículo |
| DELETE | `/api/vehiculos/{id}` | Retirar un vehículo |
| POST | `/api/despachos` | Despachar el vehículo de la zona más crítica |
| GET | `/api/centro` | Pila del centro de reciclaje (tope primero) |
| POST | `/api/centro/procesar` | Procesar el residuo del tope |
| GET | `/api/estadisticas` | Estadísticas por tipo y por zona |
| GET | `/api/datos/exportar` | Descargar los residuos en CSV |
| POST | `/api/datos/importar?modo=agregar\|reemplazar` | Subir un CSV (multipart, campo `archivo`) |

## Despliegue

- **GitHub Pages** publica el frontend en **modo demo** en cada push a `main`
  (`.github/workflows/pages.yml`). GitHub Pages solo sirve archivos estáticos,
  así que la demo replica en TypeScript las reglas del backend y guarda los datos
  en `localStorage`.
- La **API** es un jar autónomo (`java -jar`) para ejecutar donde haya un JDK 25.
- **CI** (`.github/workflows/ci.yml`): compila y prueba backend y frontend en
  cada PR.

## Flujo de trabajo

Modelo de ramas forward-only y solo por Pull Request:
`feature/* → dev → staging → main`, con un tag `vX.Y.Z` en `main` por release.

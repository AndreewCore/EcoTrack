# EcoTrack

**Aplicación de escritorio en Java + JavaFX para la gestión de recolección de
residuos y reciclaje por zonas de Guayaquil (Ecuador).**

EcoTrack nació como proyecto académico de **Estructuras de Datos**: las
colecciones del paquete `TDA` están escritas a mano (no se usa `java.util`) y el
resto del dominio depende directamente de ellas. Esta rama `main` conserva la
**línea base JavaFX** del proyecto, etiquetada como `v0.0.0`.

---

## Qué hace

- **Gestión de residuos**: registrar residuos con su tipo, peso y zona; filtrar
  por tipo, ordenar por peso y consultar estadísticas.
- **Zonas de Guayaquil**: ~18 zonas modeladas como enum (`ZonaGuayaquil`),
  con peso pendiente/recolectado por zona.
- **Reciclaje y logística**: centros de reciclaje, vehículos y despachos.
- **Estadísticas** (`EstadisticasMap`): agregación de residuos en estructuras
  `HashMap`/`TreeMap` propias, por tipo de residuo y por zona (peso por tipo,
  pendiente/recolectado y utilidad por zona).
- **Persistencia CSV** (`DataManager`): lectura/escritura de un archivo plano
  (`residuos_ecotrack.txt`) relativo al directorio de trabajo.

## Cómo funciona (arquitectura)

El código fuente en `backend/src/main/java/` se organiza en cuatro capas:

| Capa | Paquete | Rol |
|------|---------|-----|
| Estructuras de datos | `TDA/` | Colecciones genéricas propias: `ArrayList`, `HashMap`, `TreeMap`, `CircularDoublyLinkedList`, `Pila`, `Cola`, `ColaPrioridad`, e interfaces `List`/`Map`/`Entry`. Son la base de todo lo demás. |
| Dominio | `ecotrack/` | Modelo y lógica de negocio: `Residuo`, `TipoResiduo`, `Zona`, `ZonaGuayaquil`, `Vehiculo`, `Despacho`, `CentroReciclaje`, `EstadisticasMap`, `Main`. |
| Interfaz | `controladores/` | Controladores JavaFX, uno por pantalla (`Ecotrack`, `Residuos`, `Zonas`, `CentroReciclaje`, `Estadisticas`). |
| Utilidades | `Utilitaria/` | Apoyo transversal: `DataManager` (persistencia), comparadores, `ResiduoIterator`. |

- Las **vistas** (`vistas/*.fxml`) y las **imágenes** (`images/`) viven en
  `backend/src/main/resources/` y se
  cargan por ruta de recurso (p. ej. `/vistas/ecotrack.fxml`, `/images/icon.png`).
- El **almacén central de datos** es un singleton mutable global:
  `ListaResiduos.getListaResiduosGlobal()` devuelve una
  `CircularDoublyLinkedList<Residuo>` que toda la app lee y escribe.
- La **navegación** vive en `EcotrackController`: cada `switchToScene*` carga un
  FXML y reemplaza la `Scene` sobre el `Stage`.

## Requisitos

- **JDK 25 o superior.** El código se compila con `--release 25` (Java 25 LTS).
- Nada más: **Maven** llega con el wrapper (`./mvnw`) y **JavaFX 25** se
  descarga como dependencia, sin instalar ningún SDK aparte.

## Compilar y ejecutar

El backend es un proyecto **Maven** en `backend/`. El punto de entrada es
`ecotrack.Main`.

```bash
cd backend
./mvnw javafx:run    # app de escritorio JavaFX (se retira en v1.0.0)
./mvnw package       # empaqueta target/ecotrack-backend-<versión>.jar
./mvnw clean         # elimina target/
```

En Windows se usa `mvnw.cmd` en lugar de `./mvnw`.

> No hay pruebas automatizadas en el repositorio (no hay JUnit ni fuentes `test/`).

---

## API REST (Spring Boot)

El backend expone el dominio como API REST en `http://localhost:8080/api`
(Spring Boot 4.1, Java 25). Los datos se guardan en CSV en `backend/data/`
(`residuos.csv`, `vehiculos.csv`); la primera vez se cargan datos de ejemplo.

```bash
cd backend
./mvnw spring-boot:run     # API en http://localhost:8080
./mvnw test                # pruebas JUnit
```

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/residuos?tipo&zona&estado&orden&dir&pagina&tamano` | Lista paginada con filtros y orden (`peso`, `tipo`, `prioridad`, `id`) |
| GET/PUT/DELETE | `/api/residuos/{id}` | Consultar, editar o eliminar un residuo |
| POST | `/api/residuos` | Registrar un residuo |
| GET | `/api/zonas` | GeoJSON de las 18 zonas con sus pesos y utilidad |
| GET | `/api/zonas/estado` | Pesos y utilidad de cada zona |
| GET/POST | `/api/vehiculos` | Flota en orden de despacho / registrar vehículo |
| DELETE | `/api/vehiculos/{id}` | Retirar un vehículo |
| POST | `/api/despachos` | Despachar el vehículo de la zona más crítica |
| GET | `/api/centro` | Pila del centro de reciclaje |
| POST | `/api/centro/procesar` | Procesar el residuo del tope |
| GET | `/api/estadisticas` | Estadísticas por tipo y por zona |
| GET | `/api/datos/exportar` | Descargar los residuos en CSV |
| POST | `/api/datos/importar?modo=agregar\|reemplazar` | Subir un CSV (multipart, campo `archivo`) |

## Frontend

Cliente web en `frontend/` (React + TypeScript + Vite, gestionado con **pnpm**):

```bash
cd frontend
pnpm install
pnpm dev        # http://localhost:5173
```

---

## Estructura del repositorio

| Carpeta | Contenido |
|---------|-----------|
| `backend/` | Aplicación Java con Maven (hoy JavaFX; pasará a Spring Boot). |
| `frontend/` | Cliente React + TypeScript + Vite (pnpm). |

---

## Estado del proyecto — migración en curso

> **Esta rama `main` (tag `v0.0.0`) es la versión final de la etapa JavaFX y se
> mantiene como línea base histórica.**
>
> EcoTrack está siendo **migrado** de *Java + JavaFX* a un monorepo
> **Java (Spring Boot) + React + Leaflet**:
>
> - **Backend** — Java (Spring Boot) exponiendo el dominio y las zonas como
>   API REST / GeoJSON, reutilizando la lógica actual.
> - **Frontend** — React + TypeScript con **Leaflet** para el mapa interactivo
>   de Guayaquil (reemplaza el intercambio de imágenes PNG por zonas coloreables).
>
> El desarrollo sigue un modelo de ramas estricto, forward-only y solo por Pull
> Request: `feature/* → dev → staging → main`.

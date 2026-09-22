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

El código fuente en `src/` se organiza en cuatro capas:

| Capa | Paquete | Rol |
|------|---------|-----|
| Estructuras de datos | `TDA/` | Colecciones genéricas propias: `ArrayList`, `HashMap`, `TreeMap`, `CircularDoublyLinkedList`, `Pila`, `Cola`, `ColaPrioridad`, e interfaces `List`/`Map`/`Entry`. Son la base de todo lo demás. |
| Dominio | `ecotrack/` | Modelo y lógica de negocio: `Residuo`, `TipoResiduo`, `Zona`, `ZonaGuayaquil`, `Vehiculo`, `Despacho`, `CentroReciclaje`, `EstadisticasMap`, `Main`. |
| Interfaz | `controladores/` | Controladores JavaFX, uno por pantalla (`Ecotrack`, `Residuos`, `Zonas`, `CentroReciclaje`, `Estadisticas`). |
| Utilidades | `Utilitaria/` | Apoyo transversal: `DataManager` (persistencia), comparadores, `ResiduoIterator`. |

- Las **vistas** (`src/vistas/*.fxml`) y las **imágenes** (`src/images/`) se
  cargan por ruta de recurso (p. ej. `/vistas/ecotrack.fxml`, `/images/icon.png`).
- El **almacén central de datos** es un singleton mutable global:
  `ListaResiduos.getListaResiduosGlobal()` devuelve una
  `CircularDoublyLinkedList<Residuo>` que toda la app lee y escribe.
- La **navegación** vive en `EcotrackController`: cada `switchToScene*` carga un
  FXML y reemplaza la `Scene` sobre el `Stage`.

## Requisitos

Ambos están fijados de forma estricta en la configuración del proyecto:

- **JDK 25** — `nbproject/project.properties` fija
  `javac.source=25`, `javac.target=25`, `platform.active=JDK_25`.
- **JavaFX SDK externo** (no viene empaquetado). Los argumentos de ejecución
  apuntan a `--module-path <ruta>/javafx-sdk-25.0.2/lib --add-modules
  javafx.controls,javafx.fxml`. Ese SDK debe existir en esa ruta o el lanzamiento
  falla.

## Compilar y ejecutar

El proyecto se construye con **NetBeans Ant** (`build.xml` delega en
`nbproject/build-impl.xml`). El punto de entrada es `ecotrack.Main`.

```bash
ant compile      # compila a build/
ant run          # compila + lanza la app JavaFX
ant jar          # empaqueta dist/EcoTrack.jar
ant clean        # elimina build/ y dist/
```

> No hay pruebas automatizadas en el repositorio (no hay JUnit ni fuentes `test/`).

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
> El plan de ramas, el roadmap de aprendizaje y el análisis del código están
> documentados en `MIGRACION_GIT.txt`, `PLAN_APRENDIZAJE.txt` y `ANALISIS.txt`.
> El desarrollo sigue un modelo de ramas estricto, forward-only y solo por Pull
> Request: `feature/* → dev → staging → main`.

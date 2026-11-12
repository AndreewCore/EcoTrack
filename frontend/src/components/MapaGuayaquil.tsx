import type { Feature, FeatureCollection, Geometry } from 'geojson'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { useEffect, useMemo, useRef } from 'react'
import { GeoJSON, MapContainer, TileLayer, useMap } from 'react-leaflet'
import zonasCrudas from '../data/zonas.geojson?raw'
import { NOMBRE_ZONA, formatearKg, nivelPorUtilidad, type NivelZona } from '../lib/dominio'
import type { EstadoZona, ZonaGuayaquil } from '../types'

type PropsZona = { id: ZonaGuayaquil; nombre: string; origen: string }
type ColeccionZonas = FeatureCollection<Geometry, PropsZona>

// Se parsea una sola vez: el archivo es estático y lo comparten todas las instancias.
const ZONAS_GEOJSON = JSON.parse(zonasCrudas) as ColeccionZonas
const LIMITES = L.geoJSON(ZONAS_GEOJSON).getBounds()

const COLOR_NIVEL: Record<NivelZona, string> = {
  verde: '#22b14c',
  amarillo: '#f5c518',
  rojo: '#e53935',
}

interface MapaGuayaquilProps {
  zonas: EstadoZona[]
  seleccionada: ZonaGuayaquil | null
  onSeleccionar: (zona: ZonaGuayaquil) => void
}

/**
 * Mapa interactivo de Guayaquil: cada zona es un polígono coloreado según su
 * utilidad ambiental (sustituye a los PNG verde/amarillo/rojo de JavaFX).
 */
export function MapaGuayaquil({ zonas, seleccionada, onSeleccionar }: MapaGuayaquilProps) {
  const porZona = useMemo(() => new Map(zonas.map((z) => [z.zona, z])), [zonas])

  // Leaflet guarda los manejadores al crear la capa; la ref evita que queden con datos viejos.
  const onSeleccionarRef = useRef(onSeleccionar)
  useEffect(() => {
    onSeleccionarRef.current = onSeleccionar
  })

  /** Estilo de cada polígono: color por nivel y borde grueso si está seleccionado. */
  function estilo(feature?: Feature<Geometry, PropsZona>): L.PathOptions {
    const id = feature?.properties.id
    const estado = id ? porZona.get(id) : undefined
    const nivel = nivelPorUtilidad(estado?.utilidad ?? 0)
    const activa = id === seleccionada
    return {
      color: activa ? '#1f1f1f' : '#ffffff',
      weight: activa ? 4 : 2,
      fillColor: COLOR_NIVEL[nivel],
      fillOpacity: activa ? 0.8 : 0.55,
    }
  }

  /** Tooltip con el nombre y popup con los pesos, como el panel de RutasMenu. */
  function alCrearZona(feature: Feature<Geometry, PropsZona>, capa: L.Layer) {
    const id = feature.properties.id
    capa.on('click', () => onSeleccionarRef.current(id))
    ;(capa as L.Path).bindTooltip(NOMBRE_ZONA[id], { sticky: true, className: 'font-semibold' })
  }

  return (
    <MapContainer
      bounds={LIMITES}
      boundsOptions={{ padding: [8, 8] }}
      scrollWheelZoom
      className="size-full min-h-[22rem] rounded-xl"
      aria-label="Mapa de zonas de Guayaquil"
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {/* La key fuerza a recrear la capa cuando cambian los datos o la selección. */}
      <GeoJSON key={`${seleccionada}-${zonas.map((z) => z.utilidad).join(',')}`} data={ZONAS_GEOJSON} style={estilo} onEachFeature={alCrearZona} />
      <PopupZona estado={seleccionada ? porZona.get(seleccionada) : undefined} />
    </MapContainer>
  )
}

/** Abre un popup sobre la zona seleccionada con sus pesos y su utilidad. */
function PopupZona({ estado }: { estado?: EstadoZona }) {
  const mapa = useMap()
  useEffect(() => {
    if (!estado) return
    const feature = ZONAS_GEOJSON.features.find((f) => f.properties.id === estado.zona)
    if (!feature) return
    const centro = L.geoJSON(feature).getBounds().getCenter()
    const popup = L.popup({ autoPan: true, closeButton: true })
      .setLatLng(centro)
      .setContent(
        `<strong>${NOMBRE_ZONA[estado.zona]}</strong><br/>` +
          `Recolectado: ${formatearKg(estado.pesoRecolectado)}<br/>` +
          `Pendiente: ${formatearKg(estado.pesoPendiente)}<br/>` +
          `Utilidad: ${formatearKg(estado.utilidad)}`,
      )
      .openOn(mapa)
    return () => {
      mapa.closePopup(popup)
    }
  }, [estado, mapa])
  return null
}

/** Leyenda de colores con los umbrales de utilidad. */
export function LeyendaMapa() {
  const items: { nivel: NivelZona; texto: string }[] = [
    { nivel: 'verde', texto: 'Utilidad ≥ 0: zona limpia' },
    { nivel: 'amarillo', texto: '-500 a 0 kg: atención' },
    { nivel: 'rojo', texto: '< -500 kg: foco crítico' },
  ]
  return (
    <ul className="grid gap-1 text-xs">
      {items.map((i) => (
        <li key={i.nivel} className="flex items-center gap-2">
          <span className="size-3.5 rounded-sm" style={{ backgroundColor: COLOR_NIVEL[i.nivel] }} aria-hidden="true" />
          {i.texto}
        </li>
      ))}
    </ul>
  )
}

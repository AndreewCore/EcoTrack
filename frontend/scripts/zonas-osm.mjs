// Genera src/data/zonas.geojson (y la copia del backend) con los polígonos de las
// 18 zonas de ZonaGuayaquil a partir de límites de OpenStreetMap.
//
// Uso: pnpm zonas            (descarga de Overpass y regenera)
//      pnpm zonas --cache    (reutiliza scripts/.osm-cache.json)
//
// Datos © OpenStreetMap contributors, ODbL 1.0. OSM no tiene un límite oficial
// para cada zona del enum, así que cada zona es la unión de sectores municipales
// (admin_level 10) o de urbanizaciones; las zonas de Samborondón salen de recortar
// el límite cantonal. El campo `origen` de cada feature documenta cómo se armó.

import { bboxClip, difference, featureCollection, simplify, truncate, union } from '@turf/turf'
import osmtogeojson from 'osmtogeojson'
import { existsSync, mkdirSync, readFileSync, writeFileSync } from 'node:fs'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const raiz = dirname(fileURLToPath(import.meta.url))
const CACHE = resolve(raiz, '.osm-cache.json')
const SALIDAS = [
  resolve(raiz, '../src/data/zonas.geojson'),
  resolve(raiz, '../../backend/src/main/resources/zonas.geojson'),
]

const SAMBORONDON_CANTON = 2674241

// El orden importa: cada zona se recorta con las anteriores para que no se solapen,
// así que los sectores municipales (más fiables) van antes que las urbanizaciones.
const ZONAS = [
  { id: 'CENTRO', nombre: 'Centro de Guayaquil', relaciones: [2032997, 2032995, 2032971, 2032992, 2033000, 2032991, 2033865, 2033929, 2033861, 2033860, 2033863, 10298644, 10298057, 10316154] },
  { id: 'PARQUE_CENTENARIO', nombre: 'Parque Centenario', relaciones: [2032824, 2032978, 2032972, 2032976, 2032985, 2032974, 2032975, 2032979, 2032982, 10334645, 10334865, 10335274, 10316244, 10316370, 10319344] },
  { id: 'GUASMO', nombre: 'Guasmo', relaciones: [2032981, 2032980, 2032996, 2032977, 2032993, 2032990, 10371853, 10371854] },
  { id: 'ISLA_TRINITARIA', nombre: 'Isla Trinitaria', relaciones: [2032983] },
  { id: 'PORTETE', nombre: 'Portete', relaciones: [2033912, 2033907, 2033910, 2033924, 2033913, 2033927, 10312676] },
  { id: 'FAE', nombre: 'FAE', relaciones: [2033928] },
  { id: 'URDESA_KENNEDY', nombre: 'Urdesa / Kennedy', relaciones: [2048286, 2033920, 2033911, 10295003, 10344872, 10312697, 10312698, 10295044, 10295041, 10351733, 10295042, 10295043, 10363519, 10363520, 10298050, 10298049, 10298051, 10298052] },
  { id: 'SAN_FELIPE_MAPASINGUE', nombre: 'San Felipe / Mapasingue', relaciones: [2048285, 10275022, 10294850] },
  { id: 'PROSPERINA_CEIBOS', nombre: 'Prosperina / Ceibos', relaciones: [2048288, 2048283, 10340612, 10275933, 10275837, 10312688] },
  { id: 'SAUCES_SAMANES', nombre: 'Sauces / Samanes', relaciones: [2033926, 2033917, 2033909, 2048278, 2033908, 2048280, 10291241, 10291231, 10340390, 10340245, 10291214, 10291211, 10291220, 10291221, 10291275, 10291276, 10294237, 10336157, 10336306, 10339110, 10326899] },
  { id: 'VERGELES', nombre: 'Vergeles', relaciones: [2033919, 2033862, 2033925, 10343873, 10344027, 10344028, 10344029, 10344031, 10344032, 10344033] },
  { id: 'BASTION', nombre: 'Bastión', relaciones: [2048279, 2048289, 2033916] },
  { id: 'FORTIN_MONTE_SINAI', nombre: 'Fortín / Monte Sinaí', relaciones: [2048287, 2048282] },
  { id: 'VIA_COSTA_1', nombre: 'Vía a la Costa 1', relaciones: [2033922, 2033923] },
  { id: 'VIA_COSTA_2', nombre: 'Vía a la Costa 2', relaciones: [10363521, 10363523, 10363524, 10363525, 10363526, 10363527, 10363528, 10363531, 10363532, 10363748] },
  { id: 'ISLA_MOCOLI', nombre: 'Isla Mocolí', vias: [33774386] },
  // Península sur de Samborondón, entre los ríos Daule y Babahoyo.
  { id: 'ENTRE_RIOS_PUNTILLA', nombre: 'Entre Ríos / La Puntilla', recorteCanton: [-79.885, -2.175, -79.83, -2.128] },
  // Corredor urbano de la vía a Samborondón, al norte de la península.
  { id: 'SAMBORONDON', nombre: 'Samborondón', recorteCanton: [-79.895, -2.128, -79.83, -2.06] },
]

/** Descarga de Overpass las relaciones y vías citadas, con su geometría completa. */
async function descargar() {
  const relaciones = [SAMBORONDON_CANTON, ...ZONAS.flatMap((z) => z.relaciones ?? [])]
  const vias = ZONAS.flatMap((z) => z.vias ?? [])
  const consulta = `[out:json][timeout:180];(relation(id:${relaciones.join(',')});way(id:${vias.join(',')}););out body;>;out skel qt;`
  const res = await fetch('https://overpass-api.de/api/interpreter', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'User-Agent': 'ecotrack-zonas-script/1.0' },
    body: new URLSearchParams({ data: consulta }),
  })
  if (!res.ok) throw new Error(`Overpass respondió ${res.status}`)
  return res.json()
}

/** Une una lista de polígonos en uno solo (o null si no hay ninguno). */
function unir(poligonos) {
  const validos = poligonos.filter(Boolean)
  if (validos.length === 0) return null
  if (validos.length === 1) return validos[0]
  return union(featureCollection(validos))
}

const usarCache = process.argv.includes('--cache') && existsSync(CACHE)
const osm = usarCache ? JSON.parse(readFileSync(CACHE, 'utf8')) : await descargar()
if (!usarCache) writeFileSync(CACHE, JSON.stringify(osm))

// Índice "relation/123" / "way/456" -> polígono GeoJSON.
const porId = new Map()
for (const f of osmtogeojson(osm).features) {
  const g = f.geometry
  if (g?.type === 'Polygon' || g?.type === 'MultiPolygon') porId.set(f.id, f)
  // osmtogeojson no trata place=island como área: una vía cerrada se convierte a mano.
  else if (g?.type === 'LineString' && g.coordinates.length > 3 && g.coordinates[0].join() === g.coordinates.at(-1).join())
    porId.set(f.id, { ...f, geometry: { type: 'Polygon', coordinates: [g.coordinates] } })
}

const canton = porId.get(`relation/${SAMBORONDON_CANTON}`)
let ocupado = null
const features = []
for (const zona of ZONAS) {
  let forma
  let origen
  if (zona.recorteCanton) {
    forma = bboxClip(canton, zona.recorteCanton)
    origen = 'recorte del cantón Samborondón (OSM)'
  } else {
    const ids = [...(zona.relaciones ?? []).map((r) => `relation/${r}`), ...(zona.vias ?? []).map((w) => `way/${w}`)]
    const faltan = ids.filter((id) => !porId.has(id))
    if (faltan.length) console.warn(`${zona.id}: sin geometría para ${faltan.join(', ')}`)
    forma = unir(ids.map((id) => porId.get(id)))
    origen = zona.vias ? 'polígono de la isla (OSM)' : 'unión de sectores y urbanizaciones (OSM admin_level 10)'
  }
  if (!forma) throw new Error(`La zona ${zona.id} quedó vacía`)
  if (ocupado) forma = difference(featureCollection([forma, ocupado])) ?? forma
  ocupado = unir([ocupado, forma])

  const limpia = truncate(simplify(forma, { tolerance: 0.00015, highQuality: true }), { precision: 5 })
  features.push({ type: 'Feature', properties: { id: zona.id, nombre: zona.nombre, origen }, geometry: limpia.geometry })
}

const salida = {
  type: 'FeatureCollection',
  attribution: '© OpenStreetMap contributors, ODbL 1.0',
  features,
}
for (const ruta of SALIDAS) {
  mkdirSync(dirname(ruta), { recursive: true })
  writeFileSync(ruta, JSON.stringify(salida) + '\n')
  console.log(`Escrito ${ruta}`)
}

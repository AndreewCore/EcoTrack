import type { ConsultaResiduos } from '../types'
import { ErrorApi, type EcoTrackApi } from './contrato'

/**
 * Hace una petición y devuelve el JSON. Los errores RFC 9457 de la API se
 * convierten en ErrorApi con su `detail`; un fallo de red da un mensaje claro.
 */
async function pedir<T>(base: string, ruta: string, init?: RequestInit): Promise<T> {
  let res: Response
  try {
    res = await fetch(`${base}${ruta}`, init)
  } catch {
    throw new ErrorApi(`No se pudo conectar con la API (${base}). ¿Está levantado el backend?`, 0)
  }
  if (!res.ok) {
    let detalle = `Error ${res.status}`
    try {
      const cuerpo = await res.json()
      detalle = cuerpo.detail ?? cuerpo.title ?? detalle
    } catch {
      // Respuesta sin JSON: se queda el mensaje genérico con el código.
    }
    throw new ErrorApi(detalle, res.status)
  }
  if (res.status === 204) return undefined as T
  const tipo = res.headers.get('Content-Type') ?? ''
  return (tipo.includes('json') ? res.json() : res.text()) as Promise<T>
}

/** Cuerpo JSON con su cabecera. */
function json(metodo: string, cuerpo: unknown): RequestInit {
  return { method: metodo, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(cuerpo) }
}

/** Convierte la consulta en query string omitiendo los filtros vacíos. */
function query(c: ConsultaResiduos): string {
  const params = new URLSearchParams()
  for (const [k, v] of Object.entries(c)) {
    if (v !== undefined && v !== '') params.set(k, String(v))
  }
  const s = params.toString()
  return s ? `?${s}` : ''
}

/** Implementación contra la API Spring Boot. */
export function crearApiHttp(base: string): EcoTrackApi {
  return {
    modo: 'api',
    listarResiduos: (c = {}) => pedir(base, `/residuos${query(c)}`),
    crearResiduo: (e) => pedir(base, '/residuos', json('POST', e)),
    actualizarResiduo: (id, e) => pedir(base, `/residuos/${id}`, json('PUT', e)),
    eliminarResiduo: (id) => pedir(base, `/residuos/${id}`, { method: 'DELETE' }),
    estadoZonas: () => pedir(base, '/zonas/estado'),
    listarVehiculos: () => pedir(base, '/vehiculos'),
    crearVehiculo: (e) => pedir(base, '/vehiculos', json('POST', e)),
    eliminarVehiculo: (id) => pedir(base, `/vehiculos/${encodeURIComponent(id)}`, { method: 'DELETE' }),
    despachar: () => pedir(base, '/despachos', { method: 'POST' }),
    estadoCentro: () => pedir(base, '/centro'),
    procesarSiguiente: () => pedir(base, '/centro/procesar', { method: 'POST' }),
    estadisticas: () => pedir(base, '/estadisticas'),
    exportarCsv: () => pedir(base, '/datos/exportar'),
    importarCsv: (contenido, modo) => {
      const form = new FormData()
      form.append('archivo', new Blob([contenido], { type: 'text/csv' }), 'residuos.csv')
      return pedir(base, `/datos/importar?modo=${modo}`, { method: 'POST', body: form })
    },
  }
}

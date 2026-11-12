import type { EstadoResiduo, EstadoVehiculo, TipoResiduo, ZonaGuayaquil } from '../types'

/** Nombres legibles de las zonas, tal como los mostraba la app de escritorio. */
export const NOMBRE_ZONA: Record<ZonaGuayaquil, string> = {
  FORTIN_MONTE_SINAI: 'Fortín / Monte Sinaí',
  VERGELES: 'Vergeles',
  SAUCES_SAMANES: 'Sauces / Samanes',
  BASTION: 'Bastión',
  VIA_COSTA_1: 'Vía a la Costa 1',
  VIA_COSTA_2: 'Vía a la Costa 2',
  URDESA_KENNEDY: 'Urdesa / Kennedy',
  SAN_FELIPE_MAPASINGUE: 'San Felipe / Mapasingue',
  SAMBORONDON: 'Samborondón',
  PROSPERINA_CEIBOS: 'Prosperina / Ceibos',
  PORTETE: 'Portete',
  PARQUE_CENTENARIO: 'Parque Centenario',
  ISLA_TRINITARIA: 'Isla Trinitaria',
  ISLA_MOCOLI: 'Isla Mocolí',
  GUASMO: 'Guasmo',
  FAE: 'FAE',
  ENTRE_RIOS_PUNTILLA: 'Entre Ríos / La Puntilla',
  CENTRO: 'Centro de Guayaquil',
}

export const NOMBRE_TIPO: Record<TipoResiduo, string> = {
  ORGANICO: 'Orgánico',
  PLASTICO: 'Plástico',
  VIDRIO: 'Vidrio',
  PAPEL: 'Papel',
  METAL: 'Metal',
  ELECTRONICO: 'Electrónico',
  PELIGROSO: 'Peligroso',
  OTROS: 'Otros',
}

export const NOMBRE_ESTADO: Record<EstadoResiduo, string> = {
  PENDIENTE: 'Pendiente',
  EN_CENTRO: 'En centro',
  PROCESADO: 'Procesado',
}

export const NOMBRE_ESTADO_VEHICULO: Record<EstadoVehiculo, string> = {
  DISPONIBLE: 'Disponible',
  EN_RUTA: 'En ruta',
  CARGADO: 'Cargado',
}

/**
 * Prioridad ambiental por defecto (1–5) de cada tipo. Debe coincidir con
 * TipoResiduo.getPrioridadPorDefecto() del backend.
 */
export const PRIORIDAD_POR_TIPO: Record<TipoResiduo, number> = {
  PELIGROSO: 5,
  ELECTRONICO: 4,
  ORGANICO: 3,
  PLASTICO: 3,
  METAL: 2,
  VIDRIO: 2,
  PAPEL: 1,
  OTROS: 1,
}

export type NivelZona = 'verde' | 'amarillo' | 'rojo'

/**
 * Clasifica una zona por su utilidad ambiental con los mismos umbrales que la
 * versión JavaFX: limpia (≥ 0), en riesgo (≥ -500 kg) o crítica.
 */
export function nivelPorUtilidad(utilidad: number): NivelZona {
  if (utilidad >= 0) return 'verde'
  if (utilidad >= -500) return 'amarillo'
  return 'rojo'
}

/** Formatea un peso en kilogramos con como mucho un decimal. */
export function formatearKg(peso: number): string {
  return `${peso.toLocaleString('es-EC', { maximumFractionDigits: 1 })} kg`
}

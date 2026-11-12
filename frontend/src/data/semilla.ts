import { PRIORIDAD_POR_TIPO } from '../lib/dominio'
import type { Residuo, TipoResiduo, VehiculoEntrada, ZonaGuayaquil } from '../types'

// Datos de ejemplo para el modo demo. Mismos valores que backend/src/main/resources/semilla/*.csv
// para que la demo de GitHub Pages y la API local arranquen iguales.

type Fila = [nombre: string, tipo: TipoResiduo, peso: number, zona: ZonaGuayaquil]

const FILAS: Fila[] = [
  ['Botellas PET', 'PLASTICO', 120, 'BASTION'],
  ['Restos de mercado', 'ORGANICO', 340, 'CENTRO'],
  ['Cartón corrugado', 'PAPEL', 85, 'URDESA_KENNEDY'],
  ['Baterías de auto', 'PELIGROSO', 60, 'GUASMO'],
  ['Computadoras viejas', 'ELECTRONICO', 95, 'SAUCES_SAMANES'],
  ['Latas de aluminio', 'METAL', 45, 'PROSPERINA_CEIBOS'],
  ['Frascos de vidrio', 'VIDRIO', 70, 'SAMBORONDON'],
  ['Poda de árboles', 'ORGANICO', 410, 'VIA_COSTA_1'],
  ['Fundas plásticas', 'PLASTICO', 150, 'ISLA_TRINITARIA'],
  ['Escombros mixtos', 'OTROS', 520, 'FORTIN_MONTE_SINAI'],
  ['Periódicos', 'PAPEL', 40, 'PARQUE_CENTENARIO'],
  ['Aceite de cocina usado', 'PELIGROSO', 35, 'PORTETE'],
  ['Chatarra metálica', 'METAL', 260, 'SAN_FELIPE_MAPASINGUE'],
  ['Restos de comida', 'ORGANICO', 610, 'GUASMO'],
  ['Celulares dañados', 'ELECTRONICO', 12, 'ENTRE_RIOS_PUNTILLA'],
  ['Botellas de vidrio', 'VIDRIO', 130, 'CENTRO'],
  ['Envases tetrapak', 'OTROS', 55, 'VERGELES'],
  ['Hojas secas', 'ORGANICO', 220, 'VIA_COSTA_2'],
  ['Tubos fluorescentes', 'PELIGROSO', 18, 'FAE'],
  ['Revistas', 'PAPEL', 30, 'ISLA_MOCOLI'],
  ['Bidones plásticos', 'PLASTICO', 210, 'FORTIN_MONTE_SINAI'],
  ['Electrodomésticos', 'ELECTRONICO', 180, 'BASTION'],
  ['Cáscaras de frutas', 'ORGANICO', 95, 'SAUCES_SAMANES'],
  ['Varillas de construcción', 'METAL', 300, 'VERGELES'],
]

/** Residuos iniciales, todos pendientes y con la prioridad por defecto de su tipo. */
export function residuosSemilla(): Residuo[] {
  return FILAS.map(([nombre, tipo, peso, zona], i) => ({
    id: i + 1,
    nombre,
    tipo,
    peso,
    zona,
    prioridad: PRIORIDAD_POR_TIPO[tipo],
    estado: 'PENDIENTE',
    fechaRecoleccion: null,
  }))
}

/** Flota inicial de vehículos recolectores. */
export function vehiculosSemilla(): VehiculoEntrada[] {
  return [
    { id: 'GYE-001', zonaAsignada: 'GUASMO', capacidad: 800 },
    { id: 'GYE-002', zonaAsignada: 'FORTIN_MONTE_SINAI', capacidad: 600 },
    { id: 'GYE-003', zonaAsignada: 'CENTRO', capacidad: 500 },
    { id: 'GYE-004', zonaAsignada: 'BASTION', capacidad: 400 },
    { id: 'GYE-005', zonaAsignada: 'VIA_COSTA_1', capacidad: 450 },
  ]
}

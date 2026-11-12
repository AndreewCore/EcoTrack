// Tipos del dominio compartidos por la API real y el modo demo.
// Reflejan los DTOs que expone el backend Spring Boot (paquete api).

export const TIPOS_RESIDUO = [
  'ORGANICO',
  'PLASTICO',
  'VIDRIO',
  'PAPEL',
  'METAL',
  'ELECTRONICO',
  'PELIGROSO',
  'OTROS',
] as const
export type TipoResiduo = (typeof TIPOS_RESIDUO)[number]

// Mismo orden que el enum ZonaGuayaquil del backend.
export const ZONAS = [
  'FORTIN_MONTE_SINAI',
  'VERGELES',
  'SAUCES_SAMANES',
  'BASTION',
  'VIA_COSTA_1',
  'VIA_COSTA_2',
  'URDESA_KENNEDY',
  'SAN_FELIPE_MAPASINGUE',
  'SAMBORONDON',
  'PROSPERINA_CEIBOS',
  'PORTETE',
  'PARQUE_CENTENARIO',
  'ISLA_TRINITARIA',
  'ISLA_MOCOLI',
  'GUASMO',
  'FAE',
  'ENTRE_RIOS_PUNTILLA',
  'CENTRO',
] as const
export type ZonaGuayaquil = (typeof ZONAS)[number]

export const ESTADOS_RESIDUO = ['PENDIENTE', 'EN_CENTRO', 'PROCESADO'] as const
export type EstadoResiduo = (typeof ESTADOS_RESIDUO)[number]

export interface Residuo {
  id: number
  nombre: string
  tipo: TipoResiduo
  peso: number
  zona: ZonaGuayaquil
  prioridad: number
  estado: EstadoResiduo
  /** Fecha ISO (yyyy-mm-dd) en que un vehículo lo recogió; null mientras está pendiente. */
  fechaRecoleccion: string | null
}

export interface ResiduoEntrada {
  nombre: string
  tipo: TipoResiduo
  peso: number
  zona: ZonaGuayaquil
  /** Si se omite, se usa la prioridad por defecto del tipo. */
  prioridad?: number
}

export type CriterioOrden = 'peso' | 'tipo' | 'prioridad' | 'id'
export type Direccion = 'asc' | 'desc'

export interface ConsultaResiduos {
  tipo?: TipoResiduo
  zona?: ZonaGuayaquil
  estado?: EstadoResiduo
  orden?: CriterioOrden
  dir?: Direccion
  /** Página 1-based. */
  pagina?: number
  tamano?: number
}

export interface Pagina<T> {
  elementos: T[]
  pagina: number
  totalPaginas: number
  totalElementos: number
}

export interface EstadoZona {
  zona: ZonaGuayaquil
  pesoPendiente: number
  pesoRecolectado: number
  utilidad: number
}

export const ESTADOS_VEHICULO = ['DISPONIBLE', 'EN_RUTA', 'CARGADO'] as const
export type EstadoVehiculo = (typeof ESTADOS_VEHICULO)[number]

export interface Vehiculo {
  id: string
  zonaAsignada: ZonaGuayaquil
  capacidad: number
  estado: EstadoVehiculo
  /** Posición en la cola de despacho (1 = sale primero). */
  turno: number
}

export interface VehiculoEntrada {
  id: string
  zonaAsignada: ZonaGuayaquil
  capacidad: number
}

export interface ResultadoDespacho {
  vehiculo: Vehiculo
  zona: ZonaGuayaquil
  residuos: Residuo[]
  pesoTotal: number
  /** Motivo por el que se recogió menos de lo pendiente (centro lleno, capacidad…), si aplica. */
  aviso: string | null
}

export interface EstadoCentro {
  capacidadMaxima: number
  /** Residuos en la pila, del tope al fondo. */
  pila: Residuo[]
  procesados: number
}

export interface Estadisticas {
  totalResiduos: number
  pesoTotal: number
  pesoPromedio: number
  pesoPorTipo: Partial<Record<TipoResiduo, number>>
  cantidadPorTipo: Partial<Record<TipoResiduo, number>>
  zonas: EstadoZona[]
  tipoMasComun: TipoResiduo | null
  zonaMasPendiente: ZonaGuayaquil | null
  zonaMejorUtilidad: ZonaGuayaquil | null
  zonaPeorUtilidad: ZonaGuayaquil | null
}

export type ModoImportacion = 'agregar' | 'reemplazar'

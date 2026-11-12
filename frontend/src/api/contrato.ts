import type {
  ConsultaResiduos,
  EstadoCentro,
  EstadoZona,
  Estadisticas,
  ModoImportacion,
  Pagina,
  Residuo,
  ResiduoEntrada,
  ResultadoDespacho,
  Vehiculo,
  VehiculoEntrada,
} from '../types'

export interface ResultadoImportacion {
  importados: number
  /** Líneas descartadas, con su número y el motivo. */
  errores: string[]
}

/**
 * Operaciones de datos que usan las pantallas. Hay dos implementaciones con el
 * mismo comportamiento: HTTP contra la API Spring Boot y demo en el navegador.
 */
export interface EcoTrackApi {
  /** 'api' o 'demo', para avisar en la interfaz. */
  readonly modo: 'api' | 'demo'

  listarResiduos(consulta?: ConsultaResiduos): Promise<Pagina<Residuo>>
  crearResiduo(entrada: ResiduoEntrada): Promise<Residuo>
  actualizarResiduo(id: number, entrada: ResiduoEntrada): Promise<Residuo>
  eliminarResiduo(id: number): Promise<void>

  /** Pesos y utilidad ambiental de las 18 zonas. */
  estadoZonas(): Promise<EstadoZona[]>

  /** Flota en el orden en que saldría de la cola de prioridad. */
  listarVehiculos(): Promise<Vehiculo[]>
  crearVehiculo(entrada: VehiculoEntrada): Promise<Vehiculo>
  eliminarVehiculo(id: string): Promise<void>
  despachar(): Promise<ResultadoDespacho>

  /** Pila del centro de reciclaje (tope primero). */
  estadoCentro(): Promise<EstadoCentro>
  procesarSiguiente(): Promise<Residuo>

  estadisticas(): Promise<Estadisticas>

  /** Contenido CSV de todos los residuos. */
  exportarCsv(): Promise<string>
  importarCsv(contenido: string, modo: ModoImportacion): Promise<ResultadoImportacion>
}

/** Error de la capa de datos con un mensaje apto para mostrar al usuario. */
export class ErrorApi extends Error {
  readonly estado: number

  /** @param estado código HTTP equivalente (400 validación, 404 no encontrado, 409 regla de negocio). */
  constructor(mensaje: string, estado = 400) {
    super(mensaje)
    this.name = 'ErrorApi'
    this.estado = estado
  }
}

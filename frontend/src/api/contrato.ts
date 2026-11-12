import type { ConsultaResiduos, EstadoZona, Pagina, Residuo, ResiduoEntrada } from '../types'

/**
 * Operaciones de datos que usan las pantallas. Hay una implementación por modo:
 * en memoria (demo) y, más adelante, HTTP contra la API Spring Boot.
 */
export interface EcoTrackApi {
  listarResiduos(consulta?: ConsultaResiduos): Promise<Pagina<Residuo>>
  crearResiduo(entrada: ResiduoEntrada): Promise<Residuo>
  actualizarResiduo(id: number, entrada: ResiduoEntrada): Promise<Residuo>
  eliminarResiduo(id: number): Promise<void>
  /** Pesos y utilidad ambiental de las 18 zonas. */
  estadoZonas(): Promise<EstadoZona[]>
}

/** Error de la capa de datos con un mensaje apto para mostrar al usuario. */
export class ErrorApi extends Error {
  readonly estado: number

  /** @param estado código HTTP equivalente (400 validación, 404 no encontrado…). */
  constructor(mensaje: string, estado = 400) {
    super(mensaje)
    this.name = 'ErrorApi'
    this.estado = estado
  }
}

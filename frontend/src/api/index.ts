import { crearApiMemoria } from './memoria'

/** Instancia única de la capa de datos que usan todas las pantallas. */
export const api = crearApiMemoria()
export { ErrorApi } from './contrato'

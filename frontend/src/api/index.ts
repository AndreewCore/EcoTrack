import type { EcoTrackApi } from './contrato'
import { crearApiDemo } from './demo'
import { crearApiHttp } from './http'

const modo = import.meta.env.VITE_MODO === 'demo' ? 'demo' : 'api'
const demo = modo === 'demo' ? crearApiDemo() : null

/**
 * Capa de datos que usan todas las pantallas: la API Spring Boot en local o la
 * demo en el navegador (GitHub Pages), según VITE_MODO.
 */
export const api: EcoTrackApi = demo ?? crearApiHttp(import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api')

/** Solo en modo demo: devuelve los datos de ejemplo. */
export const restablecerDemo = demo ? demo.restablecer : null

export { ErrorApi } from './contrato'
export type { ResultadoImportacion } from './contrato'

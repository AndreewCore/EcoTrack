import type { EcoTrackApi } from '../contrato'
import { estadoInicial, MotorDemo, type EstadoDemo } from './motor'

const CLAVE = 'ecotrack-demo-v1'

/** Lee el estado guardado; si no hay o está corrupto, empieza con la semilla. */
function cargar(): EstadoDemo {
  try {
    const crudo = localStorage.getItem(CLAVE)
    if (crudo) {
      const estado = JSON.parse(crudo) as EstadoDemo
      if (estado.version === 1 && Array.isArray(estado.residuos)) return estado
    }
  } catch {
    // Almacenamiento bloqueado (modo privado) o JSON dañado: la demo sigue en memoria.
  }
  return estadoInicial()
}

/** Guarda el estado; si el navegador no lo permite, la demo funciona igual sin persistir. */
function guardar(estado: EstadoDemo) {
  try {
    localStorage.setItem(CLAVE, JSON.stringify(estado))
  } catch {
    // Sin persistencia: no es un error para el usuario.
  }
}

/**
 * API de demostración que corre en el navegador. Envuelve cada llamada en una
 * promesa para tener la misma forma asíncrona que la API real.
 */
export function crearApiDemo(): EcoTrackApi & { restablecer(): Promise<void> } {
  const motor = new MotorDemo(cargar(), guardar)
  /** Ejecuta la operación como asíncrona (los errores se convierten en promesas rechazadas). */
  const asinc = <T>(f: () => T): Promise<T> => Promise.resolve().then(f)
  return {
    modo: 'demo',
    listarResiduos: (c) => asinc(() => motor.listarResiduos(c)),
    crearResiduo: (e) => asinc(() => motor.crearResiduo(e)),
    actualizarResiduo: (id, e) => asinc(() => motor.actualizarResiduo(id, e)),
    eliminarResiduo: (id) => asinc(() => motor.eliminarResiduo(id)),
    estadoZonas: () => asinc(() => motor.estadoZonas()),
    listarVehiculos: () => asinc(() => motor.listarVehiculos()),
    crearVehiculo: (e) => asinc(() => motor.crearVehiculo(e)),
    eliminarVehiculo: (id) => asinc(() => motor.eliminarVehiculo(id)),
    despachar: () => asinc(() => motor.despachar()),
    estadoCentro: () => asinc(() => motor.estadoCentro()),
    procesarSiguiente: () => asinc(() => motor.procesarSiguiente()),
    estadisticas: () => asinc(() => motor.estadisticas()),
    exportarCsv: () => asinc(() => motor.exportarCsv()),
    importarCsv: (contenido, modo) => asinc(() => motor.importarCsv(contenido, modo)),
    /** Vuelve a los datos de ejemplo. */
    restablecer: () =>
      asinc(() => {
        motor.estado = estadoInicial()
        guardar(motor.estado)
      }),
  }
}

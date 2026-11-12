import { useCallback, useEffect, useRef, useState } from 'react'

export interface EstadoDatos<T> {
  datos: T | null
  cargando: boolean
  error: string | null
  /** Vuelve a pedir los datos (tras crear, editar, despachar…). */
  recargar: () => void
}

/**
 * Pide datos asíncronos y expone los tres estados (cargando, error, datos).
 * Se vuelve a ejecutar cuando cambia `clave` (p. ej. la consulta serializada).
 * Mientras carga conserva los datos anteriores para que la tabla no parpadee.
 */
export function useDatos<T>(cargar: () => Promise<T>, clave: string): EstadoDatos<T> {
  const [version, setVersion] = useState(0)
  const [resultado, setResultado] = useState<{ solicitud: string; datos: T | null; error: string | null } | null>(null)
  // `cargar` es una closure nueva en cada render; la clave es la que decide cuándo repetir.
  const cargarRef = useRef(cargar)
  useEffect(() => {
    cargarRef.current = cargar
  })

  const solicitud = `${clave}#${version}`
  useEffect(() => {
    let vigente = true
    cargarRef
      .current()
      .then((datos) => {
        if (vigente) setResultado({ solicitud, datos, error: null })
      })
      .catch((e: unknown) => {
        const error = e instanceof Error ? e.message : String(e)
        if (vigente) setResultado((prev) => ({ solicitud, datos: prev?.datos ?? null, error }))
      })
    // Evita que una respuesta lenta de una consulta anterior pise a la actual.
    return () => {
      vigente = false
    }
  }, [solicitud])

  const recargar = useCallback(() => setVersion((v) => v + 1), [])
  return {
    datos: resultado?.datos ?? null,
    error: resultado?.error ?? null,
    cargando: resultado?.solicitud !== solicitud,
    recargar,
  }
}

export type Mensaje = { texto: string; tipo: 'ok' | 'error' | 'info' } | null

/** Convierte cualquier error en un mensaje para el componente Aviso. */
export function mensajeDeError(e: unknown): Mensaje {
  return { texto: e instanceof Error ? e.message : String(e), tipo: 'error' }
}

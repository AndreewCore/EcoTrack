import { Link } from 'react-router'
import { CLASES_SECCION, type ColorSeccion } from '../lib/colores'
import { IconoVolver } from './Iconos'

/**
 * Pestaña de color de la esquina superior izquierda con la flecha de regreso y
 * el título grande de la pantalla, igual que en las escenas JavaFX.
 */
export function CabeceraSeccion({
  titulo,
  color,
  className = '',
}: {
  titulo: string
  color: ColorSeccion
  className?: string
}) {
  return (
    <header
      className={`${CLASES_SECCION[color].fondo} rounded-br-[2rem] px-5 pt-3 pb-2 text-white sm:px-6 ${className}`}
    >
      <Link
        to="/"
        aria-label="Volver al menú principal"
        className="inline-flex rounded-md p-1 transition-transform hover:-translate-x-1 focus-visible:outline-2 focus-visible:outline-white"
      >
        <IconoVolver className="size-9 sm:size-11" />
      </Link>
      <h1 className="font-titulo text-[clamp(2.4rem,6vw,4rem)] leading-none font-semibold">{titulo}</h1>
    </header>
  )
}

/** Encabezado en forma de píldora de color (columnas de tablas, títulos de cajas). */
export function Pildora({
  children,
  color,
  className = '',
}: {
  children: React.ReactNode
  color: ColorSeccion
  className?: string
}) {
  return (
    <span
      className={`${CLASES_SECCION[color].fondo} etiqueta inline-flex items-center justify-center rounded-xl px-3 py-2 text-center text-xs text-white sm:text-sm ${className}`}
    >
      {children}
    </span>
  )
}

/** Caja blanca con borde grueso del color de la sección. */
export function Panel({
  children,
  color,
  className = '',
}: {
  children: React.ReactNode
  color: ColorSeccion
  className?: string
}) {
  return (
    <section className={`rounded-2xl border-4 bg-white ${CLASES_SECCION[color].borde} ${className}`}>
      {children}
    </section>
  )
}

/**
 * Caja con una pestaña-título que sobresale arriba a la izquierda, como los
 * recuadros "ORDENAR POR" y "FILTRAR POR" de la pantalla de residuos.
 */
export function CajaEtiquetada({
  titulo,
  color,
  children,
  className = '',
}: {
  titulo: string
  color: ColorSeccion
  children: React.ReactNode
  className?: string
}) {
  const c = CLASES_SECCION[color]
  return (
    <fieldset className={`relative rounded-2xl border-4 bg-white px-4 pt-10 pb-4 ${c.borde} ${className}`}>
      <legend className="sr-only">{titulo}</legend>
      <span
        aria-hidden="true"
        className={`etiqueta absolute -top-1 -left-1 rounded-2xl border-4 bg-white px-5 py-1.5 text-sm ${c.borde} ${c.texto}`}
      >
        {titulo}
      </span>
      {children}
    </fieldset>
  )
}

/** Mensaje de aviso bajo los formularios (éxito, error o información). */
export function Aviso({ mensaje }: { mensaje: { texto: string; tipo: 'ok' | 'error' | 'info' } | null }) {
  if (!mensaje) return null
  const estilos = {
    ok: 'bg-white text-residuos border-residuos',
    error: 'bg-white text-red-600 border-red-500',
    info: 'bg-white text-sky-700 border-sky-500',
  }[mensaje.tipo]
  return (
    <p role={mensaje.tipo === 'error' ? 'alert' : 'status'} className={`rounded-xl border-2 px-4 py-2 text-sm font-semibold ${estilos}`}>
      {mensaje.texto}
    </p>
  )
}

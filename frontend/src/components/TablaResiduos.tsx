import { NOMBRE_ESTADO, NOMBRE_TIPO, NOMBRE_ZONA, formatearKg } from '../lib/dominio'
import type { Residuo } from '../types'
import { IconoEditar, IconoEliminar } from './Iconos'
import { CLASES_SECCION, type ColorSeccion } from '../lib/colores'

export type ColumnaResiduo = 'id' | 'nombre' | 'tipo' | 'peso' | 'zona' | 'prioridad' | 'estado'

const TITULO_COLUMNA: Record<ColumnaResiduo, string> = {
  id: 'ID',
  nombre: 'Nombre',
  tipo: 'Tipo',
  peso: 'Peso',
  zona: 'Zona',
  prioridad: 'Prior.',
  estado: 'Estado',
}

interface TablaResiduosProps {
  residuos: Residuo[]
  columnas: ColumnaResiduo[]
  color: ColorSeccion
  vacio?: string
  /** Resalta la primera fila (p. ej. el tope de la pila del centro). */
  destacarPrimera?: boolean
  onEditar?: (r: Residuo) => void
  onEliminar?: (r: Residuo) => void
}

/**
 * Tabla de residuos con encabezados en píldora, como las listas de las escenas
 * originales. Las acciones solo aparecen si se pasan sus callbacks y la regla de
 * negocio las permite: editar solo pendientes y no eliminar lo que está en el centro.
 */
export function TablaResiduos({
  residuos,
  columnas,
  color,
  vacio = 'No hay residuos que mostrar.',
  destacarPrimera = false,
  onEditar,
  onEliminar,
}: TablaResiduosProps) {
  const c = CLASES_SECCION[color]
  const conAcciones = Boolean(onEditar || onEliminar)
  return (
    <div className="overflow-x-auto">
      <table className="w-full border-separate border-spacing-x-2 border-spacing-y-1.5 text-left">
        <thead>
          <tr>
            {columnas.map((col) => (
              <th key={col} scope="col" className="p-0">
                <span
                  className={`${c.fondo} etiqueta block rounded-xl px-3 py-2 text-center text-xs whitespace-nowrap text-white sm:text-sm`}
                >
                  {TITULO_COLUMNA[col]}
                </span>
              </th>
            ))}
            {conAcciones && (
              <th scope="col" className="p-0">
                <span className="sr-only">Acciones</span>
              </th>
            )}
          </tr>
        </thead>
        <tbody>
          {residuos.length === 0 && (
            <tr>
              <td colSpan={columnas.length + (conAcciones ? 1 : 0)} className="px-3 py-8 text-center text-sm text-neutral-500">
                {vacio}
              </td>
            </tr>
          )}
          {residuos.map((r, i) => (
            <tr key={r.id} className={destacarPrimera && i === 0 ? `${c.suave} font-semibold` : 'odd:bg-neutral-50'}>
              {columnas.map((col) => (
                <td key={col} className="rounded-lg px-3 py-1.5 text-sm whitespace-nowrap">
                  {celda(r, col)}
                </td>
              ))}
              {conAcciones && (
                <td className="px-1 py-1 whitespace-nowrap">
                  {onEditar && r.estado === 'PENDIENTE' && (
                    <button
                      type="button"
                      onClick={() => onEditar(r)}
                      aria-label={`Editar ${r.nombre}`}
                      className={`rounded-lg p-1.5 ${c.texto} hover:bg-neutral-100`}
                    >
                      <IconoEditar className="size-5" />
                    </button>
                  )}
                  {onEliminar && r.estado !== 'EN_CENTRO' && (
                    <button
                      type="button"
                      onClick={() => onEliminar(r)}
                      aria-label={`Eliminar ${r.nombre}`}
                      className="rounded-lg p-1.5 text-red-600 hover:bg-red-50"
                    >
                      <IconoEliminar className="size-5" />
                    </button>
                  )}
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

/** Texto que se muestra en cada celda según la columna. */
function celda(r: Residuo, col: ColumnaResiduo): React.ReactNode {
  switch (col) {
    case 'id':
      return r.id
    case 'nombre':
      return r.nombre
    case 'tipo':
      return NOMBRE_TIPO[r.tipo]
    case 'peso':
      return formatearKg(r.peso)
    case 'zona':
      return NOMBRE_ZONA[r.zona]
    case 'prioridad':
      return <Prioridad valor={r.prioridad} />
    case 'estado':
      return NOMBRE_ESTADO[r.estado]
  }
}

/** Prioridad 1–5 como puntos, con el número para lectores de pantalla. */
function Prioridad({ valor }: { valor: number }) {
  return (
    <span className="inline-flex items-center gap-0.5" title={`Prioridad ${valor} de 5`}>
      <span className="sr-only">{valor}</span>
      {[1, 2, 3, 4, 5].map((n) => (
        <span
          key={n}
          aria-hidden="true"
          className={`size-2 rounded-full ${n <= valor ? (valor >= 4 ? 'bg-red-500' : valor >= 3 ? 'bg-amber-400' : 'bg-residuos') : 'bg-neutral-200'}`}
        />
      ))}
    </span>
  )
}

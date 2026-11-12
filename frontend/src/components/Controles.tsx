import { NOMBRE_ESTADO, NOMBRE_TIPO, NOMBRE_ZONA } from '../lib/dominio'
import {
  ESTADOS_RESIDUO,
  TIPOS_RESIDUO,
  ZONAS,
  type ConsultaResiduos,
  type CriterioOrden,
  type Direccion,
  type Pagina,
} from '../types'
import { IconoChevron } from './Iconos'
import { CLASES_SECCION, type ColorSeccion } from '../lib/colores'

const OPCIONES_ORDEN: { valor: string; texto: string }[] = [
  { valor: 'id:asc', texto: 'Registro (ID)' },
  { valor: 'peso:asc', texto: 'Peso (ascendente)' },
  { valor: 'peso:desc', texto: 'Peso (descendente)' },
  { valor: 'tipo:asc', texto: 'Tipo (ascendente)' },
  { valor: 'tipo:desc', texto: 'Tipo (descendente)' },
  { valor: 'prioridad:asc', texto: 'Prioridad (ascendente)' },
  { valor: 'prioridad:desc', texto: 'Prioridad (descendente)' },
]

const claseSelect =
  'w-full rounded-lg border-2 border-neutral-200 bg-white px-2 py-1.5 text-sm outline-none focus-visible:border-current'

/** Selector del criterio de ordenamiento (los tres comparadores, en ambas direcciones). */
export function SelectorOrden({
  consulta,
  onCambiar,
  idEtiqueta,
}: {
  consulta: ConsultaResiduos
  onCambiar: (orden: CriterioOrden, dir: Direccion) => void
  idEtiqueta: string
}) {
  return (
    <select
      aria-labelledby={idEtiqueta}
      className={claseSelect}
      value={`${consulta.orden ?? 'id'}:${consulta.dir ?? 'asc'}`}
      onChange={(e) => {
        const [orden, dir] = e.target.value.split(':') as [CriterioOrden, Direccion]
        onCambiar(orden, dir)
      }}
    >
      {OPCIONES_ORDEN.map((o) => (
        <option key={o.valor} value={o.valor}>
          {o.texto}
        </option>
      ))}
    </select>
  )
}

/** Filtros por tipo, zona y (opcionalmente) estado; un valor vacío significa "todos". */
export function Filtros({
  consulta,
  onCambiar,
  conEstado = true,
}: {
  consulta: ConsultaResiduos
  onCambiar: (cambios: Partial<ConsultaResiduos>) => void
  conEstado?: boolean
}) {
  return (
    <div className="grid gap-2">
      <label className="grid gap-1 text-xs font-semibold">
        Tipo
        <select
          className={claseSelect}
          value={consulta.tipo ?? ''}
          onChange={(e) => onCambiar({ tipo: (e.target.value || undefined) as ConsultaResiduos['tipo'] })}
        >
          <option value="">Todos</option>
          {TIPOS_RESIDUO.map((t) => (
            <option key={t} value={t}>
              {NOMBRE_TIPO[t]}
            </option>
          ))}
        </select>
      </label>
      <label className="grid gap-1 text-xs font-semibold">
        Zona
        <select
          className={claseSelect}
          value={consulta.zona ?? ''}
          onChange={(e) => onCambiar({ zona: (e.target.value || undefined) as ConsultaResiduos['zona'] })}
        >
          <option value="">Todas</option>
          {ZONAS.map((z) => (
            <option key={z} value={z}>
              {NOMBRE_ZONA[z]}
            </option>
          ))}
        </select>
      </label>
      {conEstado && (
        <label className="grid gap-1 text-xs font-semibold">
          Estado
          <select
            className={claseSelect}
            value={consulta.estado ?? ''}
            onChange={(e) => onCambiar({ estado: (e.target.value || undefined) as ConsultaResiduos['estado'] })}
          >
            <option value="">Todos</option>
            {ESTADOS_RESIDUO.map((s) => (
              <option key={s} value={s}>
                {NOMBRE_ESTADO[s]}
              </option>
            ))}
          </select>
        </label>
      )}
    </div>
  )
}

/**
 * Navegación de páginas hacia adelante y atrás (los botones ▲▼ de la escena del
 * centro de reciclaje). En vertical se usa como columna junto a otros controles.
 */
export function Paginador<T>({
  pagina,
  color,
  onIr,
  vertical = false,
}: {
  pagina: Pagina<T> | null
  color: ColorSeccion
  onIr: (n: number) => void
  vertical?: boolean
}) {
  const c = CLASES_SECCION[color]
  const actual = pagina?.pagina ?? 1
  const total = pagina?.totalPaginas ?? 1
  const boton = `${c.fondo} grid place-items-center rounded-xl px-5 py-2 text-white transition hover:brightness-95 disabled:opacity-40 focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-tinta`
  return (
    <nav aria-label="Paginación" className={`flex items-center gap-2 ${vertical ? 'flex-col' : ''}`}>
      <button type="button" className={boton} disabled={actual <= 1} onClick={() => onIr(actual - 1)} aria-label="Página anterior">
        <IconoChevron className="size-5" />
      </button>
      <span className="min-w-20 text-center text-xs font-semibold" aria-live="polite">
        Pág. {actual} / {total}
        {pagina && <span className="block font-normal text-neutral-500">{pagina.totalElementos} en total</span>}
      </span>
      <button type="button" className={boton} disabled={actual >= total} onClick={() => onIr(actual + 1)} aria-label="Página siguiente">
        <IconoChevron abajo className="size-5" />
      </button>
    </nav>
  )
}

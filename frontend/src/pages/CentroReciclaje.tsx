import { useMemo, useState } from 'react'
import { api } from '../api'
import { Filtros, Paginador } from '../components/Controles'
import { IconoAvanzar } from '../components/Iconos'
import { Aviso, CabeceraSeccion, Panel } from '../components/Seccion'
import { TablaResiduos } from '../components/TablaResiduos'
import { NOMBRE_TIPO, NOMBRE_ZONA, formatearKg } from '../lib/dominio'
import { mensajeDeError, useDatos, type Mensaje } from '../lib/useDatos'
import { TIPOS_RESIDUO, type ConsultaResiduos, type Residuo } from '../types'

const TAMANO_PAGINA = 8

type OrdenCentro = 'pila' | 'peso' | 'tipo' | 'prioridad'

/**
 * Centro de reciclaje: la pila (LIFO) de residuos recogidos. Ordenar o filtrar
 * solo cambia la vista; PROCESAR siempre toma el tope de la pila.
 */
export default function CentroReciclaje() {
  const centro = useDatos(() => api.estadoCentro(), 'centro')
  const [orden, setOrden] = useState<OrdenCentro>('pila')
  const [filtro, setFiltro] = useState<ConsultaResiduos>({})
  const [pagina, setPagina] = useState(1)
  const [aviso, setAviso] = useState<Mensaje>(null)
  const [procesando, setProcesando] = useState(false)

  const pila = useMemo(() => centro.datos?.pila ?? [], [centro.datos])
  const tope = pila[0]

  const vista = useMemo(() => {
    const filtrada = pila.filter((r) => (!filtro.tipo || r.tipo === filtro.tipo) && (!filtro.zona || r.zona === filtro.zona))
    const criterios: Record<Exclude<OrdenCentro, 'pila'>, (a: Residuo, b: Residuo) => number> = {
      peso: (a, b) => b.peso - a.peso,
      prioridad: (a, b) => b.prioridad - a.prioridad,
      tipo: (a, b) => TIPOS_RESIDUO.indexOf(a.tipo) - TIPOS_RESIDUO.indexOf(b.tipo),
    }
    return orden === 'pila' ? filtrada : [...filtrada].sort(criterios[orden])
  }, [pila, filtro, orden])

  const totalPaginas = Math.max(1, Math.ceil(vista.length / TAMANO_PAGINA))
  const paginaActual = Math.min(pagina, totalPaginas)
  const elementos = vista.slice((paginaActual - 1) * TAMANO_PAGINA, paginaActual * TAMANO_PAGINA)

  /** Procesa el residuo del tope y refresca la pila. */
  async function procesar() {
    setProcesando(true)
    try {
      const r = await api.procesarSiguiente()
      setAviso({ texto: `Procesado: ${r.nombre} (${formatearKg(r.peso)}, ${NOMBRE_ZONA[r.zona]}).`, tipo: 'ok' })
      centro.recargar()
    } catch (e) {
      setAviso(mensajeDeError(e))
    } finally {
      setProcesando(false)
    }
  }

  const ocupacion = centro.datos ? pila.length / centro.datos.capacidadMaxima : 0
  const claseCaja = 'border-centro rounded-2xl border-4 bg-white p-3'

  return (
    <main className="mx-auto grid min-h-dvh max-w-[1280px] grid-cols-[minmax(0,1fr)] gap-4 pb-4 sm:px-4 lg:grid-cols-[minmax(0,52rem)_minmax(0,1fr)]">
      <div className="flex min-w-0 flex-col gap-3">
        <CabeceraSeccion titulo="Centro de reciclaje" color="centro" className="w-full uppercase sm:w-fit sm:pr-8 lg:-ml-4" />
        <h2 className="font-titulo text-centro px-4 text-[clamp(1.8rem,4vw,2.8rem)] leading-none font-medium sm:px-0">
          Lista de residuos sin procesar
        </h2>

        <Panel color="centro" className="mx-4 min-h-72 p-2 sm:mx-0">
          {centro.error ? (
            <p role="alert" className="p-6 text-center text-sm text-red-600">
              No se pudo cargar el centro: {centro.error}
            </p>
          ) : (
            <TablaResiduos
              residuos={elementos}
              columnas={['id', 'nombre', 'tipo', 'peso', 'zona', 'prioridad']}
              color="centro"
              destacarPrimera={orden === 'pila' && paginaActual === 1 && !filtro.tipo && !filtro.zona}
              vacio={centro.cargando ? 'Cargando…' : 'No hay residuos esperando. Despacha un vehículo desde Rutas.'}
            />
          )}
        </Panel>

        <div className="grid gap-3 px-4 sm:grid-cols-[auto_auto_1fr] sm:items-stretch sm:px-0">
          <div className="grid grid-cols-2 gap-3 sm:grid-cols-1">
            <label className={claseCaja}>
              <span className="etiqueta text-centro block text-xs">Ordenar por</span>
              <select
                className="mt-1 w-full rounded-lg border-2 border-neutral-200 px-2 py-1 text-sm"
                value={orden}
                onChange={(e) => {
                  setOrden(e.target.value as OrdenCentro)
                  setPagina(1)
                }}
              >
                <option value="pila">Pila (tope primero)</option>
                <option value="peso">Peso (mayor primero)</option>
                <option value="prioridad">Prioridad (mayor primero)</option>
                <option value="tipo">Tipo</option>
              </select>
            </label>
            <details className={claseCaja}>
              <summary className="etiqueta text-centro cursor-pointer text-xs">Filtrar por</summary>
              <div className="mt-2">
                <Filtros
                  consulta={filtro}
                  conEstado={false}
                  onCambiar={(c) => {
                    setFiltro((f) => ({ ...f, ...c }))
                    setPagina(1)
                  }}
                />
              </div>
            </details>
          </div>
          <Paginador
            vertical
            color="centro"
            pagina={{ elementos, pagina: paginaActual, totalPaginas, totalElementos: vista.length }}
            onIr={setPagina}
          />
          <button
            type="button"
            onClick={procesar}
            disabled={procesando || !tope}
            className="bg-centro font-titulo flex min-h-24 items-center justify-center gap-1 rounded-2xl px-6 text-[clamp(2rem,5vw,3.4rem)] font-semibold tracking-wide text-white uppercase transition hover:brightness-95 focus-visible:outline-4 focus-visible:outline-offset-2 focus-visible:outline-tinta disabled:cursor-not-allowed disabled:opacity-50"
          >
            Procesar
            <span className="flex" aria-hidden="true">
              <IconoAvanzar className="size-10 sm:size-14" />
              <IconoAvanzar className="-ml-5 size-10 sm:size-14" />
              <IconoAvanzar className="-ml-5 size-10 sm:size-14" />
            </span>
          </button>
        </div>
        <div className="px-4 sm:px-0">
          <Aviso mensaje={aviso} />
        </div>
      </div>

      <aside className="flex flex-col gap-4 px-4 sm:px-0 lg:pt-36">
        <Panel color="centro" className="p-4">
          <h2 className="etiqueta text-centro text-sm">Próximo a procesar</h2>
          {tope ? (
            <p className="mt-2">
              <span className="font-titulo block text-2xl font-semibold">{tope.nombre}</span>
              <span className="text-sm text-neutral-600">
                #{tope.id} · {NOMBRE_TIPO[tope.tipo]} · {formatearKg(tope.peso)} · {NOMBRE_ZONA[tope.zona]}
              </span>
            </p>
          ) : (
            <p className="mt-2 text-sm text-neutral-500">La pila está vacía.</p>
          )}
          <p className="mt-3 text-xs text-neutral-600">
            El centro funciona como una pila (LIFO): siempre se procesa lo último que llegó.
          </p>
        </Panel>
        <Panel color="centro" className="p-4">
          <h2 className="etiqueta text-centro text-sm">Ocupación</h2>
          <p className="font-titulo mt-1 text-3xl font-semibold">
            {pila.length} <span className="text-lg text-neutral-500">/ {centro.datos?.capacidadMaxima ?? '–'}</span>
          </p>
          <div
            className="mt-2 h-3 overflow-hidden rounded-full bg-neutral-200"
            role="progressbar"
            aria-label="Ocupación del centro"
            aria-valuenow={Math.round(ocupacion * 100)}
            aria-valuemin={0}
            aria-valuemax={100}
          >
            <div className="bg-centro h-full rounded-full" style={{ width: `${Math.min(100, ocupacion * 100)}%` }} />
          </div>
          <p className="mt-3 text-sm">
            Procesados en total: <strong>{centro.datos?.procesados ?? 0}</strong>
          </p>
        </Panel>
      </aside>
    </main>
  )
}

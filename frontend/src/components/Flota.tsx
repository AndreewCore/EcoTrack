import { useState } from 'react'
import { api } from '../api'
import { NOMBRE_ZONA, formatearKg } from '../lib/dominio'
import { mensajeDeError, useDatos, type Mensaje } from '../lib/useDatos'
import { ZONAS, type ResultadoDespacho, type ZonaGuayaquil } from '../types'
import { IconoCamion, IconoEliminar } from './Iconos'
import { Aviso, Panel } from './Seccion'

/**
 * Vehículos en orden de despacho (cola de prioridad), alta y baja de vehículos y
 * el botón DESPACHAR. Avisa a la pantalla tras cada cambio para refrescar el mapa.
 */
export function Flota({ onCambio }: { onCambio: () => void }) {
  const vehiculos = useDatos(() => api.listarVehiculos(), 'vehiculos')
  const [aviso, setAviso] = useState<Mensaje>(null)
  const [ultimo, setUltimo] = useState<ResultadoDespacho | null>(null)
  const [ocupado, setOcupado] = useState(false)
  const [nuevo, setNuevo] = useState<{ id: string; zona: ZonaGuayaquil | ''; capacidad: string }>({
    id: '',
    zona: '',
    capacidad: '',
  })

  /** Ejecuta una acción, muestra su error si falla y refresca flota y mapa. */
  async function ejecutar(accion: () => Promise<void>) {
    setOcupado(true)
    try {
      await accion()
      vehiculos.recargar()
      onCambio()
    } catch (e) {
      setAviso(mensajeDeError(e))
    } finally {
      setOcupado(false)
    }
  }

  const despachar = () =>
    ejecutar(async () => {
      const r = await api.despachar()
      setUltimo(r)
      setAviso({
        texto: `${r.vehiculo.id} recogió ${r.residuos.length} residuo(s) en ${NOMBRE_ZONA[r.zona]} (${formatearKg(r.pesoTotal)}).${r.aviso ? ' ' + r.aviso : ''}`,
        tipo: r.aviso ? 'info' : 'ok',
      })
    })

  /** Valida lo mínimo en el cliente y registra el vehículo. */
  function agregar(e: React.FormEvent) {
    e.preventDefault()
    const capacidad = Number(nuevo.capacidad)
    if (!nuevo.id.trim() || !nuevo.zona || !(capacidad > 0)) {
      setAviso({ texto: 'Completa id, zona y una capacidad mayor a 0.', tipo: 'error' })
      return
    }
    ejecutar(async () => {
      const v = await api.crearVehiculo({ id: nuevo.id, zonaAsignada: nuevo.zona as ZonaGuayaquil, capacidad })
      setNuevo({ id: '', zona: '', capacidad: '' })
      setAviso({ texto: `Vehículo ${v.id} agregado (turno ${v.turno}).`, tipo: 'ok' })
    })
  }

  const control = 'w-full rounded-lg border-2 border-neutral-200 bg-white px-2 py-1.5 text-sm'

  return (
    <Panel color="rutas" className="p-4">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <h2 className="etiqueta text-rutas flex items-center gap-2 text-sm">
          <IconoCamion className="size-5" /> Vehículos (cola de despacho)
        </h2>
        <button
          type="button"
          onClick={despachar}
          disabled={ocupado}
          className="bg-rutas etiqueta rounded-xl px-5 py-2.5 text-sm text-white transition hover:brightness-95 focus-visible:outline-4 focus-visible:outline-offset-2 focus-visible:outline-tinta disabled:opacity-50"
        >
          Despachar
        </button>
      </div>
      <p className="mt-1 text-xs text-neutral-600">
        Sale primero el vehículo de la zona con peor utilidad; recoge sus residuos más prioritarios y los lleva al centro.
      </p>

      {vehiculos.error ? (
        <p role="alert" className="mt-3 text-sm text-red-600">
          {vehiculos.error}
        </p>
      ) : (
        <ol className="mt-3 grid grid-cols-[minmax(0,1fr)] gap-1.5">
          {(vehiculos.datos ?? []).map((v) => (
            <li
              key={v.id}
              className={`flex items-center gap-2 rounded-xl px-2 py-1.5 text-sm sm:gap-3 sm:px-3 ${v.turno === 1 ? 'bg-rutas/20 font-semibold' : 'bg-neutral-50'}`}
            >
              <span className="bg-rutas grid size-6 shrink-0 place-items-center rounded-full text-xs font-bold text-white">{v.turno}</span>
              <span className="w-16 shrink-0 font-mono text-xs sm:w-20 sm:text-sm">{v.id}</span>
              <span className="min-w-0 flex-1 truncate">{NOMBRE_ZONA[v.zonaAsignada]}</span>
              <span className="tabular-nums">{formatearKg(v.capacidad)}</span>
              <button
                type="button"
                aria-label={`Retirar vehículo ${v.id}`}
                disabled={ocupado}
                onClick={() => {
                  if (window.confirm(`¿Retirar el vehículo ${v.id}?`)) ejecutar(() => api.eliminarVehiculo(v.id))
                }}
                className="rounded-lg p-1 text-red-600 hover:bg-red-50"
              >
                <IconoEliminar className="size-4" />
              </button>
            </li>
          ))}
          {vehiculos.datos?.length === 0 && <li className="text-sm text-neutral-500">No hay vehículos. Agrega uno abajo.</li>}
        </ol>
      )}

      <form onSubmit={agregar} className="mt-3 grid grid-cols-[1fr_1.4fr_1fr_auto] gap-2 max-sm:grid-cols-2" noValidate>
        <input className={control} placeholder="ID (GYE-006)" value={nuevo.id} maxLength={20} onChange={(e) => setNuevo({ ...nuevo, id: e.target.value })} aria-label="ID del vehículo" />
        <select className={control} value={nuevo.zona} onChange={(e) => setNuevo({ ...nuevo, zona: e.target.value as ZonaGuayaquil })} aria-label="Zona asignada">
          <option value="">Zona…</option>
          {ZONAS.map((z) => (
            <option key={z} value={z}>
              {NOMBRE_ZONA[z]}
            </option>
          ))}
        </select>
        <input className={control} placeholder="Capacidad kg" inputMode="decimal" value={nuevo.capacidad} onChange={(e) => setNuevo({ ...nuevo, capacidad: e.target.value })} aria-label="Capacidad en kg" />
        <button type="submit" disabled={ocupado} className="bg-gris-boton etiqueta rounded-lg px-3 py-1.5 text-xs text-white hover:brightness-110 disabled:opacity-50">
          Agregar
        </button>
      </form>

      <div className="mt-3 grid gap-2">
        <Aviso mensaje={aviso} />
        {ultimo && ultimo.residuos.length > 0 && (
          <details className="text-xs">
            <summary className="cursor-pointer font-semibold">Residuos del último despacho</summary>
            <ul className="mt-1 list-inside list-disc">
              {ultimo.residuos.map((r) => (
                <li key={r.id}>
                  {r.nombre} — {formatearKg(r.peso)} (prioridad {r.prioridad})
                </li>
              ))}
            </ul>
          </details>
        )}
      </div>
    </Panel>
  )
}

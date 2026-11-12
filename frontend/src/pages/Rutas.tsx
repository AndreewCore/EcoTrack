import { useState } from 'react'
import { api } from '../api'
import { LeyendaMapa, MapaGuayaquil } from '../components/MapaGuayaquil'
import { CabeceraSeccion, Panel, Pildora } from '../components/Seccion'
import { NOMBRE_ZONA, formatearKg } from '../lib/dominio'
import { useDatos } from '../lib/useDatos'
import { ZONAS, type ZonaGuayaquil } from '../types'

/** Pantalla Rutas: mapa de zonas con su utilidad ambiental y el detalle de la zona elegida. */
export default function Rutas() {
  const [seleccionada, setSeleccionada] = useState<ZonaGuayaquil | null>(null)
  const zonas = useDatos(() => api.estadoZonas(), 'zonas')
  const estado = zonas.datos?.find((z) => z.zona === seleccionada)

  return (
    <main className="mx-auto grid min-h-dvh max-w-[1280px] gap-4 pb-4 sm:px-4 lg:grid-cols-[minmax(20rem,32rem)_1fr]">
      <div className="flex flex-col gap-5">
        <CabeceraSeccion titulo="Rutas" color="rutas" className="w-full uppercase sm:max-w-sm lg:-ml-4" />

        <div className="grid gap-5 px-4 sm:px-0">
          <label className="grid gap-2">
            <span className="etiqueta text-rutas text-xl sm:text-2xl">Zona seleccionada:</span>
            <select
              className="rounded-xl bg-white px-4 py-3 text-lg font-semibold outline-none focus-visible:ring-4 focus-visible:ring-rutas"
              value={seleccionada ?? ''}
              onChange={(e) => setSeleccionada((e.target.value || null) as ZonaGuayaquil | null)}
            >
              <option value="">Toca una zona en el mapa…</option>
              {ZONAS.map((z) => (
                <option key={z} value={z}>
                  {NOMBRE_ZONA[z]}
                </option>
              ))}
            </select>
          </label>

          <Panel color="rutas" className="p-4">
            <div className="grid grid-cols-3 gap-2 sm:gap-4">
              <Pildora color="rutas" className="text-[0.65rem]! sm:text-xs!">
                Peso recolectado
              </Pildora>
              <Pildora color="rutas" className="text-[0.65rem]! sm:text-xs!">
                Peso pendiente
              </Pildora>
              <Pildora color="rutas">Utilidad</Pildora>
              {estado ? (
                <>
                  <Valor>{formatearKg(estado.pesoRecolectado)}</Valor>
                  <Valor>{formatearKg(estado.pesoPendiente)}</Valor>
                  <Valor destacado={estado.utilidad < 0}>{formatearKg(estado.utilidad)}</Valor>
                </>
              ) : (
                <p className="col-span-3 py-4 text-center text-sm text-neutral-500">
                  {zonas.error ? `No se pudieron cargar las zonas: ${zonas.error}` : 'Elige una zona para ver sus pesos.'}
                </p>
              )}
            </div>
            <div className="mt-4 border-t border-neutral-200 pt-3">
              <p className="mb-2 text-xs text-neutral-600">
                Utilidad = peso recolectado − peso pendiente. Las zonas con utilidad más baja se atienden primero.
              </p>
              <LeyendaMapa />
            </div>
          </Panel>
        </div>
      </div>

      <div className="border-rutas mx-4 min-h-[60vh] border-[6px] bg-white sm:mx-0 lg:mt-6">
        <MapaGuayaquil zonas={zonas.datos ?? []} seleccionada={seleccionada} onSeleccionar={setSeleccionada} />
      </div>
    </main>
  )
}

/** Valor numérico bajo cada píldora del panel de zona. */
function Valor({ children, destacado = false }: { children: React.ReactNode; destacado?: boolean }) {
  return <span className={`text-center text-lg font-bold ${destacado ? 'text-red-600' : 'text-tinta'}`}>{children}</span>
}

import { Bar, BarChart, CartesianGrid, Cell, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { api } from '../api'
import { LeyendaMapa } from '../components/MapaGuayaquil'
import { CabeceraSeccion, Panel } from '../components/Seccion'
import { NOMBRE_TIPO, NOMBRE_ZONA, formatearKg, nivelPorUtilidad } from '../lib/dominio'
import { useDatos } from '../lib/useDatos'
import { TIPOS_RESIDUO, type Estadisticas as Datos } from '../types'

// Paleta validada con el script de dataviz (CVD ΔE 28.6). El naranja queda bajo 3:1
// de contraste, por eso cada gráfico va acompañado de su tabla.
const COLOR_PENDIENTE = '#ff751f'
const COLOR_RECOLECTADO = '#1f6fd1'
const COLOR_TIPO = '#ff751f'
const COLOR_NIVEL = { verde: '#22b14c', amarillo: '#f5c518', rojo: '#e53935' } as const
const TINTA = '#3a3a3a'
const TINTA_SUAVE = '#6b6b6b'

const ejeTexto = { fill: TINTA_SUAVE, fontSize: 12 }

/** Pantalla de estadísticas globales (por tipo y por zona). */
export default function Estadisticas() {
  const { datos, error, cargando } = useDatos(() => api.estadisticas(), 'estadisticas')

  return (
    <main className="mx-auto flex min-h-dvh max-w-[1280px] flex-col gap-5 pb-6 sm:px-4">
      <CabeceraSeccion titulo="Estadísticas" color="estadisticas" className="w-full sm:w-fit sm:pr-12 lg:-ml-4" />
      {error && (
        <p role="alert" className="px-4 text-red-600">
          No se pudieron cargar las estadísticas: {error}
        </p>
      )}
      {!datos && cargando && <p className="px-4">Cargando…</p>}
      {datos && <Contenido datos={datos} />}
    </main>
  )
}

/** Tarjetas, gráficos y tablas una vez cargados los datos. */
function Contenido({ datos }: { datos: Datos }) {
  const porTipo = TIPOS_RESIDUO.map((t) => ({
    tipo: NOMBRE_TIPO[t],
    peso: datos.pesoPorTipo[t] ?? 0,
    cantidad: datos.cantidadPorTipo[t] ?? 0,
  }))
  const porZona = [...datos.zonas]
    .sort((a, b) => a.utilidad - b.utilidad)
    .map((z) => ({
      zona: NOMBRE_ZONA[z.zona],
      pendiente: z.pesoPendiente,
      recolectado: z.pesoRecolectado,
      utilidad: z.utilidad,
    }))

  return (
    <div className="grid grid-cols-[minmax(0,1fr)] gap-5 px-4 sm:px-0">
      <section aria-label="Resumen" className="grid grid-cols-2 gap-3 md:grid-cols-3 xl:grid-cols-6">
        <Tarjeta titulo="Residuos" valor={String(datos.totalResiduos)} />
        <Tarjeta titulo="Peso total" valor={formatearKg(datos.pesoTotal)} />
        <Tarjeta titulo="Peso promedio" valor={formatearKg(datos.pesoPromedio)} />
        <Tarjeta titulo="Tipo más común" valor={datos.tipoMasComun ? NOMBRE_TIPO[datos.tipoMasComun] : '—'} />
        <Tarjeta titulo="Más pendiente" valor={datos.zonaMasPendiente ? NOMBRE_ZONA[datos.zonaMasPendiente] : '—'} />
        <Tarjeta
          titulo="Mejor utilidad"
          valor={datos.zonaMejorUtilidad ? NOMBRE_ZONA[datos.zonaMejorUtilidad] : '—'}
        />
      </section>

      <div className="grid grid-cols-[minmax(0,1fr)] gap-5 xl:grid-cols-2">
        <Panel color="estadisticas" className="p-4">
          <h2 className="etiqueta text-estadisticas text-sm">Peso por tipo de residuo</h2>
          <div className="mt-3 h-72">
            <ResponsiveContainer>
              <BarChart data={porTipo} layout="vertical" margin={{ left: 8, right: 16 }} barCategoryGap={6}>
                <CartesianGrid horizontal={false} stroke="#e5e5e5" />
                <XAxis type="number" tick={ejeTexto} unit=" kg" axisLine={false} tickLine={false} />
                <YAxis type="category" dataKey="tipo" tick={ejeTexto} width={86} axisLine={false} tickLine={false} />
                <Tooltip
                  cursor={{ fill: 'rgba(0,0,0,0.04)' }}
                  formatter={(v) => [formatearKg(Number(v)), 'Peso']}
                  labelStyle={{ color: TINTA, fontWeight: 600 }}
                />
                <Bar dataKey="peso" fill={COLOR_TIPO} radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
          <TablaDatos
            titulo="Ver como tabla"
            columnas={['Tipo', 'Cantidad', 'Peso']}
            filas={porTipo.map((f) => [f.tipo, String(f.cantidad), formatearKg(f.peso)])}
          />
        </Panel>

        <Panel color="estadisticas" className="p-4">
          <h2 className="etiqueta text-estadisticas text-sm">Utilidad ambiental por zona</h2>
          <p className="mt-1 text-xs text-neutral-600">De la zona más crítica a la más limpia (recolectado − pendiente).</p>
          <div className="mt-3 h-[26rem]">
            <ResponsiveContainer>
              <BarChart data={porZona} layout="vertical" margin={{ left: 8, right: 16 }} barCategoryGap={3}>
                <CartesianGrid horizontal={false} stroke="#e5e5e5" />
                <XAxis type="number" tick={ejeTexto} unit=" kg" axisLine={false} tickLine={false} />
                <YAxis type="category" dataKey="zona" tick={{ ...ejeTexto, fontSize: 11 }} width={140} axisLine={false} tickLine={false} interval={0} />
                <Tooltip
                  cursor={{ fill: 'rgba(0,0,0,0.04)' }}
                  formatter={(v) => [formatearKg(Number(v)), 'Utilidad']}
                  labelStyle={{ color: TINTA, fontWeight: 600 }}
                />
                <Bar dataKey="utilidad" radius={4}>
                  {porZona.map((z) => (
                    <Cell key={z.zona} fill={COLOR_NIVEL[nivelPorUtilidad(z.utilidad)]} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
          <LeyendaMapa />
        </Panel>
      </div>

      <Panel color="estadisticas" className="p-4">
        <h2 className="etiqueta text-estadisticas text-sm">Peso pendiente y recolectado por zona</h2>
        <div className="mt-3 h-80">
          <ResponsiveContainer>
            <BarChart data={porZona} margin={{ top: 8, right: 8, bottom: 64 }} barGap={2}>
              <CartesianGrid vertical={false} stroke="#e5e5e5" />
              <XAxis dataKey="zona" tick={{ ...ejeTexto, fontSize: 11 }} angle={-40} textAnchor="end" interval={0} tickLine={false} />
              <YAxis tick={ejeTexto} unit=" kg" axisLine={false} tickLine={false} width={64} />
              <Tooltip cursor={{ fill: 'rgba(0,0,0,0.04)' }} formatter={(v, n) => [formatearKg(Number(v)), n]} labelStyle={{ color: TINTA, fontWeight: 600 }} />
              <Legend verticalAlign="top" align="right" wrapperStyle={{ fontSize: 12, color: TINTA }} />
              <Bar dataKey="pendiente" name="Pendiente" fill={COLOR_PENDIENTE} radius={[4, 4, 0, 0]} />
              <Bar dataKey="recolectado" name="Recolectado" fill={COLOR_RECOLECTADO} radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <TablaDatos
          titulo="Ver como tabla"
          columnas={['Zona', 'Pendiente', 'Recolectado', 'Utilidad']}
          filas={porZona.map((z) => [z.zona, formatearKg(z.pendiente), formatearKg(z.recolectado), formatearKg(z.utilidad)])}
        />
      </Panel>
    </div>
  )
}

/** Indicador resumido (número o nombre grande con su etiqueta). */
function Tarjeta({ titulo, valor }: { titulo: string; valor: string }) {
  return (
    <div className="rounded-2xl bg-white p-4 shadow-sm">
      <p className="etiqueta text-estadisticas text-[0.65rem]">{titulo}</p>
      <p className="font-titulo mt-1 text-xl leading-tight font-semibold sm:text-2xl">{valor}</p>
    </div>
  )
}

/** Tabla plegable con los mismos datos del gráfico (accesible y legible sin color). */
function TablaDatos({ titulo, columnas, filas }: { titulo: string; columnas: string[]; filas: string[][] }) {
  return (
    <details className="mt-3">
      <summary className="cursor-pointer text-sm font-semibold text-neutral-700">{titulo}</summary>
      <div className="mt-2 overflow-x-auto">
        <table className="w-full text-left text-sm">
          <thead>
            <tr>
              {columnas.map((c) => (
                <th key={c} scope="col" className="border-b-2 border-neutral-200 px-2 py-1 font-semibold">
                  {c}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filas.map((f) => (
              <tr key={f[0]} className="odd:bg-neutral-50">
                {f.map((celda, i) => (
                  <td key={i} className={`px-2 py-1 ${i > 0 ? 'tabular-nums' : ''}`}>
                    {celda}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </details>
  )
}

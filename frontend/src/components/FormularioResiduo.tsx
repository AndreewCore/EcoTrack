import { useState } from 'react'
import { NOMBRE_TIPO, NOMBRE_ZONA, PRIORIDAD_POR_TIPO } from '../lib/dominio'
import { TIPOS_RESIDUO, ZONAS, type Residuo, type ResiduoEntrada, type TipoResiduo, type ZonaGuayaquil } from '../types'
import { IconoMas, IconoUbicacion } from './Iconos'

interface FormularioResiduoProps {
  /** Si viene, el formulario edita ese residuo en lugar de crear uno nuevo. */
  editando?: Residuo | null
  onGuardar: (entrada: ResiduoEntrada) => Promise<boolean> | boolean
  onCancelarEdicion?: () => void
  /** Informa un error de validación al aviso de la pantalla. */
  onError: (mensaje: string) => void
}

interface Campos {
  nombre: string
  tipo: TipoResiduo | ''
  peso: string
  zona: ZonaGuayaquil | ''
  prioridad: string
}

const VACIO: Campos = { nombre: '', tipo: '', peso: '', zona: '', prioridad: '' }

/** Convierte un residuo existente a los valores de los campos del formulario. */
function camposDe(r: Residuo | null | undefined): Campos {
  if (!r) return VACIO
  return { nombre: r.nombre, tipo: r.tipo, peso: String(r.peso), zona: r.zona, prioridad: String(r.prioridad) }
}

/**
 * Panel verde "NUEVO RESIDUO" con el botón gris GUARDAR, como en ResiduoScene.
 * Valida en el cliente lo mismo que validaba el controlador JavaFX.
 */
export function FormularioResiduo({ editando, onGuardar, onCancelarEdicion, onError }: FormularioResiduoProps) {
  const [campos, setCampos] = useState<Campos>(() => camposDe(editando))
  const [idCargado, setIdCargado] = useState<number | null>(editando?.id ?? null)
  const [enviando, setEnviando] = useState(false)

  // Al elegir otra fila para editar se reinician los campos (patrón de estado derivado de React).
  if ((editando?.id ?? null) !== idCargado) {
    setIdCargado(editando?.id ?? null)
    setCampos(camposDe(editando))
  }

  /** Actualiza un campo; al cambiar el tipo propone su prioridad por defecto. */
  function cambiar<K extends keyof Campos>(campo: K, valor: Campos[K]) {
    setCampos((prev) => {
      const siguiente = { ...prev, [campo]: valor }
      if (campo === 'tipo' && valor) siguiente.prioridad = String(PRIORIDAD_POR_TIPO[valor as TipoResiduo])
      return siguiente
    })
  }

  /** Valida y envía; limpia el formulario solo si el guardado tuvo éxito. */
  async function enviar(e: React.FormEvent) {
    e.preventDefault()
    const nombre = campos.nombre.trim()
    if (!nombre) return onError('Por favor, escribe un nombre para el residuo.')
    if (!campos.tipo) return onError('Por favor, elige un tipo de residuo.')
    const peso = Number(campos.peso.replace(',', '.'))
    if (!campos.peso || Number.isNaN(peso)) return onError('Por favor, ingresa solo números en el peso.')
    if (peso <= 0) return onError('El peso debe ser mayor a 0.')
    if (!campos.zona) return onError('Por favor, elige una zona.')
    const prioridad = Number(campos.prioridad)
    if (!Number.isInteger(prioridad) || prioridad < 1 || prioridad > 5) return onError('La prioridad debe estar entre 1 y 5.')

    setEnviando(true)
    try {
      const ok = await onGuardar({ nombre, tipo: campos.tipo, peso, zona: campos.zona, prioridad })
      if (ok && !editando) setCampos(VACIO)
    } finally {
      setEnviando(false)
    }
  }

  const etiqueta = 'etiqueta text-residuos pointer-events-none absolute top-1.5 left-2 z-20 text-[0.7rem]'
  const control =
    'w-full rounded-lg bg-white px-2 pt-6 pb-2 text-sm text-tinta outline-none focus-visible:ring-4 focus-visible:ring-banner-1'

  return (
    <form onSubmit={enviar} className="bg-residuos flex flex-col overflow-hidden rounded-2xl sm:flex-row" noValidate>
      <div className="flex-1 p-4 sm:p-6">
        <h2 className="etiqueta mb-3 text-xl text-white sm:text-2xl">{editando ? `Editar residuo #${editando.id}` : 'Nuevo residuo'}</h2>
        <div className="grid gap-3 md:grid-cols-[1fr_minmax(12rem,0.4fr)]">
          <div className="grid gap-3">
            <label className="relative block">
              <span className={etiqueta}>Nombre</span>
              <input
                className={control}
                value={campos.nombre}
                maxLength={80}
                onChange={(e) => cambiar('nombre', e.target.value)}
                autoComplete="off"
              />
            </label>
            <div className="grid grid-cols-[1fr_7rem] gap-3">
              <label className="relative block">
                <span className={etiqueta}>Tipo</span>
                <select className={control} value={campos.tipo} onChange={(e) => cambiar('tipo', e.target.value as TipoResiduo)}>
                  <option value="">Elegir…</option>
                  {TIPOS_RESIDUO.map((t) => (
                    <option key={t} value={t}>
                      {NOMBRE_TIPO[t]}
                    </option>
                  ))}
                </select>
              </label>
              <label className="relative block">
                <span className={etiqueta}>Prioridad</span>
                <select className={control} value={campos.prioridad} onChange={(e) => cambiar('prioridad', e.target.value)}>
                  <option value="">–</option>
                  {[1, 2, 3, 4, 5].map((n) => (
                    <option key={n} value={n}>
                      {n}
                    </option>
                  ))}
                </select>
              </label>
            </div>
            <label className="relative block">
              <span className={etiqueta}>Peso (kg)</span>
              <input
                className={control}
                inputMode="decimal"
                value={campos.peso}
                onChange={(e) => cambiar('peso', e.target.value)}
              />
            </label>
          </div>
          <label className="relative flex flex-col rounded-lg bg-white">
            <span className={etiqueta}>Zona</span>
            <select
              className={`${control} relative z-10 bg-transparent`}
              value={campos.zona}
              onChange={(e) => cambiar('zona', e.target.value as ZonaGuayaquil)}
            >
              <option value="">Elegir…</option>
              {ZONAS.map((z) => (
                <option key={z} value={z}>
                  {NOMBRE_ZONA[z]}
                </option>
              ))}
            </select>
            <span className="text-residuos grid flex-1 place-items-center pb-2" aria-hidden="true">
              <IconoUbicacion className="size-16 max-md:hidden" />
            </span>
          </label>
        </div>
        {editando && (
          <button type="button" onClick={onCancelarEdicion} className="etiqueta mt-3 text-sm text-white underline underline-offset-4">
            Cancelar edición
          </button>
        )}
      </div>
      <button
        type="submit"
        disabled={enviando}
        className="bg-gris-boton flex items-center justify-center gap-2 px-6 py-4 text-white transition hover:brightness-110 focus-visible:outline-4 focus-visible:-outline-offset-4 focus-visible:outline-white disabled:opacity-60 sm:w-40 sm:flex-col sm:rounded-l-2xl"
      >
        <IconoMas className="size-10 sm:size-20" />
        <span className="font-titulo text-xl font-semibold uppercase">{editando ? 'Actualizar' : 'Guardar'}</span>
      </button>
    </form>
  )
}

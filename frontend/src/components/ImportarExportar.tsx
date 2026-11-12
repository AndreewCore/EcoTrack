import { useRef, useState } from 'react'
import { api } from '../api'
import { mensajeDeError, type Mensaje } from '../lib/useDatos'
import type { ModoImportacion } from '../types'
import { IconoDescargar, IconoSubir } from './Iconos'
import { Aviso } from './Seccion'

const MAX_BYTES = 2 * 1024 * 1024

/**
 * Botones DESCARGAR DATOS y SUBIR DATOS del menú principal. Al subir un CSV se
 * pregunta cada vez si se añade a lo existente o lo reemplaza.
 */
export function ImportarExportar() {
  const entrada = useRef<HTMLInputElement>(null)
  const dialogo = useRef<HTMLDialogElement>(null)
  const [archivo, setArchivo] = useState<File | null>(null)
  const [aviso, setAviso] = useState<Mensaje>(null)
  const [ocupado, setOcupado] = useState(false)

  /** Descarga todos los residuos como CSV. */
  async function descargar() {
    try {
      const csv = await api.exportarCsv()
      const url = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8' }))
      const a = document.createElement('a')
      a.href = url
      a.download = 'residuos_ecotrack.csv'
      a.click()
      URL.revokeObjectURL(url)
      setAviso({ texto: 'Datos descargados en residuos_ecotrack.csv.', tipo: 'ok' })
    } catch (e) {
      setAviso(mensajeDeError(e))
    }
  }

  /** Guarda el archivo elegido y abre la pregunta de modo. */
  function elegido(e: React.ChangeEvent<HTMLInputElement>) {
    const f = e.target.files?.[0]
    e.target.value = ''
    if (!f) return
    if (f.size > MAX_BYTES) {
      setAviso({ texto: 'El archivo supera 2 MB.', tipo: 'error' })
      return
    }
    setArchivo(f)
    dialogo.current?.showModal()
  }

  /** Importa con el modo elegido y resume el resultado. */
  async function importar(modo: ModoImportacion) {
    dialogo.current?.close()
    if (!archivo) return
    setOcupado(true)
    try {
      const r = await api.importarCsv(await archivo.text(), modo)
      const descartadas = r.errores.length ? ` Se descartaron ${r.errores.length} línea(s): ${r.errores.slice(0, 3).join(' ')}` : ''
      setAviso({
        texto: `${r.importados} residuo(s) ${modo === 'agregar' ? 'añadidos' : 'cargados (datos reemplazados)'}.${descartadas}`,
        tipo: r.errores.length ? 'info' : 'ok',
      })
    } catch (e) {
      setAviso(mensajeDeError(e))
    } finally {
      setArchivo(null)
      setOcupado(false)
    }
  }

  const boton =
    'bg-residuos-tarjeta etiqueta flex items-center justify-center gap-3 rounded-[2rem] px-8 py-4 text-lg text-white transition hover:brightness-95 focus-visible:outline-4 focus-visible:outline-offset-2 focus-visible:outline-tinta disabled:opacity-60 sm:min-w-96 sm:text-xl'

  return (
    <div className="mt-8 grid justify-items-center gap-3">
      <div className="flex w-full flex-col justify-center gap-4 sm:flex-row sm:gap-8">
        <button type="button" onClick={descargar} className={boton}>
          <IconoDescargar className="size-9" />
          Descargar datos
        </button>
        <button type="button" onClick={() => entrada.current?.click()} disabled={ocupado} className={boton}>
          <IconoSubir className="size-9" />
          {ocupado ? 'Subiendo…' : 'Subir datos'}
        </button>
        <input ref={entrada} type="file" accept=".csv,.txt,text/csv" className="hidden" onChange={elegido} aria-label="Archivo CSV a subir" />
      </div>
      <div className="w-full max-w-3xl">
        <Aviso mensaje={aviso} />
      </div>

      <dialog
        ref={dialogo}
        onClose={() => setArchivo(null)}
        className="m-auto max-w-md rounded-2xl p-6 backdrop:bg-black/40"
        aria-labelledby="titulo-importar"
      >
        <h2 id="titulo-importar" className="font-titulo text-residuos text-2xl font-semibold">
          Subir {archivo?.name}
        </h2>
        <p className="mt-2 text-sm">¿Qué hago con los residuos que ya existen?</p>
        <div className="mt-5 grid gap-2">
          <button type="button" onClick={() => importar('agregar')} className="bg-residuos etiqueta rounded-xl px-4 py-3 text-sm text-white">
            Añadir a los existentes
          </button>
          <button type="button" onClick={() => importar('reemplazar')} className="etiqueta rounded-xl bg-red-600 px-4 py-3 text-sm text-white">
            Reemplazar todo
          </button>
          <button type="button" onClick={() => dialogo.current?.close()} className="etiqueta rounded-xl px-4 py-2 text-sm text-neutral-600">
            Cancelar
          </button>
        </div>
        <p className="mt-3 text-xs text-neutral-500">
          Formato: ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado (el que genera "Descargar datos").
        </p>
      </dialog>
    </div>
  )
}

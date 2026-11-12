import { useState } from 'react'
import { api } from '../api'
import { Filtros, Paginador, SelectorOrden } from '../components/Controles'
import { FormularioResiduo } from '../components/FormularioResiduo'
import { Aviso, CabeceraSeccion, CajaEtiquetada, Panel } from '../components/Seccion'
import { TablaResiduos } from '../components/TablaResiduos'
import { mensajeDeError, useDatos, type Mensaje } from '../lib/useDatos'
import type { ConsultaResiduos, Residuo, ResiduoEntrada } from '../types'

const TAMANO_PAGINA = 10

/** Pantalla de gestión de residuos: listar, ordenar, filtrar, registrar, editar y eliminar. */
export default function Residuos() {
  const [consulta, setConsulta] = useState<ConsultaResiduos>({ orden: 'id', dir: 'asc', pagina: 1, tamano: TAMANO_PAGINA })
  const [editando, setEditando] = useState<Residuo | null>(null)
  const [aviso, setAviso] = useState<Mensaje>(null)
  const { datos, cargando, error, recargar } = useDatos(() => api.listarResiduos(consulta), JSON.stringify(consulta))

  /** Aplica cambios a la consulta; cualquier filtro u orden nuevo vuelve a la página 1. */
  function cambiarConsulta(cambios: Partial<ConsultaResiduos>) {
    setConsulta((c) => ({ ...c, pagina: 1, ...cambios }))
  }

  /** Crea o actualiza según haya una fila en edición; devuelve si tuvo éxito. */
  async function guardar(entrada: ResiduoEntrada): Promise<boolean> {
    try {
      if (editando) {
        const r = await api.actualizarResiduo(editando.id, entrada)
        setEditando(null)
        setAviso({ texto: `Residuo "${r.nombre}" actualizado.`, tipo: 'ok' })
      } else {
        const r = await api.crearResiduo(entrada)
        setAviso({ texto: `Residuo "${r.nombre}" agregado con ID ${r.id}.`, tipo: 'ok' })
      }
      recargar()
      return true
    } catch (e) {
      setAviso(mensajeDeError(e))
      return false
    }
  }

  /** Elimina tras confirmar; los residuos ya recogidos no se pueden borrar en el backend. */
  async function eliminar(r: Residuo) {
    if (!window.confirm(`¿Eliminar "${r.nombre}" (ID ${r.id})?`)) return
    try {
      await api.eliminarResiduo(r.id)
      if (editando?.id === r.id) setEditando(null)
      setAviso({ texto: `Residuo "${r.nombre}" eliminado.`, tipo: 'ok' })
      recargar()
    } catch (e) {
      setAviso(mensajeDeError(e))
    }
  }

  return (
    <main className="mx-auto flex min-h-dvh max-w-[1280px] flex-col gap-4 pb-4 sm:px-4">
      <div className="grid grid-cols-[minmax(0,1fr)] gap-4 lg:grid-cols-[minmax(18rem,24rem)_minmax(0,1fr)]">
        <div className="flex flex-col gap-4">
          <CabeceraSeccion titulo="Residuos" color="residuos" className="lg:-ml-4" />
          <div className="grid gap-4 px-4 sm:grid-cols-2 sm:px-0 lg:grid-cols-1">
            <CajaEtiquetada titulo="Ordenar por" color="residuos">
              <span id="etq-orden" className="sr-only">
                Ordenar por
              </span>
              <SelectorOrden idEtiqueta="etq-orden" consulta={consulta} onCambiar={(orden, dir) => cambiarConsulta({ orden, dir })} />
            </CajaEtiquetada>
            <CajaEtiquetada titulo="Filtrar por" color="residuos">
              <Filtros consulta={consulta} onCambiar={cambiarConsulta} />
            </CajaEtiquetada>
          </div>
        </div>

        <Panel color="residuos" className="mx-4 flex min-h-80 flex-col p-3 sm:mx-0 lg:mt-1">
          <h2 className="sr-only">Listado de residuos</h2>
          {error ? (
            <p role="alert" className="m-auto max-w-md p-6 text-center text-sm text-red-600">
              No se pudieron cargar los residuos: {error}
            </p>
          ) : (
            <div className={`flex-1 transition-opacity ${cargando ? 'opacity-50' : ''}`} aria-busy={cargando}>
              <TablaResiduos
                residuos={datos?.elementos ?? []}
                columnas={['id', 'nombre', 'tipo', 'peso', 'zona', 'prioridad', 'estado']}
                color="residuos"
                vacio={cargando ? 'Cargando…' : 'No hay residuos con esos filtros.'}
                onEditar={(r) => {
                  setEditando(r)
                  setAviso(null)
                }}
                onEliminar={eliminar}
              />
            </div>
          )}
          <div className="mt-3 flex justify-end">
            <Paginador pagina={datos} color="residuos" onIr={(pagina) => setConsulta((c) => ({ ...c, pagina }))} />
          </div>
        </Panel>
      </div>

      <div className="grid min-w-0 gap-3 px-4 sm:px-0">
        <Aviso mensaje={aviso} />
        <FormularioResiduo
          editando={editando}
          onGuardar={guardar}
          onCancelarEdicion={() => setEditando(null)}
          onError={(texto) => setAviso({ texto, tipo: 'error' })}
        />
      </div>
    </main>
  )
}

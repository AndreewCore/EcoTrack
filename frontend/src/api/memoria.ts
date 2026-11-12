import { residuosSemilla } from '../data/semilla'
import { PRIORIDAD_POR_TIPO } from '../lib/dominio'
import { TIPOS_RESIDUO, type ConsultaResiduos, type Pagina, type Residuo } from '../types'
import { ErrorApi, type EcoTrackApi } from './contrato'

/** Ordena una copia de los residuos según el criterio de la consulta. */
function ordenar(residuos: Residuo[], consulta: ConsultaResiduos): Residuo[] {
  const { orden = 'id', dir = 'asc' } = consulta
  const signo = dir === 'desc' ? -1 : 1
  const comparar: Record<string, (a: Residuo, b: Residuo) => number> = {
    id: (a, b) => a.id - b.id,
    peso: (a, b) => a.peso - b.peso,
    prioridad: (a, b) => a.prioridad - b.prioridad,
    // Igual que el backend: el orden del enum, no el alfabético.
    tipo: (a, b) => TIPOS_RESIDUO.indexOf(a.tipo) - TIPOS_RESIDUO.indexOf(b.tipo),
  }
  return [...residuos].sort((a, b) => signo * comparar[orden](a, b))
}

/** Recorta una lista a la página pedida (1-based), acotando el número de página. */
export function paginar<T>(lista: T[], pagina = 1, tamano = 10): Pagina<T> {
  const totalPaginas = Math.max(1, Math.ceil(lista.length / tamano))
  const actual = Math.min(Math.max(1, pagina), totalPaginas)
  return {
    elementos: lista.slice((actual - 1) * tamano, actual * tamano),
    pagina: actual,
    totalPaginas,
    totalElementos: lista.length,
  }
}

/** Implementación en memoria con los datos semilla; se pierde al recargar. */
export function crearApiMemoria(): EcoTrackApi {
  let residuos = residuosSemilla()
  let siguienteId = residuos.length + 1

  /** Busca un residuo o lanza un 404 legible. */
  function buscar(id: number): Residuo {
    const r = residuos.find((x) => x.id === id)
    if (!r) throw new ErrorApi(`No existe el residuo ${id}.`, 404)
    return r
  }

  return {
    async listarResiduos(consulta = {}) {
      const filtrados = residuos.filter(
        (r) =>
          (!consulta.tipo || r.tipo === consulta.tipo) &&
          (!consulta.zona || r.zona === consulta.zona) &&
          (!consulta.estado || r.estado === consulta.estado),
      )
      return paginar(ordenar(filtrados, consulta), consulta.pagina, consulta.tamano)
    },
    async crearResiduo(entrada) {
      const nuevo: Residuo = {
        id: siguienteId++,
        ...entrada,
        prioridad: entrada.prioridad ?? PRIORIDAD_POR_TIPO[entrada.tipo],
        estado: 'PENDIENTE',
        fechaRecoleccion: null,
      }
      residuos = [...residuos, nuevo]
      return nuevo
    },
    async actualizarResiduo(id, entrada) {
      const actual = buscar(id)
      const editado = { ...actual, ...entrada, prioridad: entrada.prioridad ?? actual.prioridad }
      residuos = residuos.map((r) => (r.id === id ? editado : r))
      return editado
    },
    async eliminarResiduo(id) {
      buscar(id)
      residuos = residuos.filter((r) => r.id !== id)
    },
  }
}

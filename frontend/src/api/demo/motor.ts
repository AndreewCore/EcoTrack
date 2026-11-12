import { residuosSemilla, vehiculosSemilla } from '../../data/semilla'
import { PRIORIDAD_POR_TIPO } from '../../lib/dominio'
import { leerResiduosCsv, residuosACsv } from '../../lib/csv'
import {
  TIPOS_RESIDUO,
  ZONAS,
  type ConsultaResiduos,
  type EstadoCentro,
  type EstadoZona,
  type Estadisticas,
  type ModoImportacion,
  type Pagina,
  type Residuo,
  type ResiduoEntrada,
  type ResultadoDespacho,
  type TipoResiduo,
  type Vehiculo,
  type VehiculoEntrada,
  type ZonaGuayaquil,
} from '../../types'
import { ErrorApi, type ResultadoImportacion } from '../contrato'

// Réplica en TypeScript de las reglas del backend (EcoTrackData, Despacho,
// CentroReciclaje, EstadisticasMap) para la demo de GitHub Pages, donde no hay
// servidor. Los mensajes de error son los mismos que devuelve la API.

export const CAPACIDAD_CENTRO = 100
const TAMANO_PAGINA_MAXIMO = 100

type VehiculoGuardado = VehiculoEntrada

/** Estado serializable de la demo (lo que se guarda en localStorage). */
export interface EstadoDemo {
  version: 1
  residuos: Residuo[]
  vehiculos: VehiculoGuardado[]
  /** Ids de los residuos en la pila del centro; el índice 0 es el tope. */
  pila: number[]
  procesados: number
  siguienteId: number
}

/** Estado inicial con los mismos datos semilla que el backend. */
export function estadoInicial(): EstadoDemo {
  const residuos = residuosSemilla()
  return {
    version: 1,
    residuos,
    vehiculos: vehiculosSemilla(),
    pila: [],
    procesados: 0,
    siguienteId: residuos.length + 1,
  }
}

/** Pendiente, recolectado y utilidad de cada zona derivados de los residuos. */
export function calcularZonas(residuos: Residuo[]): EstadoZona[] {
  return ZONAS.map((zona) => {
    let pesoPendiente = 0
    let pesoRecolectado = 0
    for (const r of residuos) {
      if (r.zona !== zona) continue
      if (r.estado === 'PENDIENTE') pesoPendiente += r.peso
      else pesoRecolectado += r.peso
    }
    return { zona, pesoPendiente, pesoRecolectado, utilidad: pesoRecolectado - pesoPendiente }
  })
}

/** Recorta una lista a la página pedida (1-based), acotando el número de página. */
export function paginar<T>(lista: T[], pagina = 1, tamano = 10): Pagina<T> {
  const tam = Math.min(Math.max(1, tamano), TAMANO_PAGINA_MAXIMO)
  const totalPaginas = Math.max(1, Math.ceil(lista.length / tam))
  const actual = Math.min(Math.max(1, pagina), totalPaginas)
  return {
    elementos: lista.slice((actual - 1) * tam, actual * tam),
    pagina: actual,
    totalPaginas,
    totalElementos: lista.length,
  }
}

/** Comparador de los criterios de la UI con desempate por id (como el backend). */
function comparador(orden: string, dir: string): (a: Residuo, b: Residuo) => number {
  const base: Record<string, (a: Residuo, b: Residuo) => number> = {
    id: () => 0,
    peso: (a, b) => a.peso - b.peso,
    prioridad: (a, b) => a.prioridad - b.prioridad,
    // Orden del enum, no alfabético.
    tipo: (a, b) => TIPOS_RESIDUO.indexOf(a.tipo) - TIPOS_RESIDUO.indexOf(b.tipo),
  }
  const f = base[orden]
  if (!f) throw new ErrorApi(`Orden no válido: ${orden}. Usa peso, tipo, prioridad o id.`, 400)
  const signo = dir === 'desc' ? -1 : 1
  return (a, b) => signo * (f(a, b) || a.id - b.id)
}

/**
 * Orden de salida de la flota: zona con menor utilidad primero, luego más peso
 * pendiente, más capacidad e id (mismo criterio que ComparadorVehiculo).
 */
function ordenarFlota(estado: EstadoDemo): VehiculoGuardado[] {
  const zonas = new Map(calcularZonas(estado.residuos).map((z) => [z.zona, z]))
  return [...estado.vehiculos].sort((a, b) => {
    const za = zonas.get(a.zonaAsignada)!
    const zb = zonas.get(b.zonaAsignada)!
    return (
      za.utilidad - zb.utilidad ||
      zb.pesoPendiente - za.pesoPendiente ||
      b.capacidad - a.capacidad ||
      a.id.localeCompare(b.id)
    )
  })
}

/** Lanza el error de validación que daría Bean Validation en el backend. */
function validarResiduo(e: ResiduoEntrada) {
  const errores: string[] = []
  if (!e.nombre?.trim()) errores.push('El nombre es obligatorio.')
  else if (e.nombre.length > 80) errores.push('El nombre admite como mucho 80 caracteres.')
  if (!TIPOS_RESIDUO.includes(e.tipo)) errores.push('El tipo es obligatorio.')
  if (!(e.peso > 0)) errores.push('El peso debe ser mayor a 0.')
  else if (e.peso > 100000) errores.push('El peso no puede superar 100000 kg.')
  if (!ZONAS.includes(e.zona)) errores.push('La zona es obligatoria.')
  if (e.prioridad != null && (e.prioridad < 1 || e.prioridad > 5)) errores.push('La prioridad debe estar entre 1 y 5.')
  if (errores.length) throw new ErrorApi(errores.join(' '), 400)
}

/**
 * Motor de la demo: aplica las operaciones sobre un EstadoDemo y avisa con
 * `alCambiar` para que se persista.
 */
export class MotorDemo {
  estado: EstadoDemo
  private readonly alCambiar: (estado: EstadoDemo) => void
  private readonly hoy: () => string

  /**
   * @param hoy fecha ISO del día; se inyecta para poder fijarla en las pruebas
   */
  constructor(estado: EstadoDemo, alCambiar: (e: EstadoDemo) => void = () => {}, hoy = () => new Date().toISOString().slice(0, 10)) {
    this.estado = estado
    this.alCambiar = alCambiar
    this.hoy = hoy
  }

  /** Copia defensiva: la UI nunca recibe objetos que el motor siga mutando. */
  private copia<T>(x: T): T {
    return structuredClone(x)
  }

  private guardar() {
    this.alCambiar(this.estado)
  }

  private buscar(id: number): Residuo {
    const r = this.estado.residuos.find((x) => x.id === id)
    if (!r) throw new ErrorApi(`No existe el residuo ${id}.`, 404)
    return r
  }

  listarResiduos(c: ConsultaResiduos = {}): Pagina<Residuo> {
    const filtrados = this.estado.residuos.filter(
      (r) => (!c.tipo || r.tipo === c.tipo) && (!c.zona || r.zona === c.zona) && (!c.estado || r.estado === c.estado),
    )
    filtrados.sort(comparador(c.orden ?? 'id', c.dir ?? 'asc'))
    return this.copia(paginar(filtrados, c.pagina, c.tamano))
  }

  crearResiduo(e: ResiduoEntrada): Residuo {
    validarResiduo(e)
    const r: Residuo = {
      id: this.estado.siguienteId++,
      nombre: e.nombre.trim(),
      tipo: e.tipo,
      peso: e.peso,
      zona: e.zona,
      prioridad: e.prioridad ?? PRIORIDAD_POR_TIPO[e.tipo],
      estado: 'PENDIENTE',
      fechaRecoleccion: null,
    }
    this.estado.residuos.push(r)
    this.guardar()
    return this.copia(r)
  }

  actualizarResiduo(id: number, e: ResiduoEntrada): Residuo {
    validarResiduo(e)
    const r = this.buscar(id)
    if (r.estado !== 'PENDIENTE') {
      throw new ErrorApi(`Solo se pueden editar residuos pendientes; "${r.nombre}" ya fue recogido.`, 409)
    }
    Object.assign(r, { nombre: e.nombre.trim(), tipo: e.tipo, peso: e.peso, zona: e.zona, prioridad: e.prioridad ?? r.prioridad })
    this.guardar()
    return this.copia(r)
  }

  eliminarResiduo(id: number): void {
    const r = this.buscar(id)
    if (r.estado === 'EN_CENTRO') {
      throw new ErrorApi(`"${r.nombre}" está en el centro de reciclaje: procésalo antes de eliminarlo.`, 409)
    }
    this.estado.residuos = this.estado.residuos.filter((x) => x.id !== id)
    this.guardar()
  }

  estadoZonas(): EstadoZona[] {
    return calcularZonas(this.estado.residuos)
  }

  listarVehiculos(): Vehiculo[] {
    return ordenarFlota(this.estado).map((v, i) => ({ ...v, estado: 'DISPONIBLE', turno: i + 1 }))
  }

  crearVehiculo(e: VehiculoEntrada): Vehiculo {
    const id = (e.id ?? '').trim().toUpperCase()
    const errores: string[] = []
    if (!/^[A-Z0-9-]{2,20}$/.test(id)) errores.push('El id debe tener de 2 a 20 letras, números o guiones.')
    if (!ZONAS.includes(e.zonaAsignada)) errores.push('La zona es obligatoria.')
    if (!(e.capacidad > 0)) errores.push('La capacidad debe ser mayor a 0.')
    else if (e.capacidad > 50000) errores.push('La capacidad no puede superar 50000 kg.')
    if (errores.length) throw new ErrorApi(errores.join(' '), 400)
    if (this.estado.vehiculos.some((v) => v.id === id)) throw new ErrorApi(`Ya existe un vehículo con id ${id}.`, 409)
    this.estado.vehiculos.push({ id, zonaAsignada: e.zonaAsignada, capacidad: e.capacidad })
    this.guardar()
    return this.listarVehiculos().find((v) => v.id === id)!
  }

  eliminarVehiculo(id: string): void {
    const antes = this.estado.vehiculos.length
    this.estado.vehiculos = this.estado.vehiculos.filter((v) => v.id.toUpperCase() !== id.toUpperCase())
    if (this.estado.vehiculos.length === antes) throw new ErrorApi(`No existe el vehículo ${id}.`, 404)
    this.guardar()
  }

  /** Mismo algoritmo que Despacho.despachar del backend. */
  despachar(): ResultadoDespacho {
    const { estado } = this
    if (estado.vehiculos.length === 0) throw new ErrorApi('No hay vehículos registrados para despachar.', 409)
    if (estado.pila.length >= CAPACIDAD_CENTRO) {
      throw new ErrorApi('El centro de reciclaje está lleno: procesa residuos antes de despachar.', 409)
    }

    let elegido: VehiculoGuardado | undefined
    let pendientes: Residuo[] = []
    for (const v of ordenarFlota(estado)) {
      pendientes = estado.residuos.filter((r) => r.estado === 'PENDIENTE' && r.zona === v.zonaAsignada)
      if (pendientes.length) {
        elegido = v
        break
      }
    }
    if (!elegido) throw new ErrorApi('Ninguna zona con vehículo asignado tiene residuos pendientes.', 409)

    // Mayor prioridad primero y, a igual prioridad, el más pesado (sort estable como el de la lista).
    pendientes.sort((a, b) => b.prioridad - a.prioridad || b.peso - a.peso)
    const espacio = CAPACIDAD_CENTRO - estado.pila.length
    const carga: Residuo[] = []
    let cargaActual = 0
    let faltoCapacidad = false
    for (const r of pendientes) {
      if (carga.length >= espacio) break
      if (cargaActual + r.peso <= elegido.capacidad) {
        carga.push(r)
        cargaActual += r.peso
      } else faltoCapacidad = true
    }

    const hoy = this.hoy()
    for (const r of carga) {
      r.estado = 'EN_CENTRO'
      r.fechaRecoleccion = hoy
      estado.pila.unshift(r.id)
    }

    const quedan = pendientes.length - carga.length
    let aviso: string | null = null
    if (carga.length === 0) {
      aviso = `Ningún residuo cabe en el vehículo ${elegido.id}: su capacidad es menor que el residuo más liviano.`
    } else if (quedan > 0) {
      aviso = `Quedan ${quedan} residuos pendientes en la zona: ${faltoCapacidad ? 'se alcanzó la capacidad del vehículo.' : 'el centro de reciclaje se llenó.'}`
    }
    this.guardar()
    const vehiculo = this.listarVehiculos().find((v) => v.id === elegido.id)!
    return this.copia({ vehiculo, zona: elegido.zonaAsignada, residuos: carga, pesoTotal: cargaActual, aviso })
  }

  estadoCentro(): EstadoCentro {
    const porId = new Map(this.estado.residuos.map((r) => [r.id, r]))
    return this.copia({
      capacidadMaxima: CAPACIDAD_CENTRO,
      pila: this.estado.pila.map((id) => porId.get(id)!).filter(Boolean),
      procesados: this.estado.procesados,
    })
  }

  procesarSiguiente(): Residuo {
    const id = this.estado.pila.shift()
    if (id === undefined) throw new ErrorApi('No hay residuos esperando en el centro de reciclaje.', 409)
    const r = this.buscar(id)
    r.estado = 'PROCESADO'
    this.estado.procesados++
    this.guardar()
    return this.copia(r)
  }

  estadisticas(): Estadisticas {
    const { residuos } = this.estado
    const pesoPorTipo = {} as Record<TipoResiduo, number>
    const cantidadPorTipo = {} as Record<TipoResiduo, number>
    for (const t of TIPOS_RESIDUO) {
      pesoPorTipo[t] = 0
      cantidadPorTipo[t] = 0
    }
    let pesoTotal = 0
    for (const r of residuos) {
      pesoPorTipo[r.tipo] += r.peso
      cantidadPorTipo[r.tipo]++
      pesoTotal += r.peso
    }
    const zonas = calcularZonas(residuos)

    let tipoMasComun: TipoResiduo | null = null
    for (const t of TIPOS_RESIDUO) {
      if (cantidadPorTipo[t] > (tipoMasComun ? cantidadPorTipo[tipoMasComun] : 0)) tipoMasComun = t
    }
    /** Zona extrema según el campo; ignora ceros si se pide (como EstadisticasMap). */
    const extremo = (campo: 'pesoPendiente' | 'utilidad', maximo: boolean, soloPositivos: boolean): ZonaGuayaquil | null => {
      let elegida: EstadoZona | null = null
      for (const z of zonas) {
        if (soloPositivos && z[campo] <= 0) continue
        if (!elegida || (maximo ? z[campo] > elegida[campo] : z[campo] < elegida[campo])) elegida = z
      }
      return elegida?.zona ?? null
    }

    return {
      totalResiduos: residuos.length,
      pesoTotal,
      pesoPromedio: residuos.length ? pesoTotal / residuos.length : 0,
      pesoPorTipo,
      cantidadPorTipo,
      zonas,
      tipoMasComun,
      zonaMasPendiente: extremo('pesoPendiente', true, true),
      zonaMejorUtilidad: extremo('utilidad', true, false),
      zonaPeorUtilidad: extremo('utilidad', false, false),
    }
  }

  exportarCsv(): string {
    return residuosACsv(this.estado.residuos)
  }

  /** Igual que EcoTrackData.incorporarResiduos: reconstruye la pila con los que están EN_CENTRO. */
  importarCsv(contenido: string, modo: ModoImportacion): ResultadoImportacion {
    const { residuos, errores } = leerResiduosCsv(contenido, this.hoy())
    if (residuos.length === 0) {
      throw new ErrorApi(`El archivo no contiene residuos válidos.${errores.length ? ' ' + errores.slice(0, 3).join(' ') : ''}`, 409)
    }
    const { estado } = this
    const reemplazar = modo === 'reemplazar'
    if (reemplazar) {
      estado.residuos = []
      estado.pila = []
      estado.siguienteId = 1
    }
    for (const r of residuos) {
      if (!reemplazar || estado.residuos.some((x) => x.id === r.id)) r.id = estado.siguienteId
      estado.siguienteId = Math.max(estado.siguienteId, r.id) + 1
      if (r.estado === 'EN_CENTRO') {
        if (estado.pila.length < CAPACIDAD_CENTRO) estado.pila.unshift(r.id)
        else {
          r.estado = 'PENDIENTE'
          r.fechaRecoleccion = null
        }
      }
      estado.residuos.push(r)
    }
    estado.procesados = estado.residuos.filter((r) => r.estado === 'PROCESADO').length
    this.guardar()
    return { importados: residuos.length, errores }
  }
}

import { ESTADOS_RESIDUO, TIPOS_RESIDUO, ZONAS, type EstadoResiduo, type Residuo, type TipoResiduo, type ZonaGuayaquil } from '../types'
import { PRIORIDAD_POR_TIPO } from './dominio'

// Mismo formato que DataManager del backend, para que un CSV descargado de un modo
// se pueda subir en el otro.
export const ENCABEZADO_RESIDUOS = 'ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado'

/** Pone comillas a un campo con coma, comillas o salto de línea (RFC 4180). */
export function escapar(texto: string): string {
  return /[",\n]/.test(texto) ? `"${texto.replaceAll('"', '""')}"` : texto
}

/** Divide una línea CSV respetando campos entre comillas. */
export function dividir(linea: string): string[] {
  const campos: string[] = []
  let actual = ''
  let comillas = false
  for (let i = 0; i < linea.length; i++) {
    const ch = linea[i]
    if (comillas) {
      if (ch === '"' && linea[i + 1] === '"') {
        actual += '"'
        i++
      } else if (ch === '"') comillas = false
      else actual += ch
    } else if (ch === '"') comillas = true
    else if (ch === ',') {
      campos.push(actual)
      actual = ''
    } else actual += ch
  }
  campos.push(actual)
  return campos
}

/** Serializa residuos con encabezado. */
export function residuosACsv(residuos: Residuo[]): string {
  const filas = residuos.map((r) =>
    [r.id, escapar(r.nombre), r.tipo, r.peso, r.fechaRecoleccion ?? '', r.zona, r.prioridad, r.estado].join(','),
  )
  return [ENCABEZADO_RESIDUOS, ...filas].join('\n') + '\n'
}

/** Valida que un texto sea uno de los valores permitidos de un enum. */
function deEnum<T extends string>(valores: readonly T[], texto: string, campo: string): T {
  const v = texto.trim().toUpperCase() as T
  if (!valores.includes(v)) throw new Error(`${campo} no válido: ${texto}`)
  return v
}

/** Parsea un CSV de residuos; las líneas inválidas se informan sin detener la lectura. */
export function leerResiduosCsv(contenido: string, hoy: string): { residuos: Residuo[]; errores: string[] } {
  const residuos: Residuo[] = []
  const errores: string[] = []
  contenido
    .replaceAll('\r', '')
    .split('\n')
    .forEach((linea, i) => {
      if (!linea.trim() || (i === 0 && linea.startsWith('ID,'))) return
      try {
        const c = dividir(linea)
        if (c.length !== 7 && c.length !== 8) throw new Error(`se esperaban 7 u 8 columnas y hay ${c.length}`)
        const id = Number.parseInt(c[0], 10)
        if (Number.isNaN(id)) throw new Error('el ID no es un número')
        const nombre = c[1].trim()
        if (!nombre) throw new Error('el nombre está vacío')
        const tipo: TipoResiduo = deEnum(TIPOS_RESIDUO, c[2], 'Tipo')
        const peso = Number(c[3])
        if (!(peso > 0)) throw new Error('el peso debe ser mayor a 0')
        const fecha = c[4].trim() || null
        const zona: ZonaGuayaquil = deEnum(ZONAS, c[5], 'Zona')
        let prioridad = c[6].trim() ? Number.parseInt(c[6], 10) : 0
        if (Number.isNaN(prioridad) || prioridad < 0 || prioridad > 5) throw new Error('la prioridad debe estar entre 1 y 5')
        if (prioridad === 0) prioridad = PRIORIDAD_POR_TIPO[tipo]
        const estado: EstadoResiduo =
          c.length === 8 && c[7].trim() ? deEnum(ESTADOS_RESIDUO, c[7], 'Estado') : fecha ? 'PROCESADO' : 'PENDIENTE'
        residuos.push({
          id,
          nombre,
          tipo,
          peso,
          zona,
          prioridad,
          estado,
          fechaRecoleccion: estado === 'PENDIENTE' ? null : (fecha ?? hoy),
        })
      } catch (e) {
        errores.push(`Línea ${i + 1}: ${e instanceof Error ? e.message : String(e)}`)
      }
    })
  return { residuos, errores }
}

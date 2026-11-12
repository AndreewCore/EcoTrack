import { beforeEach, describe, expect, it } from 'vitest'
import { estadoInicial, MotorDemo, paginar } from './motor'
import type { EstadoDemo } from './motor'

const HOY = '2026-11-12'
let motor: MotorDemo

beforeEach(() => {
  motor = new MotorDemo(estadoInicial(), () => {}, () => HOY)
})

/** Motor vacío (sin residuos ni vehículos) con un centro de capacidad normal. */
function motorVacio(): MotorDemo {
  const estado: EstadoDemo = { version: 1, residuos: [], vehiculos: [], pila: [], procesados: 0, siguienteId: 1 }
  return new MotorDemo(estado, () => {}, () => HOY)
}

describe('paginar', () => {
  it('acota la página pedida al rango válido', () => {
    expect(paginar([1, 2, 3, 4, 5], 9, 2)).toEqual({ elementos: [5], pagina: 3, totalPaginas: 3, totalElementos: 5 })
  })
})

describe('residuos', () => {
  it('asigna id y prioridad por defecto del tipo', () => {
    const r = motor.crearResiduo({ nombre: 'Pilas', tipo: 'PELIGROSO', peso: 2.5, zona: 'FAE' })
    expect(r).toMatchObject({ id: 25, prioridad: 5, estado: 'PENDIENTE' })
  })

  it('filtra y ordena como la API', () => {
    const p = motor.listarResiduos({ tipo: 'ORGANICO', orden: 'peso', dir: 'desc' })
    expect(p.totalElementos).toBe(5)
    expect(p.elementos[0].nombre).toBe('Restos de comida')
  })

  it('valida la entrada con los mensajes del backend', () => {
    expect(() => motor.crearResiduo({ nombre: ' ', tipo: 'PLASTICO', peso: -1, zona: 'FAE' })).toThrow(
      'El nombre es obligatorio. El peso debe ser mayor a 0.',
    )
  })
})

describe('flujo de recolección', () => {
  it('despacha la zona más crítica respetando la capacidad', () => {
    expect(motor.listarVehiculos()[0].id).toBe('GYE-002')
    const r = motor.despachar()
    expect(r.vehiculo.id).toBe('GYE-002')
    expect(r.zona).toBe('FORTIN_MONTE_SINAI')
    expect(r.pesoTotal).toBe(210)
    expect(r.aviso).toContain('capacidad')
    expect(r.residuos[0]).toMatchObject({ estado: 'EN_CENTRO', fechaRecoleccion: HOY })

    const fortin = motor.estadoZonas().find((z) => z.zona === 'FORTIN_MONTE_SINAI')!
    expect(fortin.utilidad).toBe(-310)
    expect(motor.listarVehiculos()[0].id).toBe('GYE-001')
  })

  it('apila en el centro y procesa en orden LIFO', () => {
    const m = motorVacio()
    m.crearResiduo({ nombre: 'Mucho', tipo: 'ORGANICO', peso: 400, zona: 'GUASMO' })
    m.crearResiduo({ nombre: 'Urgente', tipo: 'PELIGROSO', peso: 30, zona: 'GUASMO' })
    m.crearVehiculo({ id: 'B-1', zonaAsignada: 'GUASMO', capacidad: 1000 })

    const r = m.despachar()
    expect(r.residuos.map((x) => x.nombre)).toEqual(['Urgente', 'Mucho'])
    expect(m.estadoCentro().pila[0].nombre).toBe('Mucho')
    expect(m.procesarSiguiente().nombre).toBe('Mucho')
    expect(m.estadoCentro().procesados).toBe(1)
  })

  it('no permite editar ni borrar lo que está en el centro', () => {
    const m = motorVacio()
    const r = m.crearResiduo({ nombre: 'X', tipo: 'METAL', peso: 5, zona: 'FAE' })
    m.crearVehiculo({ id: 'E-1', zonaAsignada: 'FAE', capacidad: 10 })
    m.despachar()
    expect(() => m.actualizarResiduo(r.id, { nombre: 'Y', tipo: 'METAL', peso: 5, zona: 'FAE' })).toThrow(/pendientes/)
    expect(() => m.eliminarResiduo(r.id)).toThrow(/centro/)
    m.procesarSiguiente()
    m.eliminarResiduo(r.id)
    expect(m.listarResiduos().totalElementos).toBe(0)
  })

  it('informa por qué no se puede despachar', () => {
    const m = motorVacio()
    expect(() => m.despachar()).toThrow('No hay vehículos registrados para despachar.')
    m.crearVehiculo({ id: 'F-1', zonaAsignada: 'FAE', capacidad: 10 })
    expect(() => m.despachar()).toThrow(/pendientes/)
    expect(() => m.crearVehiculo({ id: 'f-1', zonaAsignada: 'CENTRO', capacidad: 5 })).toThrow(/Ya existe/)
  })
})

describe('estadísticas e importación', () => {
  it('calcula los extremos como EstadisticasMap', () => {
    const s = motor.estadisticas()
    expect(s.totalResiduos).toBe(24)
    expect(s.tipoMasComun).toBe('ORGANICO')
    expect(s.zonaPeorUtilidad).toBe('FORTIN_MONTE_SINAI')
  })

  it('exporta e importa CSV en modo agregar y reemplazar', () => {
    const csv = motor.exportarCsv()
    expect(csv.startsWith('ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado\n')).toBe(true)

    const archivo = 'ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado\n1,"Nuevo, con coma",PAPEL,1,,FAE,1,PENDIENTE\nroto\n'
    expect(motor.importarCsv(archivo, 'agregar')).toEqual({ importados: 1, errores: ['Línea 3: se esperaban 7 u 8 columnas y hay 1'] })
    expect(motor.listarResiduos().totalElementos).toBe(25)
    expect(motor.listarResiduos({ orden: 'id', dir: 'desc' }).elementos[0]).toMatchObject({ id: 25, nombre: 'Nuevo, con coma' })

    motor.importarCsv(archivo, 'reemplazar')
    expect(motor.listarResiduos().totalElementos).toBe(1)
  })
})

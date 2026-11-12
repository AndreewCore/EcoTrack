import { describe, expect, it } from 'vitest'
import { crearApiMemoria, paginar } from './memoria'

describe('paginar', () => {
  it('acota la página pedida al rango válido', () => {
    const p = paginar([1, 2, 3, 4, 5], 9, 2)
    expect(p).toEqual({ elementos: [5], pagina: 3, totalPaginas: 3, totalElementos: 5 })
  })

  it('devuelve una página vacía cuando no hay elementos', () => {
    expect(paginar([], 1, 10)).toEqual({ elementos: [], pagina: 1, totalPaginas: 1, totalElementos: 0 })
  })
})

describe('api en memoria', () => {
  it('asigna la prioridad por defecto del tipo al crear', async () => {
    const api = crearApiMemoria()
    const r = await api.crearResiduo({ nombre: 'Pilas', tipo: 'PELIGROSO', peso: 3, zona: 'FAE' })
    expect(r.prioridad).toBe(5)
    expect(r.estado).toBe('PENDIENTE')
  })

  it('filtra por tipo y ordena por peso descendente', async () => {
    const api = crearApiMemoria()
    const p = await api.listarResiduos({ tipo: 'ORGANICO', orden: 'peso', dir: 'desc', tamano: 50 })
    const pesos = p.elementos.map((r) => r.peso)
    expect(p.elementos.every((r) => r.tipo === 'ORGANICO')).toBe(true)
    expect(pesos).toEqual([...pesos].sort((a, b) => b - a))
  })

  it('responde 404 al eliminar un residuo inexistente', async () => {
    const api = crearApiMemoria()
    await expect(api.eliminarResiduo(999)).rejects.toMatchObject({ estado: 404 })
  })
})

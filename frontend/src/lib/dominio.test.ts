import { describe, expect, it } from 'vitest'
import { nivelPorUtilidad } from './dominio'

describe('nivelPorUtilidad', () => {
  it('usa los mismos umbrales que la app JavaFX', () => {
    expect(nivelPorUtilidad(0)).toBe('verde')
    expect(nivelPorUtilidad(120)).toBe('verde')
    expect(nivelPorUtilidad(-1)).toBe('amarillo')
    expect(nivelPorUtilidad(-500)).toBe('amarillo')
    expect(nivelPorUtilidad(-500.1)).toBe('rojo')
  })
})

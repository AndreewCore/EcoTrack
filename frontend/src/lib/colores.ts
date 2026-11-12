/** Colores de sección de la app original (uno por pantalla). */
export type ColorSeccion = 'residuos' | 'rutas' | 'centro' | 'estadisticas'

// Clases completas (no interpoladas) para que Tailwind las detecte al compilar.
export const CLASES_SECCION: Record<
  ColorSeccion,
  { fondo: string; borde: string; texto: string; anillo: string; suave: string }
> = {
  residuos: {
    fondo: 'bg-residuos',
    borde: 'border-residuos',
    texto: 'text-residuos',
    anillo: 'focus-visible:outline-residuos',
    suave: 'bg-residuos/10',
  },
  rutas: {
    fondo: 'bg-rutas',
    borde: 'border-rutas',
    texto: 'text-rutas',
    anillo: 'focus-visible:outline-rutas',
    suave: 'bg-rutas/15',
  },
  centro: {
    fondo: 'bg-centro',
    borde: 'border-centro',
    texto: 'text-centro',
    anillo: 'focus-visible:outline-centro',
    suave: 'bg-centro/15',
  },
  estadisticas: {
    fondo: 'bg-estadisticas',
    borde: 'border-estadisticas',
    texto: 'text-estadisticas',
    anillo: 'focus-visible:outline-estadisticas',
    suave: 'bg-estadisticas/10',
  },
}

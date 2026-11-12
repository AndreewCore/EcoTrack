// Iconos de trazo usados en las escenas originales, redibujados en SVG para que
// escalen y hereden el color del texto.

interface IconoProps {
  className?: string
}

/** Envoltorio común: viewBox 24x24 y trazo con el color actual. */
function Svg({ className, children }: IconoProps & { children: React.ReactNode }) {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={2}
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
      className={className}
    >
      {children}
    </svg>
  )
}

/** Flecha de regreso al menú principal. */
export function IconoVolver({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M20 12H5M11 5l-7 7 7 7" strokeWidth={3} />
    </Svg>
  )
}

/** Tacho de basura (tarjeta Residuos). */
export function IconoTacho({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M4 6h16M9 6V4h6v2M6 6l1 15h10l1-15M10 10v8M14 10v8" />
    </Svg>
  )
}

/** Marcador de ubicación (tarjeta Rutas y selector de zona). */
export function IconoUbicacion({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M12 21s-6-6.2-6-11a6 6 0 1 1 12 0c0 4.8-6 11-6 11z" />
      <circle cx="12" cy="10" r="2" />
      <ellipse cx="12" cy="21" rx="4" ry="1" />
    </Svg>
  )
}

/** Diana (tarjeta Centro de reciclaje). */
export function IconoDiana({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <circle cx="12" cy="12" r="7" />
      <circle cx="12" cy="12" r="3" />
      <path d="M12 2v3M12 19v3M2 12h3M19 12h3" />
    </Svg>
  )
}

/** Gráfico circular (tarjeta Estadísticas). */
export function IconoPastel({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M11 3a9 9 0 1 0 9 9h-9z" />
      <path d="M14 3.5V10h6.5A7 7 0 0 0 14 3.5z" />
    </Svg>
  )
}

/** Bandeja con flecha hacia abajo (descargar). */
export function IconoDescargar({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M12 3v12M7 10l5 5 5-5M4 15v4a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1v-4" />
    </Svg>
  )
}

/** Bandeja con flecha hacia arriba (subir). */
export function IconoSubir({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M12 15V3M7 8l5-5 5 5M4 15v4a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1v-4" />
    </Svg>
  )
}

/** Signo más grueso del botón GUARDAR. */
export function IconoMas({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M12 4v16M4 12h16" strokeWidth={4} strokeLinecap="butt" />
    </Svg>
  )
}

/** Chevron hacia arriba o abajo para la paginación. */
export function IconoChevron({ className, abajo = false }: IconoProps & { abajo?: boolean }) {
  return (
    <Svg className={className}>
      <path d={abajo ? 'M6 9l6 6 6-6' : 'M6 15l6-6 6 6'} strokeWidth={3} />
    </Svg>
  )
}

/** Chevron a la derecha (botón PROCESAR >>>). */
export function IconoAvanzar({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M9 5l7 7-7 7" strokeWidth={3.5} />
    </Svg>
  )
}

/** Papelera pequeña para eliminar filas. */
export function IconoEliminar({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M5 7h14M10 7V5h4v2M7 7l1 12h8l1-12" />
    </Svg>
  )
}

/** Lápiz para editar filas. */
export function IconoEditar({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M4 20h4L19 9l-4-4L4 16zM13 7l4 4" />
    </Svg>
  )
}

/** Camión recolector (vehículos). */
export function IconoCamion({ className }: IconoProps) {
  return (
    <Svg className={className}>
      <path d="M2 6h11v10H2zM13 10h4l3 3v3h-7" />
      <circle cx="6" cy="18" r="2" />
      <circle cx="16" cy="18" r="2" />
    </Svg>
  )
}

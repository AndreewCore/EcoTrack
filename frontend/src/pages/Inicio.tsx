import { Link } from 'react-router'
import { IconoDescargar, IconoDiana, IconoPastel, IconoSubir, IconoTacho, IconoUbicacion } from '../components/Iconos'

const TARJETAS = [
  { a: '/residuos', titulo: 'Residuos', fondo: 'bg-residuos-tarjeta', Icono: IconoTacho },
  { a: '/rutas', titulo: 'Rutas', fondo: 'bg-rutas-tarjeta', Icono: IconoUbicacion },
  { a: '/centro', titulo: 'Centro reciclaje', fondo: 'bg-centro-tarjeta', Icono: IconoDiana },
  { a: '/estadisticas', titulo: 'Estadísticas', fondo: 'bg-estadisticas', Icono: IconoPastel },
] as const

interface InicioProps {
  /** Descarga el estado actual como CSV. */
  onDescargar?: () => void
  /** Abre el flujo para importar un CSV. */
  onSubir?: () => void
}

/** Menú principal: banner en capas, acciones de datos y una tarjeta por pantalla. */
export default function Inicio({ onDescargar, onSubir }: InicioProps) {
  return (
    <main className="mx-auto flex min-h-dvh max-w-[1280px] flex-col px-4 pb-6 sm:px-8">
      {/* Banner apilado: cuatro capas de verde que se estrechan hacia el centro. */}
      <div className="relative mx-auto mt-0 h-28 w-full sm:h-40">
        <div className="bg-banner-1 absolute inset-x-[2%] top-0 bottom-0 rounded-b-[2rem]" />
        <div className="bg-banner-2 absolute inset-x-[7%] top-0 bottom-0 rounded-b-[2rem]" />
        <div className="bg-banner-3 absolute inset-x-[12%] top-0 bottom-0 rounded-b-[2rem]" />
        <div className="bg-banner-4 absolute inset-x-[25%] top-0 bottom-0 rounded-b-[2rem] max-sm:inset-x-[17%]" />
        <h1 className="font-titulo absolute inset-0 grid place-items-center pt-2 text-[clamp(2.6rem,7vw,4.4rem)] font-semibold text-white">
          EcoTrack
        </h1>
      </div>

      <div className="mt-8 flex flex-col justify-center gap-4 sm:flex-row sm:gap-8">
        <BotonDatos onClick={onDescargar} Icono={IconoDescargar} texto="Descargar datos" />
        <BotonDatos onClick={onSubir} Icono={IconoSubir} texto="Subir datos" />
      </div>

      <nav aria-label="Secciones" className="mt-8 grid flex-1 grid-cols-2 gap-4 sm:gap-8 lg:grid-cols-4 lg:gap-16">
        {TARJETAS.map(({ a, titulo, fondo, Icono }) => (
          <Link
            key={a}
            to={a}
            className={`${fondo} group flex min-h-48 flex-col rounded-t-[2rem] rounded-b-lg p-4 text-white transition-transform hover:-translate-y-1 focus-visible:outline-4 focus-visible:outline-offset-2 focus-visible:outline-tinta sm:min-h-72 sm:p-5`}
          >
            <span className="font-titulo text-xl leading-tight font-semibold uppercase sm:text-2xl">{titulo}</span>
            <span className="grid flex-1 place-items-center">
              <Icono className="size-20 text-[#ececec] transition-transform group-hover:scale-110 sm:size-28" />
            </span>
          </Link>
        ))}
      </nav>
    </main>
  )
}

/** Botón verde redondeado de la fila DESCARGAR/SUBIR DATOS. */
function BotonDatos({
  onClick,
  Icono,
  texto,
}: {
  onClick?: () => void
  Icono: (p: { className?: string }) => React.ReactNode
  texto: string
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      disabled={!onClick}
      className="bg-residuos-tarjeta etiqueta flex items-center justify-center gap-3 rounded-[2rem] px-8 py-4 text-lg text-white transition hover:brightness-95 focus-visible:outline-4 focus-visible:outline-offset-2 focus-visible:outline-tinta disabled:cursor-not-allowed disabled:opacity-60 sm:min-w-96 sm:text-xl"
    >
      <Icono className="size-9" />
      {texto}
    </button>
  )
}

import { HashRouter, Route, Routes } from 'react-router'
import { CabeceraSeccion } from './components/Seccion'
import type { ColorSeccion } from './lib/colores'
import Inicio from './pages/Inicio'
import Residuos from './pages/Residuos'
import Rutas from './pages/Rutas'

/** Pantalla provisional para las secciones que aún no se han migrado. */
function EnConstruccion({ titulo, color }: { titulo: string; color: ColorSeccion }) {
  return (
    <main className="mx-auto min-h-dvh max-w-[1280px] sm:px-4">
      <CabeceraSeccion titulo={titulo} color={color} className="w-fit pr-10 sm:-ml-4" />
      <p className="p-8 text-lg">Esta sección llega en una próxima versión.</p>
    </main>
  )
}

/**
 * Raíz de la app. Usa HashRouter porque GitHub Pages no reescribe rutas
 * desconocidas a index.html.
 */
export default function App() {
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/residuos" element={<Residuos />} />
        <Route path="/rutas" element={<Rutas />} />
        <Route path="/centro" element={<EnConstruccion titulo="Centro de reciclaje" color="centro" />} />
        <Route path="/estadisticas" element={<EnConstruccion titulo="Estadísticas" color="estadisticas" />} />
        <Route path="*" element={<Inicio />} />
      </Routes>
    </HashRouter>
  )
}

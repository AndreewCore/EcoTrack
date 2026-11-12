import { lazy, Suspense } from 'react'
import { HashRouter, Route, Routes } from 'react-router'
import { AvisoDemo } from './components/AvisoDemo'
import CentroReciclaje from './pages/CentroReciclaje'
import Inicio from './pages/Inicio'
import Residuos from './pages/Residuos'

// Leaflet y Recharts son pesados: se cargan solo al entrar a su pantalla.
const Rutas = lazy(() => import('./pages/Rutas'))
const Estadisticas = lazy(() => import('./pages/Estadisticas'))

/**
 * Raíz de la app. Usa HashRouter porque GitHub Pages no reescribe rutas
 * desconocidas a index.html.
 */
export default function App() {
  return (
    <HashRouter>
      <Suspense fallback={<p className="p-8">Cargando…</p>}>
          <Routes>
          <Route path="/" element={<Inicio />} />
          <Route path="/residuos" element={<Residuos />} />
          <Route path="/rutas" element={<Rutas />} />
          <Route path="/centro" element={<CentroReciclaje />} />
          <Route path="/estadisticas" element={<Estadisticas />} />
          <Route path="*" element={<Inicio />} />
        </Routes>
      </Suspense>
      <AvisoDemo />
    </HashRouter>
  )
}

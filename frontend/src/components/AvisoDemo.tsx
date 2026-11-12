import { useState } from 'react'
import { api, restablecerDemo } from '../api'

/**
 * Distintivo fijo que indica que la app corre en modo demo (sin backend) y
 * permite volver a los datos de ejemplo.
 */
export function AvisoDemo() {
  const [abierto, setAbierto] = useState(false)
  if (api.modo !== 'demo' || !restablecerDemo) return null
  const restablecer = restablecerDemo

  return (
    <div className="fixed right-3 bottom-3 z-[1000] max-w-xs text-xs">
      {abierto ? (
        <div role="dialog" aria-label="Modo demo" className="rounded-xl bg-white p-3 shadow-lg">
          <p>
            <strong>Modo demo.</strong> Los datos viven en tu navegador (localStorage); la versión completa usa la API Spring Boot.
          </p>
          <div className="mt-2 flex gap-2">
            <button
              type="button"
              className="bg-residuos rounded-lg px-3 py-1 font-semibold text-white"
              onClick={async () => {
                if (!window.confirm('¿Volver a los datos de ejemplo? Se perderán tus cambios.')) return
                await restablecer()
                window.location.reload()
              }}
            >
              Restablecer datos
            </button>
            <button type="button" className="rounded-lg px-3 py-1" onClick={() => setAbierto(false)}>
              Cerrar
            </button>
          </div>
        </div>
      ) : (
        <button type="button" onClick={() => setAbierto(true)} className="bg-tinta/85 rounded-full px-3 py-1.5 font-semibold text-white shadow">
          Modo demo
        </button>
      )}
    </div>
  )
}

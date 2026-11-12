/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** 'api' (backend Spring Boot) o 'demo' (lógica en el navegador). */
  readonly VITE_MODO?: 'api' | 'demo'
  /** URL base de la API, p. ej. http://localhost:8080/api. */
  readonly VITE_API_URL?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

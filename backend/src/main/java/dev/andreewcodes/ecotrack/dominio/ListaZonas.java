package dev.andreewcodes.ecotrack.dominio;

import dev.andreewcodes.ecotrack.TDA.CircularDoublyLinkedList;

/**
 * Las 18 zonas de la ciudad guardadas en la lista circular propia.
 *
 * @author Paúl Rodríguez
 */
public class ListaZonas extends CircularDoublyLinkedList<Zona> {

    // Instancia compartida solo por la app de escritorio; la API crea la suya en EcoTrackData.
    private static ListaZonas instanciaEscritorio;

    /** Crea una zona por cada valor de {@link ZonaGuayaquil}, con pesos en cero. */
    public ListaZonas() {
        for (ZonaGuayaquil zonaEnum : ZonaGuayaquil.values()) {
            addLast(new Zona(zonaEnum));
        }
    }

    /** Instancia global que usan los controladores JavaFX. */
    public static ListaZonas getInstance() {
        if (instanciaEscritorio == null) {
            instanciaEscritorio = new ListaZonas();
        }
        return instanciaEscritorio;
    }

    /** Busca la zona que corresponde al valor del enum. */
    public Zona buscarPorZonaGuayaquil(ZonaGuayaquil zonaGuayaquil) {
        for (Zona zona : this) {
            if (zona.getZonaGuayaquil() == zonaGuayaquil) {
                return zona;
            }
        }
        return null;
    }

    /**
     * Recalcula los pesos de cada zona a partir de los residuos: lo pendiente suma a
     * "pendiente" y lo que ya está en el centro o procesado suma a "recolectado".
     * Derivarlos evita que se desincronicen con la lista de residuos.
     */
    public void recalcular(Iterable<Residuo> residuos) {
        for (Zona zona : this) {
            zona.reiniciar();
        }
        for (Residuo r : residuos) {
            Zona zona = buscarPorZonaGuayaquil(r.getZona());
            if (r.estaPendiente()) {
                zona.setPesoPendiente(zona.getPesoPendiente() + r.getPeso());
            } else {
                zona.setPesoRecolectado(zona.getPesoRecolectado() + r.getPeso());
            }
        }
    }

    /** Zona con más peso pendiente, o null si ninguna tiene residuos pendientes. */
    public Zona obtenerZonaMasPendiente() {
        Zona masPendiente = null;
        for (Zona zona : this) {
            if (zona.getPesoPendiente() > 0
                    && (masPendiente == null || zona.getPesoPendiente() > masPendiente.getPesoPendiente())) {
                masPendiente = zona;
            }
        }
        return masPendiente;
    }
}

package dev.andreewcodes.ecotrack.dominio;

/**
 * Estado de recolección de una zona: cuánto peso sigue pendiente y cuánto se ha
 * recogido ya.
 *
 * @author Paúl Rodríguez
 */
public class Zona {

    private final ZonaGuayaquil zonaGuayaquil;
    private double pesoPendiente;
    private double pesoRecolectado;

    public Zona(ZonaGuayaquil zonaGuayaquil) {
        this.zonaGuayaquil = zonaGuayaquil;
    }

    public double getPesoPendiente() {
        return pesoPendiente;
    }

    public void setPesoPendiente(double p) {
        pesoPendiente = p;
    }

    public double getPesoRecolectado() {
        return pesoRecolectado;
    }

    public void setPesoRecolectado(double p) {
        pesoRecolectado = p;
    }

    public ZonaGuayaquil getZonaGuayaquil() {
        return zonaGuayaquil;
    }

    /**
     * Utilidad ambiental u = P_recolectado − P_pendiente. Negativa indica acumulación
     * de basura y, por tanto, prioridad de atención.
     */
    public double calcularUtilidadAmbiental() {
        return pesoRecolectado - pesoPendiente;
    }

    /** Pone ambos pesos a cero antes de recalcularlos desde los residuos. */
    void reiniciar() {
        pesoPendiente = 0;
        pesoRecolectado = 0;
    }
}

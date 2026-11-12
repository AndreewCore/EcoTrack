package dev.andreewcodes.ecotrack.dominio;

import dev.andreewcodes.ecotrack.TDA.Pila;

/**
 * Centro de reciclaje: los residuos recogidos esperan en una pila (LIFO), así que
 * se procesa primero lo último que llegó.
 */
public class CentroReciclaje {

    public static final int CAPACIDAD_POR_DEFECTO = 100;

    private final Pila<Residuo> pilaResiduos = new Pila<>();
    private final int capacidadMaxima;
    private int residuosProcesados;

    public CentroReciclaje(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    /**
     * Apila un residuo recogido.
     *
     * @return false si el centro está lleno
     */
    public boolean agregarResiduo(Residuo r) {
        if (estaLleno()) {
            return false;
        }
        r.setEstado(EstadoResiduo.EN_CENTRO);
        pilaResiduos.apilar(r);
        return true;
    }

    /** Desapila y marca como procesado el residuo del tope, o null si no hay ninguno. */
    public Residuo procesarResiduo() {
        if (pilaResiduos.estaVacia()) {
            return null;
        }
        Residuo r = pilaResiduos.desapilar();
        r.setEstado(EstadoResiduo.PROCESADO);
        residuosProcesados++;
        return r;
    }

    public Residuo verProximoResiduo() {
        return pilaResiduos.estaVacia() ? null : pilaResiduos.verTope();
    }

    public boolean hayPendientes() {
        return !pilaResiduos.estaVacia();
    }

    public boolean estaLleno() {
        return pilaResiduos.getTamaño() >= capacidadMaxima;
    }

    public int getEspacioLibre() {
        return capacidadMaxima - pilaResiduos.getTamaño();
    }

    /** Pila de residuos; se recorre del tope al fondo. */
    public Pila<Residuo> getResiduos() {
        return pilaResiduos;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getResiduosProcesados() {
        return residuosProcesados;
    }

    /** Ajusta el contador al reconstruir el estado desde archivo. */
    public void setResiduosProcesados(int residuosProcesados) {
        this.residuosProcesados = residuosProcesados;
    }

    /** Vacía la pila (al reemplazar todos los datos). */
    public void vaciar() {
        pilaResiduos.vaciar();
        residuosProcesados = 0;
    }
}

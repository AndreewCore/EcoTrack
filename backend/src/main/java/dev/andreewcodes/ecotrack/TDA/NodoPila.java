package dev.andreewcodes.ecotrack.TDA;

/**
 *
 * @author Paúl Rodríguez
 */
public class NodoPila<E> {
    private E elemento;
    private NodoPila<E> siguiente;
        
    public NodoPila(E elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }
        
    public E getElemento() {
        return elemento;
    }
        
    public void setElemento(E elemento) {
        this.elemento = elemento;
    }
        
    public NodoPila<E> getSiguiente() {
        return siguiente;
    }
        
    public void setSiguiente(NodoPila<E> siguiente) {
        this.siguiente = siguiente;
    }
}

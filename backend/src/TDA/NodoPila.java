/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

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

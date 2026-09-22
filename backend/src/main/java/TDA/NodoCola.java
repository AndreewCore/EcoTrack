/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Paúl Rodríguez
 */

public class NodoCola<E> {
    private E elemento;
    private NodoCola<E> siguiente;
      
    public NodoCola(E elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }
        
    public E getElemento() {
        return elemento;
    }
      
    public void setElemento(E elemento) {
        this.elemento = elemento;
    }
       
    public NodoCola<E> getSiguiente() {
        return siguiente;
    }
        
    public void setSiguiente(NodoCola<E> siguiente) {
        this.siguiente = siguiente;
    }
}

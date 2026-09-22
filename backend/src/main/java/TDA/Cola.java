package TDA;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Paúl Rodríguez
 */

public class Cola<E> {
    
    private NodoCola<E> frente;  // Nodo al frente de la cola (próximo a salir)
    private NodoCola<E> fin;     // Nodo al final de la cola (último en entrar)
    private int tamaño;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamaño = 0;
    }
    
    public void encolar(E elemento) {
        NodoCola<E> nuevoNodo = new NodoCola<>(elemento);
        
        if (estaVacia()) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
        }
        
        tamaño++;
    }
    
    public E desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desencolar de una cola vacía");
        }
        
        E elemento = frente.getElemento();
        frente = frente.getSiguiente();
        tamaño--;
        
        // Si la cola queda vacía, también actualizar fin
        if (frente == null) {
            fin = null;
        }
        
        return elemento;
    }
    
    public E verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return frente.getElemento();
    }
    
    public boolean estaVacia() {
        return tamaño == 0;
    }
    
    public int getTamaño() {
        return tamaño;
    }
    
    public void vaciar() {
        frente = null;
        fin = null;
        tamaño = 0;
    }
    
    public boolean contiene(E elemento) {
        NodoCola<E> actual = frente;
        while (actual != null) {
            if (actual.getElemento().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    @Override
    public String toString() {
        if (estaVacia()) {
            return "Cola vacía";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Cola [Frente -> Fin]: ");
        NodoCola<E> actual = frente;
        
        while (actual != null) {
            sb.append(actual.getElemento().toString());
            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        if (estaVacia()) {
            return (E[]) new Object[0];
        }
        
        E[] array = (E[]) new Object[tamaño];
        NodoCola<E> actual = frente;
        int index = 0;
        
        while (actual != null) {
            array[index++] = actual.getElemento();
            actual = actual.getSiguiente();
        }
        
        return array;
    }

    public E obtenerEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamaño) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        
        NodoCola<E> actual = frente;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        
        return actual.getElemento();
    }

}

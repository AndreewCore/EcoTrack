package dev.andreewcodes.ecotrack.TDA;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Paúl Rodríguez
 */

public class Pila<E> implements Iterable<E> {
    
    private NodoPila<E> tope;  // Nodo en el tope de la pila
    private int tamaño;

    public Pila() {
        this.tope = null;
        this.tamaño = 0;
    }

    public void apilar(E elemento) {
        NodoPila<E> nuevoNodo = new NodoPila<>(elemento);
        
        if (!estaVacia()) {
            nuevoNodo.setSiguiente(tope);
        }
        
        tope = nuevoNodo;
        tamaño++;
    }
    
    public E desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desapilar de una pila vacía");
        }
        
        E elemento = tope.getElemento();
        tope = tope.getSiguiente();
        tamaño--;
        
        return elemento;
    }
    
    public E verTope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return tope.getElemento();
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int getTamaño() {
        return tamaño;
    }
 
    public void vaciar() {
        tope = null;
        tamaño = 0;
    }

    public boolean contiene(E elemento) {
        NodoPila<E> actual = tope;
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
            return "Pila vacía";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Pila [Tope -> Base]: ");
        NodoPila<E> actual = tope;
        
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
        NodoPila<E> actual = tope;
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
        
        NodoPila<E> actual = tope;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        
        return actual.getElemento();
    }

    public int buscar(E elemento) {
        NodoPila<E> actual = tope;
        int posicion = 0;
        
        while (actual != null) {
            if (actual.getElemento().equals(elemento)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        
        return -1; // No encontrado
    }
    
    public Pila<E> copiar() {
        if (estaVacia()) {
            return new Pila<>();
        }
        
        // Usar una pila auxiliar para mantener el orden
        Pila<E> pilaAuxiliar = new Pila<>();
        Pila<E> nuevaPila = new Pila<>();
        
        // Volcar a auxiliar (invierte el orden)
        NodoPila<E> actual = tope;
        while (actual != null) {
            pilaAuxiliar.apilar(actual.getElemento());
            actual = actual.getSiguiente();
        }
        
        // Volcar de auxiliar a nueva (restaura el orden)
        while (!pilaAuxiliar.estaVacia()) {
            nuevaPila.apilar(pilaAuxiliar.desapilar());
        }
        
        return nuevaPila;
    }

    public void invertir() {
        if (tamaño <= 1) {
            return;
        }
        
        Pila<E> pilaAuxiliar = new Pila<>();
        
        // Desapilar todo a la pila auxiliar (esto invierte el orden)
        while (!estaVacia()) {
            pilaAuxiliar.apilar(this.desapilar());
        }
        
        // Ahora pilaAuxiliar tiene el orden invertido
        this.tope = pilaAuxiliar.tope;
        this.tamaño = pilaAuxiliar.tamaño;
    }


    /** Recorre los elementos del tope al fondo sin modificar la estructura. */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private NodoPila<E> actual = tope;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public E next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                E elemento = actual.getElemento();
                actual = actual.getSiguiente();
                return elemento;
            }
        };
    }
}

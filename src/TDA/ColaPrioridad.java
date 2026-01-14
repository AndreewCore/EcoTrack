/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

import java.util.Comparator;

/**
 * 
 * @author Paúl Rodríguez
 */
public class ColaPrioridad<E> {
    
    private NodoCola<E> frente;
    private int tamaño;
    private Comparator<E> comparador;
    
    public ColaPrioridad() {
        this.frente = null;
        this.tamaño = 0;
        this.comparador = null;
    }
    
    public ColaPrioridad(Comparator<E> comparador) {
        this.frente = null;
        this.tamaño = 0;
        this.comparador = comparador;
    }
    
    public void encolar(E elemento) {
        NodoCola<E> nuevoNodo = new NodoCola<>(elemento);
        
        // Si la cola está vacía o el nuevo elemento tiene mayor prioridad que el frente
        if (estaVacia() || tieneMayorPrioridad(elemento, frente.getElemento())) {
            nuevoNodo.setSiguiente(frente);
            frente = nuevoNodo;
        } else {
            // Buscar la posición correcta para insertar
            NodoCola<E> actual = frente;
            
            while (actual.getSiguiente() != null && 
                   !tieneMayorPrioridad(elemento, actual.getSiguiente().getElemento())) {
                actual = actual.getSiguiente();
            }
            
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        
        tamaño++;
    }
    
    @SuppressWarnings("unchecked")
    private boolean tieneMayorPrioridad(E elemento1, E elemento2) {
        if (comparador != null) {
            return comparador.compare(elemento1, elemento2) > 0;
        } else {
            // Asume que E implementa Comparable
            return ((Comparable<E>) elemento1).compareTo(elemento2) > 0;
        }
    }
    
    public E desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desencolar de una cola vacía");
        }
        
        E elemento = frente.getElemento();
        frente = frente.getSiguiente();
        tamaño--;
        
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
            return "Cola de Prioridad vacía";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Cola de Prioridad [Mayor -> Menor]: ");
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

    public void cambiarComparador(Comparator<E> nuevoComparador) {
        if (estaVacia()) {
            this.comparador = nuevoComparador;
            return;
        }
        
        // Guardar todos los elementos
        E[] elementos = toArray();
        
        // Vaciar la cola
        vaciar();
        
        // Cambiar el comparador
        this.comparador = nuevoComparador;
        
        // Volver a encolar todos los elementos (se reordenarán automáticamente)
        for (E elemento : elementos) {
            encolar(elemento);
        }
    }

    public boolean remover(E elemento) {
        if (estaVacia()) {
            return false;
        }
        
        // Si el elemento está al frente
        if (frente.getElemento().equals(elemento)) {
            frente = frente.getSiguiente();
            tamaño--;
            return true;
        }
        
        // Buscar en el resto de la cola
        NodoCola<E> actual = frente;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getElemento().equals(elemento)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamaño--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        
        return false;
    }
}

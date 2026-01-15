/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Paúl Rodríguez
 */

import java.util.Comparator;
import java.util.Iterator;

public class CircularDoublyLinkedList<E> implements List<E>, Iterable<E>{
    private DoublyNodeList<E> last;
    private DoublyNodeList<E> header;
	
    public CircularDoublyLinkedList() {
        this.last = null;
    }
    
    @Override
    public int size() {
        if (last == null) return 0;
        int valor = 1;
        DoublyNodeList actual = last.getNext();
        while (actual != last){
            valor++;
            actual = actual.getNext();            
        }
        return valor;
    }
	
    @Override
    public boolean isEmpty() { return size() == 0; }
	
    @Override
    public void clear() { last = null; } 
	
    @Override
    public E get(int index){
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        DoublyNodeList<E> actual = last.getNext();
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }
        return actual.getContent();
    }

    @Override
    public E removeFirst(){
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }

        DoublyNodeList<E> first = last.getNext();
        E content = first.getContent();

        if (first == last) { 
            last = null; // Solo hay un elemento
        }
        else {
            DoublyNodeList<E> second = first.getNext();
            last.setNext(second);
            second.setPrev(last);
        }
        
        header = last.getNext();
        return content;
    }

    @Override
    public E removeLast(){
        if (isEmpty()) { 
            throw new IllegalStateException("La lista está vacía"); 
        }

        E content = last.getContent();

        if (last.getNext() == last) { // Solo hay un elemento
            last = null;
        } else {
            DoublyNodeList<E> newLast = last.getPrev();
            DoublyNodeList<E> first = last.getNext();
            newLast.setNext(first);
            first.setPrev(newLast);
            last = newLast;
        }

        header = last.getNext();
        return content;
    }

    @Override
    public E remove(int index){
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        if (index == 0) {
            return removeFirst();
        }
        if (index == size() - 1) {
            return removeLast();
        }

        DoublyNodeList<E> actual = last.getNext();
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }

        E content = actual.getContent();
        DoublyNodeList<E> prevNode = actual.getPrev();
        DoublyNodeList<E> nextNode = actual.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);

        header = last.getNext();
        return content;
    }

    @Override
    public boolean addFirst(E element) {
        DoublyNodeList<E> newNode = new DoublyNodeList<>();
        newNode.setContent(element);

        if (isEmpty()) {
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
            last = newNode;
        } else {
            DoublyNodeList<E> first = last.getNext();
            newNode.setNext(first);
            newNode.setPrev(last);
            first.setPrev(newNode);
            last.setNext(newNode);
        }
        header = last.getNext();

        return true;
    }

    @Override
    public boolean addLast(E element) {
        DoublyNodeList<E> newNode = new DoublyNodeList<>();
        newNode.setContent(element);

        if (isEmpty()) {
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
            last = newNode;
            header = last.getNext();
        } else {
            DoublyNodeList<E> first = last.getNext();
            newNode.setNext(first);
            newNode.setPrev(last);
            last.setNext(newNode);
            first.setPrev(newNode);
            last = newNode;
            header = last.getNext();
        }

        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size()) {
            addLast(element);
            return;
        }

        DoublyNodeList<E> newNode = new DoublyNodeList<>();
        newNode.setContent(element);

        DoublyNodeList<E> actual = last.getNext();
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }

        DoublyNodeList<E> prevNode = actual.getPrev();
        newNode.setNext(actual);
        newNode.setPrev(prevNode);
        prevNode.setNext(newNode);
        actual.setPrev(newNode);
        header = last.getNext();
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        DoublyNodeList<E> actual = last.getNext();
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }

        E oldContent = actual.getContent();
        actual.setContent(element);
        header = last.getNext();
        return oldContent;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        DoublyNodeList<E> current = last.getNext();
        do {
            sb.append(current.getContent());
            if (current != last) {
                sb.append(", ");
            }
            current = current.getNext();
        } while (current != last.getNext());
        sb.append("]");
        return sb.toString();
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private DoublyNodeList<E> current = (last != null) ? last.getNext() : null;
            private boolean started = false;

            public boolean hasNext() {
                return current != null && (!started || current != last.getNext());
            }

            public E next() {
                if (!started) {
                    started = true;
                }
                E content = current.getContent();
                current = current.getNext();
                return content;
            }
        };
    }

    private DoublyNodeList<E> getNodeAt(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamaño: " + size());
        }

        // Si la lista está vacía
        if (last == null) return null;

        // Si es el último nodo
        if (index == size() - 1) return last;

        // Si es el primer nodo
        if (index == 0) return last.getNext();

        int size = size();

        // Optimización: recorrer desde el extremo más cercano
        if (index <= size / 2) {
            // Recorrer desde el principio
            DoublyNodeList<E> current = last.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current;
        } else {
            // Recorrer desde el final usando getPrev()
            DoublyNodeList<E> current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
            return current;
        }
    }

    // Este método ya no es necesario en una lista doblemente enlazada
    // porque cada nodo tiene referencia directa a su predecesor con getPrev()
    // Lo incluyo solo por completitud, pero podrías simplemente usar node.getPrev()
    private DoublyNodeList<E> getPreviousNode(DoublyNodeList<E> node) {
        if (last == null || node == null) return null;
        return node.getPrev();
    }

    public void sort(Comparator<E> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("El comparador no puede ser null");
        }

        if (last == null || last.getNext() == last) {
            return;
        }

        int n = size();

        // Insertion Sort adaptado para lista circular doblemente enlazada
        for (int i = 1; i < n; i++) {
            E key = get(i); // Elemento a insertar en su posición correcta
            int j = i - 1;

            // Mover elementos mayores que 'key' una posición adelante
            while (j >= 0 && comparator.compare(get(j), key) > 0) {
                // Mover elemento en posición j a j+1
                getNodeAt(j + 1).setContent(get(j));
                j--;
            }

            // Insertar 'key' en su posición correcta
            getNodeAt(j + 1).setContent(key);
        }
    }
	
}

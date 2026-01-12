/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author Dominic
 */
public class CircularLinkedList<E> implements List<E>, Iterable<E> {

    private CircularNodeList<E> last;

    public CircularNodeList<E> getLast() {
        return last;
    }

    public CircularNodeList<E> getHeader() {
        return (last != null) ? last.getNext() : null;
    }

    public void setLast(CircularNodeList<E> last) {
        this.last = last;
    }

    @Override
    public boolean addFirst(E e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (e == null) {
            return false;
        }

        CircularNodeList<E> newNode = new CircularNodeList<>(e);

        if (last == null) {
            last = newNode;
            newNode.setNext(newNode); // Apunta a sí mismo
        } else {
            newNode.setNext(last.getNext());
            last.setNext(newNode);
        }
        return true;
    }

    @Override
    public boolean addLast(E e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (e == null) {
            return false;
        }

        CircularNodeList<E> newNode = new CircularNodeList<>(e);

        if (last == null) {
            last = newNode;
            newNode.setNext(newNode);
        } else {
            newNode.setNext(last.getNext());
            last.setNext(newNode);
            last = newNode;
        }
        return true;
    }

    @Override
    public E removeFirst() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (last == null) {
            return null;
        }

        CircularNodeList<E> first = last.getNext();
        E removed = first.getContent();

        if (first == last) {
            last = null;
        } else {
            last.setNext(first.getNext());
        }

        return removed;
    }

    @Override
    public E removeLast() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (last == null) {
            return null;
        }

        E removed = last.getContent();

        if (last.getNext() == last) {
            last = null;
        } else {
            CircularNodeList<E> current = last.getNext();
            while (current.getNext() != last) {
                current = current.getNext();
            }
            current.setNext(last.getNext());
            last = current;
        }

        return removed;
    }

    @Override
    public int size() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (last == null) {
            return 0;
        }

        int size = 1;
        CircularNodeList<E> current = last.getNext();

        while (current != last) {
            size++;
            current = current.getNext();
        }

        return size;
    }

    @Override
    public boolean isEmpty() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return last == null;
    }

    @Override
    public void clear() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        last = null;
    }

    @Override
    public void add(int index, E element) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (index < 0 || index > size() || element == null) {
            return;
        }

        if (index == 0) {
            addFirst(element);
        } else if (index == size()) {
            addLast(element);
        } else {
            CircularNodeList<E> newNode = new CircularNodeList<>(element);
            CircularNodeList<E> current = last.getNext();

            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }

            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
    }

    @Override
    public E remove(int index) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (index < 0 || index >= size()) {
            return null;
        }

        if (index == 0) {
            return removeFirst();
        }
        if (index == size() - 1) {
            return removeLast();
        }

        CircularNodeList<E> current = last.getNext();
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }

        CircularNodeList<E> toRemove = current.getNext();
        E removed = toRemove.getContent();
        current.setNext(toRemove.getNext());

        return removed;
    }

    @Override
    public E get(int index) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (index < 0 || index >= size()) {
            return null;
        }

        CircularNodeList<E> current = last.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current.getContent();
    }

    @Override
    public E set(int index, E element) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (index < 0 || index >= size() || element == null) {
            return null;
        }

        CircularNodeList<E> current = last.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        E oldValue = current.getContent();
        current.setContent(element);
        return oldValue;

    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        CircularNodeList<E> current = last.getNext();

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
            private CircularNodeList<E> current = (last != null) ? last.getNext() : null;
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
    private CircularNodeList<E> getNodeAt(int index) {
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
        
        if (index <= size / 2) {
            CircularNodeList<E> current = last.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current;
        } else {
            CircularNodeList<E> current = last;
            for (int i = size - 1; i > index; i--) {
                current = getPreviousNode(current);
            }
            return current;
        }
    }
     
     private CircularNodeList<E> getPreviousNode(CircularNodeList<E> node) {
        if (last == null || node == null) return null;
        
        if (node == last.getNext()) return last;
        
        CircularNodeList<E> current = last.getNext();
        while (current != last) { // Mientras no lleguemos al último
            if (current.getNext() == node) {
                return current;
            }
            current = current.getNext();
        }
        
        if (last.getNext() == node) {
            return last;
        }
        
        return null; 
    }
     

    public void sort(Comparator<E> comparator) {
    if (comparator == null) {
        throw new IllegalArgumentException("El comparador no puede ser null");
    }
    
    if (last == null || last.getNext() == last) {
        return;
    }
    
    int n = size();
    
    // Insertion Sort tradicional adaptado
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

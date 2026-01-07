/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Paúl Rodríguez
 */

public class CircularDoublyLinkedList<E> implements List<E>{
    private DoublyNodeList<E> last;
    private DoublyNodeList<E> header = last.getNext();
	
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
        } else {
            DoublyNodeList<E> first = last.getNext();
            newNode.setNext(first);
            newNode.setPrev(last);
            last.setNext(newNode);
            first.setPrev(newNode);
            last = newNode;
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
        return oldContent;
    }
	
}

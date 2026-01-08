/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Paúl Rodríguez
 */

public class ArrayList<E> implements List<E>{
    private E[] elements = null; //arreglo de genéricos
    private int capacity = 100;
    private int effectiveSize;
	
    public ArrayList(){
	elements = (E[])(new Object[capacity]); // Funciona con Casting
	effectiveSize = 0;
    }
	
    private boolean isFull(){
	return effectiveSize == capacity;
    }

    @Override
    public boolean addFirst(E e){
	if (e == null){
            return false;
	} else if (isEmpty()){
            elements[effectiveSize++] = e;
            return true;
	} else if (isFull()){
            addCapacity();
	}
        for(int i = effectiveSize - 1; i>= 0; i--){
            elements[i + 1] = elements[i];
	}
	elements[0] = e;
	effectiveSize++;
	return true;
    }
	
    @Override
    public boolean addLast(E e){
	if (e == null){
            return false;
        } 
        if (isFull()){
            addCapacity();
	}
	elements[effectiveSize++] = e;
	return true;
    }
    
    @Override
    public E removeFirst(){
        if (isEmpty()) {
            return null;
        }
        E removed = elements[0];
        for (int i = 0; i < effectiveSize - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removed;
    }
    
    @Override
    public E removeLast(){
        if (isEmpty()) {
            return null;
        }
        E removed = elements[effectiveSize - 1];
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removed;
    }

    @Override
    public int size(){
	return effectiveSize;
    }

    @Override
    public boolean isEmpty(){
	return effectiveSize == 0;
    }

    @Override
    public void clear(){
        for (int i = 0; i < effectiveSize; i++) {
            elements[i] = null;
        }
        effectiveSize = 0;
    }
    
    @Override
    public void add(int index, E element){
        if (index < 0 || index > effectiveSize || element == null){
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        if (isFull()){
            addCapacity();
        }
        for(int i = effectiveSize; i > index; i--){
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        effectiveSize++;
    }
    
    @Override
    public E remove(int index){
        if (index < 0 || index >= effectiveSize) {
            return null;
        }
        E removido = elements[index];
        for (int i = index; i < effectiveSize - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removido;
    }
    
    @Override
    public E get(int index){
        if (index < 0 || index >= effectiveSize) {
            return null;
        }
        return elements[index];
    }
	
    @Override
    public E set(int index, E element){
        if (index < 0 || index >= effectiveSize || element == null) {
            return null;
        }
        E viejo = elements[index];
        elements[index] = element;
        return viejo;
    }
	
    public void addCapacity(){
   	E[] tmp = (E[]) new Object[capacity * 2];
        for (int i = 0; i < capacity; i++){
            tmp[i] = elements[i];
        }
        elements = tmp;
        capacity = capacity * 2;
    }
    
    @Override
    public String toString(){
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder string = new StringBuilder("[");
        for (int i = 0; i < effectiveSize; i++) {
            string.append(elements[i]);
            if (i < effectiveSize - 1) {
                string.append(", ");
            }
        }
        string.append("]");
        return string.toString();
    }
    
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Paúl Rodríguez
 */

public class DoublyNodeList<E>{
	private E content;
	private DoublyNodeList<E> next;
	private DoublyNodeList<E> prev;
	
	public E getContent(){ return content; }
	public DoublyNodeList<E> getNext(){ return next; }
	public DoublyNodeList<E> getPrev(){ return prev; }
	public void setContent(E content){ this.content = content; }
	public void setNext(DoublyNodeList<E> next){ this.next = next; }
	public void setPrev(DoublyNodeList<E> prev){ this.prev = prev; }
}

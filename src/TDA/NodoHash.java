/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;
/*
 *
 * @author Paúl Rodríguez
 *
 */
public class NodoHash<K, V> {
    
    private Entry<K, V> entry;
    private NodoHash<K, V> siguiente;
    
    /**
     * Constructor
     */
    public NodoHash(K key, V value) {
        this.entry = new Entry<>(key, value);
        this.siguiente = null;
    }
    
    /**
     * Constructor con Entry existente
     */
    public NodoHash(Entry<K, V> entry) {
        this.entry = entry;
        this.siguiente = null;
    }
    
    // ============ GETTERS Y SETTERS ============
    
    public Entry<K, V> getEntry() {
        return entry;
    }
    
    public void setEntry(Entry<K, V> entry) {
        this.entry = entry;
    }
    
    public K getKey() {
        return entry.getKey();
    }
    
    public V getValue() {
        return entry.getValue();
    }
    
    public void setValue(V value) {
        entry.setValue(value);
    }
    
    public NodoHash<K, V> getSiguiente() {
        return siguiente;
    }
    
    public void setSiguiente(NodoHash<K, V> siguiente) {
        this.siguiente = siguiente;
    }
    
    @Override
    public String toString() {
        return entry.toString();
    }
}

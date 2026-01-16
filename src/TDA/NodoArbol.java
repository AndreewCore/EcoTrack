package TDA;

/**
 * 
 * @author Paúl Rodríguez
 *
 */
public class NodoArbol<K extends Comparable<K>, V> {
    
    public static final boolean ROJO = true;
    public static final boolean NEGRO = false;
    
    private Entry<K, V> entry;
    private NodoArbol<K, V> izquierdo;
    private NodoArbol<K, V> derecho;
    private boolean color;
    
    /**
     * Constructor
     */
    public NodoArbol(K key, V value, boolean color) {
        this.entry = new Entry<>(key, value);
        this.izquierdo = null;
        this.derecho = null;
        this.color = color;
    }
    
    /**
     * Constructor con Entry existente
     */
    public NodoArbol(Entry<K, V> entry, boolean color) {
        this.entry = entry;
        this.izquierdo = null;
        this.derecho = null;
        this.color = color;
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
    
    public NodoArbol<K, V> getIzquierdo() {
        return izquierdo;
    }
    
    public void setIzquierdo(NodoArbol<K, V> izquierdo) {
        this.izquierdo = izquierdo;
    }
    
    public NodoArbol<K, V> getDerecho() {
        return derecho;
    }
    
    public void setDerecho(NodoArbol<K, V> derecho) {
        this.derecho = derecho;
    }
    
    public boolean getColor() {
        return color;
    }
    
    public void setColor(boolean color) {
        this.color = color;
    }
    
    public boolean esRojo() {
        return color == ROJO;
    }
    
    public boolean esNegro() {
        return color == NEGRO;
    }
    
    @Override
    public String toString() {
        return entry.toString() + " [" + (color == ROJO ? "R" : "N") + "]";
    }
}
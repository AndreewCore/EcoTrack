package TDA;

/**
 * 
 * @author Paúl Rodríguez
 * 
 */
public class TreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    
    private NodoArbol<K, V> raiz;
    private int size;
    
    /**
     * Constructor
     */
    public TreeMap() {
        this.raiz = null;
        this.size = 0;
    }
        
    public NodoArbol<K, V> getRaiz() {
        return raiz;
    }
    
    public int getSize() {
        return size;
    }

    private boolean esRojo(NodoArbol<K, V> nodo) {
        if (nodo == null) {
            return false;
        }
        return nodo.esRojo();
    }
    
    private NodoArbol<K, V> rotarIzquierda(NodoArbol<K, V> h) {
        NodoArbol<K, V> x = h.getDerecho();
        h.setDerecho(x.getIzquierdo());
        x.setIzquierdo(h);
        x.setColor(h.getColor());
        h.setColor(NodoArbol.ROJO);
        return x;
    }
    
    /**
     * Rotación a la derecha
     */
    private NodoArbol<K, V> rotarDerecha(NodoArbol<K, V> h) {
        NodoArbol<K, V> x = h.getIzquierdo();
        h.setIzquierdo(x.getDerecho());
        x.setDerecho(h);
        x.setColor(h.getColor());
        h.setColor(NodoArbol.ROJO);
        return x;
    }

    private void invertirColores(NodoArbol<K, V> h) {
        h.setColor(!h.getColor());
        h.getIzquierdo().setColor(!h.getIzquierdo().getColor());
        h.getDerecho().setColor(!h.getDerecho().getColor());
    }
    
    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("La clave no puede ser null en TreeMap");
        }
        
        V[] valorAnterior = (V[]) new Object[1];
        raiz = put(raiz, key, value, valorAnterior);
        raiz.setColor(NodoArbol.NEGRO);
        return valorAnterior[0];
    }

    private NodoArbol<K, V> put(NodoArbol<K, V> h, K key, V value, V[] valorAnterior) {
        if (h == null) {
            size++;
            return new NodoArbol<>(key, value, NodoArbol.ROJO);
        }
        
        int cmp = key.compareTo(h.getKey());
        
        if (cmp < 0) {
            h.setIzquierdo(put(h.getIzquierdo(), key, value, valorAnterior));
        } else if (cmp > 0) {
            h.setDerecho(put(h.getDerecho(), key, value, valorAnterior));
        } else {
            valorAnterior[0] = h.getValue();
            h.setValue(value);
        }
        
        // Balancear
        if (esRojo(h.getDerecho()) && !esRojo(h.getIzquierdo())) {
            h = rotarIzquierda(h);
        }
        if (esRojo(h.getIzquierdo()) && h.getIzquierdo() != null && esRojo(h.getIzquierdo().getIzquierdo())) {
            h = rotarDerecha(h);
        }
        if (esRojo(h.getIzquierdo()) && esRojo(h.getDerecho())) {
            invertirColores(h);
        }
        
        return h;
    }
    
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        return get(raiz, key);
    }

    private V get(NodoArbol<K, V> nodo, K key) {
        if (nodo == null) {
            return null;
        }
        
        int cmp = key.compareTo(nodo.getKey());
        
        if (cmp < 0) {
            return get(nodo.getIzquierdo(), key);
        } else if (cmp > 0) {
            return get(nodo.getDerecho(), key);
        } else {
            return nodo.getValue();
        }
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public V remove(K key) {
        if (key == null || !containsKey(key)) {
            return null;
        }
        
        V[] valorAnterior = (V[]) new Object[1];
        
        if (!esRojo(raiz.getIzquierdo()) && !esRojo(raiz.getDerecho())) {
            raiz.setColor(NodoArbol.ROJO);
        }
        
        raiz = remove(raiz, key, valorAnterior);
        
        if (raiz != null) {
            raiz.setColor(NodoArbol.NEGRO);
        }
        
        return valorAnterior[0];
    }
 
    private NodoArbol<K, V> remove(NodoArbol<K, V> h, K key, V[] valorAnterior) {
        if (key.compareTo(h.getKey()) < 0) {
            if (!esRojo(h.getIzquierdo()) && h.getIzquierdo() != null && 
                !esRojo(h.getIzquierdo().getIzquierdo())) {
                h = moverRojoIzquierda(h);
            }
            h.setIzquierdo(remove(h.getIzquierdo(), key, valorAnterior));
        } else {
            if (esRojo(h.getIzquierdo())) {
                h = rotarDerecha(h);
            }
            
            if (key.compareTo(h.getKey()) == 0 && h.getDerecho() == null) {
                valorAnterior[0] = h.getValue();
                size--;
                return null;
            }
            
            if (h.getDerecho() != null && !esRojo(h.getDerecho()) && 
                !esRojo(h.getDerecho().getIzquierdo())) {
                h = moverRojoDerecha(h);
            }
            
            if (key.compareTo(h.getKey()) == 0) {
                valorAnterior[0] = h.getValue();
                NodoArbol<K, V> minimo = minimo(h.getDerecho());
                h.setEntry(minimo.getEntry());
                h.setDerecho(eliminarMinimo(h.getDerecho()));
                size--;
            } else {
                h.setDerecho(remove(h.getDerecho(), key, valorAnterior));
            }
        }
        
        return balancear(h);
    }

    private NodoArbol<K, V> minimo(NodoArbol<K, V> nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return minimo(nodo.getIzquierdo());
    }
 
    private NodoArbol<K, V> eliminarMinimo(NodoArbol<K, V> h) {
        if (h.getIzquierdo() == null) {
            return null;
        }
        
        if (!esRojo(h.getIzquierdo()) && !esRojo(h.getIzquierdo().getIzquierdo())) {
            h = moverRojoIzquierda(h);
        }
        
        h.setIzquierdo(eliminarMinimo(h.getIzquierdo()));
        return balancear(h);
    }

    private NodoArbol<K, V> moverRojoIzquierda(NodoArbol<K, V> h) {
        invertirColores(h);
        if (h.getDerecho() != null && esRojo(h.getDerecho().getIzquierdo())) {
            h.setDerecho(rotarDerecha(h.getDerecho()));
            h = rotarIzquierda(h);
            invertirColores(h);
        }
        return h;
    }

    private NodoArbol<K, V> moverRojoDerecha(NodoArbol<K, V> h) {
        invertirColores(h);
        if (h.getIzquierdo() != null && esRojo(h.getIzquierdo().getIzquierdo())) {
            h = rotarDerecha(h);
            invertirColores(h);
        }
        return h;
    }
    
    private NodoArbol<K, V> balancear(NodoArbol<K, V> h) {
        if (esRojo(h.getDerecho())) {
            h = rotarIzquierda(h);
        }
        if (esRojo(h.getIzquierdo()) && h.getIzquierdo() != null && 
            esRojo(h.getIzquierdo().getIzquierdo())) {
            h = rotarDerecha(h);
        }
        if (esRojo(h.getIzquierdo()) && esRojo(h.getDerecho())) {
            invertirColores(h);
        }
        return h;
    }
    
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    @Override
    public boolean containsValue(V value) {
        return containsValue(raiz, value);
    }

    private boolean containsValue(NodoArbol<K, V> nodo, V value) {
        if (nodo == null) {
            return false;
        }
        
        if ((value == null && nodo.getValue() == null) ||
            (value != null && value.equals(nodo.getValue()))) {
            return true;
        }
        
        return containsValue(nodo.getIzquierdo(), value) || 
               containsValue(nodo.getDerecho(), value);
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public void clear() {
        raiz = null;
        size = 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public K[] keySet() {
        K[] keys = (K[]) new Comparable[size];
        inorden(raiz, keys, new int[]{0});
        return keys;
    }

    private void inorden(NodoArbol<K, V> nodo, K[] array, int[] index) {
        if (nodo == null) {
            return;
        }
        
        inorden(nodo.getIzquierdo(), array, index);
        array[index[0]++] = nodo.getKey();
        inorden(nodo.getDerecho(), array, index);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V[] values() {
        V[] vals = (V[]) new Object[size];
        inordenValores(raiz, vals, new int[]{0});
        return vals;
    }

    private void inordenValores(NodoArbol<K, V> nodo, V[] array, int[] index) {
        if (nodo == null) {
            return;
        }
        
        inordenValores(nodo.getIzquierdo(), array, index);
        array[index[0]++] = nodo.getValue();
        inordenValores(nodo.getDerecho(), array, index);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Entry<K, V>[] entrySet() {
        Entry<K, V>[] entries = new Entry[size];
        inordenEntries(raiz, entries, new int[]{0});
        return entries;
    }

    private void inordenEntries(NodoArbol<K, V> nodo, Entry<K, V>[] array, int[] index) {
        if (nodo == null) {
            return;
        }
        
        inordenEntries(nodo.getIzquierdo(), array, index);
        array[index[0]++] = nodo.getEntry();
        inordenEntries(nodo.getDerecho(), array, index);
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        
        StringBuilder sb = new StringBuilder("{");
        toString(raiz, sb);
        sb.setLength(sb.length() - 2); // Quitar última coma
        sb.append("}");
        return sb.toString();
    }
    
    private void toString(NodoArbol<K, V> nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        
        toString(nodo.getIzquierdo(), sb);
        sb.append(nodo.getEntry().toString()).append(", ");
        toString(nodo.getDerecho(), sb);
    }
}
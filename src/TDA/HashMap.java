package TDA;

/**
 * 
 * @author Paúl Rodríguez
 *
 */
public class HashMap<K, V> implements Map<K, V> {
    
    private static final int CAPACIDAD_INICIAL = 16;
    private static final double FACTOR_CARGA = 0.75;
    
    private NodoHash<K, V>[] tabla;
    private int size;
    private int capacidad;

    @SuppressWarnings("unchecked")
    public HashMap() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.tabla = new NodoHash[capacidad];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public HashMap(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.tabla = new NodoHash[capacidad];
        this.size = 0;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getCapacidad() {
        return capacidad;
    }
    
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h % capacidad);
    }
    
    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putNull(value);
        }
        
        // Verificar si necesitamos redimensionar
        if (size >= capacidad * FACTOR_CARGA) {
            redimensionar();
        }
        
        int indice = hash(key);
        NodoHash<K, V> nodo = tabla[indice];
        
        // Caso 1: Bucket vacío
        if (nodo == null) {
            tabla[indice] = new NodoHash<>(key, value);
            size++;
            return null;
        }
        
        // Caso 2: Buscar en la lista enlazada
        NodoHash<K, V> anterior = null;
        while (nodo != null) {
            if (nodo.getKey().equals(key)) {
                // Clave existe, actualizar valor
                V valorAnterior = nodo.getValue();
                nodo.setValue(value);
                return valorAnterior;
            }
            anterior = nodo;
            nodo = nodo.getSiguiente();
        }
        
        // Caso 3: Agregar al final de la lista
        NodoHash<K, V> nuevoNodo = new NodoHash<>(key, value);
        anterior.setSiguiente(nuevoNodo);
        size++;
        return null;
    }

    private V putNull(V value) {
        NodoHash<K, V> nodo = tabla[0];
        
        if (nodo == null) {
            tabla[0] = new NodoHash<>(null, value);
            size++;
            return null;
        }
        
        while (nodo != null) {
            if (nodo.getKey() == null) {
                V valorAnterior = nodo.getValue();
                nodo.setValue(value);
                return valorAnterior;
            }
            if (nodo.getSiguiente() == null) {
                NodoHash<K, V> nuevoNodo = new NodoHash<>(null, value);
                nodo.setSiguiente(nuevoNodo);
                size++;
                return null;
            }
            nodo = nodo.getSiguiente();
        }
        
        return null;
    }
    
    @Override
    public V get(K key) {
        int indice = hash(key);
        NodoHash<K, V> nodo = tabla[indice];
        
        while (nodo != null) {
            if ((key == null && nodo.getKey() == null) || 
                (key != null && key.equals(nodo.getKey()))) {
                return nodo.getValue();
            }
            nodo = nodo.getSiguiente();
        }
        
        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public V remove(K key) {
        int indice = hash(key);
        NodoHash<K, V> nodo = tabla[indice];
        NodoHash<K, V> anterior = null;
        
        while (nodo != null) {
            if ((key == null && nodo.getKey() == null) || 
                (key != null && key.equals(nodo.getKey()))) {
                
                // Encontrado, eliminar
                if (anterior == null) {
                    tabla[indice] = nodo.getSiguiente();
                } else {
                    anterior.setSiguiente(nodo.getSiguiente());
                }
                size--;
                return nodo.getValue();
            }
            anterior = nodo;
            nodo = nodo.getSiguiente();
        }
        
        return null;
    }
    
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    @Override
    public boolean containsValue(V value) {
        for (NodoHash<K, V> bucket : tabla) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                if ((value == null && nodo.getValue() == null) ||
                    (value != null && value.equals(nodo.getValue()))) {
                    return true;
                }
                nodo = nodo.getSiguiente();
            }
        }
        return false;
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
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = null;
        }
        size = 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public K[] keySet() {
        K[] keys = (K[]) new Object[size];
        int index = 0;
        
        for (NodoHash<K, V> bucket : tabla) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                keys[index++] = nodo.getKey();
                nodo = nodo.getSiguiente();
            }
        }
        
        return keys;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V[] values() {
        V[] vals = (V[]) new Object[size];
        int index = 0;
        
        for (NodoHash<K, V> bucket : tabla) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                vals[index++] = nodo.getValue();
                nodo = nodo.getSiguiente();
            }
        }
        
        return vals;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Entry<K, V>[] entrySet() {
        Entry<K, V>[] entries = new Entry[size];
        int index = 0;
        
        for (NodoHash<K, V> bucket : tabla) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                entries[index++] = nodo.getEntry();
                nodo = nodo.getSiguiente();
            }
        }
        
        return entries;
    }

    @SuppressWarnings("unchecked")
    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        NodoHash<K, V>[] tablaVieja = tabla;
        
        tabla = new NodoHash[nuevaCapacidad];
        capacidad = nuevaCapacidad;
        size = 0;
        
        // Reinsertar todos los elementos
        for (NodoHash<K, V> bucket : tablaVieja) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                put(nodo.getKey(), nodo.getValue());
                nodo = nodo.getSiguiente();
            }
        }
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        
        StringBuilder sb = new StringBuilder("{");
        boolean primero = true;
        
        for (NodoHash<K, V> bucket : tabla) {
            NodoHash<K, V> nodo = bucket;
            while (nodo != null) {
                if (!primero) {
                    sb.append(", ");
                }
                sb.append(nodo.toString());
                primero = false;
                nodo = nodo.getSiguiente();
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
}
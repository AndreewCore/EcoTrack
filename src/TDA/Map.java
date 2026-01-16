package TDA;

/**
 * 
 * @author Paúl Rodríguez
 */
public interface Map<K, V> {

    V put(K key, V value);

    V get(K key);
    
    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    int size();

    boolean isEmpty();

    void clear();
 
    K[] keySet();

    V[] values();

    Entry<K, V>[] entrySet();
}
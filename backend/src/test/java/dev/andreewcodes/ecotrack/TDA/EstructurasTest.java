package dev.andreewcodes.ecotrack.TDA;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class EstructurasTest {

    /** Vuelca un Iterable a una lista de java.util para comparar fácilmente. */
    private static <E> List<E> aLista(Iterable<E> it) {
        List<E> l = new ArrayList<>();
        it.forEach(l::add);
        return l;
    }

    @Test
    void pilaEsLifoYSeRecorreDelTopeAlFondo() {
        Pila<Integer> pila = new Pila<>();
        pila.apilar(1);
        pila.apilar(2);
        pila.apilar(3);
        assertThat(aLista(pila)).containsExactly(3, 2, 1);
        assertThat(pila.desapilar()).isEqualTo(3);
        assertThat(pila.verTope()).isEqualTo(2);
        assertThat(pila.getTamaño()).isEqualTo(2);
    }

    @Test
    void colaEsFifo() {
        Cola<String> cola = new Cola<>();
        cola.encolar("a");
        cola.encolar("b");
        assertThat(aLista(cola)).containsExactly("a", "b");
        assertThat(cola.desencolar()).isEqualTo("a");
    }

    @Test
    void colaPrioridadSacaPrimeroElMayorYPuedeReordenarse() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<>(Comparator.<Integer>naturalOrder());
        cola.encolar(5);
        cola.encolar(9);
        cola.encolar(1);
        assertThat(aLista(cola)).containsExactly(9, 5, 1);

        cola.cambiarComparador(Comparator.<Integer>reverseOrder());
        assertThat(cola.desencolar()).isEqualTo(1);
        assertThat(cola.remover(9)).isTrue();
        assertThat(aLista(cola)).containsExactly(5);
    }

    @Test
    void listaCircularOrdenaYSeRecorreEnAmbosSentidos() {
        CircularDoublyLinkedList<Integer> lista = new CircularDoublyLinkedList<>();
        for (int n : new int[] {4, 1, 3, 2}) {
            lista.addLast(n);
        }
        lista.sort(Comparator.naturalOrder());
        assertThat(aLista(lista)).containsExactly(1, 2, 3, 4);

        IteradorBidireccional<Integer> it = lista.iteradorBidireccional();
        assertThat(it.hasPrevious()).isFalse();
        assertThat(it.next()).isEqualTo(1);
        assertThat(it.next()).isEqualTo(2);
        assertThat(it.previous()).isEqualTo(2);
        assertThat(it.previous()).isEqualTo(1);
        while (it.hasNext()) {
            it.next();
        }
        // No da la vuelta aunque la lista sea circular.
        assertThatThrownBy(it::next).isInstanceOf(NoSuchElementException.class);
        assertThat(it.previous()).isEqualTo(4);
    }

    @Test
    void iteradorBidireccionalEnListaVacia() {
        IteradorBidireccional<Integer> it = new CircularDoublyLinkedList<Integer>().iteradorBidireccional();
        assertThat(it.hasNext()).isFalse();
        assertThat(it.hasPrevious()).isFalse();
    }

    @Test
    void mapasDevuelvenEntradasOrdenadasEnElTreeMap() {
        TreeMap<String, Integer> tree = new TreeMap<>();
        tree.put("b", 2);
        tree.put("a", 1);
        tree.put("c", 3);
        Entry<String, Integer>[] entradas = tree.entrySet();
        assertThat(entradas).extracting(Entry::getKey).containsExactly("a", "b", "c");

        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("x", 1);
        hash.put("x", 5);
        assertThat(hash.get("x")).isEqualTo(5);
        assertThat(hash.getOrDefault("y", 0)).isZero();
    }
}

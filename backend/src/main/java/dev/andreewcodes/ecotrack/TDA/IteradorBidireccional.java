package dev.andreewcodes.ecotrack.TDA;

import java.util.Iterator;

/**
 * Iterador que puede avanzar y retroceder elemento a elemento.
 *
 * @param <E> tipo de los elementos recorridos
 */
public interface IteradorBidireccional<E> extends Iterator<E> {

    /** Si queda algún elemento antes de la posición actual. */
    boolean hasPrevious();

    /** Retrocede una posición y devuelve ese elemento. */
    E previous();
}

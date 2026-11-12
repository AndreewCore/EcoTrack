package dev.andreewcodes.ecotrack.Utilitaria;

import dev.andreewcodes.ecotrack.dominio.Residuo;
import java.util.Comparator;

/**
 *
 * @author Paúl Rodríguez
 */
public class ResiduoComparators {

    /** Orden de registro (id ascendente). */
    public static Comparator<Residuo> porId() {
        return Comparator.comparingInt(Residuo::getId);
    }


    public static Comparator<Residuo> porPeso() {
        return (o1, o2) -> {
            if (o1 == null && o2 == null) {
                throw new IllegalArgumentException("Ambos residuos no pueden ser null");
            }
            if (o1 == null) {
                throw new IllegalArgumentException("Primer residuo no puede ser null");
            }
            if (o2 == null) {
                throw new IllegalArgumentException("Segundo residuo no puede ser null");
            }
            return Double.compare(o1.getPeso(), o2.getPeso());
        };
    }

    public static Comparator<Residuo> porTipo() {
        return (o1, o2) -> {
            if (o1 == null && o2 == null) {
                throw new IllegalArgumentException("Ambos residuos no pueden ser null");

            }
            if (o1 == null) {
                throw new IllegalArgumentException("Primer residuo no puede ser null");
            }
            if (o2 == null) {
                throw new IllegalArgumentException("Segundo residuo no puede ser null");
            }

            return o1.getTipo().compareTo(o2.getTipo());
        };
    }

    public static Comparator<Residuo> porPrioridad() {
        return (o1, o2) -> {
            if (o1 == null && o2 == null) {
                throw new IllegalArgumentException("Ambos residuos no pueden ser null");
            }

            if (o1 == null) {
                throw new IllegalArgumentException("Primer residuo no puede ser null");
            }
            if (o2 == null) {
                throw new IllegalArgumentException("Segundo residuo no puede ser null");
            }

            return Integer.compare(o1.getPrioridad(), o2.getPrioridad());
        };
    }
}

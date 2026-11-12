package dev.andreewcodes.ecotrack.dominio;

import dev.andreewcodes.ecotrack.TDA.CircularDoublyLinkedList;
import dev.andreewcodes.ecotrack.Utilitaria.ResiduoComparators;
import java.util.function.Predicate;

/**
 * Lista circular doblemente enlazada de residuos con operaciones de filtrado,
 * ordenamiento y búsqueda.
 *
 * @author Dominic Izurrieta &amp; Paúl Rodríguez
 */
public class ListaResiduos extends CircularDoublyLinkedList<Residuo> {

    // Lista global solo para la app de escritorio; la API usa la de EcoTrackData.
    private static ListaResiduos listaResiduosGlobal;

    /** Lista global que comparten los controladores JavaFX. */
    public static ListaResiduos getListaResiduosGlobal() {
        if (listaResiduosGlobal == null) {
            listaResiduosGlobal = new ListaResiduos();
        }
        return listaResiduosGlobal;
    }

    // ---------- Ordenamiento (in situ) ----------

    public void ordenarPorPeso() {
        sort(ResiduoComparators.porPeso());
    }

    public void ordenarPorPesoDescendente() {
        sort(ResiduoComparators.porPeso().reversed());
    }

    public void ordenarPorPrioridad() {
        sort(ResiduoComparators.porPrioridad());
    }

    public void ordenarPorPrioridadDescendente() {
        sort(ResiduoComparators.porPrioridad().reversed());
    }

    public void ordenarPorTipo() {
        sort(ResiduoComparators.porTipo());
    }

    public void ordenarPorTipoDescendente() {
        sort(ResiduoComparators.porTipo().reversed());
    }

    // ---------- Filtrado (devuelven una lista nueva) ----------

    /** Copia con los residuos que cumplen la condición, en el mismo orden. */
    public ListaResiduos filtrar(Predicate<Residuo> condicion) {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (condicion.test(residuo)) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }

    public ListaResiduos filtrarPorTipo(TipoResiduo tipo) {
        return filtrar(r -> r.getTipo() == tipo);
    }

    public ListaResiduos filtrarPorZona(ZonaGuayaquil zona) {
        return filtrar(r -> r.getZona() == zona);
    }

    public ListaResiduos filtrarPorEstado(EstadoResiduo estado) {
        return filtrar(r -> r.getEstado() == estado);
    }

    public ListaResiduos filtrarPorPrioridadRango(int min, int max) {
        return filtrar(r -> r.getPrioridad() >= min && r.getPrioridad() <= max);
    }

    public ListaResiduos filtrarPorPesoMayorA(double pesoMinimo) {
        return filtrar(r -> r.getPeso() > pesoMinimo);
    }

    // ---------- Estadísticas ----------

    public double calcularPesoTotal() {
        double total = 0.0;
        for (Residuo residuo : this) {
            total += residuo.getPeso();
        }
        return total;
    }

    public double calcularPesoPromedio() {
        return isEmpty() ? 0.0 : calcularPesoTotal() / size();
    }

    // ---------- Búsquedas ----------

    /** Residuo con ese id, o null si no existe. */
    public Residuo buscarPorId(int id) {
        for (Residuo residuo : this) {
            if (residuo.getId() == id) {
                return residuo;
            }
        }
        return null;
    }

    /** Posición del residuo con ese id, o -1 si no está. */
    public int indiceDe(int id) {
        int i = 0;
        for (Residuo residuo : this) {
            if (residuo.getId() == id) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /** Mayor id presente, para continuar la numeración tras cargar datos. */
    public int maximoId() {
        int max = 0;
        for (Residuo residuo : this) {
            max = Math.max(max, residuo.getId());
        }
        return max;
    }
}

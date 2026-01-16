/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import TDA.CircularDoublyLinkedList;
import Utilitaria.ResiduoComparators;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author Dominic Izurrieta & Paúl Rodríguez
 */
public class ListaResiduos extends CircularDoublyLinkedList<Residuo> {

    private static ListaResiduos listaResiduosGlobal;

    private ListaResiduos() {
        super();
    }

    public static ListaResiduos getListaResiduosGlobal() {
        if (listaResiduosGlobal == null) {
            listaResiduosGlobal = new ListaResiduos();
        }
        return listaResiduosGlobal;
    }

    // MÉTODOS DE ORDENAMIENTO
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

    // MÉTODOS DE FILTRADO
    public ListaResiduos filtrarPorTipo(TipoResiduo tipo) {
        ListaResiduos resultado = new ListaResiduos();

        for (Residuo residuo : this) {
            if (residuo.getTipo() == tipo) {
                resultado.addLast(residuo);
            }
        }

        return resultado;
    }

    public ListaResiduos filtrarPorPrioridadMinima(int prioridadMinima) {
        ListaResiduos resultado = new ListaResiduos();

        for (Residuo residuo : this) {
            if (residuo.getPrioridad() >= prioridadMinima) {
                resultado.addLast(residuo);
            }
        }

        return resultado;
    }

    public ListaResiduos filtrarPorPesoMayorA(double pesoMinimo) {
        ListaResiduos resultado = new ListaResiduos();

        for (Residuo residuo : this) {
            if (residuo.getPeso() > pesoMinimo) {
                resultado.addLast(residuo);
            }
        }

        return resultado;
    }

    // MÉTODOS ESTADÍSTICOS
    public double calcularPesoTotal() {
        double total = 0.0;

        for (Residuo residuo : this) {
            total += residuo.getPeso();
        }

        return total;
    }

    public double calcularPesoPromedio() {
        if (isEmpty()) {
            return 0.0;
        }
        return calcularPesoTotal() / size();
    }

    public Residuo obtenerResiduoMasPesado() {
        if (isEmpty()) {
            return null;
        }

        Residuo masPesado = get(0);

        for (Residuo residuo : this) {
            if (residuo.getPeso() > masPesado.getPeso()) {
                masPesado = residuo;
            }
        }

        return masPesado;
    }

    public Residuo obtenerResiduoMasPrioritario() {
        if (isEmpty()) {
            return null;
        }

        Residuo masPrioritario = get(0);

        for (Residuo residuo : this) {
            if (residuo.getPrioridad() > masPrioritario.getPrioridad()) {
                masPrioritario = residuo;
            }
        }

        return masPrioritario;
    }

    // BÚSQUEDAS
    public Residuo buscarPorId(int id) {
        for (Residuo residuo : this) {
            if (residuo.getId() == id) {
                return residuo;
            }
        }
        return null;
    }

    public boolean contieneResiduoConId(int id) {
        return buscarPorId(id) != null;
    }

    // MÉTODO PARA DIAGNÓSTICO
    @Override
    public String toString() {
        if (isEmpty()) {
            return "ListaResiduos[vacía]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ListaResiduos[\n");

        int contador = 1;
        for (Residuo residuo : this) {
            sb.append(String.format("  %d. %s (%.2f kg) - Prioridad: %d - Tipo: %s\n",
                    contador++,
                    residuo.getNombre(),
                    residuo.getPeso(),
                    residuo.getPrioridad(),
                    residuo.getTipo()
            ));
        }

        sb.append("]");
        return sb.toString();
    }

    public ListaResiduos filtrarPorPrioridadMaxima(int prioridadMaxima) {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (residuo.getPrioridad() <= prioridadMaxima) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }

    public ListaResiduos filtrarPorPrioridadRango(int min, int max) {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (residuo.getPrioridad() >= min && residuo.getPrioridad() <= max) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }

    public ListaResiduos filtrarPorNoProcesados() {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (residuo.getFechaRecoleccion() == null) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }

    public ListaResiduos filtrarPorProcesados() {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (residuo.getFechaRecoleccion() != null) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }

    // Método para filtrar por zona
    public ListaResiduos filtrarPorZona(ZonaGuayaquil zona) {
        ListaResiduos resultado = new ListaResiduos();
        for (Residuo residuo : this) {
            if (residuo.getZona().getZonaGuayaquil() == zona) {
                resultado.addLast(residuo);
            }
        }
        return resultado;
    }
}

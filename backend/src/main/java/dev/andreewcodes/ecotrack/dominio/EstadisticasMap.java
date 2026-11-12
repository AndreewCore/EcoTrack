package dev.andreewcodes.ecotrack.dominio;

import dev.andreewcodes.ecotrack.TDA.Entry;
import dev.andreewcodes.ecotrack.TDA.HashMap;
import dev.andreewcodes.ecotrack.TDA.TreeMap;

/**
 * Estadísticas globales agregadas en mapas propios: por tipo de residuo (HashMap)
 * y por zona (TreeMap, ordenado por el enum).
 *
 * Se recorre siempre con {@code entrySet()}: {@code values()} y {@code keySet()} de
 * los mapas TDA devuelven arreglos de Object/Comparable que no se pueden asignar a
 * {@code Double[]} o {@code ZonaGuayaquil[]} sin lanzar ClassCastException.
 *
 * @author Paúl Rodríguez
 */
public class EstadisticasMap {

    private final ListaResiduos residuos;
    private final ListaZonas zonas;

    private final HashMap<TipoResiduo, Double> pesoTotalPorTipo = new HashMap<>();
    private final HashMap<TipoResiduo, Integer> cantidadPorTipo = new HashMap<>();
    private final TreeMap<ZonaGuayaquil, Double> pesoPendientePorZona = new TreeMap<>();
    private final TreeMap<ZonaGuayaquil, Double> pesoRecolectadoPorZona = new TreeMap<>();
    private final TreeMap<ZonaGuayaquil, Double> utilidadPorZona = new TreeMap<>();

    public EstadisticasMap(ListaResiduos residuos, ListaZonas zonas) {
        this.residuos = residuos;
        this.zonas = zonas;
    }

    /** Recalcula todos los mapas a partir del estado actual. */
    public void calcularEstadisticas() {
        pesoTotalPorTipo.clear();
        cantidadPorTipo.clear();
        for (TipoResiduo tipo : TipoResiduo.values()) {
            pesoTotalPorTipo.put(tipo, 0.0);
            cantidadPorTipo.put(tipo, 0);
        }
        for (Residuo r : residuos) {
            pesoTotalPorTipo.put(r.getTipo(), pesoTotalPorTipo.get(r.getTipo()) + r.getPeso());
            cantidadPorTipo.put(r.getTipo(), cantidadPorTipo.get(r.getTipo()) + 1);
        }

        pesoPendientePorZona.clear();
        pesoRecolectadoPorZona.clear();
        utilidadPorZona.clear();
        for (Zona zona : zonas) {
            pesoPendientePorZona.put(zona.getZonaGuayaquil(), zona.getPesoPendiente());
            pesoRecolectadoPorZona.put(zona.getZonaGuayaquil(), zona.getPesoRecolectado());
            utilidadPorZona.put(zona.getZonaGuayaquil(), zona.calcularUtilidadAmbiental());
        }
    }

    public HashMap<TipoResiduo, Double> getPesoTotalPorTipo() {
        return pesoTotalPorTipo;
    }

    public HashMap<TipoResiduo, Integer> getCantidadPorTipo() {
        return cantidadPorTipo;
    }

    public TreeMap<ZonaGuayaquil, Double> getPesoPendientePorZona() {
        return pesoPendientePorZona;
    }

    public TreeMap<ZonaGuayaquil, Double> getPesoRecolectadoPorZona() {
        return pesoRecolectadoPorZona;
    }

    public TreeMap<ZonaGuayaquil, Double> getUtilidadPorZona() {
        return utilidadPorZona;
    }

    public int getTotalResiduos() {
        return residuos.size();
    }

    public double getPesoTotal() {
        return residuos.calcularPesoTotal();
    }

    public double getPesoPromedio() {
        return residuos.calcularPesoPromedio();
    }

    /** Tipo con más residuos registrados, o null si no hay ninguno. */
    public TipoResiduo getTipoMasComun() {
        TipoResiduo masComun = null;
        int max = 0;
        for (Entry<TipoResiduo, Integer> e : cantidadPorTipo.entrySet()) {
            if (e != null && e.getValue() > max) {
                max = e.getValue();
                masComun = e.getKey();
            }
        }
        return masComun;
    }

    /** Zona con más peso pendiente, o null si no hay nada pendiente. */
    public ZonaGuayaquil getZonaMayorPesoPendiente() {
        return extremo(pesoPendientePorZona, true, true);
    }

    /** Zona con la utilidad ambiental más alta. */
    public ZonaGuayaquil getZonaMejorUtilidad() {
        return extremo(utilidadPorZona, true, false);
    }

    /** Zona con la utilidad ambiental más baja (el foco más crítico). */
    public ZonaGuayaquil getZonaPeorUtilidad() {
        return extremo(utilidadPorZona, false, false);
    }

    /**
     * Clave con el valor máximo o mínimo del mapa. Con {@code soloPositivos} ignora
     * los ceros para no señalar una zona cualquiera cuando no hay datos.
     */
    private static ZonaGuayaquil extremo(TreeMap<ZonaGuayaquil, Double> mapa, boolean maximo, boolean soloPositivos) {
        ZonaGuayaquil elegida = null;
        double mejor = maximo ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        for (Entry<ZonaGuayaquil, Double> e : mapa.entrySet()) {
            double v = e.getValue();
            if (soloPositivos && v <= 0) {
                continue;
            }
            if (maximo ? v > mejor : v < mejor) {
                mejor = v;
                elegida = e.getKey();
            }
        }
        return elegida;
    }
}

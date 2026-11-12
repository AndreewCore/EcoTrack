package dev.andreewcodes.ecotrack.Utilitaria;

import dev.andreewcodes.ecotrack.dominio.ListaZonas;
import dev.andreewcodes.ecotrack.dominio.Vehiculo;
import dev.andreewcodes.ecotrack.dominio.Zona;
import java.util.Comparator;

/**
 * Prioridad de despacho de los vehículos según el estado actual de su zona.
 * Devuelve un valor positivo si v1 debe salir antes que v2, que es lo que espera
 * {@link dev.andreewcodes.ecotrack.TDA.ColaPrioridad}.
 *
 * Criterios, en orden: menor utilidad ambiental de la zona (el foco más crítico
 * primero), más peso pendiente, más capacidad y, por último, id para que el orden
 * sea estable.
 */
public class ComparadorVehiculo implements Comparator<Vehiculo> {

    private final ListaZonas zonas;

    public ComparadorVehiculo(ListaZonas zonas) {
        this.zonas = zonas;
    }

    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        Zona z1 = zonas.buscarPorZonaGuayaquil(v1.getZonaAsignada());
        Zona z2 = zonas.buscarPorZonaGuayaquil(v2.getZonaAsignada());
        int porUtilidad = Double.compare(z2.calcularUtilidadAmbiental(), z1.calcularUtilidadAmbiental());
        if (porUtilidad != 0) {
            return porUtilidad;
        }
        int porPendiente = Double.compare(z1.getPesoPendiente(), z2.getPesoPendiente());
        if (porPendiente != 0) {
            return porPendiente;
        }
        int porCapacidad = Double.compare(v1.getCapacidad(), v2.getCapacidad());
        if (porCapacidad != 0) {
            return porCapacidad;
        }
        return v2.getId().compareTo(v1.getId());
    }
}

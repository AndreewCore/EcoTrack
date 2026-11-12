package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.EstadoVehiculo;
import dev.andreewcodes.ecotrack.dominio.Vehiculo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;

/**
 * Vehículo de la flota.
 *
 * @param turno posición en la cola de despacho (1 = sale primero)
 */
public record VehiculoDto(String id, ZonaGuayaquil zonaAsignada, double capacidad, EstadoVehiculo estado, int turno) {

    /** Copia el vehículo con su posición en la cola. */
    public static VehiculoDto de(Vehiculo v, int turno) {
        return new VehiculoDto(v.getId(), v.getZonaAsignada(), v.getCapacidad(), v.getEstado(), turno);
    }
}

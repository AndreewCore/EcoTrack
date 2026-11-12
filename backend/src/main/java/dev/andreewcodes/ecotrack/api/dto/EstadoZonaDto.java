package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.Zona;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;

/** Pesos y utilidad ambiental de una zona. */
public record EstadoZonaDto(ZonaGuayaquil zona, double pesoPendiente, double pesoRecolectado, double utilidad) {

    /** Copia el estado de la zona de dominio. */
    public static EstadoZonaDto de(Zona z) {
        return new EstadoZonaDto(z.getZonaGuayaquil(), z.getPesoPendiente(), z.getPesoRecolectado(),
                z.calcularUtilidadAmbiental());
    }
}

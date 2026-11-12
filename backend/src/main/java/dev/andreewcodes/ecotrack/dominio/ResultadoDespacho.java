package dev.andreewcodes.ecotrack.dominio;

import java.util.List;

/**
 * Resumen de un despacho: qué vehículo salió, de qué zona y qué residuos llevó al
 * centro.
 *
 * @param aviso motivo por el que quedó peso pendiente en la zona, o null
 */
public record ResultadoDespacho(Vehiculo vehiculo, ZonaGuayaquil zona, List<Residuo> residuos, double pesoTotal,
        String aviso) {
}

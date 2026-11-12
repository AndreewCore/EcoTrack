package dev.andreewcodes.ecotrack.api.dto;

import java.util.List;

/**
 * Estado del centro de reciclaje.
 *
 * @param pila residuos en espera, del tope al fondo
 */
public record EstadoCentroDto(int capacidadMaxima, List<ResiduoDto> pila, int procesados) {
}

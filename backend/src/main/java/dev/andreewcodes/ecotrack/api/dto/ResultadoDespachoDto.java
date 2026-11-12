package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import java.util.List;

/** Resultado de despachar un vehículo. */
public record ResultadoDespachoDto(VehiculoDto vehiculo, ZonaGuayaquil zona, List<ResiduoDto> residuos,
        double pesoTotal, String aviso) {
}

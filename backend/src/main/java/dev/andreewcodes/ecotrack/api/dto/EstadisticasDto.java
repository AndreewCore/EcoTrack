package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import java.util.List;
import java.util.Map;

/** Estadísticas globales calculadas por EstadisticasMap. */
public record EstadisticasDto(int totalResiduos, double pesoTotal, double pesoPromedio,
        Map<TipoResiduo, Double> pesoPorTipo, Map<TipoResiduo, Integer> cantidadPorTipo, List<EstadoZonaDto> zonas,
        TipoResiduo tipoMasComun, ZonaGuayaquil zonaMasPendiente, ZonaGuayaquil zonaMejorUtilidad,
        ZonaGuayaquil zonaPeorUtilidad) {
}

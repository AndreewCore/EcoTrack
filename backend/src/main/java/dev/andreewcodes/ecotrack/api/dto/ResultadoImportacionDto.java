package dev.andreewcodes.ecotrack.api.dto;

import java.util.List;

/** Cuántos residuos se importaron y qué líneas se descartaron. */
public record ResultadoImportacionDto(int importados, List<String> errores) {
}

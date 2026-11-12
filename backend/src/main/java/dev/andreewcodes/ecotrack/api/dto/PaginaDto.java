package dev.andreewcodes.ecotrack.api.dto;

import java.util.List;

/** Una página de resultados (numerada desde 1). */
public record PaginaDto<T>(List<T> elementos, int pagina, int totalPaginas, int totalElementos) {
}

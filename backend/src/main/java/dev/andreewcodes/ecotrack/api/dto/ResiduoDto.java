package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.EstadoResiduo;
import dev.andreewcodes.ecotrack.dominio.Residuo;
import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import java.time.LocalDate;

/** Residuo tal como lo ve el frontend. */
public record ResiduoDto(int id, String nombre, TipoResiduo tipo, double peso, ZonaGuayaquil zona, int prioridad,
        EstadoResiduo estado, LocalDate fechaRecoleccion) {

    /** Copia los datos del residuo de dominio. */
    public static ResiduoDto de(Residuo r) {
        return new ResiduoDto(r.getId(), r.getNombre(), r.getTipo(), r.getPeso(), r.getZona(), r.getPrioridad(),
                r.getEstado(), r.getFechaRecoleccion());
    }
}

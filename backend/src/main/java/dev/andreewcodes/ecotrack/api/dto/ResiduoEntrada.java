package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para crear o editar un residuo; la prioridad es opcional (por defecto, la del tipo). */
public record ResiduoEntrada(
        @NotBlank(message = "El nombre es obligatorio.") @Size(max = 80, message = "El nombre admite como mucho 80 caracteres.") String nombre,
        @NotNull(message = "El tipo es obligatorio.") TipoResiduo tipo,
        @Positive(message = "El peso debe ser mayor a 0.") @DecimalMax(value = "100000", message = "El peso no puede superar 100000 kg.") double peso,
        @NotNull(message = "La zona es obligatoria.") ZonaGuayaquil zona,
        @Min(value = 1, message = "La prioridad mínima es 1.") @Max(value = 5, message = "La prioridad máxima es 5.") Integer prioridad) {
}

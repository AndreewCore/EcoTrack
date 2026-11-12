package dev.andreewcodes.ecotrack.api.dto;

import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/** Datos para registrar un vehículo. */
public record VehiculoEntrada(
        @NotNull(message = "El id es obligatorio.") @Pattern(regexp = "[A-Za-z0-9-]{2,20}", message = "El id debe tener de 2 a 20 letras, números o guiones.") String id,
        @NotNull(message = "La zona es obligatoria.") ZonaGuayaquil zonaAsignada,
        @Positive(message = "La capacidad debe ser mayor a 0.") @DecimalMax(value = "50000", message = "La capacidad no puede superar 50000 kg.") double capacidad) {
}

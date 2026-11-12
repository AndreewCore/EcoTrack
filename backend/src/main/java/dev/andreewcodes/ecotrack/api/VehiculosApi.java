package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.api.dto.ResultadoDespachoDto;
import dev.andreewcodes.ecotrack.api.dto.VehiculoDto;
import dev.andreewcodes.ecotrack.api.dto.VehiculoEntrada;
import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Flota de vehículos y despacho por cola de prioridad. */
@RestController
public class VehiculosApi {

    private final EcoTrackService servicio;

    public VehiculosApi(EcoTrackService servicio) {
        this.servicio = servicio;
    }

    /** Vehículos en el orden en que saldrían. */
    @GetMapping("/api/vehiculos")
    public List<VehiculoDto> listar() {
        return servicio.listarVehiculos();
    }

    @PostMapping("/api/vehiculos")
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoDto crear(@Valid @RequestBody VehiculoEntrada entrada) {
        return servicio.crearVehiculo(entrada);
    }

    @DeleteMapping("/api/vehiculos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        servicio.eliminarVehiculo(id);
    }

    /** Despacha el vehículo de la zona más crítica y lleva sus residuos al centro. */
    @PostMapping("/api/despachos")
    public ResultadoDespachoDto despachar() {
        return servicio.despachar();
    }
}

package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.api.dto.EstadoCentroDto;
import dev.andreewcodes.ecotrack.api.dto.ResiduoDto;
import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Centro de reciclaje (pila LIFO). */
@RestController
@RequestMapping("/api/centro")
public class CentroApi {

    private final EcoTrackService servicio;

    public CentroApi(EcoTrackService servicio) {
        this.servicio = servicio;
    }

    /** Pila del tope al fondo, capacidad y total procesado. */
    @GetMapping
    public EstadoCentroDto estado() {
        return servicio.estadoCentro();
    }

    /** Procesa el residuo del tope de la pila. */
    @PostMapping("/procesar")
    public ResiduoDto procesar() {
        return servicio.procesarSiguiente();
    }
}

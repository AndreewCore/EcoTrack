package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.api.dto.EstadoZonaDto;
import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/** Zonas de la ciudad: estado de recolección y polígonos GeoJSON. */
@RestController
@RequestMapping("/api/zonas")
public class ZonasApi {

    private final EcoTrackService servicio;
    private final JsonNode geojson;

    public ZonasApi(EcoTrackService servicio, ObjectMapper mapper) {
        this.servicio = servicio;
        try (InputStream in = new ClassPathResource("zonas.geojson").getInputStream()) {
            this.geojson = mapper.readTree(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Falta zonas.geojson en el classpath", e);
        }
    }

    /** Pesos y utilidad de las 18 zonas (lo que usa el frontend para colorear el mapa). */
    @GetMapping("/estado")
    public List<EstadoZonaDto> estado() {
        return servicio.estadoZonas();
    }

    /**
     * FeatureCollection con los polígonos de las zonas y sus estadísticas en
     * {@code properties}, lista para cualquier cliente GeoJSON.
     */
    @GetMapping(produces = "application/geo+json")
    public JsonNode geojson() {
        Map<String, EstadoZonaDto> porZona = servicio.estadoZonas().stream()
                .collect(Collectors.toMap(z -> z.zona().name(), Function.identity()));
        JsonNode copia = geojson.deepCopy();
        for (JsonNode feature : copia.get("features")) {
            ObjectNode props = (ObjectNode) feature.get("properties");
            EstadoZonaDto estado = porZona.get(props.get("id").asString());
            if (estado != null) {
                props.put("pesoPendiente", estado.pesoPendiente());
                props.put("pesoRecolectado", estado.pesoRecolectado());
                props.put("utilidad", estado.utilidad());
            }
        }
        return copia;
    }
}

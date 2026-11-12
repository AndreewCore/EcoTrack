package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.api.dto.EstadisticasDto;
import dev.andreewcodes.ecotrack.api.dto.ResultadoImportacionDto;
import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Estadísticas y los botones "Descargar datos" / "Subir datos". */
@RestController
public class DatosApi {

    private final EcoTrackService servicio;

    public DatosApi(EcoTrackService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/api/estadisticas")
    public EstadisticasDto estadisticas() {
        return servicio.estadisticas();
    }

    /** Descarga todos los residuos como {@code residuos_ecotrack.csv}. */
    @GetMapping("/api/datos/exportar")
    public ResponseEntity<String> exportar() {
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("residuos_ecotrack.csv").build().toString())
                .body(servicio.exportarCsv());
    }

    /**
     * Importa un CSV de residuos.
     *
     * @param modo {@code agregar} (ids nuevos) o {@code reemplazar} (sustituye todo)
     */
    @PostMapping(path = "/api/datos/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultadoImportacionDto importar(@RequestParam("archivo") MultipartFile archivo,
            @RequestParam(defaultValue = "agregar") String modo) throws IOException {
        if (!modo.equals("agregar") && !modo.equals("reemplazar")) {
            throw new IllegalArgumentException("Modo no válido: usa 'agregar' o 'reemplazar'.");
        }
        String contenido = new String(archivo.getBytes(), StandardCharsets.UTF_8);
        return servicio.importarCsv(contenido, modo.equals("reemplazar"));
    }
}

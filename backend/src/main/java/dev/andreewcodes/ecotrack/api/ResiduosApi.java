package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.api.dto.PaginaDto;
import dev.andreewcodes.ecotrack.api.dto.ResiduoDto;
import dev.andreewcodes.ecotrack.api.dto.ResiduoEntrada;
import dev.andreewcodes.ecotrack.dominio.EstadoResiduo;
import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** CRUD de residuos con filtros, orden y paginación. */
@RestController
@RequestMapping("/api/residuos")
public class ResiduosApi {

    private final EcoTrackService servicio;

    public ResiduosApi(EcoTrackService servicio) {
        this.servicio = servicio;
    }

    /** {@code GET /api/residuos?tipo&zona&estado&orden=peso|tipo|prioridad|id&dir=asc|desc&pagina&tamano} */
    @GetMapping
    public PaginaDto<ResiduoDto> listar(@RequestParam(required = false) TipoResiduo tipo,
            @RequestParam(required = false) ZonaGuayaquil zona,
            @RequestParam(required = false) EstadoResiduo estado,
            @RequestParam(defaultValue = "id") String orden,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {
        return servicio.listarResiduos(tipo, zona, estado, orden, dir, pagina, tamano);
    }

    @GetMapping("/{id}")
    public ResiduoDto obtener(@PathVariable int id) {
        return servicio.obtenerResiduo(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResiduoDto crear(@Valid @RequestBody ResiduoEntrada entrada) {
        return servicio.crearResiduo(entrada);
    }

    @PutMapping("/{id}")
    public ResiduoDto actualizar(@PathVariable int id, @Valid @RequestBody ResiduoEntrada entrada) {
        return servicio.actualizarResiduo(id, entrada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        servicio.eliminarResiduo(id);
    }
}

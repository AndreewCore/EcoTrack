package dev.andreewcodes.ecotrack.servicio;

import dev.andreewcodes.ecotrack.Utilitaria.DataManager;
import dev.andreewcodes.ecotrack.Utilitaria.ResiduoComparators;
import dev.andreewcodes.ecotrack.Utilitaria.ResiduoIterator;
import dev.andreewcodes.ecotrack.api.dto.EstadisticasDto;
import dev.andreewcodes.ecotrack.api.dto.EstadoCentroDto;
import dev.andreewcodes.ecotrack.api.dto.EstadoZonaDto;
import dev.andreewcodes.ecotrack.api.dto.PaginaDto;
import dev.andreewcodes.ecotrack.api.dto.ResiduoDto;
import dev.andreewcodes.ecotrack.api.dto.ResiduoEntrada;
import dev.andreewcodes.ecotrack.api.dto.ResultadoDespachoDto;
import dev.andreewcodes.ecotrack.api.dto.ResultadoImportacionDto;
import dev.andreewcodes.ecotrack.api.dto.VehiculoDto;
import dev.andreewcodes.ecotrack.api.dto.VehiculoEntrada;
import dev.andreewcodes.ecotrack.dominio.EcoTrackData;
import dev.andreewcodes.ecotrack.dominio.EstadisticasMap;
import dev.andreewcodes.ecotrack.dominio.EstadoResiduo;
import dev.andreewcodes.ecotrack.dominio.ListaResiduos;
import dev.andreewcodes.ecotrack.dominio.OperacionInvalidaException;
import dev.andreewcodes.ecotrack.dominio.RecursoNoEncontradoException;
import dev.andreewcodes.ecotrack.dominio.Residuo;
import dev.andreewcodes.ecotrack.dominio.ResultadoDespacho;
import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.Vehiculo;
import dev.andreewcodes.ecotrack.dominio.Zona;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import dev.andreewcodes.ecotrack.TDA.Entry;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * Fachada de la API sobre el dominio. Mantiene un único {@link EcoTrackData} en
 * memoria, lo persiste en CSV tras cada cambio y traduce a DTOs.
 *
 * Los métodos son {@code synchronized} porque las estructuras TDA no son seguras
 * entre hilos y Spring atiende cada petición en un hilo distinto.
 */
@Service
public class EcoTrackService {

    private static final Logger log = LoggerFactory.getLogger(EcoTrackService.class);
    public static final int TAMANO_PAGINA_MAXIMO = 100;

    private final DataManager dataManager;
    private final boolean cargarSemilla;
    private final Clock reloj;
    private EcoTrackData datos = new EcoTrackData();

    public EcoTrackService(@Value("${ecotrack.data-dir:./data}") String directorioDatos,
            @Value("${ecotrack.semilla:true}") boolean cargarSemilla, Clock reloj) {
        this.dataManager = new DataManager(Path.of(directorioDatos));
        this.cargarSemilla = cargarSemilla;
        this.reloj = reloj;
    }

    /** Carga los CSV guardados o, la primera vez, los datos de ejemplo. */
    @PostConstruct
    public synchronized void inicializar() {
        if (dataManager.existenDatos()) {
            dataManager.cargar(datos);
        } else if (cargarSemilla) {
            datos.incorporarResiduos(DataManager.leerResiduos(leerRecurso("semilla/residuos.csv")).residuos(), true);
            DataManager.leerVehiculos(leerRecurso("semilla/vehiculos.csv")).forEach(datos.getDespacho()::agregarVehiculo);
            persistir();
            log.info("Datos de ejemplo cargados ({} residuos)", datos.getListaResiduos().size());
        }
    }

    // ---------- Residuos ----------

    /**
     * Lista residuos filtrados y ordenados, paginados con el {@link ResiduoIterator}
     * propio. El orden se aplica sobre una copia para no alterar el orden de registro.
     */
    public synchronized PaginaDto<ResiduoDto> listarResiduos(TipoResiduo tipo, ZonaGuayaquil zona,
            EstadoResiduo estado, String orden, String dir, int pagina, int tamano) {
        ListaResiduos lista = datos.getListaResiduos()
                .filtrar(r -> (tipo == null || r.getTipo() == tipo)
                        && (zona == null || r.getZona() == zona)
                        && (estado == null || r.getEstado() == estado));
        lista.sort(comparador(orden, dir));

        int tam = Math.clamp(tamano, 1, TAMANO_PAGINA_MAXIMO);
        ResiduoIterator iterador = new ResiduoIterator(lista, tam);
        int totalPaginas = Math.max(1, iterador.getTotalPaginas());
        int actual = Math.clamp(pagina, 1, totalPaginas);
        List<ResiduoDto> elementos = new ArrayList<>();
        if (!lista.isEmpty()) {
            iterador.irAPagina(actual);
            for (Residuo r : iterador.getPaginaActual()) {
                elementos.add(ResiduoDto.de(r));
            }
        }
        return new PaginaDto<>(elementos, actual, totalPaginas, lista.size());
    }

    public synchronized ResiduoDto obtenerResiduo(int id) {
        return ResiduoDto.de(datos.obtenerResiduo(id));
    }

    public synchronized ResiduoDto crearResiduo(ResiduoEntrada e) {
        Residuo r = datos.registrarResiduo(e.nombre().trim(), e.tipo(), e.peso(), e.zona(), e.prioridad());
        persistir();
        return ResiduoDto.de(r);
    }

    public synchronized ResiduoDto actualizarResiduo(int id, ResiduoEntrada e) {
        Residuo r = datos.actualizarResiduo(id, e.nombre().trim(), e.tipo(), e.peso(), e.zona(), e.prioridad());
        persistir();
        return ResiduoDto.de(r);
    }

    public synchronized void eliminarResiduo(int id) {
        datos.eliminarResiduo(id);
        persistir();
    }

    // ---------- Zonas ----------

    public synchronized List<EstadoZonaDto> estadoZonas() {
        List<EstadoZonaDto> zonas = new ArrayList<>();
        for (Zona z : datos.getListaZonas()) {
            zonas.add(EstadoZonaDto.de(z));
        }
        return zonas;
    }

    // ---------- Vehículos y despacho ----------

    /** Flota en el orden en que saldría de la cola de prioridad. */
    public synchronized List<VehiculoDto> listarVehiculos() {
        List<VehiculoDto> lista = new ArrayList<>();
        int turno = 1;
        for (Vehiculo v : datos.getDespacho().getVehiculosEnOrden()) {
            lista.add(VehiculoDto.de(v, turno++));
        }
        return lista;
    }

    public synchronized VehiculoDto crearVehiculo(VehiculoEntrada e) {
        Vehiculo v = new Vehiculo(e.id().trim().toUpperCase(), e.zonaAsignada(), e.capacidad());
        datos.getDespacho().agregarVehiculo(v);
        persistir();
        return listarVehiculos().stream().filter(x -> x.id().equals(v.getId())).findFirst().orElseThrow();
    }

    public synchronized void eliminarVehiculo(String id) {
        if (!datos.getDespacho().quitarVehiculo(id)) {
            throw new RecursoNoEncontradoException("No existe el vehículo " + id + ".");
        }
        persistir();
    }

    public synchronized ResultadoDespachoDto despachar() {
        ResultadoDespacho r = datos.despachar(LocalDate.now(reloj));
        persistir();
        int turno = listarVehiculos().stream().filter(v -> v.id().equals(r.vehiculo().getId()))
                .findFirst().map(VehiculoDto::turno).orElse(0);
        return new ResultadoDespachoDto(VehiculoDto.de(r.vehiculo(), turno), r.zona(),
                r.residuos().stream().map(ResiduoDto::de).toList(), r.pesoTotal(), r.aviso());
    }

    // ---------- Centro de reciclaje ----------

    /** Pila del centro del tope al fondo. */
    public synchronized EstadoCentroDto estadoCentro() {
        List<ResiduoDto> pila = new ArrayList<>();
        for (Residuo r : datos.getCentroReciclaje().getResiduos()) {
            pila.add(ResiduoDto.de(r));
        }
        return new EstadoCentroDto(datos.getCentroReciclaje().getCapacidadMaxima(), pila,
                datos.getCentroReciclaje().getResiduosProcesados());
    }

    public synchronized ResiduoDto procesarSiguiente() {
        Residuo r = datos.procesarSiguiente();
        persistir();
        return ResiduoDto.de(r);
    }

    // ---------- Estadísticas ----------

    public synchronized EstadisticasDto estadisticas() {
        EstadisticasMap stats = new EstadisticasMap(datos.getListaResiduos(), datos.getListaZonas());
        stats.calcularEstadisticas();
        Map<TipoResiduo, Double> peso = new EnumMap<>(TipoResiduo.class);
        for (Entry<TipoResiduo, Double> e : stats.getPesoTotalPorTipo().entrySet()) {
            peso.put(e.getKey(), e.getValue());
        }
        Map<TipoResiduo, Integer> cantidad = new EnumMap<>(TipoResiduo.class);
        for (Entry<TipoResiduo, Integer> e : stats.getCantidadPorTipo().entrySet()) {
            cantidad.put(e.getKey(), e.getValue());
        }
        return new EstadisticasDto(stats.getTotalResiduos(), stats.getPesoTotal(), stats.getPesoPromedio(), peso,
                cantidad, estadoZonas(), stats.getTipoMasComun(), stats.getZonaMayorPesoPendiente(),
                stats.getZonaMejorUtilidad(), stats.getZonaPeorUtilidad());
    }

    // ---------- Importar / exportar ----------

    public synchronized String exportarCsv() {
        return DataManager.residuosACsv(datos.getListaResiduos());
    }

    /**
     * Importa residuos de un CSV.
     *
     * @param reemplazar true sustituye todos los residuos actuales; false los añade con ids nuevos
     */
    public synchronized ResultadoImportacionDto importarCsv(String contenido, boolean reemplazar) {
        DataManager.ResultadoLectura lectura = DataManager.leerResiduos(contenido);
        if (lectura.residuos().isEmpty()) {
            throw new OperacionInvalidaException("El archivo no contiene residuos válidos."
                    + (lectura.errores().isEmpty() ? "" : " " + String.join(" ", lectura.errores().subList(0, Math.min(3, lectura.errores().size())))));
        }
        if (reemplazar) {
            datos.vaciarResiduos();
        }
        datos.incorporarResiduos(lectura.residuos(), reemplazar);
        persistir();
        return new ResultadoImportacionDto(lectura.residuos().size(), lectura.errores());
    }

    /** Descarta el estado en memoria y vuelve a leer del disco (útil en pruebas). */
    public synchronized void recargar() {
        datos = new EcoTrackData();
        inicializar();
    }

    // ---------- Internos ----------

    private void persistir() {
        dataManager.guardar(datos);
    }

    /** Comparador de los criterios de la UI: peso, tipo, prioridad o id. */
    private static Comparator<Residuo> comparador(String orden, String dir) {
        Comparator<Residuo> base = switch (orden == null ? "id" : orden) {
            case "peso" -> ResiduoComparators.porPeso();
            case "tipo" -> ResiduoComparators.porTipo();
            case "prioridad" -> ResiduoComparators.porPrioridad();
            case "id" -> ResiduoComparators.porId();
            default -> throw new IllegalArgumentException("Orden no válido: " + orden + ". Usa peso, tipo, prioridad o id.");
        };
        // Desempate por id para que el orden sea estable entre peticiones.
        base = base.thenComparing(ResiduoComparators.porId());
        return "desc".equalsIgnoreCase(dir) ? base.reversed() : base;
    }

    private static String leerRecurso(String ruta) {
        try (InputStream in = new ClassPathResource(ruta).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se encontró el recurso " + ruta, e);
        }
    }
}

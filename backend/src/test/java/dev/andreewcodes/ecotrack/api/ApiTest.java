package dev.andreewcodes.ecotrack.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.andreewcodes.ecotrack.servicio.EcoTrackService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApiTest {

    private static final Path DIR;

    static {
        try {
            DIR = Files.createTempDirectory("ecotrack-api-test");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry r) {
        r.add("ecotrack.data-dir", DIR::toString);
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    EcoTrackService servicio;

    /** Cada prueba arranca desde los datos semilla. */
    @BeforeEach
    void reiniciar() throws IOException {
        try (Stream<Path> archivos = Files.list(DIR)) {
            for (Path p : archivos.toList()) {
                Files.delete(p);
            }
        }
        servicio.recargar();
    }

    @AfterAll
    static void limpiar() throws IOException {
        try (Stream<Path> s = Files.walk(DIR)) {
            s.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
    }

    @Test
    void listaResiduosSemillaFiltradosYOrdenados() throws Exception {
        mvc.perform(get("/api/residuos").param("tipo", "ORGANICO").param("orden", "peso").param("dir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos").value(5))
                .andExpect(jsonPath("$.elementos[0].nombre").value("Restos de comida"))
                .andExpect(jsonPath("$.elementos[*].tipo", everyItem(is("ORGANICO"))));

        mvc.perform(get("/api/residuos").param("tamano", "10").param("pagina", "3"))
                .andExpect(jsonPath("$.pagina").value(3))
                .andExpect(jsonPath("$.totalPaginas").value(3))
                .andExpect(jsonPath("$.elementos", hasSize(4)));
    }

    @Test
    void crearEditarYEliminarResiduo() throws Exception {
        String json = """
                {"nombre":"Pilas","tipo":"PELIGROSO","peso":2.5,"zona":"FAE"}""";
        mvc.perform(post("/api/residuos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(25))
                .andExpect(jsonPath("$.prioridad").value(5))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        mvc.perform(put("/api/residuos/25").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":"Pilas AA","tipo":"PELIGROSO","peso":3,"zona":"FAE","prioridad":4}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pilas AA"))
                .andExpect(jsonPath("$.prioridad").value(4));

        mvc.perform(delete("/api/residuos/25")).andExpect(status().isNoContent());
        mvc.perform(get("/api/residuos/25")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe el residuo 25."));
    }

    @Test
    void validaLaEntrada() throws Exception {
        mvc.perform(post("/api/residuos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":" ","tipo":"PLASTICO","peso":-1,"zona":"FAE"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("El peso debe ser mayor a 0.")))
                .andExpect(jsonPath("$.detail", containsString("El nombre es obligatorio.")));

        mvc.perform(post("/api/residuos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":"x","tipo":"MADERA","peso":1,"zona":"FAE"}"""))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/residuos").param("orden", "color")).andExpect(status().isBadRequest());
    }

    @Test
    void despacharLlevaResiduosAlCentroYProcesarLosSaca() throws Exception {
        // Fortín / Monte Sinaí es el peor balance de la semilla (730 kg pendientes) y tiene a GYE-002.
        mvc.perform(get("/api/vehiculos")).andExpect(jsonPath("$[0].id").value("GYE-002"));

        // Con 600 kg de capacidad sube primero los bidones (prioridad 3); los escombros (520 kg) ya no caben.
        mvc.perform(post("/api/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehiculo.id").value("GYE-002"))
                .andExpect(jsonPath("$.zona").value("FORTIN_MONTE_SINAI"))
                .andExpect(jsonPath("$.pesoTotal").value(210.0))
                .andExpect(jsonPath("$.aviso", containsString("capacidad")));

        mvc.perform(get("/api/centro"))
                .andExpect(jsonPath("$.pila", hasSize(1)))
                .andExpect(jsonPath("$.pila[0].nombre").value("Bidones plásticos"));

        mvc.perform(get("/api/zonas/estado"))
                .andExpect(jsonPath("$[?(@.zona=='FORTIN_MONTE_SINAI')].utilidad").value(-310.0));

        // Ahora Guasmo (-670) es la zona más crítica: el siguiente turno es GYE-001.
        mvc.perform(get("/api/vehiculos")).andExpect(jsonPath("$[0].id").value("GYE-001"));

        mvc.perform(post("/api/centro/procesar"))
                .andExpect(jsonPath("$.estado").value("PROCESADO"));
        mvc.perform(get("/api/centro")).andExpect(jsonPath("$.procesados").value(1));
    }

    @Test
    void zonasEnGeoJsonConEstadisticas() throws Exception {
        mvc.perform(get("/api/zonas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("FeatureCollection"))
                .andExpect(jsonPath("$.features", hasSize(18)))
                .andExpect(jsonPath("$.features[?(@.properties.id=='GUASMO')].properties.pesoPendiente").value(670.0));
    }

    @Test
    void estadisticas() throws Exception {
        mvc.perform(get("/api/estadisticas"))
                .andExpect(jsonPath("$.totalResiduos").value(24))
                .andExpect(jsonPath("$.tipoMasComun").value("ORGANICO"))
                .andExpect(jsonPath("$.zonaPeorUtilidad").value("FORTIN_MONTE_SINAI"))
                .andExpect(jsonPath("$.zonas", hasSize(18)));
    }

    @Test
    void vehiculosDuplicadosOInexistentes() throws Exception {
        mvc.perform(post("/api/vehiculos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"id":"gye-001","zonaAsignada":"FAE","capacidad":100}"""))
                .andExpect(status().isConflict());
        mvc.perform(delete("/api/vehiculos/NADA")).andExpect(status().isNotFound());
    }

    @Test
    void exportarEImportarCsv() throws Exception {
        mvc.perform(get("/api/datos/exportar"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("residuos_ecotrack.csv")))
                .andExpect(content().string(containsString("ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado")));

        MockMultipartFile archivo = new MockMultipartFile("archivo", "r.csv", "text/csv",
                "ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado\n1,Nuevo,PAPEL,1,,FAE,1,PENDIENTE\nroto\n".getBytes());
        mvc.perform(multipart("/api/datos/importar").file(archivo).param("modo", "agregar"))
                .andExpect(jsonPath("$.importados").value(1))
                .andExpect(jsonPath("$.errores", hasSize(1)));
        mvc.perform(get("/api/residuos")).andExpect(jsonPath("$.totalElementos").value(25));

        mvc.perform(multipart("/api/datos/importar").file(archivo).param("modo", "reemplazar"))
                .andExpect(jsonPath("$.importados").value(1));
        mvc.perform(get("/api/residuos")).andExpect(jsonPath("$.totalElementos").value(1));
    }
}

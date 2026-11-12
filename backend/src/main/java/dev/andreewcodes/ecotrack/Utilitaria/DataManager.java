package dev.andreewcodes.ecotrack.Utilitaria;

import dev.andreewcodes.ecotrack.dominio.EcoTrackData;
import dev.andreewcodes.ecotrack.dominio.EstadoResiduo;
import dev.andreewcodes.ecotrack.dominio.Residuo;
import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.Vehiculo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Persistencia de EcoTrack en archivos CSV dentro de un directorio de datos:
 * {@code residuos.csv} y {@code vehiculos.csv}.
 *
 * Formato de residuos: {@code ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado}.
 * También acepta el formato antiguo de 7 columnas (sin Estado), en el que un
 * residuo con fecha se considera procesado.
 *
 * @author Paúl Rodríguez
 */
public class DataManager {

    private static final Logger log = LoggerFactory.getLogger(DataManager.class);

    public static final String ARCHIVO_RESIDUOS = "residuos.csv";
    public static final String ARCHIVO_VEHICULOS = "vehiculos.csv";
    public static final String ENCABEZADO_RESIDUOS = "ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad,Estado";
    public static final String ENCABEZADO_VEHICULOS = "ID,Zona,Capacidad";

    private final Path directorio;

    /** @param directorio carpeta donde viven los CSV; se crea al guardar si no existe */
    public DataManager(Path directorio) {
        this.directorio = directorio;
    }

    /** Si ya hay datos guardados en el directorio. */
    public boolean existenDatos() {
        return Files.exists(directorio.resolve(ARCHIVO_RESIDUOS));
    }

    /** Guarda residuos y vehículos. Escribe a un temporal y lo mueve para no dejar archivos a medias. */
    public void guardar(EcoTrackData datos) {
        try {
            Files.createDirectories(directorio);
            escribirAtomico(directorio.resolve(ARCHIVO_RESIDUOS), residuosACsv(datos.getListaResiduos()));
            StringBuilder sb = new StringBuilder(ENCABEZADO_VEHICULOS).append('\n');
            for (Vehiculo v : datos.getDespacho().getVehiculosEnOrden()) {
                sb.append(escapar(v.getId())).append(',').append(v.getZonaAsignada()).append(',')
                        .append(v.getCapacidad()).append('\n');
            }
            escribirAtomico(directorio.resolve(ARCHIVO_VEHICULOS), sb.toString());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudieron guardar los datos en " + directorio, e);
        }
    }

    /** Carga residuos y vehículos del directorio en {@code datos} (que debe estar vacío). */
    public void cargar(EcoTrackData datos) {
        try {
            Path residuos = directorio.resolve(ARCHIVO_RESIDUOS);
            if (Files.exists(residuos)) {
                ResultadoLectura lectura = leerResiduos(Files.readString(residuos, StandardCharsets.UTF_8));
                lectura.errores().forEach(err -> log.warn("{}: {}", ARCHIVO_RESIDUOS, err));
                datos.incorporarResiduos(lectura.residuos(), true);
            }
            Path vehiculos = directorio.resolve(ARCHIVO_VEHICULOS);
            if (Files.exists(vehiculos)) {
                for (Vehiculo v : leerVehiculos(Files.readString(vehiculos, StandardCharsets.UTF_8))) {
                    datos.getDespacho().agregarVehiculo(v);
                }
            }
            log.info("Cargados {} residuos y vehículos desde {}", datos.getListaResiduos().size(), directorio);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudieron leer los datos de " + directorio, e);
        }
    }

    // ---------- Conversión CSV (también la usan exportar/importar) ----------

    /** Serializa los residuos con encabezado. */
    public static String residuosACsv(Iterable<Residuo> residuos) {
        StringBuilder sb = new StringBuilder(ENCABEZADO_RESIDUOS).append('\n');
        for (Residuo r : residuos) {
            sb.append(r.getId()).append(',')
                    .append(escapar(r.getNombre())).append(',')
                    .append(r.getTipo()).append(',')
                    .append(r.getPeso()).append(',')
                    .append(r.getFechaRecoleccion() != null ? r.getFechaRecoleccion().toString() : "").append(',')
                    .append(r.getZona()).append(',')
                    .append(r.getPrioridad()).append(',')
                    .append(r.getEstado()).append('\n');
        }
        return sb.toString();
    }

    /** Residuos válidos leídos y mensajes de las líneas que se descartaron. */
    public record ResultadoLectura(List<Residuo> residuos, List<String> errores) {
    }

    /**
     * Parsea un CSV de residuos. Las líneas inválidas no detienen la lectura: se
     * informan en {@code errores} con su número de línea.
     */
    public static ResultadoLectura leerResiduos(String contenido) {
        List<Residuo> residuos = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        String[] lineas = contenido.replace("\r", "").split("\n");
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            if (linea.isBlank() || (i == 0 && linea.startsWith("ID,"))) {
                continue;
            }
            try {
                residuos.add(lineaAResiduo(linea));
            } catch (RuntimeException e) {
                errores.add("Línea " + (i + 1) + ": " + e.getMessage());
            }
        }
        return new ResultadoLectura(residuos, errores);
    }

    /** Convierte una línea CSV en residuo, validando cada campo. */
    private static Residuo lineaAResiduo(String linea) {
        List<String> c = dividir(linea);
        if (c.size() != 7 && c.size() != 8) {
            throw new IllegalArgumentException("se esperaban 7 u 8 columnas y hay " + c.size());
        }
        int id = Integer.parseInt(c.get(0).trim());
        String nombre = c.get(1).trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("el nombre está vacío");
        }
        TipoResiduo tipo = TipoResiduo.valueOf(c.get(2).trim().toUpperCase());
        double peso = Double.parseDouble(c.get(3).trim());
        if (!(peso > 0)) {
            throw new IllegalArgumentException("el peso debe ser mayor a 0");
        }
        LocalDate fecha = c.get(4).isBlank() ? null : LocalDate.parse(c.get(4).trim());
        ZonaGuayaquil zona = ZonaGuayaquil.valueOf(c.get(5).trim().toUpperCase());
        int prioridad = c.get(6).isBlank() ? tipo.getPrioridadPorDefecto() : Integer.parseInt(c.get(6).trim());
        if (prioridad < 0 || prioridad > 5) {
            throw new IllegalArgumentException("la prioridad debe estar entre 1 y 5");
        }
        // El CSV de la app de escritorio guardaba prioridad 0; se normaliza a la del tipo.
        if (prioridad == 0) {
            prioridad = tipo.getPrioridadPorDefecto();
        }
        EstadoResiduo estado = c.size() == 8 && !c.get(7).isBlank()
                ? EstadoResiduo.valueOf(c.get(7).trim().toUpperCase())
                : (fecha != null ? EstadoResiduo.PROCESADO : EstadoResiduo.PENDIENTE);

        Residuo r = new Residuo(id, nombre, tipo, peso, zona, prioridad);
        r.setEstado(estado);
        r.setFechaRecoleccion(estado == EstadoResiduo.PENDIENTE ? null : (fecha != null ? fecha : LocalDate.now()));
        return r;
    }

    /** Parsea el CSV de vehículos (ignora líneas inválidas registrándolas en el log). */
    public static List<Vehiculo> leerVehiculos(String contenido) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String[] lineas = contenido.replace("\r", "").split("\n");
        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].isBlank() || (i == 0 && lineas[i].startsWith("ID,"))) {
                continue;
            }
            try {
                List<String> c = dividir(lineas[i]);
                vehiculos.add(new Vehiculo(c.get(0).trim(), ZonaGuayaquil.valueOf(c.get(1).trim()),
                        Double.parseDouble(c.get(2).trim())));
            } catch (RuntimeException e) {
                log.warn("{} línea {} descartada: {}", ARCHIVO_VEHICULOS, i + 1, e.getMessage());
            }
        }
        return vehiculos;
    }

    /** Pone comillas a un campo si contiene coma, comillas o salto de línea (RFC 4180). */
    static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }

    /** Divide una línea CSV respetando campos entre comillas y comillas dobles escapadas. */
    static List<String> dividir(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char ch = linea.charAt(i);
            if (entreComillas) {
                if (ch == '"' && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    actual.append('"');
                    i++;
                } else if (ch == '"') {
                    entreComillas = false;
                } else {
                    actual.append(ch);
                }
            } else if (ch == '"') {
                entreComillas = true;
            } else if (ch == ',') {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(ch);
            }
        }
        campos.add(actual.toString());
        return campos;
    }

    private static void escribirAtomico(Path destino, String contenido) throws IOException {
        Path temporal = destino.resolveSibling(destino.getFileName() + ".tmp");
        Files.writeString(temporal, contenido, StandardCharsets.UTF_8);
        Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
}

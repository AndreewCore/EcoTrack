package Utilitaria;

import ecotrack.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Gestor de persistencia de datos para EcoTrack
 * Permite guardar y cargar residuos desde archivos de texto
 * 
 * Formato CSV:
 * ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad
 * 
 * @author Paúl Rodríguez
 */
public class DataManager {
    
    private static final String ARCHIVO_RESIDUOS = "residuos_ecotrack.txt";
    private static final String SEPARADOR = ",";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    public static boolean guardarResiduos() {
        return guardarResiduos(ARCHIVO_RESIDUOS);
    }
    
    public static boolean guardarResiduos(String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            
            // Escribir encabezado
            writer.write("ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad");
            writer.newLine();
            
            // Escribir cada residuo
            ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
            
            for (Residuo residuo : listaGlobal) {
                String linea = convertirResiduoALinea(residuo);
                writer.write(linea);
                writer.newLine();
            }
            
            System.out.println("✅ Datos guardados exitosamente en: " + nombreArchivo);
            System.out.println("   Total de residuos guardados: " + listaGlobal.size());
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error al guardar residuos: " + e.getMessage());
            return false;
        }
    }

    public static int cargarResiduos() {
        return cargarResiduos(ARCHIVO_RESIDUOS);
    }

    public static int cargarResiduos(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            System.out.println("⚠️  No se encontró el archivo: " + nombreArchivo);
            return 0;
        }
        
        int contador = 0;
        int lineaActual = 0;
        
        // Limpiar la lista actual
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        listaGlobal.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            
            // Saltar el encabezado
            String encabezado = reader.readLine();
            lineaActual++;
            
            if (encabezado == null) {
                System.out.println("⚠️  El archivo está vacío");
                return 0;
            }
            
            // Leer cada línea
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineaActual++;
                
                if (linea.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                
                try {
                    Residuo residuo = convertirLineaAResiduo(linea);
                    if (residuo != null) {
                        listaGlobal.addLast(residuo);
                        contador++;
                    }
                } catch (Exception e) {
                    System.err.println("⚠️  Error en línea " + lineaActual + ": " + e.getMessage());
                }
            }
            
            System.out.println("✅ Datos cargados exitosamente desde: " + nombreArchivo);
            System.out.println("   Total de residuos cargados: " + contador);
            return contador;
            
        } catch (IOException e) {
            System.err.println("❌ Error al cargar residuos: " + e.getMessage());
            return -1;
        }
    }
 
    public static int importarResiduos(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            System.out.println("⚠️  No se encontró el archivo: " + nombreArchivo);
            return 0;
        }
        
        int contador = 0;
        int lineaActual = 0;
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            
            // Saltar el encabezado
            String encabezado = reader.readLine();
            lineaActual++;
            
            // Leer cada línea
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineaActual++;
                
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Residuo residuo = convertirLineaAResiduo(linea);
                    if (residuo != null) {
                        listaGlobal.addLast(residuo);
                        contador++;
                    }
                } catch (Exception e) {
                    System.err.println("⚠️  Error en línea " + lineaActual + ": " + e.getMessage());
                }
            }
            
            System.out.println("✅ Residuos importados exitosamente: " + contador);
            return contador;
            
        } catch (IOException e) {
            System.err.println("❌ Error al importar residuos: " + e.getMessage());
            return -1;
        }
    }

    private static String convertirResiduoALinea(Residuo residuo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(residuo.getId()).append(SEPARADOR);
        sb.append(escaparTexto(residuo.getNombre())).append(SEPARADOR);
        sb.append(residuo.getTipo().toString()).append(SEPARADOR);
        sb.append(residuo.getPeso()).append(SEPARADOR);
        
        // Fecha de recolección (puede ser null)
        if (residuo.getFechaRecoleccion() != null) {
            sb.append(residuo.getFechaRecoleccion().format(FORMATO_FECHA));
        }
        sb.append(SEPARADOR);
        
        sb.append(residuo.getZona().getZonaGuayaquil().toString()).append(SEPARADOR);
        sb.append(residuo.getPrioridad());
        
        return sb.toString();
    }
        
    private static Residuo convertirLineaAResiduo(String linea) {
        String[] partes = linea.split(SEPARADOR, -1); // -1 para mantener campos vacíos
        
        if (partes.length < 7) {
            throw new IllegalArgumentException("Formato de línea inválido. Se esperaban 7 campos.");
        }
        
        try {
            // Parsear campos
            int id = Integer.parseInt(partes[0].trim());
            String nombre = desescaparTexto(partes[1].trim());
            TipoResiduo tipo = TipoResiduo.valueOf(partes[2].trim());
            double peso = Double.parseDouble(partes[3].trim());
            
            LocalDate fechaRecoleccion = null;
            if (!partes[4].trim().isEmpty()) {
                fechaRecoleccion = LocalDate.parse(partes[4].trim(), FORMATO_FECHA);
            }
            
            ZonaGuayaquil zonaEnum = ZonaGuayaquil.valueOf(partes[5].trim());
            int prioridad = Integer.parseInt(partes[6].trim());
            
            // Crear residuo (el constructor ya lo agrega a la zona)
            Residuo residuo = new Residuo(nombre, tipo, peso, zonaEnum);
            
            // Establecer campos adicionales
            residuo.setId(id);
            residuo.setFechaRecoleccion(fechaRecoleccion);
            residuo.setPrioridad(prioridad);
            
            return residuo;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear residuo: " + e.getMessage());
        }
    }

    private static String escaparTexto(String texto) {
        if (texto == null) {
            return "";
        }
        
        if (texto.contains(SEPARADOR) || texto.contains("\"")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        
        return texto;
    }

    private static String desescaparTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        
        if (texto.startsWith("\"") && texto.endsWith("\"")) {
            texto = texto.substring(1, texto.length() - 1);
            texto = texto.replace("\"\"", "\"");
        }
        
        return texto;
    }

    public static boolean exportarReporte(String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            
            EstadisticasMap stats = new EstadisticasMap();
            stats.calcularEstadisticas();
            
            writer.write(stats.generarReporteCompleto());
            
            System.out.println("✅ Reporte exportado exitosamente: " + nombreArchivo);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error al exportar reporte: " + e.getMessage());
            return false;
        }
    }

    public static boolean existeArchivo() {
        return new File(ARCHIVO_RESIDUOS).exists();
    }

    public static boolean eliminarArchivo() {
        File archivo = new File(ARCHIVO_RESIDUOS);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }
}
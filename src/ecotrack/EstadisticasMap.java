package ecotrack;

import TDA.HashMap;
import TDA.TreeMap;
import TDA.Entry;
import Utilitaria.ResiduoIterator;
import java.util.Iterator;

/**
 * 
 * @author Paúl Rodríguez
 */
public class EstadisticasMap {
    
    // Maps para estadísticas
    private HashMap<TipoResiduo, Double> pesoTotalPorTipo;
    private HashMap<TipoResiduo, Integer> cantidadPorTipo;
    private TreeMap<ZonaGuayaquil, Double> pesoPendientePorZona;
    private TreeMap<ZonaGuayaquil, Double> pesoRecolectadoPorZona;
    private TreeMap<ZonaGuayaquil, Double> utilidadPorZona;
    private HashMap<String, Double> estadisticasGenerales;
    
    /**
     * Constructor - inicializa todos los maps
     */
    public EstadisticasMap() {
        this.pesoTotalPorTipo = new HashMap<>();
        this.cantidadPorTipo = new HashMap<>();
        this.pesoPendientePorZona = new TreeMap<>();
        this.pesoRecolectadoPorZona = new TreeMap<>();
        this.utilidadPorZona = new TreeMap<>();
        this.estadisticasGenerales = new HashMap<>();
    }
    
    // ============ GETTERS ============
    
    public HashMap<TipoResiduo, Double> getPesoTotalPorTipo() {
        return pesoTotalPorTipo;
    }
    
    public HashMap<TipoResiduo, Integer> getCantidadPorTipo() {
        return cantidadPorTipo;
    }
    
    public TreeMap<ZonaGuayaquil, Double> getPesoPendientePorZona() {
        return pesoPendientePorZona;
    }
    
    public TreeMap<ZonaGuayaquil, Double> getPesoRecolectadoPorZona() {
        return pesoRecolectadoPorZona;
    }
    
    public TreeMap<ZonaGuayaquil, Double> getUtilidadPorZona() {
        return utilidadPorZona;
    }
    
    public HashMap<String, Double> getEstadisticasGenerales() {
        return estadisticasGenerales;
    }
    
    // ============ MÉTODOS PRINCIPALES ============
    
    /**
     * Calcula todas las estadísticas basándose en los datos actuales
     */
    public void calcularEstadisticas() {
        limpiarMaps();
        calcularEstadisticasPorTipo();
        calcularEstadisticasPorZona();
        calcularEstadisticasGenerales();
    }
    
    /**
     * Limpia todos los maps antes de recalcular
     */
    private void limpiarMaps() {
        pesoTotalPorTipo.clear();
        cantidadPorTipo.clear();
        pesoPendientePorZona.clear();
        pesoRecolectadoPorZona.clear();
        utilidadPorZona.clear();
        estadisticasGenerales.clear();
        
        // Inicializar todos los tipos de residuo con 0
        for (TipoResiduo tipo : TipoResiduo.values()) {
            pesoTotalPorTipo.put(tipo, 0.0);
            cantidadPorTipo.put(tipo, 0);
        }
        
        // Inicializar todas las zonas con 0
        for (ZonaGuayaquil zonaEnum : ZonaGuayaquil.values()) {
            pesoPendientePorZona.put(zonaEnum, 0.0);
            pesoRecolectadoPorZona.put(zonaEnum, 0.0);
            utilidadPorZona.put(zonaEnum, 0.0);
        }
    }
    
    /**
     * Calcula estadísticas agrupadas por tipo de residuo
     * USA EL ITERADOR de CircularDoublyLinkedList (iterator() heredado)
     */
    private void calcularEstadisticasPorTipo() {
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        
        // Usando el iterador de CircularDoublyLinkedList
        Iterator<Residuo> iterador = listaGlobal.iterator();
        
        while (iterador.hasNext()) {
            Residuo residuo = iterador.next();
            TipoResiduo tipo = residuo.getTipo();
            
            // Acumular peso
            double pesoActual = pesoTotalPorTipo.getOrDefault(tipo, 0.0);
            pesoTotalPorTipo.put(tipo, pesoActual + residuo.getPeso());
            
            // Acumular cantidad
            int cantidadActual = cantidadPorTipo.getOrDefault(tipo, 0);
            cantidadPorTipo.put(tipo, cantidadActual + 1);
        }
    }
    
    /**
     * Calcula estadísticas agrupadas por zona
     * USA EL ITERADOR de CircularDoublyLinkedList para zonas
     */
    private void calcularEstadisticasPorZona() {
        ListaZonas listaZonas = ListaZonas.getInstance();
        
        // Usando el iterador de CircularDoublyLinkedList
        Iterator<Zona> iterador = listaZonas.iterator();
        
        while (iterador.hasNext()) {
            Zona zona = iterador.next();
            ZonaGuayaquil zonaEnum = zona.getZonaGuayaquil();
            
            pesoPendientePorZona.put(zonaEnum, zona.getPesoPendiente());
            pesoRecolectadoPorZona.put(zonaEnum, zona.getPesoRecolectado());
            utilidadPorZona.put(zonaEnum, zona.calcularUtilidadAmbiental());
        }
    }
    
    /**
     * Calcula estadísticas generales del sistema
     */
    private void calcularEstadisticasGenerales() {
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        
        // Total de residuos
        estadisticasGenerales.put("totalResiduos", (double) listaGlobal.size());
        
        // Peso total (usando iterador)
        double pesoTotal = 0;
        Iterator<Residuo> iterador = listaGlobal.iterator();
        while (iterador.hasNext()) {
            pesoTotal += iterador.next().getPeso();
        }
        estadisticasGenerales.put("pesoTotal", pesoTotal);
        
        // Peso promedio
        double pesoPromedio = listaGlobal.isEmpty() ? 0.0 : pesoTotal / listaGlobal.size();
        estadisticasGenerales.put("pesoPromedio", pesoPromedio);
        
        // Residuo más pesado (usando iterador)
        double pesoMaximo = 0;
        iterador = listaGlobal.iterator();
        while (iterador.hasNext()) {
            double peso = iterador.next().getPeso();
            if (peso > pesoMaximo) {
                pesoMaximo = peso;
            }
        }
        estadisticasGenerales.put("pesoMaximo", pesoMaximo);
        
        // Total pendiente en todas las zonas (usando arrays porque TreeMap no tiene iterador propio)
        double totalPendiente = 0;
        Double[] pesosPendientes = pesoPendientePorZona.values();
        for (Double peso : pesosPendientes) {
            if (peso != null) {
                totalPendiente += peso;
            }
        }
        estadisticasGenerales.put("totalPesoPendiente", totalPendiente);
        
        // Total recolectado en todas las zonas
        double totalRecolectado = 0;
        Double[] pesosRecolectados = pesoRecolectadoPorZona.values();
        for (Double peso : pesosRecolectados) {
            if (peso != null) {
                totalRecolectado += peso;
            }
        }
        estadisticasGenerales.put("totalPesoRecolectado", totalRecolectado);
        
        // Cantidad de zonas
        estadisticasGenerales.put("totalZonas", (double) ZonaGuayaquil.values().length);
    }
    
    // ============ MÉTODOS DE CONSULTA (Usando iteradores donde sea posible) ============
    
    /**
     * Obtiene el tipo de residuo más común
     * USA ARRAYS porque HashMap/TreeMap no implementan Iterable
     */
    public TipoResiduo getTipoMasComun() {
        TipoResiduo tipoMasComun = null;
        int maxCantidad = 0;
        
        Entry<TipoResiduo, Integer>[] entries = cantidadPorTipo.entrySet();
        
        for (Entry<TipoResiduo, Integer> entry : entries) {
            if (entry != null && entry.getValue() != null && entry.getValue() > maxCantidad) {
                maxCantidad = entry.getValue();
                tipoMasComun = entry.getKey();
            }
        }
        
        return tipoMasComun;
    }
    
    /**
     * Obtiene el tipo de residuo con mayor peso acumulado
     */
    public TipoResiduo getTipoConMayorPeso() {
        TipoResiduo tipoMayorPeso = null;
        double maxPeso = 0;
        
        Entry<TipoResiduo, Double>[] entries = pesoTotalPorTipo.entrySet();
        
        for (Entry<TipoResiduo, Double> entry : entries) {
            if (entry != null && entry.getValue() != null && entry.getValue() > maxPeso) {
                maxPeso = entry.getValue();
                tipoMayorPeso = entry.getKey();
            }
        }
        
        return tipoMayorPeso;
    }
    
    /**
     * Obtiene la zona con mayor peso pendiente
     */
    public ZonaGuayaquil getZonaMayorPesoPendiente() {
        ZonaGuayaquil zonaCritica = null;
        double maxPeso = 0;
        
        Entry<ZonaGuayaquil, Double>[] entries = pesoPendientePorZona.entrySet();
        
        for (Entry<ZonaGuayaquil, Double> entry : entries) {
            if (entry != null && entry.getValue() != null && entry.getValue() > maxPeso) {
                maxPeso = entry.getValue();
                zonaCritica = entry.getKey();
            }
        }
        
        return zonaCritica;
    }
    
    /**
     * Obtiene la zona con mejor utilidad ambiental
     */
    public ZonaGuayaquil getZonaMejorUtilidad() {
        ZonaGuayaquil zonaMejor = null;
        double maxUtilidad = Double.NEGATIVE_INFINITY;
        
        Entry<ZonaGuayaquil, Double>[] entries = utilidadPorZona.entrySet();
        
        for (Entry<ZonaGuayaquil, Double> entry : entries) {
            if (entry != null && entry.getValue() != null && entry.getValue() > maxUtilidad) {
                maxUtilidad = entry.getValue();
                zonaMejor = entry.getKey();
            }
        }
        
        return zonaMejor;
    }
    
    /**
     * Obtiene la zona con peor utilidad ambiental
     */
    public ZonaGuayaquil getZonaPeorUtilidad() {
        ZonaGuayaquil zonaPeor = null;
        double minUtilidad = Double.POSITIVE_INFINITY;
        
        Entry<ZonaGuayaquil, Double>[] entries = utilidadPorZona.entrySet();
        
        for (Entry<ZonaGuayaquil, Double> entry : entries) {
            if (entry != null && entry.getValue() != null && entry.getValue() < minUtilidad) {
                minUtilidad = entry.getValue();
                zonaPeor = entry.getKey();
            }
        }
        
        return zonaPeor;
    }
    
    /**
     * Genera un reporte textual completo de estadísticas
     * USA ITERADOR para recorrer residuos por páginas
     */
    public String generarReporteCompleto() {
        calcularEstadisticas(); // Asegurar datos actualizados
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("═══════════════════════════════════════════\n");
        reporte.append("    REPORTE DE ESTADÍSTICAS - ECOTRACK\n");
        reporte.append("═══════════════════════════════════════════\n\n");
        
        // Estadísticas generales
        reporte.append("📊 ESTADÍSTICAS GENERALES:\n");
        
        Double totalResiduos = estadisticasGenerales.get("totalResiduos");
        Double pesoTotal = estadisticasGenerales.get("pesoTotal");
        Double pesoPromedio = estadisticasGenerales.get("pesoPromedio");
        Double totalPendiente = estadisticasGenerales.get("totalPesoPendiente");
        Double totalRecolectado = estadisticasGenerales.get("totalPesoRecolectado");
        
        reporte.append(String.format("   • Total de residuos: %.0f\n", 
                totalResiduos != null ? totalResiduos : 0.0));
        reporte.append(String.format("   • Peso total: %.2f kg\n", 
                pesoTotal != null ? pesoTotal : 0.0));
        reporte.append(String.format("   • Peso promedio: %.2f kg\n", 
                pesoPromedio != null ? pesoPromedio : 0.0));
        reporte.append(String.format("   • Peso pendiente total: %.2f kg\n", 
                totalPendiente != null ? totalPendiente : 0.0));
        reporte.append(String.format("   • Peso recolectado total: %.2f kg\n\n", 
                totalRecolectado != null ? totalRecolectado : 0.0));
        
        // Por tipo (usando arrays porque es un Map)
        reporte.append("🗑️  ESTADÍSTICAS POR TIPO DE RESIDUO:\n");
        
        for (TipoResiduo tipo : TipoResiduo.values()) {
            Integer cantidad = cantidadPorTipo.get(tipo);
            Double peso = pesoTotalPorTipo.get(tipo);
            
            if (cantidad != null && cantidad > 0) {
                reporte.append(String.format("   • %s: %d unidades, %.2f kg\n",
                        tipo.toString(),
                        cantidad,
                        peso != null ? peso : 0.0));
            }
        }
        reporte.append("\n");
        
        // Zona crítica
        ZonaGuayaquil zonaCritica = getZonaMayorPesoPendiente();
        if (zonaCritica != null) {
            Double pesoPendienteZona = pesoPendientePorZona.get(zonaCritica);
            reporte.append("⚠️  ZONA CRÍTICA (mayor peso pendiente):\n");
            reporte.append(String.format("   • %s: %.2f kg pendientes\n\n",
                    zonaCritica.toString(),
                    pesoPendienteZona != null ? pesoPendienteZona : 0.0));
        }
        
        // Mejor zona
        ZonaGuayaquil zonaMejor = getZonaMejorUtilidad();
        if (zonaMejor != null) {
            Double utilidadZona = utilidadPorZona.get(zonaMejor);
            reporte.append("✅ ZONA CON MEJOR UTILIDAD:\n");
            reporte.append(String.format("   • %s: utilidad %.2f\n\n",
                    zonaMejor.toString(),
                    utilidadZona != null ? utilidadZona : 0.0));
        }
        
        // Peor zona
        ZonaGuayaquil zonaPeor = getZonaPeorUtilidad();
        if (zonaPeor != null) {
            Double utilidadZonaPeor = utilidadPorZona.get(zonaPeor);
            reporte.append("❌ ZONA CON PEOR UTILIDAD:\n");
            reporte.append(String.format("   • %s: utilidad %.2f\n\n",
                    zonaPeor.toString(),
                    utilidadZonaPeor != null ? utilidadZonaPeor : 0.0));
        }
        
        // Tipo más común
        TipoResiduo tipoComun = getTipoMasComun();
        if (tipoComun != null) {
            Integer cantidadTipo = cantidadPorTipo.get(tipoComun);
            reporte.append("📌 TIPO DE RESIDUO MÁS COMÚN:\n");
            reporte.append(String.format("   • %s: %d unidades\n\n",
                    tipoComun.toString(),
                    cantidadTipo != null ? cantidadTipo : 0));
        }
        
        // Tipo con mayor peso
        TipoResiduo tipoMayorPeso = getTipoConMayorPeso();
        if (tipoMayorPeso != null) {
            Double pesoTipo = pesoTotalPorTipo.get(tipoMayorPeso);
            reporte.append("⚖️  TIPO DE RESIDUO CON MAYOR PESO:\n");
            reporte.append(String.format("   • %s: %.2f kg\n\n",
                    tipoMayorPeso.toString(),
                    pesoTipo != null ? pesoTipo : 0.0));
        }
        
        reporte.append("═══════════════════════════════════════════\n");
        
        return reporte.toString();
    }
    
    /**
     * Genera un listado paginado de residuos usando ResiduoIterator
     * ESTE MÉTODO SÍ USA EL ITERADOR PERSONALIZADO
     */
    public String generarListadoPaginado(int numeroPagina) {
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        
        if (listaGlobal.isEmpty()) {
            return "No hay residuos registrados.\n";
        }
        
        // Crear iterador con páginas de 10 elementos
        ResiduoIterator iterador = new ResiduoIterator(listaGlobal, 10);
        
        // Ir a la página solicitada
        try {
            iterador.irAPagina(numeroPagina);
        } catch (IllegalArgumentException e) {
            return "Número de página inválido: " + e.getMessage() + "\n";
        }
        
        StringBuilder listado = new StringBuilder();
        listado.append("═══════════════════════════════════════════\n");
        listado.append(String.format("    LISTADO DE RESIDUOS - %s\n", iterador.getInfoPagina()));
        listado.append("═══════════════════════════════════════════\n\n");
        
        Residuo[] paginaActual = iterador.getPaginaActual();
        
        int contador = (numeroPagina - 1) * 10 + 1;
        for (Residuo residuo : paginaActual) {
            if (residuo != null) {
                listado.append(String.format("%d. %s [%s] - %.2f kg - Zona: %s\n",
                        contador++,
                        residuo.getNombre(),
                        residuo.getTipo(),
                        residuo.getPeso(),
                        residuo.getZona().getZonaGuayaquil()));
            }
        }
        
        listado.append("\n═══════════════════════════════════════════\n");
        
        return listado.toString();
    }
    
    /**
     * Genera todos los listados paginados
     * ESTE MÉTODO USA EL ITERADOR PARA RECORRER TODAS LAS PÁGINAS
     */
    public String generarListadoCompleto() {
        ListaResiduos listaGlobal = ListaResiduos.getListaResiduosGlobal();
        
        if (listaGlobal.isEmpty()) {
            return "No hay residuos registrados.\n";
        }
        
        ResiduoIterator iterador = new ResiduoIterator(listaGlobal, 10);
        StringBuilder listado = new StringBuilder();
        
        listado.append("═══════════════════════════════════════════\n");
        listado.append("    LISTADO COMPLETO DE RESIDUOS\n");
        listado.append("═══════════════════════════════════════════\n\n");
        
        int totalPaginas = iterador.getTotalPaginas();
        
        for (int pagina = 1; pagina <= totalPaginas; pagina++) {
            iterador.irAPagina(pagina);
            
            listado.append(String.format("--- %s ---\n\n", iterador.getInfoPagina()));
            
            Residuo[] paginaActual = iterador.getPaginaActual();
            int contador = (pagina - 1) * 10 + 1;
            
            for (Residuo residuo : paginaActual) {
                if (residuo != null) {
                    listado.append(String.format("%d. %s [%s] - %.2f kg - Zona: %s\n",
                            contador++,
                            residuo.getNombre(),
                            residuo.getTipo(),
                            residuo.getPeso(),
                            residuo.getZona().getZonaGuayaquil()));
                }
            }
            
            listado.append("\n");
        }
        
        listado.append("═══════════════════════════════════════════\n");
        
        return listado.toString();
    }
    
    /**
     * Genera un resumen corto de estadísticas
     */
    public String generarResumen() {
        calcularEstadisticas();
        
        StringBuilder resumen = new StringBuilder();
        
        Double totalResiduos = estadisticasGenerales.get("totalResiduos");
        Double pesoTotal = estadisticasGenerales.get("pesoTotal");
        
        resumen.append("📊 Resumen: ");
        resumen.append(String.format("%.0f residuos, ", 
                totalResiduos != null ? totalResiduos : 0.0));
        resumen.append(String.format("%.2f kg totales", 
                pesoTotal != null ? pesoTotal : 0.0));
        
        ZonaGuayaquil zonaCritica = getZonaMayorPesoPendiente();
        if (zonaCritica != null) {
            resumen.append(String.format(" | ⚠️ Zona crítica: %s", 
                    zonaCritica.toString()));
        }
        
        return resumen.toString();
    }
    
    /**
     * Obtiene estadísticas de una zona específica
     */
    public String getEstadisticasZona(ZonaGuayaquil zona) {
        calcularEstadisticas();
        
        Double pendiente = pesoPendientePorZona.get(zona);
        Double recolectado = pesoRecolectadoPorZona.get(zona);
        Double utilidad = utilidadPorZona.get(zona);
        
        return String.format("Zona: %s\nPeso pendiente: %.2f kg\nPeso recolectado: %.2f kg\nUtilidad: %.2f",
                zona.toString(),
                pendiente != null ? pendiente : 0.0,
                recolectado != null ? recolectado : 0.0,
                utilidad != null ? utilidad : 0.0);
    }
    
    /**
     * Obtiene estadísticas de un tipo de residuo específico
     */
    public String getEstadisticasTipo(TipoResiduo tipo) {
        calcularEstadisticas();
        
        Integer cantidad = cantidadPorTipo.get(tipo);
        Double peso = pesoTotalPorTipo.get(tipo);
        
        return String.format("Tipo: %s\nCantidad: %d unidades\nPeso total: %.2f kg",
                tipo.toString(),
                cantidad != null ? cantidad : 0,
                peso != null ? peso : 0.0);
    }
    
    @Override
    public String toString() {
        return generarResumen();
    }
}
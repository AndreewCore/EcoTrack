package dev.andreewcodes.ecotrack.dominio;

import java.time.LocalDate;

/**
 * Estado completo del sistema: residuos (lista circular), zonas, centro de
 * reciclaje (pila) y despacho (cola de prioridad). Centraliza las reglas que
 * mantienen todo consistente.
 */
public class EcoTrackData {

    private final ListaResiduos listaResiduos = new ListaResiduos();
    private final ListaZonas listaZonas = new ListaZonas();
    private final CentroReciclaje centroReciclaje;
    private final Despacho despacho = new Despacho(listaZonas);
    private int siguienteId = 1;

    public EcoTrackData() {
        this(CentroReciclaje.CAPACIDAD_POR_DEFECTO);
    }

    public EcoTrackData(int capacidadCentro) {
        this.centroReciclaje = new CentroReciclaje(capacidadCentro);
    }

    /** Registra un residuo pendiente con id nuevo; si la prioridad es null usa la del tipo. */
    public Residuo registrarResiduo(String nombre, TipoResiduo tipo, double peso, ZonaGuayaquil zona, Integer prioridad) {
        int p = prioridad != null ? prioridad : tipo.getPrioridadPorDefecto();
        Residuo r = new Residuo(siguienteId++, nombre, tipo, peso, zona, p);
        listaResiduos.addLast(r);
        listaZonas.recalcular(listaResiduos);
        return r;
    }

    /** Residuo por id o excepción 404. */
    public Residuo obtenerResiduo(int id) {
        Residuo r = listaResiduos.buscarPorId(id);
        if (r == null) {
            throw new RecursoNoEncontradoException("No existe el residuo " + id + ".");
        }
        return r;
    }

    /**
     * Edita un residuo. Solo se permite mientras está pendiente: una vez recogido,
     * su peso y zona ya se contabilizaron en el centro.
     */
    public Residuo actualizarResiduo(int id, String nombre, TipoResiduo tipo, double peso, ZonaGuayaquil zona,
            Integer prioridad) {
        Residuo r = obtenerResiduo(id);
        if (!r.estaPendiente()) {
            throw new OperacionInvalidaException("Solo se pueden editar residuos pendientes; \"" + r.getNombre()
                    + "\" ya fue recogido.");
        }
        r.setNombre(nombre);
        r.setTipo(tipo);
        r.setPeso(peso);
        r.setZona(zona);
        r.setPrioridad(prioridad != null ? prioridad : r.getPrioridad());
        listaZonas.recalcular(listaResiduos);
        return r;
    }

    /** Elimina un residuo que no esté esperando en la pila del centro. */
    public void eliminarResiduo(int id) {
        Residuo r = obtenerResiduo(id);
        if (r.getEstado() == EstadoResiduo.EN_CENTRO) {
            throw new OperacionInvalidaException("\"" + r.getNombre()
                    + "\" está en el centro de reciclaje: procésalo antes de eliminarlo.");
        }
        listaResiduos.remove(listaResiduos.indiceDe(id));
        listaZonas.recalcular(listaResiduos);
    }

    /** Despacha el vehículo más prioritario con la fecha de hoy. */
    public ResultadoDespacho despachar(LocalDate hoy) {
        return despacho.despachar(listaResiduos, centroReciclaje, hoy);
    }

    /** Procesa el residuo del tope del centro. */
    public Residuo procesarSiguiente() {
        Residuo r = centroReciclaje.procesarResiduo();
        if (r == null) {
            throw new OperacionInvalidaException("No hay residuos esperando en el centro de reciclaje.");
        }
        return r;
    }

    /**
     * Agrega residuos ya existentes (al cargar o importar) reconstruyendo la pila
     * del centro con los que estén EN_CENTRO, en el orden recibido (el último queda
     * en el tope). Si el centro no tiene sitio, el residuo vuelve a pendiente.
     *
     * @param conservarIds false para renumerarlos y evitar choques con los actuales
     */
    public void incorporarResiduos(Iterable<Residuo> residuos, boolean conservarIds) {
        for (Residuo r : residuos) {
            if (!conservarIds || listaResiduos.buscarPorId(r.getId()) != null) {
                r.setId(siguienteId);
            }
            siguienteId = Math.max(siguienteId, r.getId()) + 1;
            if (r.getEstado() == EstadoResiduo.EN_CENTRO && !centroReciclaje.agregarResiduo(r)) {
                r.setEstado(EstadoResiduo.PENDIENTE);
                r.setFechaRecoleccion(null);
            }
            listaResiduos.addLast(r);
        }
        centroReciclaje.setResiduosProcesados(listaResiduos.filtrarPorEstado(EstadoResiduo.PROCESADO).size());
        listaZonas.recalcular(listaResiduos);
    }

    /** Borra todos los residuos y la pila del centro (los vehículos se conservan). */
    public void vaciarResiduos() {
        listaResiduos.clear();
        centroReciclaje.vaciar();
        siguienteId = 1;
        listaZonas.recalcular(listaResiduos);
    }

    public ListaResiduos getListaResiduos() {
        return listaResiduos;
    }

    public ListaZonas getListaZonas() {
        return listaZonas;
    }

    public CentroReciclaje getCentroReciclaje() {
        return centroReciclaje;
    }

    public Despacho getDespacho() {
        return despacho;
    }
}

package dev.andreewcodes.ecotrack.dominio;

import java.time.LocalDate;

/**
 * Residuo registrado en una zona. Su estado avanza PENDIENTE → EN_CENTRO →
 * PROCESADO a medida que se despacha y se procesa.
 *
 * @author Paúl Rodríguez
 */
public class Residuo {

    // Contador solo para la app de escritorio, que crea residuos sin pasar por EcoTrackData.
    private static int siguienteIdEscritorio = 1;

    private int id;
    private String nombre;
    private TipoResiduo tipo;
    private double peso;
    private ZonaGuayaquil zona;
    private int prioridad;
    private EstadoResiduo estado;
    private LocalDate fechaRecoleccion;

    /** Crea un residuo pendiente con un id ya asignado. */
    public Residuo(int id, String nombre, TipoResiduo tipo, double peso, ZonaGuayaquil zona, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.zona = zona;
        this.prioridad = prioridad;
        this.estado = EstadoResiduo.PENDIENTE;
    }

    /**
     * Constructor de la app de escritorio: asigna id propio, prioridad por defecto y
     * suma el peso a la zona global, como hacía la versión JavaFX.
     */
    public Residuo(String nombre, TipoResiduo tipo, double peso, ZonaGuayaquil zona) {
        this(siguienteIdEscritorio++, nombre, tipo, peso, zona, tipo.getPrioridadPorDefecto());
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(zona);
        if (z != null) {
            z.setPesoPendiente(z.getPesoPendiente() + peso);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int n) {
        id = n;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        nombre = n;
    }

    public TipoResiduo getTipo() {
        return tipo;
    }

    public void setTipo(TipoResiduo n) {
        tipo = n;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double n) {
        peso = n;
    }

    public ZonaGuayaquil getZona() {
        return zona;
    }

    public void setZona(ZonaGuayaquil z) {
        zona = z;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int n) {
        prioridad = n;
    }

    public EstadoResiduo getEstado() {
        return estado;
    }

    public void setEstado(EstadoResiduo estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(LocalDate n) {
        fechaRecoleccion = n;
    }

    /** Si sigue en su zona esperando un vehículo. */
    public boolean estaPendiente() {
        return estado == EstadoResiduo.PENDIENTE;
    }

    @Override
    public String toString() {
        return String.format("#%d %s (%s, %.1f kg, %s, %s)", id, nombre, tipo, peso, zona.getNombre(), estado);
    }
}

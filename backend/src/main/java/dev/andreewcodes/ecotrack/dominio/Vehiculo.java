package dev.andreewcodes.ecotrack.dominio;

/**
 * Vehículo recolector asignado a una zona, con una capacidad de carga en kg.
 */
public class Vehiculo {

    private final String id;
    private ZonaGuayaquil zonaAsignada;
    private double capacidad;
    private double cargaActual;
    private EstadoVehiculo estado;

    public Vehiculo(String id, ZonaGuayaquil zonaAsignada, double capacidad) {
        this.id = id;
        this.zonaAsignada = zonaAsignada;
        this.capacidad = capacidad;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    /**
     * Sube el residuo al vehículo si cabe.
     *
     * @return false si superaría la capacidad
     */
    public boolean recolectarResiduo(Residuo r) {
        if (cargaActual + r.getPeso() > capacidad) {
            return false;
        }
        cargaActual += r.getPeso();
        estado = EstadoVehiculo.CARGADO;
        return true;
    }

    /** Marca el vehículo en ruta hacia su zona. */
    public void salirARuta() {
        estado = EstadoVehiculo.EN_RUTA;
    }

    /** Vacía el vehículo en el centro de reciclaje y lo deja disponible. */
    public void descargarResiduos() {
        cargaActual = 0;
        estado = EstadoVehiculo.DISPONIBLE;
    }

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    public double getCapacidadRestante() {
        return capacidad - cargaActual;
    }

    public String getId() {
        return id;
    }

    public ZonaGuayaquil getZonaAsignada() {
        return zonaAsignada;
    }

    public double getCargaActual() {
        return cargaActual;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }
}

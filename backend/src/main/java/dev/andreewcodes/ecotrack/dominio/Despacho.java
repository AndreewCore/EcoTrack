package dev.andreewcodes.ecotrack.dominio;

import dev.andreewcodes.ecotrack.TDA.Cola;
import dev.andreewcodes.ecotrack.TDA.ColaPrioridad;
import dev.andreewcodes.ecotrack.Utilitaria.ComparadorVehiculo;
import dev.andreewcodes.ecotrack.Utilitaria.ResiduoComparators;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Flota de vehículos ordenada en una cola de prioridad propia. Despachar envía el
 * vehículo de la zona más crítica, recoge sus residuos pendientes y los apila en el
 * centro de reciclaje.
 */
public class Despacho {

    private final ListaZonas zonas;
    private final ColaPrioridad<Vehiculo> cola;

    /** @param zonas estado de las zonas con el que se decide la prioridad */
    public Despacho(ListaZonas zonas) {
        this.zonas = zonas;
        this.cola = new ColaPrioridad<>(new ComparadorVehiculo(zonas));
    }

    /**
     * Registra un vehículo nuevo en la cola.
     *
     * @throws OperacionInvalidaException si ya existe uno con ese id
     */
    public void agregarVehiculo(Vehiculo v) {
        if (buscar(v.getId()) != null) {
            throw new OperacionInvalidaException("Ya existe un vehículo con id " + v.getId() + ".");
        }
        cola.encolar(v);
    }

    /** Quita un vehículo de la flota; devuelve false si no existía. */
    public boolean quitarVehiculo(String id) {
        Vehiculo v = buscar(id);
        return v != null && cola.remover(v);
    }

    /** Vehículo con ese id, o null. */
    public Vehiculo buscar(String id) {
        for (Vehiculo v : cola) {
            if (v.getId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Reordena la cola con los pesos actuales de las zonas. La prioridad depende de
     * datos que cambian (residuos nuevos, despachos), así que se recalcula antes de usarla.
     */
    public void reordenar() {
        cola.cambiarComparador(new ComparadorVehiculo(zonas));
    }

    /** Vehículos en orden de salida (el primero es el próximo en despacharse). */
    public List<Vehiculo> getVehiculosEnOrden() {
        reordenar();
        List<Vehiculo> enOrden = new ArrayList<>();
        for (Vehiculo v : cola) {
            enOrden.add(v);
        }
        return enOrden;
    }

    public boolean hayVehiculos() {
        return !cola.estaVacia();
    }

    /**
     * Despacha el vehículo más prioritario cuya zona tenga residuos pendientes.
     * Carga primero los residuos de mayor prioridad ambiental (y, a igual prioridad,
     * los más pesados) mientras quepan en el vehículo y en el centro.
     *
     * @throws OperacionInvalidaException si no hay vehículos, el centro está lleno o
     *         ninguna zona con vehículo tiene residuos pendientes
     */
    public ResultadoDespacho despachar(ListaResiduos residuos, CentroReciclaje centro, LocalDate hoy) {
        if (cola.estaVacia()) {
            throw new OperacionInvalidaException("No hay vehículos registrados para despachar.");
        }
        if (centro.estaLleno()) {
            throw new OperacionInvalidaException("El centro de reciclaje está lleno: procesa residuos antes de despachar.");
        }
        reordenar();

        Vehiculo elegido = null;
        ListaResiduos pendientes = null;
        for (Vehiculo v : cola) {
            ListaResiduos deZona = residuos.filtrar(r -> r.estaPendiente() && r.getZona() == v.getZonaAsignada());
            if (!deZona.isEmpty()) {
                elegido = v;
                pendientes = deZona;
                break;
            }
        }
        if (elegido == null) {
            throw new OperacionInvalidaException("Ninguna zona con vehículo asignado tiene residuos pendientes.");
        }

        cola.remover(elegido);
        elegido.salirARuta();
        pendientes.sort(ResiduoComparators.porPrioridad().thenComparing(ResiduoComparators.porPeso()).reversed());

        // La cola de carga respeta el orden en que el vehículo los subió al descargarlos.
        Cola<Residuo> carga = new Cola<>();
        int espacioCentro = centro.getEspacioLibre();
        boolean faltoCapacidad = false;
        for (Residuo r : pendientes) {
            if (carga.getTamaño() >= espacioCentro) {
                break;
            }
            if (elegido.recolectarResiduo(r)) {
                carga.encolar(r);
            } else {
                faltoCapacidad = true;
            }
        }

        List<Residuo> recogidos = new ArrayList<>();
        double pesoTotal = 0;
        while (!carga.estaVacia()) {
            Residuo r = carga.desencolar();
            centro.agregarResiduo(r);
            r.setFechaRecoleccion(hoy);
            recogidos.add(r);
            pesoTotal += r.getPeso();
        }
        elegido.descargarResiduos();
        cola.encolar(elegido);
        zonas.recalcular(residuos);

        String aviso = null;
        int quedan = pendientes.size() - recogidos.size();
        if (recogidos.isEmpty()) {
            aviso = "Ningún residuo cabe en el vehículo " + elegido.getId() + ": su capacidad es menor que el residuo más liviano.";
        } else if (quedan > 0) {
            aviso = "Quedan " + quedan + " residuos pendientes en la zona: "
                    + (faltoCapacidad ? "se alcanzó la capacidad del vehículo." : "el centro de reciclaje se llenó.");
        }
        return new ResultadoDespacho(elegido, elegido.getZonaAsignada(), recogidos, pesoTotal, aviso);
    }

    /** Elimina todos los vehículos (al reemplazar los datos). */
    public void vaciar() {
        cola.vaciar();
    }
}

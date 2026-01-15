/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import java.io.Serializable;

/**
 *
 * @author IBM
 */
public class Vehiculo implements Serializable {

    private String id;
    private String zonaAsignada;
    private double capacidad;
    private int prioridad;
    private double cargaActual;
    private EstadoVehiculo estado;

    public Vehiculo(String id, String zonaAsignada,
                    double capacidad, int prioridad) {

        this.id = id;
        this.zonaAsignada = zonaAsignada;
        this.capacidad = capacidad;
        this.prioridad = prioridad;
        this.cargaActual = 0;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    public boolean recolectarResiduo(Residuo r) {
        if (cargaActual + r.getPeso() > capacidad) return false;
        cargaActual += r.getPeso();
        return true;
    }

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

    public int getPrioridad() {
        return prioridad;
    }

    public String getId() {
        return id;
    }

    public String getZonaAsignada() {
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import Utilitaria.ComparadorVehiculo;
import java.util.PriorityQueue;

/**
 *
 * @author IBM
 */
public class Despacho {

    private PriorityQueue<Vehiculo> cola;

    public Despacho() {
        cola = new PriorityQueue<>(new ComparadorVehiculo());
    }

    public void agregarVehiculo(Vehiculo v) {
        cola.offer(v);
    }

    public Vehiculo despacharVehiculo() {
        return cola.poll();
    }

    public boolean hayVehiculosPendientes() {
        return !cola.isEmpty();
    }

    public PriorityQueue<Vehiculo> getCola() {
        return cola;
    }
}


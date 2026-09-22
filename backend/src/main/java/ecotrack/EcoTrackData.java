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
public class EcoTrackData implements Serializable {

    private ListaResiduos listaResiduos;
    private CentroReciclaje centroReciclaje;
    private Despacho despacho;

    public EcoTrackData() {
        listaResiduos = new ListaResiduos();
        centroReciclaje = new CentroReciclaje(100);
        despacho = new Despacho();
    }

    public void registrarResiduo(Residuo r) {
        listaResiduos.addLast(r);           // lista circular
        centroReciclaje.agregarResiduo(r);  // pila
    }

    public Residuo procesarResiduo() {
        return centroReciclaje.procesarResiduo();
    }

    public void agregarVehiculo(Vehiculo v) {
        despacho.agregarVehiculo(v);        // cola prioridad
    }

    public Vehiculo despacharVehiculo() {
        return despacho.despacharVehiculo();
    }

    public ListaResiduos getListaResiduos() {
        return listaResiduos;
    }

    public CentroReciclaje getCentroReciclaje() {
        return centroReciclaje;
    }

    public Despacho getDespacho() {
        return despacho;
    }
}
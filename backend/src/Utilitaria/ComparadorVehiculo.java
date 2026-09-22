/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilitaria;

import ecotrack.Vehiculo;
import java.util.Comparator;

/**
 *
 * @author IBM
 */
public class ComparadorVehiculo implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Integer.compare(v2.getPrioridad(), v1.getPrioridad());
    }
}
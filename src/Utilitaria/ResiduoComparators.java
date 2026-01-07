/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilitaria;

import ecotrack.Residuo;
import java.util.Comparator;

/**
 *
 * @author Paúl Rodríguez
 */
public class ResiduoComparators {
    public static Comparator<Residuo> porPeso(){
        return (o1, o2) -> {
            if (o1 == null || o2 == null) return 0;
            return Integer.compare(o1.getPeso(), o2.getPeso());
        };
    }
    
    public static Comparator<Residuo> porTipo(){
        return (o1, o2) -> {
            if (o1 == null || o2 == null) return 0;
            return o1.getTipo().compareTo(o2.getTipo());
        };
    }
}

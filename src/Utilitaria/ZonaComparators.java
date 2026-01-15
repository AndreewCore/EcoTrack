/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilitaria;

import ecotrack.Zona;
import java.util.Comparator;

/**
 *
 * @author Paúl Rodríguez
 */
public class ZonaComparators {
    public static Comparator<Zona> porVolumenResiduos(){
        return (o1, o2) -> {
            if (o1 == null || o2 == null) return 0;
            return Double.compare(o1.getPesoPendiente(), o2.getPesoPendiente());
        };
    }
}

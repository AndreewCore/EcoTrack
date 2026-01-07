/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

/**
 *
 * @author Paúl Rodríguez
 */
public class Zona {
    int pesoPendiente;
    
    public int getPesoPendiente(){ return pesoPendiente; }
    public void setPesoPendiente(int p){ pesoPendiente = p; }
    
    public int calcularUtilidadAmbiental(int pesoRecolectado){
        return pesoRecolectado - pesoPendiente;  
    }
    
    public int recogerPeso(int pesoRecogido){
        pesoPendiente -= pesoRecogido;
        if (pesoPendiente < 0) {
            pesoRecogido += pesoPendiente;
            pesoPendiente = 0;
            return pesoRecogido;
        }
        return pesoRecogido;
    }
    
}

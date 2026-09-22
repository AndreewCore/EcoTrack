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
    private ZonaGuayaquil zonaGuayaquil;
    private double pesoPendiente;
    private double pesoRecolectado;
    
    public Zona(ZonaGuayaquil zonaGuayaquil) {
        this.zonaGuayaquil = zonaGuayaquil;
        this.pesoPendiente = 0;
    }
    
    public double getPesoPendiente(){ return pesoPendiente; }
    public void setPesoPendiente(double p){ pesoPendiente = p; }
    public double getPesoRecolectado() { return pesoRecolectado; }
    public void setPesoRecolectado(double p) { pesoRecolectado = p; }
    public ZonaGuayaquil getZonaGuayaquil(){ return zonaGuayaquil; }
    
    public double calcularUtilidadAmbiental(){
        Double n = pesoRecolectado - pesoPendiente;
        return n; 
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

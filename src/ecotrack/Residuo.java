/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import java.time.LocalDate;

/**
 *
 * @author Paúl Rodríguez
 */
public class Residuo {

    private int currentId = 0;
    private int id;
    private String nombre;
    private TipoResiduo tipo;
    private double peso;
    private LocalDate fechaRecoleccion;
    private int prioridad;

    public Residuo(String nombre, TipoResiduo tipo, double peso){
        id = currentId++;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        fechaRecoleccion = null;
        System.out.println(this);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int n) {
        id = n;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        nombre = n;
    }

    public TipoResiduo getTipo() {
        return tipo;
    }

    public void setTipo(TipoResiduo n) {
        tipo = n;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(int n) {
        peso = n;
    }

    public LocalDate getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(LocalDate n) {
        fechaRecoleccion = n;
    }

    public int getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(int n){
        prioridad= n;
    }
    
    @Override
    public String toString(){
        if (fechaRecoleccion != null){
            return nombre + " de tipo: " + tipo.toString() + " con un peso de: " + peso + " recolectado el: " + fechaRecoleccion;
        } else {
            return nombre + " de tipo: " + tipo.toString() + " con un peso de: " + peso + " NOTA: Aun no ha sido procesado.";
        }
    }

}

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

    private String id;
    private String nombre;
    private TipoResiduo tipo;
    private double peso;
    private LocalDate fechaRecoleccion;
    private int prioridad;

    public String getId() {
        return id;
    }

    public void setId(String n) {
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
    public void setPriorida(int n){
        prioridad= n;
    }

    public enum TipoResiduo {
        ORGANICO,
        PLASTICO,
        VIDRIO,
        PAPEL,
        METAL,
        ELECTRONICO,
        PELIGROSO,
        OTROS
    }

}

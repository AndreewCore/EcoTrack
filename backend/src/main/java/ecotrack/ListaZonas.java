/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import TDA.CircularDoublyLinkedList;

/**
 *
 * @author Paúl Rodríguez
 */

public class ListaZonas extends CircularDoublyLinkedList<Zona> {
    
    private static ListaZonas instance;
    
    // Constructor privado
    private ListaZonas() {
        super();
        inicializarZonas();
    }
    
    // Método para obtener la única instancia
    public static ListaZonas getInstance() {
        if (instance == null) {
            instance = new ListaZonas();
        }
        return instance;
    }
    
    // Inicializar todas las zonas de Guayaquil
    private void inicializarZonas() {
        for (ZonaGuayaquil zonaEnum : ZonaGuayaquil.values()) {
            Zona zona = new Zona(zonaEnum);
            addLast(zona);
        }
    }
    
    // MÉTODO PRINCIPAL: Buscar zona por enum
    public Zona buscarPorZonaGuayaquil(ZonaGuayaquil zonaGuayaquil) {
        for (Zona zona : this) {
            if (zona.getZonaGuayaquil() == zonaGuayaquil) {
                return zona;
            }
        }
        return null; // No debería ocurrir si todas las zonas están inicializadas
    }
    
    // Método alternativo: buscar por nombre
    public Zona buscarPorNombre(String nombre) {
        for (Zona zona : this) {
            if (zona.getZonaGuayaquil().toString().equalsIgnoreCase(nombre)) {
                return zona;
            }
        }
        return null;
    }
    
    // Obtener peso total pendiente de todas las zonas
    public int calcularPesoTotalPendiente() {
        int total = 0;
        for (Zona zona : this) {
            total += zona.getPesoPendiente();
        }
        return total;
    }
    
    // Obtener zona con más peso pendiente
    public Zona obtenerZonaMasPendiente() {
        if (isEmpty()) {
            return null;
        }
        
        Zona zonaMasPendiente = get(0);
        for (Zona zona : this) {
            if (zona.getPesoPendiente() > zonaMasPendiente.getPesoPendiente()) {
                zonaMasPendiente = zona;
            }
        }
        return zonaMasPendiente;
    }
    
    // Para JavaFX - Conversión a ObservableList
    public javafx.collections.ObservableList<Zona> toObservableList() {
        javafx.collections.ObservableList<Zona> observableList
                = javafx.collections.FXCollections.observableArrayList();
        
        for (Zona zona : this) {
            observableList.add(zona);
        }
        
        return observableList;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "ListaZonas[vacía]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ListaZonas[\n");
        
        for (Zona zona : this) {
            sb.append("  ").append(zona.toString()).append("\n");
        }
        
        sb.append("]");
        return sb.toString();
    }
}
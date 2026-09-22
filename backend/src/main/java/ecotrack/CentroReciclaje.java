/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author IBM
 */
public class CentroReciclaje {

    private Deque<Residuo> pilaResiduos;
    private int capacidadMaxima;
    private int residuosProcesados;

    public CentroReciclaje(int capacidadMaxima) {
        this.pilaResiduos = new ArrayDeque<>();
        this.capacidadMaxima = capacidadMaxima;
        this.residuosProcesados = 0;
    }

    public boolean agregarResiduo(Residuo r) {
        if (pilaResiduos.size() >= capacidadMaxima) return false;
        pilaResiduos.push(r); // LIFO
        return true;
    }

    public Residuo procesarResiduo() {
        if (pilaResiduos.isEmpty()) return null;
        residuosProcesados++;
        return pilaResiduos.pop();
    }

    public Residuo verProximoResiduo() {
        return pilaResiduos.peek();
    }

    public boolean hayPendientes() {
        return !pilaResiduos.isEmpty();
    }

    public Deque<Residuo> getResiduos() {
        return pilaResiduos;
    }
}


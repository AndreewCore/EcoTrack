package Utilitaria;

import ecotrack.Residuo;
import TDA.CircularDoublyLinkedList;
import java.util.NoSuchElementException;

/**
 *
 * 
 * @author Paúl Rodríguez
 */
public class ResiduoIterator {
    
    private final CircularDoublyLinkedList<Residuo> lista;
    private int posicionActual;
    private final int tamanoPagina;
    
    public ResiduoIterator(CircularDoublyLinkedList<Residuo> lista, int tamanoPagina) {
        this.lista = lista;
        this.posicionActual = 0;
        this.tamanoPagina = tamanoPagina;
    }
    
    public ResiduoIterator(CircularDoublyLinkedList<Residuo> lista) {
        this(lista, 10);
    }
    
    public boolean hasNext() {
        return posicionActual < lista.size();
    }

    public boolean hasPrevious() {
        return posicionActual > 0;
    }

    public Residuo[] getPaginaActual() {
        if (lista.isEmpty()) {
            return new Residuo[0];
        }
        
        int inicio = posicionActual;
        int fin = Math.min(inicio + tamanoPagina, lista.size());
        int tamanoPaginaReal = fin - inicio;
        
        Residuo[] pagina = new Residuo[tamanoPaginaReal];
        
        for (int i = 0; i < tamanoPaginaReal; i++) {
            pagina[i] = lista.get(inicio + i);
        }
        
        return pagina;
    }

    public Residuo[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No hay más páginas hacia adelante");
        }
        
        posicionActual += tamanoPagina;

        if (posicionActual >= lista.size()) {
            posicionActual = Math.max(0, lista.size() - tamanoPagina);
        }
        
        return getPaginaActual();
    }

    public Residuo[] previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException("No hay más páginas hacia atrás");
        }
        
        posicionActual -= tamanoPagina;
        
        // Ajustar si nos vamos a negativo
        if (posicionActual < 0) {
            posicionActual = 0;
        }
        
        return getPaginaActual();
    }

    public void reset() {
        posicionActual = 0;
    }

    public void irAUltimaPagina() {
        if (!lista.isEmpty()) {
            int ultimaPosicion = ((lista.size() - 1) / tamanoPagina) * tamanoPagina;
            posicionActual = ultimaPosicion;
        }
    }

    public int getNumeroPaginaActual() {
        if (lista.isEmpty()) {
            return 0;
        }
        return (posicionActual / tamanoPagina) + 1;
    }

    public int getTotalPaginas() {
        if (lista.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil((double) lista.size() / tamanoPagina);
    }

    public String getInfoPagina() {
        if (lista.isEmpty()) {
            return "No hay residuos para mostrar";
        }
        
        int inicio = posicionActual + 1;
        int fin = Math.min(posicionActual + tamanoPagina, lista.size());
        
        return String.format("Página %d de %d (mostrando %d-%d de %d residuos)",
                getNumeroPaginaActual(),
                getTotalPaginas(),
                inicio,
                fin,
                lista.size());
    }

    public void irAPagina(int numeroPagina) {
        if (numeroPagina < 1 || numeroPagina > getTotalPaginas()) {
            throw new IllegalArgumentException(
                "Página inválida. Debe estar entre 1 y " + getTotalPaginas()
            );
        }
        
        posicionActual = (numeroPagina - 1) * tamanoPagina;
    }

    public int getPosicionActual() {
        return posicionActual;
    }
}
package dev.andreewcodes.ecotrack.dominio;

/**
 * Tipos de residuo que gestiona EcoTrack, cada uno con su prioridad ambiental
 * por defecto (1 = baja, 5 = crítica).
 *
 * @author Paúl Rodríguez
 */
public enum TipoResiduo {
    ORGANICO(3),
    PLASTICO(3),
    VIDRIO(2),
    PAPEL(1),
    METAL(2),
    ELECTRONICO(4),
    PELIGROSO(5),
    OTROS(1);

    private final int prioridadPorDefecto;

    TipoResiduo(int prioridadPorDefecto) {
        this.prioridadPorDefecto = prioridadPorDefecto;
    }

    /** Prioridad que se asigna al registrar un residuo si el usuario no indica otra. */
    public int getPrioridadPorDefecto() {
        return prioridadPorDefecto;
    }
}

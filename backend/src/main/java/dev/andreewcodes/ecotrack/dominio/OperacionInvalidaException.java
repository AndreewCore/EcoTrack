package dev.andreewcodes.ecotrack.dominio;

/** Regla de negocio incumplida (p. ej. despachar sin vehículos o editar un residuo ya recogido). */
public class OperacionInvalidaException extends RuntimeException {

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}

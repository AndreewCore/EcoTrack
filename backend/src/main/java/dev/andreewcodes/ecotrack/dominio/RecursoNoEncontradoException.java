package dev.andreewcodes.ecotrack.dominio;

/** Se pidió un residuo o vehículo que no existe. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

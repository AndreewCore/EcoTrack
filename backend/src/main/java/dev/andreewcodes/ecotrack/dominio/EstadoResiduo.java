package dev.andreewcodes.ecotrack.dominio;

/** Etapas por las que pasa un residuo dentro del flujo de recolección. */
public enum EstadoResiduo {
    /** Registrado y esperando en su zona a que un vehículo lo recoja. */
    PENDIENTE,
    /** Recogido por un vehículo y apilado en el centro de reciclaje. */
    EN_CENTRO,
    /** Procesado por el centro de reciclaje. */
    PROCESADO
}

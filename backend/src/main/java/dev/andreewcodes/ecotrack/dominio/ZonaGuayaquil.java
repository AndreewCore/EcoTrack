package dev.andreewcodes.ecotrack.dominio;

/**
 * Zonas de recolección de Guayaquil y Samborondón.
 *
 * @author Paúl Rodríguez
 */
public enum ZonaGuayaquil {
    FORTIN_MONTE_SINAI("Fortín / Monte Sinaí"),
    VERGELES("Vergeles"),
    SAUCES_SAMANES("Sauces / Samanes"),
    BASTION("Bastión"),
    VIA_COSTA_1("Vía a la Costa 1"),
    VIA_COSTA_2("Vía a la Costa 2"),
    URDESA_KENNEDY("Urdesa / Kennedy"),
    SAN_FELIPE_MAPASINGUE("San Felipe / Mapasingue"),
    SAMBORONDON("Samborondón"),
    PROSPERINA_CEIBOS("Prosperina / Ceibos"),
    PORTETE("Portete"),
    PARQUE_CENTENARIO("Parque Centenario"),
    ISLA_TRINITARIA("Isla Trinitaria"),
    ISLA_MOCOLI("Isla Mocolí"),
    GUASMO("Guasmo"),
    FAE("FAE"),
    ENTRE_RIOS_PUNTILLA("Entre Ríos / La Puntilla"),
    CENTRO("Centro de Guayaquil");

    private final String nombre;

    ZonaGuayaquil(String nombre) {
        this.nombre = nombre;
    }

    /** Nombre legible para mostrar en la interfaz. */
    public String getNombre() {
        return nombre;
    }
}

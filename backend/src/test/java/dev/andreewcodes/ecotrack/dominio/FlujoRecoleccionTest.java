package dev.andreewcodes.ecotrack.dominio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlujoRecoleccionTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 12);
    private EcoTrackData datos;

    @BeforeEach
    void preparar() {
        datos = new EcoTrackData(3);
    }

    private Zona zona(ZonaGuayaquil z) {
        return datos.getListaZonas().buscarPorZonaGuayaquil(z);
    }

    @Test
    void registrarUsaLaPrioridadDelTipoYSumaPendiente() {
        Residuo r = datos.registrarResiduo("Pilas", TipoResiduo.PELIGROSO, 10, ZonaGuayaquil.FAE, null);
        assertThat(r.getPrioridad()).isEqualTo(5);
        assertThat(zona(ZonaGuayaquil.FAE).getPesoPendiente()).isEqualTo(10);
        assertThat(zona(ZonaGuayaquil.FAE).calcularUtilidadAmbiental()).isEqualTo(-10);
    }

    @Test
    void despachaElVehiculoDeLaZonaMasCriticaYApilaEnElCentro() {
        datos.registrarResiduo("Poco", TipoResiduo.PAPEL, 50, ZonaGuayaquil.CENTRO, null);
        datos.registrarResiduo("Mucho", TipoResiduo.ORGANICO, 400, ZonaGuayaquil.GUASMO, null);
        datos.registrarResiduo("Urgente", TipoResiduo.PELIGROSO, 30, ZonaGuayaquil.GUASMO, null);
        datos.getDespacho().agregarVehiculo(new Vehiculo("A", ZonaGuayaquil.CENTRO, 1000));
        datos.getDespacho().agregarVehiculo(new Vehiculo("B", ZonaGuayaquil.GUASMO, 1000));

        ResultadoDespacho r = datos.despachar(HOY);

        assertThat(r.vehiculo().getId()).isEqualTo("B");
        // Sube primero lo más prioritario; al descargar en la pila, lo último queda en el tope.
        assertThat(r.residuos()).extracting(Residuo::getNombre).containsExactly("Urgente", "Mucho");
        assertThat(datos.getCentroReciclaje().verProximoResiduo().getNombre()).isEqualTo("Mucho");
        assertThat(r.residuos()).allSatisfy(x -> {
            assertThat(x.getEstado()).isEqualTo(EstadoResiduo.EN_CENTRO);
            assertThat(x.getFechaRecoleccion()).isEqualTo(HOY);
        });
        assertThat(zona(ZonaGuayaquil.GUASMO).getPesoPendiente()).isZero();
        assertThat(zona(ZonaGuayaquil.GUASMO).getPesoRecolectado()).isEqualTo(430);
        assertThat(r.vehiculo().estaDisponible()).isTrue();
    }

    @Test
    void respetaLaCapacidadDelVehiculoYAvisa() {
        datos.registrarResiduo("Grande", TipoResiduo.OTROS, 300, ZonaGuayaquil.BASTION, null);
        datos.registrarResiduo("Chico", TipoResiduo.OTROS, 50, ZonaGuayaquil.BASTION, null);
        datos.getDespacho().agregarVehiculo(new Vehiculo("C", ZonaGuayaquil.BASTION, 100));

        ResultadoDespacho r = datos.despachar(HOY);

        assertThat(r.residuos()).extracting(Residuo::getNombre).containsExactly("Chico");
        assertThat(r.aviso()).contains("capacidad");
    }

    @Test
    void elCentroLlenoBloqueaElDespachoYProcesarEsLifo() {
        for (int i = 1; i <= 4; i++) {
            datos.registrarResiduo("R" + i, TipoResiduo.PAPEL, i, ZonaGuayaquil.PORTETE, 1);
        }
        datos.getDespacho().agregarVehiculo(new Vehiculo("D", ZonaGuayaquil.PORTETE, 1000));

        ResultadoDespacho r = datos.despachar(HOY);
        assertThat(r.residuos()).hasSize(3);
        assertThat(r.aviso()).contains("centro");
        assertThatThrownBy(() -> datos.despachar(HOY)).isInstanceOf(OperacionInvalidaException.class);

        Residuo tope = datos.getCentroReciclaje().verProximoResiduo();
        assertThat(datos.procesarSiguiente()).isSameAs(tope);
        assertThat(tope.getEstado()).isEqualTo(EstadoResiduo.PROCESADO);
        assertThat(datos.getCentroReciclaje().getResiduosProcesados()).isEqualTo(1);
    }

    @Test
    void noSePuedeEditarNiBorrarLoQueEstaEnElCentro() {
        Residuo r = datos.registrarResiduo("X", TipoResiduo.METAL, 5, ZonaGuayaquil.FAE, null);
        datos.getDespacho().agregarVehiculo(new Vehiculo("E", ZonaGuayaquil.FAE, 10));
        datos.despachar(HOY);

        assertThatThrownBy(() -> datos.actualizarResiduo(r.getId(), "Y", TipoResiduo.METAL, 5, ZonaGuayaquil.FAE, 2))
                .isInstanceOf(OperacionInvalidaException.class);
        assertThatThrownBy(() -> datos.eliminarResiduo(r.getId())).isInstanceOf(OperacionInvalidaException.class);

        datos.procesarSiguiente();
        datos.eliminarResiduo(r.getId());
        assertThat(datos.getListaResiduos().isEmpty()).isTrue();
    }

    @Test
    void sinVehiculosOSinPendientesNoHayDespacho() {
        assertThatThrownBy(() -> datos.despachar(HOY)).hasMessageContaining("No hay vehículos");
        datos.getDespacho().agregarVehiculo(new Vehiculo("F", ZonaGuayaquil.FAE, 10));
        assertThatThrownBy(() -> datos.despachar(HOY)).hasMessageContaining("pendientes");
        assertThatThrownBy(() -> datos.getDespacho().agregarVehiculo(new Vehiculo("f", ZonaGuayaquil.CENTRO, 5)))
                .isInstanceOf(OperacionInvalidaException.class);
    }

    @Test
    void estadisticasPorTipoYZona() {
        datos.registrarResiduo("A", TipoResiduo.PLASTICO, 10, ZonaGuayaquil.CENTRO, null);
        datos.registrarResiduo("B", TipoResiduo.PLASTICO, 20, ZonaGuayaquil.GUASMO, null);
        datos.registrarResiduo("C", TipoResiduo.VIDRIO, 5, ZonaGuayaquil.GUASMO, null);
        EstadisticasMap stats = new EstadisticasMap(datos.getListaResiduos(), datos.getListaZonas());
        stats.calcularEstadisticas();

        assertThat(stats.getPesoTotalPorTipo().get(TipoResiduo.PLASTICO)).isEqualTo(30.0);
        assertThat(stats.getCantidadPorTipo().get(TipoResiduo.VIDRIO)).isEqualTo(1);
        assertThat(stats.getTipoMasComun()).isEqualTo(TipoResiduo.PLASTICO);
        assertThat(stats.getZonaMayorPesoPendiente()).isEqualTo(ZonaGuayaquil.GUASMO);
        assertThat(stats.getZonaPeorUtilidad()).isEqualTo(ZonaGuayaquil.GUASMO);
        assertThat(stats.getPesoPromedio()).isEqualTo(35.0 / 3);
    }
}

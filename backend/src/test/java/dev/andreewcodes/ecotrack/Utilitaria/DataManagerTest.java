package dev.andreewcodes.ecotrack.Utilitaria;

import static org.assertj.core.api.Assertions.assertThat;

import dev.andreewcodes.ecotrack.dominio.EcoTrackData;
import dev.andreewcodes.ecotrack.dominio.EstadoResiduo;
import dev.andreewcodes.ecotrack.dominio.Residuo;
import dev.andreewcodes.ecotrack.dominio.TipoResiduo;
import dev.andreewcodes.ecotrack.dominio.Vehiculo;
import dev.andreewcodes.ecotrack.dominio.ZonaGuayaquil;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DataManagerTest {

    @TempDir
    Path dir;

    @Test
    void guardarYCargarConservaElEstadoCompleto() {
        EcoTrackData original = new EcoTrackData();
        original.registrarResiduo("Cartón, \"seco\"", TipoResiduo.PAPEL, 12.5, ZonaGuayaquil.CENTRO, 4);
        original.registrarResiduo("Latas", TipoResiduo.METAL, 3, ZonaGuayaquil.CENTRO, null);
        original.registrarResiduo("Vidrio", TipoResiduo.VIDRIO, 7, ZonaGuayaquil.FAE, null);
        original.getDespacho().agregarVehiculo(new Vehiculo("V-1", ZonaGuayaquil.CENTRO, 100));
        original.despachar(LocalDate.of(2026, 1, 2));
        original.procesarSiguiente();
        new DataManager(dir).guardar(original);

        EcoTrackData cargado = new EcoTrackData();
        new DataManager(dir).cargar(cargado);

        assertThat(cargado.getListaResiduos().size()).isEqualTo(3);
        Residuo carton = cargado.getListaResiduos().buscarPorId(1);
        assertThat(carton.getNombre()).isEqualTo("Cartón, \"seco\"");
        assertThat(carton.getPeso()).isEqualTo(12.5);
        assertThat(carton.getPrioridad()).isEqualTo(4);
        assertThat(cargado.getCentroReciclaje().getResiduos().getTamaño()).isEqualTo(1);
        assertThat(cargado.getCentroReciclaje().getResiduosProcesados()).isEqualTo(1);
        assertThat(cargado.getDespacho().buscar("V-1")).isNotNull();
        // La numeración continúa tras el mayor id cargado.
        assertThat(cargado.registrarResiduo("Nuevo", TipoResiduo.OTROS, 1, ZonaGuayaquil.FAE, null).getId()).isEqualTo(4);
    }

    @Test
    void leeElFormatoAntiguoDeSieteColumnasYReportaLineasInvalidas() {
        String csv = """
                ID,Nombre,Tipo,Peso,FechaRecoleccion,Zona,Prioridad
                1,Botella,PLASTICO,2.0,,BASTION,0
                2,Caja,PAPEL,1.0,2025-05-01,CENTRO,2
                3,Mal,NO_EXISTE,1.0,,CENTRO,2
                4,Negativo,PAPEL,-3,,CENTRO,2
                """;
        DataManager.ResultadoLectura r = DataManager.leerResiduos(csv);
        assertThat(r.residuos()).hasSize(2);
        assertThat(r.residuos().get(0).getPrioridad()).isEqualTo(3);
        assertThat(r.residuos().get(0).getEstado()).isEqualTo(EstadoResiduo.PENDIENTE);
        assertThat(r.residuos().get(1).getEstado()).isEqualTo(EstadoResiduo.PROCESADO);
        assertThat(r.errores()).hasSize(2).first().asString().startsWith("Línea 4");
    }

    @Test
    void dividirRespetaComillas() {
        assertThat(DataManager.dividir("1,\"a,b\",\"c\"\"d\",")).containsExactly("1", "a,b", "c\"d", "");
        assertThat(DataManager.escapar("x,y")).isEqualTo("\"x,y\"");
    }
}

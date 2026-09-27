package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.CostoDeIncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.PeriodoDeFacturacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PeriodoDeFacturacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodoDeFacturacion.class);
        PeriodoDeFacturacion periodoDeFacturacion1 = getPeriodoDeFacturacionSample1();
        PeriodoDeFacturacion periodoDeFacturacion2 = new PeriodoDeFacturacion();
        assertThat(periodoDeFacturacion1).isNotEqualTo(periodoDeFacturacion2);

        periodoDeFacturacion2.setId(periodoDeFacturacion1.getId());
        assertThat(periodoDeFacturacion1).isEqualTo(periodoDeFacturacion2);

        periodoDeFacturacion2 = getPeriodoDeFacturacionSample2();
        assertThat(periodoDeFacturacion1).isNotEqualTo(periodoDeFacturacion2);
    }

    @Test
    void costoTest() {
        PeriodoDeFacturacion periodoDeFacturacion = getPeriodoDeFacturacionRandomSampleGenerator();
        CostoDeIncidente costoDeIncidenteBack = getCostoDeIncidenteRandomSampleGenerator();

        periodoDeFacturacion.addCosto(costoDeIncidenteBack);
        assertThat(periodoDeFacturacion.getCostos()).containsOnly(costoDeIncidenteBack);
        assertThat(costoDeIncidenteBack.getPeriodo()).isEqualTo(periodoDeFacturacion);

        periodoDeFacturacion.removeCosto(costoDeIncidenteBack);
        assertThat(periodoDeFacturacion.getCostos()).doesNotContain(costoDeIncidenteBack);
        assertThat(costoDeIncidenteBack.getPeriodo()).isNull();

        periodoDeFacturacion.costos(new HashSet<>(Set.of(costoDeIncidenteBack)));
        assertThat(periodoDeFacturacion.getCostos()).containsOnly(costoDeIncidenteBack);
        assertThat(costoDeIncidenteBack.getPeriodo()).isEqualTo(periodoDeFacturacion);

        periodoDeFacturacion.setCostos(new HashSet<>());
        assertThat(periodoDeFacturacion.getCostos()).doesNotContain(costoDeIncidenteBack);
        assertThat(costoDeIncidenteBack.getPeriodo()).isNull();
    }
}

package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.CostoDeIncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.IncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.PeriodoDeFacturacionTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostoDeIncidenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostoDeIncidente.class);
        CostoDeIncidente costoDeIncidente1 = getCostoDeIncidenteSample1();
        CostoDeIncidente costoDeIncidente2 = new CostoDeIncidente();
        assertThat(costoDeIncidente1).isNotEqualTo(costoDeIncidente2);

        costoDeIncidente2.setId(costoDeIncidente1.getId());
        assertThat(costoDeIncidente1).isEqualTo(costoDeIncidente2);

        costoDeIncidente2 = getCostoDeIncidenteSample2();
        assertThat(costoDeIncidente1).isNotEqualTo(costoDeIncidente2);
    }

    @Test
    void incidenteTest() {
        CostoDeIncidente costoDeIncidente = getCostoDeIncidenteRandomSampleGenerator();
        Incidente incidenteBack = getIncidenteRandomSampleGenerator();

        costoDeIncidente.setIncidente(incidenteBack);
        assertThat(costoDeIncidente.getIncidente()).isEqualTo(incidenteBack);

        costoDeIncidente.incidente(null);
        assertThat(costoDeIncidente.getIncidente()).isNull();
    }

    @Test
    void servicioTest() {
        CostoDeIncidente costoDeIncidente = getCostoDeIncidenteRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        costoDeIncidente.setServicio(servicioBack);
        assertThat(costoDeIncidente.getServicio()).isEqualTo(servicioBack);

        costoDeIncidente.servicio(null);
        assertThat(costoDeIncidente.getServicio()).isNull();
    }

    @Test
    void periodoTest() {
        CostoDeIncidente costoDeIncidente = getCostoDeIncidenteRandomSampleGenerator();
        PeriodoDeFacturacion periodoDeFacturacionBack = getPeriodoDeFacturacionRandomSampleGenerator();

        costoDeIncidente.setPeriodo(periodoDeFacturacionBack);
        assertThat(costoDeIncidente.getPeriodo()).isEqualTo(periodoDeFacturacionBack);

        costoDeIncidente.periodo(null);
        assertThat(costoDeIncidente.getPeriodo()).isNull();
    }
}

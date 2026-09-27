package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.TarifaDeServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifaDeServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifaDeServicio.class);
        TarifaDeServicio tarifaDeServicio1 = getTarifaDeServicioSample1();
        TarifaDeServicio tarifaDeServicio2 = new TarifaDeServicio();
        assertThat(tarifaDeServicio1).isNotEqualTo(tarifaDeServicio2);

        tarifaDeServicio2.setId(tarifaDeServicio1.getId());
        assertThat(tarifaDeServicio1).isEqualTo(tarifaDeServicio2);

        tarifaDeServicio2 = getTarifaDeServicioSample2();
        assertThat(tarifaDeServicio1).isNotEqualTo(tarifaDeServicio2);
    }

    @Test
    void servicioTest() {
        TarifaDeServicio tarifaDeServicio = getTarifaDeServicioRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        tarifaDeServicio.setServicio(servicioBack);
        assertThat(tarifaDeServicio.getServicio()).isEqualTo(servicioBack);

        tarifaDeServicio.servicio(null);
        assertThat(tarifaDeServicio.getServicio()).isNull();
    }
}

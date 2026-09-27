package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeriodoDeFacturacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodoDeFacturacionDTO.class);
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO1 = new PeriodoDeFacturacionDTO();
        periodoDeFacturacionDTO1.setId(1L);
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO2 = new PeriodoDeFacturacionDTO();
        assertThat(periodoDeFacturacionDTO1).isNotEqualTo(periodoDeFacturacionDTO2);
        periodoDeFacturacionDTO2.setId(periodoDeFacturacionDTO1.getId());
        assertThat(periodoDeFacturacionDTO1).isEqualTo(periodoDeFacturacionDTO2);
        periodoDeFacturacionDTO2.setId(2L);
        assertThat(periodoDeFacturacionDTO1).isNotEqualTo(periodoDeFacturacionDTO2);
        periodoDeFacturacionDTO1.setId(null);
        assertThat(periodoDeFacturacionDTO1).isNotEqualTo(periodoDeFacturacionDTO2);
    }
}

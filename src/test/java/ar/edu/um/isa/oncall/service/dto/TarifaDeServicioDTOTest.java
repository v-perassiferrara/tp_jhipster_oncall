package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifaDeServicioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifaDeServicioDTO.class);
        TarifaDeServicioDTO tarifaDeServicioDTO1 = new TarifaDeServicioDTO();
        tarifaDeServicioDTO1.setId(1L);
        TarifaDeServicioDTO tarifaDeServicioDTO2 = new TarifaDeServicioDTO();
        assertThat(tarifaDeServicioDTO1).isNotEqualTo(tarifaDeServicioDTO2);
        tarifaDeServicioDTO2.setId(tarifaDeServicioDTO1.getId());
        assertThat(tarifaDeServicioDTO1).isEqualTo(tarifaDeServicioDTO2);
        tarifaDeServicioDTO2.setId(2L);
        assertThat(tarifaDeServicioDTO1).isNotEqualTo(tarifaDeServicioDTO2);
        tarifaDeServicioDTO1.setId(null);
        assertThat(tarifaDeServicioDTO1).isNotEqualTo(tarifaDeServicioDTO2);
    }
}

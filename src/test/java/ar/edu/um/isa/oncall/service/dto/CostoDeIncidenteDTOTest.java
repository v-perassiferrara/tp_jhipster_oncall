package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostoDeIncidenteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostoDeIncidenteDTO.class);
        CostoDeIncidenteDTO costoDeIncidenteDTO1 = new CostoDeIncidenteDTO();
        costoDeIncidenteDTO1.setId(1L);
        CostoDeIncidenteDTO costoDeIncidenteDTO2 = new CostoDeIncidenteDTO();
        assertThat(costoDeIncidenteDTO1).isNotEqualTo(costoDeIncidenteDTO2);
        costoDeIncidenteDTO2.setId(costoDeIncidenteDTO1.getId());
        assertThat(costoDeIncidenteDTO1).isEqualTo(costoDeIncidenteDTO2);
        costoDeIncidenteDTO2.setId(2L);
        assertThat(costoDeIncidenteDTO1).isNotEqualTo(costoDeIncidenteDTO2);
        costoDeIncidenteDTO1.setId(null);
        assertThat(costoDeIncidenteDTO1).isNotEqualTo(costoDeIncidenteDTO2);
    }
}

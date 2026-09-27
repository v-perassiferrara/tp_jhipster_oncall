package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.PeriodoDeFacturacionAsserts.*;
import static ar.edu.um.isa.oncall.domain.PeriodoDeFacturacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeriodoDeFacturacionMapperTest {

    private PeriodoDeFacturacionMapper periodoDeFacturacionMapper;

    @BeforeEach
    void setUp() {
        periodoDeFacturacionMapper = new PeriodoDeFacturacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPeriodoDeFacturacionSample1();
        var actual = periodoDeFacturacionMapper.toEntity(periodoDeFacturacionMapper.toDto(expected));
        assertPeriodoDeFacturacionAllPropertiesEquals(expected, actual);
    }
}

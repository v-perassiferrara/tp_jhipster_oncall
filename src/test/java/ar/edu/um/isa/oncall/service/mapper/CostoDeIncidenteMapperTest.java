package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.CostoDeIncidenteAsserts.*;
import static ar.edu.um.isa.oncall.domain.CostoDeIncidenteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CostoDeIncidenteMapperTest {

    private CostoDeIncidenteMapper costoDeIncidenteMapper;

    @BeforeEach
    void setUp() {
        costoDeIncidenteMapper = new CostoDeIncidenteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCostoDeIncidenteSample1();
        var actual = costoDeIncidenteMapper.toEntity(costoDeIncidenteMapper.toDto(expected));
        assertCostoDeIncidenteAllPropertiesEquals(expected, actual);
    }
}

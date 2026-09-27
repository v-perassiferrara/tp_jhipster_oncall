package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.TarifaDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.domain.TarifaDeServicioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TarifaDeServicioMapperTest {

    private TarifaDeServicioMapper tarifaDeServicioMapper;

    @BeforeEach
    void setUp() {
        tarifaDeServicioMapper = new TarifaDeServicioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTarifaDeServicioSample1();
        var actual = tarifaDeServicioMapper.toEntity(tarifaDeServicioMapper.toDto(expected));
        assertTarifaDeServicioAllPropertiesEquals(expected, actual);
    }
}

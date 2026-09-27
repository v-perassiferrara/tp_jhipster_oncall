package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import ar.edu.um.isa.oncall.service.dto.PeriodoDeFacturacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeriodoDeFacturacion} and its DTO {@link PeriodoDeFacturacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeriodoDeFacturacionMapper extends EntityMapper<PeriodoDeFacturacionDTO, PeriodoDeFacturacion> {}

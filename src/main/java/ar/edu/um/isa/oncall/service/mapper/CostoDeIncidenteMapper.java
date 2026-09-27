package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.CostoDeIncidente;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.service.dto.CostoDeIncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.PeriodoDeFacturacionDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CostoDeIncidente} and its DTO {@link CostoDeIncidenteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CostoDeIncidenteMapper extends EntityMapper<CostoDeIncidenteDTO, CostoDeIncidente> {
    @Mapping(target = "incidente", source = "incidente", qualifiedByName = "incidenteTitulo")
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioNombre")
    @Mapping(target = "periodo", source = "periodo", qualifiedByName = "periodoDeFacturacionFechaInicio")
    CostoDeIncidenteDTO toDto(CostoDeIncidente s);

    @Named("incidenteTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    IncidenteDTO toDtoIncidenteTitulo(Incidente incidente);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);

    @Named("periodoDeFacturacionFechaInicio")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    PeriodoDeFacturacionDTO toDtoPeriodoDeFacturacionFechaInicio(PeriodoDeFacturacion periodoDeFacturacion);
}

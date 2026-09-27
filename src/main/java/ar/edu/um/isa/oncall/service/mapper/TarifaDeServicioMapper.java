package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.TarifaDeServicio;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import ar.edu.um.isa.oncall.service.dto.TarifaDeServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TarifaDeServicio} and its DTO {@link TarifaDeServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface TarifaDeServicioMapper extends EntityMapper<TarifaDeServicioDTO, TarifaDeServicio> {
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioNombre")
    TarifaDeServicioDTO toDto(TarifaDeServicio s);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);
}

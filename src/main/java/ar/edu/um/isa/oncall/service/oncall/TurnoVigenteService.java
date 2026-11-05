package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.Rotacion;
import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import ar.edu.um.isa.oncall.repository.ServicioRepository;
import ar.edu.um.isa.oncall.repository.oncall.GuardiaLookupRepository;
import ar.edu.um.isa.oncall.service.ServicioInexistenteException;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaVigenteDTO;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaVigenteDTO.Estado;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaVigenteDTO.GuardiaPorRotacion;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regla C. Los criterios de selección están registrados en el ADR 002. */
@Service
@Transactional(readOnly = true)
public class TurnoVigenteService {
    private static final ZoneId MENDOZA = ZoneId.of("America/Argentina/Mendoza");
    private static final Comparator<TurnoDeGuardia> PRIORIDAD = Comparator
        .comparing((TurnoDeGuardia t) -> Boolean.TRUE.equals(t.getEsReemplazo()))
        .thenComparing(TurnoDeGuardia::getDesde)
        .thenComparing(TurnoDeGuardia::getId);

    private final ServicioRepository servicios;
    private final GuardiaLookupRepository guardias;

    public TurnoVigenteService(ServicioRepository servicios, GuardiaLookupRepository guardias) {
        this.servicios = servicios;
        this.guardias = guardias;
    }

    public GuardiaVigenteDTO consultar(Long servicioId, Instant instante) {
        Objects.requireNonNull(instante, "El instante es obligatorio");
        var servicio = servicios.findOneWithEagerRelationships(servicioId)
            .orElseThrow(() -> new ServicioInexistenteException(servicioId));
        Long equipoId = servicio.getEquipo().getId();
        List<Rotacion> rotaciones = guardias.buscarRotacionesActivas(equipoId);
        Map<Long, TurnoDeGuardia> elegidos = guardias.buscarTurnosVigentes(equipoId, instante).stream()
            .collect(Collectors.toMap(t -> t.getRotacion().getId(), t -> t, BinaryOperator.maxBy(PRIORIDAD)));
        List<GuardiaPorRotacion> resultados = rotaciones.stream()
            .map(r -> resultado(r, elegidos.get(r.getId())))
            .toList();
        boolean hayCobertura = resultados.stream().anyMatch(r -> r.estado() == Estado.CON_COBERTURA);
        return new GuardiaVigenteDTO(servicioId, instante, instante.atZone(MENDOZA), hayCobertura, resultados);
    }

    private GuardiaPorRotacion resultado(Rotacion rotacion, TurnoDeGuardia turno) {
        if (turno == null) {
            return new GuardiaPorRotacion(rotacion.getId(), rotacion.getNombre(), Estado.SIN_COBERTURA,
                null, null, null, null, null, false);
        }
        return new GuardiaPorRotacion(rotacion.getId(), rotacion.getNombre(), Estado.CON_COBERTURA,
            turno.getId(), turno.getResponsable().getId(), turno.getResponsable().getLogin(),
            turno.getDesde(), turno.getHasta(), Boolean.TRUE.equals(turno.getEsReemplazo()));
    }
}

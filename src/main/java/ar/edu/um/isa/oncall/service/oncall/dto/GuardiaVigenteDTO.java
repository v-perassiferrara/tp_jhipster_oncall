package ar.edu.um.isa.oncall.service.oncall.dto;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/** Respuesta acotada; nunca serializa una entidad User. */
public record GuardiaVigenteDTO(
    Long servicioId,
    Instant instante,
    ZonedDateTime horaMendoza,
    boolean hayCobertura,
    List<GuardiaPorRotacion> rotaciones
) {
    public enum Estado { CON_COBERTURA, SIN_COBERTURA }

    public record GuardiaPorRotacion(
        Long rotacionId,
        String rotacionNombre,
        Estado estado,
        Long turnoId,
        Long responsableId,
        String responsableLogin,
        Instant desde,
        Instant hasta,
        boolean reemplazo
    ) {}
}

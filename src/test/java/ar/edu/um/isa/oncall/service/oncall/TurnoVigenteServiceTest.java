package ar.edu.um.isa.oncall.service.oncall;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.um.isa.oncall.domain.*;
import ar.edu.um.isa.oncall.repository.ServicioRepository;
import ar.edu.um.isa.oncall.repository.oncall.GuardiaLookupRepository;
import ar.edu.um.isa.oncall.service.ServicioInexistenteException;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaVigenteDTO.Estado;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TurnoVigenteServiceTest {
    private static final Instant T = Instant.parse("2026-11-05T12:00:00Z");
    @Mock ServicioRepository servicios;
    @Mock GuardiaLookupRepository guardias;
    private TurnoVigenteService regla;
    private final Rotacion rotacion = new Rotacion().id(10L).nombre("Principal");

    @BeforeEach
    void preparar() {
        regla = new TurnoVigenteService(servicios, guardias);
    }

    private void catalogo() {
        when(servicios.findOneWithEagerRelationships(1L))
            .thenReturn(Optional.of(new Servicio().id(1L).equipo(new Equipo().id(7L))));
    }

    private TurnoDeGuardia turno(long id, Boolean reemplazo, long segundosDesde) {
        User persona = new User();
        persona.setId(id + 100);
        persona.setLogin("persona" + id);
        return new TurnoDeGuardia().id(id).rotacion(rotacion).responsable(persona)
            .esReemplazo(reemplazo).desde(T.minusSeconds(segundosDesde)).hasta(T.plusSeconds(3600));
    }

    @ParameterizedTest
    @CsvSource({"true,false,7200,3600,1", "false,false,7200,3600,2", "false,false,3600,3600,2"})
    void aplicaPrioridadSinDependerDelOrden(boolean reemplazo1, boolean reemplazo2, long inicio1, long inicio2, long ganador) {
        catalogo();
        var primero = turno(1L, reemplazo1, inicio1);
        var segundo = turno(2L, reemplazo2, inicio2);
        when(guardias.buscarRotacionesActivas(7L)).thenReturn(List.of(rotacion));
        when(guardias.buscarTurnosVigentes(7L, T)).thenReturn(List.of(primero, segundo), List.of(segundo, primero));
        for (int i = 0; i < 2; i++) {
            var respuesta = regla.consultar(1L, T);
            assertThat(respuesta.hayCobertura()).isTrue();
            assertThat(respuesta.rotaciones().getFirst().turnoId()).isEqualTo(ganador);
        }
    }

    @Test
    void reemplazoNuloEquivaleARegular() {
        catalogo();
        when(guardias.buscarRotacionesActivas(7L)).thenReturn(List.of(rotacion));
        when(guardias.buscarTurnosVigentes(7L, T)).thenReturn(List.of(turno(1, null, 3600), turno(2, true, 7200)));
        assertThat(regla.consultar(1L, T).rotaciones().getFirst().turnoId()).isEqualTo(2L);
    }

    @Test
    void informaHuecoSinAsignarRespaldo() {
        catalogo();
        when(guardias.buscarRotacionesActivas(7L)).thenReturn(List.of(rotacion));
        when(guardias.buscarTurnosVigentes(7L, T)).thenReturn(List.of());
        var respuesta = regla.consultar(1L, T);
        assertThat(respuesta.hayCobertura()).isFalse();
        assertThat(respuesta.rotaciones()).singleElement().satisfies(r -> {
            assertThat(r.estado()).isEqualTo(Estado.SIN_COBERTURA);
            assertThat(r.responsableId()).isNull();
            assertThat(r.turnoId()).isNull();
        });
    }

    @Test
    void ausenciaDeRotacionesDevuelveColeccionVacia() {
        catalogo();
        when(guardias.buscarRotacionesActivas(7L)).thenReturn(List.of());
        when(guardias.buscarTurnosVigentes(7L, T)).thenReturn(List.of());
        var respuesta = regla.consultar(1L, T);
        assertThat(respuesta.rotaciones()).isEmpty();
        assertThat(respuesta.hayCobertura()).isFalse();
    }

    @Test
    void devuelveCadaRotacionInclusoSiUnaNoTieneCobertura() {
        catalogo();
        var segunda = new Rotacion().id(11L).nombre("Otra");
        when(guardias.buscarRotacionesActivas(7L)).thenReturn(List.of(rotacion, segunda));
        when(guardias.buscarTurnosVigentes(7L, T)).thenReturn(List.of(turno(1, false, 3600)));
        var respuesta = regla.consultar(1L, T);
        assertThat(respuesta.rotaciones()).extracting(r -> r.estado())
            .containsExactly(Estado.CON_COBERTURA, Estado.SIN_COBERTURA);
        assertThat(respuesta.hayCobertura()).isTrue();
        assertThat(respuesta.horaMendoza().getHour()).isEqualTo(9);
        assertThat(respuesta.horaMendoza().toInstant()).isEqualTo(T);
    }

    @Test
    void servicioInexistenteNoBuscaGuardias() {
        when(servicios.findOneWithEagerRelationships(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> regla.consultar(999L, T)).isInstanceOf(ServicioInexistenteException.class);
        verifyNoInteractions(guardias);
    }
}

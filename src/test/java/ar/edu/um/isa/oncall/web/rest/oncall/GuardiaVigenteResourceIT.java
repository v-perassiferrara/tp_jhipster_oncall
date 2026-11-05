package ar.edu.um.isa.oncall.web.rest.oncall;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.*;
import ar.edu.um.isa.oncall.domain.enumeration.*;
import ar.edu.um.isa.oncall.repository.oncall.GuardiaLookupRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/** HTTP + servicio + JPQL + PostgreSQL; no simula las condiciones temporales de la consulta. */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
class GuardiaVigenteResourceIT {
    private static final Instant T = Instant.parse("2026-11-05T12:00:00Z");
    private static final AtomicInteger SECUENCIA = new AtomicInteger();
    @Autowired EntityManager em;
    @Autowired MockMvc mvc;
    @Autowired GuardiaLookupRepository guardias;
    private Servicio servicio;
    private Rotacion rotacion;
    private User persona;

    @BeforeEach
    void preparar() {
        int n = SECUENCIA.incrementAndGet();
        var equipo = new Equipo().nombre("Guardia equipo " + n).emailContacto("guardia" + n + "@example.test");
        em.persist(equipo);
        servicio = new Servicio().nombre("Guardia servicio " + n).equipo(equipo)
            .criticidad(Criticidad.TIER1).entorno(Entorno.PRODUCCION).activo(true);
        em.persist(servicio);
        rotacion = nuevaRotacion(equipo, true);
        persona = new User();
        persona.setLogin("guardia" + n);
        persona.setPassword("x".repeat(60));
        em.persist(persona);
    }

    private Rotacion nuevaRotacion(Equipo equipo, boolean activa) {
        var r = new Rotacion().nombre("Guardia").tipo(TipoRotacion.DIARIA)
            .zonaHoraria("America/Argentina/Mendoza").activa(activa).equipo(equipo);
        em.persist(r);
        return r;
    }

    private TurnoDeGuardia turno(Rotacion r, Instant desde, Instant hasta) {
        var t = new TurnoDeGuardia().rotacion(r).responsable(persona).desde(desde).hasta(hasta).esReemplazo(false);
        em.persist(t);
        em.flush();
        return t;
    }

    private String ruta() { return "/api/oncall/servicios/" + servicio.getId() + "/guardia"; }

    @ParameterizedTest
    @CsvSource({"-1,false", "0,true", "3599,true", "3600,false"})
    void respetaInicioInclusivoYFinExclusivo(long segundos, boolean cubierta) throws Exception {
        var t = turno(rotacion, T, T.plusSeconds(3600));
        var resultado = mvc.perform(get(ruta()).param("instante", T.plusSeconds(segundos).toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hayCobertura").value(cubierta))
            .andExpect(jsonPath("$.rotaciones[0].estado").value(cubierta ? "CON_COBERTURA" : "SIN_COBERTURA"));
        if (cubierta) {
            resultado.andExpect(jsonPath("$.rotaciones[0].turnoId").value(t.getId().intValue()))
                .andExpect(jsonPath("$.rotaciones[0].responsableLogin").value(persona.getLogin()))
                .andExpect(jsonPath("$.rotaciones[0].password").doesNotExist())
                .andExpect(jsonPath("$.rotaciones[0].email").doesNotExist());
        } else {
            resultado.andExpect(jsonPath("$.rotaciones[0].responsableId").isEmpty());
        }
    }

    @Test
    void enElRelevoSoloQuedaElTurnoEntrante() throws Exception {
        turno(rotacion, T.minusSeconds(3600), T);
        var entrante = turno(rotacion, T, T.plusSeconds(3600));
        assertThat(guardias.buscarTurnosVigentes(servicio.getEquipo().getId(), T))
            .extracting(TurnoDeGuardia::getId).containsExactly(entrante.getId());
        mvc.perform(get(ruta()).param("instante", T.toString())).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones[0].turnoId").value(entrante.getId().intValue()));
    }

    @Test
    void aislaEquiposExcluyeInactivasYConservaRotacionesSinTurno() throws Exception {
        turno(rotacion, T.minusSeconds(3600), T.plusSeconds(3600));
        var sinTurno = nuevaRotacion(servicio.getEquipo(), true);
        turno(nuevaRotacion(servicio.getEquipo(), false), T.minusSeconds(3600), T.plusSeconds(3600));
        var otroEquipo = new Equipo().nombre("Otro " + SECUENCIA.incrementAndGet()).emailContacto("otro@example.test");
        em.persist(otroEquipo);
        turno(nuevaRotacion(otroEquipo, true), T.minusSeconds(3600), T.plusSeconds(3600));
        em.clear();
        mvc.perform(get(ruta()).param("instante", T.toString())).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones.length()").value(2))
            .andExpect(jsonPath("$.rotaciones[0].estado").value("CON_COBERTURA"))
            .andExpect(jsonPath("$.rotaciones[1].rotacionId").value(sinTurno.getId().intValue()))
            .andExpect(jsonPath("$.rotaciones[1].estado").value("SIN_COBERTURA"));
    }

    @Test
    void variasRotacionesPuedenEstarCubiertasALaVez() throws Exception {
        turno(rotacion, T.minusSeconds(3600), T.plusSeconds(3600));
        turno(nuevaRotacion(servicio.getEquipo(), true), T.minusSeconds(3600), T.plusSeconds(3600));
        mvc.perform(get(ruta()).param("instante", T.toString())).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones.length()").value(2))
            .andExpect(jsonPath("$.rotaciones[0].estado").value("CON_COBERTURA"))
            .andExpect(jsonPath("$.rotaciones[1].estado").value("CON_COBERTURA"));
    }

    @Test
    void sinRotacionesActivasNoEsUnError() throws Exception {
        rotacion.setActiva(false);
        em.flush();
        mvc.perform(get(ruta()).param("instante", T.toString())).andExpect(status().isOk())
            .andExpect(jsonPath("$.hayCobertura").value(false))
            .andExpect(jsonPath("$.rotaciones").isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"2026-11-05T12:00:00Z", "2026-11-05T09:00:00-03:00"})
    void offsetsEquivalentesSeleccionanElMismoTurno(String instante) throws Exception {
        var t = turno(rotacion, T, T.plusSeconds(60));
        mvc.perform(get(ruta()).param("instante", instante)).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones[0].turnoId").value(t.getId().intValue()));
    }

    @Test
    void horaRepetidaPorDSTRepresentaDosInstantesDistintos() throws Exception {
        var primero = turno(rotacion, Instant.parse("2026-11-01T05:00:00Z"), Instant.parse("2026-11-01T06:00:00Z"));
        var segundo = turno(rotacion, Instant.parse("2026-11-01T06:00:00Z"), Instant.parse("2026-11-01T07:00:00Z"));
        mvc.perform(get(ruta()).param("instante", "2026-11-01T01:30:00-04:00")).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones[0].turnoId").value(primero.getId().intValue()));
        mvc.perform(get(ruta()).param("instante", "2026-11-01T01:30:00-05:00")).andExpect(status().isOk())
            .andExpect(jsonPath("$.rotaciones[0].turnoId").value(segundo.getId().intValue()));
    }

    @ParameterizedTest
    @CsvSource({"2026-11-05T09:00:00", "fecha-invalida"})
    void rechazaFechaSinOffsetOInvalida(String instante) throws Exception {
        mvc.perform(get(ruta()).param("instante", instante)).andExpect(status().isBadRequest());
    }

    @Test
    void instanteObligatorioYServicioInexistente() throws Exception {
        mvc.perform(get(ruta())).andExpect(status().isBadRequest());
        mvc.perform(get("/api/oncall/servicios/0/guardia").param("instante", T.toString())).andExpect(status().isBadRequest());
        mvc.perform(get("/api/oncall/servicios/9223372036854775807/guardia").param("instante", T.toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void requiereAutenticacion() throws Exception {
        mvc.perform(get(ruta()).param("instante", T.toString())).andExpect(status().isUnauthorized());
    }
}

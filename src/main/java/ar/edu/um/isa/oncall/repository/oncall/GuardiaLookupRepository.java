package ar.edu.um.isa.oncall.repository.oncall;

import ar.edu.um.isa.oncall.domain.Rotacion;
import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/** Consultas de lectura propias: no modifica los repositorios generados. */
public interface GuardiaLookupRepository extends Repository<Rotacion, Long> {
    @Query("select r from Rotacion r where r.equipo.id = :equipoId and r.activa = true order by r.id")
    List<Rotacion> buscarRotacionesActivas(@Param("equipoId") Long equipoId);

    @Query("""
        select t from TurnoDeGuardia t
        join fetch t.rotacion r
        join fetch t.responsable
        where r.equipo.id = :equipoId and r.activa = true
          and t.desde <= :instante and t.hasta > :instante
        """
    )
    List<TurnoDeGuardia> buscarTurnosVigentes(@Param("equipoId") Long equipoId, @Param("instante") Instant instante);
}

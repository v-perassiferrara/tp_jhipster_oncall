package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.CostoDeIncidente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CostoDeIncidente entity.
 */
@Repository
public interface CostoDeIncidenteRepository extends JpaRepository<CostoDeIncidente, Long> {
    default Optional<CostoDeIncidente> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CostoDeIncidente> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CostoDeIncidente> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select costoDeIncidente from CostoDeIncidente costoDeIncidente left join fetch costoDeIncidente.incidente left join fetch costoDeIncidente.servicio left join fetch costoDeIncidente.periodo",
        countQuery = "select count(costoDeIncidente) from CostoDeIncidente costoDeIncidente"
    )
    Page<CostoDeIncidente> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select costoDeIncidente from CostoDeIncidente costoDeIncidente left join fetch costoDeIncidente.incidente left join fetch costoDeIncidente.servicio left join fetch costoDeIncidente.periodo"
    )
    List<CostoDeIncidente> findAllWithToOneRelationships();

    @Query(
        "select costoDeIncidente from CostoDeIncidente costoDeIncidente left join fetch costoDeIncidente.incidente left join fetch costoDeIncidente.servicio left join fetch costoDeIncidente.periodo where costoDeIncidente.id =:id"
    )
    Optional<CostoDeIncidente> findOneWithToOneRelationships(@Param("id") Long id);
}

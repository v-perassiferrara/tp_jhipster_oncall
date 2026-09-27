package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.TarifaDeServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TarifaDeServicio entity.
 */
@Repository
public interface TarifaDeServicioRepository extends JpaRepository<TarifaDeServicio, Long> {
    default Optional<TarifaDeServicio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TarifaDeServicio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TarifaDeServicio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select tarifaDeServicio from TarifaDeServicio tarifaDeServicio left join fetch tarifaDeServicio.servicio",
        countQuery = "select count(tarifaDeServicio) from TarifaDeServicio tarifaDeServicio"
    )
    Page<TarifaDeServicio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tarifaDeServicio from TarifaDeServicio tarifaDeServicio left join fetch tarifaDeServicio.servicio")
    List<TarifaDeServicio> findAllWithToOneRelationships();

    @Query(
        "select tarifaDeServicio from TarifaDeServicio tarifaDeServicio left join fetch tarifaDeServicio.servicio where tarifaDeServicio.id =:id"
    )
    Optional<TarifaDeServicio> findOneWithToOneRelationships(@Param("id") Long id);
}

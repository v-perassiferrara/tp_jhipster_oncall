package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PeriodoDeFacturacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoDeFacturacionRepository extends JpaRepository<PeriodoDeFacturacion, Long> {}

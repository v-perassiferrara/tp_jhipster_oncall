package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import ar.edu.um.isa.oncall.repository.PeriodoDeFacturacionRepository;
import ar.edu.um.isa.oncall.service.dto.PeriodoDeFacturacionDTO;
import ar.edu.um.isa.oncall.service.mapper.PeriodoDeFacturacionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion}.
 */
@Service
@Transactional
public class PeriodoDeFacturacionService {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodoDeFacturacionService.class);

    private final PeriodoDeFacturacionRepository periodoDeFacturacionRepository;

    private final PeriodoDeFacturacionMapper periodoDeFacturacionMapper;

    public PeriodoDeFacturacionService(
        PeriodoDeFacturacionRepository periodoDeFacturacionRepository,
        PeriodoDeFacturacionMapper periodoDeFacturacionMapper
    ) {
        this.periodoDeFacturacionRepository = periodoDeFacturacionRepository;
        this.periodoDeFacturacionMapper = periodoDeFacturacionMapper;
    }

    /**
     * Save a periodoDeFacturacion.
     *
     * @param periodoDeFacturacionDTO the entity to save.
     * @return the persisted entity.
     */
    public PeriodoDeFacturacionDTO save(PeriodoDeFacturacionDTO periodoDeFacturacionDTO) {
        LOG.debug("Request to save PeriodoDeFacturacion : {}", periodoDeFacturacionDTO);
        PeriodoDeFacturacion periodoDeFacturacion = periodoDeFacturacionMapper.toEntity(periodoDeFacturacionDTO);
        periodoDeFacturacion = periodoDeFacturacionRepository.save(periodoDeFacturacion);
        return periodoDeFacturacionMapper.toDto(periodoDeFacturacion);
    }

    /**
     * Update a periodoDeFacturacion.
     *
     * @param periodoDeFacturacionDTO the entity to save.
     * @return the persisted entity.
     */
    public PeriodoDeFacturacionDTO update(PeriodoDeFacturacionDTO periodoDeFacturacionDTO) {
        LOG.debug("Request to update PeriodoDeFacturacion : {}", periodoDeFacturacionDTO);
        PeriodoDeFacturacion periodoDeFacturacion = periodoDeFacturacionMapper.toEntity(periodoDeFacturacionDTO);
        periodoDeFacturacion = periodoDeFacturacionRepository.save(periodoDeFacturacion);
        return periodoDeFacturacionMapper.toDto(periodoDeFacturacion);
    }

    /**
     * Partially update a periodoDeFacturacion.
     *
     * @param periodoDeFacturacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeriodoDeFacturacionDTO> partialUpdate(PeriodoDeFacturacionDTO periodoDeFacturacionDTO) {
        LOG.debug("Request to partially update PeriodoDeFacturacion : {}", periodoDeFacturacionDTO);

        return periodoDeFacturacionRepository
            .findById(periodoDeFacturacionDTO.getId())
            .map(existingPeriodoDeFacturacion -> {
                periodoDeFacturacionMapper.partialUpdate(existingPeriodoDeFacturacion, periodoDeFacturacionDTO);

                return existingPeriodoDeFacturacion;
            })
            .map(periodoDeFacturacionRepository::save)
            .map(periodoDeFacturacionMapper::toDto);
    }

    /**
     * Get all the periodoDeFacturacions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodoDeFacturacionDTO> findAll() {
        LOG.debug("Request to get all PeriodoDeFacturacions");
        return periodoDeFacturacionRepository
            .findAll()
            .stream()
            .map(periodoDeFacturacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one periodoDeFacturacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodoDeFacturacionDTO> findOne(Long id) {
        LOG.debug("Request to get PeriodoDeFacturacion : {}", id);
        return periodoDeFacturacionRepository.findById(id).map(periodoDeFacturacionMapper::toDto);
    }

    /**
     * Delete the periodoDeFacturacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PeriodoDeFacturacion : {}", id);
        periodoDeFacturacionRepository.deleteById(id);
    }
}

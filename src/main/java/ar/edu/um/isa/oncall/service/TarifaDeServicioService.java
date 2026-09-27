package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.TarifaDeServicio;
import ar.edu.um.isa.oncall.repository.TarifaDeServicioRepository;
import ar.edu.um.isa.oncall.service.dto.TarifaDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.TarifaDeServicioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.TarifaDeServicio}.
 */
@Service
@Transactional
public class TarifaDeServicioService {

    private static final Logger LOG = LoggerFactory.getLogger(TarifaDeServicioService.class);

    private final TarifaDeServicioRepository tarifaDeServicioRepository;

    private final TarifaDeServicioMapper tarifaDeServicioMapper;

    public TarifaDeServicioService(TarifaDeServicioRepository tarifaDeServicioRepository, TarifaDeServicioMapper tarifaDeServicioMapper) {
        this.tarifaDeServicioRepository = tarifaDeServicioRepository;
        this.tarifaDeServicioMapper = tarifaDeServicioMapper;
    }

    /**
     * Save a tarifaDeServicio.
     *
     * @param tarifaDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public TarifaDeServicioDTO save(TarifaDeServicioDTO tarifaDeServicioDTO) {
        LOG.debug("Request to save TarifaDeServicio : {}", tarifaDeServicioDTO);
        TarifaDeServicio tarifaDeServicio = tarifaDeServicioMapper.toEntity(tarifaDeServicioDTO);
        tarifaDeServicio = tarifaDeServicioRepository.save(tarifaDeServicio);
        return tarifaDeServicioMapper.toDto(tarifaDeServicio);
    }

    /**
     * Update a tarifaDeServicio.
     *
     * @param tarifaDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public TarifaDeServicioDTO update(TarifaDeServicioDTO tarifaDeServicioDTO) {
        LOG.debug("Request to update TarifaDeServicio : {}", tarifaDeServicioDTO);
        TarifaDeServicio tarifaDeServicio = tarifaDeServicioMapper.toEntity(tarifaDeServicioDTO);
        tarifaDeServicio = tarifaDeServicioRepository.save(tarifaDeServicio);
        return tarifaDeServicioMapper.toDto(tarifaDeServicio);
    }

    /**
     * Partially update a tarifaDeServicio.
     *
     * @param tarifaDeServicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TarifaDeServicioDTO> partialUpdate(TarifaDeServicioDTO tarifaDeServicioDTO) {
        LOG.debug("Request to partially update TarifaDeServicio : {}", tarifaDeServicioDTO);

        return tarifaDeServicioRepository
            .findById(tarifaDeServicioDTO.getId())
            .map(existingTarifaDeServicio -> {
                tarifaDeServicioMapper.partialUpdate(existingTarifaDeServicio, tarifaDeServicioDTO);

                return existingTarifaDeServicio;
            })
            .map(tarifaDeServicioRepository::save)
            .map(tarifaDeServicioMapper::toDto);
    }

    /**
     * Get all the tarifaDeServicios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TarifaDeServicioDTO> findAll() {
        LOG.debug("Request to get all TarifaDeServicios");
        return tarifaDeServicioRepository
            .findAll()
            .stream()
            .map(tarifaDeServicioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the tarifaDeServicios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TarifaDeServicioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tarifaDeServicioRepository.findAllWithEagerRelationships(pageable).map(tarifaDeServicioMapper::toDto);
    }

    /**
     * Get one tarifaDeServicio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TarifaDeServicioDTO> findOne(Long id) {
        LOG.debug("Request to get TarifaDeServicio : {}", id);
        return tarifaDeServicioRepository.findOneWithEagerRelationships(id).map(tarifaDeServicioMapper::toDto);
    }

    /**
     * Delete the tarifaDeServicio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TarifaDeServicio : {}", id);
        tarifaDeServicioRepository.deleteById(id);
    }
}

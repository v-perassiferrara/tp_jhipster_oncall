package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.CostoDeIncidente;
import ar.edu.um.isa.oncall.repository.CostoDeIncidenteRepository;
import ar.edu.um.isa.oncall.service.dto.CostoDeIncidenteDTO;
import ar.edu.um.isa.oncall.service.mapper.CostoDeIncidenteMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.CostoDeIncidente}.
 */
@Service
@Transactional
public class CostoDeIncidenteService {

    private static final Logger LOG = LoggerFactory.getLogger(CostoDeIncidenteService.class);

    private final CostoDeIncidenteRepository costoDeIncidenteRepository;

    private final CostoDeIncidenteMapper costoDeIncidenteMapper;

    public CostoDeIncidenteService(CostoDeIncidenteRepository costoDeIncidenteRepository, CostoDeIncidenteMapper costoDeIncidenteMapper) {
        this.costoDeIncidenteRepository = costoDeIncidenteRepository;
        this.costoDeIncidenteMapper = costoDeIncidenteMapper;
    }

    /**
     * Save a costoDeIncidente.
     *
     * @param costoDeIncidenteDTO the entity to save.
     * @return the persisted entity.
     */
    public CostoDeIncidenteDTO save(CostoDeIncidenteDTO costoDeIncidenteDTO) {
        LOG.debug("Request to save CostoDeIncidente : {}", costoDeIncidenteDTO);
        CostoDeIncidente costoDeIncidente = costoDeIncidenteMapper.toEntity(costoDeIncidenteDTO);
        costoDeIncidente = costoDeIncidenteRepository.save(costoDeIncidente);
        return costoDeIncidenteMapper.toDto(costoDeIncidente);
    }

    /**
     * Update a costoDeIncidente.
     *
     * @param costoDeIncidenteDTO the entity to save.
     * @return the persisted entity.
     */
    public CostoDeIncidenteDTO update(CostoDeIncidenteDTO costoDeIncidenteDTO) {
        LOG.debug("Request to update CostoDeIncidente : {}", costoDeIncidenteDTO);
        CostoDeIncidente costoDeIncidente = costoDeIncidenteMapper.toEntity(costoDeIncidenteDTO);
        costoDeIncidente = costoDeIncidenteRepository.save(costoDeIncidente);
        return costoDeIncidenteMapper.toDto(costoDeIncidente);
    }

    /**
     * Partially update a costoDeIncidente.
     *
     * @param costoDeIncidenteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CostoDeIncidenteDTO> partialUpdate(CostoDeIncidenteDTO costoDeIncidenteDTO) {
        LOG.debug("Request to partially update CostoDeIncidente : {}", costoDeIncidenteDTO);

        return costoDeIncidenteRepository
            .findById(costoDeIncidenteDTO.getId())
            .map(existingCostoDeIncidente -> {
                costoDeIncidenteMapper.partialUpdate(existingCostoDeIncidente, costoDeIncidenteDTO);

                return existingCostoDeIncidente;
            })
            .map(costoDeIncidenteRepository::save)
            .map(costoDeIncidenteMapper::toDto);
    }

    /**
     * Get all the costoDeIncidentes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CostoDeIncidenteDTO> findAll() {
        LOG.debug("Request to get all CostoDeIncidentes");
        return costoDeIncidenteRepository
            .findAll()
            .stream()
            .map(costoDeIncidenteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the costoDeIncidentes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CostoDeIncidenteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return costoDeIncidenteRepository.findAllWithEagerRelationships(pageable).map(costoDeIncidenteMapper::toDto);
    }

    /**
     * Get one costoDeIncidente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CostoDeIncidenteDTO> findOne(Long id) {
        LOG.debug("Request to get CostoDeIncidente : {}", id);
        return costoDeIncidenteRepository.findOneWithEagerRelationships(id).map(costoDeIncidenteMapper::toDto);
    }

    /**
     * Delete the costoDeIncidente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CostoDeIncidente : {}", id);
        costoDeIncidenteRepository.deleteById(id);
    }
}

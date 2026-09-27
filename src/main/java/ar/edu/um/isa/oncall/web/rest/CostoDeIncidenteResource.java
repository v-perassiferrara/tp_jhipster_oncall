package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.CostoDeIncidenteRepository;
import ar.edu.um.isa.oncall.service.CostoDeIncidenteService;
import ar.edu.um.isa.oncall.service.dto.CostoDeIncidenteDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.CostoDeIncidente}.
 */
@RestController
@RequestMapping("/api/costo-de-incidentes")
public class CostoDeIncidenteResource {

    private static final Logger LOG = LoggerFactory.getLogger(CostoDeIncidenteResource.class);

    private static final String ENTITY_NAME = "costoDeIncidente";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final CostoDeIncidenteService costoDeIncidenteService;

    private final CostoDeIncidenteRepository costoDeIncidenteRepository;

    public CostoDeIncidenteResource(
        CostoDeIncidenteService costoDeIncidenteService,
        CostoDeIncidenteRepository costoDeIncidenteRepository
    ) {
        this.costoDeIncidenteService = costoDeIncidenteService;
        this.costoDeIncidenteRepository = costoDeIncidenteRepository;
    }

    /**
     * {@code POST  /costo-de-incidentes} : Create a new costoDeIncidente.
     *
     * @param costoDeIncidenteDTO the costoDeIncidenteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new costoDeIncidenteDTO, or with status {@code 400 (Bad Request)} if the costoDeIncidente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CostoDeIncidenteDTO> createCostoDeIncidente(@Valid @RequestBody CostoDeIncidenteDTO costoDeIncidenteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CostoDeIncidente : {}", costoDeIncidenteDTO);
        if (costoDeIncidenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new costoDeIncidente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        costoDeIncidenteDTO = costoDeIncidenteService.save(costoDeIncidenteDTO);
        return ResponseEntity.created(new URI("/api/costo-de-incidentes/" + costoDeIncidenteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, costoDeIncidenteDTO.getId().toString()))
            .body(costoDeIncidenteDTO);
    }

    /**
     * {@code PUT  /costo-de-incidentes/:id} : Updates an existing costoDeIncidente.
     *
     * @param id the id of the costoDeIncidenteDTO to save.
     * @param costoDeIncidenteDTO the costoDeIncidenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costoDeIncidenteDTO,
     * or with status {@code 400 (Bad Request)} if the costoDeIncidenteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the costoDeIncidenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CostoDeIncidenteDTO> updateCostoDeIncidente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CostoDeIncidenteDTO costoDeIncidenteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CostoDeIncidente : {}, {}", id, costoDeIncidenteDTO);
        if (costoDeIncidenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, costoDeIncidenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!costoDeIncidenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        costoDeIncidenteDTO = costoDeIncidenteService.update(costoDeIncidenteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, costoDeIncidenteDTO.getId().toString()))
            .body(costoDeIncidenteDTO);
    }

    /**
     * {@code PATCH  /costo-de-incidentes/:id} : Partial updates given fields of an existing costoDeIncidente, field will ignore if it is null
     *
     * @param id the id of the costoDeIncidenteDTO to save.
     * @param costoDeIncidenteDTO the costoDeIncidenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costoDeIncidenteDTO,
     * or with status {@code 400 (Bad Request)} if the costoDeIncidenteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the costoDeIncidenteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the costoDeIncidenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CostoDeIncidenteDTO> partialUpdateCostoDeIncidente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CostoDeIncidenteDTO costoDeIncidenteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CostoDeIncidente partially : {}, {}", id, costoDeIncidenteDTO);
        if (costoDeIncidenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, costoDeIncidenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!costoDeIncidenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CostoDeIncidenteDTO> result = costoDeIncidenteService.partialUpdate(costoDeIncidenteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, costoDeIncidenteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /costo-de-incidentes} : get all the Costo De Incidentes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Costo De Incidentes in body.
     */
    @GetMapping("")
    public List<CostoDeIncidenteDTO> getAllCostoDeIncidentes(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CostoDeIncidentes");
        return costoDeIncidenteService.findAll();
    }

    /**
     * {@code GET  /costo-de-incidentes/:id} : get the "id" costoDeIncidente.
     *
     * @param id the id of the costoDeIncidenteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the costoDeIncidenteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CostoDeIncidenteDTO> getCostoDeIncidente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CostoDeIncidente : {}", id);
        Optional<CostoDeIncidenteDTO> costoDeIncidenteDTO = costoDeIncidenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costoDeIncidenteDTO);
    }

    /**
     * {@code DELETE  /costo-de-incidentes/:id} : delete the "id" costoDeIncidente.
     *
     * @param id the id of the costoDeIncidenteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostoDeIncidente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CostoDeIncidente : {}", id);
        costoDeIncidenteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

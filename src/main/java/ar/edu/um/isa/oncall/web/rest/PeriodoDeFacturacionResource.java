package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.PeriodoDeFacturacionRepository;
import ar.edu.um.isa.oncall.service.PeriodoDeFacturacionService;
import ar.edu.um.isa.oncall.service.dto.PeriodoDeFacturacionDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion}.
 */
@RestController
@RequestMapping("/api/periodo-de-facturacions")
public class PeriodoDeFacturacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodoDeFacturacionResource.class);

    private static final String ENTITY_NAME = "periodoDeFacturacion";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final PeriodoDeFacturacionService periodoDeFacturacionService;

    private final PeriodoDeFacturacionRepository periodoDeFacturacionRepository;

    public PeriodoDeFacturacionResource(
        PeriodoDeFacturacionService periodoDeFacturacionService,
        PeriodoDeFacturacionRepository periodoDeFacturacionRepository
    ) {
        this.periodoDeFacturacionService = periodoDeFacturacionService;
        this.periodoDeFacturacionRepository = periodoDeFacturacionRepository;
    }

    /**
     * {@code POST  /periodo-de-facturacions} : Create a new periodoDeFacturacion.
     *
     * @param periodoDeFacturacionDTO the periodoDeFacturacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodoDeFacturacionDTO, or with status {@code 400 (Bad Request)} if the periodoDeFacturacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PeriodoDeFacturacionDTO> createPeriodoDeFacturacion(
        @Valid @RequestBody PeriodoDeFacturacionDTO periodoDeFacturacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PeriodoDeFacturacion : {}", periodoDeFacturacionDTO);
        if (periodoDeFacturacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new periodoDeFacturacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        periodoDeFacturacionDTO = periodoDeFacturacionService.save(periodoDeFacturacionDTO);
        return ResponseEntity.created(new URI("/api/periodo-de-facturacions/" + periodoDeFacturacionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, periodoDeFacturacionDTO.getId().toString()))
            .body(periodoDeFacturacionDTO);
    }

    /**
     * {@code PUT  /periodo-de-facturacions/:id} : Updates an existing periodoDeFacturacion.
     *
     * @param id the id of the periodoDeFacturacionDTO to save.
     * @param periodoDeFacturacionDTO the periodoDeFacturacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodoDeFacturacionDTO,
     * or with status {@code 400 (Bad Request)} if the periodoDeFacturacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodoDeFacturacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PeriodoDeFacturacionDTO> updatePeriodoDeFacturacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeriodoDeFacturacionDTO periodoDeFacturacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PeriodoDeFacturacion : {}, {}", id, periodoDeFacturacionDTO);
        if (periodoDeFacturacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodoDeFacturacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodoDeFacturacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        periodoDeFacturacionDTO = periodoDeFacturacionService.update(periodoDeFacturacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodoDeFacturacionDTO.getId().toString()))
            .body(periodoDeFacturacionDTO);
    }

    /**
     * {@code PATCH  /periodo-de-facturacions/:id} : Partial updates given fields of an existing periodoDeFacturacion, field will ignore if it is null
     *
     * @param id the id of the periodoDeFacturacionDTO to save.
     * @param periodoDeFacturacionDTO the periodoDeFacturacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodoDeFacturacionDTO,
     * or with status {@code 400 (Bad Request)} if the periodoDeFacturacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the periodoDeFacturacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the periodoDeFacturacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeriodoDeFacturacionDTO> partialUpdatePeriodoDeFacturacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeriodoDeFacturacionDTO periodoDeFacturacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PeriodoDeFacturacion partially : {}, {}", id, periodoDeFacturacionDTO);
        if (periodoDeFacturacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodoDeFacturacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodoDeFacturacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeriodoDeFacturacionDTO> result = periodoDeFacturacionService.partialUpdate(periodoDeFacturacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodoDeFacturacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /periodo-de-facturacions} : get all the Periodo De Facturacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Periodo De Facturacions in body.
     */
    @GetMapping("")
    public List<PeriodoDeFacturacionDTO> getAllPeriodoDeFacturacions() {
        LOG.debug("REST request to get all PeriodoDeFacturacions");
        return periodoDeFacturacionService.findAll();
    }

    /**
     * {@code GET  /periodo-de-facturacions/:id} : get the "id" periodoDeFacturacion.
     *
     * @param id the id of the periodoDeFacturacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodoDeFacturacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PeriodoDeFacturacionDTO> getPeriodoDeFacturacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PeriodoDeFacturacion : {}", id);
        Optional<PeriodoDeFacturacionDTO> periodoDeFacturacionDTO = periodoDeFacturacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodoDeFacturacionDTO);
    }

    /**
     * {@code DELETE  /periodo-de-facturacions/:id} : delete the "id" periodoDeFacturacion.
     *
     * @param id the id of the periodoDeFacturacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriodoDeFacturacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PeriodoDeFacturacion : {}", id);
        periodoDeFacturacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

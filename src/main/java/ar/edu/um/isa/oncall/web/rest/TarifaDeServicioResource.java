package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.TarifaDeServicioRepository;
import ar.edu.um.isa.oncall.service.TarifaDeServicioService;
import ar.edu.um.isa.oncall.service.dto.TarifaDeServicioDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.TarifaDeServicio}.
 */
@RestController
@RequestMapping("/api/tarifa-de-servicios")
public class TarifaDeServicioResource {

    private static final Logger LOG = LoggerFactory.getLogger(TarifaDeServicioResource.class);

    private static final String ENTITY_NAME = "tarifaDeServicio";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final TarifaDeServicioService tarifaDeServicioService;

    private final TarifaDeServicioRepository tarifaDeServicioRepository;

    public TarifaDeServicioResource(
        TarifaDeServicioService tarifaDeServicioService,
        TarifaDeServicioRepository tarifaDeServicioRepository
    ) {
        this.tarifaDeServicioService = tarifaDeServicioService;
        this.tarifaDeServicioRepository = tarifaDeServicioRepository;
    }

    /**
     * {@code POST  /tarifa-de-servicios} : Create a new tarifaDeServicio.
     *
     * @param tarifaDeServicioDTO the tarifaDeServicioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarifaDeServicioDTO, or with status {@code 400 (Bad Request)} if the tarifaDeServicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TarifaDeServicioDTO> createTarifaDeServicio(@Valid @RequestBody TarifaDeServicioDTO tarifaDeServicioDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TarifaDeServicio : {}", tarifaDeServicioDTO);
        if (tarifaDeServicioDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarifaDeServicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tarifaDeServicioDTO = tarifaDeServicioService.save(tarifaDeServicioDTO);
        return ResponseEntity.created(new URI("/api/tarifa-de-servicios/" + tarifaDeServicioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tarifaDeServicioDTO.getId().toString()))
            .body(tarifaDeServicioDTO);
    }

    /**
     * {@code PUT  /tarifa-de-servicios/:id} : Updates an existing tarifaDeServicio.
     *
     * @param id the id of the tarifaDeServicioDTO to save.
     * @param tarifaDeServicioDTO the tarifaDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDeServicioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarifaDeServicioDTO> updateTarifaDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TarifaDeServicioDTO tarifaDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TarifaDeServicio : {}, {}", id, tarifaDeServicioDTO);
        if (tarifaDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifaDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tarifaDeServicioDTO = tarifaDeServicioService.update(tarifaDeServicioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifaDeServicioDTO.getId().toString()))
            .body(tarifaDeServicioDTO);
    }

    /**
     * {@code PATCH  /tarifa-de-servicios/:id} : Partial updates given fields of an existing tarifaDeServicio, field will ignore if it is null
     *
     * @param id the id of the tarifaDeServicioDTO to save.
     * @param tarifaDeServicioDTO the tarifaDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDeServicioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarifaDeServicioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TarifaDeServicioDTO> partialUpdateTarifaDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TarifaDeServicioDTO tarifaDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TarifaDeServicio partially : {}, {}", id, tarifaDeServicioDTO);
        if (tarifaDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifaDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TarifaDeServicioDTO> result = tarifaDeServicioService.partialUpdate(tarifaDeServicioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifaDeServicioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tarifa-de-servicios} : get all the Tarifa De Servicios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tarifa De Servicios in body.
     */
    @GetMapping("")
    public List<TarifaDeServicioDTO> getAllTarifaDeServicios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all TarifaDeServicios");
        return tarifaDeServicioService.findAll();
    }

    /**
     * {@code GET  /tarifa-de-servicios/:id} : get the "id" tarifaDeServicio.
     *
     * @param id the id of the tarifaDeServicioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarifaDeServicioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarifaDeServicioDTO> getTarifaDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TarifaDeServicio : {}", id);
        Optional<TarifaDeServicioDTO> tarifaDeServicioDTO = tarifaDeServicioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarifaDeServicioDTO);
    }

    /**
     * {@code DELETE  /tarifa-de-servicios/:id} : delete the "id" tarifaDeServicio.
     *
     * @param id the id of the tarifaDeServicioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifaDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TarifaDeServicio : {}", id);
        tarifaDeServicioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

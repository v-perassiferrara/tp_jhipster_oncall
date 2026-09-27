package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.PeriodoDeFacturacionAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import ar.edu.um.isa.oncall.repository.PeriodoDeFacturacionRepository;
import ar.edu.um.isa.oncall.service.dto.PeriodoDeFacturacionDTO;
import ar.edu.um.isa.oncall.service.mapper.PeriodoDeFacturacionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeriodoDeFacturacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodoDeFacturacionResourceIT {

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.parse("2023-12-04");

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.parse("2023-12-04");

    private static final Boolean DEFAULT_CERRADO = false;
    private static final Boolean UPDATED_CERRADO = true;

    private static final String ENTITY_API_URL = "/api/periodo-de-facturacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeriodoDeFacturacionRepository periodoDeFacturacionRepository;

    @Autowired
    private PeriodoDeFacturacionMapper periodoDeFacturacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodoDeFacturacionMockMvc;

    private PeriodoDeFacturacion periodoDeFacturacion;

    private PeriodoDeFacturacion insertedPeriodoDeFacturacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodoDeFacturacion createEntity() {
        return new PeriodoDeFacturacion().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).cerrado(DEFAULT_CERRADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodoDeFacturacion createUpdatedEntity() {
        return new PeriodoDeFacturacion().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).cerrado(UPDATED_CERRADO);
    }

    @BeforeEach
    void initTest() {
        periodoDeFacturacion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPeriodoDeFacturacion != null) {
            periodoDeFacturacionRepository.delete(insertedPeriodoDeFacturacion);
            insertedPeriodoDeFacturacion = null;
        }
    }

    @Test
    @Transactional
    void createPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);
        var returnedPeriodoDeFacturacionDTO = om.readValue(
            restPeriodoDeFacturacionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PeriodoDeFacturacionDTO.class
        );

        // Validate the PeriodoDeFacturacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPeriodoDeFacturacion = periodoDeFacturacionMapper.toEntity(returnedPeriodoDeFacturacionDTO);
        assertPeriodoDeFacturacionUpdatableFieldsEquals(
            returnedPeriodoDeFacturacion,
            getPersistedPeriodoDeFacturacion(returnedPeriodoDeFacturacion)
        );

        insertedPeriodoDeFacturacion = returnedPeriodoDeFacturacion;
    }

    @Test
    @Transactional
    void createPeriodoDeFacturacionWithExistingId() throws Exception {
        // Create the PeriodoDeFacturacion with an existing ID
        periodoDeFacturacion.setId(1L);
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodoDeFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        periodoDeFacturacion.setFechaInicio(null);

        // Create the PeriodoDeFacturacion, which fails.
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        restPeriodoDeFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        periodoDeFacturacion.setFechaFin(null);

        // Create the PeriodoDeFacturacion, which fails.
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        restPeriodoDeFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCerradoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        periodoDeFacturacion.setCerrado(null);

        // Create the PeriodoDeFacturacion, which fails.
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        restPeriodoDeFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeriodoDeFacturacions() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        // Get all the periodoDeFacturacionList
        restPeriodoDeFacturacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodoDeFacturacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].cerrado").value(hasItem(DEFAULT_CERRADO)));
    }

    @Test
    @Transactional
    void getPeriodoDeFacturacion() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        // Get the periodoDeFacturacion
        restPeriodoDeFacturacionMockMvc
            .perform(get(ENTITY_API_URL_ID, periodoDeFacturacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periodoDeFacturacion.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.cerrado").value(DEFAULT_CERRADO));
    }

    @Test
    @Transactional
    void getNonExistingPeriodoDeFacturacion() throws Exception {
        // Get the periodoDeFacturacion
        restPeriodoDeFacturacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeriodoDeFacturacion() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodoDeFacturacion
        PeriodoDeFacturacion updatedPeriodoDeFacturacion = periodoDeFacturacionRepository
            .findById(periodoDeFacturacion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPeriodoDeFacturacion are not directly saved in db
        em.detach(updatedPeriodoDeFacturacion);
        updatedPeriodoDeFacturacion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).cerrado(UPDATED_CERRADO);
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(updatedPeriodoDeFacturacion);

        restPeriodoDeFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodoDeFacturacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeriodoDeFacturacionToMatchAllProperties(updatedPeriodoDeFacturacion);
    }

    @Test
    @Transactional
    void putNonExistingPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodoDeFacturacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodoDeFacturacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodoDeFacturacionWithPatch() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodoDeFacturacion using partial update
        PeriodoDeFacturacion partialUpdatedPeriodoDeFacturacion = new PeriodoDeFacturacion();
        partialUpdatedPeriodoDeFacturacion.setId(periodoDeFacturacion.getId());

        partialUpdatedPeriodoDeFacturacion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).cerrado(UPDATED_CERRADO);

        restPeriodoDeFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodoDeFacturacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriodoDeFacturacion))
            )
            .andExpect(status().isOk());

        // Validate the PeriodoDeFacturacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodoDeFacturacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPeriodoDeFacturacion, periodoDeFacturacion),
            getPersistedPeriodoDeFacturacion(periodoDeFacturacion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePeriodoDeFacturacionWithPatch() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodoDeFacturacion using partial update
        PeriodoDeFacturacion partialUpdatedPeriodoDeFacturacion = new PeriodoDeFacturacion();
        partialUpdatedPeriodoDeFacturacion.setId(periodoDeFacturacion.getId());

        partialUpdatedPeriodoDeFacturacion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).cerrado(UPDATED_CERRADO);

        restPeriodoDeFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodoDeFacturacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriodoDeFacturacion))
            )
            .andExpect(status().isOk());

        // Validate the PeriodoDeFacturacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodoDeFacturacionUpdatableFieldsEquals(
            partialUpdatedPeriodoDeFacturacion,
            getPersistedPeriodoDeFacturacion(partialUpdatedPeriodoDeFacturacion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, periodoDeFacturacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriodoDeFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodoDeFacturacion.setId(longCount.incrementAndGet());

        // Create the PeriodoDeFacturacion
        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = periodoDeFacturacionMapper.toDto(periodoDeFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodoDeFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(periodoDeFacturacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodoDeFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriodoDeFacturacion() throws Exception {
        // Initialize the database
        insertedPeriodoDeFacturacion = periodoDeFacturacionRepository.saveAndFlush(periodoDeFacturacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the periodoDeFacturacion
        restPeriodoDeFacturacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, periodoDeFacturacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return periodoDeFacturacionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PeriodoDeFacturacion getPersistedPeriodoDeFacturacion(PeriodoDeFacturacion periodoDeFacturacion) {
        return periodoDeFacturacionRepository.findById(periodoDeFacturacion.getId()).orElseThrow();
    }

    protected void assertPersistedPeriodoDeFacturacionToMatchAllProperties(PeriodoDeFacturacion expectedPeriodoDeFacturacion) {
        assertPeriodoDeFacturacionAllPropertiesEquals(
            expectedPeriodoDeFacturacion,
            getPersistedPeriodoDeFacturacion(expectedPeriodoDeFacturacion)
        );
    }

    protected void assertPersistedPeriodoDeFacturacionToMatchUpdatableProperties(PeriodoDeFacturacion expectedPeriodoDeFacturacion) {
        assertPeriodoDeFacturacionAllUpdatablePropertiesEquals(
            expectedPeriodoDeFacturacion,
            getPersistedPeriodoDeFacturacion(expectedPeriodoDeFacturacion)
        );
    }
}

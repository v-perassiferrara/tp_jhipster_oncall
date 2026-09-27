package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.TarifaDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.TarifaDeServicio;
import ar.edu.um.isa.oncall.repository.TarifaDeServicioRepository;
import ar.edu.um.isa.oncall.service.TarifaDeServicioService;
import ar.edu.um.isa.oncall.service.dto.TarifaDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.TarifaDeServicioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TarifaDeServicioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TarifaDeServicioResourceIT {

    private static final BigDecimal DEFAULT_COSTO_POR_MINUTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_COSTO_POR_MINUTO = new BigDecimal(1);

    private static final Instant DEFAULT_VIGENTE_DESDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VIGENTE_DESDE = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_VIGENTE_HASTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VIGENTE_HASTA = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/tarifa-de-servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TarifaDeServicioRepository tarifaDeServicioRepository;

    @Mock
    private TarifaDeServicioRepository tarifaDeServicioRepositoryMock;

    @Autowired
    private TarifaDeServicioMapper tarifaDeServicioMapper;

    @Mock
    private TarifaDeServicioService tarifaDeServicioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifaDeServicioMockMvc;

    private TarifaDeServicio tarifaDeServicio;

    private TarifaDeServicio insertedTarifaDeServicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TarifaDeServicio createEntity(EntityManager em) {
        TarifaDeServicio tarifaDeServicio = new TarifaDeServicio()
            .costoPorMinuto(DEFAULT_COSTO_POR_MINUTO)
            .vigenteDesde(DEFAULT_VIGENTE_DESDE)
            .vigenteHasta(DEFAULT_VIGENTE_HASTA);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        tarifaDeServicio.setServicio(servicio);
        return tarifaDeServicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TarifaDeServicio createUpdatedEntity(EntityManager em) {
        TarifaDeServicio updatedTarifaDeServicio = new TarifaDeServicio()
            .costoPorMinuto(UPDATED_COSTO_POR_MINUTO)
            .vigenteDesde(UPDATED_VIGENTE_DESDE)
            .vigenteHasta(UPDATED_VIGENTE_HASTA);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedTarifaDeServicio.setServicio(servicio);
        return updatedTarifaDeServicio;
    }

    @BeforeEach
    void initTest() {
        tarifaDeServicio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTarifaDeServicio != null) {
            tarifaDeServicioRepository.delete(insertedTarifaDeServicio);
            insertedTarifaDeServicio = null;
        }
    }

    @Test
    @Transactional
    void createTarifaDeServicio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);
        var returnedTarifaDeServicioDTO = om.readValue(
            restTarifaDeServicioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDeServicioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TarifaDeServicioDTO.class
        );

        // Validate the TarifaDeServicio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTarifaDeServicio = tarifaDeServicioMapper.toEntity(returnedTarifaDeServicioDTO);
        assertTarifaDeServicioUpdatableFieldsEquals(returnedTarifaDeServicio, getPersistedTarifaDeServicio(returnedTarifaDeServicio));

        insertedTarifaDeServicio = returnedTarifaDeServicio;
    }

    @Test
    @Transactional
    void createTarifaDeServicioWithExistingId() throws Exception {
        // Create the TarifaDeServicio with an existing ID
        tarifaDeServicio.setId(1L);
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCostoPorMinutoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifaDeServicio.setCostoPorMinuto(null);

        // Create the TarifaDeServicio, which fails.
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        restTarifaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVigenteDesdeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifaDeServicio.setVigenteDesde(null);

        // Create the TarifaDeServicio, which fails.
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        restTarifaDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTarifaDeServicios() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        // Get all the tarifaDeServicioList
        restTarifaDeServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarifaDeServicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].costoPorMinuto").value(hasItem(sameNumber(DEFAULT_COSTO_POR_MINUTO))))
            .andExpect(jsonPath("$.[*].vigenteDesde").value(hasItem(DEFAULT_VIGENTE_DESDE.toString())))
            .andExpect(jsonPath("$.[*].vigenteHasta").value(hasItem(DEFAULT_VIGENTE_HASTA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTarifaDeServiciosWithEagerRelationshipsIsEnabled() throws Exception {
        when(tarifaDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTarifaDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tarifaDeServicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTarifaDeServiciosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tarifaDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTarifaDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tarifaDeServicioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTarifaDeServicio() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        // Get the tarifaDeServicio
        restTarifaDeServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, tarifaDeServicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarifaDeServicio.getId().intValue()))
            .andExpect(jsonPath("$.costoPorMinuto").value(sameNumber(DEFAULT_COSTO_POR_MINUTO)))
            .andExpect(jsonPath("$.vigenteDesde").value(DEFAULT_VIGENTE_DESDE.toString()))
            .andExpect(jsonPath("$.vigenteHasta").value(DEFAULT_VIGENTE_HASTA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTarifaDeServicio() throws Exception {
        // Get the tarifaDeServicio
        restTarifaDeServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarifaDeServicio() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifaDeServicio
        TarifaDeServicio updatedTarifaDeServicio = tarifaDeServicioRepository.findById(tarifaDeServicio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTarifaDeServicio are not directly saved in db
        em.detach(updatedTarifaDeServicio);
        updatedTarifaDeServicio
            .costoPorMinuto(UPDATED_COSTO_POR_MINUTO)
            .vigenteDesde(UPDATED_VIGENTE_DESDE)
            .vigenteHasta(UPDATED_VIGENTE_HASTA);
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(updatedTarifaDeServicio);

        restTarifaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifaDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarifaDeServicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTarifaDeServicioToMatchAllProperties(updatedTarifaDeServicio);
    }

    @Test
    @Transactional
    void putNonExistingTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifaDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarifaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarifaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDeServicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifaDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifaDeServicio using partial update
        TarifaDeServicio partialUpdatedTarifaDeServicio = new TarifaDeServicio();
        partialUpdatedTarifaDeServicio.setId(tarifaDeServicio.getId());

        restTarifaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifaDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarifaDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the TarifaDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifaDeServicioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTarifaDeServicio, tarifaDeServicio),
            getPersistedTarifaDeServicio(tarifaDeServicio)
        );
    }

    @Test
    @Transactional
    void fullUpdateTarifaDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifaDeServicio using partial update
        TarifaDeServicio partialUpdatedTarifaDeServicio = new TarifaDeServicio();
        partialUpdatedTarifaDeServicio.setId(tarifaDeServicio.getId());

        partialUpdatedTarifaDeServicio
            .costoPorMinuto(UPDATED_COSTO_POR_MINUTO)
            .vigenteDesde(UPDATED_VIGENTE_DESDE)
            .vigenteHasta(UPDATED_VIGENTE_HASTA);

        restTarifaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifaDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarifaDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the TarifaDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifaDeServicioUpdatableFieldsEquals(
            partialUpdatedTarifaDeServicio,
            getPersistedTarifaDeServicio(partialUpdatedTarifaDeServicio)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarifaDeServicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifaDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarifaDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifaDeServicio.setId(longCount.incrementAndGet());

        // Create the TarifaDeServicio
        TarifaDeServicioDTO tarifaDeServicioDTO = tarifaDeServicioMapper.toDto(tarifaDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaDeServicioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarifaDeServicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TarifaDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarifaDeServicio() throws Exception {
        // Initialize the database
        insertedTarifaDeServicio = tarifaDeServicioRepository.saveAndFlush(tarifaDeServicio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarifaDeServicio
        restTarifaDeServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarifaDeServicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tarifaDeServicioRepository.count();
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

    protected TarifaDeServicio getPersistedTarifaDeServicio(TarifaDeServicio tarifaDeServicio) {
        return tarifaDeServicioRepository.findById(tarifaDeServicio.getId()).orElseThrow();
    }

    protected void assertPersistedTarifaDeServicioToMatchAllProperties(TarifaDeServicio expectedTarifaDeServicio) {
        assertTarifaDeServicioAllPropertiesEquals(expectedTarifaDeServicio, getPersistedTarifaDeServicio(expectedTarifaDeServicio));
    }

    protected void assertPersistedTarifaDeServicioToMatchUpdatableProperties(TarifaDeServicio expectedTarifaDeServicio) {
        assertTarifaDeServicioAllUpdatablePropertiesEquals(
            expectedTarifaDeServicio,
            getPersistedTarifaDeServicio(expectedTarifaDeServicio)
        );
    }
}

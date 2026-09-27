package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.CostoDeIncidenteAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.CostoDeIncidente;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.repository.CostoDeIncidenteRepository;
import ar.edu.um.isa.oncall.service.CostoDeIncidenteService;
import ar.edu.um.isa.oncall.service.dto.CostoDeIncidenteDTO;
import ar.edu.um.isa.oncall.service.mapper.CostoDeIncidenteMapper;
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
 * Integration tests for the {@link CostoDeIncidenteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CostoDeIncidenteResourceIT {

    private static final Integer DEFAULT_MINUTOS_CAIDOS = 1;
    private static final Integer UPDATED_MINUTOS_CAIDOS = 2;

    private static final BigDecimal DEFAULT_MONTO_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTO_TOTAL = new BigDecimal(1);

    private static final Instant DEFAULT_CALCULADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CALCULADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/costo-de-incidentes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CostoDeIncidenteRepository costoDeIncidenteRepository;

    @Mock
    private CostoDeIncidenteRepository costoDeIncidenteRepositoryMock;

    @Autowired
    private CostoDeIncidenteMapper costoDeIncidenteMapper;

    @Mock
    private CostoDeIncidenteService costoDeIncidenteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCostoDeIncidenteMockMvc;

    private CostoDeIncidente costoDeIncidente;

    private CostoDeIncidente insertedCostoDeIncidente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostoDeIncidente createEntity(EntityManager em) {
        CostoDeIncidente costoDeIncidente = new CostoDeIncidente()
            .minutosCaidos(DEFAULT_MINUTOS_CAIDOS)
            .montoTotal(DEFAULT_MONTO_TOTAL)
            .calculadoEn(DEFAULT_CALCULADO_EN);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        costoDeIncidente.setIncidente(incidente);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        costoDeIncidente.setServicio(servicio);
        // Add required entity
        PeriodoDeFacturacion periodoDeFacturacion;
        if (TestUtil.findAll(em, PeriodoDeFacturacion.class).isEmpty()) {
            periodoDeFacturacion = PeriodoDeFacturacionResourceIT.createEntity();
            em.persist(periodoDeFacturacion);
            em.flush();
        } else {
            periodoDeFacturacion = TestUtil.findAll(em, PeriodoDeFacturacion.class).get(0);
        }
        costoDeIncidente.setPeriodo(periodoDeFacturacion);
        return costoDeIncidente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostoDeIncidente createUpdatedEntity(EntityManager em) {
        CostoDeIncidente updatedCostoDeIncidente = new CostoDeIncidente()
            .minutosCaidos(UPDATED_MINUTOS_CAIDOS)
            .montoTotal(UPDATED_MONTO_TOTAL)
            .calculadoEn(UPDATED_CALCULADO_EN);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createUpdatedEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        updatedCostoDeIncidente.setIncidente(incidente);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedCostoDeIncidente.setServicio(servicio);
        // Add required entity
        PeriodoDeFacturacion periodoDeFacturacion;
        if (TestUtil.findAll(em, PeriodoDeFacturacion.class).isEmpty()) {
            periodoDeFacturacion = PeriodoDeFacturacionResourceIT.createUpdatedEntity();
            em.persist(periodoDeFacturacion);
            em.flush();
        } else {
            periodoDeFacturacion = TestUtil.findAll(em, PeriodoDeFacturacion.class).get(0);
        }
        updatedCostoDeIncidente.setPeriodo(periodoDeFacturacion);
        return updatedCostoDeIncidente;
    }

    @BeforeEach
    void initTest() {
        costoDeIncidente = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCostoDeIncidente != null) {
            costoDeIncidenteRepository.delete(insertedCostoDeIncidente);
            insertedCostoDeIncidente = null;
        }
    }

    @Test
    @Transactional
    void createCostoDeIncidente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);
        var returnedCostoDeIncidenteDTO = om.readValue(
            restCostoDeIncidenteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costoDeIncidenteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CostoDeIncidenteDTO.class
        );

        // Validate the CostoDeIncidente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCostoDeIncidente = costoDeIncidenteMapper.toEntity(returnedCostoDeIncidenteDTO);
        assertCostoDeIncidenteUpdatableFieldsEquals(returnedCostoDeIncidente, getPersistedCostoDeIncidente(returnedCostoDeIncidente));

        insertedCostoDeIncidente = returnedCostoDeIncidente;
    }

    @Test
    @Transactional
    void createCostoDeIncidenteWithExistingId() throws Exception {
        // Create the CostoDeIncidente with an existing ID
        costoDeIncidente.setId(1L);
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostoDeIncidenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costoDeIncidenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMinutosCaidosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        costoDeIncidente.setMinutosCaidos(null);

        // Create the CostoDeIncidente, which fails.
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        restCostoDeIncidenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costoDeIncidenteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        costoDeIncidente.setMontoTotal(null);

        // Create the CostoDeIncidente, which fails.
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        restCostoDeIncidenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costoDeIncidenteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCostoDeIncidentes() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        // Get all the costoDeIncidenteList
        restCostoDeIncidenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costoDeIncidente.getId().intValue())))
            .andExpect(jsonPath("$.[*].minutosCaidos").value(hasItem(DEFAULT_MINUTOS_CAIDOS)))
            .andExpect(jsonPath("$.[*].montoTotal").value(hasItem(sameNumber(DEFAULT_MONTO_TOTAL))))
            .andExpect(jsonPath("$.[*].calculadoEn").value(hasItem(DEFAULT_CALCULADO_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCostoDeIncidentesWithEagerRelationshipsIsEnabled() throws Exception {
        when(costoDeIncidenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCostoDeIncidenteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(costoDeIncidenteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCostoDeIncidentesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(costoDeIncidenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCostoDeIncidenteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(costoDeIncidenteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCostoDeIncidente() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        // Get the costoDeIncidente
        restCostoDeIncidenteMockMvc
            .perform(get(ENTITY_API_URL_ID, costoDeIncidente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(costoDeIncidente.getId().intValue()))
            .andExpect(jsonPath("$.minutosCaidos").value(DEFAULT_MINUTOS_CAIDOS))
            .andExpect(jsonPath("$.montoTotal").value(sameNumber(DEFAULT_MONTO_TOTAL)))
            .andExpect(jsonPath("$.calculadoEn").value(DEFAULT_CALCULADO_EN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCostoDeIncidente() throws Exception {
        // Get the costoDeIncidente
        restCostoDeIncidenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCostoDeIncidente() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costoDeIncidente
        CostoDeIncidente updatedCostoDeIncidente = costoDeIncidenteRepository.findById(costoDeIncidente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCostoDeIncidente are not directly saved in db
        em.detach(updatedCostoDeIncidente);
        updatedCostoDeIncidente.minutosCaidos(UPDATED_MINUTOS_CAIDOS).montoTotal(UPDATED_MONTO_TOTAL).calculadoEn(UPDATED_CALCULADO_EN);
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(updatedCostoDeIncidente);

        restCostoDeIncidenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costoDeIncidenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costoDeIncidenteDTO))
            )
            .andExpect(status().isOk());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCostoDeIncidenteToMatchAllProperties(updatedCostoDeIncidente);
    }

    @Test
    @Transactional
    void putNonExistingCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costoDeIncidenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costoDeIncidenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costoDeIncidenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costoDeIncidenteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCostoDeIncidenteWithPatch() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costoDeIncidente using partial update
        CostoDeIncidente partialUpdatedCostoDeIncidente = new CostoDeIncidente();
        partialUpdatedCostoDeIncidente.setId(costoDeIncidente.getId());

        partialUpdatedCostoDeIncidente.minutosCaidos(UPDATED_MINUTOS_CAIDOS).calculadoEn(UPDATED_CALCULADO_EN);

        restCostoDeIncidenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostoDeIncidente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCostoDeIncidente))
            )
            .andExpect(status().isOk());

        // Validate the CostoDeIncidente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCostoDeIncidenteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCostoDeIncidente, costoDeIncidente),
            getPersistedCostoDeIncidente(costoDeIncidente)
        );
    }

    @Test
    @Transactional
    void fullUpdateCostoDeIncidenteWithPatch() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costoDeIncidente using partial update
        CostoDeIncidente partialUpdatedCostoDeIncidente = new CostoDeIncidente();
        partialUpdatedCostoDeIncidente.setId(costoDeIncidente.getId());

        partialUpdatedCostoDeIncidente
            .minutosCaidos(UPDATED_MINUTOS_CAIDOS)
            .montoTotal(UPDATED_MONTO_TOTAL)
            .calculadoEn(UPDATED_CALCULADO_EN);

        restCostoDeIncidenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostoDeIncidente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCostoDeIncidente))
            )
            .andExpect(status().isOk());

        // Validate the CostoDeIncidente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCostoDeIncidenteUpdatableFieldsEquals(
            partialUpdatedCostoDeIncidente,
            getPersistedCostoDeIncidente(partialUpdatedCostoDeIncidente)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, costoDeIncidenteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(costoDeIncidenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(costoDeIncidenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCostoDeIncidente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costoDeIncidente.setId(longCount.incrementAndGet());

        // Create the CostoDeIncidente
        CostoDeIncidenteDTO costoDeIncidenteDTO = costoDeIncidenteMapper.toDto(costoDeIncidente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostoDeIncidenteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(costoDeIncidenteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostoDeIncidente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCostoDeIncidente() throws Exception {
        // Initialize the database
        insertedCostoDeIncidente = costoDeIncidenteRepository.saveAndFlush(costoDeIncidente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the costoDeIncidente
        restCostoDeIncidenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, costoDeIncidente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return costoDeIncidenteRepository.count();
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

    protected CostoDeIncidente getPersistedCostoDeIncidente(CostoDeIncidente costoDeIncidente) {
        return costoDeIncidenteRepository.findById(costoDeIncidente.getId()).orElseThrow();
    }

    protected void assertPersistedCostoDeIncidenteToMatchAllProperties(CostoDeIncidente expectedCostoDeIncidente) {
        assertCostoDeIncidenteAllPropertiesEquals(expectedCostoDeIncidente, getPersistedCostoDeIncidente(expectedCostoDeIncidente));
    }

    protected void assertPersistedCostoDeIncidenteToMatchUpdatableProperties(CostoDeIncidente expectedCostoDeIncidente) {
        assertCostoDeIncidenteAllUpdatablePropertiesEquals(
            expectedCostoDeIncidente,
            getPersistedCostoDeIncidente(expectedCostoDeIncidente)
        );
    }
}

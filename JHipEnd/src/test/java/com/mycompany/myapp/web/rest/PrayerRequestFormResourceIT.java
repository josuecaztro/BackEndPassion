package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PrayerRequestFormAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PrayerRequestForm;
import com.mycompany.myapp.repository.PrayerRequestFormRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrayerRequestFormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrayerRequestFormResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_FRAME = "AAAAAAAAAA";
    private static final String UPDATED_TIME_FRAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prayer-request-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrayerRequestFormRepository prayerRequestFormRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrayerRequestFormMockMvc;

    private PrayerRequestForm prayerRequestForm;

    private PrayerRequestForm insertedPrayerRequestForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrayerRequestForm createEntity(EntityManager em) {
        PrayerRequestForm prayerRequestForm = new PrayerRequestForm()
            .description(DEFAULT_DESCRIPTION)
            .timeFrame(DEFAULT_TIME_FRAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return prayerRequestForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrayerRequestForm createUpdatedEntity(EntityManager em) {
        PrayerRequestForm prayerRequestForm = new PrayerRequestForm()
            .description(UPDATED_DESCRIPTION)
            .timeFrame(UPDATED_TIME_FRAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return prayerRequestForm;
    }

    @BeforeEach
    public void initTest() {
        prayerRequestForm = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPrayerRequestForm != null) {
            prayerRequestFormRepository.delete(insertedPrayerRequestForm);
            insertedPrayerRequestForm = null;
        }
    }

    @Test
    @Transactional
    void createPrayerRequestForm() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PrayerRequestForm
        var returnedPrayerRequestForm = om.readValue(
            restPrayerRequestFormMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prayerRequestForm)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrayerRequestForm.class
        );

        // Validate the PrayerRequestForm in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPrayerRequestFormUpdatableFieldsEquals(returnedPrayerRequestForm, getPersistedPrayerRequestForm(returnedPrayerRequestForm));

        insertedPrayerRequestForm = returnedPrayerRequestForm;
    }

    @Test
    @Transactional
    void createPrayerRequestFormWithExistingId() throws Exception {
        // Create the PrayerRequestForm with an existing ID
        prayerRequestForm.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrayerRequestFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prayerRequestForm)))
            .andExpect(status().isBadRequest());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrayerRequestForms() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        // Get all the prayerRequestFormList
        restPrayerRequestFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prayerRequestForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeFrame").value(hasItem(DEFAULT_TIME_FRAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getPrayerRequestForm() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        // Get the prayerRequestForm
        restPrayerRequestFormMockMvc
            .perform(get(ENTITY_API_URL_ID, prayerRequestForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prayerRequestForm.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.timeFrame").value(DEFAULT_TIME_FRAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingPrayerRequestForm() throws Exception {
        // Get the prayerRequestForm
        restPrayerRequestFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrayerRequestForm() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prayerRequestForm
        PrayerRequestForm updatedPrayerRequestForm = prayerRequestFormRepository.findById(prayerRequestForm.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrayerRequestForm are not directly saved in db
        em.detach(updatedPrayerRequestForm);
        updatedPrayerRequestForm
            .description(UPDATED_DESCRIPTION)
            .timeFrame(UPDATED_TIME_FRAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restPrayerRequestFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrayerRequestForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPrayerRequestForm))
            )
            .andExpect(status().isOk());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrayerRequestFormToMatchAllProperties(updatedPrayerRequestForm);
    }

    @Test
    @Transactional
    void putNonExistingPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prayerRequestForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prayerRequestForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prayerRequestForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prayerRequestForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrayerRequestFormWithPatch() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prayerRequestForm using partial update
        PrayerRequestForm partialUpdatedPrayerRequestForm = new PrayerRequestForm();
        partialUpdatedPrayerRequestForm.setId(prayerRequestForm.getId());

        partialUpdatedPrayerRequestForm
            .timeFrame(UPDATED_TIME_FRAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restPrayerRequestFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrayerRequestForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrayerRequestForm))
            )
            .andExpect(status().isOk());

        // Validate the PrayerRequestForm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrayerRequestFormUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrayerRequestForm, prayerRequestForm),
            getPersistedPrayerRequestForm(prayerRequestForm)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrayerRequestFormWithPatch() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prayerRequestForm using partial update
        PrayerRequestForm partialUpdatedPrayerRequestForm = new PrayerRequestForm();
        partialUpdatedPrayerRequestForm.setId(prayerRequestForm.getId());

        partialUpdatedPrayerRequestForm
            .description(UPDATED_DESCRIPTION)
            .timeFrame(UPDATED_TIME_FRAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restPrayerRequestFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrayerRequestForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrayerRequestForm))
            )
            .andExpect(status().isOk());

        // Validate the PrayerRequestForm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrayerRequestFormUpdatableFieldsEquals(
            partialUpdatedPrayerRequestForm,
            getPersistedPrayerRequestForm(partialUpdatedPrayerRequestForm)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prayerRequestForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prayerRequestForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prayerRequestForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrayerRequestForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prayerRequestForm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrayerRequestFormMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prayerRequestForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrayerRequestForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrayerRequestForm() throws Exception {
        // Initialize the database
        insertedPrayerRequestForm = prayerRequestFormRepository.saveAndFlush(prayerRequestForm);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prayerRequestForm
        restPrayerRequestFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, prayerRequestForm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prayerRequestFormRepository.count();
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

    protected PrayerRequestForm getPersistedPrayerRequestForm(PrayerRequestForm prayerRequestForm) {
        return prayerRequestFormRepository.findById(prayerRequestForm.getId()).orElseThrow();
    }

    protected void assertPersistedPrayerRequestFormToMatchAllProperties(PrayerRequestForm expectedPrayerRequestForm) {
        assertPrayerRequestFormAllPropertiesEquals(expectedPrayerRequestForm, getPersistedPrayerRequestForm(expectedPrayerRequestForm));
    }

    protected void assertPersistedPrayerRequestFormToMatchUpdatableProperties(PrayerRequestForm expectedPrayerRequestForm) {
        assertPrayerRequestFormAllUpdatablePropertiesEquals(
            expectedPrayerRequestForm,
            getPersistedPrayerRequestForm(expectedPrayerRequestForm)
        );
    }
}

package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ContactRequestAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ContactRequest;
import com.mycompany.myapp.repository.ContactRequestRepository;
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
 * Integration tests for the {@link ContactRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactRequestResourceIT {

    private static final String DEFAULT_MESSAGE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactRequestMockMvc;

    private ContactRequest contactRequest;

    private ContactRequest insertedContactRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRequest createEntity(EntityManager em) {
        ContactRequest contactRequest = new ContactRequest()
            .messageBody(DEFAULT_MESSAGE_BODY)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return contactRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRequest createUpdatedEntity(EntityManager em) {
        ContactRequest contactRequest = new ContactRequest()
            .messageBody(UPDATED_MESSAGE_BODY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return contactRequest;
    }

    @BeforeEach
    public void initTest() {
        contactRequest = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedContactRequest != null) {
            contactRequestRepository.delete(insertedContactRequest);
            insertedContactRequest = null;
        }
    }

    @Test
    @Transactional
    void createContactRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactRequest
        var returnedContactRequest = om.readValue(
            restContactRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactRequest.class
        );

        // Validate the ContactRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContactRequestUpdatableFieldsEquals(returnedContactRequest, getPersistedContactRequest(returnedContactRequest));

        insertedContactRequest = returnedContactRequest;
    }

    @Test
    @Transactional
    void createContactRequestWithExistingId() throws Exception {
        // Create the ContactRequest with an existing ID
        contactRequest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactRequest)))
            .andExpect(status().isBadRequest());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactRequests() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        // Get all the contactRequestList
        restContactRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageBody").value(hasItem(DEFAULT_MESSAGE_BODY)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getContactRequest() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        // Get the contactRequest
        restContactRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, contactRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactRequest.getId().intValue()))
            .andExpect(jsonPath("$.messageBody").value(DEFAULT_MESSAGE_BODY))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingContactRequest() throws Exception {
        // Get the contactRequest
        restContactRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactRequest() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactRequest
        ContactRequest updatedContactRequest = contactRequestRepository.findById(contactRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactRequest are not directly saved in db
        em.detach(updatedContactRequest);
        updatedContactRequest
            .messageBody(UPDATED_MESSAGE_BODY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restContactRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContactRequest))
            )
            .andExpect(status().isOk());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactRequestToMatchAllProperties(updatedContactRequest);
    }

    @Test
    @Transactional
    void putNonExistingContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactRequestWithPatch() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactRequest using partial update
        ContactRequest partialUpdatedContactRequest = new ContactRequest();
        partialUpdatedContactRequest.setId(contactRequest.getId());

        partialUpdatedContactRequest.messageBody(UPDATED_MESSAGE_BODY).lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restContactRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactRequest))
            )
            .andExpect(status().isOk());

        // Validate the ContactRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactRequest, contactRequest),
            getPersistedContactRequest(contactRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactRequestWithPatch() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactRequest using partial update
        ContactRequest partialUpdatedContactRequest = new ContactRequest();
        partialUpdatedContactRequest.setId(contactRequest.getId());

        partialUpdatedContactRequest
            .messageBody(UPDATED_MESSAGE_BODY)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restContactRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactRequest))
            )
            .andExpect(status().isOk());

        // Validate the ContactRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactRequestUpdatableFieldsEquals(partialUpdatedContactRequest, getPersistedContactRequest(partialUpdatedContactRequest));
    }

    @Test
    @Transactional
    void patchNonExistingContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactRequest() throws Exception {
        // Initialize the database
        insertedContactRequest = contactRequestRepository.saveAndFlush(contactRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactRequest
        restContactRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactRequestRepository.count();
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

    protected ContactRequest getPersistedContactRequest(ContactRequest contactRequest) {
        return contactRequestRepository.findById(contactRequest.getId()).orElseThrow();
    }

    protected void assertPersistedContactRequestToMatchAllProperties(ContactRequest expectedContactRequest) {
        assertContactRequestAllPropertiesEquals(expectedContactRequest, getPersistedContactRequest(expectedContactRequest));
    }

    protected void assertPersistedContactRequestToMatchUpdatableProperties(ContactRequest expectedContactRequest) {
        assertContactRequestAllUpdatablePropertiesEquals(expectedContactRequest, getPersistedContactRequest(expectedContactRequest));
    }
}

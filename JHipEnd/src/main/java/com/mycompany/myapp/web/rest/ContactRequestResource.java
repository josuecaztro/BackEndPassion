package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ContactRequest;
import com.mycompany.myapp.repository.ContactRequestRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ContactRequest}.
 */
@RestController
@RequestMapping("/api/contact-requests")
@Transactional
public class ContactRequestResource {

    private static final Logger log = LoggerFactory.getLogger(ContactRequestResource.class);

    private static final String ENTITY_NAME = "contactRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactRequestRepository contactRequestRepository;

    public ContactRequestResource(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
    }

    /**
     * {@code POST  /contact-requests} : Create a new contactRequest.
     *
     * @param contactRequest the contactRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactRequest, or with status {@code 400 (Bad Request)} if the contactRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactRequest> createContactRequest(@RequestBody ContactRequest contactRequest) throws URISyntaxException {
        log.debug("REST request to save ContactRequest : {}", contactRequest);
        if (contactRequest.getId() != null) {
            throw new BadRequestAlertException("A new contactRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactRequest = contactRequestRepository.save(contactRequest);
        return ResponseEntity.created(new URI("/api/contact-requests/" + contactRequest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactRequest.getId().toString()))
            .body(contactRequest);
    }

    /**
     * {@code PUT  /contact-requests/:id} : Updates an existing contactRequest.
     *
     * @param id the id of the contactRequest to save.
     * @param contactRequest the contactRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactRequest,
     * or with status {@code 400 (Bad Request)} if the contactRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactRequest> updateContactRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactRequest contactRequest
    ) throws URISyntaxException {
        log.debug("REST request to update ContactRequest : {}, {}", id, contactRequest);
        if (contactRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactRequest = contactRequestRepository.save(contactRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactRequest.getId().toString()))
            .body(contactRequest);
    }

    /**
     * {@code PATCH  /contact-requests/:id} : Partial updates given fields of an existing contactRequest, field will ignore if it is null
     *
     * @param id the id of the contactRequest to save.
     * @param contactRequest the contactRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactRequest,
     * or with status {@code 400 (Bad Request)} if the contactRequest is not valid,
     * or with status {@code 404 (Not Found)} if the contactRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactRequest> partialUpdateContactRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactRequest contactRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactRequest partially : {}, {}", id, contactRequest);
        if (contactRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactRequest> result = contactRequestRepository
            .findById(contactRequest.getId())
            .map(existingContactRequest -> {
                if (contactRequest.getMessageBody() != null) {
                    existingContactRequest.setMessageBody(contactRequest.getMessageBody());
                }
                if (contactRequest.getFirstName() != null) {
                    existingContactRequest.setFirstName(contactRequest.getFirstName());
                }
                if (contactRequest.getLastName() != null) {
                    existingContactRequest.setLastName(contactRequest.getLastName());
                }
                if (contactRequest.getPhoneNumber() != null) {
                    existingContactRequest.setPhoneNumber(contactRequest.getPhoneNumber());
                }

                return existingContactRequest;
            })
            .map(contactRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-requests} : get all the contactRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactRequests in body.
     */
    @GetMapping("")
    public List<ContactRequest> getAllContactRequests() {
        log.debug("REST request to get all ContactRequests");
        return contactRequestRepository.findAll();
    }

    /**
     * {@code GET  /contact-requests/:id} : get the "id" contactRequest.
     *
     * @param id the id of the contactRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactRequest> getContactRequest(@PathVariable("id") Long id) {
        log.debug("REST request to get ContactRequest : {}", id);
        Optional<ContactRequest> contactRequest = contactRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contactRequest);
    }

    /**
     * {@code DELETE  /contact-requests/:id} : delete the "id" contactRequest.
     *
     * @param id the id of the contactRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactRequest(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContactRequest : {}", id);
        contactRequestRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

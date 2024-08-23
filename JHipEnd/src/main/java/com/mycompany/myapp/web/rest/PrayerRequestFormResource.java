package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PrayerRequestForm;
import com.mycompany.myapp.repository.PrayerRequestFormRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PrayerRequestForm}.
 */
@RestController
@RequestMapping("/api/prayer-request-forms")
@Transactional
public class PrayerRequestFormResource {

    private static final Logger log = LoggerFactory.getLogger(PrayerRequestFormResource.class);

    private static final String ENTITY_NAME = "prayerRequestForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrayerRequestFormRepository prayerRequestFormRepository;

    public PrayerRequestFormResource(PrayerRequestFormRepository prayerRequestFormRepository) {
        this.prayerRequestFormRepository = prayerRequestFormRepository;
    }

    /**
     * {@code POST  /prayer-request-forms} : Create a new prayerRequestForm.
     *
     * @param prayerRequestForm the prayerRequestForm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prayerRequestForm, or with status {@code 400 (Bad Request)} if the prayerRequestForm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrayerRequestForm> createPrayerRequestForm(@RequestBody PrayerRequestForm prayerRequestForm)
        throws URISyntaxException {
        log.debug("REST request to save PrayerRequestForm : {}", prayerRequestForm);
        if (prayerRequestForm.getId() != null) {
            throw new BadRequestAlertException("A new prayerRequestForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prayerRequestForm = prayerRequestFormRepository.save(prayerRequestForm);
        return ResponseEntity.created(new URI("/api/prayer-request-forms/" + prayerRequestForm.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prayerRequestForm.getId().toString()))
            .body(prayerRequestForm);
    }

    /**
     * {@code PUT  /prayer-request-forms/:id} : Updates an existing prayerRequestForm.
     *
     * @param id the id of the prayerRequestForm to save.
     * @param prayerRequestForm the prayerRequestForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prayerRequestForm,
     * or with status {@code 400 (Bad Request)} if the prayerRequestForm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prayerRequestForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrayerRequestForm> updatePrayerRequestForm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrayerRequestForm prayerRequestForm
    ) throws URISyntaxException {
        log.debug("REST request to update PrayerRequestForm : {}, {}", id, prayerRequestForm);
        if (prayerRequestForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prayerRequestForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prayerRequestFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prayerRequestForm = prayerRequestFormRepository.save(prayerRequestForm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prayerRequestForm.getId().toString()))
            .body(prayerRequestForm);
    }

    /**
     * {@code PATCH  /prayer-request-forms/:id} : Partial updates given fields of an existing prayerRequestForm, field will ignore if it is null
     *
     * @param id the id of the prayerRequestForm to save.
     * @param prayerRequestForm the prayerRequestForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prayerRequestForm,
     * or with status {@code 400 (Bad Request)} if the prayerRequestForm is not valid,
     * or with status {@code 404 (Not Found)} if the prayerRequestForm is not found,
     * or with status {@code 500 (Internal Server Error)} if the prayerRequestForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrayerRequestForm> partialUpdatePrayerRequestForm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrayerRequestForm prayerRequestForm
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrayerRequestForm partially : {}, {}", id, prayerRequestForm);
        if (prayerRequestForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prayerRequestForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prayerRequestFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrayerRequestForm> result = prayerRequestFormRepository
            .findById(prayerRequestForm.getId())
            .map(existingPrayerRequestForm -> {
                if (prayerRequestForm.getDescription() != null) {
                    existingPrayerRequestForm.setDescription(prayerRequestForm.getDescription());
                }
                if (prayerRequestForm.getTimeFrame() != null) {
                    existingPrayerRequestForm.setTimeFrame(prayerRequestForm.getTimeFrame());
                }
                if (prayerRequestForm.getFirstName() != null) {
                    existingPrayerRequestForm.setFirstName(prayerRequestForm.getFirstName());
                }
                if (prayerRequestForm.getLastName() != null) {
                    existingPrayerRequestForm.setLastName(prayerRequestForm.getLastName());
                }
                if (prayerRequestForm.getPhoneNumber() != null) {
                    existingPrayerRequestForm.setPhoneNumber(prayerRequestForm.getPhoneNumber());
                }

                return existingPrayerRequestForm;
            })
            .map(prayerRequestFormRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prayerRequestForm.getId().toString())
        );
    }

    /**
     * {@code GET  /prayer-request-forms} : get all the prayerRequestForms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prayerRequestForms in body.
     */
    @GetMapping("")
    public List<PrayerRequestForm> getAllPrayerRequestForms() {
        log.debug("REST request to get all PrayerRequestForms");
        return prayerRequestFormRepository.findAll();
    }

    /**
     * {@code GET  /prayer-request-forms/:id} : get the "id" prayerRequestForm.
     *
     * @param id the id of the prayerRequestForm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prayerRequestForm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrayerRequestForm> getPrayerRequestForm(@PathVariable("id") Long id) {
        log.debug("REST request to get PrayerRequestForm : {}", id);
        Optional<PrayerRequestForm> prayerRequestForm = prayerRequestFormRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prayerRequestForm);
    }

    /**
     * {@code DELETE  /prayer-request-forms/:id} : delete the "id" prayerRequestForm.
     *
     * @param id the id of the prayerRequestForm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrayerRequestForm(@PathVariable("id") Long id) {
        log.debug("REST request to delete PrayerRequestForm : {}", id);
        prayerRequestFormRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

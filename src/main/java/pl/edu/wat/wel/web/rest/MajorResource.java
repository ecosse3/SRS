package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.Major;
import pl.edu.wat.wel.repository.MajorRepository;
import pl.edu.wat.wel.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pl.edu.wat.wel.domain.Major}.
 */
@RestController
@RequestMapping("/api")
public class MajorResource {

    private final Logger log = LoggerFactory.getLogger(MajorResource.class);

    private static final String ENTITY_NAME = "major";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MajorRepository majorRepository;

    public MajorResource(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    /**
     * {@code POST  /majors} : Create a new major.
     *
     * @param major the major to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new major, or with status {@code 400 (Bad Request)} if the major has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/majors")
    public ResponseEntity<Major> createMajor(@Valid @RequestBody Major major) throws URISyntaxException {
        log.debug("REST request to save Major : {}", major);
        if (major.getId() != null) {
            throw new BadRequestAlertException("A new major cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Major result = majorRepository.save(major);
        return ResponseEntity.created(new URI("/api/majors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /majors} : Updates an existing major.
     *
     * @param major the major to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated major,
     * or with status {@code 400 (Bad Request)} if the major is not valid,
     * or with status {@code 500 (Internal Server Error)} if the major couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/majors")
    public ResponseEntity<Major> updateMajor(@Valid @RequestBody Major major) throws URISyntaxException {
        log.debug("REST request to update Major : {}", major);
        if (major.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Major result = majorRepository.save(major);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, major.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /majors} : get all the majors.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of majors in body.
     */
    @GetMapping("/majors")
    public List<Major> getAllMajors() {
        log.debug("REST request to get all Majors");
        return majorRepository.findAll();
    }

    /**
     * {@code GET  /majors/:id} : get the "id" major.
     *
     * @param id the id of the major to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the major, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/majors/{id}")
    public ResponseEntity<Major> getMajor(@PathVariable Long id) {
        log.debug("REST request to get Major : {}", id);
        Optional<Major> major = majorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(major);
    }

    /**
     * {@code DELETE  /majors/:id} : delete the "id" major.
     *
     * @param id the id of the major to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/majors/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable Long id) {
        log.debug("REST request to delete Major : {}", id);
        majorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

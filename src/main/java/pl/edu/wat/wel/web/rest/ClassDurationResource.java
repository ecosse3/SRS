package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.ClassDuration;
import pl.edu.wat.wel.repository.ClassDurationRepository;
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
 * REST controller for managing {@link pl.edu.wat.wel.domain.ClassDuration}.
 */
@RestController
@RequestMapping("/api")
public class ClassDurationResource {

    private final Logger log = LoggerFactory.getLogger(ClassDurationResource.class);

    private static final String ENTITY_NAME = "classDuration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassDurationRepository classDurationRepository;

    public ClassDurationResource(ClassDurationRepository classDurationRepository) {
        this.classDurationRepository = classDurationRepository;
    }

    /**
     * {@code POST  /class-durations} : Create a new classDuration.
     *
     * @param classDuration the classDuration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classDuration, or with status {@code 400 (Bad Request)} if the classDuration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-durations")
    public ResponseEntity<ClassDuration> createClassDuration(@Valid @RequestBody ClassDuration classDuration) throws URISyntaxException {
        log.debug("REST request to save ClassDuration : {}", classDuration);
        if (classDuration.getId() != null) {
            throw new BadRequestAlertException("A new classDuration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassDuration result = classDurationRepository.save(classDuration);
        return ResponseEntity.created(new URI("/api/class-durations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-durations} : Updates an existing classDuration.
     *
     * @param classDuration the classDuration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classDuration,
     * or with status {@code 400 (Bad Request)} if the classDuration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classDuration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-durations")
    public ResponseEntity<ClassDuration> updateClassDuration(@Valid @RequestBody ClassDuration classDuration) throws URISyntaxException {
        log.debug("REST request to update ClassDuration : {}", classDuration);
        if (classDuration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassDuration result = classDurationRepository.save(classDuration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classDuration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-durations} : get all the classDurations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classDurations in body.
     */
    @GetMapping("/class-durations")
    public List<ClassDuration> getAllClassDurations() {
        log.debug("REST request to get all ClassDurations");
        return classDurationRepository.findAll();
    }

    /**
     * {@code GET  /class-durations/:id} : get the "id" classDuration.
     *
     * @param id the id of the classDuration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classDuration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-durations/{id}")
    public ResponseEntity<ClassDuration> getClassDuration(@PathVariable Long id) {
        log.debug("REST request to get ClassDuration : {}", id);
        Optional<ClassDuration> classDuration = classDurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(classDuration);
    }

    /**
     * {@code DELETE  /class-durations/:id} : delete the "id" classDuration.
     *
     * @param id the id of the classDuration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-durations/{id}")
    public ResponseEntity<Void> deleteClassDuration(@PathVariable Long id) {
        log.debug("REST request to delete ClassDuration : {}", id);
        classDurationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

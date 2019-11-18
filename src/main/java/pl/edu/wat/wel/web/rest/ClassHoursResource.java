package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.ClassHours;
import pl.edu.wat.wel.repository.ClassHoursRepository;
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
 * REST controller for managing {@link pl.edu.wat.wel.domain.ClassHours}.
 */
@RestController
@RequestMapping("/api")
public class ClassHoursResource {

    private final Logger log = LoggerFactory.getLogger(ClassHoursResource.class);

    private static final String ENTITY_NAME = "classHours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassHoursRepository classHoursRepository;

    public ClassHoursResource(ClassHoursRepository classHoursRepository) {
        this.classHoursRepository = classHoursRepository;
    }

    /**
     * {@code POST  /class-hours} : Create a new classHours.
     *
     * @param classHours the classHours to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classHours, or with status {@code 400 (Bad Request)} if the classHours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-hours")
    public ResponseEntity<ClassHours> createClassHours(@Valid @RequestBody ClassHours classHours) throws URISyntaxException {
        log.debug("REST request to save ClassHours : {}", classHours);
        if (classHours.getId() != null) {
            throw new BadRequestAlertException("A new classHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassHours result = classHoursRepository.save(classHours);
        return ResponseEntity.created(new URI("/api/class-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-hours} : Updates an existing classHours.
     *
     * @param classHours the classHours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classHours,
     * or with status {@code 400 (Bad Request)} if the classHours is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classHours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-hours")
    public ResponseEntity<ClassHours> updateClassHours(@Valid @RequestBody ClassHours classHours) throws URISyntaxException {
        log.debug("REST request to update ClassHours : {}", classHours);
        if (classHours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassHours result = classHoursRepository.save(classHours);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classHours.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-hours} : get all the classHours.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classHours in body.
     */
    @GetMapping("/class-hours")
    public List<ClassHours> getAllClassHours() {
        log.debug("REST request to get all ClassHours");
        return classHoursRepository.findAll();
    }

    /**
     * {@code GET  /class-hours/:id} : get the "id" classHours.
     *
     * @param id the id of the classHours to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classHours, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-hours/{id}")
    public ResponseEntity<ClassHours> getClassHours(@PathVariable Long id) {
        log.debug("REST request to get ClassHours : {}", id);
        Optional<ClassHours> classHours = classHoursRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(classHours);
    }

    /**
     * {@code DELETE  /class-hours/:id} : delete the "id" classHours.
     *
     * @param id the id of the classHours to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-hours/{id}")
    public ResponseEntity<Void> deleteClassHours(@PathVariable Long id) {
        log.debug("REST request to delete ClassHours : {}", id);
        classHoursRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

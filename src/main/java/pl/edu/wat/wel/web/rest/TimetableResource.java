package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.Timetable;
import pl.edu.wat.wel.service.TimetableService;
import pl.edu.wat.wel.web.rest.errors.BadRequestAlertException;
import pl.edu.wat.wel.service.dto.TimetableCriteria;
import pl.edu.wat.wel.service.TimetableQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pl.edu.wat.wel.domain.Timetable}.
 */
@RestController
@RequestMapping("/api")
public class TimetableResource {

    private final Logger log = LoggerFactory.getLogger(TimetableResource.class);

    private static final String ENTITY_NAME = "timetable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimetableService timetableService;

    private final TimetableQueryService timetableQueryService;

    public TimetableResource(TimetableService timetableService, TimetableQueryService timetableQueryService) {
        this.timetableService = timetableService;
        this.timetableQueryService = timetableQueryService;
    }

    /**
     * {@code POST  /timetables} : Create a new timetable.
     *
     * @param timetable the timetable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timetable, or with status {@code 400 (Bad Request)} if the timetable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/timetables")
    public ResponseEntity<Timetable> createTimetable(@Valid @RequestBody Timetable timetable) throws URISyntaxException {
        log.debug("REST request to save Timetable : {}", timetable);
        if (timetable.getId() != null) {
            throw new BadRequestAlertException("A new timetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Timetable result = timetableService.save(timetable);
        return ResponseEntity.created(new URI("/api/timetables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /timetables} : Updates an existing timetable.
     *
     * @param timetable the timetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timetable,
     * or with status {@code 400 (Bad Request)} if the timetable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/timetables")
    public ResponseEntity<Timetable> updateTimetable(@Valid @RequestBody Timetable timetable) throws URISyntaxException {
        log.debug("REST request to update Timetable : {}", timetable);
        if (timetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Timetable result = timetableService.save(timetable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timetable.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /timetables} : get all the timetables.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timetables in body.
     */
    @GetMapping("/timetables")
    public ResponseEntity<List<Timetable>> getAllTimetables(TimetableCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Timetables by criteria: {}", criteria);
        Page<Timetable> page = timetableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /timetables/count} : count all the timetables.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/timetables/count")
    public ResponseEntity<Long> countTimetables(TimetableCriteria criteria) {
        log.debug("REST request to count Timetables by criteria: {}", criteria);
        return ResponseEntity.ok().body(timetableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /timetables/:id} : get the "id" timetable.
     *
     * @param id the id of the timetable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timetable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/timetables/{id}")
    public ResponseEntity<Timetable> getTimetable(@PathVariable Long id) {
        log.debug("REST request to get Timetable : {}", id);
        Optional<Timetable> timetable = timetableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timetable);
    }

    /**
     * {@code DELETE  /timetables/:id} : delete the "id" timetable.
     *
     * @param id the id of the timetable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/timetables/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        log.debug("REST request to delete Timetable : {}", id);
        timetableService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

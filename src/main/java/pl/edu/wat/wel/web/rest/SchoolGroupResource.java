package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.SchoolGroup;
import pl.edu.wat.wel.service.SchoolGroupService;
import pl.edu.wat.wel.web.rest.errors.BadRequestAlertException;
import pl.edu.wat.wel.service.dto.SchoolGroupCriteria;
import pl.edu.wat.wel.service.SchoolGroupQueryService;

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
 * REST controller for managing {@link pl.edu.wat.wel.domain.SchoolGroup}.
 */
@RestController
@RequestMapping("/api")
public class SchoolGroupResource {

    private final Logger log = LoggerFactory.getLogger(SchoolGroupResource.class);

    private static final String ENTITY_NAME = "schoolGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolGroupService schoolGroupService;

    private final SchoolGroupQueryService schoolGroupQueryService;

    public SchoolGroupResource(SchoolGroupService schoolGroupService, SchoolGroupQueryService schoolGroupQueryService) {
        this.schoolGroupService = schoolGroupService;
        this.schoolGroupQueryService = schoolGroupQueryService;
    }

    /**
     * {@code POST  /school-groups} : Create a new schoolGroup.
     *
     * @param schoolGroup the schoolGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolGroup, or with status {@code 400 (Bad Request)} if the schoolGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/school-groups")
    public ResponseEntity<SchoolGroup> createSchoolGroup(@Valid @RequestBody SchoolGroup schoolGroup) throws URISyntaxException {
        log.debug("REST request to save SchoolGroup : {}", schoolGroup);
        if (schoolGroup.getId() != null) {
            throw new BadRequestAlertException("A new schoolGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolGroup result = schoolGroupService.save(schoolGroup);
        return ResponseEntity.created(new URI("/api/school-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /school-groups} : Updates an existing schoolGroup.
     *
     * @param schoolGroup the schoolGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolGroup,
     * or with status {@code 400 (Bad Request)} if the schoolGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/school-groups")
    public ResponseEntity<SchoolGroup> updateSchoolGroup(@Valid @RequestBody SchoolGroup schoolGroup) throws URISyntaxException {
        log.debug("REST request to update SchoolGroup : {}", schoolGroup);
        if (schoolGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SchoolGroup result = schoolGroupService.save(schoolGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /school-groups} : get all the schoolGroups.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schoolGroups in body.
     */
    @GetMapping("/school-groups")
    public ResponseEntity<List<SchoolGroup>> getAllSchoolGroups(SchoolGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SchoolGroups by criteria: {}", criteria);
        Page<SchoolGroup> page = schoolGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /school-groups/count} : count all the schoolGroups.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/school-groups/count")
    public ResponseEntity<Long> countSchoolGroups(SchoolGroupCriteria criteria) {
        log.debug("REST request to count SchoolGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(schoolGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /school-groups/:id} : get the "id" schoolGroup.
     *
     * @param id the id of the schoolGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/school-groups/{id}")
    public ResponseEntity<SchoolGroup> getSchoolGroup(@PathVariable Long id) {
        log.debug("REST request to get SchoolGroup : {}", id);
        Optional<SchoolGroup> schoolGroup = schoolGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolGroup);
    }

    /**
     * {@code DELETE  /school-groups/:id} : delete the "id" schoolGroup.
     *
     * @param id the id of the schoolGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/school-groups/{id}")
    public ResponseEntity<Void> deleteSchoolGroup(@PathVariable Long id) {
        log.debug("REST request to delete SchoolGroup : {}", id);
        schoolGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

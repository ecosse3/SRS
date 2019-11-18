package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.service.ClassRoomService;
import pl.edu.wat.wel.web.rest.errors.BadRequestAlertException;
import pl.edu.wat.wel.service.dto.ClassRoomCriteria;
import pl.edu.wat.wel.service.ClassRoomQueryService;

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
 * REST controller for managing {@link pl.edu.wat.wel.domain.ClassRoom}.
 */
@RestController
@RequestMapping("/api")
public class ClassRoomResource {

    private final Logger log = LoggerFactory.getLogger(ClassRoomResource.class);

    private static final String ENTITY_NAME = "classRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassRoomService classRoomService;

    private final ClassRoomQueryService classRoomQueryService;

    public ClassRoomResource(ClassRoomService classRoomService, ClassRoomQueryService classRoomQueryService) {
        this.classRoomService = classRoomService;
        this.classRoomQueryService = classRoomQueryService;
    }

    /**
     * {@code POST  /class-rooms} : Create a new classRoom.
     *
     * @param classRoom the classRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classRoom, or with status {@code 400 (Bad Request)} if the classRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-rooms")
    public ResponseEntity<ClassRoom> createClassRoom(@Valid @RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to save ClassRoom : {}", classRoom);
        if (classRoom.getId() != null) {
            throw new BadRequestAlertException("A new classRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassRoom result = classRoomService.save(classRoom);
        return ResponseEntity.created(new URI("/api/class-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-rooms} : Updates an existing classRoom.
     *
     * @param classRoom the classRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classRoom,
     * or with status {@code 400 (Bad Request)} if the classRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-rooms")
    public ResponseEntity<ClassRoom> updateClassRoom(@Valid @RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to update ClassRoom : {}", classRoom);
        if (classRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassRoom result = classRoomService.save(classRoom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classRoom.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-rooms} : get all the classRooms.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classRooms in body.
     */
    @GetMapping("/class-rooms")
    public ResponseEntity<List<ClassRoom>> getAllClassRooms(ClassRoomCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassRooms by criteria: {}", criteria);
        Page<ClassRoom> page = classRoomQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /class-rooms/count} : count all the classRooms.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/class-rooms/count")
    public ResponseEntity<Long> countClassRooms(ClassRoomCriteria criteria) {
        log.debug("REST request to count ClassRooms by criteria: {}", criteria);
        return ResponseEntity.ok().body(classRoomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-rooms/:id} : get the "id" classRoom.
     *
     * @param id the id of the classRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-rooms/{id}")
    public ResponseEntity<ClassRoom> getClassRoom(@PathVariable Long id) {
        log.debug("REST request to get ClassRoom : {}", id);
        Optional<ClassRoom> classRoom = classRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classRoom);
    }

    /**
     * {@code DELETE  /class-rooms/:id} : delete the "id" classRoom.
     *
     * @param id the id of the classRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-rooms/{id}")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable Long id) {
        log.debug("REST request to delete ClassRoom : {}", id);
        classRoomService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

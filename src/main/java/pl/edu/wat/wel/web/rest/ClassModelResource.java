package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.domain.ClassModel;
import pl.edu.wat.wel.service.ClassModelService;
import pl.edu.wat.wel.web.rest.errors.BadRequestAlertException;
import pl.edu.wat.wel.service.dto.ClassModelCriteria;
import pl.edu.wat.wel.service.ClassModelQueryService;

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
 * REST controller for managing {@link pl.edu.wat.wel.domain.ClassModel}.
 */
@RestController
@RequestMapping("/api")
public class ClassModelResource {

    private final Logger log = LoggerFactory.getLogger(ClassModelResource.class);

    private static final String ENTITY_NAME = "classModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassModelService classModelService;

    private final ClassModelQueryService classModelQueryService;

    public ClassModelResource(ClassModelService classModelService, ClassModelQueryService classModelQueryService) {
        this.classModelService = classModelService;
        this.classModelQueryService = classModelQueryService;
    }

    /**
     * {@code POST  /class-models} : Create a new classModel.
     *
     * @param classModel the classModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classModel, or with status {@code 400 (Bad Request)} if the classModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-models")
    public ResponseEntity<ClassModel> createClassModel(@Valid @RequestBody ClassModel classModel) throws URISyntaxException {
        log.debug("REST request to save ClassModel : {}", classModel);
        if (classModel.getId() != null) {
            throw new BadRequestAlertException("A new classModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassModel result = classModelService.save(classModel);
        return ResponseEntity.created(new URI("/api/class-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-models} : Updates an existing classModel.
     *
     * @param classModel the classModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classModel,
     * or with status {@code 400 (Bad Request)} if the classModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-models")
    public ResponseEntity<ClassModel> updateClassModel(@Valid @RequestBody ClassModel classModel) throws URISyntaxException {
        log.debug("REST request to update ClassModel : {}", classModel);
        if (classModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassModel result = classModelService.save(classModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-models} : get all the classModels.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classModels in body.
     */
    @GetMapping("/class-models")
    public ResponseEntity<List<ClassModel>> getAllClassModels(ClassModelCriteria criteria) {
        log.debug("REST request to get ClassModels by criteria: {}", criteria);
        List<ClassModel> entityList = classModelQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /class-models/count} : count all the classModels.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/class-models/count")
    public ResponseEntity<Long> countClassModels(ClassModelCriteria criteria) {
        log.debug("REST request to count ClassModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(classModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-models/:id} : get the "id" classModel.
     *
     * @param id the id of the classModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-models/{id}")
    public ResponseEntity<ClassModel> getClassModel(@PathVariable Long id) {
        log.debug("REST request to get ClassModel : {}", id);
        Optional<ClassModel> classModel = classModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classModel);
    }

    /**
     * {@code DELETE  /class-models/:id} : delete the "id" classModel.
     *
     * @param id the id of the classModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-models/{id}")
    public ResponseEntity<Void> deleteClassModel(@PathVariable Long id) {
        log.debug("REST request to delete ClassModel : {}", id);
        classModelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

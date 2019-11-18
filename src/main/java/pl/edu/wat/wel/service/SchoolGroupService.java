package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.SchoolGroup;
import pl.edu.wat.wel.repository.SchoolGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SchoolGroup}.
 */
@Service
@Transactional
public class SchoolGroupService {

    private final Logger log = LoggerFactory.getLogger(SchoolGroupService.class);

    private final SchoolGroupRepository schoolGroupRepository;

    public SchoolGroupService(SchoolGroupRepository schoolGroupRepository) {
        this.schoolGroupRepository = schoolGroupRepository;
    }

    /**
     * Save a schoolGroup.
     *
     * @param schoolGroup the entity to save.
     * @return the persisted entity.
     */
    public SchoolGroup save(SchoolGroup schoolGroup) {
        log.debug("Request to save SchoolGroup : {}", schoolGroup);
        return schoolGroupRepository.save(schoolGroup);
    }

    /**
     * Get all the schoolGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolGroup> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolGroups");
        return schoolGroupRepository.findAll(pageable);
    }


    /**
     * Get one schoolGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolGroup> findOne(Long id) {
        log.debug("Request to get SchoolGroup : {}", id);
        return schoolGroupRepository.findById(id);
    }

    /**
     * Delete the schoolGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolGroup : {}", id);
        schoolGroupRepository.deleteById(id);
    }
}

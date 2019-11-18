package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.Building;
import pl.edu.wat.wel.repository.BuildingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Building}.
 */
@Service
@Transactional
public class BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingService.class);

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    /**
     * Save a building.
     *
     * @param building the entity to save.
     * @return the persisted entity.
     */
    public Building save(Building building) {
        log.debug("Request to save Building : {}", building);
        return buildingRepository.save(building);
    }

    /**
     * Get all the buildings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Building> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable);
    }


    /**
     * Get one building by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Building> findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        return buildingRepository.findById(id);
    }

    /**
     * Delete the building by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.deleteById(id);
    }
}

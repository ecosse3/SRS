package pl.edu.wat.wel.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pl.edu.wat.wel.domain.Building;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.BuildingRepository;
import pl.edu.wat.wel.service.dto.BuildingCriteria;

/**
 * Service for executing complex queries for {@link Building} entities in the database.
 * The main input is a {@link BuildingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Building} or a {@link Page} of {@link Building} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BuildingQueryService extends QueryService<Building> {

    private final Logger log = LoggerFactory.getLogger(BuildingQueryService.class);

    private final BuildingRepository buildingRepository;

    public BuildingQueryService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    /**
     * Return a {@link List} of {@link Building} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Building> findByCriteria(BuildingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Building} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Building> findByCriteria(BuildingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BuildingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingRepository.count(specification);
    }

    /**
     * Function to convert {@link BuildingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Building> createSpecification(BuildingCriteria criteria) {
        Specification<Building> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Building_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Building_.number));
            }
            if (criteria.getClassRoomBId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassRoomBId(),
                    root -> root.join(Building_.classRoomBS, JoinType.LEFT).get(ClassRoom_.id)));
            }
            if (criteria.getReservationBId() != null) {
                specification = specification.and(buildSpecification(criteria.getReservationBId(),
                    root -> root.join(Building_.reservationBS, JoinType.LEFT).get(Reservation_.id)));
            }
        }
        return specification;
    }
}

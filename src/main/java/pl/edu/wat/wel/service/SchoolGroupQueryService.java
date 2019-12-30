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

import pl.edu.wat.wel.domain.SchoolGroup;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.SchoolGroupRepository;
import pl.edu.wat.wel.service.dto.SchoolGroupCriteria;

/**
 * Service for executing complex queries for {@link SchoolGroup} entities in the database.
 * The main input is a {@link SchoolGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolGroup} or a {@link Page} of {@link SchoolGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolGroupQueryService extends QueryService<SchoolGroup> {

    private final Logger log = LoggerFactory.getLogger(SchoolGroupQueryService.class);

    private final SchoolGroupRepository schoolGroupRepository;

    public SchoolGroupQueryService(SchoolGroupRepository schoolGroupRepository) {
        this.schoolGroupRepository = schoolGroupRepository;
    }

    /**
     * Return a {@link List} of {@link SchoolGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolGroup> findByCriteria(SchoolGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SchoolGroup> specification = createSpecification(criteria);
        return schoolGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SchoolGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolGroup> findByCriteria(SchoolGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolGroup> specification = createSpecification(criteria);
        return schoolGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolGroup> specification = createSpecification(criteria);
        return schoolGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolGroup> createSpecification(SchoolGroupCriteria criteria) {
        Specification<SchoolGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SchoolGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SchoolGroup_.name));
            }
            if (criteria.getStarostId() != null) {
                specification = specification.and(buildSpecification(criteria.getStarostId(),
                    root -> root.join(SchoolGroup_.starost, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getReservationSId() != null) {
                specification = specification.and(buildSpecification(criteria.getReservationSId(),
                    root -> root.join(SchoolGroup_.reservationS, JoinType.LEFT).get(Reservation_.id)));
            }
            if (criteria.getMajorId() != null) {
                specification = specification.and(buildSpecification(criteria.getMajorId(),
                    root -> root.join(SchoolGroup_.major, JoinType.LEFT).get(Major_.id)));
            }
            if (criteria.getTimetableId() != null) {
                specification = specification.and(buildSpecification(criteria.getTimetableId(),
                    root -> root.join(SchoolGroup_.timetables, JoinType.LEFT).get(Timetable_.id)));
            }
        }
        return specification;
    }
}

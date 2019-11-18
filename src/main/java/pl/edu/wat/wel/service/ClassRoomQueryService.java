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

import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.ClassRoomRepository;
import pl.edu.wat.wel.service.dto.ClassRoomCriteria;

/**
 * Service for executing complex queries for {@link ClassRoom} entities in the database.
 * The main input is a {@link ClassRoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassRoom} or a {@link Page} of {@link ClassRoom} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassRoomQueryService extends QueryService<ClassRoom> {

    private final Logger log = LoggerFactory.getLogger(ClassRoomQueryService.class);

    private final ClassRoomRepository classRoomRepository;

    public ClassRoomQueryService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    /**
     * Return a {@link List} of {@link ClassRoom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassRoom> findByCriteria(ClassRoomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassRoom> specification = createSpecification(criteria);
        return classRoomRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassRoom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassRoom> findByCriteria(ClassRoomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassRoom> specification = createSpecification(criteria);
        return classRoomRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassRoomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassRoom> specification = createSpecification(criteria);
        return classRoomRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassRoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassRoom> createSpecification(ClassRoomCriteria criteria) {
        Specification<ClassRoom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ClassRoom_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), ClassRoom_.number));
            }
            if (criteria.getMaximumSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximumSize(), ClassRoom_.maximumSize));
            }
            if (criteria.getReservationCId() != null) {
                specification = specification.and(buildSpecification(criteria.getReservationCId(),
                    root -> root.join(ClassRoom_.reservationCS, JoinType.LEFT).get(Reservation_.id)));
            }
            if (criteria.getClassModelId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassModelId(),
                    root -> root.join(ClassRoom_.classModels, JoinType.LEFT).get(ClassModel_.id)));
            }
            if (criteria.getBuildingId() != null) {
                specification = specification.and(buildSpecification(criteria.getBuildingId(),
                    root -> root.join(ClassRoom_.building, JoinType.LEFT).get(Building_.id)));
            }
        }
        return specification;
    }
}

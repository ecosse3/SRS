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

import pl.edu.wat.wel.domain.Timetable;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.TimetableRepository;
import pl.edu.wat.wel.service.dto.TimetableCriteria;

/**
 * Service for executing complex queries for {@link Timetable} entities in the database.
 * The main input is a {@link TimetableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Timetable} or a {@link Page} of {@link Timetable} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimetableQueryService extends QueryService<Timetable> {

    private final Logger log = LoggerFactory.getLogger(TimetableQueryService.class);

    private final TimetableRepository timetableRepository;

    public TimetableQueryService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * Return a {@link List} of {@link Timetable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Timetable> findByCriteria(TimetableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Timetable> specification = createSpecification(criteria);
        return timetableRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Timetable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Timetable> findByCriteria(TimetableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Timetable> specification = createSpecification(criteria);
        return timetableRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimetableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Timetable> specification = createSpecification(criteria);
        return timetableRepository.count(specification);
    }

    /**
     * Function to convert {@link TimetableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Timetable> createSpecification(TimetableCriteria criteria) {
        Specification<Timetable> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Timetable_.id));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Timetable_.subject));
            }
            if (criteria.getClassDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClassDate(), Timetable_.classDate));
            }
            if (criteria.getSchoolGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getSchoolGroupId(),
                    root -> root.join(Timetable_.schoolGroup, JoinType.LEFT).get(SchoolGroup_.id)));
            }
            if (criteria.getBuildingId() != null) {
                specification = specification.and(buildSpecification(criteria.getBuildingId(),
                    root -> root.join(Timetable_.building, JoinType.LEFT).get(Building_.id)));
            }
            if (criteria.getClassRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassRoomId(),
                    root -> root.join(Timetable_.classRoom, JoinType.LEFT).get(ClassRoom_.id)));
            }
            if (criteria.getStartTimeId() != null) {
                specification = specification.and(buildSpecification(criteria.getStartTimeId(),
                    root -> root.join(Timetable_.startTime, JoinType.LEFT).get(ClassHours_.id)));
            }
            if (criteria.getClassDurationId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassDurationId(),
                    root -> root.join(Timetable_.classDuration, JoinType.LEFT).get(ClassDuration_.id)));
            }
        }
        return specification;
    }
}

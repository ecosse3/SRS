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

import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.ReservationRepository;
import pl.edu.wat.wel.service.dto.ReservationCriteria;

/**
 * Service for executing complex queries for {@link Reservation} entities in the database.
 * The main input is a {@link ReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Reservation} or a {@link Page} of {@link Reservation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservationQueryService extends QueryService<Reservation> {

    private final Logger log = LoggerFactory.getLogger(ReservationQueryService.class);

    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Return a {@link List} of {@link Reservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByCriteria(ReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Reservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReservationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.count(specification);
    }

    /**
     * Function to convert {@link ReservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reservation> createSpecification(ReservationCriteria criteria) {
        Specification<Reservation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reservation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Reservation_.name));
            }
            if (criteria.getNoteToTeacher() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoteToTeacher(), Reservation_.noteToTeacher));
            }
            if (criteria.getOriginalClassDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOriginalClassDate(), Reservation_.originalClassDate));
            }
            if (criteria.getNewClassDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNewClassDate(), Reservation_.newClassDate));
            }
            if (criteria.getRequestedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestedBy(), Reservation_.requestedBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Reservation_.createdDate));
            }
            if (criteria.getParticipantsId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantsId(),
                    root -> root.join(Reservation_.participants, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getSchoolGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getSchoolGroupId(),
                    root -> root.join(Reservation_.schoolGroup, JoinType.LEFT).get(SchoolGroup_.id)));
            }
            if (criteria.getBuildingId() != null) {
                specification = specification.and(buildSpecification(criteria.getBuildingId(),
                    root -> root.join(Reservation_.building, JoinType.LEFT).get(Building_.id)));
            }
            if (criteria.getClassRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassRoomId(),
                    root -> root.join(Reservation_.classRoom, JoinType.LEFT).get(ClassRoom_.id)));
            }
            if (criteria.getOriginalStartTimeId() != null) {
                specification = specification.and(buildSpecification(criteria.getOriginalStartTimeId(),
                    root -> root.join(Reservation_.originalStartTime, JoinType.LEFT).get(ClassHours_.id)));
            }
            if (criteria.getNewStartTimeId() != null) {
                specification = specification.and(buildSpecification(criteria.getNewStartTimeId(),
                    root -> root.join(Reservation_.newStartTime, JoinType.LEFT).get(ClassHours_.id)));
            }
            if (criteria.getClassDurationId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassDurationId(),
                    root -> root.join(Reservation_.classDuration, JoinType.LEFT).get(ClassDuration_.id)));
            }
            if (criteria.getStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusId(),
                    root -> root.join(Reservation_.status, JoinType.LEFT).get(Status_.id)));
            }
        }
        return specification;
    }
}

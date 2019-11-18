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

import pl.edu.wat.wel.domain.ClassModel;
import pl.edu.wat.wel.domain.*; // for static metamodels
import pl.edu.wat.wel.repository.ClassModelRepository;
import pl.edu.wat.wel.service.dto.ClassModelCriteria;

/**
 * Service for executing complex queries for {@link ClassModel} entities in the database.
 * The main input is a {@link ClassModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassModel} or a {@link Page} of {@link ClassModel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassModelQueryService extends QueryService<ClassModel> {

    private final Logger log = LoggerFactory.getLogger(ClassModelQueryService.class);

    private final ClassModelRepository classModelRepository;

    public ClassModelQueryService(ClassModelRepository classModelRepository) {
        this.classModelRepository = classModelRepository;
    }

    /**
     * Return a {@link List} of {@link ClassModel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassModel> findByCriteria(ClassModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassModel> specification = createSpecification(criteria);
        return classModelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassModel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassModel> findByCriteria(ClassModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassModel> specification = createSpecification(criteria);
        return classModelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassModel> specification = createSpecification(criteria);
        return classModelRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassModel> createSpecification(ClassModelCriteria criteria) {
        Specification<ClassModel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ClassModel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ClassModel_.name));
            }
            if (criteria.getClassRoomId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassRoomId(),
                    root -> root.join(ClassModel_.classRooms, JoinType.LEFT).get(ClassRoom_.id)));
            }
        }
        return specification;
    }
}

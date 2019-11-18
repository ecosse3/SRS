package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.ClassModel;
import pl.edu.wat.wel.repository.ClassModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassModel}.
 */
@Service
@Transactional
public class ClassModelService {

    private final Logger log = LoggerFactory.getLogger(ClassModelService.class);

    private final ClassModelRepository classModelRepository;

    public ClassModelService(ClassModelRepository classModelRepository) {
        this.classModelRepository = classModelRepository;
    }

    /**
     * Save a classModel.
     *
     * @param classModel the entity to save.
     * @return the persisted entity.
     */
    public ClassModel save(ClassModel classModel) {
        log.debug("Request to save ClassModel : {}", classModel);
        return classModelRepository.save(classModel);
    }

    /**
     * Get all the classModels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClassModel> findAll() {
        log.debug("Request to get all ClassModels");
        return classModelRepository.findAll();
    }


    /**
     * Get one classModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassModel> findOne(Long id) {
        log.debug("Request to get ClassModel : {}", id);
        return classModelRepository.findById(id);
    }

    /**
     * Delete the classModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassModel : {}", id);
        classModelRepository.deleteById(id);
    }
}

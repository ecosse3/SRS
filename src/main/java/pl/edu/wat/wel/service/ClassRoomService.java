package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.repository.ClassRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassRoom}.
 */
@Service
@Transactional
public class ClassRoomService {

    private final Logger log = LoggerFactory.getLogger(ClassRoomService.class);

    private final ClassRoomRepository classRoomRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    /**
     * Save a classRoom.
     *
     * @param classRoom the entity to save.
     * @return the persisted entity.
     */
    public ClassRoom save(ClassRoom classRoom) {
        log.debug("Request to save ClassRoom : {}", classRoom);
        return classRoomRepository.save(classRoom);
    }

    /**
     * Get all the classRooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassRoom> findAll(Pageable pageable) {
        log.debug("Request to get all ClassRooms");
        return classRoomRepository.findAll(pageable);
    }

    /**
     * Get all the classRooms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClassRoom> findAllWithEagerRelationships(Pageable pageable) {
        return classRoomRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one classRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassRoom> findOne(Long id) {
        log.debug("Request to get ClassRoom : {}", id);
        return classRoomRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the classRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassRoom : {}", id);
        classRoomRepository.deleteById(id);
    }
}

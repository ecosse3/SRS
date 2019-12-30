package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.Timetable;
import pl.edu.wat.wel.repository.TimetableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Timetable}.
 */
@Service
@Transactional
public class TimetableService {

    private final Logger log = LoggerFactory.getLogger(TimetableService.class);

    private final TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * Save a timetable.
     *
     * @param timetable the entity to save.
     * @return the persisted entity.
     */
    public Timetable save(Timetable timetable) {
        log.debug("Request to save Timetable : {}", timetable);
        return timetableRepository.save(timetable);
    }

    /**
     * Get all the timetables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Timetable> findAll(Pageable pageable) {
        log.debug("Request to get all Timetables");
        return timetableRepository.findAll(pageable);
    }


    /**
     * Get one timetable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Timetable> findOne(Long id) {
        log.debug("Request to get Timetable : {}", id);
        return timetableRepository.findById(id);
    }

    /**
     * Delete the timetable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Timetable : {}", id);
        timetableRepository.deleteById(id);
    }
}

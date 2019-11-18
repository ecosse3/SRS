package pl.edu.wat.wel.service;

import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    public Reservation save(Reservation reservation) {
        log.debug("Request to save Reservation : {}", reservation);
        return reservationRepository.save(reservation);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        return reservationRepository.findAll(pageable);
    }

    /**
     * Get all the reservations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Reservation> findAllWithEagerRelationships(Pageable pageable) {
        return reservationRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }
}

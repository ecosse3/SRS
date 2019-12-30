package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.Timetable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Timetable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long>, JpaSpecificationExecutor<Timetable> {

}

package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.ClassHours;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClassHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassHoursRepository extends JpaRepository<ClassHours, Long> {

}

package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.ClassDuration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClassDuration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassDurationRepository extends JpaRepository<ClassDuration, Long> {

}

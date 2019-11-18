package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.Major;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Major entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {

}

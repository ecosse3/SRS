package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.SchoolGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SchoolGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolGroupRepository extends JpaRepository<SchoolGroup, Long>, JpaSpecificationExecutor<SchoolGroup> {

}

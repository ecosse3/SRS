package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.Building;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Building entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long>, JpaSpecificationExecutor<Building> {

}

package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.ClassModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClassModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassModelRepository extends JpaRepository<ClassModel, Long>, JpaSpecificationExecutor<ClassModel> {

}

package pl.edu.wat.wel.repository;
import pl.edu.wat.wel.domain.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ClassRoom entity.
 */
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long>, JpaSpecificationExecutor<ClassRoom> {

    @Query(value = "select distinct classRoom from ClassRoom classRoom left join fetch classRoom.classModels",
        countQuery = "select count(distinct classRoom) from ClassRoom classRoom")
    Page<ClassRoom> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct classRoom from ClassRoom classRoom left join fetch classRoom.classModels")
    List<ClassRoom> findAllWithEagerRelationships();

    @Query("select classRoom from ClassRoom classRoom left join fetch classRoom.classModels where classRoom.id =:id")
    Optional<ClassRoom> findOneWithEagerRelationships(@Param("id") Long id);

}

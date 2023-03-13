package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Dorm;

import java.util.List;

public interface DormRepository extends JpaRepository<Dorm, Long> {
    List<Dorm> getDormsByBranch_Id(Long branchId);

}

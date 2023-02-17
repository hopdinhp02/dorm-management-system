package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Dorm;

public interface DormRepository extends JpaRepository<Dorm, Long> {

}

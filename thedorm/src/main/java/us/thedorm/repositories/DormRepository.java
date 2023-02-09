package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.dorm;

public interface DormRepository extends JpaRepository<dorm, Long> {
}

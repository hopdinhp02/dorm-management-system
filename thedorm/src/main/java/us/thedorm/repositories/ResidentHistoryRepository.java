package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.resident_history;

public interface ResidentHistoryRepository extends JpaRepository<resident_history,Long> {
}
